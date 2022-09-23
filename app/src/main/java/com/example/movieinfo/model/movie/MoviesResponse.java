package com.example.movieinfo.model.movie;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class MoviesResponse {
    @SerializedName("page")
    public int page;

    @SerializedName("results")
    public ArrayList<MovieData> movie_list;

    @SerializedName("total_pages")
    public int total_pages;
}
