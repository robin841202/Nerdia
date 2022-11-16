package com.example.movieinfo.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.movieinfo.R;
import com.example.movieinfo.model.ImagesResponse.ImageData;
import com.example.movieinfo.model.StaticParameter;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

import java.util.ArrayList;

public class ProfileImageAdapter extends RecyclerView.Adapter<ProfileImageAdapter.ProfileImageViewHolder> {
    private ArrayList<ImageData> image_list;
    private final IProfileImageListener listener;

    public interface IProfileImageListener {
        /**
         * Profile Image item onClick Event
         */
        void onProfileImageClick(ImageData imageData);
    }


    public ProfileImageAdapter(IProfileImageListener listener) {
        this.image_list = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public ProfileImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_profile_image, parent, false);
        return new ProfileImageViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileImageViewHolder holder, int position) {
        holder.bind(image_list.get(position));
    }

    @Override
    public int getItemCount() {
        return image_list.size();
    }


    public void setImages(ArrayList<ImageData> images) {
        image_list = images;
        notifyDataSetChanged();
    }

    public void removeAll() {
        image_list.clear();
        notifyDataSetChanged();
    }


    /**
     * ViewHolder which set data to views in one itemView
     */
    static class ProfileImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView profile;
        private final View itemView;

        private final IProfileImageListener listener;
        private final ShimmerDrawable shimmerDrawable;

        public ProfileImageViewHolder(@NonNull View itemView, IProfileImageListener listener) {
            super(itemView);
            this.itemView = itemView;
            this.profile = itemView.findViewById(R.id.img_item_profile_image);

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

        public void bind(ImageData imageData) {
            String imgUrl = StaticParameter.getImageUrl(StaticParameter.ProfileSize.W185, imageData.getFilePath());

            // set image
            Glide.with(itemView)
                    .load(imgUrl)
                    .placeholder(shimmerDrawable)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_profile)
                    .centerCrop()
                    .into(profile);

            // set item onClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // callback onProfileImageClick method and pass imageData
                    listener.onProfileImageClick(imageData);
                }
            });

        }

    }


}
