package com.robinhsueh.nerdia.view.adapter;

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
import com.robinhsueh.nerdia.R;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.VideosResponse.VideoData;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

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
        private final ImageView thumbnail;
        private final View itemView;
        private final TextView title;
        private final IThumbnailListener listener;

        private final ShimmerDrawable shimmerDrawable;

        public ThumbnailsViewHolder(@NonNull View itemView, IThumbnailListener listener) {
            super(itemView);
            this.thumbnail = itemView.findViewById(R.id.thumbnail_item_video);
            this.itemView = itemView;
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
            String imgUrl = StaticParameter.getYtThumbnailUrl(videoData.getVideoId());

            // set image poster
            Glide.with(itemView)
                    .load(imgUrl)
                    .placeholder(shimmerDrawable)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_image_not_found)
                    .centerCrop()
                    .into(thumbnail);

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
