package com.example.movieinfo.ui.home;

import android.content.Context;
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
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.repository.MovieRepository;
import com.example.movieinfo.view.MoviesAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private final String LOG_TAG = "HomeFragment";
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




    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set default page
        upcomingMoviesPage = 1;
        nowPlayingMoviesPage = 1;
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
        pullToRefresh = root.findViewById(R.id.swiperefresh);

        // Initialize movieAdapter
        upcomingMoviesAdapter = new MoviesAdapter();
        nowPlayingMoviesAdapter = new MoviesAdapter();
        // Initialize linearLayoutManager
        upcomingMoviesLayoutMgr = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        nowPlayingMoviesLayoutMgr = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        // Set adapter
        upcomingMovies_RcView.setAdapter(upcomingMoviesAdapter);
        nowPlayingMovies_RcView.setAdapter(nowPlayingMoviesAdapter);
        // Set layoutManager
        upcomingMovies_RcView.setLayoutManager(upcomingMoviesLayoutMgr);
        nowPlayingMovies_RcView.setLayoutManager(nowPlayingMoviesLayoutMgr);

        // Start getting data
        getUpcomingMovies();
        getNowPlayingMovies();

        // Set SwipeRefreshListener
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUpcomingMovies();
                Log.d(LOG_TAG, "onRefreshUpcoming");

                getNowPlayingMovies();
                Log.d(LOG_TAG, "onRefreshNowPlaying");
                pullToRefresh.setRefreshing(false);
            }
        });

        return root;
    }


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

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2){
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

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2){
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




    /**
     * Callback when data fetched fail
     */
    public void onFetchDataError() {
        Log.d(LOG_TAG, "data fetched fail");
    }





}