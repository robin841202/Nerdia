package com.example.movieinfo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * OMDB Data Model, using @SerializedName to map to json key
 */
public class OmdbData {

    @SerializedName("imdbID")
    private String imdbId;

    @SerializedName("Title")
    private String title;

    @SerializedName("Type")
    private String type;

    @SerializedName("Ratings")
    private ArrayList<Rating> ratings;

    @SerializedName("tomatoURL")
    private String tomatoWebUrl;

    public String getImdbId() {
        return imdbId;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public String getTomatoWebUrl() {return tomatoWebUrl;}

    public static class Rating {
        @SerializedName("Source")
        private String source;

        @SerializedName("Value")
        private String value;

        public String getSource() {
            return source;
        }

        public String getValue() {
            return value;
        }
    }

}
