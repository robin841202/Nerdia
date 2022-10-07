package com.example.movieinfo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Genre Data Model, using @SerializedName to map to json key
 */
public class Genre {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
