package com.example.movieinfo.view.bottomsheet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.movieinfo.R;
import com.example.movieinfo.model.ExternalIdResponse;
import com.example.movieinfo.model.OmdbData;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.database.entity.MovieWatchlistEntity;
import com.example.movieinfo.model.database.entity.TvShowWatchlistEntity;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.tvshow.TvShowData;
import com.example.movieinfo.model.tvshow.TvShowDetailData;
import com.example.movieinfo.viewmodel.OperateMediaViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class OperateMediaBottomSheet extends BottomSheetDialogFragment {
    private final String LOG_TAG = "OperateMediaBottomSheet";

    private Context context;

    private MaterialButton watchlistBtn;

    private final String mediaType;
    private MovieData movieData;
    private TvShowData tvShowData;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

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

        // Get the same viewModel that created in parent activity, in order to share the data
        viewModel = new ViewModelProvider(this).get(OperateMediaViewModel.class);

        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:

                // Check and observe whether movie is already in local database watchlist or not
                viewModel.checkMovieExistInWatchlist(movieData.getId()).observe(this, aBoolean -> {
                    // Set button default status
                    watchlistBtn.setChecked(aBoolean);
                });

                // Add material button onCheckChange listener
                watchlistBtn.addOnCheckedChangeListener((button, isChecked) -> {
                    if (isChecked){ // INSERT
                        if (button.isPressed()) // button is pressed by user not programmatically
                            insertMovieToWatchlist(movieData);
                        button.setText(getString(R.string.label_existed_in_watchlist));
                        button.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_watchlist_toggle_btn_on));
                    }else { // DELETE
                        if (button.isPressed()) // button is pressed by user not programmatically
                            deleteMovieFromWatchlist(movieData);
                        button.setText(getString(R.string.label_add_to_watchlist));
                        button.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_watchlist_toggle_btn_off));
                    }
                });
                break;
            case StaticParameter.MediaType.TV:

                // Check and observe whether movie is already in local database watchlist or not
                viewModel.checkTvShowExistInWatchlist(tvShowData.getId()).observe(this, aBoolean -> {
                    // Set button default status
                    watchlistBtn.setChecked(aBoolean);
                });

                // Add material button onCheckChange listener
                watchlistBtn.addOnCheckedChangeListener((button, isChecked) -> {
                    if (isChecked){ // INSERT
                        if (button.isPressed()) // button is pressed by user not programmatically
                            insertTvShowToWatchlist(tvShowData);
                        button.setText(getString(R.string.label_existed_in_watchlist));
                        button.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_watchlist_toggle_btn_on));
                    }else { // DELETE
                        if (button.isPressed()) // button is pressed by user not programmatically
                            deleteTvShowFromWatchlist(tvShowData);
                        button.setText(getString(R.string.label_add_to_watchlist));
                        button.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_watchlist_toggle_btn_off));
                    }
                });
                break;
        }

    }

    // region Movie Watchlist
    /**
     * Insert movie to watchlist
     * @param movieData
     */
    private void insertMovieToWatchlist(MovieData movieData){
        MovieWatchlistEntity newEntity = new MovieWatchlistEntity(
                movieData.getId(),
                movieData.getTitle(),
                movieData.getPosterPath(),
                movieData.getRating(),
                movieData.getIsAdult(),
                Calendar.getInstance().getTime());
        try {
            String msg = viewModel.insertMovieWatchlist(newEntity).get() != null ? getString(R.string.msg_saved_to_watchlist) : getString(R.string.msg_operation_error);
            Snackbar.make(getDialog().getWindow().getDecorView(), msg, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(context.getColor(R.color.teal_200))
                    .show();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete movie from watchlist
     * @param movieData
     */
    private void deleteMovieFromWatchlist(MovieData movieData){
        try {
            String msg = viewModel.deleteMovieWatchlistById(movieData.getId()).get() != null ? getString(R.string.msg_removed_from_watchlist) : getString(R.string.msg_operation_error);
            Snackbar.make(getDialog().getWindow().getDecorView(), msg, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(context.getColor(R.color.teal_200))
                    .show();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // endregion


    // region TvShow Watchlist
    /**
     * Insert tvShow to watchlist
     * @param tvShowData
     */
    private void insertTvShowToWatchlist(TvShowData tvShowData){
        TvShowWatchlistEntity newEntity = new TvShowWatchlistEntity(
                tvShowData.getId(),
                tvShowData.getTitle(),
                tvShowData.getPosterPath(),
                tvShowData.getRating(),
                tvShowData.getIsAdult(),
                Calendar.getInstance().getTime());
        try {
            String msg = viewModel.insertTvShowWatchlist(newEntity).get() != null ? getString(R.string.msg_saved_to_watchlist) : getString(R.string.msg_operation_error);
            Snackbar.make(getDialog().getWindow().getDecorView(), msg, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(context.getColor(R.color.teal_200))
                    .show();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete tvShow from watchlist
     * @param tvShowData
     */
    private void deleteTvShowFromWatchlist(TvShowData tvShowData){
        try {
            String msg = viewModel.deleteTvShowWatchlistById(tvShowData.getId()).get() != null ? getString(R.string.msg_removed_from_watchlist) : getString(R.string.msg_operation_error);
            Snackbar.make(getDialog().getWindow().getDecorView(), msg, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(context.getColor(R.color.teal_200))
                    .show();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // endregion

}
