package com.example.movieinfo.view.bottomsheet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.movieinfo.R;

import com.example.movieinfo.model.ExternalIdResponse;
import com.example.movieinfo.model.OmdbData;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.tvshow.TvShowDetailData;
import com.example.movieinfo.viewmodel.MovieDetailViewModel;
import com.example.movieinfo.viewmodel.TvShowDetailViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.common.base.Strings;

import java.util.ArrayList;

public class RatingBottomSheet extends BottomSheetDialogFragment {
    private final String LOG_TAG = "RatingBottomSheet";

    private Context context;

    private View tmdbCard;
    private View imdbCard;
    private View rottenTomatoesCard;
    private TextView tmdbRatingText;
    private TextView imdbRatingText;
    private TextView tomatoRatingText;

    private String mediaType;

    private MovieDetailViewModel movieDetailViewModel;
    private TvShowDetailViewModel tvShowDetailViewModel;

    public RatingBottomSheet(String mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:
                // Get the same viewModel that created in parent activity, in order to share the data
                movieDetailViewModel = new ViewModelProvider(getActivity()).get(MovieDetailViewModel.class);
                //movieDetailViewModel.init();

                // Set movieDetailData observer
                movieDetailViewModel.getMovieDetailLiveData().observe(this, getMovieDetailObserver());

                // Set omdbData observer
                movieDetailViewModel.getOmdbLiveData().observe(this, getOmdbObserver());
                break;
            case StaticParameter.MediaType.TV:
                // Get the same viewModel that created in parent activity, in order to share the data
                tvShowDetailViewModel = new ViewModelProvider(getActivity()).get(TvShowDetailViewModel.class);
                //tvShowDetailViewModel.init();

                // Set tvShowDetailData observer
                tvShowDetailViewModel.getTvShowDetailLiveData().observe(this, getTvShowDetailObserver());

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
    }


    /**
     * Observe when MovieDetail LiveData changed
     */
    public Observer<MovieDetailData> getMovieDetailObserver() {
        return movieDetailData -> {
            // Set TMDB rating in bottom sheet
            double score = movieDetailData.getRating();
            String scoreInPercent = String.format("%.0f %%", score * 10);
            tmdbRatingText.setText(scoreInPercent);

            // Set TMDB web page link on click
            tmdbCard.setOnClickListener(v -> {
                String tmdbWebUrl = StaticParameter.getTmdbWebUrl(mediaType, movieDetailData.getId());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tmdbWebUrl));
                /*
                Use the resolveActivity() method and the Android package manager to find an Activity that can handle your implicit Intent. Make sure that the request resolved successfully.
                Make sure there is at least one Activity can handle your request.
                Remember to add <queries><intent><action> in AndroidManifest since Android 11 needs that
                 */
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Log.d(LOG_TAG, "no Activity on this device can handle this action!");
                }
            });

            // Get IMDB ID
            ExternalIdResponse externalIdResponse = movieDetailData.getExternalIdResponse();
            if (externalIdResponse != null) {
                String imdbId = externalIdResponse.getImdbId();

                // Set IMDB web page link on click
                imdbCard.setOnClickListener(v -> {
                    String imdbWebUrl = StaticParameter.getImdbWebUrl(imdbId);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(imdbWebUrl));
                    /*
                    Use the resolveActivity() method and the Android package manager to find an Activity that can handle your implicit Intent. Make sure that the request resolved successfully.
                    Make sure there is at least one Activity can handle your request.
                    Remember to add <queries><intent><action> in AndroidManifest since Android 11 needs that
                     */
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Log.d(LOG_TAG, "no Activity on this device can handle this action!");
                    }
                });

                // Start getting omdb data
                movieDetailViewModel.getDataByImdbId(imdbId);
            }
        };
    }

    /**
     * Observe when TvShowDetail LiveData changed
     */
    public Observer<TvShowDetailData> getTvShowDetailObserver() {
        return tvShowDetailData -> {
            // Set TMDB rating in bottom sheet
            double score = tvShowDetailData.getRating();
            String scoreInPercent = String.format("%.0f %%", score * 10);
            tmdbRatingText.setText(scoreInPercent);

            // Set TMDB web page link on click
            tmdbCard.setOnClickListener(v -> {
                String tmdbWebUrl = StaticParameter.getTmdbWebUrl(mediaType, tvShowDetailData.getId());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tmdbWebUrl));
                /*
                Use the resolveActivity() method and the Android package manager to find an Activity that can handle your implicit Intent. Make sure that the request resolved successfully.
                Make sure there is at least one Activity can handle your request.
                Remember to add <queries><intent><action> in AndroidManifest since Android 11 needs that
                 */
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Log.d(LOG_TAG, "no Activity on this device can handle this action!");
                }
            });

            // Get IMDB ID
            ExternalIdResponse externalIdResponse = tvShowDetailData.getExternalIdResponse();
            if (externalIdResponse != null) {
                String imdbId = externalIdResponse.getImdbId();

                // Set IMDB web page link on click
                imdbCard.setOnClickListener(v -> {
                    String imdbWebUrl = StaticParameter.getImdbWebUrl(imdbId);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(imdbWebUrl));
                    /*
                    Use the resolveActivity() method and the Android package manager to find an Activity that can handle your implicit Intent. Make sure that the request resolved successfully.
                    Make sure there is at least one Activity can handle your request.
                    Remember to add <queries><intent><action> in AndroidManifest since Android 11 needs that
                     */
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Log.d(LOG_TAG, "no Activity on this device can handle this action!");
                    }
                });

                // Start getting omdb data
                tvShowDetailViewModel.getDataByImdbId(imdbId);
            }
        };
    }


    // region Get OMDB Data (MVVM using LiveData)

    /**
     * Observe when OMDB LiveData changed
     */
    public Observer<OmdbData> getOmdbObserver() {
        return omdbData -> {

            // Set RottenTomatoes web page link on click
            rottenTomatoesCard.setOnClickListener(v -> {
                String tomatoWebUrl = omdbData.getTomatoWebUrl();
                if (!Strings.isNullOrEmpty(tomatoWebUrl) && !tomatoWebUrl.equalsIgnoreCase("N/A")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tomatoWebUrl));
                    /*
                    Use the resolveActivity() method and the Android package manager to find an Activity that can handle your implicit Intent. Make sure that the request resolved successfully.
                    Make sure there is at least one Activity can handle your request.
                    Remember to add <queries><intent><action> in AndroidManifest since Android 11 needs that
                     */
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Log.d(LOG_TAG, "no Activity on this device can handle this action!");
                    }
                }else{
                    Toast.makeText(context, R.string.toastMsg_link_not_exist, Toast.LENGTH_SHORT).show();
                }
            });

            // region Retrieve ratings and populate in views
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
            // endregion

        };
    }

    // endregion


}
