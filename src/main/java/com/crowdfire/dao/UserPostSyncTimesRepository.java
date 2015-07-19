package com.crowdfire.dao;

import com.crowdfire.model.UserPostSyncTimes;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by aniruddha on 19/7/15.
 */
public interface UserPostSyncTimesRepository extends CrudRepository<UserPostSyncTimes, Long> {

    UserPostSyncTimes findByUserId(Long userId);
}
