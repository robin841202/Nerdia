package com.robinhsueh.movieinfo.view.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.robinhsueh.movieinfo.R;
import com.robinhsueh.movieinfo.model.StaticParameter;
import com.robinhsueh.movieinfo.model.tvshow.TvShowData;
import com.robinhsueh.movieinfo.view.MediaDetailsActivity;
import com.robinhsueh.movieinfo.view.bottomsheet.OperateMediaBottomSheet;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

import java.util.ArrayList;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.TvShowsViewHolder> {
    private ArrayList<TvShowData> tvShow_list;
    private final AppCompatActivity context;

    public TvShowsAdapter(AppCompatActivity context) {
        this.tvShow_list = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public TvShowsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_media, parent, false);
        return new TvShowsViewHolder(itemView, context);
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

        private final ShimmerDrawable shimmerDrawable;
        private final AppCompatActivity context;

        public TvShowsViewHolder(@NonNull View itemView, AppCompatActivity context) {
            super(itemView);
            this.itemView = itemView;
            this.poster = itemView.findViewById(R.id.img_item_media_poster);
            this.title = itemView.findViewById(R.id.text_item_media_title);
            this.rating = itemView.findViewById(R.id.text_item_media_rating);

            this.context = context;

            // region Create image placeholder animation using shimmer
            Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                    .setBaseColor(ContextCompat.getColor(context, R.color.gray))
                    .setBaseAlpha(1)
                    .setHighlightColor(ContextCompat.getColor(context, R.color.lightGray))
                    .setHighlightAlpha(1)
                    .setDropoff(50)
                    .build();
            this.shimmerDrawable = new ShimmerDrawable();
            this.shimmerDrawable.setShimmer(shimmer);
            // endregion
        }

        public void bind(TvShowData tvShowData) {
            String imgUrl = StaticParameter.getTmdbImageUrl(StaticParameter.PosterSize.W342, tvShowData.getPosterPath());

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
            itemView.setOnClickListener(v -> {
                // navigate to MediaDetails
                navigateToDetailsPage(tvShowData.getId());
            });

            // set item onLongClickListener
            itemView.setOnLongClickListener(v -> {
                // Show OperateMedia bottom sheet dialog
                OperateMediaBottomSheet fragment = new OperateMediaBottomSheet(StaticParameter.MediaType.TV, tvShowData);
                fragment.show(context.getSupportFragmentManager(), fragment.getTag());
                return true;
            });

        }

        /**
         * Navigate to MediaDetails
         * @param id tvShow Id
         */
        private void navigateToDetailsPage(long id){
            Intent intent = new Intent(context, MediaDetailsActivity.class);
            intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY, StaticParameter.MediaType.TV);
            intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_TVSHOW_ID_KEY, id);
            context.startActivity(intent);
            // set the custom transition animation
            context.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }

    }


}
