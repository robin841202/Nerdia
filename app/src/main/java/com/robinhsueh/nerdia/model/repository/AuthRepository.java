package com.robinhsueh.nerdia.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.robinhsueh.nerdia.BuildConfig;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.auth.RequestTokenResponse;
import com.robinhsueh.nerdia.model.auth.SessionResponse;
import com.robinhsueh.nerdia.model.service.IAuthService;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthRepository {
    private final String LOG_TAG = "AuthRepository";
    private final IAuthService service;
    private final String apiKey = BuildConfig.TMDB_API_KEY;

    // RequestTokenResponse LiveData
    private MutableLiveData<RequestTokenResponse> requestTokenLiveData;

    // SessionResponse LiveData
    private MutableLiveData<SessionResponse> sessionLiveData;

    // IsLogout LiveData
    private MutableLiveData<Boolean> isLogoutLiveData;


    public AuthRepository() {

        // Initialize LiveData
        initLiveData();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticParameter.TmdbApiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IAuthService.class);
    }

    /**
     * Initialize every LiveData
     */
    private void initLiveData() {
        requestTokenLiveData = new MutableLiveData<>();
        sessionLiveData = new MutableLiveData<>();
    }

    // region MVVM architecture using LiveData

    /**
     * Request new token (using LiveData)
     */
    public void requestNewToken() {
        Call<RequestTokenResponse> call = service.requestNewToken(apiKey);
        call.enqueue(new Callback<RequestTokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<RequestTokenResponse> call, @NonNull Response<RequestTokenResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    RequestTokenResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        requestTokenLiveData.postValue(responseBody);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RequestTokenResponse> call, @NonNull Throwable t) {
                // post null to liveData
                requestTokenLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: requestNewToken,\n %s ", t.getMessage()));
            }
        });
    }

    /**
     * Create session (using LiveData)
     *
     * @param requestToken valid token
     */
    public void createSession(String requestToken) {
        Call<SessionResponse> call = service.createSession(apiKey, requestToken);
        call.enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(@NonNull Call<SessionResponse> call, @NonNull Response<SessionResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    SessionResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        sessionLiveData.postValue(responseBody);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SessionResponse> call, @NonNull Throwable t) {
                // post null to liveData
                sessionLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: createSession,\n %s ", t.getMessage()));
            }
        });
    }

    /**
     * Delete session (using LiveData)
     *
     * @param session user session
     */
    public void deletesSession(String session) {
        Call<ResponseBody> call = service.deleteSession(apiKey, session);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                isLogoutLiveData.postValue(response.isSuccessful());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                isLogoutLiveData.postValue(false);
                Log.d(LOG_TAG, String.format("data fetch failed: deletesSession,\n %s ", t.getMessage()));
            }
        });
    }


    /***
     * Get RequestToken Live Data
     * @return
     */
    public MutableLiveData<RequestTokenResponse> getRequestTokenLiveData() {
        return requestTokenLiveData;
    }

    /***
     * Get Session Live Data
     * @return
     */
    public MutableLiveData<SessionResponse> getSessionLiveData() {
        return sessionLiveData;
    }

    // endregion
}
