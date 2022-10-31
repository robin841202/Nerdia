package com.example.movieinfo.model.tvshow;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.movieinfo.model.Genre;
import com.example.movieinfo.model.ProductionCompany;
import com.example.movieinfo.model.VideosResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * TvShow Detail Data Model, using @SerializedName to map to json key
 */
public class TvShowDetailData{

    @SerializedName("number_of_episodes")
    private int numOfEpisodes;

    @SerializedName("last_air_date")
    private String lastAirDate;

    @SerializedName("adult")
    private boolean isAdult;

    @SerializedName("genres")
    private ArrayList<Genre> genres;

    @SerializedName("production_companies")
    private ArrayList<ProductionCompany> productionCompanies;

    @SerializedName("status")
    private String status;

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

    @SerializedName("vote_count")
    private int voteCount;


    @SerializedName("videos")
    private VideosResponse videosResponse;


    /**
     * Get Id
     *
     * @return Movie Id
     */
    public long getId() {
        return id;
    }

    /**
     * Get Title
     *
     * @return Movie Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get Overview
     *
     * @return Movie Overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * Get Poster Url
     *
     * @return Movie Poster Url
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * Get Backdrop Url
     *
     * @return Movie Backdrop Url
     */
    public String getBackdropPath() {
        return backdropPath;
    }

    /**
     * Get Rating
     *
     * @return Movie Rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Set Rating
     *
     * @param rating Movie Rating
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Get onAir Date
     *
     * @return Release Date
     */
    public String getOnAirDate() {
        return onAirDate;
    }

    /**
     * Get Vote Count
     *
     * @return
     */
    public int getVoteCount() {
        return voteCount;
    }

    /**
     * Get NumOfEpisodes
     *
     * @return
     */
    public int getNumOfEpisodes() {
        return numOfEpisodes;
    }


    /**
     * Get Last-air Date
     *
     * @return
     */
    public String getLastAirDate() {
        return lastAirDate;
    }

    /**
     * Get IsAdult or not
     *
     * @return
     */
    public boolean isAdult() {
        return isAdult;
    }

    /**
     * Get Genres
     *
     * @return
     */
    public ArrayList<Genre> getGenres() {
        return genres;
    }

    /**
     * Get Production Companies
     *
     * @return
     */
    public ArrayList<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    /**
     * Get Status
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * Get Videos Response
     * @return
     */
    public VideosResponse getVideosResponse() {return videosResponse;}
}
