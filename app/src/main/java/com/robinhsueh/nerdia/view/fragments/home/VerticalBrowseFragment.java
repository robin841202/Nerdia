package com.robinhsueh.nerdia.view.fragments.home;

import android.os.Bundle;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robinhsueh.nerdia.R;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.model.tvshow.TvShowData;
import com.robinhsueh.nerdia.view.adapter.EmptyDataObserver;
import com.robinhsueh.nerdia.view.adapter.MoviesAdapter;
import com.robinhsueh.nerdia.view.adapter.TvShowsAdapter;
import com.robinhsueh.nerdia.viewmodel.MoviesViewModel;
import com.robinhsueh.nerdia.viewmodel.TvShowsViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class VerticalBrowseFragment extends Fragment {

    private final String LOG_TAG = "VerticalBrowseFragment";

    int homeCategory;

    private SwipeRefreshLayout pullToRefresh;
    private ActionBar toolBar;

    private ShimmerFrameLayout mShimmer;

    private MoviesAdapter movieAdapter;
    private TvShowsAdapter tvShowAdapter;
    private RecyclerView mRcView;
    private GridLayoutManager mLayoutMgr;
    private int currentPage;

    private MoviesViewModel moviesViewModel;
    private TvShowsViewModel tvShowsViewModel;

    public VerticalBrowseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get homeCategory argument from HomeFragment
        homeCategory = getArguments().getInt(StaticParameter.ExtraDataKey.EXTRA_DATA_VERTICAL_BROWSE_KEY, 0);

        // set default page
        currentPage = 1;

        // Initialize viewModel, data only survive this fragment lifecycle
        moviesViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        moviesViewModel.init();
        tvShowsViewModel = new ViewModelProvider(this).get(TvShowsViewModel.class);
        tvShowsViewModel.init();

        /**
         * Create a observer that observe when MovieData List LiveData changed
         */
        final Observer<ArrayList<MovieData>> moviesObserver = new Observer<ArrayList<MovieData>>() {
            @Override
            public void onChanged(ArrayList<MovieData> movies) {
                if (movies != null){
                    // hide shimmer animation
                    mShimmer.stopShimmer();
                    mShimmer.setVisibility(View.GONE);

                    // append data to adapter
                    movieAdapter.appendMovies(movies);
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
                                    fetchData(homeCategory);
                                }
                            }
                        }
                    });
                    Log.d(LOG_TAG, "movies: data fetched successfully");
                }
            }
        };

        /**
         * Create a observer that observe when TvShowData List LiveData changed
         */
        final Observer<ArrayList<TvShowData>> tvShowsObserver = new Observer<ArrayList<TvShowData>>() {
            @Override
            public void onChanged(ArrayList<TvShowData> tvShows) {
                if (tvShows != null){
                    // hide shimmer animation
                    mShimmer.stopShimmer();
                    mShimmer.setVisibility(View.GONE);

                    // append data to adapter
                    tvShowAdapter.appendTvShows(tvShows);
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
                                    fetchData(homeCategory);
                                }
                            }
                        }
                    });
                    Log.d(LOG_TAG, "tvShows: data fetched successfully");
                }
            }
        };

        // Set observer
        switch (homeCategory) {
            case StaticParameter.HomeCategory.UPCOMING_MOVIES:
                moviesViewModel.getUpcomingMoviesLiveData().observe(this, moviesObserver);
                break;
            case StaticParameter.HomeCategory.NOWPLAYING_MOVIES:
                moviesViewModel.getNowPlayingMoviesLiveData().observe(this, moviesObserver);
                break;
            case StaticParameter.HomeCategory.TRENDING_MOVIES:
                moviesViewModel.getTrendingMoviesLiveData().observe(this, moviesObserver);
                break;
            case StaticParameter.HomeCategory.POPULAR_MOVIES:
                moviesViewModel.getPopularMoviesLiveData().observe(this, moviesObserver);
                break;
            case StaticParameter.HomeCategory.POPULAR_TVSHOWS:
                tvShowsViewModel.getPopularTvShowsLiveData().observe(this, tvShowsObserver);
                break;
            case StaticParameter.HomeCategory.TRENDING_TVSHOWS:
                tvShowsViewModel.getTrendingTvShowsLiveData().observe(this, tvShowsObserver);
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
        return inflater.inflate(R.layout.fragment_general_gridshimmer_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        mShimmer = view.findViewById(R.id.shimmer);
        mRcView = view.findViewById(R.id.recycler);
        pullToRefresh = view.findViewById(R.id.swiperefresh);
        toolBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        View emptyDataView = view.findViewById(R.id.empty_data_hint);

        initRecyclerView(emptyDataView);

        // Start getting data
        fetchData(homeCategory);

        // Set SwipeRefreshListener
        pullToRefresh.setOnRefreshListener(() -> {
            resetResults(homeCategory);
            Log.d(LOG_TAG, "onRefresh");
            pullToRefresh.setRefreshing(false);
        });
    }

    /**
     * Initialize RecyclerView
     */
    private void initRecyclerView(View emptyDataView) {
        // Initialize Adapter
        movieAdapter = new MoviesAdapter((AppCompatActivity) getActivity());
        tvShowAdapter = new TvShowsAdapter((AppCompatActivity) getActivity());

        // Initialize gridLayoutManager
        mLayoutMgr = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);

        // Initialize EmptyStateObserver
        EmptyDataObserver emptyDataObserver = new EmptyDataObserver(mRcView, emptyDataView);

        // Set Adapter
        switch (homeCategory) {
            case StaticParameter.HomeCategory.UPCOMING_MOVIES:
            case StaticParameter.HomeCategory.NOWPLAYING_MOVIES:
            case StaticParameter.HomeCategory.TRENDING_MOVIES:
            case StaticParameter.HomeCategory.POPULAR_MOVIES:
                mRcView.setAdapter(movieAdapter);
                // Set EmptyStateObserver
                movieAdapter.registerAdapterDataObserver(emptyDataObserver);
                break;
            case StaticParameter.HomeCategory.POPULAR_TVSHOWS:
            case StaticParameter.HomeCategory.TRENDING_TVSHOWS:
                mRcView.setAdapter(tvShowAdapter);
                // Set EmptyStateObserver
                tvShowAdapter.registerAdapterDataObserver(emptyDataObserver);
                break;
            default:
                // do nothing
                break;
        }

        // Set layoutManager
        mRcView.setLayoutManager(mLayoutMgr);
    }

    /**
     * (Private) Fetch data depends on what kinds of homeCategory
     *
     * @param homeCategory StaticParameter.HomeCategory value
     */
    private void fetchData(int homeCategory) {
        // show shimmer animation
        mShimmer.startShimmer();
        mShimmer.setVisibility(View.VISIBLE);

        switch (homeCategory) {
            case StaticParameter.HomeCategory.UPCOMING_MOVIES:
                // set toolbar title
                toolBar.setTitle(getString(R.string.title_upcoming_movies));
                moviesViewModel.getUpcomingMovies(currentPage);
                break;
            case StaticParameter.HomeCategory.NOWPLAYING_MOVIES:
                // set toolbar title
                toolBar.setTitle(getString(R.string.title_now_playing_movies));
                moviesViewModel.getNowPlayingMovies(currentPage);
                break;
            case StaticParameter.HomeCategory.TRENDING_MOVIES:
                // set toolbar title
                toolBar.setTitle(getString(R.string.title_trending_movies));
                moviesViewModel.getTrendingMovies(StaticParameter.TimeWindow.WEEKLY, currentPage);
                break;
            case StaticParameter.HomeCategory.POPULAR_MOVIES:
                // set toolbar title
                toolBar.setTitle(getString(R.string.title_popular_movies));
                moviesViewModel.getPopularMovies(currentPage);
                break;
            case StaticParameter.HomeCategory.POPULAR_TVSHOWS:
                // set toolbar title
                toolBar.setTitle(getString(R.string.title_popular_tvShows));
                tvShowsViewModel.getPopularTvShows(currentPage);
                break;
            case StaticParameter.HomeCategory.TRENDING_TVSHOWS:
                // set toolbar title
                toolBar.setTitle(getString(R.string.title_trending_tvShows));
                tvShowsViewModel.getTrendingTvShows(StaticParameter.TimeWindow.WEEKLY, currentPage);
                break;
            default:
                // do nothing
                return;
        }

    }


    /**
     * Reset results
     */
    private void resetResults(int homeCategory) {
        // set default page
        currentPage = 1;

        // remove data in adapter
        switch (homeCategory) {
            case StaticParameter.HomeCategory.UPCOMING_MOVIES:
            case StaticParameter.HomeCategory.NOWPLAYING_MOVIES:
            case StaticParameter.HomeCategory.TRENDING_MOVIES:
            case StaticParameter.HomeCategory.POPULAR_MOVIES:
                movieAdapter.removeAllMovies();
                break;
            case StaticParameter.HomeCategory.POPULAR_TVSHOWS:
            case StaticParameter.HomeCategory.TRENDING_TVSHOWS:
                tvShowAdapter.removeAllTvShows();
                break;
            default:
                // do nothing
                break;
        }

        // populate data to UI
        fetchData(homeCategory);

    }

}