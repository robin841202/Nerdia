package com.example.movieinfo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.repository.MovieRepository;
import com.example.movieinfo.model.repository.TvShowRepository;
import com.example.movieinfo.model.tvshow.TvShowData;

import java.util.ArrayList;

public class RatedListViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private TvShowRepository tvShowRepository;
    private LiveData<ArrayList<MovieData>> movieRatedListLiveData;
    private LiveData<ArrayList<TvShowData>> tvShowRatedListLiveData;

    public RatedListViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void initLiveData() {
        movieRepository = new MovieRepository();
        tvShowRepository = new TvShowRepository();
        movieRatedListLiveData = movieRepository.getMoviesLiveData();
        tvShowRatedListLiveData = tvShowRepository.getTvShowsLiveData();
    }

    // region Remote Data Source (API)

    // region Rated Movies List

    /**
     * Call repository to get rated movies and update to liveData
     *
     * @param userId   Account Id
     * @param session  Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page     target page
     */
    public void getTMDBRatedMovies(long userId, String session, String sortMode, int page) {
        movieRepository.getTMDBRatedMovies(userId, session, sortMode, page);
    }

    /**
     * Get the liveData to observe it (For Movie Rated List)
     *
     * @return
     */
    public LiveData<ArrayList<MovieData>> getRatedMoviesLiveData() {
        return movieRatedListLiveData;
    }
    // endregion

    // region Rated TvShows List

    /**
     * Call repository to get rated tvShows and update to liveData
     *
     * @param userId   Account Id
     * @param session  Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page     target page
     */
    public void getTMDBRatedTvShows(long userId, String session, String sortMode, int page) {
        tvShowRepository.getTMDBRatedTvShows(userId, session, sortMode, page);
    }

    /**
     * Get the liveData to observe it (For TvShow Rated List)
     *
     * @return
     */
    public LiveData<ArrayList<TvShowData>> getRatedTvShowsLiveData() {
        return tvShowRatedListLiveData;
    }
    // endregion

    // endregion
}
