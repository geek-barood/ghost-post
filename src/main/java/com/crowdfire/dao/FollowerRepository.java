package com.crowdfire.dao;

import com.crowdfire.model.Follower;
import com.crowdfire.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by aniruddha@primaseller.com on 14/7/15.
 */
public interface FollowerRepository extends CrudRepository<Follower, Long> {

    List<Follower> findByFollowerId(String follwerId);
    List<Follower> findByFollowingId(String followingId);
}
