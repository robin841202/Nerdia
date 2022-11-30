package com.example.movieinfo.model.service;

import com.example.movieinfo.model.tvshow.TvShowDetailData;
import com.example.movieinfo.model.tvshow.TvShowsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ITvShowService {

    /**
     * Get Popular TvShows
     *
     * @param apiKey   TMDB API Key
     * @param page     target page
     * @param language ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @param region   ISO 3166-1 code to filter release dates. Must be uppercase. ex: TW
     * @return
     */
    @GET("tv/popular")
    Call<TvShowsResponse> getPopularTvShows(
            @Query("api_key")
                    String apiKey,
            @Query("page")
                    int page,
            @Query("language")
                    String language,
            @Query("region")
                    String region
    );

    /**
     * Get Trending Movies, TvShows or Person
     *
     * @param mediaType  "all", "movie", "tv", "person"
     * @param timeWindow "day", "week"
     * @param apiKey     TMDB API Key
     * @param page       target page
     * @param language   ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @param region     ISO 3166-1 code to filter release dates. Must be uppercase. ex: TW
     * @return
     */
    @GET("trending/{media_type}/{time_window}")
    Call<TvShowsResponse> getTrendingMedia(
            @Path("media_type")
                    String mediaType,
            @Path("time_window")
                    String timeWindow,
            @Query("api_key")
                    String apiKey,
            @Query("page")
                    int page,
            @Query("language")
                    String language,
            @Query("region")
                    String region
    );


    /**
     * Get TvShow Detail By TvShow Id
     *
     * @param id             TvShow Id
     * @param apiKey         TMDB API Key
     * @param language       ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @param subRequestType Can do subRequest in the same time  ex: videos
     * @param videoLanguages Can include multiple languages of video ex:zh-TW,en
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     * @return
     */
    @GET("tv/{tv_id}")
    Call<TvShowDetailData> getTvShowDetail(
            @Path("tv_id")
                    long id,
            @Query("api_key")
                    String apiKey,
            @Query("language")
                    String language,
            @Query("append_to_response")
                    String subRequestType,
            @Query("include_video_language")
                    String videoLanguages,
            @Query("include_image_language")
                    String imageLanguages
    );


    /**
     * Search TvShows By Keyword
     *
     * @param apiKey   TMDB API Key
     * @param keyWord  searching keyword
     * @param page     target page
     * @param language ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @param region   ISO 3166-1 code to filter release dates. Must be uppercase. ex: TW
     * @return
     */
    @GET("search/tv")
    Call<TvShowsResponse> searchTvShows(
            @Query("api_key")
                    String apiKey,
            @Query("query")
                    String keyWord,
            @Query("page")
                    int page,
            @Query("language")
                    String language,
            @Query("region")
                    String region
    );


    /**
     * Get Similar TvShows By TvShow Id
     *
     * @param id       TvShow Id
     * @param apiKey   TMDB API Key
     * @param page     target page
     * @param language ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @return
     */
    @GET("tv/{tv_id}/similar")
    Call<TvShowsResponse> getSimilarTvShows(
            @Path("tv_id")
                    long id,
            @Query("api_key")
                    String apiKey,
            @Query("page")
                    int page,
            @Query("language")
                    String language
    );

    /**
     * Discover TvShows By multiple filter
     *
     * @param apiKey        TMDB API Key
     * @param page          target page
     * @param language      ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @param region        ISO 3166-1 code to filter release dates. Must be uppercase. ex: TW
     * @param includeGenres Comma separated value of genre ids that you want to include in the results.
     * @return
     */
    @GET("discover/tv")
    Call<TvShowsResponse> discoverTvShows(
            @Query("api_key")
                    String apiKey,
            @Query("page")
                    int page,
            @Query("language")
                    String language,
            @Query("region")
                    String region,
            @Query("with_genres")
                    String includeGenres
    );


    /**
     * Get TvShow Watchlist on TMDB
     * @param id Account Id
     * @param apiKey TMDB API Key
     * @param session Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page target page
     * @param language ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @return
     */
    @GET("account/{account_id}/watchlist/tv")
    Call<TvShowsResponse> getTMDBTvShowWatchlist(
            @Path("account_id")
                    long id,
            @Query("api_key")
                    String apiKey,
            @Query("session_id")
                    String session,
            @Query("sort_by")
                    String sortMode,
            @Query("page")
                    int page,
            @Query("language")
                    String language
    );


}
