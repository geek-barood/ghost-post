package com.crowdfire.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.persistence.metamodel.StaticMetamodel;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "insta_follower", uniqueConstraints = {@UniqueConstraint(columnNames = {"follower_id", "following_id"})})
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name="follower_id")
    private Long followerId;

    @Column(name="following_id")
    private Long followingId;

    protected Follower() {}

    public Follower(Long followerId, Long followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }
}
