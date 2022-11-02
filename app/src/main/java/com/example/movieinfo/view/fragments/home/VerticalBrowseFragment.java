package com.example.movieinfo.view.fragments.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.tvshow.TvShowData;
import com.example.movieinfo.view.MediaDetailsActivity;
import com.example.movieinfo.view.adapter.MoviesAdapter;
import com.example.movieinfo.view.adapter.TvShowsAdapter;
import com.example.movieinfo.viewmodel.MoviesViewModel;
import com.example.movieinfo.viewmodel.TvShowsViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class VerticalBrowseFragment extends Fragment implements MoviesAdapter.IMovieListener, TvShowsAdapter.ITvShowListener {

    private final String LOG_TAG = "VerticalBrowseFragment";

    int homeCategory;

    private SwipeRefreshLayout pullToRefresh;
    private ActionBar toolBar;

    private ShimmerFrameLayout verticalBrowse_Shimmer;

    private MoviesAdapter verticalBrowseAdapter_movie;
    private TvShowsAdapter verticalBrowseAdapter_tv;
    private RecyclerView verticalBrowse_RcView;
    private GridLayoutManager verticalBrowseLayoutMgr;
    private int verticalBrowsePage;

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
        verticalBrowsePage = 1;

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
                // hide shimmer animation
                verticalBrowse_Shimmer.stopShimmer();
                verticalBrowse_Shimmer.setVisibility(View.GONE);

                // append data to adapter
                verticalBrowseAdapter_movie.appendMovies(movies);
                // attach onScrollListener to RecyclerView
                verticalBrowse_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        // get the number of all items in recyclerView
                        int totalItemCount = verticalBrowseLayoutMgr.getItemCount();
                        // get the number of current items attached to recyclerView
                        int visibleItemCount = verticalBrowseLayoutMgr.getChildCount();
                        // get the first visible item's position
                        int firstVisibleItem = verticalBrowseLayoutMgr.findFirstVisibleItemPosition();

                        if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                            // detach current OnScrollListener
                            verticalBrowse_RcView.removeOnScrollListener(this);

                            // append nextPage data to recyclerView
                            verticalBrowsePage++;
                            fetchData(homeCategory);
                        }
                    }
                });

                Log.d(LOG_TAG, "movies: data fetched successfully");
            }
        };

        /**
         * Create a observer that observe when TvShowData List LiveData changed
         */
        final Observer<ArrayList<TvShowData>> tvShowsObserver = new Observer<ArrayList<TvShowData>>() {
            @Override
            public void onChanged(ArrayList<TvShowData> tvShows) {
                // hide shimmer animation
                verticalBrowse_Shimmer.stopShimmer();
                verticalBrowse_Shimmer.setVisibility(View.GONE);

                // append data to adapter
                verticalBrowseAdapter_tv.appendTvShows(tvShows);
                // attach onScrollListener to RecyclerView
                verticalBrowse_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                        // get the number of all items in recyclerView
                        int totalItemCount = verticalBrowseLayoutMgr.getItemCount();
                        // get the number of current items attached to recyclerView
                        int visibleItemCount = verticalBrowseLayoutMgr.getChildCount();
                        // get the first visible item's position
                        int firstVisibleItem = verticalBrowseLayoutMgr.findFirstVisibleItemPosition();

                        if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                            // detach current OnScrollListener
                            verticalBrowse_RcView.removeOnScrollListener(this);

                            // append nextPage data to recyclerView
                            verticalBrowsePage++;
                            fetchData(homeCategory);
                        }
                    }
                });

                Log.d(LOG_TAG, "tvShows: data fetched successfully");
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
        View root = inflater.inflate(R.layout.fragment_vertical_browse, container, false);

        // Get Views
        verticalBrowse_Shimmer = root.findViewById(R.id.shimmer_vertical_browse);
        verticalBrowse_RcView = root.findViewById(R.id.recycler_vertical_browse);
        pullToRefresh = root.findViewById(R.id.swiperefresh_vertical_browse);
        toolBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        // Initialize Adapter
        verticalBrowseAdapter_movie = new MoviesAdapter(new ArrayList<>(), this);
        verticalBrowseAdapter_tv = new TvShowsAdapter(new ArrayList<>(), this);

        // Initialize gridLayoutManager
        verticalBrowseLayoutMgr = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);

        // Set Adapter
        switch (homeCategory) {
            case StaticParameter.HomeCategory.UPCOMING_MOVIES:
            case StaticParameter.HomeCategory.NOWPLAYING_MOVIES:
            case StaticParameter.HomeCategory.TRENDING_MOVIES:
            case StaticParameter.HomeCategory.POPULAR_MOVIES:
                verticalBrowse_RcView.setAdapter(verticalBrowseAdapter_movie);
                break;
            case StaticParameter.HomeCategory.POPULAR_TVSHOWS:
            case StaticParameter.HomeCategory.TRENDING_TVSHOWS:
                verticalBrowse_RcView.setAdapter(verticalBrowseAdapter_tv);
                break;
            default:
                // do nothing
                break;
        }


        // Set layoutManager
        verticalBrowse_RcView.setLayoutManager(verticalBrowseLayoutMgr);

        // Start getting data
        fetchData(homeCategory);

        // Set SwipeRefreshListener
        pullToRefresh.setOnRefreshListener(() -> {
            resetResults(homeCategory);
            Log.d(LOG_TAG, "onRefresh");
            pullToRefresh.setRefreshing(false);
        });
        return root;
    }


    /**
     * (Private) Fetch data depends on what kinds of homeCategory
     *
     * @param homeCategory StaticParameter.HomeCategory value
     */
    private void fetchData(int homeCategory) {
        // show shimmer animation
        verticalBrowse_Shimmer.startShimmer();
        verticalBrowse_Shimmer.setVisibility(View.VISIBLE);

        switch (homeCategory) {
            case StaticParameter.HomeCategory.UPCOMING_MOVIES:
                // set toolbar title
                toolBar.setTitle(getString(R.string.title_upcoming_movies));
                moviesViewModel.getUpcomingMovies(verticalBrowsePage);
                break;
            case StaticParameter.HomeCategory.NOWPLAYING_MOVIES:
                // set toolbar title
                toolBar.setTitle(getString(R.string.title_now_playing_movies));
                moviesViewModel.getNowPlayingMovies(verticalBrowsePage);
                break;
            case StaticParameter.HomeCategory.TRENDING_MOVIES:
                // set toolbar title
                toolBar.setTitle(getString(R.string.title_trending_movies));
                moviesViewModel.getTrendingMovies(StaticParameter.TimeWindow.WEEKLY, verticalBrowsePage);
                break;
            case StaticParameter.HomeCategory.POPULAR_MOVIES:
                // set toolbar title
                toolBar.setTitle(getString(R.string.title_popular_movies));
                moviesViewModel.getPopularMovies(verticalBrowsePage);
                break;
            case StaticParameter.HomeCategory.POPULAR_TVSHOWS:
                // set toolbar title
                toolBar.setTitle(getString(R.string.title_popular_tvShows));
                tvShowsViewModel.getPopularTvShows(verticalBrowsePage);
                break;
            case StaticParameter.HomeCategory.TRENDING_TVSHOWS:
                // set toolbar title
                toolBar.setTitle(getString(R.string.title_trending_tvShows));
                tvShowsViewModel.getTrendingTvShows(StaticParameter.TimeWindow.WEEKLY, verticalBrowsePage);
                break;
            default:
                // do nothing
                return;
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


    /**
     * Reset results
     */
    private void resetResults(int homeCategory) {
        // set default page
        verticalBrowsePage = 1;

        // remove data in adapter
        switch (homeCategory) {
            case StaticParameter.HomeCategory.UPCOMING_MOVIES:
            case StaticParameter.HomeCategory.NOWPLAYING_MOVIES:
            case StaticParameter.HomeCategory.TRENDING_MOVIES:
            case StaticParameter.HomeCategory.POPULAR_MOVIES:
                verticalBrowseAdapter_movie.removeAllMovies();
                break;
            case StaticParameter.HomeCategory.POPULAR_TVSHOWS:
            case StaticParameter.HomeCategory.TRENDING_TVSHOWS:
                verticalBrowseAdapter_tv.removeAllTvShows();
                break;
            default:
                // do nothing
                break;
        }

        // populate data to UI
        fetchData(homeCategory);

    }

}