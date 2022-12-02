package com.example.movieinfo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.movieinfo.model.TmdbStatusResponse;
import com.example.movieinfo.model.database.entity.MovieWatchlistEntity;
import com.example.movieinfo.model.database.entity.TvShowWatchlistEntity;
import com.example.movieinfo.model.repository.UserRepository;
import com.example.movieinfo.model.repository.WatchlistRepository;
import com.example.movieinfo.model.user.AccountStatesOnMedia;
import com.example.movieinfo.model.user.BodyWatchlist;
import com.google.common.util.concurrent.ListenableFuture;

public class OperateMediaViewModel extends AndroidViewModel {
    private final WatchlistRepository watchlistRepository;
    private UserRepository userRepository;
    private LiveData<TmdbStatusResponse> watchlistUpdateResponseLiveData;
    private LiveData<AccountStatesOnMedia> accountStatesLiveData;

    public OperateMediaViewModel(@NonNull Application application) {
        super(application);
        watchlistRepository = new WatchlistRepository(application);
    }

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void init() {
        userRepository = new UserRepository();
        watchlistUpdateResponseLiveData = userRepository.getStatusResponseLiveData();
        accountStatesLiveData = userRepository.getAccountStatesLiveData();
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
        userRepository.getTMDBAccountStatesOnMovie(movieId, session);
    }

    /**
     * Call repository to get account states on TMDB tvShow and update status to liveData
     *
     * @param tvShowId TvShow Id
     * @param session Valid session
     */
    public void getTMDBAccountStatesOnTvShow(long tvShowId, String session) {
        userRepository.getTMDBAccountStatesOnTvShow(tvShowId, session);
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
        userRepository.updateMediaToWatchlistTMDB(userId, session, bodyWatchlist);
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
