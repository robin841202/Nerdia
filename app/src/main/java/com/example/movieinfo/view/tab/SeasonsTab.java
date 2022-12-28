package com.example.movieinfo.view.tab;

import android.content.Context;
import android.content.Intent;
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

import com.example.movieinfo.R;
import com.example.movieinfo.model.CreditsResponse;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.tvshow.SeasonData;
import com.example.movieinfo.model.tvshow.TvShowDetailData;
import com.example.movieinfo.view.PersonDetailsActivity;
import com.example.movieinfo.view.adapter.CastsAdapter;
import com.example.movieinfo.view.adapter.EmptyDataObserver;
import com.example.movieinfo.view.adapter.SeasonsAdapter;
import com.example.movieinfo.viewmodel.MediaDetailViewModel;

import java.util.ArrayList;

public class SeasonsTab extends Fragment{

    private final String LOG_TAG = "SeasonsTab";

    private Context context;

    private RecyclerView mRcView;
    private SeasonsAdapter mAdapter;

    public SeasonsTab() {
        // Required empty public constructor
    }

    public static SeasonsTab newInstance() {
        Bundle args = new Bundle();
        SeasonsTab fragment = new SeasonsTab();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

        // Get the same viewModel that created in parent activity, in order to share the data
        MediaDetailViewModel mediaDetailViewModel = new ViewModelProvider(getActivity()).get(MediaDetailViewModel.class);

        // Set tvShowDetail observer
        mediaDetailViewModel.getTvShowDetailLiveData().observe(this, getTvShowDataObserver());

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
        View emptyDataView = view.findViewById(R.id.empty_data_hint);

        initRecyclerView(emptyDataView);
    }



    /**
     * Initialize RecyclerView
     */
    private void initRecyclerView(View emptyDataView) {
        // Initialize Recycler Adapter
        mAdapter = new SeasonsAdapter((AppCompatActivity) context);

        // Set adapter
        mRcView.setAdapter(mAdapter);

        // Set EmptyStateObserver
        EmptyDataObserver emptyDataObserver = new EmptyDataObserver(mRcView, emptyDataView);
        mAdapter.registerAdapterDataObserver(emptyDataObserver);

        // Set NestedScrollingEnable
        mRcView.setNestedScrollingEnabled(true);

        // Set layoutManager
        mRcView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
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
     * @param tvShowDetail
     */
    private void populateUI(TvShowDetailData tvShowDetail) {

        // set recyclerView
        ArrayList<SeasonData> seasons = tvShowDetail.getSeasons();
        if (seasons != null) {
            mAdapter.setItems(seasons);
        }
    }

}