package com.example.movieinfo.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    public ArrayList<VideoData> sortVideos(ArrayList<VideoData> target) {
        Collections.sort(target, getCustomVideoComparator());
        return target;
    }

    /**
     * Filter Videos By SourceSite
     *
     * @param sourceSite ex: YouTube
     * @return List of VideoData
     */
    public ArrayList<VideoData> getVideosBySourceSite(ArrayList<VideoData> target, String sourceSite) {
        return new ArrayList<VideoData>(Collections2.filter(target, input -> {
            Pattern pattern = Pattern.compile(sourceSite);
            return pattern.matcher(input.getSourceSite()).find();
        }));
    }

    /**
     * Filter Videos By VideoType
     *
     * @param videoType Define in StaticParameter.VideoType ex: Trailer
     * @return List of VideoData
     */
    public ArrayList<VideoData> getVideosByVideoType(ArrayList<VideoData> target, String videoType) {
        return new ArrayList<VideoData>(Collections2.filter(target, input -> {
            Pattern pattern = Pattern.compile(videoType);
            return pattern.matcher(input.getVideoType()).find();
        }));
    }

    /**
     * (Private) Get Video Custom Comparator
     *
     * @return Custom Comparator
     */
    private Comparator<VideoData> getCustomVideoComparator() {
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
    public static class VideoData {

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

