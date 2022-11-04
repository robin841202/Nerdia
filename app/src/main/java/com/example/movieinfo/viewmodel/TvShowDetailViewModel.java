package com.example.movieinfo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieinfo.model.repository.TvShowRepository;
import com.example.movieinfo.model.tvshow.TvShowDetailData;

public class TvShowDetailViewModel extends ViewModel {
    private TvShowRepository repository;
    private LiveData<TvShowDetailData> liveData;

    /**
     * Initialize ViewModel, Only call this when you need a new ViewModel instead of getting shared ViewModel
     */
    public void init() {
        repository = new TvShowRepository();
        liveData = repository.getTvShowDetailLiveData();
    }

    /**
     * Call repository to get tvShow detail and update to liveData
     *
     * @param tvShowId       TvShow Id
     * @param subRequestType Can do subRequest in the same time  ex: videos
     * @param videoLanguages Can include multiple languages of video ex:zh-TW,en
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     */
    public void getTvShowDetail(long tvShowId, String subRequestType, String videoLanguages, String imageLanguages) {
        repository.getTvShowDetail(tvShowId, subRequestType, videoLanguages, imageLanguages);
    }

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<TvShowDetailData> getTvShowDetailLiveData() {
        return liveData;
    }

}
