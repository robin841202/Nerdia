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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private final ArrayList<MovieData> movie_list;
    private final IMovieListener listener;

    public interface IMovieListener {
        /**
         * Movie item onClick Event
         */
        void onMovieClick(MovieData movie);
    }


    public MoviesAdapter(ArrayList<MovieData> movie_list, IMovieListener listener) {
        this.movie_list = movie_list;
        this.listener = listener;
    }


    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_media, parent, false);
        return new MoviesViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        holder.bind(movie_list.get(position));
    }

    @Override
    public int getItemCount() {
        return movie_list.size();
    }


    public void appendMovies(ArrayList<MovieData> movies) {
        movie_list.addAll(movies);
        int startPosition = movie_list.size();
        // refresh partially
        notifyItemRangeInserted(startPosition, movies.size() - 1);
    }

    public void removeAllMovies() {
        movie_list.clear();
        notifyDataSetChanged();
    }


    /**
     * ViewHolder which set data to views in one itemView
     */
    static class MoviesViewHolder extends RecyclerView.ViewHolder {
        private final ImageView poster;
        private final TextView title;
        private final View itemView;
        private final TextView rating;

        private final IMovieListener listener;

        public MoviesViewHolder(@NonNull View itemView, IMovieListener listener) {
            super(itemView);
            this.itemView = itemView;
            this.poster = itemView.findViewById(R.id.img_item_media_poster);
            this.title = itemView.findViewById(R.id.text_item_media_title);
            this.rating = itemView.findViewById(R.id.text_item_media_rating);

            this.listener = listener;
        }

        public void bind(MovieData movieData) {
            String imgUrl = StaticParameter.getImageUrl(StaticParameter.PosterSize.W342, movieData.getPosterPath());
            // set image poster
            Glide.with(itemView)
                    .load(imgUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_image_not_found)
                    .centerCrop()
                    .into(poster);

            // set movie title
            title.setText(movieData.getTitle());

            // set movie rating
            double ratingNum = movieData.getRating();
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
                    // callback onMovieClick method and pass movieData
                    listener.onMovieClick(movieData);
                }
            });

        }

    }


}
