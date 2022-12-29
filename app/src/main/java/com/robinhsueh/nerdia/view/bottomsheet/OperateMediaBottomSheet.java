package com.robinhsueh.nerdia.view.bottomsheet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.robinhsueh.nerdia.R;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.database.entity.MovieWatchlistEntity;
import com.robinhsueh.nerdia.model.database.entity.TvShowWatchlistEntity;
import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.model.tvshow.TvShowData;
import com.robinhsueh.nerdia.model.user.RequestBody.BodyWatchlist;
import com.robinhsueh.nerdia.model.user.LoginInfo;
import com.robinhsueh.nerdia.utils.SharedPreferenceUtils;
import com.robinhsueh.nerdia.viewmodel.OperateMediaViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class OperateMediaBottomSheet extends BottomSheetDialogFragment {
    private final String LOG_TAG = "OperateMediaBottomSheet";

    private Context context;

    private MaterialButton watchlistBtn;

    private final String mediaType;
    private MovieData movieData;
    private TvShowData tvShowData;

    private LoginInfo mLoginInfo;

    private OperateMediaViewModel viewModel;

    public OperateMediaBottomSheet(String mediaType, MovieData movieData) {
        this.mediaType = mediaType;
        this.movieData = movieData;
    }

    public OperateMediaBottomSheet(String mediaType, TvShowData tvShowData) {
        this.mediaType = mediaType;
        this.tvShowData = tvShowData;
    }

    @Override
    public void onStart() {
        super.onStart();
        //this forces the sheet to appear at max height even on landscape
        BottomSheetBehavior.from((View) requireView().getParent()).setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

        // Get SharedPreference file
        SharedPreferences sp = SharedPreferenceUtils.getOrCreateSharedPreference(StaticParameter.SharedPreferenceFileKey.SP_FILE_TMDB_KEY, context);

        // Initialize loginInfo
        mLoginInfo = SharedPreferenceUtils.getLoginInfoFromSharedPreference(sp);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet_operate_media, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        watchlistBtn = view.findViewById(R.id.btn_add_to_watchlist);

        // Initialize viewModel, data only survive this activity lifecycle
        viewModel = new ViewModelProvider(this).get(OperateMediaViewModel.class);
        viewModel.init();

        // region Set watchlist update observer if isLogin
        if (mLoginInfo.isLogin()) {
            viewModel.getWatchlistUpdateResponseLiveData().observe(getViewLifecycleOwner(), tmdbStatusResponse -> {
                switch (tmdbStatusResponse.getStatusCode()) {
                    case 1: // Success
                    case 12: // The item/record was updated successfully.
                        showWatchlistAddMsg(true);
                        break;
                    case 13: // The item/record was deleted successfully.
                        showWatchlistDeleteMsg(true);
                        break;
                    default:
                        showWatchlistAddMsg(false);
                        break;
                }
            });
        }
        // endregion

        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:

                // region Add material button onCheckChange listener
                watchlistBtn.addOnCheckedChangeListener((button, isChecked) -> {
                    if (isChecked) { // INSERT
                        if (button.isPressed()) // button is pressed by user not programmatically
                            insertMovieToWatchlist(movieData);
                        button.setText(getString(R.string.label_existed_in_watchlist));
                        button.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_watchlist_toggle_btn_on));
                    } else { // DELETE
                        if (button.isPressed()) // button is pressed by user not programmatically
                            deleteMovieFromWatchlist(movieData);
                        button.setText(getString(R.string.label_add_to_watchlist));
                        button.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_watchlist_toggle_btn_off));
                    }
                });
                // endregion

                // region Watchlist btn default state

                if (mLoginInfo.isLogin()) { // LOGIN TMDB
                    // region Observe account states on movie
                    viewModel.getAccountStatesLiveData().observe(getViewLifecycleOwner(), accountStatesOnMedia -> {
                        if (accountStatesOnMedia != null) {
                            // enable watchlist button
                            watchlistBtn.setEnabled(true);
                            // Set watchlist button default status
                            watchlistBtn.setChecked(accountStatesOnMedia.isInWatchlist());
                        } else {
                            // disable watchlist button
                            watchlistBtn.setEnabled(false);
                        }
                    });
                    // endregion
                    // Get account states on movie
                    viewModel.getTMDBAccountStatesOnMovie(movieData.getId(), mLoginInfo.getSession());
                } else { // NOT LOGIN
                    // region Check and observe whether movie is already in local database watchlist or not
                    try {
                        boolean isExisted = viewModel.checkMovieExistInWatchlist(movieData.getId()).get();
                        // enable watchlist button
                        watchlistBtn.setEnabled(true);
                        // Set watchlist button default status
                        watchlistBtn.setChecked(isExisted);
                    } catch (ExecutionException | InterruptedException e) {
                        // disable watchlist button
                        watchlistBtn.setEnabled(false);
                        e.printStackTrace();
                    }
                    // endregion
                }

                // endregion


                break;
            case StaticParameter.MediaType.TV:

                // region Add material button onCheckChange listener
                watchlistBtn.addOnCheckedChangeListener((button, isChecked) -> {
                    if (isChecked) { // INSERT
                        if (button.isPressed()) // button is pressed by user not programmatically
                            insertTvShowToWatchlist(tvShowData);
                        button.setText(getString(R.string.label_existed_in_watchlist));
                        button.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_watchlist_toggle_btn_on));
                    } else { // DELETE
                        if (button.isPressed()) // button is pressed by user not programmatically
                            deleteTvShowFromWatchlist(tvShowData);
                        button.setText(getString(R.string.label_add_to_watchlist));
                        button.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_watchlist_toggle_btn_off));
                    }
                });
                // endregion


                // region Watchlist btn default state

                if (mLoginInfo.isLogin()) { // LOGIN TMDB
                    // region Observe account states on tvShow
                    viewModel.getAccountStatesLiveData().observe(getViewLifecycleOwner(), accountStatesOnMedia -> {
                        if (accountStatesOnMedia != null) {
                            // enable watchlist button
                            watchlistBtn.setEnabled(true);
                            // Set watchlist button default status
                            watchlistBtn.setChecked(accountStatesOnMedia.isInWatchlist());
                        } else {
                            // disable watchlist button
                            watchlistBtn.setEnabled(false);
                        }
                    });
                    // endregion
                    // Get account states on tvShow
                    viewModel.getTMDBAccountStatesOnTvShow(tvShowData.getId(), mLoginInfo.getSession());
                } else { // NOT LOGIN
                    // region Check and observe whether movie is already in local database watchlist or not
                    try {
                        boolean isExisted = viewModel.checkTvShowExistInWatchlist(tvShowData.getId()).get();
                        // enable watchlist button
                        watchlistBtn.setEnabled(true);
                        // Set watchlist button default status
                        watchlistBtn.setChecked(isExisted);
                    } catch (ExecutionException | InterruptedException e) {
                        // disable watchlist button
                        watchlistBtn.setEnabled(false);
                        e.printStackTrace();
                    }
                    // endregion
                }

                // endregion


                break;
        }

    }

    // region Movie Watchlist

    /**
     * Insert movie to watchlist
     *
     * @param movieData
     */
    private void insertMovieToWatchlist(MovieData movieData) {
        if (mLoginInfo.isLogin()) { // LOGIN TMDB
            viewModel.updateMediaToWatchlistTMDB(mLoginInfo.getUserId(), mLoginInfo.getSession(), new BodyWatchlist(StaticParameter.MediaType.MOVIE, movieData.getId(), true));
        } else { // NOT LOGIN
            // region Room Database
            MovieWatchlistEntity newEntity = new MovieWatchlistEntity(
                    movieData.getId(),
                    movieData.getTitle(),
                    movieData.getPosterPath(),
                    movieData.getRating(),
                    movieData.getIsAdult(),
                    Calendar.getInstance().getTime());
            try {
                boolean isSuccess = viewModel.insertMovieWatchlist(newEntity).get() != null;
                showWatchlistAddMsg(isSuccess);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            // endregion
        }
    }

    /**
     * Delete movie from watchlist
     *
     * @param movieData
     */
    private void deleteMovieFromWatchlist(MovieData movieData) {
        if (mLoginInfo.isLogin()) { // LOGIN TMDB
            viewModel.updateMediaToWatchlistTMDB(mLoginInfo.getUserId(), mLoginInfo.getSession(), new BodyWatchlist(StaticParameter.MediaType.MOVIE, movieData.getId(), false));
        } else { // NOT LOGIN
            // region Room Database
            try {
                boolean isSuccess = viewModel.deleteMovieWatchlistById(movieData.getId()).get() != null;
                showWatchlistDeleteMsg(isSuccess);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            // endregion
        }

    }

    // endregion

    // region TvShow Watchlist

    /**
     * Insert tvShow to watchlist
     *
     * @param tvShowData
     */
    private void insertTvShowToWatchlist(TvShowData tvShowData) {
        if (mLoginInfo.isLogin()) { // LOGIN TMDB
            viewModel.updateMediaToWatchlistTMDB(mLoginInfo.getUserId(), mLoginInfo.getSession(), new BodyWatchlist(StaticParameter.MediaType.TV, tvShowData.getId(), true));
        } else { // NOT LOGIN
            // region Room Database
            TvShowWatchlistEntity newEntity = new TvShowWatchlistEntity(
                    tvShowData.getId(),
                    tvShowData.getTitle(),
                    tvShowData.getPosterPath(),
                    tvShowData.getRating(),
                    tvShowData.getIsAdult(),
                    Calendar.getInstance().getTime());
            try {
                boolean isSuccess = viewModel.insertTvShowWatchlist(newEntity).get() != null;
                showWatchlistAddMsg(isSuccess);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            // endregion
        }
    }

    /**
     * Delete tvShow from watchlist
     *
     * @param tvShowData
     */
    private void deleteTvShowFromWatchlist(TvShowData tvShowData) {
        if (mLoginInfo.isLogin()) { // LOGIN TMDB
            viewModel.updateMediaToWatchlistTMDB(mLoginInfo.getUserId(), mLoginInfo.getSession(), new BodyWatchlist(StaticParameter.MediaType.TV, tvShowData.getId(), false));
        } else { // NOT LOGIN
            // region Room Database
            try {
                boolean isSuccess = viewModel.deleteTvShowWatchlistById(tvShowData.getId()).get() != null;
                showWatchlistDeleteMsg(isSuccess);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            // endregion
        }
    }

    // endregion


    /**
     * Show watchlist message after adding
     *
     * @param isSuccess
     */
    private void showWatchlistAddMsg(boolean isSuccess) {
        String msg = isSuccess ? getString(R.string.msg_saved_to_watchlist) : getString(R.string.msg_operation_error);
        Snackbar.make(getDialog().getWindow().getDecorView(), msg, Snackbar.LENGTH_LONG)
                .setBackgroundTint(context.getColor(R.color.teal_200))
                .show();
    }

    /**
     * Show watchlist message after deleting
     *
     * @param isSuccess
     */
    private void showWatchlistDeleteMsg(boolean isSuccess) {
        String msg = isSuccess ? getString(R.string.msg_removed_from_watchlist) : getString(R.string.msg_operation_error);
        Snackbar.make(getDialog().getWindow().getDecorView(), msg, Snackbar.LENGTH_LONG)
                .setBackgroundTint(context.getColor(R.color.teal_200))
                .show();
    }
}
