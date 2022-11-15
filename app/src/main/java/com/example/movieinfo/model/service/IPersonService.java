package com.example.movieinfo.model.service;

import com.example.movieinfo.model.OmdbData;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.person.PersonDetailData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IPersonService {

    /**
     * Get Person Detail By Person Id
     *
     * @param id             Person Id
     * @param apiKey         TMDB API Key
     * @param language       ISO 639-1 value to display translated data for the fields that support. ex: zh-TW
     * @param subRequestType Can do subRequest in the same time  ex: images
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     * @return
     */
    @GET("person/{person_id}")
    Call<PersonDetailData> getPersonDetail(
            @Path("person_id")
                    long id,
            @Query("api_key")
                    String apiKey,
            @Query("language")
                    String language,
            @Query("append_to_response")
                    String subRequestType,
            @Query("include_image_language")
                    String imageLanguages
    );

}
