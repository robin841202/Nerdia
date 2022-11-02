package com.example.movieinfo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

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
     * Sort Videos, Taiwan(zh) videos always sort in first, then sort Trailer, Teaser on top
     */
    public void sortVideos(){
        Collections.sort(video_list, getCustomVideoComparator());
    }

    /**
     * (Private) Get Video Custom Comparator
     * @return Custom Comparator
     */
    private Comparator<VideoData> getCustomVideoComparator(){
        String tch_ISO = Locale.TRADITIONAL_CHINESE.getLanguage();
        return new Comparator<VideoData>() {
            @Override
            public int compare(VideoData o1, VideoData o2) {
                String lc1 = o1.getLanguageCode();
                String lc2 = o2.getLanguageCode();
                String vt1 = o1.getVideoType();
                String vt2 = o2.getVideoType();

                if (lc1.equalsIgnoreCase(tch_ISO) && lc2.equalsIgnoreCase(tch_ISO)) {
                    // region Assure VideoType TRAILER will always come first, TEASER comes second
                    if (vt1.equalsIgnoreCase(StaticParameter.VideoType.TRAILER))
                        return -1;
                    if (vt2.equalsIgnoreCase(StaticParameter.VideoType.TRAILER))
                        return 1;
                    if (vt1.equalsIgnoreCase(StaticParameter.VideoType.TEASER))
                        return -1;
                    if (vt2.equalsIgnoreCase(StaticParameter.VideoType.TEASER))
                        return 1;
                    return vt1.compareTo(vt2);
                    // endregion
                }

                // region Assure specific language (zh) of video will always be top
                if (lc1.equalsIgnoreCase(tch_ISO))
                    return -1;
                if (lc2.equalsIgnoreCase(tch_ISO))
                    return 1;
                // endregion

                // region Assure VideoType TRAILER will always come first, TEASER comes second
                if (vt1.equalsIgnoreCase(StaticParameter.VideoType.TRAILER))
                    return -1;
                if (vt2.equalsIgnoreCase(StaticParameter.VideoType.TRAILER))
                    return 1;
                if (vt1.equalsIgnoreCase(StaticParameter.VideoType.TEASER))
                    return -1;
                if (vt2.equalsIgnoreCase(StaticParameter.VideoType.TEASER))
                    return 1;
                return vt1.compareTo(vt2);
                // endregion

            }
        };
    }

    /**
     * Individual Video Data
     */
    public static class VideoData{

        /**
         * Language Code according in iso 639-1 ,ex: en,zh
         */
        @SerializedName("iso_639_1")
        private String languageCode;

        /**
         * Country Code according in iso 3166-1 ,ex: TW,US
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

