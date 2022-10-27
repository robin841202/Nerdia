package com.example.movieinfo.view.tab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movieinfo.R;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.tvshow.TvShowDetailData;
import com.example.movieinfo.viewmodel.MovieDetailViewModel;
import com.example.movieinfo.viewmodel.TvShowDetailViewModel;

import io.github.giangpham96.expandabletextview.ExpandableTextView;

public class TvShowDetails_AboutTab extends Fragment {

    private final String LOG_TAG = "TvShowDetails_AboutTab";

    private TvShowDetailViewModel tvShowDetailViewModel;

    private ExpandableTextView overView;


    public TvShowDetails_AboutTab() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the same viewModel that created in parent activity, in order to share the data
        tvShowDetailViewModel = new ViewModelProvider(getActivity()).get(TvShowDetailViewModel.class);

        // Set movieDetail observer
        tvShowDetailViewModel.getTvShowDetailLiveData().observe(this, getDataObserver());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show_details_about_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        overView = view.findViewById(R.id.expandText_tvShow_overview);
    }

    /**
     * Observe when TvShowDetail LiveData changed
     */
    private Observer<TvShowDetailData> getDataObserver() {
        return tvShowDetail -> {
            // populate data to UI
            populateUI(tvShowDetail);
        };
    }

    private void populateUI(TvShowDetailData tvShowDetail) {
        // set overview - using ExpandableTextView library setOriginalText function to show contents, do not use setText
        overView.setOriginalText(tvShowDetail.getOverview() == null ? "" : tvShowDetail.getOverview());

    }
}