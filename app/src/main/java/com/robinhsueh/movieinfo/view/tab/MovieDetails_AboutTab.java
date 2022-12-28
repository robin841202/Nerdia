package com.robinhsueh.movieinfo.view.tab;

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

import com.robinhsueh.movieinfo.R;
import com.robinhsueh.movieinfo.model.GenreData;
import com.robinhsueh.movieinfo.view.YoutubePlayerActivity;
import com.robinhsueh.movieinfo.model.StaticParameter;
import com.robinhsueh.movieinfo.model.VideosResponse;
import com.robinhsueh.movieinfo.model.movie.MovieDetailData;
import com.robinhsueh.movieinfo.view.adapter.ThumbnailsAdapter;
import com.robinhsueh.movieinfo.viewmodel.MediaDetailViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.common.base.Strings;

import java.util.ArrayList;

import io.github.giangpham96.expandabletextview.ExpandableTextView;

public class MovieDetails_AboutTab extends Fragment implements ThumbnailsAdapter.IThumbnailListener {

    private final String LOG_TAG = "MovieDetails_AboutTab";

    private Context context;

    private ExpandableTextView overViewTextView;
    private ViewGroup genresGroup;
    private ViewGroup videosGroup;
    private TextView statusTextView;
    private TextView runtimeTextView;
    private TextView budgetTextView;
    private TextView revenueTextView;

    private ShimmerFrameLayout videoThumbnail_Shimmer;
    private ThumbnailsAdapter videoThumbnailAdapter;

    public MovieDetails_AboutTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

        // Get the same viewModel that created in parent activity, in order to share the data
        MediaDetailViewModel mediaDetailViewModel = new ViewModelProvider(getActivity()).get(MediaDetailViewModel.class);

        // Set movieDetail observer
        mediaDetailViewModel.getMovieDetailLiveData().observe(this, getDataObserver());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_details_about_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        overViewTextView = view.findViewById(R.id.expandText_movie_overview);
        genresGroup = view.findViewById(R.id.group_genres);
        videosGroup = view.findViewById(R.id.group_videos);
        statusTextView = view.findViewById(R.id.text_status);
        runtimeTextView = view.findViewById(R.id.text_runtime);
        budgetTextView = view.findViewById(R.id.text_budget);
        revenueTextView = view.findViewById(R.id.text_revenue);
        RecyclerView videoThumbnail_RcView = view.findViewById(R.id.recycler_videos);
        videoThumbnail_Shimmer = view.findViewById(R.id.shimmer_videos);

        // Initialize Recycler Adapter
        videoThumbnailAdapter = new ThumbnailsAdapter(this);

        // Set adapter
        videoThumbnail_RcView.setAdapter(videoThumbnailAdapter);

        // Set layoutManager
        videoThumbnail_RcView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        // show shimmer animation
        videoThumbnail_Shimmer.startShimmer();
        videoThumbnail_Shimmer.setVisibility(View.VISIBLE);

    }

    /**
     * Observe when MovieDetail LiveData changed
     */
    private Observer<MovieDetailData> getDataObserver() {
        return movieDetail -> {
            if (movieDetail != null){
                // populate data to UI
                populateUI(movieDetail);
            }
        };
    }

    private void populateUI(MovieDetailData movieDetail) {
        // hide shimmer animation
        videoThumbnail_Shimmer.stopShimmer();
        videoThumbnail_Shimmer.setVisibility(View.GONE);

        // get all information
        String overView = Strings.isNullOrEmpty(movieDetail.getOverview()) ? getString(R.string.label_empty) : movieDetail.getOverview();
        ArrayList<GenreData> genre_List = movieDetail.getGenres();
        String status = Strings.isNullOrEmpty(movieDetail.getStatus()) ? getString(R.string.label_empty) : movieDetail.getStatus();
        int runtimeInMinutes = movieDetail.getRuntime();
        long budgetNum = movieDetail.getBudget();
        long revenueNum = movieDetail.getRevenue();

        // set overview - using ExpandableTextView library setOriginalText function to show contents, do not use setText
        overViewTextView.setOriginalText(overView);

        // set genres
        if (genre_List != null) {
            if (genre_List.size() > 0){
                for (int i = 0; i < genre_List.size(); i++) {
                    GenreData genre = genre_List.get(i);
                    if (genre != null) {
                        // add a TextView dynamically
                        addGenreTextViewToGroup(genre, genresGroup);
                    }
                }
            }else{ // no data available
                TextView emptyTextView = new TextView(context);
                emptyTextView.setText(R.string.label_empty);
                genresGroup.addView(emptyTextView);
            }
        }

        // set status
        statusTextView.setText(status);

        // set runtime
        String runtime;
        if (runtimeInMinutes > 0) {
            int hours = runtimeInMinutes / 60;
            int minutes = runtimeInMinutes % 60;
            runtime = String.format("%d 小時 %02d 分鐘", hours, minutes);
        } else {
            runtime = getString(R.string.label_empty);
        }
        runtimeTextView.setText(runtime);

        // set budget
        String budget = budgetNum > 0 ? String.format("USD$ %,d", budgetNum) : getString(R.string.label_empty);
        budgetTextView.setText(budget);

        // set revenue
        String revenue = revenueNum > 0 ? String.format("USD$ %,d", revenueNum) : getString(R.string.label_empty);
        revenueTextView.setText(revenue);

        // set video thumbnails recyclerView
        VideosResponse videosResponse = movieDetail.getVideosResponse();
        if (videosResponse != null) {
            ArrayList<VideosResponse.VideoData> videos = videosResponse.getVideo_list();
            // sort videos first
            videos = videosResponse.sortVideos(videos);
            // get videos only provided by youtube
            videos = videosResponse.getVideosBySourceSite(videos, StaticParameter.VideoSourceSite.YOUTUBE);
            if (videos.size() > 0){
                videosGroup.setVisibility(View.VISIBLE);
                videoThumbnailAdapter.setVideos(videos);
            }else{ // no data available
                videosGroup.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Add a new TextView to Genres Group
     *
     * @param genre GenreData
     * @param group Container that contains multiple genre TextView
     */
    private void addGenreTextViewToGroup(GenreData genre, ViewGroup group) {
        TextView genreTextView = new TextView(context);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMarginEnd(16);
        genreTextView.setLayoutParams(params);
        genreTextView.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Small);
        genreTextView.setBackgroundResource(R.drawable.rounded_corner);
        genreTextView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.teal_700));
        genreTextView.setText(genre.getName());
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
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}

