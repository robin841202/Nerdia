package com.robinhsueh.nerdia.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.model.movie.MoviesResponse;
import com.robinhsueh.nerdia.model.repository.MovieRepository;
import com.robinhsueh.nerdia.model.repository.TvShowRepository;
import com.robinhsueh.nerdia.model.tvshow.TvShowData;
import com.robinhsueh.nerdia.model.tvshow.TvShowsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatedListViewModel extends AndroidViewModel {
    private final String LOG_TAG = "RatedListViewModel";
    private MovieRepository movieRepository;
    private TvShowRepository tvShowRepository;
    private MutableLiveData<ArrayList<MovieData>> movieRatedListLiveData;
    private MutableLiveData<ArrayList<TvShowData>> tvShowRatedListLiveData;

    public RatedListViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void initLiveData() {
        movieRepository = new MovieRepository();
        tvShowRepository = new TvShowRepository();
        movieRatedListLiveData = new MutableLiveData<>();
        tvShowRatedListLiveData = new MutableLiveData<>();
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
        Call<MoviesResponse> response = movieRepository.getTMDBRatedMovies(userId, session, sortMode, page);
        response.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    MoviesResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        movieRatedListLiveData.postValue(responseBody.movie_list);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                // post null to liveData
                movieRatedListLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: \n %s ", t.getMessage()));
            }
        });
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
        Call<TvShowsResponse> response = tvShowRepository.getTMDBRatedTvShows(userId, session, sortMode, page);
        response.enqueue(new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowsResponse> call, @NonNull Response<TvShowsResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    TvShowsResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        tvShowRatedListLiveData.postValue(responseBody.tvShow_list);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowsResponse> call, @NonNull Throwable t) {
                // post null to liveData
                tvShowRatedListLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: \n %s ", t.getMessage()));
            }
        });
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
