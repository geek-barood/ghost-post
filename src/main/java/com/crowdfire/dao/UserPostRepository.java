package com.crowdfire.dao;

import com.crowdfire.model.UserPost;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by aniruddha@primaseller.com on 14/7/15.
 */
public interface UserPostRepository extends CrudRepository<UserPost, Long> {

    List<UserPost> findByUserId(Long userId);

}
