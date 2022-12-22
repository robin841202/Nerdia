package com.example.movieinfo.view.tab;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.tvshow.TvShowData;
import com.example.movieinfo.view.adapter.EmptyDataObserver;
import com.example.movieinfo.view.adapter.MoviesAdapter;
import com.example.movieinfo.view.adapter.TvShowsAdapter;
import com.example.movieinfo.viewmodel.SimilarTabViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class SimilarTab extends Fragment {

    private final String LOG_TAG = "SimilarTab";

    private Context context;

    private SimilarTabViewModel viewModel;

    private String mediaType;
    private long id;
    private int currentPage;

    private ShimmerFrameLayout mShimmer;

    private SwipeRefreshLayout pullToRefresh;
    private RecyclerView mRcView;
    private MoviesAdapter moviesAdapter;
    private TvShowsAdapter tvShowsAdapter;
    private GridLayoutManager mLayoutMgr;

    public SimilarTab() {
        // Required empty public constructor
    }

    public static SimilarTab newInstance(String mediaType, long id) {
        Bundle args = new Bundle();
        args.putString(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY, mediaType);
        args.putLong("id", id);
        SimilarTab fragment = new SimilarTab();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

        // Get arguments from constructor
        if (getArguments() != null) {
            mediaType = getArguments().getString(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY);
            id = getArguments().getLong("id");
        }

        // set default page
        currentPage = 1;

        // Initialize viewModel, data only survive this fragment lifecycle
        viewModel = new ViewModelProvider(this).get(SimilarTabViewModel.class);
        viewModel.init();

        // Set observer
        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:
                viewModel.getMoviesLiveData().observe(this, getSimilarMoviesObserver());
                break;
            case StaticParameter.MediaType.TV:
                viewModel.getTvShowsLiveData().observe(this, getSimilarTvShowsObserver());
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
        mRcView = view.findViewById(R.id.recycler);
        pullToRefresh = view.findViewById(R.id.swiperefresh);
        mShimmer = view.findViewById(R.id.shimmer);
        View emptyDataView = view.findViewById(R.id.empty_data_hint);

        initRecyclerView(emptyDataView);

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
     * Initialize RecyclerView
     */
    private void initRecyclerView(View emptyDataView) {
        // Initialize EmptyStateObserver
        EmptyDataObserver emptyDataObserver = new EmptyDataObserver(mRcView, emptyDataView);

        // Initialize Recycler Adapter
        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:
                moviesAdapter = new MoviesAdapter((AppCompatActivity) getActivity());
                // Set adapter
                mRcView.setAdapter(moviesAdapter);

                // Register EmptyStateObserver
                moviesAdapter.registerAdapterDataObserver(emptyDataObserver);
                break;
            case StaticParameter.MediaType.TV:
                tvShowsAdapter = new TvShowsAdapter((AppCompatActivity) getActivity());
                // Set adapter
                mRcView.setAdapter(tvShowsAdapter);

                // Register EmptyStateObserver
                tvShowsAdapter.registerAdapterDataObserver(emptyDataObserver);
                break;
        }

        // Set NestedScrollingEnable
        mRcView.setNestedScrollingEnabled(true);

        // Initialize gridLayoutManager
        mLayoutMgr = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);

        // Set layoutManager
        mRcView.setLayoutManager(mLayoutMgr);
    }


    // region Similar Movies

    /**
     * Fetching similar movies
     *
     * @param movieId Movie Id
     * @param page    result page
     */
    private void fetchSimilarMovies(long movieId, int page) {
        if (movieId >= 0) {
            // show shimmer animation
            mShimmer.startShimmer();
            mShimmer.setVisibility(View.VISIBLE);
            viewModel.getSimilarMovies(movieId, page);
        }
    }

    /**
     * Observe when MovieData List LiveData changed
     */
    public Observer<ArrayList<MovieData>> getSimilarMoviesObserver() {
        return movies -> {
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
                                fetchSimilarMovies(id, currentPage);
                            }
                        }
                    }
                });
            }

            Log.d(LOG_TAG, "similar movies: data fetched successfully");
        };
    }

    // endregion


    // region Similar TvShows

    /**
     * Fetching similar tvShows
     *
     * @param tvShowId TvShow Id
     * @param page     result page
     */
    private void fetchSimilarTvShows(long tvShowId, int page) {
        if (tvShowId >= 0) {
            // show shimmer animation
            mShimmer.startShimmer();
            mShimmer.setVisibility(View.VISIBLE);
            viewModel.getSimilarTvShows(tvShowId, page);
        }
    }

    /**
     * Observe when MovieData List LiveData changed
     */
    public Observer<ArrayList<TvShowData>> getSimilarTvShowsObserver() {
        return tvShows -> {
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
                                fetchSimilarTvShows(id, currentPage);
                            }
                        }
                    }
                });
            }

            Log.d(LOG_TAG, "similar tvShows: data fetched successfully");
        };
    }

    // endregion


    /**
     * Update results
     */
    private void updateData(String mediaType) {
        // set default page
        currentPage = 1;

        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:
                moviesAdapter.removeAllMovies();
                fetchSimilarMovies(id, currentPage);
                break;
            case StaticParameter.MediaType.TV:
                tvShowsAdapter.removeAllTvShows();
                fetchSimilarTvShows(id, currentPage);
                break;
        }

    }

}