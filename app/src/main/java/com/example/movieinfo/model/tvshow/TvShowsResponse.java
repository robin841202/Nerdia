package com.example.movieinfo.model.tvshow;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class TvShowsResponse {
    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private ArrayList<TvShowData> tvShow_list = new ArrayList<TvShowData>();

    @SerializedName("total_pages")
    private int total_pages;
}
