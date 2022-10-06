package com.example.movieinfo.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.movie.MoviesResponse;
import com.example.movieinfo.model.service.IMovieService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private final String LOG_TAG = "MovieRepository";
    private final IMovieService service;
    private final String apiKey = "45573754115aa294605178ed2769f617";
    private String language;
    private String region;
    // set default mediaType to movie
    private final String mediaType = StaticParameter.MediaType.MOVIE;

    private MutableLiveData<MovieDetailData> movieDetailLiveData;


    public MovieRepository() {
        this.language = "zh-TW";
        this.region = "TW";

        // Initialize LiveData
        movieDetailLiveData = new MutableLiveData<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IMovieService.class);
    }

    /**
     * Get Upcoming Movies
     *
     * @param page             target page
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     */
    public void getUpcomingMovies(int page,
                                  Object objectToInvokeOn,
                                  Method onSuccess,
                                  Method onError) {
        Call<MoviesResponse> call = service.getUpcomingMovies(apiKey, page, language, region);
        Callback<MoviesResponse> requestHandler = getRequestHandler(objectToInvokeOn, onSuccess, onError);
        call.enqueue(requestHandler);

    }


    /**
     * Get Now-Playing Movies
     *
     * @param page             target page
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     */
    public void getNowPlayingMovies(int page,
                                    Object objectToInvokeOn,
                                    Method onSuccess,
                                    Method onError) {
        Call<MoviesResponse> call = service.getNowPlayingMovies(apiKey, page, language, region);
        Callback<MoviesResponse> requestHandler = getRequestHandler(objectToInvokeOn, onSuccess, onError);
        call.enqueue(requestHandler);
    }


    /**
     * Get Popular Movies
     *
     * @param page             target page
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     */
    public void getPopularMovies(int page,
                                 Object objectToInvokeOn,
                                 Method onSuccess,
                                 Method onError) {
        Call<MoviesResponse> call = service.getPopularMovies(apiKey, page, language, region);
        Callback<MoviesResponse> requestHandler = getRequestHandler(objectToInvokeOn, onSuccess, onError);
        call.enqueue(requestHandler);
    }

    /**
     * Get Trending Movies
     *
     * @param timeWindow       weekly or daily trending: "day", "week"
     * @param page             target page
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     */
    public void getTrendingMovies(String timeWindow,
                                  int page,
                                  Object objectToInvokeOn,
                                  Method onSuccess,
                                  Method onError) {
        Call<MoviesResponse> call = service.getTrendingMedia(mediaType, timeWindow, apiKey, page, language, region);
        Callback<MoviesResponse> requestHandler = getRequestHandler(objectToInvokeOn, onSuccess, onError);
        call.enqueue(requestHandler);
    }


    /**
     * Get Movie Detail By Movie Id (Deprecated)
     *
     * @param movieId          Movie Id
     * @param subRequestType   Can do subRequest in the same time  ex: videos
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     */
    /*public void getMovieDetail(long movieId,
                               String subRequestType,
                               Object objectToInvokeOn,
                               Method onSuccess,
                               Method onError) {
        Call<MovieDetailData> call = service.getMovieDetail(movieId, apiKey, language, subRequestType);
        Callback<MovieDetailData> requestHandler = new Callback<MovieDetailData>() {
            @Override
            public void onResponse(Call<MovieDetailData> call, Response<MovieDetailData> response) {
                if (response.isSuccessful()) { // Request successfully
                    MovieDetailData responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        try {
                            // invoke the callback method with the data
                            onSuccess.invoke(objectToInvokeOn, responseBody);
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else { // No Data exists
                        try {
                            // invoke the callback method
                            onError.invoke(objectToInvokeOn);
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieDetailData> call, Throwable t) {
                try {
                    // invoke the callback method
                    onError.invoke(objectToInvokeOn);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        };
        call.enqueue(requestHandler);
    }*/


    /**
     * Search Movies By Keyword
     *
     * @param keyWord          keyword for searching
     * @param page             target page
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     */
    public void searchMovies(String keyWord,
                             int page,
                             Object objectToInvokeOn,
                             Method onSuccess,
                             Method onError) {
        Call<MoviesResponse> call = service.searchMovies(apiKey, keyWord, page, language, region);
        Callback<MoviesResponse> requestHandler = getRequestHandler(objectToInvokeOn, onSuccess, onError);
        call.enqueue(requestHandler);
    }


    /**
     * (private) Get Request Handler
     *
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     * @return Request Handler
     */
    private Callback<MoviesResponse> getRequestHandler(Object objectToInvokeOn, Method onSuccess, Method onError) {
        return new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    MoviesResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        try {
                            // get the object to invoke on
                            //Object objectToInvokeOn = onSuccess.getDeclaringClass().newInstance();
                            // invoke the callback method with the data
                            onSuccess.invoke(objectToInvokeOn, responseBody.movie_list);
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else { // No Data exists
                        try {
                            // get the object to invoke on
                            //Object objectToInvokeOn = onError.getDeclaringClass().newInstance();
                            // invoke the callback method
                            onError.invoke(objectToInvokeOn);
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                try {
                    // get the object to invoke on
                    //Object objectToInvokeOn = onError.getDeclaringClass().newInstance();
                    // invoke the callback method
                    onError.invoke(objectToInvokeOn);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    public void setLanguage(String language) {
        this.language = language;
    }

    public void setRegion(String region) {
        this.region = region;
    }




    // region MVVM architecture using LiveData


    /**
     * Get Movie Detail By Movie Id (using LiveData)
     *
     * @param movieId          Movie Id
     * @param subRequestType   Can do subRequest in the same time  ex: videos
     */
    public void getMovieDetail(long movieId, String subRequestType) {
        Call<MovieDetailData> call = service.getMovieDetail(movieId, apiKey, language, subRequestType);
        call.enqueue(new Callback<MovieDetailData>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetailData> call, @NonNull Response<MovieDetailData> response) {
                if (response.isSuccessful()) { // Request successfully
                    MovieDetailData responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        movieDetailLiveData.postValue(responseBody);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetailData> call, @NonNull Throwable t) {
                // post null to liveData
                movieDetailLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: getMovieDetail,\n %s ", t.getMessage()));
            }
        });
    }


    /***
     * Get Movie Detail Live Data
     * @return
     */
    public MutableLiveData<MovieDetailData> getMovieDetailLiveData(){
        return movieDetailLiveData;
    }
    // endregion
}
