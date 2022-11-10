package com.example.movieinfo.view.tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.movieinfo.view.MediaDetailsActivity;
import com.example.movieinfo.view.adapter.CastsAdapter;
import com.example.movieinfo.view.adapter.MoviesAdapter;
import com.example.movieinfo.view.adapter.TvShowsAdapter;
import com.example.movieinfo.viewmodel.MoviesViewModel;
import com.example.movieinfo.viewmodel.SimilarTabViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class SimilarTab extends Fragment implements MoviesAdapter.IMovieListener, TvShowsAdapter.ITvShowListener {

    private final String LOG_TAG = "SimilarTab";

    private Context context;

    private SimilarTabViewModel viewModel;

    private String mediaType;
    private long id;
    private int currentPage;

    private ShimmerFrameLayout mShimmer;

    private SwipeRefreshLayout pullToRefresh;
    private RecyclerView similar_RcView;
    private MoviesAdapter moviesAdapter;
    private TvShowsAdapter tvShowsAdapter;
    private GridLayoutManager mLayoutMgr;

    public SimilarTab(String mediaType, long id) {
        this.mediaType = mediaType;
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

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
        return inflater.inflate(R.layout.fragment_similar_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        similar_RcView = view.findViewById(R.id.recycler_similar);
        pullToRefresh = view.findViewById(R.id.swiperefresh);
        mShimmer = view.findViewById(R.id.shimmer_similar);

        // Initialize Recycler Adapter
        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:
                moviesAdapter = new MoviesAdapter(this);
                // Set adapter
                similar_RcView.setAdapter(moviesAdapter);
                break;
            case StaticParameter.MediaType.TV:
                tvShowsAdapter = new TvShowsAdapter(this);
                // Set adapter
                similar_RcView.setAdapter(tvShowsAdapter);
                break;
        }

        // Set NestedScrollingEnable
        similar_RcView.setNestedScrollingEnabled(true);

        // Initialize gridLayoutManager
        mLayoutMgr = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);

        // Set layoutManager
        similar_RcView.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));

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
                similar_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                similar_RcView.removeOnScrollListener(this);

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
                similar_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                similar_RcView.removeOnScrollListener(this);

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

    /**
     * Callback when tvShow item get clicked
     *
     * @param tvShow tvShow data
     */
    @Override
    public void onTvShowClick(TvShowData tvShow) {
        Intent intent = new Intent(getContext(), MediaDetailsActivity.class);
        intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY, StaticParameter.MediaType.TV);
        intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_TVSHOW_ID_KEY, tvShow.getId());
        startActivity(intent);
        // set the custom transition animation
        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}