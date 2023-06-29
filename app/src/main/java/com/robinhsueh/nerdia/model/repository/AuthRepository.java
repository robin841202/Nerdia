package com.robinhsueh.nerdia.model.repository;

import com.robinhsueh.nerdia.BuildConfig;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.auth.RequestTokenResponse;
import com.robinhsueh.nerdia.model.auth.SessionResponse;
import com.robinhsueh.nerdia.model.service.IAuthService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthRepository {
    private final IAuthService service;
    private final String apiKey = BuildConfig.TMDB_API_KEY;

    public AuthRepository() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticParameter.TmdbApiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IAuthService.class);
    }

    // region MVVM architecture using LiveData

    /**
     * Request new token (using LiveData)
     */
    public Call<RequestTokenResponse> requestNewToken() {
        return service.requestNewToken(apiKey);
    }

    /**
     * Create session (using LiveData)
     *
     * @param requestToken valid token
     */
    public Call<SessionResponse> createSession(String requestToken) {
        return service.createSession(apiKey, requestToken);
    }

    /**
     * Delete session (using LiveData)
     *
     * @param session user session
     */
    public Call<ResponseBody> deletesSession(String session) {
        return service.deleteSession(apiKey, session);
    }

    // endregion
}
