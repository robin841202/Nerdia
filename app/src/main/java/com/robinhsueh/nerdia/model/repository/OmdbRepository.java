package com.robinhsueh.nerdia.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.robinhsueh.nerdia.BuildConfig;
import com.robinhsueh.nerdia.model.OmdbData;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.service.IOmdbService;

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
        Call<OmdbData> call = service.getDataByImdbId(apiKey, imdbId, true);
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
