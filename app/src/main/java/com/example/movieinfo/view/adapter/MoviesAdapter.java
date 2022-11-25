package com.example.movieinfo.view.adapter;

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
import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.view.MediaDetailsActivity;
import com.example.movieinfo.view.bottomsheet.OperateMediaBottomSheet;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private ArrayList<MovieData> movie_list;
    private final AppCompatActivity context;

    public MoviesAdapter(AppCompatActivity context) {
        this.movie_list = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_media, parent, false);
        return new MoviesViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        holder.bind(movie_list.get(position));
    }

    @Override
    public int getItemCount() {
        return movie_list.size();
    }


    public void setMovies(ArrayList<MovieData> movies) {
        movie_list = movies;
        notifyDataSetChanged();
    }

    public void appendMovies(ArrayList<MovieData> movies) {
        int startPosition = movie_list.size();
        movie_list.addAll(movies);
        // refresh partially
        notifyItemRangeInserted(startPosition, movies.size());
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

        private final ShimmerDrawable shimmerDrawable;
        private final AppCompatActivity context;

        public MoviesViewHolder(@NonNull View itemView, AppCompatActivity context) {
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

        public void bind(MovieData movieData) {
            String imgUrl = StaticParameter.getImageUrl(StaticParameter.PosterSize.W342, movieData.getPosterPath());

            // set image poster
            Glide.with(itemView)
                    .load(imgUrl)
                    .placeholder(shimmerDrawable)
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
            itemView.setOnClickListener(v -> {
                // navigate to MediaDetails
                navigateToDetailsPage(movieData.getId());
            });

            // set item onLongClickListener
            itemView.setOnLongClickListener(v -> {
                // Show OperateMedia bottom sheet dialog
                OperateMediaBottomSheet fragment = new OperateMediaBottomSheet(StaticParameter.MediaType.MOVIE, movieData);
                fragment.show(context.getSupportFragmentManager(), fragment.getTag());
                return true;
            });

        }

        /**
         * Navigate to MediaDetails
         *
         * @param id movie Id
         */
        private void navigateToDetailsPage(long id) {
            Intent intent = new Intent(context, MediaDetailsActivity.class);
            intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY, StaticParameter.MediaType.MOVIE);
            intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_MOVIE_ID_KEY, id);
            context.startActivity(intent);
            // set the custom transition animation
            context.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }

    }


}
