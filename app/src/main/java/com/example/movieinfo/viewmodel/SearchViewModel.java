package com.example.movieinfo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.person.PersonData;
import com.example.movieinfo.model.repository.MovieRepository;
import com.example.movieinfo.model.repository.PersonRepository;
import com.example.movieinfo.model.repository.TvShowRepository;
import com.example.movieinfo.model.tvshow.TvShowData;

import java.util.ArrayList;

public class SearchViewModel extends ViewModel {
    private MovieRepository movieRepository;
    private TvShowRepository tvShowRepository;
    private PersonRepository personRepository;

    private LiveData<ArrayList<MovieData>> moviesLiveData;
    private LiveData<ArrayList<TvShowData>> tvShowsLiveData;
    private LiveData<ArrayList<PersonData>> peopleLiveData;

    /**
     * Initialize ViewModel, Only call this when you need a new ViewModel instead of getting shared ViewModel
     */
    public void init() {
        movieRepository = new MovieRepository();
        tvShowRepository = new TvShowRepository();
        personRepository = new PersonRepository();
        moviesLiveData = movieRepository.getMoviesLiveData();
        tvShowsLiveData = tvShowRepository.getTvShowsLiveData();
        peopleLiveData = personRepository.getPeopleLiveData();
    }

    // region Search Movies

    /**
     * Call repository to get searching movies and update to liveData
     *
     * @param keyword searching keyword
     * @param page    target page
     */
    public void searchMovies(String keyword, int page) {
        movieRepository.searchMovies(keyword, page);
    }

    // endregion

    // region Search TvShows

    /**
     * Call repository to get searching tvShows and update to liveData
     *
     * @param keyword searching keyword
     * @param page    target page
     */
    public void searchTvShows(String keyword, int page) {
        tvShowRepository.searchTvShows(keyword, page);
    }

    // endregion


    // region Search People

    /**
     * Call repository to get searching people and update to liveData
     *
     * @param keyword searching keyword
     * @param page    target page
     */
    public void searchPeople(String keyword, int page) {
        personRepository.searchPeople(keyword, page);
    }

    // endregion

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

    /**
     * Get the People liveData to observe it
     *
     * @return
     */
    public LiveData<ArrayList<PersonData>> getPeopleLiveData() {
        return peopleLiveData;
    }
}
