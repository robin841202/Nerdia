package com.example.movieinfo.model.user;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class AccountStatesOnMedia {

    @SerializedName("favorite")
    private boolean isFavourite;

    @SerializedName("watchlist")
    private boolean isInWatchlist;

    private double score;

    public boolean isFavourite() {
        return isFavourite;
    }

    public boolean isInWatchlist() {
        return isInWatchlist;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Boolean false or Rated object
     */
    @SerializedName("rated")
    private Object rated;

    public Object getRated() {
        return rated;
    }

    /**
     * Rated object
     */
    public static class Rated {
        @SerializedName("value")
        public double score;
    }
}
