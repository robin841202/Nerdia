package com.example.movieinfo.model;

import com.example.movieinfo.model.movie.MovieData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class VideosResponse {

    @SerializedName("results")
    public ArrayList<VideoData> video_list;

    public static class VideoData{

        /**
         * Language Code according in iso 639-1 ,ex: en,zh-TW
         */
        @SerializedName("iso_639_1")
        private String languageCode;

        /**
         * Country Code according in iso 639-1 ,ex: TW,US
         */
        @SerializedName("iso_3166_1")
        private String countryCode;

        /**
         * Video Title
         */
        @SerializedName("name")
        private String name;

        /**
         * Video Id in their Source Site
         */
        @SerializedName("key")
        private String videoId;

        /**
         * Video Source Site, ex: Youtube
         */
        @SerializedName("site")
        private String sourceSite;

        /**
         * Video Resolution, ex: 1080
         */
        @SerializedName("size")
        private int resolution;

        /**
         * Video Type, ex: Behind the Scenes, Teaser
         */
        @SerializedName("type")
        private String videoType;

        /**
         * Video is official or not
         */
        @SerializedName("official")
        private boolean isOfficial;


        public String getLanguageCode() {
            return languageCode;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public String getName() {
            return name;
        }

        public String getVideoId() {
            return videoId;
        }

        public String getSourceSite() {
            return sourceSite;
        }

        public int getResolution() {
            return resolution;
        }

        public String getVideoType() {
            return videoType;
        }

        public boolean isOfficial() {
            return isOfficial;
        }
    }
}

