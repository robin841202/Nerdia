package com.robinhsueh.nerdia.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.model.movie.MoviesResponse;
import com.robinhsueh.nerdia.model.person.PeopleResponse;
import com.robinhsueh.nerdia.model.person.PersonData;
import com.robinhsueh.nerdia.model.repository.MovieRepository;
import com.robinhsueh.nerdia.model.repository.PersonRepository;
import com.robinhsueh.nerdia.model.repository.TvShowRepository;
import com.robinhsueh.nerdia.model.tvshow.TvShowData;
import com.robinhsueh.nerdia.model.tvshow.TvShowsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {
    private final String LOG_TAG = "SearchViewModel";
    private MovieRepository movieRepository;
    private TvShowRepository tvShowRepository;
    private PersonRepository personRepository;

    private MutableLiveData<ArrayList<MovieData>> moviesLiveData;
    private MutableLiveData<ArrayList<TvShowData>> tvShowsLiveData;
    private MutableLiveData<ArrayList<PersonData>> peopleLiveData;

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void init() {
        movieRepository = new MovieRepository();
        tvShowRepository = new TvShowRepository();
        personRepository = new PersonRepository();
        moviesLiveData = new MutableLiveData<>();
        tvShowsLiveData = new MutableLiveData<>();
        peopleLiveData = new MutableLiveData<>();
    }

    // region Search Movies

    /**
     * Call repository to get searching movies and update to liveData
     *
     * @param keyword searching keyword
     * @param page    target page
     */
    public void searchMovies(String keyword, int page) {
        Call<MoviesResponse> response = movieRepository.searchMovies(keyword, page);
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

    // region Search TvShows

    /**
     * Call repository to get searching tvShows and update to liveData
     *
     * @param keyword searching keyword
     * @param page    target page
     */
    public void searchTvShows(String keyword, int page) {
        Call<TvShowsResponse> response = tvShowRepository.searchTvShows(keyword, page);
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

    // endregion

    // region Search People

    /**
     * Call repository to get searching people and update to liveData
     *
     * @param keyword searching keyword
     * @param page    target page
     */
    public void searchPeople(String keyword, int page) {
        Call<PeopleResponse> response = personRepository.searchPeople(keyword, page);
        response.enqueue(new Callback<PeopleResponse>() {
            @Override
            public void onResponse(@NonNull Call<PeopleResponse> call, @NonNull Response<PeopleResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    PeopleResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        peopleLiveData.postValue(responseBody.people_list);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PeopleResponse> call, @NonNull Throwable t) {
                // post null to liveData
                peopleLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: \n %s ", t.getMessage()));
            }
        });
    }

    /**
     * Get the People liveData to observe it
     *
     * @return
     */
    public LiveData<ArrayList<PersonData>> getPeopleLiveData() {
        return peopleLiveData;
    }

    // endregion

}
