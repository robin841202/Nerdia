package com.example.movieinfo.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.movieinfo.BuildConfig;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MoviesResponse;
import com.example.movieinfo.model.service.ITvShowService;
import com.example.movieinfo.model.tvshow.TvShowData;
import com.example.movieinfo.model.tvshow.TvShowDetailData;
import com.example.movieinfo.model.tvshow.TvShowsResponse;
import com.example.movieinfo.model.user.AccountStatesOnMedia;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvShowRepository {
    private final String LOG_TAG = "TvShowRepository";
    private final ITvShowService service;
    private final String apiKey = BuildConfig.TMDB_API_KEY;
    private String language;
    private String region;
    // set default mediaType to tv
    private final String mediaType = StaticParameter.MediaType.TV;

    // region TvShowData List LiveData
    private MutableLiveData<ArrayList<TvShowData>> tvShowsLiveData;

    // used when multiple liveData needs to observe different data in same activity or fragment
    private MutableLiveData<ArrayList<TvShowData>> popularTvShowsLiveData;
    private MutableLiveData<ArrayList<TvShowData>> trendingTvShowsLiveData;

    // endregion

    // region TvShowDetail LiveData
    private MutableLiveData<TvShowDetailData> tvShowDetailLiveData;
    // endregion

    public TvShowRepository() {
        this.language = Locale.TRADITIONAL_CHINESE.toLanguageTag();
        this.region = Locale.TAIWAN.getCountry();

        // Initialize LiveData
        initLiveData();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticParameter.TmdbApiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ITvShowService.class);
    }

    /**
     * Initialize every LiveData
     */
    private void initLiveData() {
        tvShowsLiveData = new MutableLiveData<>();
        popularTvShowsLiveData = new MutableLiveData<>();
        trendingTvShowsLiveData = new MutableLiveData<>();
        tvShowDetailLiveData = new MutableLiveData<>();
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

    // region POPULAR TVSHOWS

    /**
     * Get Popular TvShows (using LiveData)
     *
     * @param page target page
     */
    public void getPopularTvShows(int page) {
        Call<TvShowsResponse> call = service.getPopularTvShows(apiKey, page, language, region);
        Callback<TvShowsResponse> requestHandler = getTvShowsResponseRequestHandler(popularTvShowsLiveData);
        call.enqueue(requestHandler);
    }

    /***
     * Get TvShows Response Live Data (For Popular TvShows)
     * @return
     */
    public MutableLiveData<ArrayList<TvShowData>> getPopularTvShowsLiveData() {
        return popularTvShowsLiveData;
    }

    // endregion

    // region TRENDING TVSHOWS

    /**
     * Get Trending TvShows (using LiveData)
     *
     * @param timeWindow weekly or daily trending: "day", "week"
     * @param page       target page
     */
    public void getTrendingTvShows(String timeWindow, int page) {
        Call<TvShowsResponse> call = service.getTrendingMedia(mediaType, timeWindow, apiKey, page, language, region);
        Callback<TvShowsResponse> requestHandler = getTvShowsResponseRequestHandler(trendingTvShowsLiveData);
        call.enqueue(requestHandler);
    }

    /***
     * Get TvShows Response Live Data (For Trending TvShows)
     * @return
     */
    public MutableLiveData<ArrayList<TvShowData>> getTrendingTvShowsLiveData() {
        return trendingTvShowsLiveData;
    }

    // endregion

    /**
     * Search TvShows By Keyword (using LiveData)
     *
     * @param keyWord keyword for searching
     * @param page    target page
     */
    public void searchTvShows(String keyWord, int page) {
        Call<TvShowsResponse> call = service.searchTvShows(apiKey, keyWord, page, language, region);
        Callback<TvShowsResponse> requestHandler = getTvShowsResponseRequestHandler(tvShowsLiveData);
        call.enqueue(requestHandler);
    }

    /**
     * Get Similar TvShows (using LiveData)
     *
     * @param tvShowId TvShow Id
     * @param page     target page
     */
    public void getSimilarTvShows(long tvShowId, int page) {
        Call<TvShowsResponse> call = service.getSimilarTvShows(tvShowId, apiKey, page, language);
        Callback<TvShowsResponse> requestHandler = getTvShowsResponseRequestHandler(tvShowsLiveData);
        call.enqueue(requestHandler);
    }

    /**
     * Get TvShow Watchlist on TMDB (using LiveData)
     *
     * @param userId   Account Id
     * @param session  Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page     target page
     */
    public void getTMDBTvShowWatchlist(long userId, String session, String sortMode, int page) {
        Call<TvShowsResponse> call = service.getTMDBTvShowWatchlist(userId, apiKey, session, sortMode, page, language);
        Callback<TvShowsResponse> requestHandler = getTvShowsResponseRequestHandler(tvShowsLiveData);
        call.enqueue(requestHandler);
    }

    /**
     * Discover TvShows (using LiveData)
     *
     * @param page          target page
     * @param includeGenres Comma separated value of genre ids that you want to include in the results.
     */
    public void discoverTvShows(int page, String includeGenres) {
        Call<TvShowsResponse> call = service.discoverTvShows(apiKey, page, language, region, includeGenres);
        Callback<TvShowsResponse> requestHandler = getTvShowsResponseRequestHandler(tvShowsLiveData);
        call.enqueue(requestHandler);
    }

    /**
     * (private) Get Request Handler (using LiveData)
     *
     * @param tvShowsLiveData live data
     * @return Request Handler
     */
    private Callback<TvShowsResponse> getTvShowsResponseRequestHandler(MutableLiveData<ArrayList<TvShowData>> tvShowsLiveData) {
        return new Callback<TvShowsResponse>() {
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
        };
    }

    /***
     * Get TvShows Response Live Data (For Trending TvShows)
     * @return
     */
    public MutableLiveData<ArrayList<TvShowData>> getTvShowsLiveData() {
        return tvShowsLiveData;
    }

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
    public void getTvShowDetail(long tvShowId,
                                String subRequestType, String videoLanguages, String imageLanguages) {
        Call<TvShowDetailData> call = service.getTvShowDetail(tvShowId, apiKey, language, subRequestType, videoLanguages, imageLanguages);
        call.enqueue(getTvShowDetailRequestHandler(tvShowDetailLiveData));
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
    public void getTvShowDetail(long tvShowId,
                                String subRequestType, String videoLanguages, String imageLanguages, String session) {
        Call<TvShowDetailData> call = service.getTvShowDetail(tvShowId, apiKey, language, subRequestType, videoLanguages, imageLanguages, session);
        call.enqueue(getTvShowDetailRequestHandler(tvShowDetailLiveData));
    }

    /**
     * (private) Get TvShowDetail Request Handler (using LiveData)
     *
     * @param tvShowDetailLiveData live data
     * @return Request Handler
     */
    private Callback<TvShowDetailData> getTvShowDetailRequestHandler(MutableLiveData<TvShowDetailData> tvShowDetailLiveData) {
        return new Callback<TvShowDetailData>() {
            @Override
            public void onResponse(@NonNull Call<TvShowDetailData> call, @NonNull Response<TvShowDetailData> response) {
                if (response.isSuccessful()) { // Request successfully
                    TvShowDetailData tvShowDetailData = response.body();
                    if (tvShowDetailData != null) { // Data exists
                        // region Handle rated score in accountStates
                        if (tvShowDetailData.getAccountStatesOnMedia() != null) {
                            if (tvShowDetailData.getAccountStatesOnMedia().getRated() instanceof LinkedTreeMap<?, ?>) { // rate score existed
                                Type type = new TypeToken<AccountStatesOnMedia.Rated>() {
                                }.getType();
                                Gson gson = new Gson();
                                AccountStatesOnMedia.Rated ratedObj = gson.fromJson(gson.toJson(tvShowDetailData.getAccountStatesOnMedia().getRated()), type);
                                double score = ratedObj.score;
                                // Set the score
                                tvShowDetailData.getAccountStatesOnMedia().setScore(score);
                            } else { // rate score not existed, type will be Boolean
                                // Set the score to negative
                                tvShowDetailData.getAccountStatesOnMedia().setScore(-1);
                            }
                        }
                        // endregion

                        // post result data to liveData
                        tvShowDetailLiveData.postValue(tvShowDetailData);
                    }
                }
            }

            @Override
            public void onFailure(Call<TvShowDetailData> call, Throwable t) {
                // post null to liveData
                tvShowDetailLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: getTvShowDetail,\n %s ", t.getMessage()));
            }
        };
    }


    /***
     * Get TvShow Detail Live Data
     * @return
     */
    public MutableLiveData<TvShowDetailData> getTvShowDetailLiveData() {
        return tvShowDetailLiveData;
    }

    // endregion

    // endregion
}
