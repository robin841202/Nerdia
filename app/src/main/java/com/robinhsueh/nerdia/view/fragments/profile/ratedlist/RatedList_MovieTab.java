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
import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.model.user.LoginInfo;
import com.robinhsueh.nerdia.utils.SharedPreferenceUtils;
import com.robinhsueh.nerdia.view.adapter.EmptyDataObserver;
import com.robinhsueh.nerdia.view.adapter.MoviesAdapter;
import com.robinhsueh.nerdia.viewmodel.RatedListViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class RatedList_MovieTab extends Fragment {

    private final String LOG_TAG = "RatedList_MovieTab";

    private RatedListViewModel viewModel;
    private Context context;

    private ShimmerFrameLayout mShimmer;
    private RecyclerView mRcView;
    private MoviesAdapter mAdapter;
    private GridLayoutManager mLayoutMgr;
    private SwipeRefreshLayout pullToRefresh;

    private LoginInfo mLoginInfo;
    private int mCurrentPage;
    // Set sortMode Desc as default
    private final String mSortMode = StaticParameter.SortMode.CREATED_DATE_DESC;

    public RatedList_MovieTab() {
        // Required empty public constructor
    }

    public static RatedList_MovieTab newInstance() {
        Bundle args = new Bundle();
        RatedList_MovieTab fragment = new RatedList_MovieTab();
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
            viewModel.getRatedMoviesLiveData().observe(getViewLifecycleOwner(), ratedMoviesTMDBObserver);

            // Set SwipeRefreshListener
            pullToRefresh.setOnRefreshListener(() -> {
                // Refetching data
                resetTMDBResult();
                Log.d(LOG_TAG, "onRefresh");
                pullToRefresh.setRefreshing(false);
            });
            fetchRatedMoviesFromTMDB(mLoginInfo.getUserId(), mLoginInfo.getSession(), mSortMode, mCurrentPage);
        }
    }

    /**
     * Initialize RecyclerView
     */
    private void initRecyclerView(View emptyDataView) {
        // Initialize Recycler Adapter
        mAdapter = new MoviesAdapter((AppCompatActivity) getActivity());

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
     * Start fetching rated movies from TMDB
     *
     * @param userId   Account Id
     * @param session  Valid session
     * @param sortMode Allowed Values: created_at.asc, created_at.desc, defined in StaticParameter.SortMode
     * @param page     target page
     */
    private void fetchRatedMoviesFromTMDB(long userId, String session, String sortMode, int page) {
        if (userId >= 0) {
            // show shimmer animation
            mShimmer.startShimmer();
            mShimmer.setVisibility(View.VISIBLE);
            viewModel.getTMDBRatedMovies(userId, session, sortMode, page);
        }
    }


    /**
     * Observe when rated movies from TMDB LiveData changed
     */
    private final Observer<ArrayList<MovieData>> ratedMoviesTMDBObserver = movies -> {
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
                                mCurrentPage++;
                                fetchRatedMoviesFromTMDB(mLoginInfo.getUserId(), mLoginInfo.getSession(), mSortMode, mCurrentPage);
                            }
                        }
                    }
                });
            }

            Log.d(LOG_TAG, "rated movies: data fetched successfully");
        }
    };


    /**
     * Reset TMDB results
     */
    private void resetTMDBResult() {
        // set default page
        mCurrentPage = 1;

        // remove data in adapter
        mAdapter.removeAllMovies();

        // Start fetching data
        fetchRatedMoviesFromTMDB(mLoginInfo.getUserId(), mLoginInfo.getSession(), mSortMode, mCurrentPage);
    }

    // endregion
}