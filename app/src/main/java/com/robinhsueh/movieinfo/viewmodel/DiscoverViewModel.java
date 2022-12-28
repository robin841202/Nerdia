package com.robinhsueh.movieinfo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.robinhsueh.movieinfo.model.GenreData;
import com.robinhsueh.movieinfo.model.movie.MovieData;
import com.robinhsueh.movieinfo.model.repository.GenreRepository;
import com.robinhsueh.movieinfo.model.repository.MovieRepository;
import com.robinhsueh.movieinfo.model.repository.TvShowRepository;
import com.robinhsueh.movieinfo.model.tvshow.TvShowData;

import java.util.ArrayList;

public class DiscoverViewModel extends ViewModel {
    private GenreRepository genreRepository;
    private MovieRepository movieRepository;
    private TvShowRepository tvShowRepository;

    private LiveData<ArrayList<GenreData>> genresLiveData;
    private LiveData<ArrayList<MovieData>> moviesLiveData;
    private LiveData<ArrayList<TvShowData>> tvShowsLiveData;

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void init() {
        genreRepository = new GenreRepository();
        movieRepository = new MovieRepository();
        tvShowRepository = new TvShowRepository();
        genresLiveData = genreRepository.getGenresLiveData();
        moviesLiveData = movieRepository.getMoviesLiveData();
        tvShowsLiveData = tvShowRepository.getTvShowsLiveData();
    }

    /**
     * Call repository to get movie genres and update to liveData
     */
    public void getMovieGenres() {
        genreRepository.getMovieGenres();
    }

    /**
     * Call repository to get tvShow genres and update to liveData
     */
    public void getTvShowGenres() {
        genreRepository.getTvShowGenres();
    }

    /**
     * Call repository to discover movies and update to liveData
     * @param page    target page
     * @param includeGenres Comma separated value of genre ids that you want to include in the results.
     */
    public void discoverMovies(int page, String includeGenres) {
        movieRepository.discoverMovies(page, includeGenres);
    }

    /**
     * Call repository to discover tvShows and update to liveData
     * @param page    target page
     * @param includeGenres Comma separated value of genre ids that you want to include in the results.
     */
    public void discoverTvShows(int page, String includeGenres) {
        tvShowRepository.discoverTvShows(page, includeGenres);
    }

    /**
     * Get the Genres liveData to observe it
     *
     * @return
     */
    public LiveData<ArrayList<GenreData>> getGenresLiveData() {
        return genresLiveData;
    }


    /**
     * Get the Movies liveData to observe it
     *
     * @return
     */
    public LiveData<ArrayList<MovieData>> getMoviesLiveData() {
        return moviesLiveData;
    }

    /**
     * Get the TvShows liveData to observe it
     *
     * @return
     */
    public LiveData<ArrayList<TvShowData>> getTvShowsLiveData() {
        return tvShowsLiveData;
    }
}
