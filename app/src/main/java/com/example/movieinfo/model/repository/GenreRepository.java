package com.example.movieinfo.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.movieinfo.BuildConfig;
import com.example.movieinfo.model.GenreData;
import com.example.movieinfo.model.GenresResponse;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.service.IGenreService;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenreRepository {
    private final String LOG_TAG = "GenreRepository";
    private final IGenreService service;
    private final String apiKey = BuildConfig.TMDB_API_KEY;
    private String language;
    private String region;

    // region GenreData List LiveData

    private MutableLiveData<ArrayList<GenreData>> genresLiveData;

    // endregion

    public GenreRepository() {
        this.language = Locale.TRADITIONAL_CHINESE.toLanguageTag();
        this.region = Locale.TAIWAN.getCountry();

        // Initialize LiveData
        initLiveData();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticParameter.TmdbApiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IGenreService.class);
    }

    /**
     * Initialize every LiveData
     */
    private void initLiveData() {
        genresLiveData = new MutableLiveData<>();
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
    public void getMovieGenres() {
        Call<GenresResponse> call = service.getMovieGenres(apiKey, language);
        Callback<GenresResponse> requestHandler = getGenresResponseRequestHandler(genresLiveData);
        call.enqueue(requestHandler);
    }

    /**
     * Get TvShow Genres List (using LiveData)
     */
    public void getTvShowGenres() {
        Call<GenresResponse> call = service.getTvShowGenres(apiKey, language);
        Callback<GenresResponse> requestHandler = getGenresResponseRequestHandler(genresLiveData);
        call.enqueue(requestHandler);
    }


    /**
     * (private) Get Request Handler (using LiveData)
     *
     * @param genresLiveData live data
     * @return Request Handler
     */
    private Callback<GenresResponse> getGenresResponseRequestHandler(MutableLiveData<ArrayList<GenreData>> genresLiveData) {
        return new Callback<GenresResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenresResponse> call, @NonNull Response<GenresResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    GenresResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        genresLiveData.postValue(responseBody.genres);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenresResponse> call, @NonNull Throwable t) {
                // post null to liveData
                genresLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: \n %s ", t.getMessage()));
            }
        };
    }

    /***
     * Get Genres Response Live Data
     * @return
     */
    public MutableLiveData<ArrayList<GenreData>> getGenresLiveData() {
        return genresLiveData;
    }
    // endregion
}
