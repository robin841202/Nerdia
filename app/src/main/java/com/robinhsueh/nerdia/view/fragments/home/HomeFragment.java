package com.robinhsueh.nerdia.view.fragments.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robinhsueh.nerdia.R;
import com.robinhsueh.nerdia.model.CategoryData;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.model.tvshow.TvShowData;
import com.robinhsueh.nerdia.view.adapter.CategoryAdapter;
import com.robinhsueh.nerdia.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeFragment extends Fragment implements CategoryAdapter.ICategoryListener {

    private final String LOG_TAG = "HomeFragment";

    private Context context;

    private SwipeRefreshLayout pullToRefresh;

    private HomeViewModel viewModel;

    //region Categories Variables
    private CategoryAdapter mAdapter;
    private RecyclerView mRcView;
    private ArrayList<CategoryData> categoryList;
    //endregion

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

        // Initialize viewModel, data only survive this fragment lifecycle
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.init();

        // Set observer
        viewModel.getUpcomingMoviesLiveData().observe(this, getUpcomingMoviesObserver(StaticParameter.HomeCategory.UPCOMING_MOVIES));
        viewModel.getNowPlayingMoviesLiveData().observe(this, getNowPlayingMoviesObserver(StaticParameter.HomeCategory.NOWPLAYING_MOVIES));
        viewModel.getTrendingMoviesLiveData().observe(this, getTrendingMoviesObserver(StaticParameter.HomeCategory.TRENDING_MOVIES));
        viewModel.getPopularMoviesLiveData().observe(this, getPopularMoviesObserver(StaticParameter.HomeCategory.POPULAR_MOVIES));
        viewModel.getNetflixMoviesLiveData().observe(this, getNetflixMoviesObserver(StaticParameter.HomeCategory.NETFLIX_MOVIES));
        viewModel.getDisneyMoviesLiveData().observe(this, getDisneyMoviesObserver(StaticParameter.HomeCategory.DISNEY_MOVIES));
        viewModel.getCatchplayMoviesLiveData().observe(this, getCatchplayMoviesObserver(StaticParameter.HomeCategory.CATCHPLAY_MOVIES));
        viewModel.getPrimeMoviesLiveData().observe(this, getPrimeMoviesObserver(StaticParameter.HomeCategory.PRIME_MOVIES));

        viewModel.getPopularTvShowsLiveData().observe(this, getPopularTvShowsObserver(StaticParameter.HomeCategory.POPULAR_TVSHOWS));
        viewModel.getTrendingTvShowsLiveData().observe(this, getTrendingTvShowsObserver(StaticParameter.HomeCategory.TRENDING_TVSHOWS));
        viewModel.getNetflixTvShowsLiveData().observe(this, getNetflixTvShowsObserver(StaticParameter.HomeCategory.NETFLIX_TVSHOWS));
        viewModel.getDisneyTvShowsLiveData().observe(this, getDisneyTvShowsObserver(StaticParameter.HomeCategory.DISNEY_TVSHOWS));
        viewModel.getCatchplayTvShowsLiveData().observe(this, getCatchplayTvShowsObserver(StaticParameter.HomeCategory.CATCHPLAY_TVSHOWS));
        viewModel.getPrimeTvShowsLiveData().observe(this, getPrimeTvShowsObserver(StaticParameter.HomeCategory.PRIME_TVSHOWS));

    }

    /**
     * Initialize Views
     *
     * @param root
     */
    private void initViews(View root) {
        pullToRefresh = root.findViewById(R.id.swiperefresh);
        mRcView = root.findViewById(R.id.recycler_category);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Views
        initViews(root);

        // Initialize Adapter
        mAdapter = new CategoryAdapter(this, this);

        // Set adapter
        mRcView.setAdapter(mAdapter);

        // Set layoutManager
        mRcView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Start getting data
        initialAllData();

        // Set SwipeRefreshListener
        pullToRefresh.setOnRefreshListener(() -> {
            // reset all results
            resetResults();

            Log.d(LOG_TAG, "onRefresh");

            pullToRefresh.setRefreshing(false);
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
    @Override
    public void getUpcomingMovies(int page) {
        viewModel.getUpcomingMovies(page);
    }

    /**
     * Observe when MovieData List LiveData changed (For Upcoming Movies)
     */
    public Observer<ArrayList<MovieData>> getUpcomingMoviesObserver(int categoryType) {
        return movies -> {
            if (movies != null) {
                // get the specific viewHolder in category RecyclerView by using category type
                CategoryAdapter.CategoryViewHolder viewHolder = getCategoryViewHolderByType(categoryType);
                if (viewHolder != null) {
                    viewHolder.stopShimmering();
                    viewHolder.appendMoviesToMediaRcView(movies);

                    // update the onScroll event in media RecyclerView
                    CategoryData targetCategoryData = mAdapter.getCategoryByType(categoryType);
                    // only update onScroll event when get data
                    if (targetCategoryData != null && movies.size() > 0) {
                        viewHolder.updateMediaRcViewScrollEvent(targetCategoryData);
                    }
                }
                Log.d(LOG_TAG, "upcoming movies: data fetched successfully");
            }
        };
    }


    // endregion

    // region Now-Playing Movies

    /**
     * Get Now-Playing Movies (using LiveData)
     */
    @Override
    public void getNowPlayingMovies(int page) {
        viewModel.getNowPlayingMovies(page);
    }

    /**
     * Observe when MovieData List LiveData changed (For NowPlaying Movies)
     */
    public Observer<ArrayList<MovieData>> getNowPlayingMoviesObserver(int categoryType) {
        return movies -> {
            if (movies != null) {
                // get the specific viewHolder in category RecyclerView by using category type
                CategoryAdapter.CategoryViewHolder viewHolder = getCategoryViewHolderByType(categoryType);
                if (viewHolder != null) {
                    viewHolder.stopShimmering();
                    viewHolder.appendMoviesToMediaRcView(movies);

                    // update the onScroll event in media RecyclerView
                    CategoryData targetCategoryData = mAdapter.getCategoryByType(categoryType);
                    // only update onScroll event when get data
                    if (targetCategoryData != null && movies.size() > 0) {
                        viewHolder.updateMediaRcViewScrollEvent(targetCategoryData);
                    }
                }
                Log.d(LOG_TAG, "nowPlaying movies: data fetched successfully");
            }
        };
    }

    // endregion

    // region Trending Movies

    /**
     * Get Trending Movies (using LiveData)
     */
    @Override
    public void getTrendingMovies(int page) {
        viewModel.getTrendingMovies(StaticParameter.TimeWindow.WEEKLY, page);
    }

    /**
     * Observe when MovieData List LiveData changed (For Trending Movies)
     */
    public Observer<ArrayList<MovieData>> getTrendingMoviesObserver(int categoryType) {
        return movies -> {
            if (movies != null) {
                // get the specific viewHolder in category RecyclerView by using category type
                CategoryAdapter.CategoryViewHolder viewHolder = getCategoryViewHolderByType(categoryType);
                if (viewHolder != null) {
                    viewHolder.stopShimmering();
                    viewHolder.appendMoviesToMediaRcView(movies);

                    // update the onScroll event in media RecyclerView
                    CategoryData targetCategoryData = mAdapter.getCategoryByType(categoryType);
                    // only update onScroll event when get data
                    if (targetCategoryData != null && movies.size() > 0) {
                        viewHolder.updateMediaRcViewScrollEvent(targetCategoryData);
                    }
                }
                Log.d(LOG_TAG, "trending movies: data fetched successfully");
            }
        };
    }

    // endregion

    // region Popular Movies

    /**
     * Get Popular Movies (using LiveData)
     */
    @Override
    public void getPopularMovies(int page) {
        viewModel.getPopularMovies(page);
    }

    /**
     * Observe when MovieData List LiveData changed (For Popular Movies)
     */
    public Observer<ArrayList<MovieData>> getPopularMoviesObserver(int categoryType) {
        return movies -> {
            if (movies != null) {
                // get the specific viewHolder in category RecyclerView by using category type
                CategoryAdapter.CategoryViewHolder viewHolder = getCategoryViewHolderByType(categoryType);
                if (viewHolder != null) {
                    viewHolder.stopShimmering();
                    viewHolder.appendMoviesToMediaRcView(movies);

                    // update the onScroll event in media RecyclerView
                    CategoryData targetCategoryData = mAdapter.getCategoryByType(categoryType);
                    // only update onScroll event when get data
                    if (targetCategoryData != null && movies.size() > 0) {
                        viewHolder.updateMediaRcViewScrollEvent(targetCategoryData);
                    }
                }
                Log.d(LOG_TAG, "popular movies: data fetched successfully");
            }
        };
    }

    // endregion

    // region Netflix Movies

    /**
     * Get Netflix Movies (using LiveData)
     */
    @Override
    public void getNetflixMovies(int page) {
        viewModel.getNetflixMovies(page);
    }

    /**
     * Observe when MovieData List LiveData changed (For Netflix Movies)
     */
    public Observer<ArrayList<MovieData>> getNetflixMoviesObserver(int categoryType) {
        return movies -> {
            if (movies != null) {
                // get the specific viewHolder in category RecyclerView by using category type
                CategoryAdapter.CategoryViewHolder viewHolder = getCategoryViewHolderByType(categoryType);
                if (viewHolder != null) {
                    viewHolder.stopShimmering();
                    viewHolder.appendMoviesToMediaRcView(movies);

                    // update the onScroll event in media RecyclerView
                    CategoryData targetCategoryData = mAdapter.getCategoryByType(categoryType);
                    // only update onScroll event when get data
                    if (targetCategoryData != null && movies.size() > 0) {
                        viewHolder.updateMediaRcViewScrollEvent(targetCategoryData);
                    }
                }
                Log.d(LOG_TAG, "netflix movies: data fetched successfully");
            }
        };
    }

    // endregion

    // region Disney Movies

    /**
     * Get Disney Movies (using LiveData)
     */
    @Override
    public void getDisneyMovies(int page) {
        viewModel.getDisneyMovies(page);
    }

    /**
     * Observe when MovieData List LiveData changed (For Disney Movies)
     */
    public Observer<ArrayList<MovieData>> getDisneyMoviesObserver(int categoryType) {
        return movies -> {
            if (movies != null) {
                // get the specific viewHolder in category RecyclerView by using category type
                CategoryAdapter.CategoryViewHolder viewHolder = getCategoryViewHolderByType(categoryType);
                if (viewHolder != null) {
                    viewHolder.stopShimmering();
                    viewHolder.appendMoviesToMediaRcView(movies);

                    // update the onScroll event in media RecyclerView
                    CategoryData targetCategoryData = mAdapter.getCategoryByType(categoryType);
                    // only update onScroll event when get data
                    if (targetCategoryData != null && movies.size() > 0) {
                        viewHolder.updateMediaRcViewScrollEvent(targetCategoryData);
                    }
                }
                Log.d(LOG_TAG, "disney movies: data fetched successfully");
            }
        };
    }

    // endregion

    // region Catchplay Movies

    /**
     * Get Catchplay Movies (using LiveData)
     */
    @Override
    public void getCatchplayMovies(int page) {
        viewModel.getCatchplayMovies(page);
    }

    /**
     * Observe when MovieData List LiveData changed (For Catchplay Movies)
     */
    public Observer<ArrayList<MovieData>> getCatchplayMoviesObserver(int categoryType) {
        return movies -> {
            if (movies != null) {
                // get the specific viewHolder in category RecyclerView by using category type
                CategoryAdapter.CategoryViewHolder viewHolder = getCategoryViewHolderByType(categoryType);
                if (viewHolder != null) {
                    viewHolder.stopShimmering();
                    viewHolder.appendMoviesToMediaRcView(movies);

                    // update the onScroll event in media RecyclerView
                    CategoryData targetCategoryData = mAdapter.getCategoryByType(categoryType);
                    // only update onScroll event when get data
                    if (targetCategoryData != null && movies.size() > 0) {
                        viewHolder.updateMediaRcViewScrollEvent(targetCategoryData);
                    }
                }
                Log.d(LOG_TAG, "catchplay movies: data fetched successfully");
            }
        };
    }

    // endregion

    // region Prime Movies

    /**
     * Get Prime Movies (using LiveData)
     */
    @Override
    public void getPrimeMovies(int page) {
        viewModel.getPrimeMovies(page);
    }

    /**
     * Observe when MovieData List LiveData changed (For Prime Movies)
     */
    public Observer<ArrayList<MovieData>> getPrimeMoviesObserver(int categoryType) {
        return movies -> {
            if (movies != null) {
                // get the specific viewHolder in category RecyclerView by using category type
                CategoryAdapter.CategoryViewHolder viewHolder = getCategoryViewHolderByType(categoryType);
                if (viewHolder != null) {
                    viewHolder.stopShimmering();
                    viewHolder.appendMoviesToMediaRcView(movies);

                    // update the onScroll event in media RecyclerView
                    CategoryData targetCategoryData = mAdapter.getCategoryByType(categoryType);
                    // only update onScroll event when get data
                    if (targetCategoryData != null && movies.size() > 0) {
                        viewHolder.updateMediaRcViewScrollEvent(targetCategoryData);
                    }
                }
                Log.d(LOG_TAG, "prime movies: data fetched successfully");
            }
        };
    }

    // endregion

    // region Popular TvShows

    /**
     * Get Popular TvShows (using LiveData)
     */
    @Override
    public void getPopularTvShows(int page) {
        viewModel.getPopularTvShows(page);
    }

    /**
     * Observe when TvShowData List LiveData changed (For Popular TvShows)
     */
    public Observer<ArrayList<TvShowData>> getPopularTvShowsObserver(int categoryType) {
        return tvShows -> {
            if (tvShows != null) {
                // get the specific viewHolder in category RecyclerView by using category type
                CategoryAdapter.CategoryViewHolder viewHolder = getCategoryViewHolderByType(categoryType);
                if (viewHolder != null) {
                    viewHolder.stopShimmering();
                    viewHolder.appendTvShowsToMediaRcView(tvShows);

                    // update the onScroll event in media RecyclerView
                    CategoryData targetCategoryData = mAdapter.getCategoryByType(categoryType);
                    // only update onScroll event when get data
                    if (targetCategoryData != null && tvShows.size() > 0) {
                        viewHolder.updateMediaRcViewScrollEvent(targetCategoryData);
                    }
                }
                Log.d(LOG_TAG, "popular tvShows: data fetched successfully");
            }
        };
    }

    // endregion

    // region Trending TvShows

    /**
     * Get Trending TvShows (using LiveData)
     */
    @Override
    public void getTrendingTvShows(int page) {
        viewModel.getTrendingTvShows(StaticParameter.TimeWindow.WEEKLY, page);
    }

    /**
     * Observe when TvShowData List LiveData changed (For Trending TvShows)
     */
    public Observer<ArrayList<TvShowData>> getTrendingTvShowsObserver(int categoryType) {
        return tvShows -> {
            if (tvShows != null) {
                // get the specific viewHolder in category RecyclerView by using category type
                CategoryAdapter.CategoryViewHolder viewHolder = getCategoryViewHolderByType(categoryType);
                if (viewHolder != null) {
                    viewHolder.stopShimmering();
                    viewHolder.appendTvShowsToMediaRcView(tvShows);

                    // update the onScroll event in media RecyclerView
                    CategoryData targetCategoryData = mAdapter.getCategoryByType(categoryType);
                    // only update onScroll event when get data
                    if (targetCategoryData != null && tvShows.size() > 0) {
                        viewHolder.updateMediaRcViewScrollEvent(targetCategoryData);
                    }
                }
                Log.d(LOG_TAG, "trending tvShows: data fetched successfully");
            }
        };
    }

    // endregion

    // region Netflix TvShows

    /**
     * Get Netflix TvShows (using LiveData)
     */
    @Override
    public void getNetflixTvShows(int page) {
        viewModel.getNetflixTvShows(page);
    }

    /**
     * Observe when TvShowData List LiveData changed (For Netflix TvShows)
     */
    public Observer<ArrayList<TvShowData>> getNetflixTvShowsObserver(int categoryType) {
        return tvShows -> {
            if (tvShows != null) {
                // get the specific viewHolder in category RecyclerView by using category type
                CategoryAdapter.CategoryViewHolder viewHolder = getCategoryViewHolderByType(categoryType);
                if (viewHolder != null) {
                    viewHolder.stopShimmering();
                    viewHolder.appendTvShowsToMediaRcView(tvShows);

                    // update the onScroll event in media RecyclerView
                    CategoryData targetCategoryData = mAdapter.getCategoryByType(categoryType);
                    // only update onScroll event when get data
                    if (targetCategoryData != null && tvShows.size() > 0) {
                        viewHolder.updateMediaRcViewScrollEvent(targetCategoryData);
                    }
                }
                Log.d(LOG_TAG, "netflix tvShows: data fetched successfully");
            }
        };
    }

    // endregion

    // region Disney TvShows

    /**
     * Get Disney TvShows (using LiveData)
     */
    @Override
    public void getDisneyTvShows(int page) {
        viewModel.getDisneyTvShows(page);
    }

    /**
     * Observe when TvShowData List LiveData changed (For Disney TvShows)
     */
    public Observer<ArrayList<TvShowData>> getDisneyTvShowsObserver(int categoryType) {
        return tvShows -> {
            if (tvShows != null) {
                // get the specific viewHolder in category RecyclerView by using category type
                CategoryAdapter.CategoryViewHolder viewHolder = getCategoryViewHolderByType(categoryType);
                if (viewHolder != null) {
                    viewHolder.stopShimmering();
                    viewHolder.appendTvShowsToMediaRcView(tvShows);

                    // update the onScroll event in media RecyclerView
                    CategoryData targetCategoryData = mAdapter.getCategoryByType(categoryType);
                    // only update onScroll event when get data
                    if (targetCategoryData != null && tvShows.size() > 0) {
                        viewHolder.updateMediaRcViewScrollEvent(targetCategoryData);
                    }
                }
                Log.d(LOG_TAG, "disney tvShows: data fetched successfully");
            }
        };
    }

    // endregion

    // region Catchplay TvShows

    /**
     * Get Catchplay TvShows (using LiveData)
     */
    @Override
    public void getCatchplayTvShows(int page) {
        viewModel.getCatchplayTvShows(page);
    }

    /**
     * Observe when TvShowData List LiveData changed (For Catchplay TvShows)
     */
    public Observer<ArrayList<TvShowData>> getCatchplayTvShowsObserver(int categoryType) {
        return tvShows -> {
            if (tvShows != null) {
                // get the specific viewHolder in category RecyclerView by using category type
                CategoryAdapter.CategoryViewHolder viewHolder = getCategoryViewHolderByType(categoryType);
                if (viewHolder != null) {
                    viewHolder.stopShimmering();
                    viewHolder.appendTvShowsToMediaRcView(tvShows);

                    // update the onScroll event in media RecyclerView
                    CategoryData targetCategoryData = mAdapter.getCategoryByType(categoryType);
                    // only update onScroll event when get data
                    if (targetCategoryData != null && tvShows.size() > 0) {
                        viewHolder.updateMediaRcViewScrollEvent(targetCategoryData);
                    }
                }
                Log.d(LOG_TAG, "catchplay tvShows: data fetched successfully");
            }
        };
    }

    // endregion

    // region Prime TvShows

    /**
     * Get Prime TvShows (using LiveData)
     */
    @Override
    public void getPrimeTvShows(int page) {
        viewModel.getPrimeTvShows(page);
    }

    /**
     * Observe when TvShowData List LiveData changed (For Prime TvShows)
     */
    public Observer<ArrayList<TvShowData>> getPrimeTvShowsObserver(int categoryType) {
        return tvShows -> {
            if (tvShows != null) {
                // get the specific viewHolder in category RecyclerView by using category type
                CategoryAdapter.CategoryViewHolder viewHolder = getCategoryViewHolderByType(categoryType);
                if (viewHolder != null) {
                    viewHolder.stopShimmering();
                    viewHolder.appendTvShowsToMediaRcView(tvShows);

                    // update the onScroll event in media RecyclerView
                    CategoryData targetCategoryData = mAdapter.getCategoryByType(categoryType);
                    // only update onScroll event when get data
                    if (targetCategoryData != null && tvShows.size() > 0) {
                        viewHolder.updateMediaRcViewScrollEvent(targetCategoryData);
                    }
                }
                Log.d(LOG_TAG, "prime tvShows: data fetched successfully");
            }
        };
    }

    // endregion

    // endregion

    /**
     * Reset all results
     */
    private void resetResults() {
        mAdapter.removeAllItems();
        initialAllData();
    }

    /**
     * Update all data
     */
    private void initialAllData() {
        categoryList = new ArrayList<>(Arrays.asList(
                new CategoryData(getString(R.string.upcoming), getString(R.string.label_movies), StaticParameter.HomeCategory.UPCOMING_MOVIES),
                new CategoryData(getString(R.string.now_playing), getString(R.string.label_movies), StaticParameter.HomeCategory.NOWPLAYING_MOVIES),
                new CategoryData(getString(R.string.trending), getString(R.string.label_movies), StaticParameter.HomeCategory.TRENDING_MOVIES),
                new CategoryData(getString(R.string.trending), getString(R.string.label_tvshows), StaticParameter.HomeCategory.TRENDING_TVSHOWS),
                new CategoryData(getString(R.string.popular), getString(R.string.label_movies), StaticParameter.HomeCategory.POPULAR_MOVIES),
                new CategoryData(getString(R.string.popular), getString(R.string.label_tvshows), StaticParameter.HomeCategory.POPULAR_TVSHOWS),
                new CategoryData(getString(R.string.label_nfx), getString(R.string.label_movies), StaticParameter.HomeCategory.NETFLIX_MOVIES),
                new CategoryData(getString(R.string.label_nfx), getString(R.string.label_tvshows), StaticParameter.HomeCategory.NETFLIX_TVSHOWS),
                new CategoryData(getString(R.string.label_disney_plus), getString(R.string.label_movies), StaticParameter.HomeCategory.DISNEY_MOVIES),
                new CategoryData(getString(R.string.label_disney_plus), getString(R.string.label_tvshows), StaticParameter.HomeCategory.DISNEY_TVSHOWS),
                new CategoryData(getString(R.string.label_catchplay_plus), getString(R.string.label_movies), StaticParameter.HomeCategory.CATCHPLAY_MOVIES),
                new CategoryData(getString(R.string.label_catchplay_plus), getString(R.string.label_tvshows), StaticParameter.HomeCategory.CATCHPLAY_TVSHOWS),
                new CategoryData(getString(R.string.label_prime_video), getString(R.string.label_movies), StaticParameter.HomeCategory.PRIME_MOVIES),
                new CategoryData(getString(R.string.label_prime_video), getString(R.string.label_tvshows), StaticParameter.HomeCategory.PRIME_TVSHOWS)));

        mAdapter.setItems(categoryList);
    }

    /**
     * Get single category viewholder by categoryType
     *
     * @param categoryType categoryType, defined in StaticParameter.HomeCategory
     * @return
     */
    private CategoryAdapter.CategoryViewHolder getCategoryViewHolderByType(int categoryType) {
        CategoryData targetCategoryData = categoryList.stream().filter(data -> data.getCategoryType() == categoryType).findFirst().orElse(null);
        int position = categoryList.indexOf(targetCategoryData);
        return (CategoryAdapter.CategoryViewHolder) mRcView.findViewHolderForAdapterPosition(position);
    }

}