package com.example.movieinfo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieinfo.model.OmdbData;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.repository.MovieRepository;
import com.example.movieinfo.model.repository.OmdbRepository;

public class MovieDetailViewModel extends ViewModel {
    private MovieRepository repository;
    private OmdbRepository omdbRepository;
    private LiveData<MovieDetailData> liveData;
    private LiveData<OmdbData> omdbLiveData;

    /**
     * Initialize ViewModel, Only call this when you need a new ViewModel instead of getting shared ViewModel
     */
    public void init() {
        repository = new MovieRepository();
        omdbRepository = new OmdbRepository();
        liveData = repository.getMovieDetailLiveData();
        omdbLiveData = omdbRepository.getOmdbLiveData();
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
     * Call repository to get omdb data by IMDB Id and update to liveData
     *
     * @param imdbId IMDB Id
     */
    public void getDataByImdbId(String imdbId) {
        omdbRepository.getDataByImdbId(imdbId);
    }

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<MovieDetailData> getMovieDetailLiveData() {
        return liveData;
    }


    /**
     * Get the OMDB liveData to observe it
     *
     * @return
     */
    public LiveData<OmdbData> getOmdbLiveData() {
        return omdbLiveData;
    }

}
