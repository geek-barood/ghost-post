package com.crowdfire.controller;

import com.crowdfire.dao.FollowsRepository;
import com.crowdfire.dao.UserPostRepository;
import com.crowdfire.dao.UserRepository;
import com.crowdfire.model.FollowerResp;
import com.crowdfire.model.User;
import com.crowdfire.model.UserPost;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by aniruddha@primaseller.com on 14/7/15.
 */

@RestController
@RequestMapping("/api")
public class UserController {

    private static String FOLLOWER_ENDPOINT_URL = "https://api.instagram.com/v1/users/{0}/followed-by?access_token={1}";
    private static String POSTS_ENDPOINT_URL = "https://api.instagram.com/v1/users/{0}/media/recent/?access_token={1}";
    private static String ACCESS_TOKEN = "2048655076.b560c89.8cfea09d85aa427a83c94a4bac8c59f0";
    private static Long demoUserId = 25025320L;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPostRepository userPostRepository;

    @Autowired
    private FollowsRepository followsRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/followers/{userId}")
    public String fetchFollowers(@PathVariable Long userId) {
        FollowerResp resp = new RestTemplate().getForObject(String.format(FOLLOWER_ENDPOINT_URL, demoUserId.toString(), ACCESS_TOKEN), FollowerResp.class);

        for (User follower : resp.getData()) {
            followsRepository.save(follower);
        }

        return "";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/posts/{userId}")
    public String fetchLastPosts(@PathVariable Long userId) {

        try {
            BufferedReader rd  = null;
            StringBuilder sb = null;
            URL url = new URL(String.format(POSTS_ENDPOINT_URL, demoUserId.toString(), ACCESS_TOKEN));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(connection.getInputStream());
            UserPost userPost = new UserPost();
            userPostRepository.save(userPost);
        } catch (Exception e) {

        }
        return "";
    }
}
