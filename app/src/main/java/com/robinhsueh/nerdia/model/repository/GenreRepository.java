package com.robinhsueh.nerdia.model.repository;


import com.robinhsueh.nerdia.BuildConfig;
import com.robinhsueh.nerdia.model.GenresResponse;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.service.IGenreService;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenreRepository {
    private final IGenreService service;
    private final String apiKey = BuildConfig.TMDB_API_KEY;
    private String language;
    private String region;

    public GenreRepository() {
        this.language = Locale.TRADITIONAL_CHINESE.toLanguageTag();
        this.region = Locale.TAIWAN.getCountry();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticParameter.TmdbApiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IGenreService.class);
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    // region MVVM architecture using LiveData

    /**
     * Get Movie Genres List (using LiveData)
     */
    public Call<GenresResponse> getMovieGenres() {
        return service.getMovieGenres(apiKey, language);
    }

    /**
     * Get TvShow Genres List (using LiveData)
     */
    public Call<GenresResponse> getTvShowGenres() {
        return service.getTvShowGenres(apiKey, language);
    }

    // endregion
}
