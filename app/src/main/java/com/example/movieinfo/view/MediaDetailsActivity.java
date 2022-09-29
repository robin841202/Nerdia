package com.example.movieinfo.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieData;
import com.example.movieinfo.model.tvshow.TvShowData;
import com.example.movieinfo.ui.home.HomeFragment;


public class MediaDetailsActivity extends AppCompatActivity {

    private final String LOG_TAG = "MediaDetailsActivity";
    private Context context;

    private final String image_baseUrl = "https://image.tmdb.org/t/p/";

    private ImageView backdrop;
    private ImageView poster;
    private TextView title;
    private TextView overview;
    private TextView releaseDate;
    private TextView scoreText;
    private RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_details);

        context = this;

        backdrop = findViewById(R.id.img_movie_backdrop);
        poster = findViewById(R.id.img_movie_poster);
        title = findViewById(R.id.text_movie_title);
        overview = findViewById(R.id.text_movie_overview);
        releaseDate = findViewById(R.id.text_movie_release_date);
        scoreText = findViewById(R.id.text_score);
        ratingBar = findViewById(R.id.ratingBar_movie_rating);

        // get mediaType from intent
        String mediaType = getIntent().getStringExtra(HomeFragment.EXTRA_DATA_MEDIA_TYPE_KEY);

        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE: // Populate Movie UI
                // get data object from intent
                MovieData movieData = getIntent().getParcelableExtra(HomeFragment.EXTRA_DATA_MOVIE_KEY);
                //  if data exists, populate data in Views
                if (movieData != null) {
                    populateDetails(movieData);
                }
                break;
            case StaticParameter.MediaType.TV: // Populate TV Show UI
                // get data object from intent
                TvShowData tvShowData = getIntent().getParcelableExtra(HomeFragment.EXTRA_DATA_TVSHOW_KEY);
                //  if data exists, populate data in Views
                if (tvShowData != null) {
                    populateDetails(tvShowData);
                }
                break;
            default:
                // do nothing
                break;
        }
    }

    /**
     * When Back Button Pressed
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // set custom transition animation
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    /**
     * Populate Details Data in Views (TV Show)
     *
     * @param tvShow tvShow data
     */
    private void populateDetails(TvShowData tvShow) {
        String backdropPath = tvShow.getBackdropPath();
        if (backdropPath != null && !backdropPath.isEmpty()) {
            // set image backdrop
            Glide.with(this)
                    .load(image_baseUrl + "w1280" + backdropPath)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(backdrop);


        }

        String posterPath = tvShow.getPosterPath();
        if (posterPath != null && !posterPath.isEmpty()) {
            // set image poster
            Glide.with(this)
                    .load(image_baseUrl + "w342" + posterPath)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.ic_arrow_left)
                    .centerCrop()
                    .into(poster);

        }

        // set title
        title.setText(tvShow.getTitle() == null ? "" : tvShow.getTitle());

        // set overview
        overview.setText(tvShow.getOverview() == null ? "" : tvShow.getOverview());

        // set releaseDate
        releaseDate.setText(tvShow.getOnAirDate() == null ? "" : tvShow.getOnAirDate());

        // set score
        double score = tvShow.getRating();
        String scoreInPercent = String.format("%.0f %%", score * 10);
        scoreText.setText(scoreInPercent);

        // set ratingBar
        float scoreInRatingScale = (float) score / 2f;
        ratingBar.setRating(scoreInRatingScale);
    }

    /**
     * Populate Details Data in Views (Movie)
     *
     * @param movie movie data
     */
    private void populateDetails(MovieData movie) {
        String backdropPath = movie.getBackdropPath();
        if (backdropPath != null && !backdropPath.isEmpty()) {
            // set image backdrop
            Glide.with(this)
                    .load(image_baseUrl + "w1280" + backdropPath)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(backdrop);


        }

        String posterPath = movie.getPosterPath();
        if (posterPath != null && !posterPath.isEmpty()) {
            // set image poster
            Glide.with(this)
                    .load(image_baseUrl + "w342" + posterPath)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(poster);

        }

        // set title
        title.setText(movie.getTitle() == null ? "" : movie.getTitle());

        // set overview
        overview.setText(movie.getOverview() == null ? "" : movie.getOverview());

        // set releaseDate
        releaseDate.setText(movie.getReleaseDate() == null ? "" : movie.getReleaseDate());

        // set score
        double score = movie.getRating();
        String scoreInPercent = String.format("%.0f %%", score * 10);
        scoreText.setText(scoreInPercent);

        // set ratingBar
        float scoreInRatingScale = (float) score / 2f;
        ratingBar.setRating(scoreInRatingScale);
    }

}