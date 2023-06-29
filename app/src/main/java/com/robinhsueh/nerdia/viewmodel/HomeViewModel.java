package com.robinhsueh.nerdia.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.model.movie.MoviesResponse;
import com.robinhsueh.nerdia.model.repository.MovieRepository;
import com.robinhsueh.nerdia.model.repository.TvShowRepository;
import com.robinhsueh.nerdia.model.tvshow.TvShowData;
import com.robinhsueh.nerdia.model.tvshow.TvShowsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private final String LOG_TAG = "HomeViewModel";
    private MovieRepository movieRepository;
    private TvShowRepository tvShowRepository;

    // region Movies Livedata
    private MutableLiveData<ArrayList<MovieData>> upcomingMoviesLiveData;
    private MutableLiveData<ArrayList<MovieData>> nowPlayingMoviesLiveData;
    private MutableLiveData<ArrayList<MovieData>> trendingMoviesLiveData;
    private MutableLiveData<ArrayList<MovieData>> popularMoviesLiveData;
    private MutableLiveData<ArrayList<MovieData>> netflixMoviesLiveData;
    private MutableLiveData<ArrayList<MovieData>> disneyMoviesLiveData;
    private MutableLiveData<ArrayList<MovieData>> catchplayMoviesLiveData;
    private MutableLiveData<ArrayList<MovieData>> primeMoviesLiveData;
    // endregion

    // region TvShows Livedata
    private MutableLiveData<ArrayList<TvShowData>> popularTvShowsLiveData;
    private MutableLiveData<ArrayList<TvShowData>> trendingTvShowsLiveData;
    private MutableLiveData<ArrayList<TvShowData>> netflixTvShowsLiveData;
    private MutableLiveData<ArrayList<TvShowData>> disneyTvShowsLiveData;
    private MutableLiveData<ArrayList<TvShowData>> catchplayTvShowsLiveData;
    private MutableLiveData<ArrayList<TvShowData>> primeTvShowsLiveData;
    // endregion

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void init() {
        movieRepository = new MovieRepository();
        tvShowRepository = new TvShowRepository();

        // Initialize movies Livedata
        upcomingMoviesLiveData = new MutableLiveData<>();
        nowPlayingMoviesLiveData = new MutableLiveData<>();
        trendingMoviesLiveData = new MutableLiveData<>();
        popularMoviesLiveData = new MutableLiveData<>();
        netflixMoviesLiveData = new MutableLiveData<>();
        disneyMoviesLiveData = new MutableLiveData<>();
        catchplayMoviesLiveData = new MutableLiveData<>();
        primeMoviesLiveData = new MutableLiveData<>();

        // Initialize tvShows Livedata
        trendingTvShowsLiveData = new MutableLiveData<>();
        popularTvShowsLiveData = new MutableLiveData<>();
        netflixTvShowsLiveData = new MutableLiveData<>();
        disneyTvShowsLiveData = new MutableLiveData<>();
        catchplayTvShowsLiveData = new MutableLiveData<>();
        primeTvShowsLiveData = new MutableLiveData<>();
    }

    // region MOVIES

    // region Upcoming Movies

    /**
     * Call repository to get upcoming movies and update to liveData
     *
     * @param page target page
     */
    public void getUpcomingMovies(int page) {
        Call<MoviesResponse> response = movieRepository.getUpcomingMovies(page);
        Callback<MoviesResponse> requestHandler = getMoviesResponseRequestHandler(upcomingMoviesLiveData);
        response.enqueue(requestHandler);
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
        Call<MoviesResponse> response = movieRepository.getNowPlayingMovies(page);
        Callback<MoviesResponse> requestHandler = getMoviesResponseRequestHandler(nowPlayingMoviesLiveData);
        response.enqueue(requestHandler);
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
        Call<MoviesResponse> response = movieRepository.getTrendingMovies(timeWindow, page);
        Callback<MoviesResponse> requestHandler = getMoviesResponseRequestHandler(trendingMoviesLiveData);
        response.enqueue(requestHandler);
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
        Call<MoviesResponse> response = movieRepository.getPopularMovies(page);
        Callback<MoviesResponse> requestHandler = getMoviesResponseRequestHandler(popularMoviesLiveData);
        response.enqueue(requestHandler);
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
        Call<MoviesResponse> response = movieRepository.getNetflixMovies(page);
        Callback<MoviesResponse> requestHandler = getMoviesResponseRequestHandler(netflixMoviesLiveData);
        response.enqueue(requestHandler);
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
        Call<MoviesResponse> response = movieRepository.getDisneyMovies(page);
        Callback<MoviesResponse> requestHandler = getMoviesResponseRequestHandler(disneyMoviesLiveData);
        response.enqueue(requestHandler);
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
        Call<MoviesResponse> response = movieRepository.getCatchplayMovies(page);
        Callback<MoviesResponse> requestHandler = getMoviesResponseRequestHandler(catchplayMoviesLiveData);
        response.enqueue(requestHandler);
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
        Call<MoviesResponse> response = movieRepository.getPrimeMovies(page);
        Callback<MoviesResponse> requestHandler = getMoviesResponseRequestHandler(primeMoviesLiveData);
        response.enqueue(requestHandler);
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

    /**
     * (private) Get Request Handler (using LiveData)
     *
     * @param moviesLiveData live data
     * @return Request Handler
     */
    private Callback<MoviesResponse> getMoviesResponseRequestHandler(MutableLiveData<ArrayList<MovieData>> moviesLiveData) {
        return new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    MoviesResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        moviesLiveData.postValue(responseBody.movie_list);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                // post null to liveData
                moviesLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: \n %s ", t.getMessage()));
            }
        };
    }

    // endregion

    // region TVSHOWS

    // region Popular TvShows

    /**
     * Call repository to get popular tvShows and update to liveData
     *
     * @param page target page
     */
    public void getPopularTvShows(int page) {
        Call<TvShowsResponse> response = tvShowRepository.getPopularTvShows(page);
        Callback<TvShowsResponse> requestHandler = getTvShowsResponseRequestHandler(popularTvShowsLiveData);
        response.enqueue(requestHandler);
    }

    /**
     * Get the liveData to observe it (For Popular TvShows)
     *
     * @return
     */
    public LiveData<ArrayList<TvShowData>> getPopularTvShowsLiveData() {
        return popularTvShowsLiveData;
    }
    // endregion

    // region Trending TvShows

    /**
     * Call repository to get trending tvShows and update to liveData
     *
     * @param page target page
     */
    public void getTrendingTvShows(String timeWindow, int page) {
        Call<TvShowsResponse> response = tvShowRepository.getTrendingTvShows(timeWindow, page);
        Callback<TvShowsResponse> requestHandler = getTvShowsResponseRequestHandler(trendingTvShowsLiveData);
        response.enqueue(requestHandler);
    }

    /**
     * Get the liveData to observe it (For TvShows Movies)
     *
     * @return
     */
    public LiveData<ArrayList<TvShowData>> getTrendingTvShowsLiveData() {
        return trendingTvShowsLiveData;
    }
    // endregion

    // region Netflix TvShows

    /**
     * Call repository to get netflix tvShows and update to liveData
     *
     * @param page target page
     */
    public void getNetflixTvShows(int page) {
        Call<TvShowsResponse> response = tvShowRepository.getNetflixTvShows(page);
        Callback<TvShowsResponse> requestHandler = getTvShowsResponseRequestHandler(netflixTvShowsLiveData);
        response.enqueue(requestHandler);
    }

    /**
     * Get the liveData to observe it (For Netflix TvShows)
     *
     * @return
     */
    public LiveData<ArrayList<TvShowData>> getNetflixTvShowsLiveData() {
        return netflixTvShowsLiveData;
    }
    // endregion

    // region Disney TvShows

    /**
     * Call repository to get disney tvShows and update to liveData
     *
     * @param page target page
     */
    public void getDisneyTvShows(int page) {
        Call<TvShowsResponse> response = tvShowRepository.getDisneyTvShows(page);
        Callback<TvShowsResponse> requestHandler = getTvShowsResponseRequestHandler(disneyTvShowsLiveData);
        response.enqueue(requestHandler);
    }

    /**
     * Get the liveData to observe it (For Disney TvShows)
     *
     * @return
     */
    public LiveData<ArrayList<TvShowData>> getDisneyTvShowsLiveData() {
        return disneyTvShowsLiveData;
    }
    // endregion

    // region Catchplay TvShows

    /**
     * Call repository to get catchplay tvShows and update to liveData
     *
     * @param page target page
     */
    public void getCatchplayTvShows(int page) {
        Call<TvShowsResponse> response = tvShowRepository.getCatchplayTvShows(page);
        Callback<TvShowsResponse> requestHandler = getTvShowsResponseRequestHandler(catchplayTvShowsLiveData);
        response.enqueue(requestHandler);
    }

    /**
     * Get the liveData to observe it (For Catchplay TvShows)
     *
     * @return
     */
    public LiveData<ArrayList<TvShowData>> getCatchplayTvShowsLiveData() {
        return catchplayTvShowsLiveData;
    }
    // endregion

    // region Prime TvShows

    /**
     * Call repository to get prime tvShows and update to liveData
     *
     * @param page target page
     */
    public void getPrimeTvShows(int page) {
        Call<TvShowsResponse> response = tvShowRepository.getPrimeTvShows(page);
        Callback<TvShowsResponse> requestHandler = getTvShowsResponseRequestHandler(primeTvShowsLiveData);
        response.enqueue(requestHandler);
    }

    /**
     * Get the liveData to observe it (For Prime TvShows)
     *
     * @return
     */
    public LiveData<ArrayList<TvShowData>> getPrimeTvShowsLiveData() {
        return primeTvShowsLiveData;
    }
    // endregion

    /**
     * (private) Get Request Handler (using LiveData)
     *
     * @param tvShowsLiveData live data
     * @return Request Handler
     */
    private Callback<TvShowsResponse> getTvShowsResponseRequestHandler(MutableLiveData<ArrayList<TvShowData>> tvShowsLiveData) {
        return new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowsResponse> call, @NonNull Response<TvShowsResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    TvShowsResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        tvShowsLiveData.postValue(responseBody.tvShow_list);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowsResponse> call, @NonNull Throwable t) {
                // post null to liveData
                tvShowsLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: \n %s ", t.getMessage()));
            }
        };
    }

    // endregion
}
