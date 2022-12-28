package com.robinhsueh.movieinfo.view.bottomsheet;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.robinhsueh.movieinfo.R;
import com.robinhsueh.movieinfo.viewmodel.MediaDetailViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class RatingBottomSheet extends BottomSheetDialogFragment {
    private final String LOG_TAG = "RatingBottomSheet";

    private RatingBar ratingBar;
    private ProgressBar progressBar;
    private TextView progressText;

    private MediaDetailViewModel viewModel;

    private double mCurrentScore;
    private final IRatingListener listener;

    public RatingBottomSheet(IRatingListener listener) {
        this.listener = listener;
    }

    public interface IRatingListener {
        /**
         * Rating Submit Event
         */
        void onRatingSubmit(boolean isSubmit, double score);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the same viewModel that created in parent activity, in order to share the data
        viewModel = new ViewModelProvider(getActivity()).get(MediaDetailViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_rating, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        ratingBar = view.findViewById(R.id.ratingBar);
        progressBar = view.findViewById(R.id.progressBar);
        progressText = view.findViewById(R.id.text_progress);
        MaterialButton applyBtn = view.findViewById(R.id.btn_apply);
        MaterialButton removeBtn = view.findViewById(R.id.btn_remove);

        // Set ratedScore observer
        viewModel.getRatedScore().observe(getViewLifecycleOwner(), score -> {
            // Populate the existed score into UI
            ratingBar.setRating(score > 0 ? score.floatValue() : 0);
        });

        // Set onRatingBarChange listener
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            mCurrentScore = rating;
            setScoreOnRatingBarChange(rating);
        });

        // Set apply button onClick listener
        applyBtn.setOnClickListener(v -> {
            // Callback
            listener.onRatingSubmit(true, mCurrentScore);
            dismiss();
        });

        // Set remove button onClick listener
        removeBtn.setOnClickListener(v -> {
            // Callback
            listener.onRatingSubmit(false, mCurrentScore);
            dismiss();
        });
    }

    /**
     * Set the score display on UI when ratingBar change
     *
     * @param score score
     */
    private void setScoreOnRatingBarChange(float score) {
        int scoreInPercent = (int) (score * 10);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress(scoreInPercent, true);
        } else {
            progressBar.setProgress(scoreInPercent);
        }
        progressText.setText(String.format(Locale.TAIWAN, "%.1f", score));
    }

}
