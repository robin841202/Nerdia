package com.example.movieinfo.view.adapter;

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
import com.example.movieinfo.R;
import com.example.movieinfo.model.ImagesResponse.ImageData;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.VideosResponse;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

import java.util.ArrayList;

public class SlideShowAdapter extends RecyclerView.Adapter<SlideShowAdapter.SlideShowViewHolder> {
    private ArrayList<ImageData> image_list;
    private String imageSizeType;
    private final ISlideShowListener listener;

    public interface ISlideShowListener {
        /**
         * Image item onClick Event
         */
        void onImageClick(ImageData image);
    }


    public SlideShowAdapter(ArrayList<ImageData> image_list, ISlideShowListener listener) {
        this.image_list = image_list;
        this.imageSizeType = StaticParameter.BackdropSize.W1280;
        this.listener = listener;
    }


    @NonNull
    @Override
    public SlideShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_slideshow_image, parent, false);
        return new SlideShowViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideShowViewHolder holder, int position) {
        holder.bind(image_list.get(position), imageSizeType);
    }

    @Override
    public int getItemCount() {
        return image_list.size();
    }


    public void setImages(ArrayList<ImageData> images, String imageSizeType) {
        this.image_list = images;
        this.imageSizeType = imageSizeType;
        notifyDataSetChanged();
    }

    public void removeAllMovies() {
        image_list.clear();
        notifyDataSetChanged();
    }


    /**
     * ViewHolder which set data to views in one itemView
     */
    static class SlideShowViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        private final View itemView;

        private final ISlideShowListener listener;
        private final ShimmerDrawable shimmerDrawable;

        public SlideShowViewHolder(@NonNull View itemView, ISlideShowListener listener) {
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

        public void bind(ImageData imageData, String imageSizeType) {
            String imgUrl = StaticParameter.getImageUrl(imageSizeType, imageData.getFilePath());

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
                    // callback onImageClick method and pass imageData
                    listener.onImageClick(imageData);
                }
            });

        }

    }


}
