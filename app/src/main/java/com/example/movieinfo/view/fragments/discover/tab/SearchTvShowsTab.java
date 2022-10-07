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
import com.example.movieinfo.model.tvshow.TvShowData;
import com.example.movieinfo.view.MediaDetailsActivity;
import com.example.movieinfo.view.adapter.TvShowsAdapter;
import com.example.movieinfo.viewmodel.SearchKeywordViewModel;
import com.example.movieinfo.viewmodel.TvShowsViewModel;

import java.util.ArrayList;

public class SearchTvShowsTab extends Fragment implements TvShowsAdapter.ITvShowListener {

    private final String LOG_TAG = "SearchTvShowsTab";


    private TvShowsViewModel tvShowsViewModel;

    private RecyclerView mRcView;
    private SwipeRefreshLayout pullToRefresh;
    private TvShowsAdapter tvShowsAdapter;
    private GridLayoutManager mLayoutMgr;
    private int currentPage;

    private SearchKeywordViewModel searchKeywordViewModel;
    private String currentKeyword;

    private NavController navController;

    public SearchTvShowsTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set default page
        currentPage = 1;

        // Initialize viewModel, data only survive this fragment lifecycle
        tvShowsViewModel = new ViewModelProvider(this).get(TvShowsViewModel.class);
        tvShowsViewModel.init();

        // Set Observer
        tvShowsViewModel.getTvShowsLiveData().observe(this, getSearchTvShowsObserver());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_tv_shows_tab, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get Views
        mRcView = view.findViewById(R.id.recycler_search);
        pullToRefresh = view.findViewById(R.id.swiperefresh_search);

        // Initialize Adapter
        tvShowsAdapter = new TvShowsAdapter(new ArrayList<>(), this);

        // Initialize gridLayoutManager
        mLayoutMgr = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);

        // Set Adapter & LayoutManager to RecyclerView
        mRcView.setAdapter(tvShowsAdapter);
        mRcView.setLayoutManager(mLayoutMgr);

        // Set SwipeRefreshListener
        pullToRefresh.setOnRefreshListener(() -> {
            clearResults();
            searchTvShows(currentKeyword, currentPage);
            Log.d(LOG_TAG, "onRefresh");
            pullToRefresh.setRefreshing(false);
        });

        // Get navBackStackEntry to scope LiveData lifecycle
        navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getCurrentBackStackEntry();

        if (backStackEntry != null) {
            // Initialize keyword LiveData
            searchKeywordViewModel = new ViewModelProvider(backStackEntry).get(SearchKeywordViewModel.class);

            // Keep monitoring search keyword changes
            searchKeywordViewModel.getKeyWord().observe(backStackEntry, new Observer<String>() {
                @Override
                public void onChanged(String keyword) {
                    // reset searched results
                    clearResults();

                    currentKeyword = keyword;

                    // begin searching
                    searchTvShows(currentKeyword, currentPage);
                }
            });
        }

    }

    /**
     * Searching TvShows
     *
     * @param keyword Searching keyword
     * @param page    result page
     */
    private void searchTvShows(String keyword, int page) {
        if (keyword != null && !keyword.isEmpty()) {
            tvShowsViewModel.searchTvShows(keyword, page);
        }
    }


    /**
     * Observe when TvShowData List LiveData changed
     */
    public Observer<ArrayList<TvShowData>> getSearchTvShowsObserver() {
        return tvShows -> {
            // append data to adapter
            tvShowsAdapter.appendTvShows(tvShows);
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
                        searchTvShows(currentKeyword, currentPage);
                    }
                }
            });

            Log.d(LOG_TAG, "tvShows: data fetched successfully");
        };
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
     * Reset search results
     */
    private void clearResults() {
        currentPage = 1;
        tvShowsAdapter.removeAllTvShows();
    }
}