package com.example.movieinfo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.tvshow.TvShowDetailData;
import com.example.movieinfo.view.adapter.CustomPagerAdapter;
import com.example.movieinfo.view.tab.MovieDetails_AboutTab;
import com.example.movieinfo.view.tab.TvShowDetails_AboutTab;
import com.example.movieinfo.viewmodel.MovieDetailViewModel;
import com.example.movieinfo.viewmodel.TvShowDetailViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class MediaDetailsActivity extends AppCompatActivity {

    private final String LOG_TAG = "MediaDetailsActivity";
    private Context context;

    // Define extra data key for passing data to other activities or fragments
    public static final String EXTRA_DATA_IMAGE_PATH_KEY = "EXTRA_DATA_IMAGE_PATH";

    private ImageView backdrop;
    private ImageView poster;
    private TextView title;
    private TextView releaseDate;
    private TextView scoreText;
    private RatingBar ratingBar;
    private TextView voteCount;

    private MovieDetailViewModel movieDetailViewModel;
    private TvShowDetailViewModel tvShowDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_details);

        context = this;

        // Initialize Views
        backdrop = findViewById(R.id.img_movie_backdrop);
        poster = findViewById(R.id.img_movie_poster);
        title = findViewById(R.id.text_movie_title);
        releaseDate = findViewById(R.id.text_movie_release_date);
        scoreText = findViewById(R.id.text_score);
        ratingBar = findViewById(R.id.ratingBar_movie_rating);
        voteCount = findViewById(R.id.text_vote_count);
        ViewPager2 viewPager = findViewById(R.id.viewpager_details);
        TabLayout tabLayout = findViewById(R.id.tabLayout_details);

        // Initialize pagerAdapter
        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), getLifecycle());


        // Get mediaType from intent
        Intent intent = getIntent();
        String mediaType = intent.getStringExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY);

        // Get Detail Data depends on mediaType
        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:

                // Create and bind tabs and viewpager together (movie)
                createMovieTabContents(customPagerAdapter, viewPager, tabLayout);

                // get movie id from intent
                long movieId = intent.getLongExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_MOVIE_ID_KEY, 0);
                //  if data exists, get detail and populate in Views
                if (movieId > 0) {
                    // Initialize viewModel, data only survive this activity lifecycle
                    movieDetailViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
                    movieDetailViewModel.init();

                    // Set movieDetail observer
                    movieDetailViewModel.getMovieDetailLiveData().observe(this, getMovieDetailObserver());

                    getMovieDetail(movieId, null);
                }
                break;
            case StaticParameter.MediaType.TV:

                // Create and bind tabs and viewpager together (tvShow)
                createTvShowTabContents(customPagerAdapter, viewPager, tabLayout);

                // get tvShow id from intent
                long tvShowId = intent.getLongExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_TVSHOW_ID_KEY, 0);
                //  if data exists, get detail and populate in Views
                if (tvShowId > 0) {
                    // Initialize viewModel, data only survive this activity lifecycle
                    tvShowDetailViewModel = new ViewModelProvider(this).get(TvShowDetailViewModel.class);
                    tvShowDetailViewModel.init();

                    // Set tvShowDetail observer
                    tvShowDetailViewModel.getTvShowDetailLiveData().observe(this, getTvShowDetailObserver());

                    getTvShowDetail(tvShowId, null);
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


    // region Get Movie Detail (Deprecated)
    /*

     */
/**
 * Get Movie Detail By Movie Id
 * @param id movie id
 *//*

    public void getMovieDetail(long id) {
        try {
            Method onMovieDetailFetched = getClass().getMethod("onMovieDetailFetched", MovieDetailData.class);
            Method onFetchDataError = getClass().getMethod("onFetchDataError");
            movieRepository.getMovieDetail(id,null,this, onMovieDetailFetched, onFetchDataError);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    */
/**
 * Callback when get movie detail successfully
 *
 * @param movieDetail movie detail data
 *//*

    public void onMovieDetailFetched(MovieDetailData movieDetail) {

        // populate data in views
        populateDetails(movieDetail);

        Log.d(LOG_TAG, "movie detail: data fetched successfully");
    }

    */
/**
 * Callback when data fetched fail
 *//*

    public void onFetchDataError() {
        Log.d(LOG_TAG, "data fetched fail");
    }
*/


    // endregion

    // region Get Movie Detail (MVVM using LiveData)

    /**
     * Get Movie Detail By Movie Id
     *
     * @param id             movie id
     * @param subRequestType Can do subRequest in the same time  ex: videos
     */
    public void getMovieDetail(long id, String subRequestType) {
        movieDetailViewModel.getMovieDetail(id, subRequestType);
    }

    /**
     * Observe when MovieDetail LiveData changed
     */
    public Observer<MovieDetailData> getMovieDetailObserver() {
        return movieDetailData -> {
            // populate data to UI
            populateDetails(movieDetailData);
        };
    }

    // endregion

    // region Populate Movie Detail

    /**
     * Populate Details Data in Views (Movie)
     *
     * @param movieDetail movie detail data
     */
    private void populateDetails(MovieDetailData movieDetail) {
        String backdropPath = movieDetail.getBackdropPath();
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

        String posterPath = movieDetail.getPosterPath();
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
        title.setText(movieDetail.getTitle() == null ? "" : movieDetail.getTitle());

        // set releaseDate
        releaseDate.setText(movieDetail.getReleaseDate() == null ? "" : movieDetail.getReleaseDate());

        // set score
        double score = movieDetail.getRating();
        String scoreInPercent = String.format("%.0f %%", score * 10);
        scoreText.setText(scoreInPercent);

        // set ratingBar
        float scoreInRatingScale = (float) score / 2f;
        ratingBar.setRating(scoreInRatingScale);

        // set vote count
        voteCount.setText(String.valueOf(movieDetail.getVoteCount()));

        Log.d(LOG_TAG, "movie detail: data populate to UI successfully");
    }

    // endregion


    // region Get TvShow Detail (Deprecated)
    /*

     */
/**
 * Get TvShow Detail By TvShow Id
 *
 * @param id tvShow id
 *//*

    public void getTvShowDetail(long id) {
        try {
            Method onTvShowDetailFetched = getClass().getMethod("onTvShowDetailFetched", TvShowDetailData.class);
            Method onFetchDataError = getClass().getMethod("onFetchDataError");
            tvShowRepository.getTvShowDetail(id, null, this, onTvShowDetailFetched, onFetchDataError);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    */
/**
 * Callback when get tvShow detail successfully
 *
 * @param tvShowDetail tvShow detail data
 *//*

    public void onTvShowDetailFetched(TvShowDetailData tvShowDetail) {

        // populate data in views
        populateDetails(tvShowDetail);

        Log.d(LOG_TAG, "tvShow detail: data fetched successfully");
    }
*/

    // endregion

    // region Get TvShow Detail (MVVM using LiveData)

    /**
     * Get TvShow Detail By TvShow Id
     *
     * @param id tvShow id
     * @param subRequestType Can do subRequest in the same time  ex: videos
     */
    public void getTvShowDetail(long id, String subRequestType) {
        tvShowDetailViewModel.getTvShowDetail(id, subRequestType);
    }

    /**
     * Observe when TvShowDetail LiveData changed
     */
    public Observer<TvShowDetailData> getTvShowDetailObserver() {
        return tvShowDetailData -> {
            // populate data to UI
            populateDetails(tvShowDetailData);
        };
    }

    // endregion

    // region Populate TvShow Detail

    /**
     * Populate Details Data in Views (TV Show)
     *
     * @param tvShowDetail tvShow Detail data
     */
    private void populateDetails(TvShowDetailData tvShowDetail) {
        String backdropPath = tvShowDetail.getBackdropPath();
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

        String posterPath = tvShowDetail.getPosterPath();
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
        title.setText(tvShowDetail.getTitle() == null ? "" : tvShowDetail.getTitle());

        // set releaseDate
        releaseDate.setText(tvShowDetail.getOnAirDate() == null ? "" : tvShowDetail.getOnAirDate());

        // set score
        double score = tvShowDetail.getRating();
        String scoreInPercent = String.format("%.0f %%", score * 10);
        scoreText.setText(scoreInPercent);

        // set ratingBar
        float scoreInRatingScale = (float) score / 2f;
        ratingBar.setRating(scoreInRatingScale);

        // set vote count
        voteCount.setText(String.valueOf(tvShowDetail.getVoteCount()));
    }

    // endregion


    // region Create Tabs & Contents

    /**
     * Create and bind tabs and viewpager together (For Movie)
     */
    private void createMovieTabContents(CustomPagerAdapter pagerAdapter, ViewPager2 viewPager, TabLayout tabLayout) {
        /*
        Use custom CustomPagerAdapter class to manage page views in fragments.
        Each page is represented by its own fragment.
        */
        pagerAdapter.addFragment(new MovieDetails_AboutTab(), getString(R.string.label_about));
        viewPager.setAdapter(pagerAdapter);

        // Generate tabItem by viewpager2 and attach viewpager2 & tabLayout together
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                // Get page title from viewpager2
                String title = pagerAdapter.getPageTitle(position);
                // Set tab title
                tab.setText(title);

                Log.d(LOG_TAG, String.valueOf(position));
            }
        }).attach();
    }


    /**
     * Create and bind tabs and viewpager together (For TvShow)
     */
    private void createTvShowTabContents(CustomPagerAdapter pagerAdapter, ViewPager2 viewPager, TabLayout tabLayout) {
        /*
        Use custom CustomPagerAdapter class to manage page views in fragments.
        Each page is represented by its own fragment.
        */
        pagerAdapter.addFragment(new TvShowDetails_AboutTab(), getString(R.string.label_about));
        viewPager.setAdapter(pagerAdapter);

        // Generate tabItem by viewpager2 and attach viewpager2 & tabLayout together
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                // Get page title from viewpager2
                String title = pagerAdapter.getPageTitle(position);
                // Set tab title
                tab.setText(title);

                Log.d(LOG_TAG, String.valueOf(position));
            }
        }).attach();
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