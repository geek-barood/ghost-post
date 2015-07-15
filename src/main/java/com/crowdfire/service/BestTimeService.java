package com.crowdfire.service;

import com.crowdfire.dao.FollowsRepository;
import com.crowdfire.dao.UserPostRepository;
import com.crowdfire.dao.UserRepository;
import com.crowdfire.model.User;
import com.crowdfire.model.UserPost;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by aniruddha@primaseller.com on 14/7/15.
 */
public class BestTimeService {

    @Autowired
    UserPostRepository userPostRepository;

    @Autowired
    FollowsRepository followsRepository;

    @Autowired
    UserRepository userRepository;

    public class Pair implements Comparable<Pair> {
        int hour;
        int count;

        public Pair() {
            this.hour = 0;
            this.count = 0;
        }

        @Override
        public int compareTo(Pair o) {
            return this.count - o.count;
        }
    }

    List<Integer> findBestTimesForUser(Long userId, int n) {
        Pair[] bestTimes = new Pair[24];

        List<User> followers = followsRepository.findByFollows(userId);
        for (User follower : followers) {
            List<UserPost> posts = userPostRepository.findByUserId(Long.valueOf(follower.getId()));
            for (UserPost post : posts) {
                DateTime timeStamp = new DateTime(post.getLastPostTimeStamp().getTime());
                bestTimes[timeStamp.getHourOfDay()].count++;
                bestTimes[timeStamp.getHourOfDay()].hour = timeStamp.getHourOfDay();
            }
        }

        Arrays.sort(bestTimes);
        List<Integer> nBestTimes = new ArrayList<>();

        for (int i=0; i < n; ++i) {
            nBestTimes.add(bestTimes[23-i].hour);

        }
        return nBestTimes;
    }
}
