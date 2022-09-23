package com.example.movieinfo.view;

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

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private ArrayList<MovieData> movie_list;

    public MoviesAdapter() {
        movie_list = new ArrayList<MovieData>();
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MoviesViewHolder(itemView);
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
        private final String image_baseUrl = "https://image.tmdb.org/t/p/w342";

        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.poster = itemView.findViewById(R.id.img_item_movie_poster);
            this.title = itemView.findViewById(R.id.text_item_movie_title);
        }

        public void bind(MovieData movieData) {
            Glide.with(itemView)
                    .load(image_baseUrl + movieData.getPosterPath())
                    .centerCrop()
                    .into(poster);
            title.setText(movieData.getTitle());

        }

    }

}
