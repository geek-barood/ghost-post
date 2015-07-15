package com.crowdfire.model;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by aniruddha@primaseller.com on 14/7/15.
 */

@Entity
@Table(name="user_post")
public class UserPost implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userId;

    private Timestamp lastPostTimeStamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getLastPostTimeStamp() {
        return lastPostTimeStamp;
    }

    public void setLastPostTimeStamp(Timestamp lastPostTimeStamp) {
        this.lastPostTimeStamp = lastPostTimeStamp;
    }
}
