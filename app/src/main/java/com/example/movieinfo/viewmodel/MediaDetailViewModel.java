package com.example.movieinfo.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.example.movieinfo.model.OmdbData;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.TmdbStatusResponse;
import com.example.movieinfo.model.database.entity.MovieWatchlistEntity;
import com.example.movieinfo.model.database.entity.TvShowWatchlistEntity;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.repository.MovieRepository;
import com.example.movieinfo.model.repository.OmdbRepository;
import com.example.movieinfo.model.repository.TvShowRepository;
import com.example.movieinfo.model.repository.UserRepository;
import com.example.movieinfo.model.repository.WatchlistRepository;
import com.example.movieinfo.model.tvshow.TvShowDetailData;
import com.example.movieinfo.model.user.RequestBody.BodyRate;
import com.example.movieinfo.model.user.RequestBody.BodyWatchlist;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Locale;

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

    private MovieRepository movieRepository;
    private TvShowRepository tvShowRepository;
    private OmdbRepository omdbRepository;
    private UserRepository userRepository;
    private final WatchlistRepository watchlistRepository;
    private LiveData<MovieDetailData> movieDetailLiveData;
    private LiveData<TvShowDetailData> tvShowDetailLiveData;
    private LiveData<OmdbData> omdbLiveData;
    private LiveData<TmdbStatusResponse> watchlistUpdateResponseLiveData;

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
        movieDetailLiveData = movieRepository.getMovieDetailLiveData();
        tvShowDetailLiveData = tvShowRepository.getTvShowDetailLiveData();
        omdbLiveData = omdbRepository.getOmdbLiveData();
        watchlistUpdateResponseLiveData = userRepository.getStatusResponseLiveData();
        ratedScore.addSource(Transformations.map(movieDetailLiveData, input -> input.getAccountStatesOnMedia().getScore()), score -> ratedScore.postValue(score));
        ratedScore.addSource(Transformations.map(tvShowDetailLiveData, input -> input.getAccountStatesOnMedia().getScore()), score -> ratedScore.postValue(score));
    }

    // region Remote Data Source (API)

    // region Movie Details

    /**
     * Call repository to get movie detail and update to liveData
     *
     * @param movieId Movie Id
     */
    public void getMovieDetail(long movieId) {
        movieRepository.getMovieDetail(movieId, SUB_REQUEST_TYPE, VIDEO_LANGUAGES, IMAGE_LANGUAGES);
    }

    /**
     * Call repository to get movie detail and update to liveData (Login)
     *
     * @param movieId Movie Id
     * @param session Valid session
     */
    public void getMovieDetailWithLogin(long movieId, String session) {
        movieRepository.getMovieDetail(movieId, SUB_REQUEST_TYPE_LOGIN, VIDEO_LANGUAGES, IMAGE_LANGUAGES, session);
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
        tvShowRepository.getTvShowDetail(tvShowId, SUB_REQUEST_TYPE, VIDEO_LANGUAGES, IMAGE_LANGUAGES);
    }

    /**
     * Call repository to get tvShow detail and update to liveData (Login)
     *
     * @param tvShowId TvShow Id
     * @param session  Valid session
     */
    public void getTvShowDetailWithLogin(long tvShowId, String session) {
        tvShowRepository.getTvShowDetail(tvShowId, SUB_REQUEST_TYPE_LOGIN, VIDEO_LANGUAGES, IMAGE_LANGUAGES, session);
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

    // region Omdb Data

    /**
     * Call repository to get omdb data by IMDB Id and update to liveData
     *
     * @param imdbId IMDB Id
     */
    public void getDataByImdbId(String imdbId) {
        omdbRepository.getDataByImdbId(imdbId);
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
        userRepository.updateMediaToWatchlistTMDB(userId, session, bodyWatchlist);
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
