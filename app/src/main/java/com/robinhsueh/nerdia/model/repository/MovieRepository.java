package com.robinhsueh.nerdia.model.repository;

import com.robinhsueh.nerdia.BuildConfig;
import com.robinhsueh.nerdia.model.ReviewsResponse;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.WatchProvidersResponse;
import com.robinhsueh.nerdia.model.movie.MovieDetailData;
import com.robinhsueh.nerdia.model.movie.MoviesResponse;
import com.robinhsueh.nerdia.model.service.IMovieService;
import java.util.Locale;
import retrofit2.Call;
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
    public Call<MovieDetailData> getMovieDetail(long movieId, String subRequestType, String videoLanguages, String imageLanguages) {
        return service.getMovieDetail(movieId, apiKey, language, subRequestType, videoLanguages, imageLanguages);
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
    public Call<MovieDetailData> getMovieDetail(long movieId, String subRequestType, String videoLanguages, String imageLanguages, String session) {
        return service.getMovieDetail(movieId, apiKey, language, subRequestType, videoLanguages, imageLanguages, session);
    }

    // endregion

    // region MOVIES RESPONSE

    // region GET UPCOMING MOVIES

    /**
     * Get Upcoming Movies (using LiveData)
     *
     * @param page target page
     */
    public Call<MoviesResponse> getUpcomingMovies(int page) {
        return service.getUpcomingMovies(apiKey, page, language, region);
    }

    // endregion

    // region GET NOW-PLAYING MOVIES

    /**
     * Get Now-Playing Movies (using LiveData)
     *
     * @param page target page
     */
    public Call<MoviesResponse> getNowPlayingMovies(int page) {
        return service.getNowPlayingMovies(apiKey, page, language, region);
    }

    // endregion

    // region GET TRENDING MOVIES

    /**
     * Get Trending Movies (using LiveData)
     *
     * @param timeWindow weekly or daily trending: "day", "week"
     * @param page       target page
     */
    public Call<MoviesResponse> getTrendingMovies(String timeWindow, int page) {
        return service.getTrendingMedia(mediaType, timeWindow, apiKey, page, language, region);
    }

    // endregion

    // region GET POPULAR MOVIES

    /**
     * Get Popular Movies (using LiveData)
     *
     * @param page target page
     */
    public Call<MoviesResponse> getPopularMovies(int page) {
        return service.getPopularMovies(apiKey, page, language, region);
    }

    // endregion

    // region GET NETFLIX MOVIES

    /**
     * Get Netflix Movies (using LiveData)
     *
     * @param page target page
     */
    public Call<MoviesResponse> getNetflixMovies(int page) {
        return service.discoverMovies(apiKey, page, language, region, StaticParameter.WatchProvidersID.NetflixID);
    }

    // endregion

    // region GET DISNEY MOVIES

    /**
     * Get Disney Movies (using LiveData)
     *
     * @param page target page
     */
    public Call<MoviesResponse> getDisneyMovies(int page) {
        return service.discoverMovies(apiKey, page, language, region, StaticParameter.WatchProvidersID.DisneyPlusID);
    }

    // endregion

    // region GET CATCHPLAY MOVIES

    /**
     * Get Catchplay Movies (using LiveData)
     *
     * @param page target page
     */
    public Call<MoviesResponse> getCatchplayMovies(int page) {
        return service.discoverMovies(apiKey, page, language, region, StaticParameter.WatchProvidersID.CatchPlayID);
    }

    // endregion

    // region GET PRIME MOVIES

    /**
     * Get Prime Movies (using LiveData)
     *
     * @param page target page
     */
    public Call<MoviesResponse> getPrimeMovies(int page) {
        return service.discoverMovies(apiKey, page, language, region, StaticParameter.WatchProvidersID.PrimeVideoID);
    }

    // endregion

    // region SEARCH MOVIES
    /**
     * Search Movies By Keyword (using LiveData)
     *
     * @param keyWord keyword for searching
     * @param page    target page
     */
    public Call<MoviesResponse> searchMovies(String keyWord, int page) {
        return service.searchMovies(apiKey, keyWord, page, language, region);
    }
    // endregion

    // region GET SIMILAR MOVIES
    /**
     * Get Similar Movies (using LiveData)
     *
     * @param movieId Movie Id
     * @param page    target page
     */
    public Call<MoviesResponse> getSimilarMovies(long movieId, int page) {
        return service.getSimilarMovies(movieId, apiKey, page, language);
    }
    // endregion

    // region GET MOVIE WATCHLIST
    /**
     * Get Movie Watchlist on TMDB (using LiveData)
     *
     * @param userId   Account Id
     * @param session  Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page     target page
     */
    public Call<MoviesResponse> getTMDBMovieWatchlist(long userId, String session, String sortMode, int page) {
        return service.getTMDBMovieWatchlist(userId, apiKey, session, sortMode, page, language);
    }
    // endregion

    // region GET RATED MOVIES
    /**
     * Get Movies rated by user on TMDB (using LiveData)
     *
     * @param userId   Account Id
     * @param session  Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page     target page
     */
    public Call<MoviesResponse> getTMDBRatedMovies(long userId, String session, String sortMode, int page) {
        return service.getTMDBRatedMovies(userId, apiKey, session, sortMode, page, language);
    }
    // endregion

    // region DISCOVER MOVIES BY GENRES
    /**
     * Discover Movies by Genres (using LiveData)
     *
     * @param page          target page
     * @param includeGenres Comma separated value of genre ids that you want to include in the results.
     */
    public Call<MoviesResponse> discoverMoviesByGenres(int page, String includeGenres) {
        return service.discoverMovies(apiKey, page, language, region, includeGenres);
    }
    // endregion

    // endregion

    // region REVIEWS RESPONSE

    /**
     * Get Movie reviews on TMDB (using LiveData)
     *
     * @param movieId Movie Id
     * @param page    target page
     */
    public Call<ReviewsResponse> getTMDBMovieReviews(long movieId, int page) {
        return service.getTMDBMovieReviews(movieId, apiKey, page);
    }

    // endregion

    // region WATCH PROVIDER RESPONSE

    /**
     * Get Movie watch provider (using LiveData)
     *
     * @param movieId Movie Id
     */
    public Call<WatchProvidersResponse> getWatchProviderByMovie(long movieId) {
        return service.getWatchProviderByMovie(movieId, apiKey);
    }

    // endregion

    // endregion
}
