package com.crowdfire.controller;

import com.crowdfire.dao.FollowerRepository;
import com.crowdfire.dao.UserPostRepository;
import com.crowdfire.dao.UserRepository;
import com.crowdfire.model.*;
import com.crowdfire.service.BestTimeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by aniruddha@primaseller.com on 14/7/15.
 */

@RestController
@RequestMapping("/api")
public class UserController {

    private static String FOLLOWER_ENDPOINT_URL = "https://api.instagram.com/v1/users/{0}/followed-by?access_token={1}";
    private static String POSTS_ENDPOINT_URL = "https://api.instagram.com/v1/users/{0}/media/recent/?access_token={1}";
    //private static String ACCESS_TOKEN = "2048655076.b560c89.8cfea09d85aa427a83c94a4bac8c59f0";
    private static String ACCESS_TOKEN = null;
    private static String redirectUri = "http://localhost:8080/api/auth/instagram/callback";
    private static String CLIENT_ID = "b560c89704c143e7bcac8708b7c01f9c";
    private static String CLIENT_SECRET = "4ef66a95305643d7907ee6893a8f0815";
    private static String AUTH_URL = "https://instagram.com/oauth/authorize/?client_id=" + CLIENT_ID + "&redirect_uri=" + redirectUri + "&response_type=token";
    private static String OAUTH_TOKEN_URL = "https://api.instagram.com/oauth/access_token";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPostRepository userPostRepository;

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    BestTimeService bestTimeService;

    Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    @RequestMapping(method = RequestMethod.GET, value = "/auth/instagram/callback")
    public String fetchAccessToken(@RequestParam(value = "code") String code) {
        logger.info("Got code: " + code);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", "http://localhost:8080/api/auth/instagram/callback");

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity entity = new HttpEntity(params, requestHeaders);
        try {
            OauthResp oauthResp = new RestTemplate().postForObject(UriComponentsBuilder.fromHttpUrl(OAUTH_TOKEN_URL).toUriString(),
                    entity, OauthResp.class);
            ACCESS_TOKEN = oauthResp.getAccessToken();
            logger.debug("access token: " + ACCESS_TOKEN);
        } catch (RestClientException re) {
            logger.error(re.toString());
        }
        return "";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/followers/{userId}")
    public String fetchFollowers(@PathVariable Long userId) {
        String url = MessageFormat.format(FOLLOWER_ENDPOINT_URL, userId.toString(), ACCESS_TOKEN);
        FollowerResp resp = new RestTemplate().getForObject(url, FollowerResp.class);

        for (User follower : resp.getData()) {
            followerRepository.save(new Follower(follower.getId(), String.valueOf(userId)));
        }

        return "";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/posts/{userId}")
    public String fetchLastPosts(@PathVariable Long userId) {
        HttpURLConnection connection = null;
        BufferedReader rd  = null;
        StringBuilder sb = null;
        try {
            URL url = new URL(MessageFormat.format(POSTS_ENDPOINT_URL, userId.toString(), ACCESS_TOKEN));
             connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            ObjectMapper mapper = new ObjectMapper();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = null;
            sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + '\n');
            }
            JsonObject jsonObject = new JsonParser().parse(sb.toString()).getAsJsonObject();
            JsonArray posts = jsonObject.get("data").getAsJsonArray();
            for(JsonElement post : posts) {
                UserPost userPost = new UserPost();
                userPost.setUserId(String.valueOf(userId));
                userPost.setLastPostTimeStamp(new Timestamp(post.getAsJsonObject().get("created_time").getAsLong()*1000L));
                logger.info("Got post: " + userPost);
                userPostRepository.save(userPost);
            }

        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return "";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/make_me_popular")
    public List<String> makeMePopular(@RequestParam(value = "user_id") Long userId,
                                      @RequestParam(value = "num_hours") int num,
                                      @RequestParam(value = "day") int day) {

        // Ideally this should run as a scheduled service
        // But here I am fetching followers every time the user queries
        fetchFollowers(userId);
        for (Follower follower : followerRepository.findAll()) {
            // This should also ideally be run as a scheduled service
            fetchLastPosts(Long.valueOf(follower.getFollowerId()));
        }

        List<String> bestTimes = new ArrayList<>();
        try {
            for (Integer hour : bestTimeService.findBestTimesForUser(userId, num, day)) {
                bestTimes.add(new DateTime().withHourOfDay(hour).withMinuteOfHour(0).toString("hh:mm a"));
            }
        } catch (Exception e) {
            return Arrays.asList(new String[]{e.getMessage()});
        }
        return bestTimes;
    }
}
