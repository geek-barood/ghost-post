package com.crowdfire.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by aniruddha@primaseller.com on 14/7/15.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowerResp {

    @JsonProperty("pagination")
    private Map<String, String> pagination;

    @JsonProperty("meta")
    private Map<String, Integer> meta;

    @JsonProperty("data")
    private List<User> data = new LinkedList<>();

    public Map<String, String> getPagination() {
        return pagination;
    }

    public void setPagination(Map<String, String> pagination) {
        this.pagination = pagination;
    }

    public Map<String, Integer> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Integer> meta) {
        this.meta = meta;
    }

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }
}
