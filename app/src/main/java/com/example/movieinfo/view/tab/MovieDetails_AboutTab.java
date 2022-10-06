package com.example.movieinfo.view.tab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movieinfo.R;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.viewmodel.MovieDetailViewModel;
import com.example.movieinfo.viewmodel.TvShowDetailViewModel;

public class MovieDetails_AboutTab extends Fragment {

    private final String LOG_TAG = "MovieDetails_AboutTab";

    private MovieDetailViewModel movieDetailViewModel;

    private TextView overView;


    public MovieDetails_AboutTab() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        overView = view.findViewById(R.id.text_movie_overview);

    }

    /**
     * Observe when MovieDetail LiveData changed
     */
    private Observer<MovieDetailData> getDataObserver(){
        return movieDetail -> {
            // populate data to UI
            populateUI(movieDetail);
        };
    }

    private void populateUI(MovieDetailData movieDetail){
        // set overview
        overView.setText(movieDetail.getOverview() == null ? "" : movieDetail.getOverview());
    }
}