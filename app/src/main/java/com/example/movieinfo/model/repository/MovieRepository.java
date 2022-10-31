package com.example.movieinfo.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieinfo.BuildConfig;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.movie.MoviesResponse;
import com.example.movieinfo.model.service.IMovieService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private final String LOG_TAG = "MovieRepository";
    private final IMovieService service;
    private final String apiKey = BuildConfig.TMDB_API_KEY;
    private String language;
    private String region;
    // set default mediaType to movie
    private final String mediaType = StaticParameter.MediaType.MOVIE;

    // region MovieData List LiveData

    private MutableLiveData<ArrayList<MovieData>> moviesLiveData;

    // used when multiple liveData needs to observe different data in same activity or fragment
    private MutableLiveData<ArrayList<MovieData>> upcomingMoviesLiveData;
    private MutableLiveData<ArrayList<MovieData>> nowPlayingMoviesLiveData;
    private MutableLiveData<ArrayList<MovieData>> trendingMoviesLiveData;
    private MutableLiveData<ArrayList<MovieData>> popularMoviesLiveData;

    // endregion

    // region MovieDetail LiveData
    private MutableLiveData<MovieDetailData> movieDetailLiveData;
    // endregion

    public MovieRepository() {
        this.language = "zh-TW";
        this.region = "TW";

        // Initialize LiveData
        initLiveData();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IMovieService.class);
    }

    /**
     * Initialize every LiveData
     */
    private void initLiveData(){
        moviesLiveData = new MutableLiveData<>();
        upcomingMoviesLiveData = new MutableLiveData<>();
        nowPlayingMoviesLiveData = new MutableLiveData<>();
        trendingMoviesLiveData = new MutableLiveData<>();
        popularMoviesLiveData = new MutableLiveData<>();
        movieDetailLiveData = new MutableLiveData<>();
    }


    public void setLanguage(String language) {
        this.language = language;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    // region MVC methods [without viewModel] (Deprecated)

    /**
     * Get Upcoming Movies (Deprecated)
     *
     * @param page             target page
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     */
    /*public void getUpcomingMovies(int page,
                                  Object objectToInvokeOn,
                                  Method onSuccess,
                                  Method onError) {
        Call<MoviesResponse> call = service.getUpcomingMovies(apiKey, page, language, region);
        Callback<MoviesResponse> requestHandler = getRequestHandler(objectToInvokeOn, onSuccess, onError);
        call.enqueue(requestHandler);

    }*/


    /**
     * Get Now-Playing Movies (Deprecated)
     *
     * @param page             target page
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     */
    /*public void getNowPlayingMovies(int page,
                                    Object objectToInvokeOn,
                                    Method onSuccess,
                                    Method onError) {
        Call<MoviesResponse> call = service.getNowPlayingMovies(apiKey, page, language, region);
        Callback<MoviesResponse> requestHandler = getRequestHandler(objectToInvokeOn, onSuccess, onError);
        call.enqueue(requestHandler);
    }*/

    /**
     * Get Trending Movies (Deprecated)
     *
     * @param timeWindow       weekly or daily trending: "day", "week"
     * @param page             target page
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     */
    /*public void getTrendingMovies(String timeWindow,
                                  int page,
                                  Object objectToInvokeOn,
                                  Method onSuccess,
                                  Method onError) {
        Call<MoviesResponse> call = service.getTrendingMedia(mediaType, timeWindow, apiKey, page, language, region);
        Callback<MoviesResponse> requestHandler = getRequestHandler(objectToInvokeOn, onSuccess, onError);
        call.enqueue(requestHandler);
    }*/


    /**
     * Get Popular Movies (Deprecated)
     *
     * @param page             target page
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     */
    /*public void getPopularMovies(int page,
                                 Object objectToInvokeOn,
                                 Method onSuccess,
                                 Method onError) {
        Call<MoviesResponse> call = service.getPopularMovies(apiKey, page, language, region);
        Callback<MoviesResponse> requestHandler = getRequestHandler(objectToInvokeOn, onSuccess, onError);
        call.enqueue(requestHandler);
    }*/

    /**
     * Search Movies By Keyword (Deprecated)
     *
     * @param keyWord          keyword for searching
     * @param page             target page
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     */
    /*public void searchMovies(String keyWord,
                             int page,
                             Object objectToInvokeOn,
                             Method onSuccess,
                             Method onError) {
        Call<MoviesResponse> call = service.searchMovies(apiKey, keyWord, page, language, region);
        Callback<MoviesResponse> requestHandler = getRequestHandler(objectToInvokeOn, onSuccess, onError);
        call.enqueue(requestHandler);
    }*/


    /**
     * (private) Get Request Handler (Deprecated)
     *
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     * @return Request Handler
     */
    /*private Callback<MoviesResponse> getRequestHandler(Object objectToInvokeOn, Method onSuccess, Method onError) {
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
    }*/


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

    // endregion

    // region MVVM architecture using LiveData


    /**
     * Get Upcoming Movies (using LiveData)
     *
     * @param page target page
     */
    public void getUpcomingMovies(int page) {
        Call<MoviesResponse> call = service.getUpcomingMovies(apiKey, page, language, region);
        Callback<MoviesResponse> requestHandler = getMoviesResponseRequestHandler(upcomingMoviesLiveData);
        call.enqueue(requestHandler);
    }

    /**
     * Get Now-Playing Movies (using LiveData)
     *
     * @param page target page
     */
    public void getNowPlayingMovies(int page) {
        Call<MoviesResponse> call = service.getNowPlayingMovies(apiKey, page, language, region);
        Callback<MoviesResponse> requestHandler = getMoviesResponseRequestHandler(nowPlayingMoviesLiveData);
        call.enqueue(requestHandler);
    }

    /**
     * Get Trending Movies (using LiveData)
     *
     * @param timeWindow weekly or daily trending: "day", "week"
     * @param page       target page
     */
    public void getTrendingMovies(String timeWindow, int page) {
        Call<MoviesResponse> call = service.getTrendingMedia(mediaType, timeWindow, apiKey, page, language, region);
        Callback<MoviesResponse> requestHandler = getMoviesResponseRequestHandler(trendingMoviesLiveData);
        call.enqueue(requestHandler);
    }

    /**
     * Get Popular Movies (using LiveData)
     *
     * @param page target page
     */
    public void getPopularMovies(int page) {
        Call<MoviesResponse> call = service.getPopularMovies(apiKey, page, language, region);
        Callback<MoviesResponse> requestHandler = getMoviesResponseRequestHandler(popularMoviesLiveData);
        call.enqueue(requestHandler);
    }

    /**
     * Search Movies By Keyword (using LiveData)
     *
     * @param keyWord keyword for searching
     * @param page    target page
     */
    public void searchMovies(String keyWord, int page) {
        Call<MoviesResponse> call = service.searchMovies(apiKey, keyWord, page, language, region);
        Callback<MoviesResponse> requestHandler = getMoviesResponseRequestHandler(moviesLiveData);
        call.enqueue(requestHandler);
    }

    /**
     * (private) Get Request Handler (using LiveData)
     *
     * @param moviesLiveData live data
     * @return Request Handler
     */
    private Callback<MoviesResponse> getMoviesResponseRequestHandler(MutableLiveData<ArrayList<MovieData>> moviesLiveData) {
        return new Callback<MoviesResponse>() {
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
        };
    }


    /**
     * Get Movie Detail By Movie Id (using LiveData)
     *
     * @param movieId        Movie Id
     * @param subRequestType Can do subRequest in the same time  ex: videos
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
     * Get Movies Response Live Data
     * @return
     */
    public MutableLiveData<ArrayList<MovieData>> getMoviesLiveData() {
        return moviesLiveData;
    }

    /***
     * Get Movies Response Live Data (For Upcoming Movies)
     * @return
     */
    public MutableLiveData<ArrayList<MovieData>> getUpcomingMoviesLiveData() {
        return upcomingMoviesLiveData;
    }

    /***
     * Get Movies Response Live Data (For Now-Playing Movies)
     * @return
     */
    public MutableLiveData<ArrayList<MovieData>> getNowPlayingMoviesLiveData() {
        return nowPlayingMoviesLiveData;
    }

    /***
     * Get Movies Response Live Data (For Trending Movies)
     * @return
     */
    public MutableLiveData<ArrayList<MovieData>> getTrendingMoviesLiveData() {
        return trendingMoviesLiveData;
    }

    /***
     * Get Movies Response Live Data (For Popular Movies)
     * @return
     */
    public MutableLiveData<ArrayList<MovieData>> getPopularMoviesLiveData() {
        return popularMoviesLiveData;
    }


    /***
     * Get Movie Detail Live Data
     * @return
     */
    public MutableLiveData<MovieDetailData> getMovieDetailLiveData() {
        return movieDetailLiveData;
    }
    // endregion
}
