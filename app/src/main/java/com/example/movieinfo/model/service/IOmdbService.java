package com.example.movieinfo.model.service;

import com.example.movieinfo.model.OmdbData;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.movie.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IOmdbService {

    /**
     * Get Data By IMDB Id
     *
     * @param apiKey OMDB API Key
     * @param imdbId IMDB Id
     * @return
     */
    @GET("/")
    Call<OmdbData> getDataByImdbId(
            @Query("apikey")
                    String apiKey,
            @Query("i")
                    String imdbId,
            @Query("tomatoes")
                    boolean includeTomatoes
    );

}
