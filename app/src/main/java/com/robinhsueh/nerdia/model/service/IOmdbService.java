package com.robinhsueh.nerdia.model.service;

import com.robinhsueh.nerdia.model.OmdbData;

import retrofit2.Call;
import retrofit2.http.GET;
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
