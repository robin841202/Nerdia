package com.robinhsueh.nerdia.model.repository;

import com.robinhsueh.nerdia.BuildConfig;
import com.robinhsueh.nerdia.model.ReviewsResponse;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.WatchProvidersResponse;
import com.robinhsueh.nerdia.model.service.ITvShowService;
import com.robinhsueh.nerdia.model.tvshow.TvShowDetailData;
import com.robinhsueh.nerdia.model.tvshow.TvShowsResponse;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvShowRepository {
    private final ITvShowService service;
    private final String apiKey = BuildConfig.TMDB_API_KEY;
    private String language;
    private String region;
    // set default mediaType to tv
    private final String mediaType = StaticParameter.MediaType.TV;

    public TvShowRepository() {
        this.language = Locale.TRADITIONAL_CHINESE.toLanguageTag();
        this.region = Locale.TAIWAN.getCountry();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticParameter.TmdbApiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ITvShowService.class);
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    // region MVC methods [without viewModel] (Deprecated)

    /**
     * Get Popular TvShows (Deprecated)
     *
     * @param page             target page
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     */
    /*public void getPopularTvShows(int page,
                                  Object objectToInvokeOn,
                                  Method onSuccess,
                                  Method onError) {
        Call<TvShowsResponse> call = service.getPopularTvShows(apiKey, page, language, region);
        Callback<TvShowsResponse> requestHandler = getRequestHandler(objectToInvokeOn, onSuccess, onError);
        call.enqueue(requestHandler);
    }*/

    /**
     * Get Trending TvShows (Deprecated)
     *
     * @param timeWindow       weekly or daily trending: "day", "week"
     * @param page             target page
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     */
    /*public void getTrendingTvShows(String timeWindow,
                                   int page,
                                   Object objectToInvokeOn,
                                   Method onSuccess,
                                   Method onError) {
        Call<TvShowsResponse> call = service.getTrendingMedia(mediaType, timeWindow, apiKey, page, language, region);
        Callback<TvShowsResponse> requestHandler = getRequestHandler(objectToInvokeOn, onSuccess, onError);
        call.enqueue(requestHandler);
    }*/

    /**
     * Get TvShow Detail By TvShow Id (Deprecated)
     *
     * @param tvShowId         TvShow Id
     * @param subRequestType   Can do subRequest in the same time  ex: videos
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     */
    /*public void getTvShowDetail(long tvShowId,
                                String subRequestType,
                                Object objectToInvokeOn,
                                Method onSuccess,
                                Method onError) {
        Call<TvShowDetailData> call = service.getTvShowDetail(tvShowId, apiKey, language, subRequestType);
        Callback<TvShowDetailData> requestHandler = new Callback<TvShowDetailData>() {
            @Override
            public void onResponse(Call<TvShowDetailData> call, Response<TvShowDetailData> response) {
                if (response.isSuccessful()) { // Request successfully
                    TvShowDetailData responseBody = response.body();
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
            public void onFailure(Call<TvShowDetailData> call, Throwable t) {
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
     * Search TvShows By Keyword (Deprecated)
     *
     * @param keyWord          keyword for searching
     * @param page             target page
     * @param objectToInvokeOn Object instance that "onSuccess", "onError" belongs
     * @param onSuccess        callback when data fetched successfully
     * @param onError          callback when data fetched fail
     */
    /*public void searchTvShows(String keyWord,
                              int page,
                              Object objectToInvokeOn,
                              Method onSuccess,
                              Method onError) {
        Call<TvShowsResponse> call = service.searchTvShows(apiKey, keyWord, page, language, region);
        Callback<TvShowsResponse> requestHandler = getRequestHandler(objectToInvokeOn, onSuccess, onError);
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
    /*private Callback<TvShowsResponse> getRequestHandler(Object objectToInvokeOn, Method onSuccess, Method onError) {
        return new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(Call<TvShowsResponse> call, Response<TvShowsResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    TvShowsResponse responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        try {
                            // get the object to invoke on
                            //Object objectToInvokeOn = onSuccess.getDeclaringClass().newInstance();
                            // invoke the callback method with the data
                            onSuccess.invoke(objectToInvokeOn, responseBody.tvShow_list);
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
            public void onFailure(Call<TvShowsResponse> call, Throwable t) {
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

    // endregion

    // region MVVM architecture using LiveData

    // region TVSHOWS RESPONSE

    // region GET POPULAR TVSHOWS

    /**
     * Get Popular TvShows (using LiveData)
     *
     * @param page target page
     */
    public Call<TvShowsResponse> getPopularTvShows(int page) {
        return service.getPopularTvShows(apiKey, page, language, region);
    }

    // endregion

    // region GET TRENDING TVSHOWS

    /**
     * Get Trending TvShows (using LiveData)
     *
     * @param timeWindow weekly or daily trending: "day", "week"
     * @param page       target page
     */
    public Call<TvShowsResponse> getTrendingTvShows(String timeWindow, int page) {
        return service.getTrendingMedia(mediaType, timeWindow, apiKey, page, language, region);
    }

    // endregion

    // region GET NETFLIX TVSHOWS

    /**
     * Get Netflix TvShows (using LiveData)
     *
     * @param page target page
     */
    public Call<TvShowsResponse> getNetflixTvShows(int page) {
        return service.discoverTvShows(apiKey, page, language, region, StaticParameter.WatchProvidersID.NetflixID);
    }

    // endregion

    // region GET DISNEY TVSHOWS

    /**
     * Get Disney TvShows (using LiveData)
     *
     * @param page target page
     */
    public Call<TvShowsResponse> getDisneyTvShows(int page) {
        return service.discoverTvShows(apiKey, page, language, region, StaticParameter.WatchProvidersID.DisneyPlusID);
    }

    // endregion

    // region GET CATCHPLAY TVSHOWS

    /**
     * Get Catchplay TvShows (using LiveData)
     *
     * @param page target page
     */
    public Call<TvShowsResponse> getCatchplayTvShows(int page) {
        return service.discoverTvShows(apiKey, page, language, region, StaticParameter.WatchProvidersID.CatchPlayID);
    }

    // endregion

    // region GET PRIME TVSHOWS

    /**
     * Get Prime TvShows (using LiveData)
     *
     * @param page target page
     */
    public Call<TvShowsResponse> getPrimeTvShows(int page) {
        return service.discoverTvShows(apiKey, page, language, region, StaticParameter.WatchProvidersID.PrimeVideoID);
    }

    // endregion

    // region SEARCH TVSHOWS
    /**
     * Search TvShows By Keyword (using LiveData)
     *
     * @param keyWord keyword for searching
     * @param page    target page
     */
    public Call<TvShowsResponse> searchTvShows(String keyWord, int page) {
        return service.searchTvShows(apiKey, keyWord, page, language, region);
    }
    // endregion

    // region GET SIMILAR TVSHOWS
    /**
     * Get Similar TvShows (using LiveData)
     *
     * @param tvShowId TvShow Id
     * @param page     target page
     */
    public Call<TvShowsResponse> getSimilarTvShows(long tvShowId, int page) {
        return service.getSimilarTvShows(tvShowId, apiKey, page, language);
    }
    // endregion

    // region GET TVSHOW WATCHLIST
    /**
     * Get TvShow Watchlist on TMDB (using LiveData)
     *
     * @param userId   Account Id
     * @param session  Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page     target page
     */
    public Call<TvShowsResponse> getTMDBTvShowWatchlist(long userId, String session, String sortMode, int page) {
        return service.getTMDBTvShowWatchlist(userId, apiKey, session, sortMode, page, language);
    }
    // endregion

    // region GET RATED TVSHOWS
    /**
     * Get TvShows rated by user on TMDB (using LiveData)
     *
     * @param userId   Account Id
     * @param session  Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page     target page
     */
    public Call<TvShowsResponse> getTMDBRatedTvShows(long userId, String session, String sortMode, int page) {
        return service.getTMDBRatedTvShows(userId, apiKey, session, sortMode, page, language);
    }
    // endregion

    // region DISCOVER TVSHOWS BY GENRES
    /**
     * Discover TvShows (using LiveData)
     *
     * @param page          target page
     * @param includeGenres Comma separated value of genre ids that you want to include in the results.
     */
    public Call<TvShowsResponse> discoverTvShows(int page, String includeGenres) {
        return service.discoverTvShows(apiKey, page, language, region, includeGenres);
    }
    // endregion

    // endregion

    // region TVSHOW DETAILS

    /**
     * Get TvShow Detail By TvShow Id (using LiveData)
     *
     * @param tvShowId       TvShow Id
     * @param subRequestType Can do subRequest in the same time  ex: videos
     * @param videoLanguages Can include multiple languages of video ex:zh-TW,en
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     */
    public Call<TvShowDetailData> getTvShowDetail(long tvShowId,
                                String subRequestType, String videoLanguages, String imageLanguages) {
        return service.getTvShowDetail(tvShowId, apiKey, language, subRequestType, videoLanguages, imageLanguages);
    }

    /**
     * Get TvShow Detail By TvShow Id (using LiveData)
     *
     * @param tvShowId       TvShow Id
     * @param subRequestType Can do subRequest in the same time  ex: videos
     * @param videoLanguages Can include multiple languages of video ex:zh-TW,en
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     * @param session        Valid session
     */
    public Call<TvShowDetailData> getTvShowDetail(long tvShowId,
                                String subRequestType, String videoLanguages, String imageLanguages, String session) {
        return service.getTvShowDetail(tvShowId, apiKey, language, subRequestType, videoLanguages, imageLanguages, session);
    }

    // endregion

    // region REVIEWS RESPONSE

    /**
     * Get TvShow reviews on TMDB (using LiveData)
     *
     * @param tvShowId TvShow Id
     * @param page    target page
     */
    public Call<ReviewsResponse> getTMDBTvShowReviews(long tvShowId, int page) {
        return service.getTMDBTvShowReviews(tvShowId, apiKey, page);
    }

    // endregion

    // region WATCH PROVIDER RESPONSE

    /**
     * Get TvShow watch provider (using LiveData)
     *
     * @param tvShowId TvShow Id
     */
    public Call<WatchProvidersResponse> getWatchProviderByTvShow(long tvShowId) {
        return service.getWatchProviderByTvShow(tvShowId, apiKey);
    }

    // endregion

    // endregion
}
