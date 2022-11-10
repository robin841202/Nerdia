package com.example.movieinfo.view.fragments.discover.tab;

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
import com.example.movieinfo.view.MediaDetailsActivity;
import com.example.movieinfo.view.adapter.MoviesAdapter;
import com.example.movieinfo.viewmodel.MoviesViewModel;
import com.example.movieinfo.viewmodel.SearchKeywordViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class SearchMoviesTab extends Fragment implements MoviesAdapter.IMovieListener {


    private final String LOG_TAG = "SearchMoviesTab";

    private MoviesViewModel moviesViewModel;

    private ShimmerFrameLayout mShimmer;
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

        // Initialize viewModel, data only survive this fragment lifecycle
        moviesViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        moviesViewModel.init();

        // Set Observer
        moviesViewModel.getMoviesLiveData().observe(this, getSearchMoviesObserver());
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
        mShimmer = view.findViewById(R.id.shimmer_search);

        // Initialize Adapter
        movieAdapter = new MoviesAdapter(this);

        // Initialize gridLayoutManager
        mLayoutMgr = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);

        // Set Adapter & LayoutManager to RecyclerView
        mRcView.setAdapter(movieAdapter);
        mRcView.setLayoutManager(mLayoutMgr);

        // Set SwipeRefreshListener
        pullToRefresh.setOnRefreshListener(() -> {
            clearResults();
            searchMovies(currentKeyword, currentPage);
            Log.d(LOG_TAG, "onRefresh");
            pullToRefresh.setRefreshing(false);
        });

        // Get navBackStackEntry to scope LiveData lifecycle
        navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getCurrentBackStackEntry();

        if (backStackEntry != null) {
            // Initialize Keyword ViewModel
            searchKeywordViewModel = new ViewModelProvider(backStackEntry).get(SearchKeywordViewModel.class);

            // Keep monitoring search keyword changes
            searchKeywordViewModel.getKeyWord().observe(backStackEntry, new Observer<String>() {
                @Override
                public void onChanged(String keyword) {
                    // reset searched results
                    clearResults();

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
            // show shimmer animation
            mShimmer.startShimmer();
            mShimmer.setVisibility(View.VISIBLE);
            moviesViewModel.searchMovies(keyword, page);
        }
    }

    /**
     * Observe when MovieData List LiveData changed
     */
    public Observer<ArrayList<MovieData>> getSearchMoviesObserver() {
        return movies -> {
            // hide shimmer animation
            mShimmer.stopShimmer();
            mShimmer.setVisibility(View.GONE);

            if (movies.size() > 0){
                // append data to adapter
                movieAdapter.appendMovies(movies);

                // attach onScrollListener to RecyclerView
                mRcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        // when scrolling up
                        if(dy > 0){
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
                                searchMovies(currentKeyword, currentPage);
                            }
                        }
                    }
                });
            }

            Log.d(LOG_TAG, "search movies: data fetched successfully");
        };
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
    private void clearResults() {
        currentPage = 1;
        movieAdapter.removeAllMovies();
    }

}