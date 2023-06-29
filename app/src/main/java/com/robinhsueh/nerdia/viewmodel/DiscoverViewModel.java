package com.robinhsueh.nerdia.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.robinhsueh.nerdia.model.GenreData;
import com.robinhsueh.nerdia.model.GenresResponse;
import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.model.movie.MoviesResponse;
import com.robinhsueh.nerdia.model.repository.GenreRepository;
import com.robinhsueh.nerdia.model.repository.MovieRepository;
import com.robinhsueh.nerdia.model.repository.TvShowRepository;
import com.robinhsueh.nerdia.model.tvshow.TvShowData;
import com.robinhsueh.nerdia.model.tvshow.TvShowsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverViewModel extends ViewModel {
    private final String LOG_TAG = "DiscoverViewModel";
    private GenreRepository genreRepository;
    private MovieRepository movieRepository;
    private TvShowRepository tvShowRepository;

    private MutableLiveData<ArrayList<GenreData>> genresLiveData;
    private MutableLiveData<ArrayList<MovieData>> moviesLiveData;
    private MutableLiveData<ArrayList<TvShowData>> tvShowsLiveData;

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void init() {
        genreRepository = new GenreRepository();
        movieRepository = new MovieRepository();
        tvShowRepository = new TvShowRepository();

        genresLiveData = new MutableLiveData<>();
        moviesLiveData = new MutableLiveData<>();
        tvShowsLiveData = new MutableLiveData<>();
    }

    // region Get Genres

    // region Movie Genres

    /**
     * Call repository to get movie genres and update to liveData
     */
    public void getMovieGenres() {
        Call<GenresResponse> response = genreRepository.getMovieGenres();
        Callback<GenresResponse> requestHandler = getGenresResponseRequestHandler(genresLiveData);
        response.enqueue(requestHandler);
    }

    // endregion

    // region TvShow Genres

    /**
     * Call repository to get tvShow genres and update to liveData
     */
    public void getTvShowGenres() {
        Call<GenresResponse> response = genreRepository.getTvShowGenres();
        Callback<GenresResponse> requestHandler = getGenresResponseRequestHandler(genresLiveData);
        response.enqueue(requestHandler);
    }

    // endregion

    /**
     * (private) Get Request Handler (using LiveData)
     *
     * @param genresLiveData live data
     * @return Request Handler
     */
    private Callback<GenresResponse> getGenresResponseRequestHandler(MutableLiveData<ArrayList<GenreData>> genresLiveData) {
        return new Callback<GenresResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenresResponse> call, @NonNull Response<GenresResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    if (response.body() != null) { // Data exists
                        // post result data to liveData
                        genresLiveData.postValue(response.body().genres);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenresResponse> call, @NonNull Throwable t) {
                // post null to liveData
                genresLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: \n %s ", t.getMessage()));
            }
        };
    }


    /**
     * Get the Genres liveData to observe it
     *
     * @return
     */
    public LiveData<ArrayList<GenreData>> getGenresLiveData() {
        return genresLiveData;
    }

    // endregion

    // region Discover Movies

    /**
     * Call repository to discover movies and update to liveData
     *
     * @param page          target page
     * @param includeGenres Comma separated value of genre ids that you want to include in the results.
     */
    public void discoverMovies(int page, String includeGenres) {
        Call<MoviesResponse> response = movieRepository.discoverMoviesByGenres(page, includeGenres);
        response.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    MoviesResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        moviesLiveData.postValue(responseBody.movie_list);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                // post null to liveData
                moviesLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: \n %s ", t.getMessage()));
            }
        });
    }

    /**
     * Get the Movies liveData to observe it
     *
     * @return
     */
    public LiveData<ArrayList<MovieData>> getMoviesLiveData() {
        return moviesLiveData;
    }

    // endregion

    // region Discover TvShows

    /**
     * Call repository to discover tvShows and update to liveData
     *
     * @param page          target page
     * @param includeGenres Comma separated value of genre ids that you want to include in the results.
     */
    public void discoverTvShows(int page, String includeGenres) {
        Call<TvShowsResponse> response = tvShowRepository.discoverTvShows(page, includeGenres);
        response.enqueue(new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowsResponse> call, @NonNull Response<TvShowsResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    TvShowsResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        tvShowsLiveData.postValue(responseBody.tvShow_list);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowsResponse> call, @NonNull Throwable t) {
                // post null to liveData
                tvShowsLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: \n %s ", t.getMessage()));
            }
        });
    }

    /**
     * Get the TvShows liveData to observe it
     *
     * @return
     */
    public LiveData<ArrayList<TvShowData>> getTvShowsLiveData() {
        return tvShowsLiveData;
    }

    //endregion

}
