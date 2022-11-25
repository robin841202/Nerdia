package com.example.movieinfo.view.fragments.watchlist.tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.database.entity.MovieWatchlistEntity;
import com.example.movieinfo.model.database.entity.TvShowWatchlistEntity;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.tvshow.TvShowData;
import com.example.movieinfo.view.MediaDetailsActivity;
import com.example.movieinfo.view.adapter.MoviesAdapter;
import com.example.movieinfo.view.adapter.TvShowsAdapter;
import com.example.movieinfo.viewmodel.SimilarTabViewModel;
import com.example.movieinfo.viewmodel.WatchlistViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Watchlist_TvShowTab extends Fragment{

    private final String LOG_TAG = "Watchlist_TvShowTab";

    private WatchlistViewModel viewModel;

    private ShimmerFrameLayout mShimmer;
    private RecyclerView mRcView;
    private TvShowsAdapter tvShowsAdapter;
    private GridLayoutManager mLayoutMgr;

    public Watchlist_TvShowTab() {
        // Required empty public constructor
    }

    public static Watchlist_TvShowTab newInstance() {
        Bundle args = new Bundle();
        Watchlist_TvShowTab fragment = new Watchlist_TvShowTab();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize viewModel, data only survive this fragment lifecycle
        viewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        viewModel.init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watchlist_tv_show_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize Views
        mRcView = view.findViewById(R.id.recycler);
        mShimmer = view.findViewById(R.id.shimmer);

        // show shimmer animation
        mShimmer.startShimmer();
        mShimmer.setVisibility(View.VISIBLE);

        // Initialize Recycler Adapter
        tvShowsAdapter = new TvShowsAdapter((AppCompatActivity)getActivity());

        // Set adapter
        mRcView.setAdapter(tvShowsAdapter);

        // Set NestedScrollingEnable
        mRcView.setNestedScrollingEnabled(true);

        // Initialize gridLayoutManager
        mLayoutMgr = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);

        // Set layoutManager
        mRcView.setLayoutManager(mLayoutMgr);

        // Load tvShow watchlist and observe it
        viewModel.loadAllTvShowWatchlist().observe(getViewLifecycleOwner(), loadTvShowWatchlistObserver());
    }

    /**
     * Observe when TvShow Watchlist LiveData changed
     */
    public Observer<List<TvShowWatchlistEntity>> loadTvShowWatchlistObserver() {
        return tvShowWatchlist -> {
            // hide shimmer animation
            mShimmer.stopShimmer();
            mShimmer.setVisibility(View.GONE);

            // Mapping "TvShowWatchlistEntity" object to "TvShowData" object
            ArrayList<TvShowData> tvShowData_list = tvShowWatchlist.stream().map(data -> new TvShowData(
                    data.getTvShowId(),
                    data.getTitle(),
                    data.getPosterPath(),
                    data.getRating())).collect(Collectors.toCollection(ArrayList::new));

            // append data to adapter
            tvShowsAdapter.setTvShows(tvShowData_list);

            Log.d(LOG_TAG, "watchlist tvShows: data loaded successfully");
        };
    }

}