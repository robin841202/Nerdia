package com.robinhsueh.nerdia.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.robinhsueh.nerdia.model.database.entity.MovieWatchlistEntity;
import com.robinhsueh.nerdia.model.database.entity.TvShowWatchlistEntity;
import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.model.repository.MovieRepository;
import com.robinhsueh.nerdia.model.repository.TvShowRepository;
import com.robinhsueh.nerdia.model.repository.WatchlistRepository;
import com.robinhsueh.nerdia.model.tvshow.TvShowData;

import java.util.ArrayList;
import java.util.List;

public class WatchlistViewModel extends AndroidViewModel {
    private final WatchlistRepository watchlistRepository;
    private MovieRepository movieRepository;
    private TvShowRepository tvShowRepository;
    private LiveData<ArrayList<MovieData>> movieWatchlistLiveData;
    private LiveData<ArrayList<TvShowData>> tvShowWatchlistLiveData;

    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        watchlistRepository = new WatchlistRepository(application);
    }

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void initLiveData() {
        movieRepository = new MovieRepository();
        tvShowRepository = new TvShowRepository();
        movieWatchlistLiveData = movieRepository.getMoviesLiveData();
        tvShowWatchlistLiveData = tvShowRepository.getTvShowsLiveData();
    }

    // region Room Database

    /**
     * Load All Movie Watchlist from room database (using LiveData)
     */
    public LiveData<List<MovieWatchlistEntity>> loadAllMovieWatchlist() {
        return watchlistRepository.getAllMovieWatchlist();
    }

    /**
     * Load All TvShow Watchlist from room database (using LiveData)
     */
    public LiveData<List<TvShowWatchlistEntity>> loadAllTvShowWatchlist() {
        return watchlistRepository.getAllTvShowWatchlist();
    }

    // endregion

    // region Remote Data Source (API)

    // region Movie Watchlist

    /**
     * Call repository to get movie watchlist and update to liveData
     *
     * @param userId   Account Id
     * @param session  Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page     target page
     */
    public void getTMDBMovieWatchlist(long userId, String session, String sortMode, int page) {
        movieRepository.getTMDBMovieWatchlist(userId, session, sortMode, page);
    }

    /**
     * Get the liveData to observe it (For Movie Watchlist)
     *
     * @return
     */
    public LiveData<ArrayList<MovieData>> getMovieWatchlistLiveData() {
        return movieWatchlistLiveData;
    }
    // endregion

    // region TvShow Watchlist

    /**
     * Call repository to get tvShow watchlist and update to liveData
     *
     * @param userId   Account Id
     * @param session  Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page     target page
     */
    public void getTMDBTvShowWatchlist(long userId, String session, String sortMode, int page) {
        tvShowRepository.getTMDBTvShowWatchlist(userId, session, sortMode, page);
    }

    /**
     * Get the liveData to observe it (For TvShow Watchlist)
     *
     * @return
     */
    public LiveData<ArrayList<TvShowData>> getTvShowWatchlistLiveData() {
        return tvShowWatchlistLiveData;
    }
    // endregion

    // endregion
}
