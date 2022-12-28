package com.robinhsueh.movieinfo.model;

import com.google.common.collect.Collections2;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

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
     * Filter Main Crew
     *
     * @param target target list
     * @return List of CrewData
     */
    public ArrayList<CrewData> filterMainCrew(ArrayList<CrewData> target) {
        String mainCrewJobRegex = "^Director$|^Producer$|^Writer$";
        return new ArrayList<>(Collections2.filter(target, input -> {
            Pattern pattern = Pattern.compile(mainCrewJobRegex);
            return pattern.matcher(input.getJob()).find();
        }));
    }

    /**
     * Sort Crew By Job
     */
    public ArrayList<CrewData> sortCrewByJob(ArrayList<CrewData> target) {
        Collections.sort(target, (o1, o2) -> o1.getJob().compareTo(o2.getJob()));
        return target;
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

