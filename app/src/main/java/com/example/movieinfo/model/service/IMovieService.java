package com.example.movieinfo.model.service;

import com.example.movieinfo.model.GenresResponse;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.movie.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IMovieService {

    /**
     * Get Upcoming Movies
     *
     * @param apiKey   TMDB API Key
     * @param page     target page
     * @param language ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @param region   ISO 3166-1 code to filter release dates. Must be uppercase. ex: TW
     * @return
     */
    @GET("movie/upcoming")
    Call<MoviesResponse> getUpcomingMovies(
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
     * Get Popular Movies
     *
     * @param apiKey   TMDB API Key
     * @param page     target page
     * @param language ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @param region   ISO 3166-1 code to filter release dates. Must be uppercase. ex: TW
     * @return
     */
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(
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
     * Get Now playing Movies
     *
     * @param apiKey   TMDB API Key
     * @param page     target page
     * @param language ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @param region   ISO 3166-1 code to filter release dates. Must be uppercase. ex: TW
     * @return
     */
    @GET("movie/now_playing")
    Call<MoviesResponse> getNowPlayingMovies(
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
    Call<MoviesResponse> getTrendingMedia(
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
     * Get Movie Detail By Movie Id
     *
     * @param id             Movie Id
     * @param apiKey         TMDB API Key
     * @param language       ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @param subRequestType Can do subRequest in the same time  ex: videos
     * @param videoLanguages Can include multiple languages of video ex:zh-TW,en
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     * @return
     */
    @GET("movie/{movie_id}")
    Call<MovieDetailData> getMovieDetail(
            @Path("movie_id")
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
     * Search Movies By Keyword
     *
     * @param apiKey   TMDB API Key
     * @param keyWord  searching keyword
     * @param page     target page
     * @param language ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @param region   ISO 3166-1 code to filter release dates. Must be uppercase. ex: TW
     * @return
     */
    @GET("search/movie")
    Call<MoviesResponse> searchMovies(
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
     * Get Similar Movies By Movie Id
     *
     * @param id       Movie Id
     * @param apiKey   TMDB API Key
     * @param page     target page
     * @param language ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @return
     */
    @GET("movie/{movie_id}/similar")
    Call<MoviesResponse> getSimilarMovies(
            @Path("movie_id")
                    long id,
            @Query("api_key")
                    String apiKey,
            @Query("page")
                    int page,
            @Query("language")
                    String language
    );

    /**
     * Discover Movies By multiple filter
     *
     * @param apiKey        TMDB API Key
     * @param page          target page
     * @param language      ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @param region        ISO 3166-1 code to filter release dates. Must be uppercase. ex: TW
     * @param includeGenres Comma separated value of genre ids that you want to include in the results.
     * @return
     */
    @GET("discover/movie")
    Call<MoviesResponse> discoverMovies(
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


}
