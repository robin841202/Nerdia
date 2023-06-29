package com.robinhsueh.nerdia.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.robinhsueh.nerdia.model.TmdbStatusResponse;
import com.robinhsueh.nerdia.model.database.entity.MovieWatchlistEntity;
import com.robinhsueh.nerdia.model.database.entity.TvShowWatchlistEntity;
import com.robinhsueh.nerdia.model.repository.UserRepository;
import com.robinhsueh.nerdia.model.repository.WatchlistRepository;
import com.robinhsueh.nerdia.model.user.AccountStatesOnMedia;
import com.robinhsueh.nerdia.model.user.RequestBody.BodyWatchlist;
import com.google.common.util.concurrent.ListenableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperateMediaViewModel extends AndroidViewModel {
    private final String LOG_TAG = "OperateMediaViewModel";
    private final WatchlistRepository watchlistRepository;
    private UserRepository userRepository;
    private MutableLiveData<TmdbStatusResponse> watchlistUpdateResponseLiveData;
    private MutableLiveData<AccountStatesOnMedia> accountStatesLiveData;

    public OperateMediaViewModel(@NonNull Application application) {
        super(application);
        watchlistRepository = new WatchlistRepository(application);
    }

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void init() {
        userRepository = new UserRepository();
        watchlistUpdateResponseLiveData = new MutableLiveData<>();
        accountStatesLiveData = new MutableLiveData<>();
    }

    // region Room Database

    // region Movie Watchlist

    /**
     * Check movie if exist in watchlist By Id and get liveData to observe it (Room Database)
     *
     * @param id movie Id
     * @return
     */
    public ListenableFuture<Boolean> checkMovieExistInWatchlist(long id) {
        return watchlistRepository.checkMovieExistInWatchlist(id);
    }


    /**
     * Insert movie in watchlist (Room Database)
     *
     * @param entity
     * @return
     */
    public ListenableFuture<Long> insertMovieWatchlist(MovieWatchlistEntity entity) {
        return watchlistRepository.insertMovie(entity);
    }

    /**
     * Delete movie in watchlist by movieId (Room Database)
     *
     * @param id movie Id
     * @return
     */
    public ListenableFuture<Integer> deleteMovieWatchlistById(long id) {
        return watchlistRepository.deleteMovieById(id);
    }

    // endregion

    // region TvShow Watchlist

    /**
     * Check tvShow if exist in watchlist By Id and get liveData to observe it (Room Database)
     *
     * @param id tvShow Id
     * @return
     */
    public ListenableFuture<Boolean> checkTvShowExistInWatchlist(long id) {
        return watchlistRepository.checkTvShowExistInWatchlist(id);
    }

    /**
     * Insert tvShow in watchlist (Room Database)
     *
     * @param entity
     * @return
     */
    public ListenableFuture<Long> insertTvShowWatchlist(TvShowWatchlistEntity entity) {
        return watchlistRepository.insertTvShow(entity);
    }

    /**
     * Delete tvShow in watchlist by tvShowId (Room Database)
     *
     * @param id tvShow Id
     * @return
     */
    public ListenableFuture<Integer> deleteTvShowWatchlistById(long id) {
        return watchlistRepository.deleteTvShowById(id);
    }

    // endregion

    // endregion


    // region Remote Data Source (API)

    // region Get Account States on Media

    /**
     * Call repository to get account states on TMDB movie and update status to liveData
     *
     * @param movieId Movie Id
     * @param session Valid session
     */
    public void getTMDBAccountStatesOnMovie(long movieId, String session) {
        Call<AccountStatesOnMedia> response = userRepository.getTMDBAccountStatesOnMovie(movieId, session);
        response.enqueue(new Callback<AccountStatesOnMedia>() {
            @Override
            public void onResponse(@NonNull Call<AccountStatesOnMedia> call, @NonNull Response<AccountStatesOnMedia> response) {
                if (response.isSuccessful()) { // Request successfully
                    if (response.body() != null) { // Data exists
                        // post result data to liveData
                        accountStatesLiveData.postValue(response.body());
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
     * Call repository to get account states on TMDB tvShow and update status to liveData
     *
     * @param tvShowId TvShow Id
     * @param session Valid session
     */
    public void getTMDBAccountStatesOnTvShow(long tvShowId, String session) {
        Call<AccountStatesOnMedia> response = userRepository.getTMDBAccountStatesOnTvShow(tvShowId, session);
        response.enqueue(new Callback<AccountStatesOnMedia>() {
            @Override
            public void onResponse(@NonNull Call<AccountStatesOnMedia> call, @NonNull Response<AccountStatesOnMedia> response) {
                if (response.isSuccessful()) { // Request successfully
                    if (response.body() != null) { // Data exists
                        // post result data to liveData
                        accountStatesLiveData.postValue(response.body());
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

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<AccountStatesOnMedia> getAccountStatesLiveData() {
        return accountStatesLiveData;
    }

    // endregion

    // region Update Watchlist

    /**
     * Call repository to add or delete media in TMDB watchlist and update status to liveData
     *
     * @param userId        User Id
     * @param session       Valid session
     * @param bodyWatchlist Post body
     */
    public void updateMediaToWatchlistTMDB(long userId, String session, BodyWatchlist bodyWatchlist) {
        Call<TmdbStatusResponse> response = userRepository.updateMediaToWatchlistTMDB(userId, session, bodyWatchlist);
        response.enqueue(new Callback<TmdbStatusResponse>() {
            @Override
            public void onResponse(@NonNull Call<TmdbStatusResponse> call, @NonNull Response<TmdbStatusResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    if (response.body() != null) { // Data exists
                        // post result data to liveData
                        watchlistUpdateResponseLiveData.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TmdbStatusResponse> call, @NonNull Throwable t) {
                // post null to liveData
                watchlistUpdateResponseLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: updateMediaToWatchlistTMDB,\n %s ", t.getMessage()));
            }
        });
    }

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<TmdbStatusResponse> getWatchlistUpdateResponseLiveData() {
        return watchlistUpdateResponseLiveData;
    }

    // endregion

    // endregion
}
