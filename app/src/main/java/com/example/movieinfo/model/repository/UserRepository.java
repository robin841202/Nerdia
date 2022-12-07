package com.example.movieinfo.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.movieinfo.BuildConfig;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.TmdbStatusResponse;
import com.example.movieinfo.model.service.IUserService;
import com.example.movieinfo.model.user.AccountStatesOnMedia;
import com.example.movieinfo.model.user.RequestBody.BodyRate;
import com.example.movieinfo.model.user.RequestBody.BodyWatchlist;
import com.example.movieinfo.model.user.UserData;
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

    // UserData LiveData
    private final MutableLiveData<UserData> userLiveData = new MutableLiveData<>();

    // TmdbStatusResponse LiveData
    private final MutableLiveData<TmdbStatusResponse> statusResponseLiveData = new MutableLiveData<>();

    // AccountStatesOnMedia LiveData
    private final MutableLiveData<AccountStatesOnMedia> accountStatesLiveData = new MutableLiveData<>();

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

    // region TMDB STATUS RESPONSE

    /**
     * Add or delete movie or tvShow in TMDB watchlist (using LiveData)
     *
     * @param userId        User Id
     * @param session       Valid session
     * @param bodyWatchlist Post body
     */
    public void updateMediaToWatchlistTMDB(long userId, String session, BodyWatchlist bodyWatchlist) {
        Call<TmdbStatusResponse> call = service.updateMediaToWatchlistTMDB(userId, apiKey, session, bodyWatchlist);
        call.enqueue(new Callback<TmdbStatusResponse>() {
            @Override
            public void onResponse(@NonNull Call<TmdbStatusResponse> call, @NonNull Response<TmdbStatusResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    TmdbStatusResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        statusResponseLiveData.postValue(responseBody);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TmdbStatusResponse> call, @NonNull Throwable t) {
                // post null to liveData
                statusResponseLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: updateMediaToWatchlistTMDB,\n %s ", t.getMessage()));
            }
        });
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

    /***
     * Get TmdbStatusResponse Live Data
     * @return
     */
    public MutableLiveData<TmdbStatusResponse> getStatusResponseLiveData() {
        return statusResponseLiveData;
    }

    // endregion

    // region ACCOUNT STATES ON MEDIA

    /**
     * Get Account states on TMDB movie (using LiveData)
     *
     * @param movieId Movie Id
     * @param session User session
     */
    public void getTMDBAccountStatesOnMovie(long movieId, String session) {
        Call<AccountStatesOnMedia> call = service.getTMDBAccountStatesOnMovie(movieId, apiKey, session);
        call.enqueue(new Callback<AccountStatesOnMedia>() {
            @Override
            public void onResponse(@NonNull Call<AccountStatesOnMedia> call, @NonNull Response<AccountStatesOnMedia> response) {
                if (response.isSuccessful()) { // Request successfully
                    AccountStatesOnMedia responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        accountStatesLiveData.postValue(responseBody);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccountStatesOnMedia> call, @NonNull Throwable t) {
                // post null to liveData
                accountStatesLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: getTMDBAccountStatesOnMovie,\n %s ", t.getMessage()));
            }
        });
    }

    /**
     * Get Account states on TMDB tvShow (using LiveData)
     *
     * @param tvShowId TvShow Id
     * @param session  User session
     */
    public void getTMDBAccountStatesOnTvShow(long tvShowId, String session) {
        Call<AccountStatesOnMedia> call = service.getTMDBAccountStatesOnTvShow(tvShowId, apiKey, session);
        call.enqueue(new Callback<AccountStatesOnMedia>() {
            @Override
            public void onResponse(@NonNull Call<AccountStatesOnMedia> call, @NonNull Response<AccountStatesOnMedia> response) {
                if (response.isSuccessful()) { // Request successfully
                    AccountStatesOnMedia responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        accountStatesLiveData.postValue(responseBody);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccountStatesOnMedia> call, @NonNull Throwable t) {
                // post null to liveData
                accountStatesLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: getTMDBAccountStatesOnTvShow,\n %s ", t.getMessage()));
            }
        });
    }

    /***
     * Get AccountStatesOnMedia Live Data
     * @return
     */
    public MutableLiveData<AccountStatesOnMedia> getAccountStatesLiveData() {
        return accountStatesLiveData;
    }

    // endregion

    // endregion
}
