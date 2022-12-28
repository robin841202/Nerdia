package com.example.movieinfo.view.adapter;

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
import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.tvshow.SeasonData;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Locale;

public class SeasonsAdapter extends RecyclerView.Adapter<SeasonsAdapter.SeasonViewHolder> {
    private final String LOG_TAG = "SeasonsAdapter";

    private ArrayList<SeasonData> season_list;
    private final AppCompatActivity context;

    public SeasonsAdapter(AppCompatActivity context) {
        this.season_list = new ArrayList<>();
        this.context = context;
    }


    @NonNull
    @Override
    public SeasonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_season, parent, false);
        return new SeasonViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonViewHolder holder, int position) {
        holder.bind(season_list.get(position));
    }

    @Override
    public int getItemCount() {
        return season_list.size();
    }

    public void appendItems(ArrayList<SeasonData> seasons) {
        int startPosition = season_list.size();
        season_list.addAll(seasons);
        // refresh partially
        notifyItemRangeInserted(startPosition, seasons.size());
    }

    public void setItems(ArrayList<SeasonData> seasons) {
        season_list = seasons;
        notifyDataSetChanged();
    }

    public void removeAll() {
        season_list.clear();
        notifyDataSetChanged();
    }


    /**
     * ViewHolder which set data to views in one itemView
     */
    class SeasonViewHolder extends RecyclerView.ViewHolder {
        private final ImageView poster;
        private final TextView title;
        private final TextView airDate;
        private final TextView episodes;
        private final AppCompatActivity context;
        private final ShimmerDrawable shimmerDrawable;

        public SeasonViewHolder(@NonNull View itemView, AppCompatActivity context) {
            super(itemView);
            this.poster = itemView.findViewById(R.id.img_poster);
            this.title = itemView.findViewById(R.id.text_main_name);
            this.airDate = itemView.findViewById(R.id.text_air_date);
            this.episodes =  itemView.findViewById(R.id.text_episodes);

            this.context = context;

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

        public void bind(SeasonData seasonData) {
            if (!Strings.isNullOrEmpty(seasonData.getPosterPath())) {
                String imgUrl = StaticParameter.getTmdbImageUrl(StaticParameter.PosterSize.W342, seasonData.getPosterPath());
                // set profile image
                Glide.with(itemView)
                        .load(imgUrl)
                        .placeholder(shimmerDrawable)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_image_not_found)
                        .centerCrop()
                        .into(poster);
            }

            // set name
            title.setText(seasonData.getTitle());

            // set air date
            airDate.setText(seasonData.getOnAirDate());

            // set episodes
            episodes.setText(String.format(Locale.TAIWAN, "%d集", seasonData.getEpisodes()));

            // set item onClickListener
            itemView.setOnClickListener(v -> {

            });

        }

    }


}
