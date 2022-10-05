package com.example.movieinfo.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    // Define extra data key for passing data to other activities or fragments
    public static final String EXTRA_DATA_IMAGE_PATH_KEY = "EXTRA_DATA_IMAGE_PATH";

    private ImageView backdrop;
    private ImageView poster;
    private TextView title;
    private TextView overview;
    private TextView releaseDate;
    private TextView scoreText;
    private RatingBar ratingBar;
    private TextView voteCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_details);

        context = this;

        // Get Views
        backdrop = findViewById(R.id.img_movie_backdrop);
        poster = findViewById(R.id.img_movie_poster);
        title = findViewById(R.id.text_movie_title);
        overview = findViewById(R.id.text_movie_overview);
        releaseDate = findViewById(R.id.text_movie_release_date);
        scoreText = findViewById(R.id.text_score);
        ratingBar = findViewById(R.id.ratingBar_movie_rating);
        voteCount = findViewById(R.id.text_vote_count);

        // Get mediaType from intent
        String mediaType = getIntent().getStringExtra(HomeFragment.EXTRA_DATA_MEDIA_TYPE_KEY);

        // Populate Data in Views depends on mediaType
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

    // region TvShows

    /**
     * Populate Details Data in Views (TV Show)
     *
     * @param tvShow tvShow data
     */
    private void populateDetails(TvShowData tvShow) {
        String backdropPath = tvShow.getBackdropPath();
        if (backdropPath != null && !backdropPath.isEmpty()) {
            String imgUrl = StaticParameter.getImageUrl(StaticParameter.BackdropSize.W1280, backdropPath);
            // set image backdrop
            Glide.with(this)
                    .load(imgUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_image_not_found)
                    .centerCrop()
                    .into(backdrop);

            // set image onClickListener
            backdrop.setOnClickListener(v -> {
                displayImageFullScreen(backdropPath);
            });

        }

        String posterPath = tvShow.getPosterPath();
        if (posterPath != null && !posterPath.isEmpty()) {
            String imgUrl = StaticParameter.getImageUrl(StaticParameter.PosterSize.W342, posterPath);
            // set image poster
            Glide.with(this)
                    .load(imgUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_image_not_found)
                    .placeholder(R.drawable.ic_arrow_left)
                    .centerCrop()
                    .into(poster);

            // set image onClickListener
            poster.setOnClickListener(v -> {
                displayImageFullScreen(posterPath);
            });
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

        // set vote count
        voteCount.setText(String.valueOf(tvShow.getVoteCount()));
    }

    // endregion

    // region Movies

    /**
     * Populate Details Data in Views (Movie)
     *
     * @param movie movie data
     */
    private void populateDetails(MovieData movie) {
        String backdropPath = movie.getBackdropPath();
        if (backdropPath != null && !backdropPath.isEmpty()) {
            String imgUrl = StaticParameter.getImageUrl(StaticParameter.BackdropSize.W1280, backdropPath);
            // set image backdrop
            Glide.with(this)
                    .load(imgUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_image_not_found)
                    .centerCrop()
                    .into(backdrop);

            // set image onClickListener
            backdrop.setOnClickListener(v -> {
                displayImageFullScreen(backdropPath);
            });

        }

        String posterPath = movie.getPosterPath();
        if (posterPath != null && !posterPath.isEmpty()) {
            String imgUrl = StaticParameter.getImageUrl(StaticParameter.PosterSize.W342, posterPath);
            // set image poster
            Glide.with(this)
                    .load(imgUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_image_not_found)
                    .centerCrop()
                    .into(poster);

            // set image onClickListener
            poster.setOnClickListener(v -> {
                displayImageFullScreen(posterPath);
            });
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

        // set vote count
        voteCount.setText(String.valueOf(movie.getVoteCount()));
    }

    // endregion


    /**
     * Display image in fullscreen on another activity
     *
     * @param imgFilePath image file path, not the full url
     */
    private void displayImageFullScreen(String imgFilePath) {
        Intent intent = new Intent(context, ImageDisplayActivity.class);
        intent.putExtra(EXTRA_DATA_IMAGE_PATH_KEY, imgFilePath);
        startActivity(intent);
        // set the custom transition animation
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}