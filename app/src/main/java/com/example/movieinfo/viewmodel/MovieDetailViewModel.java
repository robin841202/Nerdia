package com.example.movieinfo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.repository.MovieRepository;

public class MovieDetailViewModel extends ViewModel {
    private MovieRepository repository;
    private LiveData<MovieDetailData> liveData;

    /**
     * Initialize ViewModel, Only call this when you need a new ViewModel instead of getting shared ViewModel
     */
    public void init() {
        repository = new MovieRepository();
        liveData = repository.getMovieDetailLiveData();
    }

    /**
     * Call repository to get movie detail and update to liveData
     *
     * @param movieId
     * @param subRequestType
     * @param videoLanguages
     */
    public void getMovieDetail(long movieId, String subRequestType, String videoLanguages) {
        repository.getMovieDetail(movieId, subRequestType, videoLanguages);
    }

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<MovieDetailData> getMovieDetailLiveData() {
        return liveData;
    }

}
