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
     * @param movieId        Movie Id
     * @param subRequestType Can do subRequest in the same time  ex: videos
     * @param videoLanguages Can include multiple languages of video ex:zh-TW,en
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     */
    public void getMovieDetail(long movieId, String subRequestType, String videoLanguages, String imageLanguages) {
        repository.getMovieDetail(movieId, subRequestType, videoLanguages, imageLanguages);
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
