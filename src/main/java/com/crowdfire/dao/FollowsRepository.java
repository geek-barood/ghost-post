package com.crowdfire.dao;

import com.crowdfire.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by aniruddha@primaseller.com on 14/7/15.
 */
public interface FollowsRepository extends CrudRepository<User, Long> {

    // Returns a list of users who follow user with userId
    List<User> findByUser(Long userId);
}
