package com.example.movieinfo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.repository.MovieRepository;

import java.util.ArrayList;

public class MoviesViewModel extends ViewModel {
    private MovieRepository repository;

    // used when multiple liveData needs to observe different data in same activity or fragment
    private LiveData<ArrayList<MovieData>> upcomingMoviesLiveData;
    private LiveData<ArrayList<MovieData>> nowPlayingMoviesLiveData;
    private LiveData<ArrayList<MovieData>> trendingMoviesLiveData;
    private LiveData<ArrayList<MovieData>> popularMoviesLiveData;

    /**
     * Initialize ViewModel, Only call this when you need a new ViewModel instead of getting shared ViewModel
     */
    public void init() {
        repository = new MovieRepository();
        upcomingMoviesLiveData = repository.getUpcomingMoviesLiveData();
        nowPlayingMoviesLiveData = repository.getNowPlayingMoviesLiveData();
        trendingMoviesLiveData = repository.getTrendingMoviesLiveData();
        popularMoviesLiveData = repository.getPopularMoviesLiveData();
    }

    // region Upcoming Movies

    /**
     * Call repository to get upcoming movies and update to liveData
     *
     * @param page target page
     */
    public void getUpcomingMovies(int page) {
        repository.getUpcomingMovies(page);
    }

    /**
     * Get the liveData to observe it (For Upcoming Movies)
     *
     * @return
     */
    public LiveData<ArrayList<MovieData>> getUpcomingMoviesLiveData() {
        return upcomingMoviesLiveData;
    }
    // endregion

    // region Now-Playing Movies

    /**
     * Call repository to get now-playing movies and update to liveData
     *
     * @param page target page
     */
    public void getNowPlayingMovies(int page) {
        repository.getNowPlayingMovies(page);
    }

    /**
     * Get the liveData to observe it (For NowPlaying Movies)
     *
     * @return
     */
    public LiveData<ArrayList<MovieData>> getNowPlayingMoviesLiveData() {
        return nowPlayingMoviesLiveData;
    }
    // endregion

    // region Trending Movies

    /**
     * Call repository to get trending movies and update to liveData
     *
     * @param page target page
     */
    public void getTrendingMovies(String timeWindow, int page) {
        repository.getTrendingMovies(timeWindow, page);
    }

    /**
     * Get the liveData to observe it (For Trending Movies)
     *
     * @return
     */
    public LiveData<ArrayList<MovieData>> getTrendingMoviesLiveData() {
        return trendingMoviesLiveData;
    }
    // endregion

    // region Popular Movies

    /**
     * Call repository to get popular movies and update to liveData
     *
     * @param page target page
     */
    public void getPopularMovies(int page) {
        repository.getPopularMovies(page);
    }

    /**
     * Get the liveData to observe it (For Popular Movies)
     *
     * @return
     */
    public LiveData<ArrayList<MovieData>> getPopularMoviesLiveData() {
        return popularMoviesLiveData;
    }
    // endregion

}
