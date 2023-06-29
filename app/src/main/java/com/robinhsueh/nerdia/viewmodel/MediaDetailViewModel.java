package com.robinhsueh.nerdia.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.robinhsueh.nerdia.model.OmdbData;
import com.robinhsueh.nerdia.model.ReviewsResponse;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.TmdbStatusResponse;
import com.robinhsueh.nerdia.model.WatchProvidersResponse;
import com.robinhsueh.nerdia.model.database.entity.MovieWatchlistEntity;
import com.robinhsueh.nerdia.model.database.entity.TvShowWatchlistEntity;
import com.robinhsueh.nerdia.model.movie.MovieDetailData;
import com.robinhsueh.nerdia.model.repository.MovieRepository;
import com.robinhsueh.nerdia.model.repository.OmdbRepository;
import com.robinhsueh.nerdia.model.repository.TvShowRepository;
import com.robinhsueh.nerdia.model.repository.UserRepository;
import com.robinhsueh.nerdia.model.repository.WatchlistRepository;
import com.robinhsueh.nerdia.model.tvshow.TvShowDetailData;
import com.robinhsueh.nerdia.model.user.AccountStatesOnMedia;
import com.robinhsueh.nerdia.model.user.RequestBody.BodyRate;
import com.robinhsueh.nerdia.model.user.RequestBody.BodyWatchlist;
import com.google.common.util.concurrent.ListenableFuture;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaDetailViewModel extends AndroidViewModel {

    // region Define static variables

    // Define default subRequest type
    private static final String SUB_REQUEST_TYPE = TextUtils.join(",", new String[]{
            StaticParameter.SubRequestType.VIDEOS,
            StaticParameter.SubRequestType.IMAGES,
            StaticParameter.SubRequestType.CREDITS,
            StaticParameter.SubRequestType.EXTERNAL_IDS
    });

    // Define subRequest type with login status
    private static final String SUB_REQUEST_TYPE_LOGIN = TextUtils.join(",", new String[]{
            StaticParameter.SubRequestType.VIDEOS,
            StaticParameter.SubRequestType.IMAGES,
            StaticParameter.SubRequestType.CREDITS,
            StaticParameter.SubRequestType.EXTERNAL_IDS,
            StaticParameter.SubRequestType.ACCOUNT_STATES
    });

    // Define video languages
    private static final String VIDEO_LANGUAGES = TextUtils.join(",", new String[]{
            Locale.TRADITIONAL_CHINESE.toLanguageTag(),
            Locale.ENGLISH.getLanguage()
    });

    // Define image languages
    private static final String IMAGE_LANGUAGES = TextUtils.join(",", new String[]{
            Locale.TRADITIONAL_CHINESE.toLanguageTag(),
            Locale.ENGLISH.getLanguage(),
            "null"
    });

    // endregion

    private final String LOG_TAG = "MediaDetailViewModel";
    private MovieRepository movieRepository;
    private TvShowRepository tvShowRepository;
    private OmdbRepository omdbRepository;
    private UserRepository userRepository;
    private final WatchlistRepository watchlistRepository;
    private MutableLiveData<MovieDetailData> movieDetailLiveData;
    private MutableLiveData<TvShowDetailData> tvShowDetailLiveData;
    private MutableLiveData<OmdbData> omdbLiveData;
    private MutableLiveData<TmdbStatusResponse> watchlistUpdateResponseLiveData;
    private MutableLiveData<ArrayList<ReviewsResponse.ReviewData>> reviewsLiveData;
    private MutableLiveData<WatchProvidersResponse> watchProvidersLiveData;

    // Used to observe MovieDetailData.accountStatesOnMedia.score
    private final MediatorLiveData<Double> ratedScore = new MediatorLiveData<>();

    public MediaDetailViewModel(@NonNull Application application) {
        super(application);
        watchlistRepository = new WatchlistRepository(application);
    }

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void init() {
        movieRepository = new MovieRepository();
        tvShowRepository = new TvShowRepository();
        omdbRepository = new OmdbRepository();
        userRepository = new UserRepository();

        movieDetailLiveData = new MutableLiveData<>();
        tvShowDetailLiveData = new MutableLiveData<>();
        reviewsLiveData = new MutableLiveData<>();
        watchProvidersLiveData = new MutableLiveData<>();
        omdbLiveData = new MutableLiveData<>();
        watchlistUpdateResponseLiveData = new MutableLiveData<>();
        ratedScore.addSource(Transformations.map(movieDetailLiveData, input -> input != null ? input.getAccountStatesOnMedia().getScore() : 0.0), score -> ratedScore.postValue(score));
        ratedScore.addSource(Transformations.map(tvShowDetailLiveData, input -> input != null ? input.getAccountStatesOnMedia().getScore() : 0.0), score -> ratedScore.postValue(score));
    }

    // region Remote Data Source (API)

    // region Movie Details

    /**
     * Call repository to get movie detail and update to liveData
     *
     * @param movieId Movie Id
     */
    public void getMovieDetail(long movieId) {
        Call<MovieDetailData> response = movieRepository.getMovieDetail(movieId, SUB_REQUEST_TYPE, VIDEO_LANGUAGES, IMAGE_LANGUAGES);
        response.enqueue(getMovieDetailRequestHandler(movieDetailLiveData));
    }

    /**
     * Call repository to get movie detail and update to liveData (Login)
     *
     * @param movieId Movie Id
     * @param session Valid session
     */
    public void getMovieDetailWithLogin(long movieId, String session) {
        Call<MovieDetailData> response = movieRepository.getMovieDetail(movieId, SUB_REQUEST_TYPE_LOGIN, VIDEO_LANGUAGES, IMAGE_LANGUAGES, session);
        response.enqueue(getMovieDetailRequestHandler(movieDetailLiveData));
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

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<MovieDetailData> getMovieDetailLiveData() {
        return movieDetailLiveData;
    }

    // endregion

    // region TvShow Details

    /**
     * Call repository to get tvShow detail and update to liveData
     *
     * @param tvShowId TvShow Id
     */
    public void getTvShowDetail(long tvShowId) {
        Call<TvShowDetailData> response = tvShowRepository.getTvShowDetail(tvShowId, SUB_REQUEST_TYPE, VIDEO_LANGUAGES, IMAGE_LANGUAGES);
        response.enqueue(getTvShowDetailRequestHandler(tvShowDetailLiveData));
    }

    /**
     * Call repository to get tvShow detail and update to liveData (Login)
     *
     * @param tvShowId TvShow Id
     * @param session  Valid session
     */
    public void getTvShowDetailWithLogin(long tvShowId, String session) {
        Call<TvShowDetailData> response = tvShowRepository.getTvShowDetail(tvShowId, SUB_REQUEST_TYPE_LOGIN, VIDEO_LANGUAGES, IMAGE_LANGUAGES, session);
        response.enqueue(getTvShowDetailRequestHandler(tvShowDetailLiveData));
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

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<TvShowDetailData> getTvShowDetailLiveData() {
        return tvShowDetailLiveData;
    }

    // endregion

    // region Reviews

    /**
     * Call repository to get movie reviews and update to liveData
     *
     * @param movieId Movie Id
     * @param page    target page
     */
    public void getTMDBMovieReviews(long movieId, int page) {
        Call<ReviewsResponse> response = movieRepository.getTMDBMovieReviews(movieId, page);
        response.enqueue(new Callback<ReviewsResponse>() {
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
        });
    }

    /**
     * Call repository to get tvShow reviews and update to liveData
     *
     * @param tvShowId TvShow Id
     * @param page     target page
     */
    public void getTMDBTvShowReviews(long tvShowId, int page) {
        Call<ReviewsResponse> response = tvShowRepository.getTMDBTvShowReviews(tvShowId, page);
        response.enqueue(new Callback<ReviewsResponse>() {
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
        });
    }

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<ArrayList<ReviewsResponse.ReviewData>> getReviewsLiveData() {
        return reviewsLiveData;
    }

    // endregion

    // region WatchProviders

    /**
     * Call repository to get movie watchProviders and update to liveData
     *
     * @param movieId Movie Id
     */
    public void getWatchProviderByMovie(long movieId) {
        Call<WatchProvidersResponse> response = movieRepository.getWatchProviderByMovie(movieId);
        response.enqueue(new Callback<WatchProvidersResponse>() {
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
        });
    }

    /**
     * Call repository to get tvShow watchProviders and update to liveData
     *
     * @param tvShowId TvShow Id
     */
    public void getWatchProviderByTvShow(long tvShowId) {
        Call<WatchProvidersResponse> response = tvShowRepository.getWatchProviderByTvShow(tvShowId);
        response.enqueue(new Callback<WatchProvidersResponse>() {
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
        });
    }

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<WatchProvidersResponse> getWatchProvidersLiveData() {
        return watchProvidersLiveData;
    }

    // endregion

    // region TvShow WatchProviders


    // endregion

    // region Omdb Data

    /**
     * Call repository to get omdb data by IMDB Id and update to liveData
     *
     * @param imdbId IMDB Id
     */
    public void getDataByImdbId(String imdbId) {
        Call<OmdbData> response = omdbRepository.getDataByImdbId(imdbId);
        response.enqueue(new Callback<OmdbData>() {
            @Override
            public void onResponse(@NonNull Call<OmdbData> call, @NonNull Response<OmdbData> response) {
                if (response.isSuccessful()) { // Request successfully
                    if (response.body() != null) { // Data exists
                        // post result data to liveData
                        omdbLiveData.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<OmdbData> call, @NonNull Throwable t) {
                // post null to liveData
                omdbLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: getDataByImdbId,\n %s ", t.getMessage()));
            }
        });
    }

    /**
     * Get the OMDB liveData to observe it
     *
     * @return
     */
    public LiveData<OmdbData> getOmdbLiveData() {
        return omdbLiveData;
    }

    // endregion

    // region Update Watchlist

    /**
     * Call repository to add or delete media in TMDB watchlist and update status to liveData
     *
     * @param userId        User Id
     * @param session       Valid session
     * @param bodyWatchlist Post body
     */
    public void updateMediaToWatchlistTMDB(long userId, String session, BodyWatchlist bodyWatchlist) {
        Call<TmdbStatusResponse> response = userRepository.updateMediaToWatchlistTMDB(userId, session, bodyWatchlist);
        response.enqueue(new Callback<TmdbStatusResponse>() {
            @Override
            public void onResponse(@NonNull Call<TmdbStatusResponse> call, @NonNull Response<TmdbStatusResponse> response) {
                if (response.isSuccessful()) { // Request successfully
                    if (response.body() != null) { // Data exists
                        // post result data to liveData
                        watchlistUpdateResponseLiveData.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TmdbStatusResponse> call, @NonNull Throwable t) {
                // post null to liveData
                watchlistUpdateResponseLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: updateMediaToWatchlistTMDB,\n %s ", t.getMessage()));
            }
        });
    }

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<TmdbStatusResponse> getWatchlistUpdateResponseLiveData() {
        return watchlistUpdateResponseLiveData;
    }

    // endregion

    // region Rate Media

    /**
     * Call repository to rate movie in TMDB
     *
     * @param movieId  Movie Id
     * @param session  Valid session
     * @param bodyRate Post body
     */
    public boolean rateMovieTMDB(long movieId, String session, BodyRate bodyRate) {
        return userRepository.rateMovieTMDB(movieId, session, bodyRate);
    }

    /**
     * Call repository to remove rate on movie in TMDB
     *
     * @param movieId Movie Id
     * @param session Valid session
     */
    public boolean removeRateMovieTMDB(long movieId, String session) {
        return userRepository.removeRateMovieTMDB(movieId, session);
    }

    /**
     * Call repository to rate tvShow in TMDB
     *
     * @param tvShowId TvShow Id
     * @param session  Valid session
     * @param bodyRate Post body
     */
    public boolean rateTvShowTMDB(long tvShowId, String session, BodyRate bodyRate) {
        return userRepository.rateTvShowTMDB(tvShowId, session, bodyRate);
    }

    /**
     * Call repository to remove rate on tvShow in TMDB
     *
     * @param tvShowId TvShow Id
     * @param session  Valid session
     */
    public boolean removeRateTvShowTMDB(long tvShowId, String session) {
        return userRepository.removeRateTvShowTMDB(tvShowId, session);
    }

    // endregion

    // endregion

    // region Room Database

    // region Movie Watchlist

    /**
     * Check movie if exist in watchlist By Id and get liveData to observe it (Room Database)
     *
     * @param id movie Id
     * @return
     */
    public ListenableFuture<Boolean> checkMovieExistInRoomWatchlist(long id) {
        return watchlistRepository.checkMovieExistInWatchlist(id);
    }


    /**
     * Insert movie in watchlist (Room Database)
     *
     * @param entity
     * @return
     */
    public ListenableFuture<Long> insertMovieWatchlist(MovieWatchlistEntity entity) {
        return watchlistRepository.insertMovie(entity);
    }

    /**
     * Delete movie in watchlist by movieId (Room Database)
     *
     * @param id movie Id
     * @return
     */
    public ListenableFuture<Integer> deleteMovieWatchlistById(long id) {
        return watchlistRepository.deleteMovieById(id);
    }

    // endregion

    // region TvShow Watchlist

    /**
     * Check tvShow if exist in watchlist By Id and get liveData to observe it (Room Database)
     *
     * @param id tvShow Id
     * @return
     */
    public ListenableFuture<Boolean> checkTvShowExistInWatchlist(long id) {
        return watchlistRepository.checkTvShowExistInWatchlist(id);
    }

    /**
     * Insert tvShow in watchlist (Room Database)
     *
     * @param entity
     * @return
     */
    public ListenableFuture<Long> insertTvShowWatchlist(TvShowWatchlistEntity entity) {
        return watchlistRepository.insertTvShow(entity);
    }

    /**
     * Delete tvShow in watchlist by tvShowId (Room Database)
     *
     * @param id tvShow Id
     * @return
     */
    public ListenableFuture<Integer> deleteTvShowWatchlistById(long id) {
        return watchlistRepository.deleteTvShowById(id);
    }

    // endregion

    // endregion

    // region Separated observe field

    /**
     * Get the liveData to observe it, Used to observe (MovieDetailData/TvShowDetailData).accountStatesOnMedia.score
     *
     * @return
     */
    public LiveData<Double> getRatedScore() {
        return ratedScore;
    }

    /**
     * Set the ratedScore
     *
     * @param score
     */
    public void setRatedScore(double score) {
        ratedScore.postValue(score);
    }

    // endregion
}
