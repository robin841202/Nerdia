package com.robinhsueh.nerdia.view.tab;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.robinhsueh.nerdia.R;
import com.robinhsueh.nerdia.model.ReviewsResponse;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.view.adapter.EmptyDataObserver;
import com.robinhsueh.nerdia.view.adapter.ReviewAdapter;
import com.robinhsueh.nerdia.viewmodel.MediaDetailViewModel;

import java.util.ArrayList;

public class ReviewsTab extends Fragment {

    private final String LOG_TAG = "ReviewsTab";

    private Context context;

    private MediaDetailViewModel viewModel;

    private String mediaType;
    private long mediaId;
    private int currentPage;

    private ProgressBar progressBar;

    private SwipeRefreshLayout pullToRefresh;
    private RecyclerView mRcView;
    private ReviewAdapter mAdapter;
    private LinearLayoutManager mLayoutMgr;

    public ReviewsTab() {
        // Required empty public constructor
    }

    public static ReviewsTab newInstance(String mediaType, long id) {
        Bundle args = new Bundle();
        args.putString(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY, mediaType);
        args.putLong("id", id);
        ReviewsTab fragment = new ReviewsTab();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

        // Get arguments from constructor
        if (getArguments() != null) {
            mediaType = getArguments().getString(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY);
            mediaId = getArguments().getLong("id");
        }

        // set default page
        currentPage = 1;

        // Get the same viewModel that created in parent activity, in order to share the data
        viewModel = new ViewModelProvider(getActivity()).get(MediaDetailViewModel.class);

        // Set observer
        viewModel.getReviewsLiveData().observe(this, reviewsObserver);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_general_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        mRcView = view.findViewById(R.id.recycler);
        pullToRefresh = view.findViewById(R.id.swiperefresh);
        progressBar = view.findViewById(R.id.progressBar);
        View emptyDataView = view.findViewById(R.id.empty_data_hint);

        initRecyclerView(emptyDataView);

        // Set SwipeRefreshListener
        pullToRefresh.setOnRefreshListener(() -> {
            // Start getting data
            updateData(mediaType);
            Log.d(LOG_TAG, "onRefresh");
            pullToRefresh.setRefreshing(false);
        });

        // Start getting data
        updateData(mediaType);
    }

    /**
     * Initialize RecyclerView
     */
    private void initRecyclerView(View emptyDataView){
        // Initialize Recycler Adapter
        mAdapter = new ReviewAdapter((AppCompatActivity) context);

        // Set adapter
        mRcView.setAdapter(mAdapter);

        // Set EmptyStateObserver
        EmptyDataObserver emptyDataObserver = new EmptyDataObserver(mRcView, emptyDataView);
        mAdapter.registerAdapterDataObserver(emptyDataObserver);

        // Set NestedScrollingEnable
        mRcView.setNestedScrollingEnabled(true);

        // Initialize gridLayoutManager
        mLayoutMgr = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        // Set layoutManager
        mRcView.setLayoutManager(mLayoutMgr);
    }

    /**
     * Observe when ReviewData List LiveData changed
     */
    private final Observer<ArrayList<ReviewsResponse.ReviewData>> reviewsObserver = reviews -> {
        if (reviews != null){
            // hide progressBar
            progressBar.setVisibility(View.GONE);

            if (reviews.size() > 0) {
                // append data to adapter
                mAdapter.appendItems(reviews);

                // attach onScrollListener to RecyclerView
                mRcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                        // when scrolling up
                        if (dy > 0) {
                            final int visibleThreshold = 5;

                            // get the number of all items in recyclerView
                            int totalItemCount = mLayoutMgr.getItemCount();
                            // get the last visible item's position
                            int lastVisibleItem = mLayoutMgr.findLastCompletelyVisibleItemPosition();

                            if (totalItemCount <= lastVisibleItem + visibleThreshold) {
                                // detach current OnScrollListener
                                mRcView.removeOnScrollListener(this);

                                // append nextPage data to recyclerView
                                currentPage++;

                                // fetch more data
                                switch (mediaType) {
                                    case StaticParameter.MediaType.MOVIE:
                                        fetchMovieReviews(mediaId, currentPage);
                                        break;
                                    case StaticParameter.MediaType.TV:
                                        fetchTvShowReviews(mediaId, currentPage);
                                        break;
                                }

                            }
                        }
                    }
                });
            }

            Log.d(LOG_TAG, "reviews: data fetched successfully");
        }
    };


    // region Movie Reviews

    /**
     * Fetching movie reviews
     *
     * @param movieId Movie Id
     * @param page    result page
     */
    private void fetchMovieReviews(long movieId, int page) {
        if (movieId >= 0) {
            // show progressBar
            progressBar.setVisibility(View.VISIBLE);
            viewModel.getTMDBMovieReviews(movieId, page);
        }
    }

    // endregion


    // region TvShow Reviews

    /**
     * Fetching tvShow reviews
     *
     * @param tvShowId TvShow Id
     * @param page     result page
     */
    private void fetchTvShowReviews(long tvShowId, int page) {
        if (tvShowId >= 0) {
            // show shimmer animation
            progressBar.setVisibility(View.VISIBLE);
            viewModel.getTMDBTvShowReviews(tvShowId, page);
        }
    }

    // endregion


    /**
     * Update results
     */
    private void updateData(String mediaType) {
        // set default page
        currentPage = 1;

        // remove items
        mAdapter.removeAll();
        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:
                fetchMovieReviews(mediaId, currentPage);
                break;
            case StaticParameter.MediaType.TV:
                fetchTvShowReviews(mediaId, currentPage);
                break;
        }

    }

}