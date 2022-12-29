package com.robinhsueh.nerdia.model.person;

import com.google.gson.annotations.SerializedName;

/**
 * Person Data Model, using @SerializedName to map to json key
 */
public class PersonData {

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("profile_path")
    private String profilePath;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }
}
