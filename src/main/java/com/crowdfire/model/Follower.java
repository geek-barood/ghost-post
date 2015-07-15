package com.crowdfire.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "insta_follower")
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name="follower_id")
    private String followerId;

    @Column(name="following_id")
    private String followingId;

    protected Follower() {}

    public Follower(String followerId, String followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getFollowingId() {
        return followingId;
    }

    public void setFollowingId(String followingId) {
        this.followingId = followingId;
    }
}
