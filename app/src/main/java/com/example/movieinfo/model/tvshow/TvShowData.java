package com.example.movieinfo.model.tvshow;

import com.google.gson.annotations.SerializedName;

/**
 * TvShow Data Model, using @SerializedName to map to json key
 */
public class TvShowData {

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("vote_average")
    private double rating;

    @SerializedName("first_air_date")
    private String onAirDate;

    public TvShowData(long id, String title, String overview, String posterPath, String backdropPath, double rating, String onAirDate){
        this.id = id;
        this.title = title;
        this.overview = overview;


        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.rating = rating;
        this.onAirDate = onAirDate;
    }

    /**
     * Get Movie Id
     * @return Movie Id
     */
    public long getId() {
        return id;
    }

    /**
     * Get Movie Title
     * @return Movie Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get Movie Overview
     * @return Movie Overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * Get Movie Poster Url
     * @return Movie Poster Url
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * Get Movie Backdrop Url
     * @return Movie Backdrop Url
     */
    public String getBackdropPath() {
        return backdropPath;
    }

    /**
     * Get Movie Rating
     * @return Movie Rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Set Movie Rating
     * @param rating Movie Rating
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Get Movie onAir Date
     * @return Release Date
     */
    public String getOnAirDate() {
        return onAirDate;
    }
}
