package com.robinhsueh.nerdia.view.fragments.discover.tab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import com.robinhsueh.nerdia.R;
import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.view.adapter.EmptyDataObserver;
import com.robinhsueh.nerdia.view.adapter.MoviesAdapter;
import com.robinhsueh.nerdia.viewmodel.SearchKeywordViewModel;
import com.robinhsueh.nerdia.viewmodel.SearchViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class SearchMoviesTab extends Fragment {


    private final String LOG_TAG = "SearchMoviesTab";

    private SearchViewModel searchViewModel;

    private ShimmerFrameLayout mShimmer;
    private RecyclerView mRcView;
    private SwipeRefreshLayout pullToRefresh;
    private MoviesAdapter mAdapter;
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
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.init();

        // Set Observer
        searchViewModel.getMoviesLiveData().observe(this, getSearchMoviesObserver());
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
            searchKeywordViewModel.getKeyWord().observe(backStackEntry, keyword -> {
                // reset searched results
                clearResults();

                currentKeyword = keyword;

                // begin searching
                searchMovies(currentKeyword, currentPage);
            });
        }
    }

    /**
     * Initialize RecyclerView
     */
    private void initRecyclerView(View emptyDataView) {
        // Initialize Adapter
        mAdapter = new MoviesAdapter((AppCompatActivity) getActivity());

        // Initialize gridLayoutManager
        mLayoutMgr = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);

        // Set Adapter & LayoutManager to RecyclerView
        mRcView.setAdapter(mAdapter);
        mRcView.setLayoutManager(mLayoutMgr);

        // Set EmptyStateObserver
        EmptyDataObserver emptyDataObserver = new EmptyDataObserver(mRcView, emptyDataView);
        mAdapter.registerAdapterDataObserver(emptyDataObserver);
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
            searchViewModel.searchMovies(keyword, page);
        }
    }

    /**
     * Observe when MovieData List LiveData changed
     */
    public Observer<ArrayList<MovieData>> getSearchMoviesObserver() {
        return movies -> {
            if (movies != null){
                // hide shimmer animation
                mShimmer.stopShimmer();
                mShimmer.setVisibility(View.GONE);

                if (movies.size() > 0) {
                    // append data to adapter
                    mAdapter.appendMovies(movies);

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
                                    searchMovies(currentKeyword, currentPage);
                                }
                            }
                        }
                    });
                }

                Log.d(LOG_TAG, "search movies: data fetched successfully");
            }
        };
    }

    /**
     * Reset search results
     */
    private void clearResults() {
        currentPage = 1;
        mAdapter.removeAllMovies();
    }

}