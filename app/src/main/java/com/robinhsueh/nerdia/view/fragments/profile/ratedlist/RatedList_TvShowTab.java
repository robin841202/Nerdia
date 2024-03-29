package com.robinhsueh.nerdia.view.fragments.profile.ratedlist;

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

import com.robinhsueh.nerdia.R;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.tvshow.TvShowData;
import com.robinhsueh.nerdia.model.user.LoginInfo;
import com.robinhsueh.nerdia.utils.SharedPreferenceUtils;
import com.robinhsueh.nerdia.view.adapter.EmptyDataObserver;
import com.robinhsueh.nerdia.view.adapter.TvShowsAdapter;
import com.robinhsueh.nerdia.viewmodel.RatedListViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class RatedList_TvShowTab extends Fragment {

    private final String LOG_TAG = "RatedList_TvShowTab";

    private RatedListViewModel viewModel;
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

    public RatedList_TvShowTab() {
        // Required empty public constructor
    }

    public static RatedList_TvShowTab newInstance() {
        Bundle args = new Bundle();
        RatedList_TvShowTab fragment = new RatedList_TvShowTab();
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
        viewModel = new ViewModelProvider(this).get(RatedListViewModel.class);
        // Initialize liveData in viewModel, prevent from triggering observer multiple times
        viewModel.initLiveData();

        // Get SharedPreference file
        SharedPreferences sp = SharedPreferenceUtils.getOrCreateSharedPreference(StaticParameter.SharedPreferenceFileKey.SP_FILE_TMDB_KEY, context);

        // Initialize loginInfo
        mLoginInfo = SharedPreferenceUtils.getLoginInfoFromSharedPreference(sp);
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
            // Set the observer
            viewModel.getRatedTvShowsLiveData().observe(getViewLifecycleOwner(), ratedTvShowsTMDBObserver);

            // Set SwipeRefreshListener
            pullToRefresh.setOnRefreshListener(() -> {
                // Refetching data
                resetTMDBResult();
                Log.d(LOG_TAG, "onRefresh");
                pullToRefresh.setRefreshing(false);
            });
            fetchRatedTvShowsFromTMDB(mLoginInfo.getUserId(), mLoginInfo.getSession(), mSortMode, mCurrentPage);
        }

    }

    /**
     * Initialize RecyclerView
     */
    private void initRecyclerView(View emptyDataView) {
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

    // region Remote Data Source (API)

    /**
     * Start fetching rated tvShows from TMDB
     *
     * @param userId   Account Id
     * @param session  Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page     target page
     */
    private void fetchRatedTvShowsFromTMDB(long userId, String session, String sortMode, int page) {
        if (userId >= 0) {
            // show shimmer animation
            mShimmer.startShimmer();
            mShimmer.setVisibility(View.VISIBLE);
            viewModel.getTMDBRatedTvShows(userId, session, sortMode, page);
        }
    }


    /**
     * Observe when rated tvShows from TMDB LiveData changed
     */
    private final Observer<ArrayList<TvShowData>> ratedTvShowsTMDBObserver = tvShows -> {
        if (tvShows != null){
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
                                fetchRatedTvShowsFromTMDB(mLoginInfo.getUserId(), mLoginInfo.getSession(), mSortMode, mCurrentPage);
                            }
                        }
                    }
                });
            }

            Log.d(LOG_TAG, "rated tvShows: data fetched successfully");
        }
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
        fetchRatedTvShowsFromTMDB(mLoginInfo.getUserId(), mLoginInfo.getSession(), mSortMode, mCurrentPage);
    }

    // endregion

}