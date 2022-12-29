package com.robinhsueh.nerdia.model.service;

import com.robinhsueh.nerdia.model.GenresResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IGenreService {

    /**
     * Get Movie Genres List
     *
     * @param apiKey   TMDB API Key
     * @param language ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @return
     */
    @GET("genre/movie/list")
    Call<GenresResponse> getMovieGenres(
            @Query("api_key")
                    String apiKey,
            @Query("language")
                    String language
    );


    /**
     * Get TvShow Genres List
     *
     * @param apiKey   TMDB API Key
     * @param language ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @return
     */
    @GET("genre/tv/list")
    Call<GenresResponse> getTvShowGenres(
            @Query("api_key")
                    String apiKey,
            @Query("language")
                    String language
    );
}
