package com.example.movieinfo.model.service;

import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.movie.MoviesResponse;
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
                    String videoLanguages
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

}
