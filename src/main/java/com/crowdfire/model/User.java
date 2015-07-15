package com.crowdfire.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by aniruddha@primaseller.com on 14/7/15.
 */

@Entity
@Table(name = "insta_user")
public class User implements Serializable {

    @JsonProperty("username")
    private String userName;

    @JsonProperty("profile_picture")
    private String profilePicture;

    @JsonProperty("id")
    @Id
    private String id;

    @JsonProperty("full_name")
    private String fullName;

    protected User() {}

    public User(String id, String userName) {
        this.userName = userName;
        this.id = id;
    }

    public User(String id, String userName, String profilePicture, String fullName) {
        this.id = id;
        this.userName = userName;
        this.profilePicture = profilePicture;
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
