package com.robinhsueh.movieinfo.view.tab;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.robinhsueh.movieinfo.R;
import com.robinhsueh.movieinfo.model.CreditsResponse;
import com.robinhsueh.movieinfo.model.StaticParameter;
import com.robinhsueh.movieinfo.model.movie.MovieDetailData;
import com.robinhsueh.movieinfo.model.tvshow.TvShowDetailData;
import com.robinhsueh.movieinfo.view.adapter.CrewAdapter;
import com.robinhsueh.movieinfo.viewmodel.MediaDetailViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class CrewTab extends Fragment {

    private final String LOG_TAG = "CrewTab";

    private final String peopleCountFormatText = "%d 位";
    private Context context;

    private MediaDetailViewModel mediaDetailViewModel;

    private String mediaType;

    private TextView countTextView;
    private RecyclerView mRcView;
    private CrewAdapter mAdapter;

    public CrewTab() {
        // Required empty public constructor
    }

    public static CrewTab newInstance(String mediaType) {
        Bundle args = new Bundle();
        args.putString(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY, mediaType);
        CrewTab fragment = new CrewTab();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

        // Get the same viewModel that created in parent activity, in order to share the data
        mediaDetailViewModel = new ViewModelProvider(getActivity()).get(MediaDetailViewModel.class);

        // Get arguments from constructor
        if (getArguments() != null) {
            mediaType = getArguments().getString(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY);
        }

        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:
                // Set movieDetail observer
                mediaDetailViewModel.getMovieDetailLiveData().observe(this, getMovieDataObserver());
                break;
            case StaticParameter.MediaType.TV:
                // Set tvShowDetail observer
                mediaDetailViewModel.getTvShowDetailLiveData().observe(this, getTvShowDataObserver());
                break;
            default:
                // do nothing
                break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cast_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        mRcView = view.findViewById(R.id.recycler_cast);
        countTextView = view.findViewById(R.id.text_count);

        initRecyclerView();

        // Set default people count text
        countTextView.setText(String.format(Locale.TAIWAN, peopleCountFormatText, 0));
    }

    /**
     * Initialize RecyclerView
     */
    private void initRecyclerView() {
        // Initialize Recycler Adapter
        mAdapter = new CrewAdapter((AppCompatActivity) context);

        // Set adapter
        mRcView.setAdapter(mAdapter);

        // Set NestedScrollingEnable
        mRcView.setNestedScrollingEnabled(true);

        // Set layoutManager
        mRcView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }


    /**
     * Observe when MovieDetail LiveData changed
     */
    private Observer<MovieDetailData> getMovieDataObserver() {
        return movieDetail -> {
            if (movieDetail != null) {
                // populate data to UI
                populateUI(movieDetail);
            }
        };
    }

    /**
     * Observe when TvShowDetail LiveData changed
     */
    private Observer<TvShowDetailData> getTvShowDataObserver() {
        return tvShowDetail -> {
            if (tvShowDetail != null) {
                // populate data to UI
                populateUI(tvShowDetail);
            }
        };
    }

    /**
     * Populate data into view
     *
     * @param movieDetail
     */
    private void populateUI(MovieDetailData movieDetail) {

        // set crew recyclerView
        CreditsResponse creditsResponse = movieDetail.getCreditsResponse();
        if (creditsResponse != null) {
            ArrayList<CreditsResponse.CrewData> crew = creditsResponse.getCrew_list();
            // filter the main crew
            crew = creditsResponse.filterMainCrew(crew);
            // sort by job
            crew = creditsResponse.sortCrewByJob(crew);
            mAdapter.setItems(crew);
            // Set people count text
            countTextView.setText(String.format(Locale.TAIWAN, peopleCountFormatText, crew.size()));
        }
    }

    /**
     * Populate data into view
     *
     * @param tvShowDetail
     */
    private void populateUI(TvShowDetailData tvShowDetail) {

        // set crew recyclerView
        CreditsResponse creditsResponse = tvShowDetail.getCreditsResponse();
        if (creditsResponse != null) {
            ArrayList<CreditsResponse.CrewData> crew = creditsResponse.getCrew_list();
            // filter the main crew
            crew = creditsResponse.filterMainCrew(crew);
            // sort by job
            crew = creditsResponse.sortCrewByJob(crew);
            mAdapter.setItems(crew);
            // Set people count text
            countTextView.setText(String.format(Locale.TAIWAN, peopleCountFormatText, crew.size()));
        }
    }
}