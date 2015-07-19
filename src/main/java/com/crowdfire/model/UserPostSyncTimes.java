package com.crowdfire.model;

import javax.persistence.*;

/**
 * Created by aniruddha on 19/7/15.
 */

@Entity
@Table(name="user_post_sycn_times", uniqueConstraints =  {@UniqueConstraint(columnNames = {"user_id", "last_sync_timestamp"})})
public class UserPostSyncTimes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "last_sync_timestamp")
    private Long lastSyncTimestamp;

    protected UserPostSyncTimes() {}

    public UserPostSyncTimes(Long userId, Long lastSyncTimestamp) {
        this.userId = userId;
        this.lastSyncTimestamp = lastSyncTimestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLastSyncTimestamp() {
        return lastSyncTimestamp;
    }

    public void setLastSyncTimestamp(Long lastSyncTimestamp) {
        this.lastSyncTimestamp = lastSyncTimestamp;
    }
}
