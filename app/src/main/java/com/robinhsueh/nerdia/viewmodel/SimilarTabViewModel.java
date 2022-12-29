package com.robinhsueh.nerdia.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.model.repository.MovieRepository;
import com.robinhsueh.nerdia.model.repository.TvShowRepository;
import com.robinhsueh.nerdia.model.tvshow.TvShowData;

import java.util.ArrayList;

public class SimilarTabViewModel extends ViewModel {
    private MovieRepository movieRepository;
    private TvShowRepository tvShowRepository;

    private LiveData<ArrayList<MovieData>> moviesLiveData;
    private LiveData<ArrayList<TvShowData>> tvShowsLiveData;

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void init() {
        movieRepository = new MovieRepository();
        tvShowRepository = new TvShowRepository();
        moviesLiveData = movieRepository.getMoviesLiveData();
        tvShowsLiveData = tvShowRepository.getTvShowsLiveData();
    }

    /**
     * Call repository to get similar movies and update to liveData
     *
     * @param movieId movie Id
     * @param page    target page
     */
    public void getSimilarMovies(long movieId, int page) {
        movieRepository.getSimilarMovies(movieId, page);
    }


    /**
     * Call repository to get similar tvShows and update to liveData
     *
     * @param tvShowId tvShow Id
     * @param page     target page
     */
    public void getSimilarTvShows(long tvShowId, int page) {
        tvShowRepository.getSimilarTvShows(tvShowId, page);
    }


    /**
     * Get the movie liveData to observe it
     *
     * @return
     */
    public LiveData<ArrayList<MovieData>> getMoviesLiveData() {
        return moviesLiveData;
    }

    /**
     * Get the tvShow liveData to observe it
     *
     * @return
     */
    public LiveData<ArrayList<TvShowData>> getTvShowsLiveData() {
        return tvShowsLiveData;
    }
}
