package com.example.movieinfo.model.user;

import com.google.gson.annotations.SerializedName;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class BodyWatchlist {

    @SerializedName("media_type")
    private String mediaType;

    @SerializedName("media_id")
    private long mediaId;

    @SerializedName("watchlist")
    private boolean isAdd;

    public BodyWatchlist(String mediaType, long mediaId, boolean isAdd) {
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.isAdd = isAdd;
    }
}
