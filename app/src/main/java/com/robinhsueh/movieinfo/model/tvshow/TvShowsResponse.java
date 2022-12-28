package com.robinhsueh.movieinfo.model.tvshow;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class TvShowsResponse {
    @SerializedName("page")
    public int page;

    @SerializedName("results")
    public ArrayList<TvShowData> tvShow_list = new ArrayList<TvShowData>();

    @SerializedName("total_pages")
    public int total_pages;
}
