package com.example.movieinfo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieinfo.model.database.entity.MovieWatchlistEntity;
import com.example.movieinfo.model.database.entity.TvShowWatchlistEntity;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.repository.MovieRepository;
import com.example.movieinfo.model.repository.TvShowRepository;
import com.example.movieinfo.model.repository.WatchlistRepository;
import com.example.movieinfo.model.tvshow.TvShowData;

import java.util.ArrayList;
import java.util.List;

public class WatchlistViewModel extends AndroidViewModel {
    private final WatchlistRepository watchlistRepository;

    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        watchlistRepository = new WatchlistRepository(application);
    }

    /**
     * Initialize ViewModel, Only call this when you need a new ViewModel instead of getting shared ViewModel
     */
    public void init() {

    }

    /**
     * Load All Movie Watchlist from room database (using LiveData)
     *
     */
    public LiveData<List<MovieWatchlistEntity>> loadAllMovieWatchlist() {
        return watchlistRepository.getAllMovieWatchlist();
    }

    /**
     * Load All TvShow Watchlist from room database (using LiveData)
     *
     */
    public LiveData<List<TvShowWatchlistEntity>> loadAllTvShowWatchlist() {
        return watchlistRepository.getAllTvShowWatchlist();
    }

}
