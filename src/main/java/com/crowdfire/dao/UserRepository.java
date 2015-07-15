package com.crowdfire.dao;

import com.crowdfire.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by aniruddha@primaseller.com on 14/7/15.
 */
public interface UserRepository extends CrudRepository<User, Long> {

}
