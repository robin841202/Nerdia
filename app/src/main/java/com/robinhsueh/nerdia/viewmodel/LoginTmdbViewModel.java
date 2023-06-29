package com.robinhsueh.nerdia.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.robinhsueh.nerdia.model.auth.RequestTokenResponse;
import com.robinhsueh.nerdia.model.auth.SessionResponse;
import com.robinhsueh.nerdia.model.repository.AuthRepository;
import com.robinhsueh.nerdia.model.repository.UserRepository;
import com.robinhsueh.nerdia.model.user.UserData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginTmdbViewModel extends AndroidViewModel {
    private final String LOG_TAG = "LoginTmdbViewModel";
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    private final MutableLiveData<RequestTokenResponse> requestTokenLiveData;
    private final MutableLiveData<SessionResponse> sessionLiveData;
    private final MutableLiveData<UserData> userLiveData;

    public LoginTmdbViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository();
        userRepository = new UserRepository();
        requestTokenLiveData = new MutableLiveData<>();
        sessionLiveData = new MutableLiveData<>();
        userLiveData = new MutableLiveData<>();
    }

    // region RequestToken

    /**
     * Call repository to get new token and update to liveData
     */
    public void requestNewToken() {
        Call<RequestTokenResponse> response = authRepository.requestNewToken();
        response.enqueue(new Callback<RequestTokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<RequestTokenResponse> call, @NonNull Response<RequestTokenResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    if (response.body() != null) { // Data exists
                        // post result data to liveData
                        requestTokenLiveData.postValue(response.body());
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
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<RequestTokenResponse> getRequestTokenLiveData() {
        return requestTokenLiveData;
    }

    // endregion

    // region Session

    /**
     * Call repository to create session by valid token and update to liveData
     */
    public void createSession(String requestToken) {
        Call<SessionResponse> response = authRepository.createSession(requestToken);
        response.enqueue(new Callback<SessionResponse>() {
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
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<SessionResponse> getSessionLiveData() {
        return sessionLiveData;
    }

    // endregion

    // region UserData

    /**
     * Call repository to get account details and update to liveData
     */
    public void getUserData(String session) {
        Call<UserData> response = userRepository.getUserData(session);
        response.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {
                if (response.isSuccessful()) { // Request successfully
                    if (response.body() != null) { // Data exists
                        // post result data to liveData
                        userLiveData.postValue(response.body());
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

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<UserData> getUserLiveData() {
        return userLiveData;
    }

    // endregion
}
