package com.example.movieinfo.view.fragments.discover;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.tvshow.TvShowData;
import com.example.movieinfo.view.MediaDetailsActivity;
import com.example.movieinfo.view.adapter.MoviesAdapter;
import com.example.movieinfo.view.adapter.TvShowsAdapter;
import com.example.movieinfo.view.bottomsheet.OperateMediaBottomSheet;
import com.example.movieinfo.viewmodel.DiscoverViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class GenreResultsFragment extends Fragment{
    private final String LOG_TAG = "GenreResultsFragment";

    private DiscoverViewModel viewModel;

    private String mediaType;
    private String includeGenres;
    private String title;

    private int currentPage;


    private ShimmerFrameLayout mShimmer;

    private SwipeRefreshLayout pullToRefresh;
    private RecyclerView mRcView;
    private MoviesAdapter moviesAdapter;
    private TvShowsAdapter tvShowsAdapter;
    private GridLayoutManager mLayoutMgr;

    public GenreResultsFragment() {
        // Required empty public constructor
    }

    public static GenreResultsFragment newInstance() {
        Bundle args = new Bundle();
        GenreResultsFragment fragment = new GenreResultsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get homeCategory argument from HomeFragment
        if (getArguments() != null) {
            mediaType = getArguments().getString(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY, StaticParameter.MediaType.MOVIE);
            includeGenres = getArguments().getString(StaticParameter.ExtraDataKey.EXTRA_DATA_INCLUDE_GENRES_KEY, "");
            title = getArguments().getString(StaticParameter.ExtraDataKey.EXTRA_DATA_ACTIONBAR_TITLE_KEY, "");
        }

        // Initialize viewModel, data only survive this fragment lifecycle
        viewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);
        viewModel.init();

        // Set observer
        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:
                viewModel.getMoviesLiveData().observe(this, getMoviesObserver());
                break;
            case StaticParameter.MediaType.TV:
                viewModel.getTvShowsLiveData().observe(this, getTvShowsObserver());
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genre_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        mRcView = view.findViewById(R.id.recycler);
        pullToRefresh = view.findViewById(R.id.swiperefresh);
        mShimmer = view.findViewById(R.id.shimmer);
        ActionBar toolBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        // Set action bar title
        toolBar.setTitle(title);


        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:

                // Initialize Recycler Adapter
                moviesAdapter = new MoviesAdapter((AppCompatActivity)getActivity());

                // Set Adapter
                mRcView.setAdapter(moviesAdapter);
                break;
            case StaticParameter.MediaType.TV:

                // Initialize Recycler Adapter
                tvShowsAdapter = new TvShowsAdapter((AppCompatActivity)getActivity());

                // Set Adapter
                mRcView.setAdapter(tvShowsAdapter);
                break;
        }

        // Initialize layoutManager
        mLayoutMgr = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);

        // Set layoutManager
        mRcView.setLayoutManager(mLayoutMgr);

        // Set SwipeRefreshListener
        pullToRefresh.setOnRefreshListener(() -> {
            // Start getting data
            updateData(mediaType);
            Log.d(LOG_TAG, "onRefresh");
            pullToRefresh.setRefreshing(false);
        });

        // Start getting data
        updateData(mediaType);

    }

    /**
     * Fetching discovered movies
     */
    private void fetchDiscoverMovies(int page, String includeGenres) {
        // show shimmer animation
        mShimmer.startShimmer();
        mShimmer.setVisibility(View.VISIBLE);
        viewModel.discoverMovies(page, includeGenres);
    }

    /**
     * Fetching discovered tvShows
     */
    private void fetchDiscoverTvShows(int page, String includeGenres) {
        // show shimmer animation
        mShimmer.startShimmer();
        mShimmer.setVisibility(View.VISIBLE);
        viewModel.discoverTvShows(page, includeGenres);
    }

    /**
     * Observe when MovieData List LiveData changed
     */
    public Observer<ArrayList<MovieData>> getMoviesObserver() {
        return movies -> {
            if (movies != null){
                // hide shimmer animation
                mShimmer.stopShimmer();
                mShimmer.setVisibility(View.GONE);

                if (movies.size() > 0) {
                    // append data to adapter
                    moviesAdapter.appendMovies(movies);

                    // attach onScrollListener to RecyclerView
                    mRcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            // when scrolling up
                            if (dy > 0) {
                                final int visibleThreshold = 5 * mLayoutMgr.getSpanCount();

                                // get the number of all items in recyclerView
                                int totalItemCount = mLayoutMgr.getItemCount();
                                // get the last visible item's position
                                int lastVisibleItem = mLayoutMgr.findLastCompletelyVisibleItemPosition();

                                if (totalItemCount <= lastVisibleItem + visibleThreshold) {
                                    // detach current OnScrollListener
                                    mRcView.removeOnScrollListener(this);

                                    // append nextPage data to recyclerView
                                    currentPage++;
                                    fetchDiscoverMovies(currentPage, includeGenres);
                                }
                            }
                        }
                    });
                }

                Log.d(LOG_TAG, "discover movies: data fetched successfully");
            }
        };
    }

    /**
     * Observe when TvShowData List LiveData changed
     */
    public Observer<ArrayList<TvShowData>> getTvShowsObserver() {
        return tvShows -> {
            if (tvShows != null){
                // hide shimmer animation
                mShimmer.stopShimmer();
                mShimmer.setVisibility(View.GONE);

                if (tvShows.size() > 0) {
                    // append data to adapter
                    tvShowsAdapter.appendTvShows(tvShows);

                    // attach onScrollListener to RecyclerView
                    mRcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            // when scrolling up
                            if (dy > 0) {
                                final int visibleThreshold = 5 * mLayoutMgr.getSpanCount();

                                // get the number of all items in recyclerView
                                int totalItemCount = mLayoutMgr.getItemCount();
                                // get the last visible item's position
                                int lastVisibleItem = mLayoutMgr.findLastCompletelyVisibleItemPosition();

                                if (totalItemCount <= lastVisibleItem + visibleThreshold) {
                                    // detach current OnScrollListener
                                    mRcView.removeOnScrollListener(this);

                                    // append nextPage data to recyclerView
                                    currentPage++;
                                    fetchDiscoverTvShows(currentPage, includeGenres);
                                }
                            }
                        }
                    });
                }

                Log.d(LOG_TAG, "discover tvShows: data fetched successfully");
            }
        };
    }


    /**
     * Update results
     */
    private void updateData(String mediaType) {
        // set default page
        currentPage = 1;

        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:
                moviesAdapter.removeAllMovies();
                fetchDiscoverMovies(currentPage, includeGenres);
                break;
            case StaticParameter.MediaType.TV:
                tvShowsAdapter.removeAllTvShows();
                fetchDiscoverTvShows(currentPage, includeGenres);
                break;
        }

    }

}