package com.robinhsueh.nerdia.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.robinhsueh.nerdia.model.auth.RequestTokenResponse;
import com.robinhsueh.nerdia.model.auth.SessionResponse;
import com.robinhsueh.nerdia.model.repository.AuthRepository;
import com.robinhsueh.nerdia.model.repository.UserRepository;
import com.robinhsueh.nerdia.model.user.UserData;


public class LoginTmdbViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private UserRepository userRepository;

    private LiveData<RequestTokenResponse> requestTokenLiveData;
    private LiveData<SessionResponse> sessionLiveData;
    private LiveData<UserData> userLiveData;

    public LoginTmdbViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository();
        userRepository = new UserRepository();
        requestTokenLiveData = authRepository.getRequestTokenLiveData();
        sessionLiveData = authRepository.getSessionLiveData();
        userLiveData = userRepository.getUserLiveData();
    }

    /**
     * Call repository to get new token and update to liveData
     */
    public void requestNewToken() {
        authRepository.requestNewToken();
    }

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<RequestTokenResponse> getRequestTokenLiveData() {
        return requestTokenLiveData;
    }


    /**
     * Call repository to create session by valid token and update to liveData
     */
    public void createSession(String requestToken) {
        authRepository.createSession(requestToken);
    }

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<SessionResponse> getSessionLiveData() {
        return sessionLiveData;
    }



    /**
     * Call repository to get account details and update to liveData
     */
    public void getUserData(String session) {
        userRepository.getUserData(session);
    }

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<UserData> getUserLiveData() {
        return userLiveData;
    }

}
