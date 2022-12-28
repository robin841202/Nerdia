package com.robinhsueh.movieinfo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.robinhsueh.movieinfo.model.repository.TvShowRepository;
import com.robinhsueh.movieinfo.model.tvshow.TvShowData;

import java.util.ArrayList;

public class TvShowsViewModel extends ViewModel {
    private TvShowRepository repository;

    // used when multiple liveData needs to observe different data in same activity or fragment
    private LiveData<ArrayList<TvShowData>> popularTvShowsLiveData;
    private LiveData<ArrayList<TvShowData>> trendingTvShowsLiveData;

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void init() {
        repository = new TvShowRepository();
        trendingTvShowsLiveData = repository.getTrendingTvShowsLiveData();
        popularTvShowsLiveData = repository.getPopularTvShowsLiveData();
    }


    // region Popular TvShows

    /**
     * Call repository to get popular tvShows and update to liveData
     *
     * @param page target page
     */
    public void getPopularTvShows(int page) {
        repository.getPopularTvShows(page);
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
        repository.getTrendingTvShows(timeWindow, page);
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

}
