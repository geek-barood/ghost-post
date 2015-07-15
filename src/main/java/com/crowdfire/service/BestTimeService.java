package com.crowdfire.service;

import com.crowdfire.dao.FollowerRepository;
import com.crowdfire.dao.UserPostRepository;
import com.crowdfire.dao.UserRepository;
import com.crowdfire.model.Follower;
import com.crowdfire.model.User;
import com.crowdfire.model.UserPost;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by aniruddha@primaseller.com on 14/7/15.
 */

@Component
public class BestTimeService {

    @Autowired
    UserPostRepository userPostRepository;

    @Autowired
    FollowerRepository followerRepository;

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

    public List<Integer> findBestTimesForUser(Long userId, int n) {
        Pair[] bestTimes = new Pair[24];
        for (int i=0; i<24; ++i) bestTimes[i] = new Pair();

        List<Follower> followers = followerRepository.findByFollowingId(String.valueOf(userId));
        for (Follower follower : followers) {
            List<UserPost> posts = userPostRepository.findByUserId(follower.getFollowerId());
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
