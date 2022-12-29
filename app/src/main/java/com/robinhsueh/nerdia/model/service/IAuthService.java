package com.robinhsueh.nerdia.model.service;


import com.robinhsueh.nerdia.model.auth.RequestTokenResponse;
import com.robinhsueh.nerdia.model.auth.SessionResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IAuthService {

    /**
     * Request New Token
     *
     * @param apiKey TMDB API Key
     * @return
     */
    @GET("authentication/token/new")
    Call<RequestTokenResponse> requestNewToken(
            @Query("api_key")
                    String apiKey
    );

    /**
     * Create Session
     *
     * @param apiKey       TMDB API Key
     * @param requestToken valid token
     * @return
     */
    @FormUrlEncoded
    @POST("authentication/session/new")
    Call<SessionResponse> createSession(
            @Query("api_key")
                    String apiKey,
            @Field("request_token")
                    String requestToken
    );


    /**
     * Delete Session
     *
     * @param apiKey  TMDB API Key
     * @param session User session
     * @return
     */
    @DELETE("authentication/session")
    Call<ResponseBody> deleteSession(
            @Query("api_key")
                    String apiKey,
            @Field("session_id")
                    String session
    );
}
