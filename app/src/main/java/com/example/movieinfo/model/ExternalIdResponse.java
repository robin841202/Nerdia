package com.example.movieinfo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class ExternalIdResponse {

    @SerializedName("imdb_id")
    private final String imdbId;

    @SerializedName("facebook_id")
    private final String facebookId;

    @SerializedName("instagram_id")
    private final String instagramId;

    @SerializedName("twitter_id")
    private final String twitterId;

    public ExternalIdResponse(String imdbId, String facebookId, String instagramId, String twitterId) {
        this.imdbId = imdbId;
        this.facebookId = facebookId;
        this.instagramId = instagramId;
        this.twitterId = twitterId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getInstagramId() {
        return instagramId;
    }

    public String getTwitterId() {
        return twitterId;
    }
}

