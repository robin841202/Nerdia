package com.example.movieinfo.model.service;

import com.example.movieinfo.model.TmdbStatusResponse;
import com.example.movieinfo.model.auth.SessionResponse;
import com.example.movieinfo.model.user.AccountStatesOnMedia;
import com.example.movieinfo.model.user.BodyWatchlist;
import com.example.movieinfo.model.user.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IUserService {

    /**
     * Get account details
     *
     * @param apiKey  TMDB API Key
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


    /**
     * Add or delete movie or tvShow in TMDB watchlist
     *
     * @param userId        User Id
     * @param apiKey        TMDB API Key
     * @param session       Valid session
     * @param bodyWatchlist Post body
     * @return Status Response Model
     */
    @POST("account/{account_id}/watchlist")
    Call<TmdbStatusResponse> updateMediaToWatchlistTMDB(
            @Path("account_id")
                    long userId,
            @Query("api_key")
                    String apiKey,
            @Query("session_id")
                    String session,
            @Body
                    BodyWatchlist bodyWatchlist
    );

    // region Account States on Media

    /**
     * Get Account states on TMDB movie
     *
     * @param id      Movie Id
     * @param apiKey  TMDB API Key
     * @param session Valid session
     * @return
     */
    @GET("movie/{movie_id}/account_states")
    Call<AccountStatesOnMedia> getTMDBAccountStatesOnMovie(
            @Path("movie_id")
                    long id,
            @Query("api_key")
                    String apiKey,
            @Query("session_id")
                    String session
    );

    /**
     * Get Account states on TMDB tvShow
     *
     * @param id      TvShow Id
     * @param apiKey  TMDB API Key
     * @param session Valid session
     * @return
     */
    @GET("tv/{tv_id}/account_states")
    Call<AccountStatesOnMedia> getTMDBAccountStatesOnTvShow(
            @Path("tv_id")
                    long id,
            @Query("api_key")
                    String apiKey,
            @Query("session_id")
                    String session
    );

    // endregion
}
