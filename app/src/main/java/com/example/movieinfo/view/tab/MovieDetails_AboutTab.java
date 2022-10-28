package com.example.movieinfo.view.tab;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movieinfo.R;
import com.example.movieinfo.model.Genre;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.viewmodel.MovieDetailViewModel;
import com.example.movieinfo.viewmodel.TvShowDetailViewModel;
import com.google.common.base.Strings;

import java.util.ArrayList;

import io.github.giangpham96.expandabletextview.ExpandableTextView;

public class MovieDetails_AboutTab extends Fragment {

    private final String LOG_TAG = "MovieDetails_AboutTab";

    private Context context;

    private MovieDetailViewModel movieDetailViewModel;

    private ExpandableTextView overViewTextView;
    private ViewGroup genresGroup;
    private TextView statusTextView;
    private TextView runtimeTextView;
    private TextView budgetTextView;
    private TextView revenueTextView;

    public MovieDetails_AboutTab() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

        // Get the same viewModel that created in parent activity, in order to share the data
        movieDetailViewModel = new ViewModelProvider(getActivity()).get(MovieDetailViewModel.class);

        // Set movieDetail observer
        movieDetailViewModel.getMovieDetailLiveData().observe(this, getDataObserver());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_details_about_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        overViewTextView = view.findViewById(R.id.expandText_movie_overview);
        genresGroup = view.findViewById(R.id.group_genres);
        statusTextView = view.findViewById(R.id.text_status);
        runtimeTextView = view.findViewById(R.id.text_runtime);
        budgetTextView = view.findViewById(R.id.text_budget);
        revenueTextView = view.findViewById(R.id.text_revenue);

    }

    /**
     * Observe when MovieDetail LiveData changed
     */
    private Observer<MovieDetailData> getDataObserver() {
        return movieDetail -> {
            // populate data to UI
            populateUI(movieDetail);
        };
    }

    private void populateUI(MovieDetailData movieDetail) {

        // get all information
        String overView = Strings.isNullOrEmpty(movieDetail.getOverview()) ? getString(R.string.label_empty) : movieDetail.getOverview();
        ArrayList<Genre> genre_List = movieDetail.getGenres();
        String status = Strings.isNullOrEmpty(movieDetail.getStatus()) ? getString(R.string.label_empty) : movieDetail.getStatus();
        int runtimeInMinutes = movieDetail.getRuntime();
        int budgetNum = movieDetail.getBudget();
        int revenueNum = movieDetail.getRevenue();

        // set overview - using ExpandableTextView library setOriginalText function to show contents, do not use setText
        overViewTextView.setOriginalText(overView);

        // set genres
        if (genre_List != null) {
            for (int i = 0; i < genre_List.size(); i++) {
                Genre genre = genre_List.get(i);

                // add a TextView dynamically
                addGenreTextViewToGroup(genre.getName(), genresGroup);
            }
        }

        // set status
        statusTextView.setText(status);

        // set runtime
        String runtime;
        if (runtimeInMinutes > 0) {
            int hours = runtimeInMinutes / 60;
            int minutes = runtimeInMinutes % 60;
            runtime = String.format("%d 小時 %02d 分鐘", hours, minutes);
        } else {
            runtime = getString(R.string.label_empty);
        }
        runtimeTextView.setText(runtime);

        // set budget
        String budget = budgetNum > 0 ? String.format("USD$ %,d", budgetNum) : getString(R.string.label_empty);
        budgetTextView.setText(budget);

        // set revenue
        String revenue = revenueNum > 0 ? String.format("USD$ %,d", revenueNum) : getString(R.string.label_empty);
        revenueTextView.setText(revenue);

    }

    /**
     * Add a new TextView to Genres Group
     *
     * @param name  Genre name
     * @param group Container that contains multiple genre TextView
     */
    private void addGenreTextViewToGroup(String name, ViewGroup group) {
        TextView genreTextView = new TextView(context);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMarginEnd(8);
        genreTextView.setLayoutParams(params);
        genreTextView.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Small);
        genreTextView.setBackgroundResource(R.drawable.rounded_corner);
        genreTextView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.lightGray));
        genreTextView.setText(name);
        group.addView(genreTextView);
    }


}

