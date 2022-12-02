package com.example.movieinfo.model.movie;

import com.example.movieinfo.model.CreditsResponse;
import com.example.movieinfo.model.ExternalIdResponse;
import com.example.movieinfo.model.GenreData;
import com.example.movieinfo.model.ImagesResponse;
import com.example.movieinfo.model.ProductionCompany;
import com.example.movieinfo.model.VideosResponse;
import com.example.movieinfo.model.user.AccountStatesOnMedia;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Movie Detail Data Model, using @SerializedName to map to json key
 */
public class MovieDetailData {


    @SerializedName("adult")
    private boolean isAdult;

    @SerializedName("budget")
    private int budget;

    @SerializedName("genres")
    private ArrayList<GenreData> genres;

    @SerializedName("production_companies")
    private ArrayList<ProductionCompany> productionCompanies;

    @SerializedName("status")
    private String status;

    @SerializedName("revenue")
    private int revenue;

    @SerializedName("runtime")
    private int runtime;

    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("vote_average")
    private double rating;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("vote_count")
    private int voteCount;


    @SerializedName("videos")
    private VideosResponse videosResponse;

    @SerializedName("images")
    private ImagesResponse imagesResponse;

    @SerializedName("credits")
    private CreditsResponse creditsResponse;

    @SerializedName("external_ids")
    private ExternalIdResponse externalIdResponse;

    @SerializedName("account_states")
    private AccountStatesOnMedia accountStatesOnMedia;

    /**
     * Get IsAdult or not
     *
     * @return
     */
    public boolean getIsAdult() {
        return isAdult;
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
     * Get Budget
     *
     * @return
     */
    public int getBudget() {
        return budget;
    }

    /**
     * Get Genres
     *
     * @return
     */
    public ArrayList<GenreData> getGenres() {
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
     * Get Revenue
     *
     * @return
     */
    public int getRevenue() {
        return revenue;
    }


    /**
     * Get Runtime
     *
     * @return
     */
    public int getRuntime() {
        return runtime;
    }

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
     * Get Release Date
     *
     * @return Release Date
     */
    public String getReleaseDate() {
        return releaseDate;
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
     * Get Videos Response
     *
     * @return
     */
    public VideosResponse getVideosResponse() {
        return videosResponse;
    }

    /**
     * Get Images Response
     *
     * @return
     */
    public ImagesResponse getImagesResponse() {
        return imagesResponse;
    }

    /**
     * Get Credits Response
     *
     * @return
     */
    public CreditsResponse getCreditsResponse() {
        return creditsResponse;
    }

    /**
     * Get ExternalId Response
     *
     * @return
     */
    public ExternalIdResponse getExternalIdResponse() {
        return externalIdResponse;
    }

    /**
     * Get Account States on Media
     *
     * @return
     */
    public AccountStatesOnMedia getAccountStatesOnMedia() {
        return accountStatesOnMedia;
    }
}
