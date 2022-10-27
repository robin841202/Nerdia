package com.example.movieinfo.view.fragments.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.tvshow.TvShowData;
import com.example.movieinfo.view.MediaDetailsActivity;
import com.example.movieinfo.view.adapter.MoviesAdapter;
import com.example.movieinfo.view.adapter.TvShowsAdapter;
import com.example.movieinfo.viewmodel.MoviesViewModel;
import com.example.movieinfo.viewmodel.TvShowsViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements MoviesAdapter.IMovieListener, TvShowsAdapter.ITvShowListener {

    private final String LOG_TAG = "HomeFragment";

    private SwipeRefreshLayout pullToRefresh;

    // region Movies Variables
    private MoviesViewModel moviesViewModel;


    private ProgressBar upcomingMovies_PB;
    private MoviesAdapter upcomingMoviesAdapter;
    private RecyclerView upcomingMovies_RcView;
    private LinearLayoutManager upcomingMoviesLayoutMgr;
    private int upcomingMoviesPage;
    private ImageButton upcomingMovies_ImgBtn;

    private ProgressBar nowPlayingMovies_PB;
    private MoviesAdapter nowPlayingMoviesAdapter;
    private RecyclerView nowPlayingMovies_RcView;
    private LinearLayoutManager nowPlayingMoviesLayoutMgr;
    private int nowPlayingMoviesPage;
    private ImageButton nowPlayingMovies_ImgBtn;

    private ProgressBar trendingMovies_PB;
    private MoviesAdapter trendingMoviesAdapter;
    private RecyclerView trendingMovies_RcView;
    private LinearLayoutManager trendingMoviesLayoutMgr;
    private int trendingMoviesPage;
    private ImageButton trendingMovies_ImgBtn;

    private ProgressBar popularMovies_PB;
    private MoviesAdapter popularMoviesAdapter;
    private RecyclerView popularMovies_RcView;
    private LinearLayoutManager popularMoviesLayoutMgr;
    private int popularMoviesPage;
    private ImageButton popularMovies_ImgBtn;
    // endregion

    // region TvShows Variables
    private TvShowsViewModel tvShowsViewModel;

    private ProgressBar popularTvShows_PB;
    private TvShowsAdapter popularTvShowsAdapter;
    private RecyclerView popularTvShows_RcView;
    private LinearLayoutManager popularTvShowsLayoutMgr;
    private int popularTvShowsPage;
    private ImageButton popularTvShows_ImgBtn;

    private ProgressBar trendingTvShows_PB;
    private TvShowsAdapter trendingTvShowsAdapter;
    private RecyclerView trendingTvShows_RcView;
    private LinearLayoutManager trendingTvShowsLayoutMgr;
    private int trendingTvShowsPage;
    private ImageButton trendingTvShows_ImgBtn;
    // endregion

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize viewModel, data only survive this fragment lifecycle
        moviesViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        moviesViewModel.init();
        tvShowsViewModel = new ViewModelProvider(this).get(TvShowsViewModel.class);
        tvShowsViewModel.init();

        // Set observer
        moviesViewModel.getUpcomingMoviesLiveData().observe(this, getUpcomingMoviesObserver());
        moviesViewModel.getNowPlayingMoviesLiveData().observe(this, getNowPlayingMoviesObserver());
        moviesViewModel.getTrendingMoviesLiveData().observe(this, getTrendingMoviesObserver());
        moviesViewModel.getPopularMoviesLiveData().observe(this, getPopularMoviesObserver());
        tvShowsViewModel.getPopularTvShowsLiveData().observe(this, getPopularTvShowsObserver());
        tvShowsViewModel.getTrendingTvShowsLiveData().observe(this, getTrendingTvShowsObserver());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        // Initialize Views
        upcomingMovies_PB = root.findViewById(R.id.pb_upcoming_movies);
        upcomingMovies_RcView = root.findViewById(R.id.recycler_upcoming_movies);
        upcomingMovies_ImgBtn = root.findViewById(R.id.imgBtn_upcoming_movies);
        nowPlayingMovies_PB = root.findViewById(R.id.pb_now_playing_movies);
        nowPlayingMovies_RcView = root.findViewById(R.id.recycler_now_playing_movies);
        nowPlayingMovies_ImgBtn = root.findViewById(R.id.imgBtn_now_playing_movies);
        trendingMovies_PB = root.findViewById(R.id.pb_trending_movies);
        trendingMovies_RcView = root.findViewById(R.id.recycler_trending_movies);
        trendingMovies_ImgBtn = root.findViewById(R.id.imgBtn_trending_movies);
        popularMovies_PB = root.findViewById(R.id.pb_popular_movies);
        popularMovies_RcView = root.findViewById(R.id.recycler_popular_movies);
        popularMovies_ImgBtn = root.findViewById(R.id.imgBtn_popular_movies);
        popularTvShows_PB = root.findViewById(R.id.pb_popular_shows);
        popularTvShows_RcView = root.findViewById(R.id.recycler_popular_shows);
        popularTvShows_ImgBtn = root.findViewById(R.id.imgBtn_popular_shows);
        trendingTvShows_PB = root.findViewById(R.id.pb_trending_shows);
        trendingTvShows_RcView = root.findViewById(R.id.recycler_trending_shows);
        trendingTvShows_ImgBtn = root.findViewById(R.id.imgBtn_trending_shows);
        pullToRefresh = root.findViewById(R.id.swiperefresh);

        // Initialize Adapter
        upcomingMoviesAdapter = new MoviesAdapter(new ArrayList<>(), this);
        nowPlayingMoviesAdapter = new MoviesAdapter(new ArrayList<>(), this);
        trendingMoviesAdapter = new MoviesAdapter(new ArrayList<>(), this);
        popularMoviesAdapter = new MoviesAdapter(new ArrayList<>(), this);
        popularTvShowsAdapter = new TvShowsAdapter(new ArrayList<>(), this);
        trendingTvShowsAdapter = new TvShowsAdapter(new ArrayList<>(), this);

        // Initialize linearLayoutManager
        upcomingMoviesLayoutMgr = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        nowPlayingMoviesLayoutMgr = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        trendingMoviesLayoutMgr = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        popularMoviesLayoutMgr = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        popularTvShowsLayoutMgr = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        trendingTvShowsLayoutMgr = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        // Set adapter
        upcomingMovies_RcView.setAdapter(upcomingMoviesAdapter);
        nowPlayingMovies_RcView.setAdapter(nowPlayingMoviesAdapter);
        trendingMovies_RcView.setAdapter(trendingMoviesAdapter);
        popularMovies_RcView.setAdapter(popularMoviesAdapter);
        popularTvShows_RcView.setAdapter(popularTvShowsAdapter);
        trendingTvShows_RcView.setAdapter(trendingTvShowsAdapter);

        // Set layoutManager
        upcomingMovies_RcView.setLayoutManager(upcomingMoviesLayoutMgr);
        nowPlayingMovies_RcView.setLayoutManager(nowPlayingMoviesLayoutMgr);
        trendingMovies_RcView.setLayoutManager(trendingMoviesLayoutMgr);
        popularMovies_RcView.setLayoutManager(popularMoviesLayoutMgr);
        popularTvShows_RcView.setLayoutManager(popularTvShowsLayoutMgr);
        trendingTvShows_RcView.setLayoutManager(trendingTvShowsLayoutMgr);

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set default page
        upcomingMoviesPage = 1;
        nowPlayingMoviesPage = 1;
        trendingMoviesPage = 1;
        popularMoviesPage = 1;
        popularTvShowsPage = 1;
        trendingTvShowsPage = 1;

        // Start getting data
        updateAllData();


        // Set SwipeRefreshListener
        pullToRefresh.setOnRefreshListener(() -> {
            // reset all results
            resetResults();

            Log.d(LOG_TAG, "onRefreshUpcomingMovies");
            Log.d(LOG_TAG, "onRefreshNowPlayingMovies");
            Log.d(LOG_TAG, "onRefreshTrendingMovies");
            Log.d(LOG_TAG, "onRefreshPopularMovies");
            Log.d(LOG_TAG, "onRefreshPopularTvShows");
            Log.d(LOG_TAG, "onRefreshTrendingTvShows");

            pullToRefresh.setRefreshing(false);
        });

        //  Set ImageButtonListener
        upcomingMovies_ImgBtn.setOnClickListener(v -> {
            showVerticalBrowse(StaticParameter.HomeCategory.UPCOMING_MOVIES);
        });
        nowPlayingMovies_ImgBtn.setOnClickListener(v -> {
            showVerticalBrowse(StaticParameter.HomeCategory.NOWPLAYING_MOVIES);
        });
        trendingMovies_ImgBtn.setOnClickListener(v -> {
            showVerticalBrowse(StaticParameter.HomeCategory.TRENDING_MOVIES);
        });
        popularMovies_ImgBtn.setOnClickListener(v -> {
            showVerticalBrowse(StaticParameter.HomeCategory.POPULAR_MOVIES);
        });
        popularTvShows_ImgBtn.setOnClickListener(v -> {
            showVerticalBrowse(StaticParameter.HomeCategory.POPULAR_TVSHOWS);
        });
        trendingTvShows_ImgBtn.setOnClickListener(v -> {
            showVerticalBrowse(StaticParameter.HomeCategory.TRENDING_TVSHOWS);
        });

    }

    // region MVC methods [without viewModel] (Deprecated)


    // region Upcoming Movies

    /**
     * Get Upcoming Movies (Deprecated)
     */
    /*public void getUpcomingMovies() {
        try {
            // show progressBar
            upcomingMovies_PB.setVisibility(View.VISIBLE);
            Method onUpcomingMoviesFetched = getClass().getMethod("onUpcomingMoviesFetched", ArrayList.class);
            Method onFetchDataError = getClass().getMethod("onFetchDataError");
            movieRepository.getUpcomingMovies(upcomingMoviesPage, this, onUpcomingMoviesFetched, onFetchDataError);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Callback when get upcoming movies successfully (Deprecated)
     *
     * @param movie_list upcoming movies data
     */
    /*public void onUpcomingMoviesFetched(ArrayList<MovieData> movie_list) {
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
    }*/

    // endregion

    // region Now-Playing Movies

    /**
     * Get Now-Playing Movies (Deprecated)
     */
    /*public void getNowPlayingMovies() {
        try {
            // show progressBar
            nowPlayingMovies_PB.setVisibility(View.VISIBLE);
            Method onNowPlayingMoviesFetched = getClass().getMethod("onNowPlayingMoviesFetched", ArrayList.class);
            Method onFetchDataError = getClass().getMethod("onFetchDataError");
            movieRepository.getNowPlayingMovies(nowPlayingMoviesPage, this, onNowPlayingMoviesFetched, onFetchDataError);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Callback when get now-playing movies successfully (Deprecated)
     *
     * @param movie_list now-playing movies data
     */
    /*public void onNowPlayingMoviesFetched(ArrayList<MovieData> movie_list) {
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
    }*/

    // endregion

    // region Trending Movies

    /**
     * Get Trending Movies (Deprecated)
     */
    /*public void getTrendingMovies() {
        try {
            // show progressBar
            trendingMovies_PB.setVisibility(View.VISIBLE);
            Method onTrendingMoviesFetched = getClass().getMethod("onTrendingMoviesFetched", ArrayList.class);
            Method onFetchDataError = getClass().getMethod("onFetchDataError");
            movieRepository.getTrendingMovies(StaticParameter.TimeWindow.WEEKLY, trendingMoviesPage, this, onTrendingMoviesFetched, onFetchDataError);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Callback when get trending movies successfully (Deprecated)
     *
     * @param movie_list trending movies data
     */
    /*public void onTrendingMoviesFetched(ArrayList<MovieData> movie_list) {
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
    }*/

    // endregion

    // region Popular Movies

    /**
     * Get Popular Movies (Deprecated)
     */
    /*public void getPopularMovies() {
        try {
            // show progressBar
            popularMovies_PB.setVisibility(View.VISIBLE);
            Method onPopularMoviesFetched = getClass().getMethod("onPopularMoviesFetched", ArrayList.class);
            Method onFetchDataError = getClass().getMethod("onFetchDataError");
            movieRepository.getPopularMovies(popularMoviesPage, this, onPopularMoviesFetched, onFetchDataError);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Callback when get popular movies successfully (Deprecated)
     *
     * @param movie_list popular movies data
     */
    /*public void onPopularMoviesFetched(ArrayList<MovieData> movie_list) {
        // hide progressBar
        popularMovies_PB.setVisibility(View.GONE);
        // append data to adapter
        popularMoviesAdapter.appendMovies(movie_list);
        // attach onScrollListener to RecyclerView
        popularMovies_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                // get the number of all items in recyclerView
                int totalItemCount = popularMoviesLayoutMgr.getItemCount();
                // get the number of current items attached to recyclerView
                int visibleItemCount = popularMoviesLayoutMgr.getChildCount();
                // get the first visible item's position
                int firstVisibleItem = popularMoviesLayoutMgr.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    // detach current OnScrollListener
                    popularMovies_RcView.removeOnScrollListener(this);

                    // append nextPage data to recyclerView
                    popularMoviesPage++;
                    getPopularMovies();
                }
            }
        });

        Log.d(LOG_TAG, "popular movies: data fetched successfully");
    }*/

    // endregion

    // region Popular TvShows

    /**
     * Get Popular TvShows (Deprecated)
     */
    /*public void getPopularTvShows() {
        try {
            // show progressBar
            popularTvShows_PB.setVisibility(View.VISIBLE);
            Method onPopularTvShowsFetched = getClass().getMethod("onPopularTvShowsFetched", ArrayList.class);
            Method onFetchDataError = getClass().getMethod("onFetchDataError");
            tvShowRepository.getPopularTvShows(popularTvShowsPage, this, onPopularTvShowsFetched, onFetchDataError);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Callback when get popular tvShows successfully (Deprecated)
     *
     * @param tvShow_list popular tvShows data
     */
    /*public void onPopularTvShowsFetched(ArrayList<TvShowData> tvShow_list) {
        // hide progressBar
        popularTvShows_PB.setVisibility(View.GONE);
        // append data to adapter
        popularTvShowsAdapter.appendTvShows(tvShow_list);
        // attach onScrollListener to RecyclerView
        popularTvShows_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                // get the number of all items in recyclerView
                int totalItemCount = popularTvShowsLayoutMgr.getItemCount();
                // get the number of current items attached to recyclerView
                int visibleItemCount = popularTvShowsLayoutMgr.getChildCount();
                // get the first visible item's position
                int firstVisibleItem = popularTvShowsLayoutMgr.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    // detach current OnScrollListener
                    popularTvShows_RcView.removeOnScrollListener(this);

                    // append nextPage data to recyclerView
                    popularTvShowsPage++;
                    getPopularTvShows();
                }
            }
        });

        Log.d(LOG_TAG, "popular tvShows: data fetched successfully");
    }*/

    // endregion

    // region Trending TvShows

    /**
     * Get Trending TvShows (Deprecated)
     */
    /*public void getTrendingTvShows() {
        try {
            // show progressBar
            trendingTvShows_PB.setVisibility(View.VISIBLE);
            Method onTrendingTvShowsFetched = getClass().getMethod("onTrendingTvShowsFetched", ArrayList.class);
            Method onFetchDataError = getClass().getMethod("onFetchDataError");
            tvShowRepository.getTrendingTvShows(StaticParameter.TimeWindow.WEEKLY, trendingTvShowsPage, this, onTrendingTvShowsFetched, onFetchDataError);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Callback when get trending tvShows successfully (Deprecated)
     *
     * @param tvShow_list trending tvShows data
     */
    /*public void onTrendingTvShowsFetched(ArrayList<TvShowData> tvShow_list) {
        // hide progressBar
        trendingTvShows_PB.setVisibility(View.GONE);
        // append data to adapter
        trendingTvShowsAdapter.appendTvShows(tvShow_list);
        // attach onScrollListener to RecyclerView
        trendingTvShows_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                // get the number of all items in recyclerView
                int totalItemCount = trendingTvShowsLayoutMgr.getItemCount();
                // get the number of current items attached to recyclerView
                int visibleItemCount = trendingTvShowsLayoutMgr.getChildCount();
                // get the first visible item's position
                int firstVisibleItem = trendingTvShowsLayoutMgr.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    // detach current OnScrollListener
                    trendingTvShows_RcView.removeOnScrollListener(this);

                    // append nextPage data to recyclerView
                    trendingTvShowsPage++;
                    getTrendingTvShows();
                }
            }
        });

        Log.d(LOG_TAG, "trending tvShows: data fetched successfully");
    }*/

    // endregion


    /**
     * Callback when data fetched fail
     */
    public void onFetchDataError() {
        Log.d(LOG_TAG, "data fetched fail");
    }


    // endregion

    // region MVVM architecture using LiveData

    // region Upcoming Movies

    /**
     * Get Upcoming Movies (using LiveData)
     */
    public void getUpcomingMovies(int page) {
        // show progressBar
        upcomingMovies_PB.setVisibility(View.VISIBLE);
        moviesViewModel.getUpcomingMovies(page);
    }

    /**
     * Observe when MovieData List LiveData changed (For Upcoming Movies)
     */
    public Observer<ArrayList<MovieData>> getUpcomingMoviesObserver() {
        return movies -> {
            // hide progressBar
            upcomingMovies_PB.setVisibility(View.GONE);

            // append data to adapter
            upcomingMoviesAdapter.appendMovies(movies);

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
                        getUpcomingMovies(upcomingMoviesPage);
                    }
                }
            });

            Log.d(LOG_TAG, "upcoming movies: data fetched successfully");

        };
    }

    // endregion

    // region Now-Playing Movies

    /**
     * Get Now-Playing Movies (using LiveData)
     */
    public void getNowPlayingMovies(int page) {
        // show progressBar
        nowPlayingMovies_PB.setVisibility(View.VISIBLE);
        moviesViewModel.getNowPlayingMovies(page);
    }

    /**
     * Observe when MovieData List LiveData changed (For NowPlaying Movies)
     */
    public Observer<ArrayList<MovieData>> getNowPlayingMoviesObserver() {
        return movies -> {
            // hide progressBar
            nowPlayingMovies_PB.setVisibility(View.GONE);
            // append data to adapter
            nowPlayingMoviesAdapter.appendMovies(movies);
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
                        getNowPlayingMovies(nowPlayingMoviesPage);
                    }
                }
            });

            Log.d(LOG_TAG, "nowPlaying movies: data fetched successfully");

        };
    }

    // endregion

    // region Trending Movies

    /**
     * Get Trending Movies (using LiveData)
     */
    public void getTrendingMovies(int page) {
        // show progressBar
        trendingMovies_PB.setVisibility(View.VISIBLE);
        moviesViewModel.getTrendingMovies(StaticParameter.TimeWindow.WEEKLY, page);
    }

    /**
     * Observe when MovieData List LiveData changed (For Trending Movies)
     */
    public Observer<ArrayList<MovieData>> getTrendingMoviesObserver() {
        return movies -> {
            // hide progressBar
            trendingMovies_PB.setVisibility(View.GONE);
            // append data to adapter
            trendingMoviesAdapter.appendMovies(movies);
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
                        getTrendingMovies(trendingMoviesPage);
                    }
                }
            });

            Log.d(LOG_TAG, "trending movies: data fetched successfully");
        };
    }

    // endregion

    // region Popular Movies

    /**
     * Get Popular Movies (using LiveData)
     */
    public void getPopularMovies(int page) {
        // show progressBar
        popularMovies_PB.setVisibility(View.VISIBLE);
        moviesViewModel.getPopularMovies(page);
    }

    /**
     * Observe when MovieData List LiveData changed (For Popular Movies)
     */
    public Observer<ArrayList<MovieData>> getPopularMoviesObserver() {
        return movies -> {
            // hide progressBar
            popularMovies_PB.setVisibility(View.GONE);
            // append data to adapter
            popularMoviesAdapter.appendMovies(movies);
            // attach onScrollListener to RecyclerView
            popularMovies_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    // get the number of all items in recyclerView
                    int totalItemCount = popularMoviesLayoutMgr.getItemCount();
                    // get the number of current items attached to recyclerView
                    int visibleItemCount = popularMoviesLayoutMgr.getChildCount();
                    // get the first visible item's position
                    int firstVisibleItem = popularMoviesLayoutMgr.findFirstVisibleItemPosition();

                    if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                        // detach current OnScrollListener
                        popularMovies_RcView.removeOnScrollListener(this);

                        // append nextPage data to recyclerView
                        popularMoviesPage++;
                        getPopularMovies(popularMoviesPage);
                    }
                }
            });

            Log.d(LOG_TAG, "popular movies: data fetched successfully");

        };
    }

    // endregion

    // region Popular TvShows

    /**
     * Get Popular TvShows (using LiveData)
     */
    public void getPopularTvShows(int page) {
        // show progressBar
        popularTvShows_PB.setVisibility(View.VISIBLE);
        tvShowsViewModel.getPopularTvShows(page);
    }

    /**
     * Observe when TvShowData List LiveData changed (For Popular TvShows)
     */
    public Observer<ArrayList<TvShowData>> getPopularTvShowsObserver() {
        return tvShows -> {
            // hide progressBar
            popularTvShows_PB.setVisibility(View.GONE);
            // append data to adapter
            popularTvShowsAdapter.appendTvShows(tvShows);
            // attach onScrollListener to RecyclerView
            popularTvShows_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    // get the number of all items in recyclerView
                    int totalItemCount = popularTvShowsLayoutMgr.getItemCount();
                    // get the number of current items attached to recyclerView
                    int visibleItemCount = popularTvShowsLayoutMgr.getChildCount();
                    // get the first visible item's position
                    int firstVisibleItem = popularTvShowsLayoutMgr.findFirstVisibleItemPosition();

                    if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                        // detach current OnScrollListener
                        popularTvShows_RcView.removeOnScrollListener(this);

                        // append nextPage data to recyclerView
                        popularTvShowsPage++;
                        getPopularTvShows(popularTvShowsPage);
                    }
                }
            });

            Log.d(LOG_TAG, "popular tvShows: data fetched successfully");

        };
    }

    // endregion

    // region Trending TvShows

    /**
     * Get Trending TvShows (using LiveData)
     */
    public void getTrendingTvShows(int page) {
        // show progressBar
        trendingTvShows_PB.setVisibility(View.VISIBLE);
        tvShowsViewModel.getTrendingTvShows(StaticParameter.TimeWindow.WEEKLY, page);
    }

    /**
     * Observe when TvShowData List LiveData changed (For Trending TvShows)
     */
    public Observer<ArrayList<TvShowData>> getTrendingTvShowsObserver() {
        return tvShows -> {
            // hide progressBar
            trendingTvShows_PB.setVisibility(View.GONE);
            // append data to adapter
            trendingTvShowsAdapter.appendTvShows(tvShows);
            // attach onScrollListener to RecyclerView
            trendingTvShows_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    // get the number of all items in recyclerView
                    int totalItemCount = trendingTvShowsLayoutMgr.getItemCount();
                    // get the number of current items attached to recyclerView
                    int visibleItemCount = trendingTvShowsLayoutMgr.getChildCount();
                    // get the first visible item's position
                    int firstVisibleItem = trendingTvShowsLayoutMgr.findFirstVisibleItemPosition();

                    if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                        // detach current OnScrollListener
                        trendingTvShows_RcView.removeOnScrollListener(this);

                        // append nextPage data to recyclerView
                        trendingTvShowsPage++;
                        getTrendingTvShows(trendingTvShowsPage);
                    }
                }
            });

            Log.d(LOG_TAG, "trending tvShows: data fetched successfully");
        };
    }

    // endregion

    // endregion


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
     * Reset all results
     */
    private void resetResults() {
        // set default page
        upcomingMoviesPage = 1;
        nowPlayingMoviesPage = 1;
        trendingMoviesPage = 1;
        popularMoviesPage = 1;
        popularTvShowsPage = 1;
        trendingTvShowsPage = 1;

        upcomingMoviesAdapter.removeAllMovies();
        nowPlayingMoviesAdapter.removeAllMovies();
        trendingMoviesAdapter.removeAllMovies();
        popularMoviesAdapter.removeAllMovies();
        popularTvShowsAdapter.removeAllTvShows();
        trendingTvShowsAdapter.removeAllTvShows();

        updateAllData();

    }

    /**
     * Update all data
     */
    private void updateAllData() {
        getUpcomingMovies(upcomingMoviesPage);
        getNowPlayingMovies(nowPlayingMoviesPage);
        getTrendingMovies(trendingMoviesPage);
        getPopularMovies(popularMoviesPage);
        getPopularTvShows(popularTvShowsPage);
        getTrendingTvShows(trendingTvShowsPage);
    }


    /**
     * Navigate to VerticalBrowseFragment
     *
     * @param homeCategory homeCategory from StaticParameter.HomeCategory
     */
    private void showVerticalBrowse(int homeCategory) {
        Bundle arguments = new Bundle();
        arguments.putInt(StaticParameter.ExtraDataKey.EXTRA_DATA_VERTICAL_BROWSE_KEY, homeCategory);
        // navigate to verticalBrowseFragment
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_homeFragment_to_verticalBrowseFragment, arguments);
    }


}