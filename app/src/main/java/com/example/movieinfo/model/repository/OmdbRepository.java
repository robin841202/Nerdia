package com.example.movieinfo.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.movieinfo.BuildConfig;
import com.example.movieinfo.model.OmdbData;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.movie.MoviesResponse;
import com.example.movieinfo.model.service.IMovieService;
import com.example.movieinfo.model.service.IOmdbService;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OmdbRepository {
    private final String LOG_TAG = "OmdbRepository";
    private final IOmdbService service;
    private final String apiKey = BuildConfig.OMDB_API_KEY;

    // region MovieDetail LiveData
    private MutableLiveData<OmdbData> omdbLiveData;
    // endregion

    public OmdbRepository() {

        // Initialize LiveData
        initLiveData();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticParameter.OmdbApiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IOmdbService.class);
    }

    /**
     * Initialize every LiveData
     */
    private void initLiveData() {
        omdbLiveData = new MutableLiveData<>();
    }

    // region MVVM architecture using LiveData

    /**
     * Get Data By IMDB Id (using LiveData)
     *
     * @param imdbId IMDB Id
     */
    public void getDataByImdbId(String imdbId) {
        Call<OmdbData> call = service.getDataByImdbId(apiKey, imdbId);
        call.enqueue(new Callback<OmdbData>() {
            @Override
            public void onResponse(@NonNull Call<OmdbData> call, @NonNull Response<OmdbData> response) {
                if (response.isSuccessful()) { // Request successfully
                    OmdbData responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        omdbLiveData.postValue(responseBody);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<OmdbData> call, @NonNull Throwable t) {
                // post null to liveData
                omdbLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: getDataByImdbId,\n %s ", t.getMessage()));
            }
        });
    }

    /***
     * Get Omdb Live Data
     * @return
     */
    public MutableLiveData<OmdbData> getOmdbLiveData() {
        return omdbLiveData;
    }
    // endregion
}
