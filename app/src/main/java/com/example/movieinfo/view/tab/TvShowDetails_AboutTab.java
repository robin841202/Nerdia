package com.example.movieinfo.view.tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.example.movieinfo.model.Genre;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.VideosResponse;
import com.example.movieinfo.model.tvshow.TvShowDetailData;
import com.example.movieinfo.view.YoutubePlayerActivity;
import com.example.movieinfo.view.adapter.ThumbnailsAdapter;
import com.example.movieinfo.viewmodel.TvShowDetailViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.common.base.Strings;

import java.util.ArrayList;

import io.github.giangpham96.expandabletextview.ExpandableTextView;

public class TvShowDetails_AboutTab extends Fragment implements ThumbnailsAdapter.IThumbnailListener {

    private final String LOG_TAG = "TvShowDetails_AboutTab";

    private Context context;

    private TvShowDetailViewModel tvShowDetailViewModel;

    private ExpandableTextView overViewTextView;
    private ViewGroup genresGroup;
    private TextView statusTextView;
    private TextView firstAirDateTextView;
    private TextView lastAirDateTextView;
    private TextView numOfEpisodesTextView;

    private ShimmerFrameLayout videoThumbnail_Shimmer;
    private RecyclerView videoThumbnail_RcView;
    private ThumbnailsAdapter videoThumbnailAdapter;

    public TvShowDetails_AboutTab() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

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
        overViewTextView = view.findViewById(R.id.expandText_tvShow_overview);
        genresGroup = view.findViewById(R.id.group_genres);
        statusTextView = view.findViewById(R.id.text_status);
        firstAirDateTextView = view.findViewById(R.id.text_first_air_date);
        lastAirDateTextView = view.findViewById(R.id.text_last_air_date);
        numOfEpisodesTextView = view.findViewById(R.id.text_numOfEpisodes);
        videoThumbnail_RcView = view.findViewById(R.id.recycler_videos);
        videoThumbnail_Shimmer = view.findViewById(R.id.shimmer_videos);

        // Initialize Recycler Adapter
        videoThumbnailAdapter = new ThumbnailsAdapter(new ArrayList<>(), this);

        // Set adapter
        videoThumbnail_RcView.setAdapter(videoThumbnailAdapter);

        // Set layoutManager
        videoThumbnail_RcView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

    }

    /**
     * Observe when TvShowDetail LiveData changed
     */
    private Observer<TvShowDetailData> getDataObserver() {
        return tvShowDetail -> {
            // show shimmer animation
            videoThumbnail_Shimmer.startShimmer();
            videoThumbnail_Shimmer.setVisibility(View.VISIBLE);
            // populate data to UI
            populateUI(tvShowDetail);
        };
    }

    private void populateUI(TvShowDetailData tvShowDetail) {
        // hide shimmer animation
        videoThumbnail_Shimmer.stopShimmer();
        videoThumbnail_Shimmer.setVisibility(View.GONE);

        // get all information
        String overView = Strings.isNullOrEmpty(tvShowDetail.getOverview()) ? getString(R.string.label_empty) : tvShowDetail.getOverview();
        ArrayList<Genre> genre_List = tvShowDetail.getGenres();
        String status = Strings.isNullOrEmpty(tvShowDetail.getStatus()) ? getString(R.string.label_empty) : tvShowDetail.getStatus();
        String firstAirDate = Strings.isNullOrEmpty(tvShowDetail.getOnAirDate()) ? getString(R.string.label_empty) : tvShowDetail.getOnAirDate();
        String lastAirDate = Strings.isNullOrEmpty(tvShowDetail.getLastAirDate()) ? getString(R.string.label_empty) : tvShowDetail.getLastAirDate();
        int numOfEpisodes = tvShowDetail.getNumOfEpisodes();


        // set overview - using ExpandableTextView library setOriginalText function to show contents, do not use setText
        overViewTextView.setOriginalText(overView);

        // set genres
        if (genre_List != null) {
            for (int i = 0; i < genre_List.size(); i++) {
                Genre genre = genre_List.get(i);

                // add a TextView dynamically
                addGenreTextViewToGroup(genre.getName(), genresGroup);
            }
        }

        // set status
        statusTextView.setText(status);

        // set firstAirDate
        firstAirDateTextView.setText(firstAirDate);

        // set lastAirDate
        lastAirDateTextView.setText(lastAirDate);

        // set episode runtime
        String numOfEpisodesDisplay = numOfEpisodes > 0 ? String.format("%d 集", numOfEpisodes) : getString(R.string.label_empty);
        numOfEpisodesTextView.setText(numOfEpisodesDisplay);

        // set video thumbnails recyclerView
        VideosResponse videosResponse = tvShowDetail.getVideosResponse();
        if (videosResponse != null) {
            // sort videos first
            videosResponse.sortVideos();
            // get videos only provided by youtube
            ArrayList<VideosResponse.VideoData> videos = videosResponse.getVideosBySourceSite(StaticParameter.VideoSourceSite.YOUTUBE);
            videoThumbnailAdapter.setVideos(videos);
        }
    }

    /**
     * Add a new TextView to Genres Group
     *
     * @param name  Genre name
     * @param group Container that contains multiple genre TextView
     */
    private void addGenreTextViewToGroup(String name, ViewGroup group) {
        TextView genreTextView = new TextView(context);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMarginEnd(8);
        genreTextView.setLayoutParams(params);
        genreTextView.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Small);
        genreTextView.setBackgroundResource(R.drawable.rounded_corner);
        genreTextView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.lightGray));
        genreTextView.setText(name);
        group.addView(genreTextView);
    }

    /**
     * Callback when video item get clicked
     *
     * @param video video Data
     */
    @Override
    public void onVideoClick(VideosResponse.VideoData video) {
        Intent intent = new Intent(getContext(), YoutubePlayerActivity.class);
        intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_VIDEO_ID_KEY, video.getVideoId());
        startActivity(intent);
        // set the custom transition animation
        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}