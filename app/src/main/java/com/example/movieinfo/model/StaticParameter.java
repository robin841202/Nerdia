package com.example.movieinfo.model;

public class StaticParameter {
    public class MediaType{
        public static final String ALL = "all";
        public static final String MOVIE = "movie";
        public static final String TV = "tv";
        public static final String PERSON = "person";
    }

    public class TimeWindow{
        public static final String DAILY = "day";
        public static final String WEEKLY = "week";
    }

    public class HomeCategory{
        public static final int UPCOMING_MOVIES = 0;
        public static final int NOWPLAYING_MOVIES = 1;
        public static final int TRENDING_MOVIES = 2;
        public static final int POPULAR_MOVIES = 3;
        public static final int POPULAR_TVSHOWS = 4;
        public static final int TRENDING_TVSHOWS = 5;
    }
}
