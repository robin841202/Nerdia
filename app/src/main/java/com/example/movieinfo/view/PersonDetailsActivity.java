package com.example.movieinfo.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.person.PersonDetailData;
import com.example.movieinfo.view.adapter.CustomPagerAdapter;
import com.example.movieinfo.view.adapter.SlideShowAdapter;
import com.example.movieinfo.view.tab.CastTab;
import com.example.movieinfo.view.tab.MovieDetails_AboutTab;
import com.example.movieinfo.view.tab.PersonDetails_AboutTab;
import com.example.movieinfo.view.tab.PersonDetails_MovieTab;
import com.example.movieinfo.view.tab.PersonDetails_TvShowTab;
import com.example.movieinfo.view.tab.SimilarTab;
import com.example.movieinfo.view.tab.TvShowDetails_AboutTab;
import com.example.movieinfo.viewmodel.PersonDetailViewModel;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.common.base.Strings;

import java.util.Locale;


public class PersonDetailsActivity extends AppCompatActivity {

    private final String LOG_TAG = "PersonDetailsActivity";
    private Context context;

    // Define subRequest type
    private final String SUB_REQUEST_TYPE = "images,movie_credits,tv_credits";

    // Define image languages
    private final String[] imageLanguagesCodeArray = {Locale.TRADITIONAL_CHINESE.toLanguageTag(), Locale.ENGLISH.getLanguage(), "null"};
    private final String IMAGE_LANGUAGES = TextUtils.join(",", imageLanguagesCodeArray);

    private ImageView profile;
    private TextView name;
    private TextView knownForDepartment;

    private PersonDetailViewModel personDetailViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

        context = this;

        // Initialize Views
        profile = findViewById(R.id.img_profile);
        name = findViewById(R.id.text_name);
        knownForDepartment = findViewById(R.id.text_known_for_department);
        ViewPager2 viewPager = findViewById(R.id.viewpager_details);
        TabLayout tabLayoutDetails = findViewById(R.id.tabLayout_details);

        // Initialize pagerAdapter
        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), getLifecycle());

        createPersonTabContents(customPagerAdapter, viewPager, tabLayoutDetails);

        // Get person id from intent
        Intent intent = getIntent();
        long personId = intent.getLongExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_PERSON_ID_KEY, 0);

        //  if data exists, get detail and populate in Views
        if (personId > 0) {
            // Initialize viewModel, data only survive this activity lifecycle
            personDetailViewModel = new ViewModelProvider(this).get(PersonDetailViewModel.class);
            personDetailViewModel.init();

            // Set personDetail observer
            personDetailViewModel.getPersonDetailLiveData().observe(this, getPersonDetailObserver());

            getPersonDetail(personId, SUB_REQUEST_TYPE, IMAGE_LANGUAGES);
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

    // region Get Person Detail (MVVM using LiveData)

    /**
     * Get Person Detail By Person Id
     *
     * @param id             person id
     * @param subRequestType Can do subRequest in the same time  ex: images
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     */
    public void getPersonDetail(long id, String subRequestType, String imageLanguages) {
        personDetailViewModel.getPersonDetail(id, subRequestType, imageLanguages);
    }

    /**
     * Observe when PersonDetail LiveData changed
     */
    public Observer<PersonDetailData> getPersonDetailObserver() {
        return personDetailData -> {
            // populate data to UI
            populateDetails(personDetailData);
        };
    }

    // endregion

    // region Populate Person Detail

    /**
     * Populate Details Data in Views (Person)
     *
     * @param personDetail person detail data
     */
    private void populateDetails(PersonDetailData personDetail) {

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


        // endregion

        // region Profile Image
        String profilePath = personDetail.getProfilePath();
        if (!Strings.isNullOrEmpty(profilePath)) {
            String imgUrl = StaticParameter.getImageUrl(StaticParameter.ProfileSize.W185, profilePath);

            // set profile image and use circleCrop
            Glide.with(this)
                    .load(imgUrl)
                    .placeholder(shimmerDrawable)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_profile)
                    .centerCrop()
                    .into(profile);

            // set image onClickListener
            profile.setOnClickListener(v -> {
                displayImageFullScreen(profilePath);
            });
        }
        // endregion

        // set title
        name.setText(personDetail.getName() == null ? "" : personDetail.getName());

        // set knownForDepartment
        knownForDepartment.setText(personDetail.getKnownForDepartment() == null ? "" : personDetail.getKnownForDepartment());

        Log.d(LOG_TAG, "person detail: data populate to UI successfully");
    }

    // endregion

    // region Create Tabs & Contents

    /**
     * Create and bind tabs and viewpager together (For Movie)
     */
    private void createPersonTabContents(CustomPagerAdapter pagerAdapter, ViewPager2 viewPager, TabLayout tabLayout) {
        /*
        Use custom CustomPagerAdapter class to manage page views in fragments.
        Each page is represented by its own fragment.
        */
        pagerAdapter.addFragment(new PersonDetails_AboutTab(), getString(R.string.label_about));
        pagerAdapter.addFragment(new PersonDetails_MovieTab(), getString(R.string.label_movies));
        pagerAdapter.addFragment(new PersonDetails_TvShowTab(), getString(R.string.label_tvshows));
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
        intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_IMAGE_PATH_KEY, imgFilePath);
        startActivity(intent);
        // set the custom transition animation
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}