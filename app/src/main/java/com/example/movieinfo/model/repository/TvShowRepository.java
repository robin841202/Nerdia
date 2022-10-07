package com.example.movieinfo.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.service.ITvShowService;
import com.example.movieinfo.model.tvshow.TvShowData;
import com.example.movieinfo.model.tvshow.TvShowDetailData;
import com.example.movieinfo.model.tvshow.TvShowsResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvShowRepository {
    private final String LOG_TAG = "TvShowRepository";
    private final ITvShowService service;
    private final String apiKey = "45573754115aa294605178ed2769f617";
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
        this.language = "zh-TW";
        this.region = "TW";

        // Initialize LiveData
        tvShowsLiveData = new MutableLiveData<>();
        popularTvShowsLiveData = new MutableLiveData<>();
        trendingTvShowsLiveData = new MutableLiveData<>();
        tvShowDetailLiveData = new MutableLiveData<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
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
     * Get TvShow Detail By TvShow Id (using LiveData)
     *
     * @param tvShowId       TvShow Id
     * @param subRequestType Can do subRequest in the same time  ex: videos
     */
    public void getTvShowDetail(long tvShowId,
                                String subRequestType) {
        Call<TvShowDetailData> call = service.getTvShowDetail(tvShowId, apiKey, language, subRequestType);
        call.enqueue(new Callback<TvShowDetailData>() {
            @Override
            public void onResponse(Call<TvShowDetailData> call, Response<TvShowDetailData> response) {
                if (response.isSuccessful()) { // Request successfully
                    TvShowDetailData responseBody = response.body();
                    if (responseBody != null) { // Data exists
                        // post result data to liveData
                        tvShowDetailLiveData.postValue(responseBody);
                    }
                }
            }

            @Override
            public void onFailure(Call<TvShowDetailData> call, Throwable t) {
                // post null to liveData
                tvShowDetailLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: getTvShowDetail,\n %s ", t.getMessage()));
            }
        });
    }


    /***
     * Get TvShows Response Live Data (For Popular TvShows)
     * @return
     */
    public MutableLiveData<ArrayList<TvShowData>> getPopularTvShowsLiveData() {
        return popularTvShowsLiveData;
    }

    /***
     * Get TvShows Response Live Data (For Trending TvShows)
     * @return
     */
    public MutableLiveData<ArrayList<TvShowData>> getTrendingTvShowsLiveData() {
        return trendingTvShowsLiveData;
    }


    /***
     * Get TvShows Response Live Data (For Trending TvShows)
     * @return
     */
    public MutableLiveData<ArrayList<TvShowData>> getTvShowsLiveData() {
        return tvShowsLiveData;
    }

    /***
     * Get TvShow Detail Live Data
     * @return
     */
    public MutableLiveData<TvShowDetailData> getTvShowDetailLiveData() {
        return tvShowDetailLiveData;
    }


    // endregion
}
