package com.crowdfire.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by aniruddha on 18/7/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OauthResp {

    @JsonProperty("access_token")
    private String accessToken;


    @JsonProperty("user")
    private User user;

    @JsonProperty("error_message")
    private String errorMessage;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
