package com.crowdfire.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.List;

/**
 * Created by aniruddha on 19/7/15.
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPostResp {

    @JsonProperty("data")
    List<UserPost> userPosts;

    public List<UserPost> getUserPosts() {
        return userPosts;
    }

    public void setUserPosts(List<UserPost> userPosts) {
        this.userPosts = userPosts;
    }
}
