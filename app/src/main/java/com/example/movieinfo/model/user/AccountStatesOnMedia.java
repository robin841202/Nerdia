package com.example.movieinfo.model.user;

import com.google.gson.annotations.SerializedName;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class AccountStatesOnMedia {

    @SerializedName("favorite")
    private boolean isFavourite;

    @SerializedName("watchlist")
    private boolean isInWatchlist;

    /**
     * Boolean false or Rated object
     */
    @SerializedName("rated")
    private Object rated;

    public boolean isFavourite() {
        return isFavourite;
    }

    public boolean isInWatchlist() {
        return isInWatchlist;
    }

    public Object getRated() {
        return rated;
    }

    /**
     * Avatar object
     */
    private static class Rated {
        @SerializedName("value")
        public double score;
    }
}
