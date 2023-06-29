package com.robinhsueh.nerdia.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.robinhsueh.nerdia.BuildConfig;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.TmdbStatusResponse;
import com.robinhsueh.nerdia.model.service.IUserService;
import com.robinhsueh.nerdia.model.user.AccountStatesOnMedia;
import com.robinhsueh.nerdia.model.user.RequestBody.BodyRate;
import com.robinhsueh.nerdia.model.user.RequestBody.BodyWatchlist;
import com.robinhsueh.nerdia.model.user.UserData;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRepository {
    private final String LOG_TAG = "UserRepository";
    private final IUserService service;
    private final String apiKey = BuildConfig.TMDB_API_KEY;

    public UserRepository() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticParameter.TmdbApiBaseUrl)
                .addCallAdapterFactory(GuavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IUserService.class);
    }

    // region MVVM architecture using LiveData

    // region USER DATA

    /**
     * Get account details (using LiveData)
     *
     * @param session User session
     */
    public Call<UserData> getUserData(String session) {
        return service.getUserData(apiKey, session);
    }

    // endregion

    // region TMDB STATUS RESPONSE

    /**
     * Add or delete movie or tvShow in TMDB watchlist (using LiveData)
     *
     * @param userId        User Id
     * @param session       Valid session
     * @param bodyWatchlist Post body
     */
    public Call<TmdbStatusResponse> updateMediaToWatchlistTMDB(long userId, String session, BodyWatchlist bodyWatchlist) {
        return service.updateMediaToWatchlistTMDB(userId, apiKey, session, bodyWatchlist);
    }

    /**
     * Rate movie in TMDB (using LiveData)
     *
     * @param movieId  Movie Id
     * @param session  Valid session
     * @param bodyRate Post body
     */
    public boolean rateMovieTMDB(long movieId, String session, BodyRate bodyRate) {
        boolean isSuccess;
        ListenableFuture<TmdbStatusResponse> call = service.rateMovieTMDB(movieId, apiKey, session, bodyRate);
        try {
            TmdbStatusResponse response = call.get();
            switch (response != null ? response.getStatusCode() : 0) {
                case 1: // Success
                case 12: // The item/record was updated successfully.
                    isSuccess = true;
                    break;
                default:
                    isSuccess = false;
                    break;
            }
        } catch (ExecutionException | InterruptedException e) {
            isSuccess = false;
            Log.d(LOG_TAG, String.format("data fetch failed: rateMovieTMDB,\n %s ", e.getMessage()));
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * Rate tvShow in TMDB (using LiveData)
     *
     * @param tvShowId TvShow Id
     * @param session  Valid session
     * @param bodyRate Post body
     */
    public boolean rateTvShowTMDB(long tvShowId, String session, BodyRate bodyRate) {
        boolean isSuccess;
        ListenableFuture<TmdbStatusResponse> call = service.rateTvShowTMDB(tvShowId, apiKey, session, bodyRate);
        try {
            TmdbStatusResponse response = call.get();
            switch (response != null ? response.getStatusCode() : 0) {
                case 1: // Success
                case 12: // The item/record was updated successfully.
                    isSuccess = true;
                    break;
                default:
                    isSuccess = false;
                    break;
            }
        } catch (ExecutionException | InterruptedException e) {
            isSuccess = false;
            Log.d(LOG_TAG, String.format("data fetch failed: rateTvShowTMDB,\n %s ", e.getMessage()));
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * Remove rate on movie in TMDB (using LiveData)
     *
     * @param movieId  Movie Id
     * @param session  Valid session
     */
    public boolean removeRateMovieTMDB(long movieId, String session) {
        boolean isSuccess;
        ListenableFuture<TmdbStatusResponse> call = service.removeRateMovieTMDB(movieId, apiKey, session);
        try {
            TmdbStatusResponse response = call.get();
            switch (response != null ? response.getStatusCode() : 0) {
                case 13: // The item/record was deleted successfully.
                    isSuccess = true;
                    break;
                default:
                    isSuccess = false;
                    break;
            }
        } catch (ExecutionException | InterruptedException e) {
            isSuccess = false;
            Log.d(LOG_TAG, String.format("data fetch failed: removeRateMovieTMDB,\n %s ", e.getMessage()));
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * Remove rate on tvShow in TMDB (using LiveData)
     *
     * @param tvShowId TvShow Id
     * @param session  Valid session
     */
    public boolean removeRateTvShowTMDB(long tvShowId, String session) {
        boolean isSuccess;
        ListenableFuture<TmdbStatusResponse> call = service.removeRateTvShowTMDB(tvShowId, apiKey, session);
        try {
            TmdbStatusResponse response = call.get();
            switch (response != null ? response.getStatusCode() : 0) {
                case 13: // The item/record was deleted successfully.
                    isSuccess = true;
                    break;
                default:
                    isSuccess = false;
                    break;
            }
        } catch (ExecutionException | InterruptedException e) {
            isSuccess = false;
            Log.d(LOG_TAG, String.format("data fetch failed: removeRateTvShowTMDB,\n %s ", e.getMessage()));
            e.printStackTrace();
        }
        return isSuccess;
    }

    // endregion

    // region ACCOUNT STATES ON MEDIA

    /**
     * Get Account states on TMDB movie (using LiveData)
     *
     * @param movieId Movie Id
     * @param session User session
     */
    public Call<AccountStatesOnMedia> getTMDBAccountStatesOnMovie(long movieId, String session) {
        return service.getTMDBAccountStatesOnMovie(movieId, apiKey, session);
    }

    /**
     * Get Account states on TMDB tvShow (using LiveData)
     *
     * @param tvShowId TvShow Id
     * @param session  User session
     */
    public Call<AccountStatesOnMedia> getTMDBAccountStatesOnTvShow(long tvShowId, String session) {
        return service.getTMDBAccountStatesOnTvShow(tvShowId, apiKey, session);
    }

    // endregion

    // endregion
}
