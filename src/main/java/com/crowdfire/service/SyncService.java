package com.crowdfire.service;

import com.crowdfire.dao.FollowerRepository;
import com.crowdfire.dao.UserPostRepository;
import com.crowdfire.dao.UserPostSyncTimesRepository;
import com.crowdfire.dao.UserRepository;
import com.crowdfire.model.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.Future;

/**
 * Created by aniruddha on 18/7/15.
 */

@Service
public class SyncService {

    private static String POSTS_ENDPOINT_URL = "https://api.instagram.com/v1/users/{0}/media/recent/?access_token={1}&min_timestamp={2}";

    private static String FOLLOWER_ENDPOINT_URL = "https://api.instagram.com/v1/users/{0}/followed-by?access_token={1}";

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserPostRepository userPostRepository;

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private UserPostSyncTimesRepository userPostSyncTimesRepository;

    @Async
    public Future<FollowerResp> syncFollowers(String accessToken, Long userId) {

        try {
            String url = MessageFormat.format(FOLLOWER_ENDPOINT_URL, userId.toString(), accessToken);
            FollowerResp resp = new RestTemplate().getForObject(url, FollowerResp.class);

            for (User follower : resp.getData()) {
                // If the current follower of userId is not present in database then only save it.
                Follower f = followerRepository.findByFollowerIdAndFollowingId(follower.getId(), userId);
                if (f == null) {
                    f = followerRepository.save(new Follower(follower.getId(), userId));
                    logger.debug("saving follower: " + f);
                } else {
                    logger.debug("follower: " + f + " already exists in db");
                }
            }

            return new AsyncResult<>(resp);
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return new AsyncResult<>(new FollowerResp());
        }
    }

    @Async
    public Future<UserPostResp> syncPosts(String accessToken, Long userId) {

        try {
            // If posts for userId has not been synced before then fetch all its post from start
            long fromTimestamp = 0;

            // else fetch after last sync time
            UserPostSyncTimes lastSync = userPostSyncTimesRepository.findByUserId(userId);
            if (lastSync != null) {
                fromTimestamp = lastSync.getLastSyncTimestamp() + 1;
                lastSync.setLastSyncTimestamp(fromTimestamp);
                userPostSyncTimesRepository.save(lastSync);
            } else {
                userPostSyncTimesRepository.save(new UserPostSyncTimes(userId, new DateTime().getMillis()/1000L));
            }
            URI uri = new URI(MessageFormat.format(POSTS_ENDPOINT_URL, userId.toString(), accessToken, String.valueOf(fromTimestamp)));
            logger.debug("Fetching posts after timestamp: " + fromTimestamp);
            logger.debug("URL: " + uri.getQuery());
            UserPostResp postList = new RestTemplate().getForObject(uri, UserPostResp.class);

            for (UserPost post : postList.getUserPosts()) {
                post.setUserId(userId);
                logger.debug("saving post: " + post + " to db");
                userPostRepository.save(post);
            }

            return new AsyncResult<>(postList);
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return new AsyncResult<>(new UserPostResp());
        }
    }
}
