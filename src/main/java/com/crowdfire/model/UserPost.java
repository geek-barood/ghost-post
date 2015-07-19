package com.crowdfire.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by aniruddha@primaseller.com on 14/7/15.
 */

@Entity
@Table(name="user_post", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "post_id"})})
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPost implements Serializable {

    @Id
    @Column(name = "post_id")
    @JsonProperty("id")
    private String id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_timestamp")
    @JsonProperty("created_time")
    private Long createdTimestamp;

    @Column(name = "type")
    @JsonProperty("type")
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
