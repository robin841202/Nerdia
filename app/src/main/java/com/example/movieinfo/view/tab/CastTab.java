package com.example.movieinfo.view.tab;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieinfo.R;
import com.example.movieinfo.model.CreditsResponse;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.tvshow.TvShowDetailData;
import com.example.movieinfo.view.adapter.CastsAdapter;
import com.example.movieinfo.viewmodel.MovieDetailViewModel;
import com.example.movieinfo.viewmodel.TvShowDetailViewModel;

import java.util.ArrayList;

public class CastTab extends Fragment implements CastsAdapter.ICastListener {

    private final String LOG_TAG = "CastTab";

    private Context context;

    private MovieDetailViewModel movieDetailViewModel;
    private TvShowDetailViewModel tvShowDetailViewModel;

    private String mediaType;

    private RecyclerView cast_RcView;
    private CastsAdapter castAdapter;

    public CastTab(String mediaType) {
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

                // Set movieDetail observer
                movieDetailViewModel.getMovieDetailLiveData().observe(this, getMovieDataObserver());
                break;
            case StaticParameter.MediaType.TV:
                // Get the same viewModel that created in parent activity, in order to share the data
                tvShowDetailViewModel = new ViewModelProvider(getActivity()).get(TvShowDetailViewModel.class);

                // Set movieDetail observer
                tvShowDetailViewModel.getTvShowDetailLiveData().observe(this, getTvShowDataObserver());
                break;
            default:
                // do nothing
                break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cast_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        cast_RcView = view.findViewById(R.id.recycler_cast);

        // Initialize Recycler Adapter
        castAdapter = new CastsAdapter(this);

        // Set adapter
        cast_RcView.setAdapter(castAdapter);
        cast_RcView.setNestedScrollingEnabled(true);;

        // Set layoutManager
        cast_RcView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

    }


    /**
     * Observe when MovieDetail LiveData changed
     */
    private Observer<MovieDetailData> getMovieDataObserver() {
        return movieDetail -> {
            // populate data to UI
            populateUI(movieDetail);
        };
    }

    /**
     * Observe when TvShowDetail LiveData changed
     */
    private Observer<TvShowDetailData> getTvShowDataObserver() {
        return tvShowDetail -> {
            // populate data to UI
            populateUI(tvShowDetail);
        };
    }

    /**
     * Populate data into view
     *
     * @param movieDetail
     */
    private void populateUI(MovieDetailData movieDetail) {

        // set casts recyclerView
        CreditsResponse creditsResponse = movieDetail.getCreditsResponse();
        if (creditsResponse != null) {
            ArrayList<CreditsResponse.CastData> casts = creditsResponse.getCast_list();
            castAdapter.setCasts(casts);
        }
    }

    /**
     * Populate data into view
     *
     * @param tvShowDetail
     */
    private void populateUI(TvShowDetailData tvShowDetail) {

        // set casts recyclerView
        CreditsResponse creditsResponse = tvShowDetail.getCreditsResponse();
        if (creditsResponse != null) {
            ArrayList<CreditsResponse.CastData> casts = creditsResponse.getCast_list();
            castAdapter.setCasts(casts);
        }
    }

    /**
     * Callback when cast item get clicked
     *
     * @param cast cast Data
     */
    @Override
    public void onCastClick(CreditsResponse.CastData cast) {

    }
}