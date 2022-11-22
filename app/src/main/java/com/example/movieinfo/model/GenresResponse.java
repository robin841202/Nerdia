package com.example.movieinfo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class GenresResponse {
    @SerializedName("genres")
    public ArrayList<GenreData> genres;
}
