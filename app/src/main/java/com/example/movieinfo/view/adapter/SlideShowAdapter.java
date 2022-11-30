package com.example.movieinfo.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.movieinfo.R;
import com.example.movieinfo.model.ImagesResponse.ImageData;
import com.example.movieinfo.model.SlideShowItemData;
import com.example.movieinfo.model.VideosResponse.VideoData;
import com.example.movieinfo.model.StaticParameter;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController;

import java.util.ArrayList;

public class SlideShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SlideShowItemData> item_list;
    private final ISlideShowListener listener;
    private Lifecycle lifecycle;

    public interface ISlideShowListener {
        /**
         * SlideShow item onClick Event
         */
        void onSlideShowItemClick(SlideShowItemData itemData);
    }


    public SlideShowAdapter(ISlideShowListener listener, Lifecycle lifecycle) {
        this.item_list = new ArrayList<>();
        this.listener = listener;
        this.lifecycle = lifecycle;
    }

    @Override
    public int getItemViewType(int position) {
        return item_list.get(position).getItemType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case StaticParameter.SlideShowType.VIDEO:
                View videoItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slideshow_video, parent, false);
                YouTubePlayerView ytPlayerView = videoItemView.findViewById(R.id.youtubePlayerView_slideshow);
                lifecycle.addObserver(ytPlayerView);
                holder = new SlideShowVideoViewHolder(videoItemView, listener);
                break;
            case StaticParameter.SlideShowType.IMAGE:
                View imageItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slideshow_image, parent, false);
                holder = new SlideShowImageViewHolder(imageItemView, listener);
                break;
        }
        assert holder != null;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SlideShowItemData itemData = item_list.get(position);
        switch (holder.getItemViewType()) {
            case StaticParameter.SlideShowType.VIDEO:
                SlideShowVideoViewHolder videoHolder = (SlideShowVideoViewHolder) holder;
                videoHolder.bind(itemData);
                break;
            case StaticParameter.SlideShowType.IMAGE:
                SlideShowImageViewHolder imageHolder = (SlideShowImageViewHolder) holder;
                imageHolder.bind(itemData);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return item_list.size();
    }


    /**
     * Set SlideShow Items into Adapter (Images & Videos)
     *
     * @param videos        VideoData list
     * @param images        ImageData list
     * @param imageSizeType SizeType for images defined in StaticParameter.BackdropSize or .PosterSize
     */
    public void setSlideShowItems(ArrayList<VideoData> videos, ArrayList<ImageData> images, String imageSizeType) {
        ArrayList<SlideShowItemData> item_list = new ArrayList<>();
        // map videos and images data into slideshowItemData and combine
        for (VideoData video : videos) {
            item_list.add(new SlideShowItemData(StaticParameter.SlideShowType.VIDEO, video.getVideoId()));
        }
        for (ImageData image : images) {
            item_list.add(new SlideShowItemData(StaticParameter.SlideShowType.IMAGE, image.getFilePath(), imageSizeType));
        }
        this.item_list = item_list;
        notifyDataSetChanged();
    }

    /**
     * Remove all items
     */
    public void removeAll() {
        item_list.clear();
        notifyDataSetChanged();
    }


    /**
     * ViewHolder which set data to views in one itemView (Video)
     */
    static class SlideShowVideoViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private final YouTubePlayerView ytPlayerView;
        private final ImageView volumeControl;
        private final ISlideShowListener listener;
        private String currentVideoId;
        private YouTubePlayer ytPlayer;
        private ShimmerFrameLayout video_Shimmer;
        private boolean isMuted;

        public SlideShowVideoViewHolder(@NonNull View itemView, ISlideShowListener listener) {
            super(itemView);
            this.itemView = itemView;
            this.listener = listener;

            // Initialize Views
            ytPlayerView = itemView.findViewById(R.id.youtubePlayerView_slideshow);
            video_Shimmer = itemView.findViewById(R.id.shimmer_item_slideshow_video);
            volumeControl = itemView.findViewById(R.id.img_volume_control);
            // show shimmer animation
            video_Shimmer.startShimmer();
            video_Shimmer.setVisibility(View.VISIBLE);

            // set player options
            IFramePlayerOptions iFramePlayerOptions = new IFramePlayerOptions.Builder()
                    .controls(0)
                    .build();
            // Initialize YoutubePlayerView
            ytPlayerView.initialize(new AbstractYouTubePlayerListener() {
                @Override
                public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                    // loop the video
                    if (state == PlayerConstants.PlayerState.ENDED) {
                        youTubePlayer.seekTo(0f);
                    }
                }

                @Override
                public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError error) {
                    if(error.equals(PlayerConstants.PlayerError.VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER)){
                        // video can't play in embedded player (age or region restricted)

                    }
                }

                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    // hide shimmer animation
                    video_Shimmer.stopShimmer();
                    video_Shimmer.setVisibility(View.GONE);
                    // using pre-made custom ui
                    DefaultPlayerUiController defaultPlayerUiController = new DefaultPlayerUiController(ytPlayerView, youTubePlayer);
                    // hide specific menu item
                    defaultPlayerUiController.showUi(true).showVideoTitle(false).showSeekBar(false).showDuration(false).showCurrentTime(false).showYouTubeButton(false).showFullscreenButton(false);
                    ytPlayerView.setCustomPlayerUi(defaultPlayerUiController.getRootView());
                    // show the youtube player
                    ytPlayerView.setVisibility(View.VISIBLE);
                }
            }, true, iFramePlayerOptions);
        }

        public void bind(SlideShowItemData itemData) {
            // mute the video as default
            isMuted = true;
            // get the youtube player when it's onReady
            ytPlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
                @Override
                public void onYouTubePlayer(@NonNull YouTubePlayer youTubePlayer) {
                    // mute the video as default
                    youTubePlayer.mute();

                    // set volume control onclick listener
                    volumeControl.setOnClickListener(v -> {
                        if (isMuted) {
                            volumeControl.setImageResource(R.drawable.ic_volume_unmute);
                            youTubePlayer.unMute();
                            isMuted = false;
                        } else {
                            volumeControl.setImageResource(R.drawable.ic_volume_mute);
                            youTubePlayer.mute();
                            isMuted = true;
                        }
                    });

                    // load the video and play it
                    youTubePlayer.loadVideo(itemData.getSource(), 0f);
                }
            });

            // set item onClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // callback onSlideShowItemClick method and pass itemData
                    listener.onSlideShowItemClick(itemData);
                }
            });

        }

    }


    /**
     * ViewHolder which set data to views in one itemView (Image)
     */
    static class SlideShowImageViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private final ImageView imageView;
        private final ISlideShowListener listener;
        private final ShimmerDrawable shimmerDrawable;

        public SlideShowImageViewHolder(@NonNull View itemView, ISlideShowListener listener) {
            super(itemView);
            this.itemView = itemView;
            this.imageView = itemView.findViewById(R.id.img_item_slideshow);
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

        public void bind(SlideShowItemData itemData) {
            String imgUrl = StaticParameter.getTmdbImageUrl(itemData.getImageSizeType(), itemData.getSource());

            // set image poster
            Glide.with(itemView)
                    .load(imgUrl)
                    .placeholder(shimmerDrawable)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_image_not_found)
                    .centerCrop()
                    .into(imageView);

            // set item onClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // callback onSlideShowItemClick method and pass itemData
                    listener.onSlideShowItemClick(itemData);
                }
            });

        }

    }


}
