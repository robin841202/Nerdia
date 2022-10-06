package com.example.movieinfo.ui.discover.tab;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
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
import com.example.movieinfo.model.repository.MovieRepository;
import com.example.movieinfo.ui.home.HomeFragment;
import com.example.movieinfo.view.MediaDetailsActivity;
import com.example.movieinfo.view.adapter.MoviesAdapter;
import com.example.movieinfo.viewmodel.SearchKeywordViewModel;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class SearchMoviesTab extends Fragment implements MoviesAdapter.IMovieListener {


    private final String LOG_TAG = "SearchMoviesTab";

    private final MovieRepository movieRepository = new MovieRepository();

    private RecyclerView mRcView;
    private SwipeRefreshLayout pullToRefresh;
    private MoviesAdapter movieAdapter;
    private GridLayoutManager mLayoutMgr;
    private int currentPage;

    private SearchKeywordViewModel searchKeywordViewModel;
    private String currentKeyword;

    private NavController navController;

    public SearchMoviesTab() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set default page
        currentPage = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_movies_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get Views
        mRcView = view.findViewById(R.id.recycler_search);
        pullToRefresh = view.findViewById(R.id.swiperefresh_search);

        // Initialize Adapter
        movieAdapter = new MoviesAdapter(new ArrayList<>(), this);

        // Initialize gridLayoutManager
        mLayoutMgr = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);

        // Set Adapter & LayoutManager to RecyclerView
        mRcView.setAdapter(movieAdapter);
        mRcView.setLayoutManager(mLayoutMgr);

        // Set SwipeRefreshListener
        pullToRefresh.setOnRefreshListener(() -> {
            searchMovies(currentKeyword, currentPage);
            Log.d(LOG_TAG, "onRefresh");
            pullToRefresh.setRefreshing(false);
        });

        // Get navBackStackEntry to scope LiveData lifecycle
        navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getCurrentBackStackEntry();

        if (backStackEntry != null){
            // Initialize keyword LiveData
            searchKeywordViewModel = new ViewModelProvider(backStackEntry).get(SearchKeywordViewModel.class);

            // Keep monitoring search keyword changes
            searchKeywordViewModel.getKeyWord().observe(backStackEntry, new Observer<String>() {
                @Override
                public void onChanged(String keyword) {
                    // reset searched results
                    resetResults();

                    currentKeyword = keyword;

                    // begin searching
                    searchMovies(currentKeyword, currentPage);
                }
            });
        }
    }

    /**
     * Searching Movies
     *
     * @param keyword Searching keyword
     * @param page    result page
     */
    private void searchMovies(String keyword, int page) {
        if (keyword != null && !keyword.isEmpty()) {
            try {
                Method onSearchMoviesFetched = getClass().getMethod("onSearchMoviesFetched", ArrayList.class);
                Method onFetchDataError = getClass().getMethod("onFetchDataError");
                movieRepository.searchMovies(keyword, page, this, onSearchMoviesFetched, onFetchDataError);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Callback when searching movies successfully
     *
     * @param movie_list searched movies data
     */
    public void onSearchMoviesFetched(ArrayList<MovieData> movie_list) {
        // append data to adapter
        movieAdapter.appendMovies(movie_list);

        // attach onScrollListener to RecyclerView
        mRcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                // get the number of all items in recyclerView
                int totalItemCount = mLayoutMgr.getItemCount();
                // get the number of current items attached to recyclerView
                int visibleItemCount = mLayoutMgr.getChildCount();
                // get the first visible item's position
                int firstVisibleItem = mLayoutMgr.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    // detach current OnScrollListener
                    mRcView.removeOnScrollListener(this);

                    // append nextPage data to recyclerView
                    currentPage++;
                    searchMovies(currentKeyword, currentPage);
                }
            }
        });

        Log.d(LOG_TAG, "search movies: data fetched successfully");
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
        intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY, StaticParameter.MediaType.MOVIE);
        intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_MOVIE_ID_KEY, movie.getId());
        startActivity(intent);
        // set the custom transition animation
        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    /**
     * Reset search results
     */
    private void resetResults() {
        currentPage = 1;
        movieAdapter.removeAllMovies();
    }

}