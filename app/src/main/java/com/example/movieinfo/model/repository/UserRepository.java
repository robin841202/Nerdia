package com.example.movieinfo.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.movieinfo.BuildConfig;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.service.IUserService;
import com.example.movieinfo.model.user.UserData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRepository {
    private final String LOG_TAG = "UserRepository";
    private final IUserService service;
    private final String apiKey = BuildConfig.TMDB_API_KEY;

    // UserData LiveData
    private MutableLiveData<UserData> userLiveData;

    public UserRepository() {

        // Initialize LiveData
        initLiveData();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticParameter.TmdbApiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IUserService.class);
    }

    /**
     * Initialize every LiveData
     */
    private void initLiveData() {
        userLiveData = new MutableLiveData<>();
    }

    // region MVVM architecture using LiveData

    /**
     * Get account details (using LiveData)
     *
     * @param session User session
     */
    public void getUserData(String session) {
        Call<UserData> call = service.getUserData(apiKey, session);
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {
                if (response.isSuccessful()) { // Request successfully
                    UserData responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        userLiveData.postValue(responseBody);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserData> call, @NonNull Throwable t) {
                // post null to liveData
                userLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: getUserData,\n %s ", t.getMessage()));
            }
        });
    }

    /***
     * Get UserData Live Data
     * @return
     */
    public MutableLiveData<UserData> getUserLiveData() {
        return userLiveData;
    }

    // endregion
}
