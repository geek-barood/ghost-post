package com.crowdfire.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Follower {

    Long follwer, following;

    public Follower() {

    }

    public Follower(String id, String userName) {
        super(id, userName);
    }

    public Follower(String id, String userName, String profilePicture, String fullName) {
        super(id, userName, profilePicture, fullName);
    }
}
