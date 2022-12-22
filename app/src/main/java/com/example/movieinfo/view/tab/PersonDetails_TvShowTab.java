package com.example.movieinfo.view.tab;

import android.content.Context;
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
import com.example.movieinfo.model.person.PersonDetailData;
import com.example.movieinfo.model.person.PersonDetailData.CreditsTvShowResponse;
import com.example.movieinfo.model.tvshow.TvShowData;
import com.example.movieinfo.view.adapter.EmptyDataObserver;
import com.example.movieinfo.view.adapter.TvShowsAdapter;
import com.example.movieinfo.viewmodel.PersonDetailViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class PersonDetails_TvShowTab extends Fragment {

    private final String LOG_TAG = "PersonDetails_TvShowTab";

    private Context context;

    private PersonDetailViewModel viewModel;

    private ShimmerFrameLayout mShimmer;
    private RecyclerView mRcView;
    private TvShowsAdapter mAdapter;
    private GridLayoutManager mLayoutMgr;

    public PersonDetails_TvShowTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

        // Get the same viewModel that created in parent activity, in order to share the data
        viewModel = new ViewModelProvider(getActivity()).get(PersonDetailViewModel.class);

        // Set observer
        viewModel.getPersonDetailLiveData().observe(this, getPersonDetailsObserver());

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
        SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.swiperefresh);
        View emptyDataView = view.findViewById(R.id.empty_data_hint);

        initRecyclerView(emptyDataView);

        // Disable swipeRefresh
        pullToRefresh.setEnabled(false);

        // show shimmer animation
        mShimmer.startShimmer();
        mShimmer.setVisibility(View.VISIBLE);
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
        mLayoutMgr = new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false);

        // Set layoutManager
        mRcView.setLayoutManager(mLayoutMgr);
    }

    /**
     * Observe when PersonDetails LiveData changed
     */
    public Observer<PersonDetailData> getPersonDetailsObserver() {
        return personDetailData -> {
            // hide shimmer animation
            mShimmer.stopShimmer();
            mShimmer.setVisibility(View.GONE);

            CreditsTvShowResponse creditsTvShowResponse = personDetailData.getCreditsTvShowResponse();
            if (creditsTvShowResponse != null) {
                ArrayList<TvShowData> allTvShows = new ArrayList<>();
                ArrayList<TvShowData> castedTvShows = creditsTvShowResponse.getCastedTvShow_list();
                ArrayList<TvShowData> crewedTvShows = creditsTvShowResponse.getCrewedTvShow_list();
                allTvShows.addAll(castedTvShows);
                allTvShows.addAll(crewedTvShows);
                // get data without duplicated id
                allTvShows = creditsTvShowResponse.getTvShowsWithoutDuplicatedId(allTvShows);
                // sort by releaseDate
                allTvShows = creditsTvShowResponse.sortByReleaseDate(allTvShows);

                if (allTvShows.size() > 0) {
                    // set data to adapter
                    mAdapter.setTvShows(allTvShows);
                }
            }

            Log.d(LOG_TAG, "PersonDetails: liveData changed");
        };
    }


}