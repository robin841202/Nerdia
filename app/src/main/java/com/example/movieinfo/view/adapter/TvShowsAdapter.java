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
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.tvshow.TvShowData;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.TvShowsViewHolder> {
    private ArrayList<TvShowData> tvShow_list;
    private final ITvShowListener listener;

    public interface ITvShowListener {
        /**
         * TvShow item onClick Event
         */
        void onTvShowClick(TvShowData tvShow);
    }


    public TvShowsAdapter(ITvShowListener listener) {
        this.tvShow_list = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public TvShowsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_media, parent, false);
        return new TvShowsViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowsViewHolder holder, int position) {
        holder.bind(tvShow_list.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShow_list.size();
    }


    public void setTvShows(ArrayList<TvShowData> tvShows) {
        tvShow_list = tvShows;
        notifyDataSetChanged();
    }

    public void appendTvShows(ArrayList<TvShowData> tvShows) {
        int startPosition = tvShow_list.size();
        tvShow_list.addAll(tvShows);
        // refresh partially
        notifyItemRangeInserted(startPosition, tvShows.size());
    }

    public void removeAllTvShows() {
        tvShow_list.clear();
        notifyDataSetChanged();
    }


    /**
     * ViewHolder which set data to views in one itemView
     */
    static class TvShowsViewHolder extends RecyclerView.ViewHolder {
        private final ImageView poster;
        private final TextView title;
        private final View itemView;
        private final TextView rating;

        private final ITvShowListener listener;
        private final ShimmerDrawable shimmerDrawable;

        public TvShowsViewHolder(@NonNull View itemView, ITvShowListener listener) {
            super(itemView);
            this.itemView = itemView;
            this.poster = itemView.findViewById(R.id.img_item_media_poster);
            this.title = itemView.findViewById(R.id.text_item_media_title);
            this.rating = itemView.findViewById(R.id.text_item_media_rating);

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

        public void bind(TvShowData tvShowData) {
            String imgUrl = StaticParameter.getImageUrl(StaticParameter.PosterSize.W342, tvShowData.getPosterPath());

            // set image poster
            Glide.with(itemView)
                    .load(imgUrl)
                    .placeholder(shimmerDrawable)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_image_not_found)
                    .centerCrop()
                    .into(poster);

            // set title
            title.setText(tvShowData.getTitle());

            // set rating
            double ratingNum = tvShowData.getRating();
            if (ratingNum > 0) {
                // set rating TextView VISIBLE
                rating.setVisibility(View.VISIBLE);
                // set rating TextView
                rating.setText(String.format("%.1f", ratingNum));
            } else {
                // set rating TextView GONE
                rating.setVisibility(View.GONE);
            }

            // set item onClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // callback onTvShowClick method and pass tvShowData
                    listener.onTvShowClick(tvShowData);
                }
            });

        }

    }


}
