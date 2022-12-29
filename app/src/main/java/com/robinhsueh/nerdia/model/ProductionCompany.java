package com.robinhsueh.nerdia.model;

import com.google.gson.annotations.SerializedName;

/**
 * GenreData Data Model, using @SerializedName to map to json key
 */
public class ProductionCompany {
    @SerializedName("id")
    private int id;

    @SerializedName("logo_path")
    private String logoPath;

    @SerializedName("name")
    private String name;

    @SerializedName("origin_country")
    private String originCountry;
}
