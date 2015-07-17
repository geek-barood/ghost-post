package com.crowdfire.service;

import com.crowdfire.dao.FollowerRepository;
import com.crowdfire.dao.UserPostRepository;
import com.crowdfire.dao.UserRepository;
import com.crowdfire.model.Follower;
import com.crowdfire.model.User;
import com.crowdfire.model.UserPost;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
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

    public List<Integer> findBestTimesForUser(Long userId, int n, int day) throws Exception {

        //Monday, the first day of the week (ISO) represents day 0
        //Sunday, the last day of the week (ISO) represents day 6

        if (day < 0 || day > 6) {
            throw new Exception("Day should be within [0,6]");
        }

        Pair[][] bestTimes = new Pair[7][24];
        for (int i=0; i<7; ++i) {
            for (int j=0; j<24; ++j) {
                bestTimes[i][j] = new Pair();
            }
        }

        // This stores for each day for every hour the number of posts from the followers of user
        List<Follower> followers = followerRepository.findByFollowingId(String.valueOf(userId));
        for (Follower follower : followers) {
            List<UserPost> posts = userPostRepository.findByUserId(follower.getFollowerId());
            for (UserPost post : posts) {
                DateTime timeStamp = new DateTime(post.getLastPostTimeStamp().getTime());
                bestTimes[timeStamp.getDayOfWeek()-1][timeStamp.getHourOfDay()].count++;
                bestTimes[timeStamp.getDayOfWeek()-1][timeStamp.getHourOfDay()].hour = timeStamp.getHourOfDay();
            }
        }

        // Sort the best times for each day
        for (int i=0; i<7; ++i) {
            Arrays.sort(bestTimes[i]);
        }

        List<Integer> nBestTimes = new ArrayList<>();
        for (int i=0; i < n; ++i) {
            nBestTimes.add(bestTimes[day][23-i].hour);

        }

        return nBestTimes;
    }
}
