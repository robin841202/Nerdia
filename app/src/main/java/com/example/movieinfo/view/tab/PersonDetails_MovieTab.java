package com.example.movieinfo.view.tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.person.PersonDetailData;
import com.example.movieinfo.model.person.PersonDetailData.CreditsMovieResponse;
import com.example.movieinfo.view.MediaDetailsActivity;
import com.example.movieinfo.view.adapter.MoviesAdapter;
import com.example.movieinfo.viewmodel.PersonDetailViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class PersonDetails_MovieTab extends Fragment implements MoviesAdapter.IMovieListener {

    private final String LOG_TAG = "PersonDetails_MovieTab";

    private Context context;

    private PersonDetailViewModel viewModel;

    private ShimmerFrameLayout mShimmer;
    private RecyclerView mRcView;
    private MoviesAdapter moviesAdapter;
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
        return inflater.inflate(R.layout.fragment_person_details_movie_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        mRcView = view.findViewById(R.id.recycler);
        mShimmer = view.findViewById(R.id.shimmer);

        // show shimmer animation
        mShimmer.startShimmer();
        mShimmer.setVisibility(View.VISIBLE);

        // Initialize Recycler Adapter
        moviesAdapter = new MoviesAdapter(this);

        // Set adapter
        mRcView.setAdapter(moviesAdapter);

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

            CreditsMovieResponse creditsMovieResponse = personDetailData.getCreditsMovieResponse();
            if (creditsMovieResponse != null){
                ArrayList<MovieData> allMovies = new ArrayList<>();
                ArrayList<MovieData> castedMovies = creditsMovieResponse.getCastedMovie_list();
                ArrayList<MovieData> crewedMovies = creditsMovieResponse.getCrewedMovie_list();
                allMovies.addAll(castedMovies);
                allMovies.addAll(crewedMovies);
                // get data without duplicated id
                allMovies = creditsMovieResponse.getMoviesWithoutDuplicatedId(allMovies);
                // sort by releaseDate
                allMovies = creditsMovieResponse.sortByReleaseDate(allMovies);

                if (allMovies.size() > 0){
                    // set data to adapter
                    moviesAdapter.setMovies(allMovies);
                }
            }

            Log.d(LOG_TAG, "PersonDetails: liveData changed");
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

}