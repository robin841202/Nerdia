package com.example.movieinfo.model.user;

import com.google.gson.annotations.SerializedName;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class UserData {

    @SerializedName("avatar")
    private Avatar avatar;

    @SerializedName("id")
    private long id;

    @SerializedName("iso_639_1")
    private String language;

    @SerializedName("iso_3166_1")
    private String country;

    @SerializedName("name")
    private String name;

    @SerializedName("include_adult")
    private boolean includeAdultContent;

    @SerializedName("username")
    private String account;

    public UserData(Avatar avatar, long id, String language, String country, String name, boolean includeAdultContent, String account) {
        this.avatar = avatar;
        this.id = id;
        this.language = language;
        this.country = country;
        this.name = name;
        this.includeAdultContent = includeAdultContent;
        this.account = account;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public long getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public boolean isIncludeAdultContent() {
        return includeAdultContent;
    }

    public String getAccount() {
        return account;
    }

    /**
     * Avatar object
     */
    public static class Avatar {

        @SerializedName("gravatar")
        public Gravatar gravatar;

        /**
         * Gravatar object
         */
        public static class Gravatar {
            @SerializedName("hash")
            public String hash;
        }


    }
}
