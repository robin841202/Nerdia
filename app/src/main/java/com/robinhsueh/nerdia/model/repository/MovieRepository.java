package com.robinhsueh.nerdia.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.robinhsueh.nerdia.BuildConfig;
import com.robinhsueh.nerdia.model.ReviewsResponse;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.WatchProvidersResponse;
import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.model.movie.MovieDetailData;
import com.robinhsueh.nerdia.model.movie.MoviesResponse;
import com.robinhsueh.nerdia.model.service.IMovieService;
import com.robinhsueh.nerdia.model.user.AccountStatesOnMedia;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

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

    private final MutableLiveData<ArrayList<MovieData>> moviesLiveData = new MutableLiveData<>();

    // used when multiple liveData needs to observe different data in same activity or fragment
    private final MutableLiveData<ArrayList<MovieData>> upcomingMoviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<MovieData>> nowPlayingMoviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<MovieData>> trendingMoviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<MovieData>> popularMoviesLiveData = new MutableLiveData<>();

    // endregion

    // region MovieDetail LiveData
    private final MutableLiveData<MovieDetailData> movieDetailLiveData = new MutableLiveData<>();
    // endregion

    // region ReviewData List LiveData
    private final MutableLiveData<ArrayList<ReviewsResponse.ReviewData>> reviewsLiveData = new MutableLiveData<>();
    // endregion

    // region WatchProvidersResponse LiveData
    private final MutableLiveData<WatchProvidersResponse> watchProvidersLiveData = new MutableLiveData<>();
    // endregion

    public MovieRepository() {
        this.language = Locale.TRADITIONAL_CHINESE.toLanguageTag();
        this.region = Locale.TAIWAN.getCountry();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticParameter.TmdbApiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IMovieService.class);
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


    // region MOVIE DETAILS

    /**
     * Get Movie Detail By Movie Id (using LiveData)
     *
     * @param movieId        Movie Id
     * @param subRequestType Can do subRequest in the same time  ex: videos
     * @param videoLanguages Can include multiple languages of video ex:zh-TW,en
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     */
    public void getMovieDetail(long movieId, String subRequestType, String videoLanguages, String imageLanguages) {
        Call<MovieDetailData> call = service.getMovieDetail(movieId, apiKey, language, subRequestType, videoLanguages, imageLanguages);
        call.enqueue(getMovieDetailRequestHandler(movieDetailLiveData));
    }

    /**
     * Get Movie Detail By Movie Id (using LiveData)
     *
     * @param movieId        Movie Id
     * @param subRequestType Can do subRequest in the same time  ex: videos
     * @param videoLanguages Can include multiple languages of video ex:zh-TW,en
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     * @param session        Valid session
     */
    public void getMovieDetail(long movieId, String subRequestType, String videoLanguages, String imageLanguages, String session) {
        Call<MovieDetailData> call = service.getMovieDetail(movieId, apiKey, language, subRequestType, videoLanguages, imageLanguages, session);
        call.enqueue(getMovieDetailRequestHandler(movieDetailLiveData));
    }

    /**
     * (private) Get MovieDetail Request Handler (using LiveData)
     *
     * @param movieDetailLiveData live data
     * @return Request Handler
     */
    private Callback<MovieDetailData> getMovieDetailRequestHandler(MutableLiveData<MovieDetailData> movieDetailLiveData) {
        return new Callback<MovieDetailData>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetailData> call, @NonNull Response<MovieDetailData> response) {
                if (response.isSuccessful()) { // Request successfully
                    MovieDetailData movieDetailData = response.body();
                    if (movieDetailData != null) { // Data exists

                        // region Handle rated score in accountStates
                        if (movieDetailData.getAccountStatesOnMedia() != null) {
                            if (movieDetailData.getAccountStatesOnMedia().getRated() instanceof LinkedTreeMap<?, ?>) { // rate score existed
                                Type type = new TypeToken<AccountStatesOnMedia.Rated>() {
                                }.getType();
                                Gson gson = new Gson();
                                AccountStatesOnMedia.Rated ratedObj = gson.fromJson(gson.toJson(movieDetailData.getAccountStatesOnMedia().getRated()), type);
                                double score = ratedObj.score;
                                // Set the score
                                movieDetailData.getAccountStatesOnMedia().setScore(score);
                            } else { // rate score not existed, type will be Boolean
                                // Set the score to negative
                                movieDetailData.getAccountStatesOnMedia().setScore(-1);
                            }
                        }
                        // endregion

                        // post result data to liveData
                        movieDetailLiveData.postValue(movieDetailData);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetailData> call, @NonNull Throwable t) {
                // post null to liveData
                movieDetailLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: getMovieDetail,\n %s ", t.getMessage()));
            }
        };
    }

    /***
     * Get Movie Detail Live Data
     * @return
     */
    public MutableLiveData<MovieDetailData> getMovieDetailLiveData() {
        return movieDetailLiveData;
    }

    // endregion

    // region MOVIES RESPONSE

    // region UPCOMING MOVIES

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

    /***
     * Get Movies Response Live Data (For Upcoming Movies)
     * @return
     */
    public MutableLiveData<ArrayList<MovieData>> getUpcomingMoviesLiveData() {
        return upcomingMoviesLiveData;
    }

    // endregion

    // region NOW-PLAYING MOVIES

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

    /***
     * Get Movies Response Live Data (For Now-Playing Movies)
     * @return
     */
    public MutableLiveData<ArrayList<MovieData>> getNowPlayingMoviesLiveData() {
        return nowPlayingMoviesLiveData;
    }

    // endregion

    // region TRENDING MOVIES

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

    /***
     * Get Movies Response Live Data (For Trending Movies)
     * @return
     */
    public MutableLiveData<ArrayList<MovieData>> getTrendingMoviesLiveData() {
        return trendingMoviesLiveData;
    }

    // endregion

    // region POPULAR MOVIES

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

    /***
     * Get Movies Response Live Data (For Popular Movies)
     * @return
     */
    public MutableLiveData<ArrayList<MovieData>> getPopularMoviesLiveData() {
        return popularMoviesLiveData;
    }

    // endregion

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
     * Get Similar Movies (using LiveData)
     *
     * @param movieId Movie Id
     * @param page    target page
     */
    public void getSimilarMovies(long movieId, int page) {
        Call<MoviesResponse> call = service.getSimilarMovies(movieId, apiKey, page, language);
        Callback<MoviesResponse> requestHandler = getMoviesResponseRequestHandler(moviesLiveData);
        call.enqueue(requestHandler);
    }

    /**
     * Get Movie Watchlist on TMDB (using LiveData)
     *
     * @param userId   Account Id
     * @param session  Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page     target page
     */
    public void getTMDBMovieWatchlist(long userId, String session, String sortMode, int page) {
        Call<MoviesResponse> call = service.getTMDBMovieWatchlist(userId, apiKey, session, sortMode, page, language);
        Callback<MoviesResponse> requestHandler = getMoviesResponseRequestHandler(moviesLiveData);
        call.enqueue(requestHandler);
    }

    /**
     * Get Movies rated by user on TMDB (using LiveData)
     *
     * @param userId   Account Id
     * @param session  Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page     target page
     */
    public void getTMDBRatedMovies(long userId, String session, String sortMode, int page) {
        Call<MoviesResponse> call = service.getTMDBRatedMovies(userId, apiKey, session, sortMode, page, language);
        Callback<MoviesResponse> requestHandler = getMoviesResponseRequestHandler(moviesLiveData);
        call.enqueue(requestHandler);
    }

    /**
     * Discover Movies (using LiveData)
     *
     * @param page          target page
     * @param includeGenres Comma separated value of genre ids that you want to include in the results.
     */
    public void discoverMovies(int page, String includeGenres) {
        Call<MoviesResponse> call = service.discoverMovies(apiKey, page, language, region, includeGenres);
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

    /***
     * Get Movies Response Live Data
     * @return
     */
    public MutableLiveData<ArrayList<MovieData>> getMoviesLiveData() {
        return moviesLiveData;
    }

    // endregion

    // region REVIEWS RESPONSE

    /**
     * Get Movie reviews on TMDB (using LiveData)
     *
     * @param movieId Movie Id
     * @param page    target page
     */
    public void getTMDBMovieReviews(long movieId, int page) {
        Call<ReviewsResponse> call = service.getTMDBMovieReviews(movieId, apiKey, page);
        Callback<ReviewsResponse> requestHandler = new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReviewsResponse> call, @NonNull Response<ReviewsResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    ReviewsResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        reviewsLiveData.postValue(responseBody.review_list);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewsResponse> call, @NonNull Throwable t) {
                // post null to liveData
                reviewsLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: \n %s ", t.getMessage()));
            }
        };
        call.enqueue(requestHandler);
    }

    /***
     * Get Reviews Response Live Data
     * @return
     */
    public MutableLiveData<ArrayList<ReviewsResponse.ReviewData>> getReviewsLiveData() {
        return reviewsLiveData;
    }

    // endregion

    // region WATCH PROVIDER RESPONSE

    /**
     * Get Movie watch provider (using LiveData)
     *
     * @param movieId Movie Id
     */
    public void getWatchProviderByMovie(long movieId) {
        Call<WatchProvidersResponse> call = service.getWatchProviderByMovie(movieId, apiKey);
        Callback<WatchProvidersResponse> requestHandler = new Callback<WatchProvidersResponse>() {
            @Override
            public void onResponse(@NonNull Call<WatchProvidersResponse> call, @NonNull Response<WatchProvidersResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    WatchProvidersResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        watchProvidersLiveData.postValue(responseBody);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<WatchProvidersResponse> call, @NonNull Throwable t) {
                // post null to liveData
                watchProvidersLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: \n %s ", t.getMessage()));
            }
        };
        call.enqueue(requestHandler);
    }

    /***
     * Get WatchProviders Response Live Data
     * @return
     */
    public MutableLiveData<WatchProvidersResponse> getWatchProvidersLiveData() {
        return watchProvidersLiveData;
    }

    // endregion


    // endregion
}
