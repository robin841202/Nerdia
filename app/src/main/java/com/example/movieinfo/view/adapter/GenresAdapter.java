package com.example.movieinfo.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieinfo.R;
import com.example.movieinfo.model.GenreData;

import java.util.ArrayList;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.GenresViewHolder> {
    private final String LOG_TAG = "GenresAdapter";

    private ArrayList<GenreData> genre_list;
    private final IGenreListener listener;

    public interface IGenreListener {
        /**
         * GenreData item onClick Event
         */
        void onGenreClick(GenreData genre);
    }


    public GenresAdapter(IGenreListener listener) {
        this.genre_list = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public GenresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_genre, parent, false);
        return new GenresViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GenresViewHolder holder, int position) {
        holder.bind(genre_list.get(position));
    }

    @Override
    public int getItemCount() {
        return genre_list.size();
    }


    public void setGenres(ArrayList<GenreData> genres) {
        genre_list = genres;
        notifyDataSetChanged();
    }

    public void removeAll() {
        genre_list.clear();
        notifyDataSetChanged();
    }


    /**
     * ViewHolder which set data to views in one itemView
     */
    class GenresViewHolder extends RecyclerView.ViewHolder {
        private final TextView genreName;
        private final IGenreListener listener;

        public GenresViewHolder(@NonNull View itemView, IGenreListener listener) {
            super(itemView);
            this.genreName = itemView.findViewById(R.id.text_genre);
            this.listener = listener;
        }

        public void bind(GenreData genreData) {

            // set genre name
            genreName.setText(genreData.getName());

            // set item onClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // callback onGenreClick method and pass genreData
                    listener.onGenreClick(genreData);
                }
            });

        }

    }


}
