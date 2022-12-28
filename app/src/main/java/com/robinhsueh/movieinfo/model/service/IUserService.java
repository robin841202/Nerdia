package com.robinhsueh.movieinfo.model.service;

import com.robinhsueh.movieinfo.model.TmdbStatusResponse;
import com.robinhsueh.movieinfo.model.user.AccountStatesOnMedia;
import com.robinhsueh.movieinfo.model.user.RequestBody.BodyRate;
import com.robinhsueh.movieinfo.model.user.RequestBody.BodyWatchlist;
import com.robinhsueh.movieinfo.model.user.UserData;
import com.google.common.util.concurrent.ListenableFuture;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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


    // region Rating

    /**
     * Rate movie in TMDB
     *
     * @param movieId  Movie Id
     * @param apiKey   TMDB API Key
     * @param session  Valid session
     * @param bodyRate Post body
     * @return Status Response Model
     */
    @POST("movie/{movie_id}/rating")
    ListenableFuture<TmdbStatusResponse> rateMovieTMDB(
            @Path("movie_id")
                    long movieId,
            @Query("api_key")
                    String apiKey,
            @Query("session_id")
                    String session,
            @Body
                    BodyRate bodyRate
    );

    /**
     * Remove rate on movie in TMDB
     *
     * @param movieId Movie Id
     * @param apiKey  TMDB API Key
     * @param session Valid session
     * @return Status Response Model
     */
    @DELETE("movie/{movie_id}/rating")
    ListenableFuture<TmdbStatusResponse> removeRateMovieTMDB(
            @Path("movie_id")
                    long movieId,
            @Query("api_key")
                    String apiKey,
            @Query("session_id")
                    String session
    );

    /**
     * Rate tvShow in TMDB
     *
     * @param tvShowId TvShow Id
     * @param apiKey   TMDB API Key
     * @param session  Valid session
     * @param bodyRate Post body
     * @return Status Response Model
     */
    @POST("tv/{tv_id}/rating")
    ListenableFuture<TmdbStatusResponse> rateTvShowTMDB(
            @Path("tv_id")
                    long tvShowId,
            @Query("api_key")
                    String apiKey,
            @Query("session_id")
                    String session,
            @Body
                    BodyRate bodyRate
    );

    /**
     * Remove rate on tvShow in TMDB
     *
     * @param tvShowId TvShow Id
     * @param apiKey   TMDB API Key
     * @param session  Valid session
     * @return Status Response Model
     */
    @DELETE("tv/{tv_id}/rating")
    ListenableFuture<TmdbStatusResponse> removeRateTvShowTMDB(
            @Path("tv_id")
                    long tvShowId,
            @Query("api_key")
                    String apiKey,
            @Query("session_id")
                    String session
    );

    // endregion

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
