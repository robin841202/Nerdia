package com.example.movieinfo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.movieinfo.R;
import com.example.movieinfo.model.ImagesResponse;
import com.example.movieinfo.model.SlideShowItemData;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.VideosResponse;
import com.example.movieinfo.model.database.entity.MovieWatchlistEntity;
import com.example.movieinfo.model.database.entity.TvShowWatchlistEntity;
import com.example.movieinfo.model.movie.MovieDetailData;
import com.example.movieinfo.model.tvshow.TvShowDetailData;
import com.example.movieinfo.view.adapter.CustomPagerAdapter;
import com.example.movieinfo.view.adapter.SlideShowAdapter;
import com.example.movieinfo.view.bottomsheet.RatingBottomSheet;
import com.example.movieinfo.view.tab.CastTab;
import com.example.movieinfo.view.tab.MovieDetails_AboutTab;
import com.example.movieinfo.view.tab.SimilarTab;
import com.example.movieinfo.view.tab.TvShowDetails_AboutTab;
import com.example.movieinfo.viewmodel.MediaDetailViewModel;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class MediaDetailsActivity extends AppCompatActivity implements SlideShowAdapter.ISlideShowListener {

    private final String LOG_TAG = "MediaDetailsActivity";
    private Context context;

    private String mediaType;

    // Define subRequest type
    private final String SUB_REQUEST_TYPE = "videos,images,credits,external_ids";

    // Define video languages
    private final String[] videoLanguagesCodeArray = {Locale.TRADITIONAL_CHINESE.toLanguageTag(), Locale.ENGLISH.getLanguage()};
    private final String VIDEO_LANGUAGES = TextUtils.join(",", videoLanguagesCodeArray);

    // Define image languages
    private final String[] imageLanguagesCodeArray = {Locale.TRADITIONAL_CHINESE.toLanguageTag(), Locale.ENGLISH.getLanguage(), "null"};
    private final String IMAGE_LANGUAGES = TextUtils.join(",", imageLanguagesCodeArray);

    private SlideShowAdapter slideshowAdapter;

    private ImageView poster;
    private TextView title;
    private TextView releaseDate;
    private TextView scoreText;
    private RatingBar ratingBar;
    private TextView voteCount;
    private ViewGroup ratingGroup;
    private ToggleButton watchlistToggleBtn;

    private MediaDetailViewModel mediaDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_details);

        context = this;

        // Initialize Views
        poster = findViewById(R.id.img_movie_poster);
        title = findViewById(R.id.text_movie_title);
        releaseDate = findViewById(R.id.text_movie_release_date);
        scoreText = findViewById(R.id.text_score);
        ratingBar = findViewById(R.id.ratingBar_movie_rating);
        voteCount = findViewById(R.id.text_vote_count);
        ratingGroup = findViewById(R.id.group_vote);
        ViewPager2 viewPager = findViewById(R.id.viewpager_details);
        TabLayout tabLayoutDetails = findViewById(R.id.tabLayout_details);
        ViewPager2 slideshowViewPager2 = findViewById(R.id.viewpager_slideshow);
        TabLayout tabLayoutSlideshow = findViewById(R.id.tabLayout_slideshow);
        watchlistToggleBtn = findViewById(R.id.toggleBtn_watchlist);

        // Initialize RecyclerView Adapter
        slideshowAdapter = new SlideShowAdapter(this, getLifecycle());

        // Initialize pagerAdapter
        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), getLifecycle());

        // Set slideshow Adapter
        initSlideshow(slideshowAdapter, slideshowViewPager2, tabLayoutSlideshow);

        // Initialize viewModel, data only survive this activity lifecycle
        mediaDetailViewModel = new ViewModelProvider(this).get(MediaDetailViewModel.class);
        mediaDetailViewModel.init();

        // Get mediaType from intent
        Intent intent = getIntent();
        mediaType = intent.getStringExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY);

        // Get Detail Data depends on mediaType
        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:

                // get movie id from intent
                long movieId = intent.getLongExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_MOVIE_ID_KEY, 0);

                // Create and bind tabs and viewpager together (movie)
                createMovieTabContents(customPagerAdapter, viewPager, tabLayoutDetails, movieId);

                //  if data exists, get detail and populate in Views
                if (movieId > 0) {

                    // Set movieDetail observer
                    mediaDetailViewModel.getMovieDetailLiveData().observe(this, getMovieDetailObserver());

                    // Check and observe whether movie is already in local database watchlist or not
                    mediaDetailViewModel.checkMovieExistInWatchlist(movieId).observe(this, aBoolean -> {
                        // Set toggle button default status
                        watchlistToggleBtn.setChecked(aBoolean);
                    });

                    // Start fetch data from api
                    getMovieDetail(movieId, SUB_REQUEST_TYPE, VIDEO_LANGUAGES, IMAGE_LANGUAGES);

                }

                break;
            case StaticParameter.MediaType.TV:

                // get tvShow id from intent
                long tvShowId = intent.getLongExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_TVSHOW_ID_KEY, 0);

                // Create and bind tabs and viewpager together (tvShow)
                createTvShowTabContents(customPagerAdapter, viewPager, tabLayoutDetails, tvShowId);

                //  if data exists, get detail and populate in Views
                if (tvShowId > 0) {

                    // Set tvShowDetail observer
                    mediaDetailViewModel.getTvShowDetailLiveData().observe(this, getTvShowDetailObserver());

                    // Check and observe whether movie is already in local database watchlist or not
                    mediaDetailViewModel.checkTvShowExistInWatchlist(tvShowId).observe(this, aBoolean -> {
                        // Set toggle button default status
                        watchlistToggleBtn.setChecked(aBoolean);
                    });

                    // Start fetch data from api
                    getTvShowDetail(tvShowId, SUB_REQUEST_TYPE, VIDEO_LANGUAGES, IMAGE_LANGUAGES);
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


    // region Movie

    // region Get Movie Detail (MVVM using LiveData)

    /**
     * Get Movie Detail By Movie Id
     *
     * @param id             movie id
     * @param subRequestType Can do subRequest in the same time  ex: videos
     * @param videoLanguages Can include multiple languages of video ex:zh-TW,en
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     */
    public void getMovieDetail(long id, String subRequestType, String videoLanguages, String imageLanguages) {
        mediaDetailViewModel.getMovieDetail(id, subRequestType, videoLanguages, imageLanguages);
    }

    /**
     * Observe when MovieDetail LiveData changed
     */
    public Observer<MovieDetailData> getMovieDetailObserver() {
        return movieDetailData -> {
            // populate data to UI
            populateDetails(movieDetailData);

            // region Set toggle button onChange listener
            watchlistToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) { // INSERT
                        insertMovieToWatchlist(movieDetailData);
                    } else { // DELETE
                        deleteMovieFromWatchlist(movieDetailData);
                    }
                }
            });
            // endregion

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

        // region Create image placeholder animation using shimmer

        // Initialize Shimmer Animation
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(ContextCompat.getColor(context, R.color.gray))
                .setBaseAlpha(1)
                .setHighlightColor(ContextCompat.getColor(context, R.color.lightGray))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .build();

        // Initialize Shimmer Drawable - placeholder for image
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);

        // endregion

        // region Backdrop Image

        // set backdrop slideshow
        ArrayList<ImagesResponse.ImageData> allBackdropImages = movieDetail.getImagesResponse().getBackdrops_list();
        ArrayList<ImagesResponse.ImageData> top10BackdropImages = allBackdropImages.size() > 10 ? new ArrayList<>(allBackdropImages.subList(0, 10)) : allBackdropImages;
        ArrayList<VideosResponse.VideoData> topNVideos = new ArrayList<>();
        VideosResponse videosResponse = movieDetail.getVideosResponse();
        if (videosResponse != null) {
            ArrayList<VideosResponse.VideoData> videos = videosResponse.getVideo_list();
            // sort videos first
            videos = videosResponse.sortVideos(videos);
            // get videos only provided by youtube
            videos = videosResponse.getVideosBySourceSite(videos, StaticParameter.VideoSourceSite.YOUTUBE);
            // get videos only belongs trailer
            videos = videosResponse.getVideosByVideoType(videos, StaticParameter.VideoType.TRAILER);
            // get top n videos
            if (videos.size() > 0) {
                topNVideos = new ArrayList<>(videos.subList(0, 1));
            }
        }
        slideshowAdapter.setSlideShowItems(topNVideos, top10BackdropImages, StaticParameter.BackdropSize.W1280);

        // endregion

        // region Poster Image
        String posterPath = movieDetail.getPosterPath();
        if (posterPath != null && !posterPath.isEmpty()) {
            String imgUrl = StaticParameter.getImageUrl(StaticParameter.PosterSize.W342, posterPath);
            // set image poster
            Glide.with(this)
                    .load(imgUrl)
                    .placeholder(shimmerDrawable)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_image_not_found)
                    .centerCrop()
                    .into(poster);

            // set image onClickListener
            poster.setOnClickListener(v -> {
                displayImageFullScreen(posterPath);
            });
        }
        // endregion

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

        // set rating click event
        ratingGroup.setOnClickListener(v -> {
            showRatingBottomSheet();
        });

        Log.d(LOG_TAG, "movie detail: data populate to UI successfully");
    }

    // endregion

    // region Movie Watchlist
    /**
     * Insert movie to watchlist
     * @param movieDetailData
     */
    private void insertMovieToWatchlist(MovieDetailData movieDetailData){
        MovieWatchlistEntity newEntity = new MovieWatchlistEntity(
                movieDetailData.getId(),
                movieDetailData.getTitle(),
                movieDetailData.getPosterPath(),
                movieDetailData.getRating(),
                movieDetailData.getIsAdult(),
                Calendar.getInstance().getTime());
        try {
            String msg = mediaDetailViewModel.insertMovieWatchlist(newEntity).get() != null ? getString(R.string.msg_saved_to_watchlist) : getString(R.string.msg_operation_error);
            Snackbar.make(watchlistToggleBtn, msg, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getColor(R.color.teal_200))
                    .show();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete movie from watchlist
     * @param movieDetailData
     */
    private void deleteMovieFromWatchlist(MovieDetailData movieDetailData){
        try {
            String msg = mediaDetailViewModel.deleteMovieWatchlistById(movieDetailData.getId()).get() != null ? getString(R.string.msg_removed_from_watchlist) : getString(R.string.msg_operation_error);
            Snackbar.make(watchlistToggleBtn, msg, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getColor(R.color.teal_200))
                    .show();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // endregion

    // endregion


    // region TvShow

    // region Get TvShow Detail (MVVM using LiveData)

    /**
     * Get TvShow Detail By TvShow Id
     *
     * @param id             tvShow id
     * @param subRequestType Can do subRequest in the same time  ex: videos
     * @param videoLanguages Can include multiple languages of video ex:zh-TW,en
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     */
    public void getTvShowDetail(long id, String subRequestType, String videoLanguages, String imageLanguages) {
        mediaDetailViewModel.getTvShowDetail(id, subRequestType, videoLanguages, imageLanguages);
    }

    /**
     * Observe when TvShowDetail LiveData changed
     */
    public Observer<TvShowDetailData> getTvShowDetailObserver() {
        return tvShowDetailData -> {
            // populate data to UI
            populateDetails(tvShowDetailData);

            // region Set toggle button onChange listener
            watchlistToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) { // INSERT
                        insertTvShowToWatchlist(tvShowDetailData);
                    } else { // DELETE
                        deleteTvShowFromWatchlist(tvShowDetailData);
                    }
                }
            });
            // endregion
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

        // region Create image placeholder animation using shimmer

        // Initialize Shimmer Animation
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(ContextCompat.getColor(context, R.color.gray))
                .setBaseAlpha(1)
                .setHighlightColor(ContextCompat.getColor(context, R.color.lightGray))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .build();

        // Initialize Shimmer Drawable - placeholder for image
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);

        // endregion

        // region Backdrop Image

        // set backdrop slideshow
        ArrayList<ImagesResponse.ImageData> allBackdropImages = tvShowDetail.getImagesResponse().getBackdrops_list();
        ArrayList<ImagesResponse.ImageData> top10BackdropImages = allBackdropImages.size() > 10 ? new ArrayList<>(allBackdropImages.subList(0, 10)) : allBackdropImages;
        ArrayList<VideosResponse.VideoData> topNVideos = new ArrayList<>();
        VideosResponse videosResponse = tvShowDetail.getVideosResponse();
        if (videosResponse != null) {
            ArrayList<VideosResponse.VideoData> videos = videosResponse.getVideo_list();
            // sort videos first
            videos = videosResponse.sortVideos(videos);
            // get videos only provided by youtube
            videos = videosResponse.getVideosBySourceSite(videos, StaticParameter.VideoSourceSite.YOUTUBE);
            // get videos only belongs trailer
            videos = videosResponse.getVideosByVideoType(videos, StaticParameter.VideoType.TRAILER);
            // get top n videos
            if (videos.size() > 0) {
                topNVideos = new ArrayList<>(videos.subList(0, 1));
            }
        }
        slideshowAdapter.setSlideShowItems(topNVideos, top10BackdropImages, StaticParameter.BackdropSize.W1280);

        // endregion

        // region Poster Image
        String posterPath = tvShowDetail.getPosterPath();
        if (posterPath != null && !posterPath.isEmpty()) {
            String imgUrl = StaticParameter.getImageUrl(StaticParameter.PosterSize.W342, posterPath);
            // set image poster
            Glide.with(this)
                    .load(imgUrl)
                    .placeholder(shimmerDrawable)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_image_not_found)
                    .centerCrop()
                    .into(poster);

            // set image onClickListener
            poster.setOnClickListener(v -> {
                displayImageFullScreen(posterPath);
            });
        }
        // endregion

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

        // set rating click event
        ratingGroup.setOnClickListener(v -> {
            showRatingBottomSheet();
        });
    }

    // endregion

    // region TvShow Watchlist
    /**
     * Insert tvShow to watchlist
     * @param tvShowDetailData
     */
    private void insertTvShowToWatchlist(TvShowDetailData tvShowDetailData){
        TvShowWatchlistEntity newEntity = new TvShowWatchlistEntity(
                tvShowDetailData.getId(),
                tvShowDetailData.getTitle(),
                tvShowDetailData.getPosterPath(),
                tvShowDetailData.getRating(),
                tvShowDetailData.getIsAdult(),
                Calendar.getInstance().getTime());
        try {
            String msg = mediaDetailViewModel.insertTvShowWatchlist(newEntity).get() != null ? getString(R.string.msg_saved_to_watchlist) : getString(R.string.msg_operation_error);
            Snackbar.make(watchlistToggleBtn, msg, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getColor(R.color.teal_200))
                    .show();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete tvShow from watchlist
     * @param tvShowDetailData
     */
    private void deleteTvShowFromWatchlist(TvShowDetailData tvShowDetailData){
        try {
            String msg = mediaDetailViewModel.deleteTvShowWatchlistById(tvShowDetailData.getId()).get() != null ? getString(R.string.msg_removed_from_watchlist) : getString(R.string.msg_operation_error);
            Snackbar.make(watchlistToggleBtn, msg, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getColor(R.color.teal_200))
                    .show();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // endregion

    // endregion



    // region Create Tabs & Contents

    /**
     * Create and bind tabs and viewpager together (For Movie)
     */
    private void createMovieTabContents(CustomPagerAdapter pagerAdapter, ViewPager2 viewPager, TabLayout tabLayout, long movieId) {
        /*
        Use custom CustomPagerAdapter class to manage page views in fragments.
        Each page is represented by its own fragment.
        */
        pagerAdapter.addFragment(new MovieDetails_AboutTab(), getString(R.string.label_about));
        pagerAdapter.addFragment(CastTab.newInstance(StaticParameter.MediaType.MOVIE), getString(R.string.label_cast));
        pagerAdapter.addFragment(SimilarTab.newInstance(StaticParameter.MediaType.MOVIE, movieId), getString(R.string.label_similar));
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
    private void createTvShowTabContents(CustomPagerAdapter pagerAdapter, ViewPager2 viewPager, TabLayout tabLayout, long tvShowId) {
        /*
        Use custom CustomPagerAdapter class to manage page views in fragments.
        Each page is represented by its own fragment.
        */
        pagerAdapter.addFragment(new TvShowDetails_AboutTab(), getString(R.string.label_about));
        pagerAdapter.addFragment(CastTab.newInstance(StaticParameter.MediaType.TV), getString(R.string.label_cast));
        pagerAdapter.addFragment(SimilarTab.newInstance(StaticParameter.MediaType.TV, tvShowId), getString(R.string.label_similar));
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

    // region Backdrop Slideshow

    /**
     * Initialize slideshows
     */
    private void initSlideshow(SlideShowAdapter slideshowAdapter, ViewPager2 viewPager, TabLayout tabLayout) {

        viewPager.setAdapter(slideshowAdapter);

        // Generate indicator by viewpager2 and attach viewpager2 & indicator together
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
        }).attach();
    }

    /**
     * Callback when slideshow item get clicked
     *
     * @param itemData SlideShowItemData
     */
    @Override
    public void onSlideShowItemClick(SlideShowItemData itemData) {
        switch (itemData.getItemType()) {
            case StaticParameter.SlideShowType.VIDEO:
                playVideoInNewActivity(itemData.getSource());
                break;
            case StaticParameter.SlideShowType.IMAGE:
                displayImageFullScreen(itemData.getSource());
                break;
        }
    }

    // endregion

    /**
     * Display image in fullscreen on another activity
     *
     * @param imgFilePath image file path, not the full url
     */
    private void displayImageFullScreen(String imgFilePath) {
        Intent intent = new Intent(context, ImageDisplayActivity.class);
        intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_IMAGE_PATH_KEY, imgFilePath);
        startActivity(intent);
        // set the custom transition animation
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    /**
     * Play youtube video in new activity
     */
    private void playVideoInNewActivity(String videoId) {
        Intent intent = new Intent(context, YoutubePlayerActivity.class);
        intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_VIDEO_ID_KEY, videoId);
        startActivity(intent);
        // set the custom transition animation
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    /**
     * Show Rating Bottom Sheet Modal
     */
    private void showRatingBottomSheet() {
        RatingBottomSheet blankFragment = new RatingBottomSheet(mediaType);
        blankFragment.show(getSupportFragmentManager(), blankFragment.getTag());
    }
}