package com.robinhsueh.nerdia.model;

import java.util.Locale;

public class StaticParameter {

    public static final String TmdbApiBaseUrl = "https://api.themoviedb.org/3/";
    public static final String OmdbApiBaseUrl = "https://www.omdbapi.com/";
    public static final String AuthLocalValidUrlAuthority = "auth";
    public static final String AuthLocalValidUrlPath = "allow";
    public static final String AuthLocalValidUrl = "nerdia://" + AuthLocalValidUrlAuthority + "/" + AuthLocalValidUrlPath;
    public static final String TmdbAuthFormatUrl = "https://www.themoviedb.org/authenticate/%s?redirect_to=" + AuthLocalValidUrl;
    public static final String JustWatchSearchFormatUrl = "https://www.justwatch.com/tw/search?content_type=%s&q=%s";
    public static final String PlayStoreDetailFormatUrl = "https://play.google.com/store/apps/details?id=%s";
    private static final String TmdbImageBaseUrl = "https://image.tmdb.org/t/p/";
    private static final String YoutubeThumbnailFormatUrl = "https://img.youtube.com/vi/%s/hqdefault.jpg";
    private static final String TmdbWebBaseUrl = "https://www.themoviedb.org/";
    private static final String ImdbWebBaseUrl = "https://www.imdb.com/";
    private static final String GravatarFormatUrl = "https://www.gravatar.com/avatar/%s?size=%d";


    /**
     * Define extra data key for passing data to other activities or fragments
     */
    public static class ExtraDataKey {
        public static final String EXTRA_DATA_MEDIA_TYPE_KEY = "EXTRA_DATA_MEDIA_TYPE";
        public static final String EXTRA_DATA_MOVIE_ID_KEY = "EXTRA_DATA_MOVIE_ID";
        public static final String EXTRA_DATA_TVSHOW_ID_KEY = "EXTRA_DATA_TVSHOW_ID";
        public static final String EXTRA_DATA_PERSON_ID_KEY = "EXTRA_DATA_PERSON_ID";
        public static final String EXTRA_DATA_VERTICAL_BROWSE_KEY = "EXTRA_DATA_VERTICAL_BROWSE";
        public static final String EXTRA_DATA_VIDEO_ID_KEY = "EXTRA_DATA_VIDEO_ID";
        public static final String EXTRA_DATA_IMAGE_PATH_KEY = "EXTRA_DATA_IMAGE_PATH";
        public static final String EXTRA_DATA_INCLUDE_GENRES_KEY = "EXTRA_DATA_INCLUDE_GENRES";
        public static final String EXTRA_DATA_ACTIONBAR_TITLE_KEY = "EXTRA_DATA_ACTIONBAR_TITLE";
        public static final String EXTRA_DATA_LOGIN_KEY = "EXTRA_DATA_LOGIN";
        public static final String EXTRA_DATA_LOGIN_IS_SUCCESS_KEY = "EXTRA_DATA_LOGIN_IS_SUCCESS";
    }

    public static class SharedPreferenceFileKey {
        public static final String SP_FILE_TMDB_KEY = "SP_TMDB";
    }

    public static class SharedPreferenceFieldKey {
        public static final String SP_FIELD_TMDB_TOKEN_KEY = "SP_FIELD_TMDB_TOKEN";
        public static final String SP_FIELD_TMDB_SESSION_KEY = "SP_FIELD_TMDB_SESSION";
        public static final String SP_FIELD_TMDB_USERDATA_KEY = "SP_FIELD_TMDB_USERDATA";
    }

    public static class OmdbSourceName {
        public static final String IMDB = "Internet Movie Database";
        public static final String ROTTEN_TOMATOES = "Rotten Tomatoes";
        public static final String METACRETIC = "Metacritic";
    }

    public static class MediaType {
        public static final String ALL = "all";
        public static final String MOVIE = "movie";
        public static final String TV = "tv";
        public static final String PERSON = "person";
    }

    public static class SubRequestType{
        public static final String VIDEOS = "videos";
        public static final String IMAGES = "images";
        public static final String CREDITS = "credits";
        public static final String EXTERNAL_IDS = "external_ids";
        public static final String ACCOUNT_STATES = "account_states";
    }

    public static class TimeWindow {
        public static final String DAILY = "day";
        public static final String WEEKLY = "week";
    }

    public static class HomeCategory {
        public static final int UPCOMING_MOVIES = 0;
        public static final int NOWPLAYING_MOVIES = 1;
        public static final int TRENDING_MOVIES = 2;
        public static final int POPULAR_MOVIES = 3;
        public static final int POPULAR_TVSHOWS = 4;
        public static final int TRENDING_TVSHOWS = 5;
        public static final int NETFLIX_MOVIES = 6;
        public static final int NETFLIX_TVSHOWS = 7;
    }

    public static class VideoType {
        public static final String TRAILER = "Trailer";
        public static final String TEASER = "Teaser";
        public static final String BEHIND_THE_SCENES = "Behind the Scenes";
        public static final String FEATURETTE = "Featurette";
    }

    public static class VideoSourceSite {
        public static final String YOUTUBE = "YouTube";
    }

    public static class SlideShowType {
        public static final int VIDEO = 0;
        public static final int IMAGE = 1;
    }

    public static class BackdropSize {
        public static final String W300 = "w300";
        public static final String W780 = "w780";
        public static final String W1280 = "w1280";
        public static final String ORIGINAL = "original";
    }

    public static class PosterSize {
        public static final String W92 = "w92";
        public static final String W154 = "w154";
        public static final String W185 = "w185";
        public static final String W342 = "w342";
        public static final String W500 = "w500";
        public static final String W780 = "w780";
        public static final String ORIGINAL = "original";
    }

    public static class ProfileSize {
        public static final String W45 = "w45";
        public static final String W185 = "w185";
        public static final String H632 = "h632";
        public static final String ORIGINAL = "original";
    }

    public static class SortMode {
        public static final String CREATED_DATE_ASC = "created_at.asc";
        public static final String CREATED_DATE_DESC = "created_at.desc";
    }

    /**
     * WatchProvider Id on TMDB API
     */
    public static class WatchProvidersID{
        public static final long NetflixID = 8;
        public static final long DisneyPlusID = 337;
        public static final long CatchPlayID = 159;
        public static final long PrimeVideoID = 119;
    }

    public static class ThirdPartyAppsPkgName{
        public static final String Netflix = "com.netflix.mediaclient";
        public static final String DisneyPlus = "com.disney.disneyplus";
        public static final String CatchPlay = "com.catchplay.asiaplay";
        public static final String PrimeVideo = "com.amazon.avod.thirdpartyclient";
    }

    /**
     * Get TMDB Image Full Url
     *
     * @param size     Define in StaticParameter.BackdropSize or .PosterSize ex: w1280
     * @param filePath
     * @return TMDB Image Full Url
     */
    public static String getTmdbImageUrl(String size, String filePath) {
        return TmdbImageBaseUrl + size + filePath;
    }

    /**
     * Get TMDB Web Page Full Url
     *
     * @param mediaType Define in StaticParameter.MediaType ex: movie,tv
     * @param id        ID
     * @return TMDB Web Page Full Url
     */
    public static String getTmdbWebUrl(String mediaType, long id) {
        return TmdbWebBaseUrl + mediaType + "/" + id;
    }

    /**
     * Get IMDB Web Page Full Url
     *
     * @param id ID
     * @return IMDB Web Page Full Url
     */
    public static String getImdbWebUrl(String id) {
        return ImdbWebBaseUrl + "title/" + id;
    }

    /**
     * Get Gravatar Image Full Url
     *
     * @param hash hash
     * @param size 1px to 2048px
     * @return Gravatar Image Full Url
     */
    public static String getGravatarImageUrl(String hash, int size) {
        return String.format(Locale.TAIWAN, GravatarFormatUrl, hash, size);
    }

    /**
     * Get Youtube Thumbnail Image Full Url
     *
     * @param videoId Youtube video Id
     * @return Youtube Thumbnail Image Full Url
     */
    public static String getYtThumbnailUrl(String videoId){
        return String.format(Locale.TAIWAN, YoutubeThumbnailFormatUrl, videoId);
    }
}
