package com.robinhsueh.movieinfo.view.tab;

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

import com.robinhsueh.movieinfo.R;
import com.robinhsueh.movieinfo.model.movie.MovieData;
import com.robinhsueh.movieinfo.model.person.PersonDetailData;
import com.robinhsueh.movieinfo.model.person.PersonDetailData.CreditsMovieResponse;
import com.robinhsueh.movieinfo.view.adapter.EmptyDataObserver;
import com.robinhsueh.movieinfo.view.adapter.MoviesAdapter;
import com.robinhsueh.movieinfo.viewmodel.PersonDetailViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class PersonDetails_MovieTab extends Fragment {

    private final String LOG_TAG = "PersonDetails_MovieTab";

    private Context context;

    private PersonDetailViewModel viewModel;

    private ShimmerFrameLayout mShimmer;
    private RecyclerView mRcView;
    private MoviesAdapter mAdapter;
    private GridLayoutManager mLayoutMgr;

    public PersonDetails_MovieTab() {
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
        mAdapter = new MoviesAdapter((AppCompatActivity) getActivity());

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
            if (personDetailData != null){
                // hide shimmer animation
                mShimmer.stopShimmer();
                mShimmer.setVisibility(View.GONE);

                CreditsMovieResponse creditsMovieResponse = personDetailData.getCreditsMovieResponse();
                if (creditsMovieResponse != null) {
                    ArrayList<MovieData> allMovies = new ArrayList<>();
                    ArrayList<MovieData> castedMovies = creditsMovieResponse.getCastedMovie_list();
                    ArrayList<MovieData> crewedMovies = creditsMovieResponse.getCrewedMovie_list();
                    allMovies.addAll(castedMovies);
                    allMovies.addAll(crewedMovies);
                    // get data without duplicated id
                    allMovies = creditsMovieResponse.getMoviesWithoutDuplicatedId(allMovies);
                    // sort by releaseDate
                    allMovies = creditsMovieResponse.sortByReleaseDate(allMovies);

                    if (allMovies.size() > 0) {
                        // set data to adapter
                        mAdapter.setMovies(allMovies);
                    }
                }

                Log.d(LOG_TAG, "PersonDetails: liveData changed");
            }
        };
    }

}