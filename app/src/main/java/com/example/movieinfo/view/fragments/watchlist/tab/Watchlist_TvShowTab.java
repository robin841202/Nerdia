package com.example.movieinfo.view.fragments.watchlist.tab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.database.entity.TvShowWatchlistEntity;
import com.example.movieinfo.model.tvshow.TvShowData;
import com.example.movieinfo.model.user.LoginInfo;
import com.example.movieinfo.utils.SharedPreferenceUtils;
import com.example.movieinfo.view.adapter.EmptyDataObserver;
import com.example.movieinfo.view.adapter.TvShowsAdapter;
import com.example.movieinfo.viewmodel.WatchlistViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Watchlist_TvShowTab extends Fragment {

    private final String LOG_TAG = "RatedList_TvShowTab";

    private WatchlistViewModel viewModel;
    private Context context;

    private ShimmerFrameLayout mShimmer;
    private RecyclerView mRcView;
    private TvShowsAdapter mAdapter;
    private GridLayoutManager mLayoutMgr;
    private SwipeRefreshLayout pullToRefresh;

    private LoginInfo mLoginInfo;
    private int mCurrentPage;
    // Set sortMode Desc as default
    private final String mSortMode = StaticParameter.SortMode.CREATED_DATE_DESC;

    public Watchlist_TvShowTab() {
        // Required empty public constructor
    }

    public static Watchlist_TvShowTab newInstance() {
        Bundle args = new Bundle();
        Watchlist_TvShowTab fragment = new Watchlist_TvShowTab();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

        // set default page
        mCurrentPage = 1;

        // Initialize viewModel, data only survive this fragment lifecycle
        viewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        // Initialize liveData in viewModel, prevent from triggering observer multiple times
        viewModel.initLiveData();

        // Get SharedPreference file
        SharedPreferences sp = SharedPreferenceUtils.getOrCreateSharedPreference(StaticParameter.SharedPreferenceFileKey.SP_FILE_TMDB_KEY, context);

        // Initialize loginInfo
        mLoginInfo = SharedPreferenceUtils.getLoginInfoFromSharedPreference(sp);

        if (mLoginInfo.isLogin()) { // LOGIN TMDB
            // Set the observer
            viewModel.getTvShowWatchlistLiveData().observe(this, tvShowWatchlistTMDBObserver);
        } else { // NOT LOGIN
            // Load tvShow watchlist from local database and observe it
            viewModel.loadAllTvShowWatchlist().observe(this, loadTvShowWatchlistObserver());
        }
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
        mShimmer = view.findViewById(R.id.shimmer);
        pullToRefresh = view.findViewById(R.id.swiperefresh);
        View emptyDataView = view.findViewById(R.id.empty_data_hint);

        initRecyclerView(emptyDataView);

        if (mLoginInfo.isLogin()) { // LOGIN TMDB
            // Set SwipeRefreshListener
            pullToRefresh.setOnRefreshListener(() -> {
                // Refetching data
                resetTMDBResult();
                Log.d(LOG_TAG, "onRefresh");
                pullToRefresh.setRefreshing(false);
            });
            fetchTvShowWatchlistFromTMDB(mLoginInfo.getUserId(), mLoginInfo.getSession(), mSortMode, mCurrentPage);
        } else { // NOT LOGIN
            // show shimmer animation
            mShimmer.startShimmer();
            mShimmer.setVisibility(View.VISIBLE);

            // Set SwipeRefreshListener
            pullToRefresh.setOnRefreshListener(() -> {
                // do nothing
                pullToRefresh.setRefreshing(false);
            });
        }

    }

    /**
     * Initialize RecyclerView
     */
    private void initRecyclerView(View emptyDataView){
        // Initialize Recycler Adapter
        mAdapter = new TvShowsAdapter((AppCompatActivity) getActivity());

        // Set adapter
        mRcView.setAdapter(mAdapter);

        // Set EmptyStateObserver
        EmptyDataObserver emptyDataObserver = new EmptyDataObserver(mRcView, emptyDataView);
        mAdapter.registerAdapterDataObserver(emptyDataObserver);

        // Set NestedScrollingEnable
        mRcView.setNestedScrollingEnabled(true);

        // Initialize gridLayoutManager
        mLayoutMgr = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);

        // Set layoutManager
        mRcView.setLayoutManager(mLayoutMgr);
    }

    // region Room Database

    /**
     * Observe when TvShow Watchlist LiveData changed
     */
    public Observer<List<TvShowWatchlistEntity>> loadTvShowWatchlistObserver() {
        return tvShowWatchlist -> {
            // hide shimmer animation
            mShimmer.stopShimmer();
            mShimmer.setVisibility(View.GONE);

            // Mapping "TvShowWatchlistEntity" object to "TvShowData" object
            ArrayList<TvShowData> tvShowData_list = tvShowWatchlist.stream().map(data -> new TvShowData(
                    data.getTvShowId(),
                    data.getTitle(),
                    data.getPosterPath(),
                    data.getRating())).collect(Collectors.toCollection(ArrayList::new));

            // append data to adapter
            mAdapter.setTvShows(tvShowData_list);

            Log.d(LOG_TAG, "watchlist tvShows: data loaded successfully");
        };
    }

    // endregion


    // region Remote Data Source (API)

    /**
     * Start fetching tvShow watchlist from TMDB
     *
     * @param userId   Account Id
     * @param session  Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page     target page
     */
    private void fetchTvShowWatchlistFromTMDB(long userId, String session, String sortMode, int page) {
        if (userId >= 0) {
            // show shimmer animation
            mShimmer.startShimmer();
            mShimmer.setVisibility(View.VISIBLE);
            viewModel.getTMDBTvShowWatchlist(userId, session, sortMode, page);
        }
    }


    /**
     * Observe when tvShow watchlist from TMDB LiveData changed
     */
    private final Observer<ArrayList<TvShowData>> tvShowWatchlistTMDBObserver = tvShows -> {
        // hide shimmer animation
        mShimmer.stopShimmer();
        mShimmer.setVisibility(View.GONE);

        if (tvShows.size() > 0) {
            // append data to adapter
            mAdapter.appendTvShows(tvShows);

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
                            mCurrentPage++;
                            fetchTvShowWatchlistFromTMDB(mLoginInfo.getUserId(), mLoginInfo.getSession(), mSortMode, mCurrentPage);
                        }
                    }
                }
            });
        }

        Log.d(LOG_TAG, "tvShow watchlist: data fetched successfully");
    };


    /**
     * Reset TMDB results
     */
    private void resetTMDBResult() {
        // set default page
        mCurrentPage = 1;

        // remove data in adapter
        mAdapter.removeAllTvShows();

        // Start fetching data
        fetchTvShowWatchlistFromTMDB(mLoginInfo.getUserId(), mLoginInfo.getSession(), mSortMode, mCurrentPage);
    }

    // endregion

}