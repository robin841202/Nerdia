package com.example.movieinfo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.movieinfo.model.OmdbData;
import com.example.movieinfo.model.database.entity.MovieWatchlistEntity;
import com.example.movieinfo.model.database.entity.TvShowWatchlistEntity;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.repository.MovieRepository;
import com.example.movieinfo.model.repository.OmdbRepository;
import com.example.movieinfo.model.repository.TvShowRepository;
import com.example.movieinfo.model.repository.WatchlistRepository;
import com.example.movieinfo.model.tvshow.TvShowDetailData;
import com.google.common.util.concurrent.ListenableFuture;

public class MediaDetailViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private TvShowRepository tvShowRepository;
    private OmdbRepository omdbRepository;
    private WatchlistRepository watchlistRepository;
    private LiveData<MovieDetailData> movieDetailLiveData;
    private LiveData<TvShowDetailData> tvShowDetailLiveData;
    private LiveData<OmdbData> omdbLiveData;

    public MediaDetailViewModel(@NonNull Application application) {
        super(application);
        watchlistRepository = new WatchlistRepository(application);
    }

    /**
     * Initialize ViewModel, Only call this when you need a new ViewModel instead of getting shared ViewModel
     */
    public void init() {
        movieRepository = new MovieRepository();
        tvShowRepository = new TvShowRepository();
        omdbRepository = new OmdbRepository();
        movieDetailLiveData = movieRepository.getMovieDetailLiveData();
        tvShowDetailLiveData = tvShowRepository.getTvShowDetailLiveData();
        omdbLiveData = omdbRepository.getOmdbLiveData();
    }

    // region Remote Data Source (API)

    // region Movie Details

    /**
     * Call repository to get movie detail and update to liveData
     *
     * @param movieId        Movie Id
     * @param subRequestType Can do subRequest in the same time  ex: videos
     * @param videoLanguages Can include multiple languages of video ex:zh-TW,en
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     */
    public void getMovieDetail(long movieId, String subRequestType, String videoLanguages, String imageLanguages) {
        movieRepository.getMovieDetail(movieId, subRequestType, videoLanguages, imageLanguages);
    }

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<MovieDetailData> getMovieDetailLiveData() {
        return movieDetailLiveData;
    }

    // endregion

    // region Omdb Data

    /**
     * Call repository to get omdb data by IMDB Id and update to liveData
     *
     * @param imdbId IMDB Id
     */
    public void getDataByImdbId(String imdbId) {
        omdbRepository.getDataByImdbId(imdbId);
    }

    /**
     * Get the OMDB liveData to observe it
     *
     * @return
     */
    public LiveData<OmdbData> getOmdbLiveData() {
        return omdbLiveData;
    }

    // endregion

    // region TvShow Details

    /**
     * Call repository to get tvShow detail and update to liveData
     *
     * @param tvShowId       TvShow Id
     * @param subRequestType Can do subRequest in the same time  ex: videos
     * @param videoLanguages Can include multiple languages of video ex:zh-TW,en
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     */
    public void getTvShowDetail(long tvShowId, String subRequestType, String videoLanguages, String imageLanguages) {
        tvShowRepository.getTvShowDetail(tvShowId, subRequestType, videoLanguages, imageLanguages);
    }

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<TvShowDetailData> getTvShowDetailLiveData() {
        return tvShowDetailLiveData;
    }

    // endregion

    // endregion

    // region Room Database

    // region Movie Watchlist

    /**
     * Check movie if exist in watchlist By Id and get liveData to observe it (Room Database)
     *
     * @param id movie Id
     * @return
     */
    public LiveData<Boolean> checkMovieExistInWatchlist(long id) {
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
    public LiveData<Boolean> checkTvShowExistInWatchlist(long id) {
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

}
