package com.example.movieinfo.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.example.movieinfo.view.MediaDetailsActivity;
import com.example.movieinfo.view.MoviesAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements MoviesAdapter.IMovieListener {

    private final String LOG_TAG = "HomeFragment";
    // Define extra data key for passing data to other activities
    public static final String EXTRA_DATA_MOVIE_KEY = "EXTRA_DATA_MOVIE";

    private final MovieRepository movieRepository = new MovieRepository();

    private SwipeRefreshLayout pullToRefresh;

    private ProgressBar upcomingMovies_PB;
    private MoviesAdapter upcomingMoviesAdapter;
    private RecyclerView upcomingMovies_RcView;
    private LinearLayoutManager upcomingMoviesLayoutMgr;
    private int upcomingMoviesPage;

    private ProgressBar nowPlayingMovies_PB;
    private MoviesAdapter nowPlayingMoviesAdapter;
    private RecyclerView nowPlayingMovies_RcView;
    private LinearLayoutManager nowPlayingMoviesLayoutMgr;
    private int nowPlayingMoviesPage;

    private ProgressBar trendingMovies_PB;
    private MoviesAdapter trendingMoviesAdapter;
    private RecyclerView trendingMovies_RcView;
    private LinearLayoutManager trendingMoviesLayoutMgr;
    private int trendingMoviesPage;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set default page
        upcomingMoviesPage = 1;
        nowPlayingMoviesPage = 1;
        trendingMoviesPage = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        // Get Views
        upcomingMovies_PB = root.findViewById(R.id.pb_upcoming_movies);
        upcomingMovies_RcView = root.findViewById(R.id.recycler_upcoming_movies);
        nowPlayingMovies_PB = root.findViewById(R.id.pb_now_playing_movies);
        nowPlayingMovies_RcView = root.findViewById(R.id.recycler_now_playing_movies);
        trendingMovies_PB = root.findViewById(R.id.pb_trending_movies);
        trendingMovies_RcView = root.findViewById(R.id.recycler_trending_movies);
        pullToRefresh = root.findViewById(R.id.swiperefresh);

        // Initialize movieAdapter
        upcomingMoviesAdapter = new MoviesAdapter(new ArrayList<>(), this);
        nowPlayingMoviesAdapter = new MoviesAdapter(new ArrayList<>(), this);
        trendingMoviesAdapter = new MoviesAdapter(new ArrayList<>(), this);

        // Initialize linearLayoutManager
        upcomingMoviesLayoutMgr = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        nowPlayingMoviesLayoutMgr = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        trendingMoviesLayoutMgr = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        // Set adapter
        upcomingMovies_RcView.setAdapter(upcomingMoviesAdapter);
        nowPlayingMovies_RcView.setAdapter(nowPlayingMoviesAdapter);
        trendingMovies_RcView.setAdapter(trendingMoviesAdapter);

        // Set layoutManager
        upcomingMovies_RcView.setLayoutManager(upcomingMoviesLayoutMgr);
        nowPlayingMovies_RcView.setLayoutManager(nowPlayingMoviesLayoutMgr);
        trendingMovies_RcView.setLayoutManager(trendingMoviesLayoutMgr);

        // Start getting data
        getUpcomingMovies();
        getNowPlayingMovies();
        getTrendingMovies();

        // Set SwipeRefreshListener
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUpcomingMovies();
                Log.d(LOG_TAG, "onRefreshUpcomingMovies");

                getNowPlayingMovies();
                Log.d(LOG_TAG, "onRefreshNowPlayingMovies");

                getTrendingMovies();
                Log.d(LOG_TAG, "onRefreshTrendingMovies");

                pullToRefresh.setRefreshing(false);
            }
        });

        return root;
    }

    // region Upcoming Movies

    /**
     * Get Upcoming Movies
     */
    public void getUpcomingMovies() {
        try {
            // show progressBar
            upcomingMovies_PB.setVisibility(View.VISIBLE);
            Method onUpcomingMoviesFetched = getClass().getMethod("onUpcomingMoviesFetched", ArrayList.class);
            Method onFetchDataError = getClass().getMethod("onFetchDataError");
            movieRepository.getUpcomingMovies(upcomingMoviesPage, this, onUpcomingMoviesFetched, onFetchDataError);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Callback when get upcoming movies successfully
     *
     * @param movie_list upcoming movies data
     */
    public void onUpcomingMoviesFetched(ArrayList<MovieData> movie_list) {
        // hide progressBar
        upcomingMovies_PB.setVisibility(View.GONE);

        // append data to adapter
        upcomingMoviesAdapter.appendMovies(movie_list);

        // attach onScrollListener to RecyclerView
        upcomingMovies_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                // get the number of all items in recyclerView
                int totalItemCount = upcomingMoviesLayoutMgr.getItemCount();
                // get the number of current items attached to recyclerView
                int visibleItemCount = upcomingMoviesLayoutMgr.getChildCount();
                // get the first visible item's position
                int firstVisibleItem = upcomingMoviesLayoutMgr.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    // detach current OnScrollListener
                    upcomingMovies_RcView.removeOnScrollListener(this);

                    // append nextPage data to recyclerView
                    upcomingMoviesPage++;
                    getUpcomingMovies();
                }
            }
        });

        Log.d(LOG_TAG, "upcoming movies: data fetched successfully");
    }

    // endregion

    // region Now-Playing Movies

    /**
     * Get Now-Playing Movies
     */
    public void getNowPlayingMovies() {
        try {
            // show progressBar
            nowPlayingMovies_PB.setVisibility(View.VISIBLE);
            Method onNowPlayingMoviesFetched = getClass().getMethod("onNowPlayingMoviesFetched", ArrayList.class);
            Method onFetchDataError = getClass().getMethod("onFetchDataError");
            movieRepository.getNowPlayingMovies(nowPlayingMoviesPage, this, onNowPlayingMoviesFetched, onFetchDataError);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Callback when get now-playing movies successfully
     *
     * @param movie_list now-playing movies data
     */
    public void onNowPlayingMoviesFetched(ArrayList<MovieData> movie_list) {
        // hide progressBar
        nowPlayingMovies_PB.setVisibility(View.GONE);
        // append data to adapter
        nowPlayingMoviesAdapter.appendMovies(movie_list);
        // attach onScrollListener to RecyclerView
        nowPlayingMovies_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                // get the number of all items in recyclerView
                int totalItemCount = nowPlayingMoviesLayoutMgr.getItemCount();
                // get the number of current items attached to recyclerView
                int visibleItemCount = nowPlayingMoviesLayoutMgr.getChildCount();
                // get the first visible item's position
                int firstVisibleItem = nowPlayingMoviesLayoutMgr.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    // detach current OnScrollListener
                    nowPlayingMovies_RcView.removeOnScrollListener(this);

                    // append nextPage data to recyclerView
                    nowPlayingMoviesPage++;
                    getNowPlayingMovies();
                }
            }
        });

        Log.d(LOG_TAG, "nowPlaying movies: data fetched successfully");
    }

    // endregion

    // region Trending Movies

    /**
     * Get Trending Movies
     */
    public void getTrendingMovies() {
        try {
            // show progressBar
            trendingMovies_PB.setVisibility(View.VISIBLE);
            Method onTrendingMoviesFetched = getClass().getMethod("onTrendingMoviesFetched", ArrayList.class);
            Method onFetchDataError = getClass().getMethod("onFetchDataError");
            movieRepository.getTrendingMovies(StaticParameter.TimeWindow.WEEKLY, trendingMoviesPage, this, onTrendingMoviesFetched, onFetchDataError);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Callback when get trending movies successfully
     *
     * @param movie_list trending movies data
     */
    public void onTrendingMoviesFetched(ArrayList<MovieData> movie_list) {
        // hide progressBar
        trendingMovies_PB.setVisibility(View.GONE);
        // append data to adapter
        trendingMoviesAdapter.appendMovies(movie_list);
        // attach onScrollListener to RecyclerView
        trendingMovies_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                // get the number of all items in recyclerView
                int totalItemCount = trendingMoviesLayoutMgr.getItemCount();
                // get the number of current items attached to recyclerView
                int visibleItemCount = trendingMoviesLayoutMgr.getChildCount();
                // get the first visible item's position
                int firstVisibleItem = trendingMoviesLayoutMgr.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    // detach current OnScrollListener
                    trendingMovies_RcView.removeOnScrollListener(this);

                    // append nextPage data to recyclerView
                    trendingMoviesPage++;
                    getTrendingMovies();
                }
            }
        });

        Log.d(LOG_TAG, "trending movies: data fetched successfully");
    }

    // endregion


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
        intent.putExtra(EXTRA_DATA_MOVIE_KEY, movie);
        startActivity(intent);
        // set the custom transition animation
        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}