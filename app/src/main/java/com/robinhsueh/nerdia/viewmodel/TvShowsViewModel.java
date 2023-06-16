package com.robinhsueh.nerdia.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.model.repository.TvShowRepository;
import com.robinhsueh.nerdia.model.tvshow.TvShowData;

import java.util.ArrayList;

public class TvShowsViewModel extends ViewModel {
    private TvShowRepository repository;

    // used when multiple liveData needs to observe different data in same activity or fragment
    private LiveData<ArrayList<TvShowData>> popularTvShowsLiveData;
    private LiveData<ArrayList<TvShowData>> trendingTvShowsLiveData;
    private LiveData<ArrayList<TvShowData>> netflixTvShowsLiveData;
    private LiveData<ArrayList<TvShowData>> disneyTvShowsLiveData;
    private LiveData<ArrayList<TvShowData>> catchplayTvShowsLiveData;
    private LiveData<ArrayList<TvShowData>> primeTvShowsLiveData;

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void init() {
        repository = new TvShowRepository();
        trendingTvShowsLiveData = repository.getTrendingTvShowsLiveData();
        popularTvShowsLiveData = repository.getPopularTvShowsLiveData();
        netflixTvShowsLiveData = repository.getNetflixTvShowsLiveData();
        disneyTvShowsLiveData = repository.getDisneyTvShowsLiveData();
        catchplayTvShowsLiveData = repository.getCatchplayTvShowsLiveData();
        primeTvShowsLiveData = repository.getPrimeTvShowsLiveData();
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

    // region Netflix TvShows

    /**
     * Call repository to get netflix tvShows and update to liveData
     *
     * @param page target page
     */
    public void getNetflixTvShows(int page) {
        repository.getNetflixTvShows(page);
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
        repository.getDisneyTvShows(page);
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
        repository.getCatchplayTvShows(page);
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
        repository.getPrimeTvShows(page);
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
}
