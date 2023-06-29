package com.robinhsueh.nerdia.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.robinhsueh.nerdia.model.database.entity.MovieWatchlistEntity;
import com.robinhsueh.nerdia.model.database.entity.TvShowWatchlistEntity;
import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.model.movie.MoviesResponse;
import com.robinhsueh.nerdia.model.repository.MovieRepository;
import com.robinhsueh.nerdia.model.repository.TvShowRepository;
import com.robinhsueh.nerdia.model.repository.WatchlistRepository;
import com.robinhsueh.nerdia.model.tvshow.TvShowData;
import com.robinhsueh.nerdia.model.tvshow.TvShowsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchlistViewModel extends AndroidViewModel {
    private final String LOG_TAG = "WatchlistViewModel";
    private final WatchlistRepository watchlistRepository;
    private MovieRepository movieRepository;
    private TvShowRepository tvShowRepository;
    private MutableLiveData<ArrayList<MovieData>> movieWatchlistLiveData;
    private MutableLiveData<ArrayList<TvShowData>> tvShowWatchlistLiveData;

    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        watchlistRepository = new WatchlistRepository(application);
    }

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void initLiveData() {
        movieRepository = new MovieRepository();
        tvShowRepository = new TvShowRepository();
        movieWatchlistLiveData = new MutableLiveData<>();
        tvShowWatchlistLiveData = new MutableLiveData<>();
    }

    // region Room Database

    /**
     * Load All Movie Watchlist from room database (using LiveData)
     */
    public LiveData<List<MovieWatchlistEntity>> loadAllMovieWatchlist() {
        return watchlistRepository.getAllMovieWatchlist();
    }

    /**
     * Load All TvShow Watchlist from room database (using LiveData)
     */
    public LiveData<List<TvShowWatchlistEntity>> loadAllTvShowWatchlist() {
        return watchlistRepository.getAllTvShowWatchlist();
    }

    // endregion

    // region Remote Data Source (API)

    // region Get Movie Watchlist

    /**
     * Call repository to get movie watchlist and update to liveData
     *
     * @param userId   Account Id
     * @param session  Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page     target page
     */
    public void getTMDBMovieWatchlist(long userId, String session, String sortMode, int page) {
        Call<MoviesResponse> response = movieRepository.getTMDBMovieWatchlist(userId, session, sortMode, page);
        response.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    MoviesResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        movieWatchlistLiveData.postValue(responseBody.movie_list);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                // post null to liveData
                movieWatchlistLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: \n %s ", t.getMessage()));
            }
        });
    }

    /**
     * Get the liveData to observe it (For Movie Watchlist)
     *
     * @return
     */
    public LiveData<ArrayList<MovieData>> getMovieWatchlistLiveData() {
        return movieWatchlistLiveData;
    }
    // endregion

    // region Get TvShow Watchlist

    /**
     * Call repository to get tvShow watchlist and update to liveData
     *
     * @param userId   Account Id
     * @param session  Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page     target page
     */
    public void getTMDBTvShowWatchlist(long userId, String session, String sortMode, int page) {
        Call<TvShowsResponse> response = tvShowRepository.getTMDBTvShowWatchlist(userId, session, sortMode, page);
        response.enqueue(new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowsResponse> call, @NonNull Response<TvShowsResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    TvShowsResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        tvShowWatchlistLiveData.postValue(responseBody.tvShow_list);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowsResponse> call, @NonNull Throwable t) {
                // post null to liveData
                tvShowWatchlistLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: \n %s ", t.getMessage()));
            }
        });
    }

    /**
     * Get the liveData to observe it (For TvShow Watchlist)
     *
     * @return
     */
    public LiveData<ArrayList<TvShowData>> getTvShowWatchlistLiveData() {
        return tvShowWatchlistLiveData;
    }
    // endregion

    // endregion
}
