package com.robinhsueh.nerdia.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class WatchProvidersResponse {

    @SerializedName("results")
    public Regions regions;

    /**
     * Regions
     */
    public static class Regions {

        /**
         * America Region
         */
        @SerializedName("US")
        public WatchProviderGroup america;

        /**
         * Taiwan Region
         */
        @SerializedName("TW")
        public WatchProviderGroup taiwan;

    }

    /**
     * WatchProvider Group
     */
    public static class WatchProviderGroup {

        /**
         * Link to TMDB watch provider web page
         */
        @SerializedName("link")
        public String tmdbLink;

        /**
         * "Flatrate" type watch providers. ex: Netflix, Disney+ ...etc
         */
        @SerializedName("flatrate")
        private ArrayList<WatchProviderData> subscriptions;

        /**
         * "Rent" type watch providers. ex: Google Play Movies ...etc
         */
        @SerializedName("rent")
        private ArrayList<WatchProviderData> rents;

        /**
         * "Buy" type watch providers. ex: Microsoft Store, Google Play Movies ...etc
         */
        @SerializedName("buy")
        private ArrayList<WatchProviderData> buys;

        public ArrayList<WatchProviderData> getSubscriptions() {
            return subscriptions == null ? new ArrayList<>() : subscriptions;
        }

        public ArrayList<WatchProviderData> getRents() {
            return rents == null ? new ArrayList<>() : rents;
        }

        public ArrayList<WatchProviderData> getBuys() {
            return buys == null ? new ArrayList<>() : buys;
        }
    }

    /**
     * Individual Watch Provider Data
     */
    public static class WatchProviderData {

        /**
         * Provider id
         */
        @SerializedName("provider_id")
        public long id;

        /**
         * Provider name
         */
        @SerializedName("provider_name")
        public String name;

        /**
         * Provider logo path
         */
        @SerializedName("logo_path")
        public String logoPath;

    }
}
