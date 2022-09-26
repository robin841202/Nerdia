package com.example.movieinfo.view;

import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.movieinfo.R;
import com.example.movieinfo.model.movie.MovieData;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private ArrayList<MovieData> movie_list;
    private IMovieListener listener;

    public interface IMovieListener{
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
                .inflate(R.layout.item_movie, parent, false);
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


    public void appendMovies(ArrayList<MovieData> movies){
        movie_list.addAll(movies);
        int startPosition = movie_list.size();
        // refresh partially
        notifyItemRangeInserted(startPosition, movies.size() - 1);
    }

    public void removeAllMovies(){
        movie_list.clear();
        notifyDataSetChanged();
    }


    /**
     * ViewHolder which set data to views in one itemView
     */
    class MoviesViewHolder extends RecyclerView.ViewHolder {
        private ImageView poster;
        private TextView title;
        private View itemView;
        private TextView rating;
        private final String image_baseUrl = "https://image.tmdb.org/t/p/w342";

        private final IMovieListener listener;

        public MoviesViewHolder(@NonNull View itemView, IMovieListener listener) {
            super(itemView);
            this.itemView = itemView;
            this.poster = itemView.findViewById(R.id.img_item_movie_poster);
            this.title = itemView.findViewById(R.id.text_item_movie_title);
            this.rating = itemView.findViewById(R.id.text_item_movie_rating);

            this.listener = listener;
        }

        public void bind(MovieData movieData) {
            // set image poster
            Glide.with(itemView)
                    .load(image_baseUrl + movieData.getPosterPath())
                    .centerCrop()
                    .into(poster);

            // set movie title
            title.setText(movieData.getTitle());

            // set movie rating
            double ratingNum = movieData.getRating();
            if(ratingNum > 0){
                // set rating TextView VISIBLE
                rating.setVisibility(View.VISIBLE);
                // set rating TextView
                rating.setText(String.format("%.1f", ratingNum));
            }else{
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
