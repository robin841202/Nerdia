package com.example.movieinfo.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.movieinfo.BuildConfig;
import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.VideosResponse.VideoData;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.VideosResponse;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;

public class ThumbnailsAdapter extends RecyclerView.Adapter<ThumbnailsAdapter.ThumbnailsViewHolder> {
    private final String LOG_TAG = "ThumbnailsAdapter";

    private ArrayList<VideoData> video_list;
    private final IThumbnailListener listener;

    public interface IThumbnailListener {
        /**
         * Video item onClick Event
         */
        void onVideoClick(VideoData video);
    }


    public ThumbnailsAdapter(IThumbnailListener listener) {
        this.video_list = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public ThumbnailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_video_thumbnail, parent, false);
        return new ThumbnailsViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ThumbnailsViewHolder holder, int position) {
        holder.bind(video_list.get(position));
    }

    @Override
    public int getItemCount() {
        return video_list.size();
    }


    public void setVideos(ArrayList<VideoData> videos) {
        video_list = videos;
        notifyDataSetChanged();
    }

    public void removeAllMovies() {
        video_list.clear();
        notifyDataSetChanged();
    }


    /**
     * ViewHolder which set data to views in one itemView
     */
    class ThumbnailsViewHolder extends RecyclerView.ViewHolder {
        private final YouTubeThumbnailView thumbnailView;
        private final TextView title;
        private final IThumbnailListener listener;

        private final ShimmerDrawable shimmerDrawable;

        public ThumbnailsViewHolder(@NonNull View itemView, IThumbnailListener listener) {
            super(itemView);
            this.thumbnailView = itemView.findViewById(R.id.thumbnail_item_video);
            this.title = itemView.findViewById(R.id.text_item_video_thumbnail_title);

            this.listener = listener;

            // region Create image placeholder animation using shimmer
            Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                    .setBaseColor(ContextCompat.getColor(itemView.getContext(), R.color.gray))
                    .setBaseAlpha(1)
                    .setHighlightColor(ContextCompat.getColor(itemView.getContext(), R.color.lightGray))
                    .setHighlightAlpha(1)
                    .setDropoff(50)
                    .build();
            this.shimmerDrawable = new ShimmerDrawable();
            this.shimmerDrawable.setShimmer(shimmer);
            // endregion
        }

        public void bind(VideoData videoData) {
            // Initialize YoutubeThumbnailView
            thumbnailView.initialize(BuildConfig.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    // show shimmer drawable effect when thumbnail is not ready
                    youTubeThumbnailView.setImageDrawable(shimmerDrawable);
                    // set video thumbnail
                    youTubeThumbnailLoader.setVideo(videoData.getVideoId());
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                            // must release after loaded
                            youTubeThumbnailLoader.release();
                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                            Log.d(LOG_TAG, String.format("YoutubeThumbnailView Loading Error! Reason: %s",  errorReason.name()));
                        }
                    });
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    Log.d(LOG_TAG, String.format("YoutubeThumbnailView Initialize Failed! Reason: %s",  youTubeInitializationResult.name()));
                }
            });

            // set video title
            title.setText(videoData.getName());

            // set item onClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // callback onVideoClick method and pass videoData
                    listener.onVideoClick(videoData);
                }
            });

        }

    }


}
