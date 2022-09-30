package com.example.movieinfo.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.tvshow.TvShowData;

import java.util.ArrayList;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.TvShowsViewHolder> {
    private ArrayList<TvShowData> tvShow_list;
    private ITvShowListener listener;

    public interface ITvShowListener {
        /**
         * TvShow item onClick Event
         */
        void onTvShowClick(TvShowData tvShow);
    }


    public TvShowsAdapter(ArrayList<TvShowData> tvShow_list, ITvShowListener listener) {
        this.tvShow_list = tvShow_list;
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


    public void appendTvShows(ArrayList<TvShowData> tvShows) {
        tvShow_list.addAll(tvShows);
        int startPosition = tvShow_list.size();
        // refresh partially
        notifyItemRangeInserted(startPosition, tvShows.size() - 1);
    }

    public void removeAllMovies() {
        tvShow_list.clear();
        notifyDataSetChanged();
    }


    /**
     * ViewHolder which set data to views in one itemView
     */
    class TvShowsViewHolder extends RecyclerView.ViewHolder {
        private ImageView poster;
        private TextView title;
        private View itemView;
        private TextView rating;

        private final ITvShowListener listener;

        public TvShowsViewHolder(@NonNull View itemView, ITvShowListener listener) {
            super(itemView);
            this.itemView = itemView;
            this.poster = itemView.findViewById(R.id.img_item_media_poster);
            this.title = itemView.findViewById(R.id.text_item_media_title);
            this.rating = itemView.findViewById(R.id.text_item_media_rating);

            this.listener = listener;
        }

        public void bind(TvShowData tvShowData) {
            String imgUrl = StaticParameter.getImageUrl(StaticParameter.PosterSize.W342, tvShowData.getPosterPath());
            // set image poster
            Glide.with(itemView)
                    .load(imgUrl)
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
