package com.robinhsueh.nerdia.model.repository;


import com.robinhsueh.nerdia.BuildConfig;
import com.robinhsueh.nerdia.model.OmdbData;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.service.IOmdbService;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OmdbRepository {
    private final IOmdbService service;
    private final String apiKey = BuildConfig.OMDB_API_KEY;

    public OmdbRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticParameter.OmdbApiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IOmdbService.class);
    }

    // region MVVM architecture using LiveData

    /**
     * Get Data By IMDB Id (using LiveData)
     *
     * @param imdbId IMDB Id
     */
    public Call<OmdbData> getDataByImdbId(String imdbId) {
        return service.getDataByImdbId(apiKey, imdbId, true);
    }

    // endregion
}
