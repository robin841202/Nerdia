package com.example.movieinfo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class CreditsResponse {

    @SerializedName("cast")
    private ArrayList<CastData> cast_list;

    @SerializedName("crew")
    private ArrayList<CrewData> crew_list;

    public CreditsResponse(ArrayList<CastData> cast_list, ArrayList<CrewData> crew_list) {
        this.cast_list = cast_list;
        this.crew_list = crew_list;
    }

    public ArrayList<CastData> getCast_list() {
        return cast_list;
    }

    public ArrayList<CrewData> getCrew_list() {
        return crew_list;
    }

    /**
     * Individual Cast Data
     */
    public static class CastData {

        /**
         * Order
         */
        @SerializedName("order")
        private int order;

        /**
         * Id
         */
        @SerializedName("id")
        private long id;

        /**
         * Cast Id
         */
        @SerializedName("cast_id")
        private int castId;

        /**
         * Credit Id
         */
        @SerializedName("credit_id")
        private String creditId;

        /**
         * Cast Name
         */
        @SerializedName("name")
        private String name;

        /**
         * Profile image path
         */
        @SerializedName("profile_path")
        private String profile_path;

        /**
         * Character Name
         */
        @SerializedName("character")
        private String characterName;

        public int getOrder() {
            return order;
        }

        public long getId() {
            return id;
        }

        public int getCastId() {
            return castId;
        }

        public String getCreditId() {
            return creditId;
        }

        public String getName() {
            return name;
        }

        public String getProfile_path() {
            return profile_path;
        }

        public String getCharacterName() {
            return characterName;
        }

    }


    /**
     * Individual Crew Data
     */
    public static class CrewData {

        /**
         * Id
         */
        @SerializedName("id")
        private int id;

        /**
         * Credit Id
         */
        @SerializedName("credit_id")
        private String creditId;

        /**
         * Job (Casting, Executive Producer, etc.)
         */
        @SerializedName("job")
        private String job;

        /**
         * Cast Name
         */
        @SerializedName("name")
        private String name;

        /**
         * Profile image path
         */
        @SerializedName("profile_path")
        private String profile_path;

        public int getId() {
            return id;
        }

        public String getCreditId() {
            return creditId;
        }

        public String getJob() {
            return job;
        }

        public String getName() {
            return name;
        }

        public String getProfile_path() {
            return profile_path;
        }
    }
}

