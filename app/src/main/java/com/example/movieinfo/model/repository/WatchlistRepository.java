package com.example.movieinfo.model.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.movieinfo.model.database.dao.IMovieWatchlistDao;
import com.example.movieinfo.model.database.dao.ITvShowWatchlistDao;
import com.example.movieinfo.model.database.entity.MovieWatchlistEntity;
import com.example.movieinfo.model.database.entity.TvShowWatchlistEntity;
import com.example.movieinfo.model.database.helper.MyRoomDatabase;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.Executors;


public class WatchlistRepository {
    private final String LOG_TAG = "WatchlistRepository";
    private IMovieWatchlistDao movieWatchlistDao;
    private ITvShowWatchlistDao tvShowWatchlistDao;

    public WatchlistRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDataBase(application);
        // Initialize Dao
        initDao(db);
    }

    /**
     * Initialize every Dao
     */
    private void initDao(MyRoomDatabase db) {
        movieWatchlistDao = db.movieWatchlistDao();
        tvShowWatchlistDao = db.tvShowWatchlistDao();
    }

    // region Movie Watchlist

    /**
     * Get All Movie Watchlist (using LiveData)
     *
     * @return all movie watchlist (live data)
     */
    public LiveData<List<MovieWatchlistEntity>> getAllMovieWatchlist() {
        return movieWatchlistDao.getAll();
    }

    /**
     * Check Movie if exist in watchlist By Id and get liveData to observe it (using LiveData)
     *
     * @return exist or not (live data)
     */
    public LiveData<Boolean> checkMovieExistInWatchlist(long id) {
        return movieWatchlistDao.checkExistById(id);
    }

    // region Insert Movie Watchlist

    /**
     * Insert Movie Watchlist singleton
     * @param entity
     * @return
     */
    public ListenableFuture<Long> insertMovie (MovieWatchlistEntity entity){
        return movieWatchlistDao.insert(entity);
    }

    // endregion

    // region Delete Movie Watchlist

    /**
     * Delete Movie Watchlist singleton By Id
     * @param id
     * @return
     */
    public ListenableFuture<Integer> deleteMovieById (long id){
        return movieWatchlistDao.deleteById(id);
    }

    // endregion

    // endregion

    // region TvShow Watchlist

    /**
     * Get All TvShow Watchlist (using LiveData)
     *
     * @return all tvShow watchlist (live data)
     */
    public LiveData<List<TvShowWatchlistEntity>> getAllTvShowWatchlist() {
        return tvShowWatchlistDao.getAll();
    }

    /**
     * Check TvShow if exist in watchlist By Id and get liveData to observe it (using LiveData)
     *
     * @return exist or not (live data)
     */
    public LiveData<Boolean> checkTvShowExistInWatchlist(long id) {
        return tvShowWatchlistDao.checkExistById(id);
    }

    // region Insert TvShow Watchlist

    /**
     * Insert TvShow Watchlist singleton
     * @param entity
     * @return
     */
    public ListenableFuture<Long> insertTvShow (TvShowWatchlistEntity entity){
        return tvShowWatchlistDao.insert(entity);
    }

    // endregion

    // region Delete TvShow Watchlist

    /**
     * Delete TvShow Watchlist singleton By Id
     * @param id
     * @return
     */
    public ListenableFuture<Integer> deleteTvShowById (long id){
        return tvShowWatchlistDao.deleteById(id);
    }

    // endregion

    // endregion

}
