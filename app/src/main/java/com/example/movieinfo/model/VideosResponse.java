package com.example.movieinfo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class VideosResponse {

    @SerializedName("results")
    private ArrayList<VideoData> video_list;

    public VideosResponse(ArrayList<VideoData> video_list) {
        this.video_list = video_list;
    }

    public ArrayList<VideoData> getVideo_list() {
        return video_list;
    }

    /**
     * Sort Videos By Language, Taiwan(zh) videos always sort in first
     */
    public void sortVideosByLanguage(){
        Collections.sort(video_list, new Comparator<VideoData>() {
            @Override
            public int compare(VideoData o1, VideoData o2) {
                String lc1 = o1.getLanguageCode();
                String lc2 = o2.getLanguageCode();
                if (lc1 != null && lc1.equalsIgnoreCase("zh"))
                    return -1;
                if (lc2 != null && lc2.equalsIgnoreCase("zh"))
                    return 1;

                if (lc1 != null && lc2 != null)
                    return lc1.compareTo(lc2);

                return 0;
            }
        });
    }

    /**
     * Individual Video Data
     */
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

