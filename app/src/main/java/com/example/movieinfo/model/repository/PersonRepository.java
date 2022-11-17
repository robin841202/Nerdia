package com.example.movieinfo.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.movieinfo.BuildConfig;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.movie.MoviesResponse;
import com.example.movieinfo.model.person.PeopleResponse;
import com.example.movieinfo.model.person.PersonData;
import com.example.movieinfo.model.person.PersonDetailData;
import com.example.movieinfo.model.service.IPersonService;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PersonRepository {
    private final String LOG_TAG = "PersonRepository";
    private final IPersonService service;
    private final String apiKey = BuildConfig.TMDB_API_KEY;
    private String language;
    private String region;

    // PersonDetail LiveData
    private MutableLiveData<PersonDetailData> personDetailLiveData;

    // PersonData List LiveData
    private MutableLiveData<ArrayList<PersonData>> peopleLiveData;

    public PersonRepository() {
        this.language = Locale.TRADITIONAL_CHINESE.toLanguageTag();
        this.region = Locale.TAIWAN.getCountry();

        // Initialize LiveData
        initLiveData();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticParameter.TmdbApiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IPersonService.class);
    }

    /**
     * Initialize every LiveData
     */
    private void initLiveData() {
        personDetailLiveData = new MutableLiveData<>();
        peopleLiveData = new MutableLiveData<>();
    }

    // region MVVM architecture using LiveData

    /**
     * Get Person Detail By Movie Id (using LiveData)
     *
     * @param movieId        Movie Id
     * @param subRequestType Can do subRequest in the same time  ex: images
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     */
    public void getPersonDetail(long movieId, String subRequestType, String imageLanguages) {
        Call<PersonDetailData> call = service.getPersonDetail(movieId, apiKey, language, subRequestType, imageLanguages);
        call.enqueue(new Callback<PersonDetailData>() {
            @Override
            public void onResponse(@NonNull Call<PersonDetailData> call, @NonNull Response<PersonDetailData> response) {
                if (response.isSuccessful()) { // Request successfully
                    PersonDetailData responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        personDetailLiveData.postValue(responseBody);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PersonDetailData> call, @NonNull Throwable t) {
                // post null to liveData
                personDetailLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: getPersonDetail,\n %s ", t.getMessage()));
            }
        });
    }

    /**
     * Search People By Keyword (using LiveData)
     *
     * @param keyWord keyword for searching
     * @param page    target page
     */
    public void searchPeople(String keyWord, int page) {
        Call<PeopleResponse> call = service.searchPeople(apiKey, keyWord, page, language, region);
        Callback<PeopleResponse> requestHandler = getPeopleResponseRequestHandler(peopleLiveData);
        call.enqueue(requestHandler);
    }

    /**
     * (private) Get Request Handler (using LiveData)
     *
     * @param personLiveData live data
     * @return Request Handler
     */
    private Callback<PeopleResponse> getPeopleResponseRequestHandler(MutableLiveData<ArrayList<PersonData>> personLiveData) {
        return new Callback<PeopleResponse>() {
            @Override
            public void onResponse(@NonNull Call<PeopleResponse> call, @NonNull Response<PeopleResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    PeopleResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        personLiveData.postValue(responseBody.people_list);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PeopleResponse> call, @NonNull Throwable t) {
                // post null to liveData
                personLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: \n %s ", t.getMessage()));
            }
        };
    }


    /***
     * Get Person Detail Live Data
     * @return
     */
    public MutableLiveData<PersonDetailData> getPersonDetailLiveData() {
        return personDetailLiveData;
    }

    /***
     * Get PersonData List  Live Data
     * @return
     */
    public MutableLiveData<ArrayList<PersonData>> getPeopleLiveData() {
        return peopleLiveData;
    }
    // endregion
}
