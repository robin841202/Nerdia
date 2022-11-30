package com.example.movieinfo.model.service;

import com.example.movieinfo.model.user.UserData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IUserService {

    /**
     * Get account details
     *
     * @param apiKey TMDB API Key
     * @param session User session
     * @return
     */
    @GET("account")
    Call<UserData> getUserData(
            @Query("api_key")
                    String apiKey,
            @Query("session_id")
                    String session
    );

}
