package com.example.movieinfo.model;

public class StaticParameter {

    private static final String ImageBaseUrl = "https://image.tmdb.org/t/p/";
    private static final String YoutubeThumbnailBaseUrl = "https://img.youtube.com/vi/";

    /**
     * Define extra data key for passing data to other activities or fragments
     */
    public static class ExtraDataKey{
        public static final String EXTRA_DATA_MEDIA_TYPE_KEY = "EXTRA_DATA_MEDIA_TYPE";
        public static final String EXTRA_DATA_MOVIE_ID_KEY = "EXTRA_DATA_MOVIE_ID";
        public static final String EXTRA_DATA_TVSHOW_ID_KEY = "EXTRA_DATA_TVSHOW_ID";
        public static final String EXTRA_DATA_VERTICAL_BROWSE_KEY = "EXTRA_DATA_VERTICAL_BROWSE";
        public static final String EXTRA_DATA_VIDEO_ID_KEY = "EXTRA_DATA_VIDEO_ID";
    }

    public static class MediaType{
        public static final String ALL = "all";
        public static final String MOVIE = "movie";
        public static final String TV = "tv";
        public static final String PERSON = "person";
    }

    public static class TimeWindow{
        public static final String DAILY = "day";
        public static final String WEEKLY = "week";
    }

    public static class HomeCategory{
        public static final int UPCOMING_MOVIES = 0;
        public static final int NOWPLAYING_MOVIES = 1;
        public static final int TRENDING_MOVIES = 2;
        public static final int POPULAR_MOVIES = 3;
        public static final int POPULAR_TVSHOWS = 4;
        public static final int TRENDING_TVSHOWS = 5;
    }

    public static class VideoType{
        public static final String TRAILER = "Trailer";
        public static final String TEASER = "Teaser";
        public static final String BEHIND_THE_SCENES = "Behind the Scenes";
        public static final String FEATURETTE = "Featurette";
    }

    public static class VideoSourceSite{
        public static final String YOUTUBE = "YouTube";
    }

    public static class SlideShowType{
        public static final int VIDEO = 0;
        public static final int IMAGE = 1;
    }

    public static class BackdropSize{
        public static final String W300 = "w300";
        public static final String W780 = "w780";
        public static final String W1280 = "w1280";
        public static final String ORIGINAL = "original";
    }

    public static class PosterSize{
        public static final String W92 = "w92";
        public static final String W154 = "w154";
        public static final String W185 = "w185";
        public static final String W342 = "w342";
        public static final String W500 = "w500";
        public static final String W780 = "w780";
        public static final String ORIGINAL = "original";
    }

    /**
     * Get TMDB Image Full Url
     * @param size Define in StaticParameter.BackdropSize or .PosterSize ex: w1280
     * @param filePath
     * @return TMDB Image Full Url
     */
    public static String getImageUrl(String size, String filePath){
        return ImageBaseUrl + size + filePath;
    }
}
