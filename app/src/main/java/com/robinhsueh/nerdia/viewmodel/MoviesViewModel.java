package com.robinhsueh.nerdia.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.model.repository.MovieRepository;

import java.util.ArrayList;

public class MoviesViewModel extends ViewModel {
    private MovieRepository repository;

    // used when multiple liveData needs to observe different data in same activity or fragment
    private LiveData<ArrayList<MovieData>> upcomingMoviesLiveData;
    private LiveData<ArrayList<MovieData>> nowPlayingMoviesLiveData;
    private LiveData<ArrayList<MovieData>> trendingMoviesLiveData;
    private LiveData<ArrayList<MovieData>> popularMoviesLiveData;
    private LiveData<ArrayList<MovieData>> netflixMoviesLiveData;
    private LiveData<ArrayList<MovieData>> disneyMoviesLiveData;
    private LiveData<ArrayList<MovieData>> catchplayMoviesLiveData;
    private LiveData<ArrayList<MovieData>> primeMoviesLiveData;

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void init() {
        repository = new MovieRepository();
        upcomingMoviesLiveData = repository.getUpcomingMoviesLiveData();
        nowPlayingMoviesLiveData = repository.getNowPlayingMoviesLiveData();
        trendingMoviesLiveData = repository.getTrendingMoviesLiveData();
        popularMoviesLiveData = repository.getPopularMoviesLiveData();
        netflixMoviesLiveData = repository.getNetflixMoviesLiveData();
        disneyMoviesLiveData = repository.getDisneyMoviesLiveData();
        catchplayMoviesLiveData = repository.getCatchplayMoviesLiveData();
        primeMoviesLiveData = repository.getPrimeMoviesLiveData();
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

    // region Netflix Movies

    /**
     * Call repository to get netflix movies and update to liveData
     *
     * @param page target page
     */
    public void getNetflixMovies(int page) {
        repository.getNetflixMovies(page);
    }

    /**
     * Get the liveData to observe it (For Netflix Movies)
     *
     * @return
     */
    public LiveData<ArrayList<MovieData>> getNetflixMoviesLiveData() {
        return netflixMoviesLiveData;
    }
    // endregion

    // region Disney Movies

    /**
     * Call repository to get disney movies and update to liveData
     *
     * @param page target page
     */
    public void getDisneyMovies(int page) {
        repository.getDisneyMovies(page);
    }

    /**
     * Get the liveData to observe it (For Disney Movies)
     *
     * @return
     */
    public LiveData<ArrayList<MovieData>> getDisneyMoviesLiveData() {
        return disneyMoviesLiveData;
    }
    // endregion

    // region Catchplay Movies

    /**
     * Call repository to get catchplay movies and update to liveData
     *
     * @param page target page
     */
    public void getCatchplayMovies(int page) {
        repository.getCatchplayMovies(page);
    }

    /**
     * Get the liveData to observe it (For Catchplay Movies)
     *
     * @return
     */
    public LiveData<ArrayList<MovieData>> getCatchplayMoviesLiveData() {
        return catchplayMoviesLiveData;
    }
    // endregion

    // region Prime Movies

    /**
     * Call repository to get prime movies and update to liveData
     *
     * @param page target page
     */
    public void getPrimeMovies(int page) {
        repository.getPrimeMovies(page);
    }

    /**
     * Get the liveData to observe it (For Prime Movies)
     *
     * @return
     */
    public LiveData<ArrayList<MovieData>> getPrimeMoviesLiveData() {
        return primeMoviesLiveData;
    }
    // endregion

}
