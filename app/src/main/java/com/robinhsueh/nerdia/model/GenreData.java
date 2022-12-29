package com.robinhsueh.nerdia.model;

import com.google.gson.annotations.SerializedName;

/**
 * GenreData Data Model, using @SerializedName to map to json key
 */
public class GenreData {
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
