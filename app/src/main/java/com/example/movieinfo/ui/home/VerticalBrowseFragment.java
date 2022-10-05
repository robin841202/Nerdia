package com.example.movieinfo.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.repository.MovieRepository;
import com.example.movieinfo.model.repository.TvShowRepository;
import com.example.movieinfo.model.tvshow.TvShowData;
import com.example.movieinfo.view.MediaDetailsActivity;
import com.example.movieinfo.view.adapter.MoviesAdapter;
import com.example.movieinfo.view.adapter.TvShowsAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class VerticalBrowseFragment extends Fragment implements MoviesAdapter.IMovieListener, TvShowsAdapter.ITvShowListener {

    private final String LOG_TAG = "VerticalBrowseFragment";

    int homeCategory;

    private final MovieRepository movieRepository = new MovieRepository();
    private final TvShowRepository tvShowRepository = new TvShowRepository();

    private SwipeRefreshLayout pullToRefresh;
    private ActionBar toolBar;

    private MoviesAdapter verticalBrowseAdapter_movie;
    private TvShowsAdapter verticalBrowseAdapter_tv;
    private RecyclerView verticalBrowse_RcView;
    private GridLayoutManager verticalBrowseLayoutMgr;
    private int verticalBrowsePage;


    public VerticalBrowseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set default page
        verticalBrowsePage = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_vertical_browse, container, false);

        // Get Views
        verticalBrowse_RcView = root.findViewById(R.id.recycler_vertical_browse);
        pullToRefresh = root.findViewById(R.id.swiperefresh_vertical_browse);
        toolBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        // Get homeCategory argument from HomeFragment
        homeCategory = getArguments().getInt(HomeFragment.EXTRA_DATA_VERTICAL_BROWSE_KEY, 0);

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
        populateData(homeCategory);

        // Set SwipeRefreshListener
        pullToRefresh.setOnRefreshListener(() -> {
            populateData(homeCategory);
            Log.d(LOG_TAG, "onRefresh");
            pullToRefresh.setRefreshing(false);
        });
        return root;
    }

    private void populateData(int homeCategory) {
        try {
            Method onMoviesFetched = getClass().getMethod("onMoviesFetched", ArrayList.class);
            Method onTvShowsFetched = getClass().getMethod("onTvShowsFetched", ArrayList.class);
            Method onFetchDataError = getClass().getMethod("onFetchDataError");
            switch (homeCategory) {
                case StaticParameter.HomeCategory.UPCOMING_MOVIES:
                    // set toolbar title
                    toolBar.setTitle(getString(R.string.title_upcoming_movies));
                    movieRepository.getUpcomingMovies(verticalBrowsePage, this, onMoviesFetched, onFetchDataError);
                    break;
                case StaticParameter.HomeCategory.NOWPLAYING_MOVIES:
                    // set toolbar title
                    toolBar.setTitle(getString(R.string.title_now_playing_movies));
                    movieRepository.getNowPlayingMovies(verticalBrowsePage, this, onMoviesFetched, onFetchDataError);
                    break;
                case StaticParameter.HomeCategory.TRENDING_MOVIES:
                    // set toolbar title
                    toolBar.setTitle(getString(R.string.title_trending_movies));
                    movieRepository.getTrendingMovies(StaticParameter.TimeWindow.WEEKLY, verticalBrowsePage, this, onMoviesFetched, onFetchDataError);
                    break;
                case StaticParameter.HomeCategory.POPULAR_MOVIES:
                    // set toolbar title
                    toolBar.setTitle(getString(R.string.title_popular_movies));
                    movieRepository.getPopularMovies(verticalBrowsePage, this, onMoviesFetched, onFetchDataError);
                    break;
                case StaticParameter.HomeCategory.POPULAR_TVSHOWS:
                    // set toolbar title
                    toolBar.setTitle(getString(R.string.title_popular_tvShows));
                    tvShowRepository.getPopularTvShows(verticalBrowsePage, this, onTvShowsFetched, onFetchDataError);
                    break;
                case StaticParameter.HomeCategory.TRENDING_TVSHOWS:
                    // set toolbar title
                    toolBar.setTitle(getString(R.string.title_trending_tvShows));
                    tvShowRepository.getTrendingTvShows(StaticParameter.TimeWindow.WEEKLY, verticalBrowsePage, this, onTvShowsFetched, onFetchDataError);
                    break;
                default:
                    // do nothing
                    return;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }


    /**
     * Callback when get movies successfully
     *
     * @param movie_list movies data
     */
    public void onMoviesFetched(ArrayList<MovieData> movie_list) {

        // append data to adapter
        verticalBrowseAdapter_movie.appendMovies(movie_list);
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
                    populateData(homeCategory);
                }
            }
        });

        Log.d(LOG_TAG, "movies: data fetched successfully");
    }

    /**
     * Callback when get tvShows successfully
     *
     * @param tvShow_list tvShows data
     */
    public void onTvShowsFetched(ArrayList<TvShowData> tvShow_list) {
        // append data to adapter
        verticalBrowseAdapter_tv.appendTvShows(tvShow_list);
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
                    populateData(homeCategory);
                }
            }
        });

        Log.d(LOG_TAG, "tvShows: data fetched successfully");
    }


    /**
     * Callback when data fetched fail
     */
    public void onFetchDataError() {
        Log.d(LOG_TAG, "data fetched fail");
    }


    /**
     * Callback when movie item get clicked
     *
     * @param movie movie data
     */
    @Override
    public void onMovieClick(MovieData movie) {
        Intent intent = new Intent(getContext(), MediaDetailsActivity.class);
        intent.putExtra(HomeFragment.EXTRA_DATA_MEDIA_TYPE_KEY, StaticParameter.MediaType.MOVIE);
        intent.putExtra(HomeFragment.EXTRA_DATA_MOVIE_KEY, movie);
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
        intent.putExtra(HomeFragment.EXTRA_DATA_MEDIA_TYPE_KEY, StaticParameter.MediaType.TV);
        intent.putExtra(HomeFragment.EXTRA_DATA_TVSHOW_KEY, tvShow);
        startActivity(intent);
        // set the custom transition animation
        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}