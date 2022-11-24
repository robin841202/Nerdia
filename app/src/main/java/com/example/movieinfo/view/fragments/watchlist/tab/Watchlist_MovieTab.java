package com.example.movieinfo.view.fragments.watchlist.tab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.database.entity.MovieWatchlistEntity;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.tvshow.TvShowData;
import com.example.movieinfo.view.MediaDetailsActivity;
import com.example.movieinfo.view.adapter.MoviesAdapter;
import com.example.movieinfo.view.adapter.TvShowsAdapter;
import com.example.movieinfo.viewmodel.WatchlistViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Watchlist_MovieTab extends Fragment implements MoviesAdapter.IMovieListener{

    private final String LOG_TAG = "Watchlist_MovieTab";

    private WatchlistViewModel viewModel;

    private ShimmerFrameLayout mShimmer;
    private RecyclerView mRcView;
    private MoviesAdapter moviesAdapter;
    private GridLayoutManager mLayoutMgr;

    public Watchlist_MovieTab() {
        // Required empty public constructor
    }

    public static Watchlist_MovieTab newInstance() {
        Bundle args = new Bundle();
        Watchlist_MovieTab fragment = new Watchlist_MovieTab();
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
        return inflater.inflate(R.layout.fragment_watchlist_movie_tab, container, false);
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
        moviesAdapter = new MoviesAdapter(this);

        // Set adapter
        mRcView.setAdapter(moviesAdapter);

        // Set NestedScrollingEnable
        mRcView.setNestedScrollingEnabled(true);

        // Initialize gridLayoutManager
        mLayoutMgr = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);

        // Set layoutManager
        mRcView.setLayoutManager(mLayoutMgr);

        // Load movie watchlist and observe it
        viewModel.loadAllMovieWatchlist().observe(getViewLifecycleOwner(), loadMovieWatchlistObserver());
    }

    /**
     * Observe when Movie Watchlist LiveData changed
     */
    public Observer<List<MovieWatchlistEntity>> loadMovieWatchlistObserver() {
        return movieWatchlist -> {
            // hide shimmer animation
            mShimmer.stopShimmer();
            mShimmer.setVisibility(View.GONE);

            if (movieWatchlist.size() > 0) {
                // Mapping "MovieWatchlistEntity" object to "MovieData" object
                ArrayList<MovieData> movieData_list = movieWatchlist.stream().map(data -> new MovieData(
                        data.getMovieId(),
                        data.getTitle(),
                        data.getPosterPath(),
                        data.getRating())).collect(Collectors.toCollection(ArrayList::new));

                // append data to adapter
                moviesAdapter.setMovies(movieData_list);
            }

            Log.d(LOG_TAG, "watchlist movies: data loaded successfully");
        };
    }


    /**
     * Callback when movie item get clicked
     *
     * @param movie movie data
     */
    @Override
    public void onMovieClick(MovieData movie) {
        Intent intent = new Intent(getContext(), MediaDetailsActivity.class);
        intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY, StaticParameter.MediaType.MOVIE);
        intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_MOVIE_ID_KEY, movie.getId());
        startActivity(intent);
        // set the custom transition animation
        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}