package com.robinhsueh.movieinfo.model.tvshow;

import com.google.gson.annotations.SerializedName;

/**
 * TvShow Season Data Model, using @SerializedName to map to json key
 */
public class SeasonData {

    @SerializedName("air_date")
    private String onAirDate;

    @SerializedName("episode_count")
    private int episodes;

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("season_number")
    private int seasonNum;


    public String getOnAirDate() {
        return onAirDate;
    }

    public int getEpisodes() {
        return episodes;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public int getSeasonNum() {
        return seasonNum;
    }
}
