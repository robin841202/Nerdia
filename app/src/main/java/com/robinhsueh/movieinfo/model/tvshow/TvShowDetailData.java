package com.robinhsueh.movieinfo.model.tvshow;

import com.robinhsueh.movieinfo.model.CreditsResponse;
import com.robinhsueh.movieinfo.model.ExternalIdResponse;
import com.robinhsueh.movieinfo.model.GenreData;
import com.robinhsueh.movieinfo.model.ImagesResponse;
import com.robinhsueh.movieinfo.model.ProductionCompany;
import com.robinhsueh.movieinfo.model.VideosResponse;
import com.robinhsueh.movieinfo.model.user.AccountStatesOnMedia;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * TvShow Detail Data Model, using @SerializedName to map to json key
 */
public class TvShowDetailData {

    @SerializedName("number_of_episodes")
    private int numOfEpisodes;

    @SerializedName("last_air_date")
    private String lastAirDate;

    @SerializedName("adult")
    private boolean isAdult;

    @SerializedName("genres")
    private ArrayList<GenreData> genres;

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

    @SerializedName("seasons")
    private ArrayList<SeasonData> seasons;


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
    public boolean getIsAdult() {
        return isAdult;
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
     * Get Status
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * Get Seasons
     * @return
     */
    public ArrayList<SeasonData> getSeasons() {
        return seasons;
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
