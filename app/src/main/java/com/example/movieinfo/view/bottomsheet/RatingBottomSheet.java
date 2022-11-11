package com.example.movieinfo.view.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.movieinfo.R;

import com.example.movieinfo.model.OmdbData;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.viewmodel.MovieDetailViewModel;
import com.example.movieinfo.viewmodel.TvShowDetailViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.common.base.Strings;

import java.util.ArrayList;

public class RatingBottomSheet extends BottomSheetDialogFragment {


    private View tmdbCard;
    private View imdbCard;
    private View rottenTomatoesCard;
    private TextView tmdbRatingText;
    private TextView imdbRatingText;
    private TextView tomatoRatingText;

    private String imdbId;
    private double tmdbRating;

    private String mediaType;

    private MovieDetailViewModel movieDetailViewModel;
    private TvShowDetailViewModel tvShowDetailViewModel;

    public RatingBottomSheet(String mediaType, String imdbId, double tmdbRating) {
        this.mediaType = mediaType;
        this.imdbId = imdbId;
        this.tmdbRating = tmdbRating;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch (mediaType){
            case StaticParameter.MediaType.MOVIE:
                // Initialize viewModel, data only survive this activity lifecycle
                movieDetailViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
                movieDetailViewModel.init();

                // Set omdbData observer
                movieDetailViewModel.getOmdbLiveData().observe(this, getOmdbObserver());
                break;
            case StaticParameter.MediaType.TV:
                // Initialize viewModel, data only survive this activity lifecycle
                tvShowDetailViewModel = new ViewModelProvider(this).get(TvShowDetailViewModel.class);
                tvShowDetailViewModel.init();

                // Set omdbData observer
                tvShowDetailViewModel.getOmdbLiveData().observe(this, getOmdbObserver());
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet_rating, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        tmdbCard = view.findViewById(R.id.card_tmdb);
        imdbCard = view.findViewById(R.id.card_imdb);
        rottenTomatoesCard = view.findViewById(R.id.card_rotten_tomatoes);
        tmdbRatingText = view.findViewById(R.id.text_rating_tmdb);
        imdbRatingText = view.findViewById(R.id.text_rating_imdb);
        tomatoRatingText = view.findViewById(R.id.text_rating_tomato);
        /*View bottomSheet = view.findViewById(R.id.rating_bottom_sheet);
        BottomSheetBehavior<?> behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(62);*/

        // Set TMDB rating in bottom sheet
        String scoreInPercent = String.format("%.0f %%", tmdbRating * 10);
        tmdbRatingText.setText(scoreInPercent);

        // Start getting data
        getOmdbByImdbId(mediaType, imdbId);
    }

    // region Get OMDB Data (MVVM using LiveData)

    /**
     * Get Data By IMDB Id
     *
     * @param mediaType media Type
     * @param imdbId    IMDB Id
     */
    public void getOmdbByImdbId(String mediaType, String imdbId) {
        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:
                movieDetailViewModel.getDataByImdbId(imdbId);
                break;
            case StaticParameter.MediaType.TV:
                tvShowDetailViewModel.getDataByImdbId(imdbId);
                break;
        }
    }

    /**
     * Observe when OMDB LiveData changed
     */
    public Observer<OmdbData> getOmdbObserver() {
        return omdbData -> {
            ArrayList<OmdbData.Rating> omdbRating_list = omdbData.getRatings();
            String imdbRating = null;
            String tomatoRating = null;
            for (OmdbData.Rating item : omdbRating_list) {
                switch (item.getSource()) {
                    case StaticParameter.OmdbSourceName.IMDB:
                        imdbRating = item.getValue();
                        break;
                    case StaticParameter.OmdbSourceName.ROTTEN_TOMATOES:
                        tomatoRating = item.getValue();
                        break;
                    default:
                        // do nothing
                        break;
                }
            }
            if (!Strings.isNullOrEmpty(imdbRating)) {
                imdbRatingText.setText(imdbRating);
            } else {
                imdbRatingText.setText(" - ");
            }

            if (!Strings.isNullOrEmpty(tomatoRating)) {
                tomatoRatingText.setText(tomatoRating);
            } else {
                tomatoRatingText.setText(" - ");
            }
        };
    }

    // endregion
}
