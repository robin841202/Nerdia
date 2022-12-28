package com.robinhsueh.movieinfo.model.person;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class PeopleResponse {
    @SerializedName("page")
    public int page;

    @SerializedName("results")
    public ArrayList<PersonData> people_list = new ArrayList<>();

    @SerializedName("total_pages")
    public int total_pages;
}
