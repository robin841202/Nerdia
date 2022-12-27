package com.example.movieinfo.view.tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movieinfo.R;
import com.example.movieinfo.model.CreditsResponse;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.tvshow.TvShowDetailData;
import com.example.movieinfo.view.PersonDetailsActivity;
import com.example.movieinfo.view.adapter.CastsAdapter;
import com.example.movieinfo.viewmodel.MediaDetailViewModel;

import java.util.ArrayList;

public class CastTab extends Fragment implements CastsAdapter.ICastListener {

    private final String LOG_TAG = "CastTab";

    private final String peopleCountFormatText = "%d 位";
    private Context context;

    private MediaDetailViewModel mediaDetailViewModel;

    private String mediaType;

    private TextView countTextView;
    private RecyclerView cast_RcView;
    private CastsAdapter castAdapter;

    public CastTab() {
        // Required empty public constructor
    }

    public static CastTab newInstance(String mediaType) {
        Bundle args = new Bundle();
        args.putString(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY, mediaType);
        CastTab fragment = new CastTab();
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
        cast_RcView = view.findViewById(R.id.recycler_cast);
        countTextView = view.findViewById(R.id.text_count);

        // Initialize Recycler Adapter
        castAdapter = new CastsAdapter(this);

        // Set adapter
        cast_RcView.setAdapter(castAdapter);
        cast_RcView.setNestedScrollingEnabled(true);
        ;

        // Set layoutManager
        cast_RcView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        // Set default people count text
        countTextView.setText(String.format(peopleCountFormatText, 0));
    }


    /**
     * Observe when MovieDetail LiveData changed
     */
    private Observer<MovieDetailData> getMovieDataObserver() {
        return movieDetail -> {
            if (movieDetail != null){
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

        // set casts recyclerView
        CreditsResponse creditsResponse = movieDetail.getCreditsResponse();
        if (creditsResponse != null) {
            ArrayList<CreditsResponse.CastData> casts = creditsResponse.getCast_list();
            castAdapter.setCasts(casts);
            // Set people count text
            countTextView.setText(String.format(peopleCountFormatText, casts.size()));
        }
    }

    /**
     * Populate data into view
     *
     * @param tvShowDetail
     */
    private void populateUI(TvShowDetailData tvShowDetail) {

        // set casts recyclerView
        CreditsResponse creditsResponse = tvShowDetail.getCreditsResponse();
        if (creditsResponse != null) {
            ArrayList<CreditsResponse.CastData> casts = creditsResponse.getCast_list();
            castAdapter.setCasts(casts);
            // Set people count text
            countTextView.setText(String.format(peopleCountFormatText, casts.size()));
        }
    }

    /**
     * Callback when cast item get clicked
     *
     * @param cast cast Data
     */
    @Override
    public void onCastClick(CreditsResponse.CastData cast) {
        Intent intent = new Intent(getContext(), PersonDetailsActivity.class);
        intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_PERSON_ID_KEY, cast.getId());
        startActivity(intent);
        // set the custom transition animation
        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}