package com.example.movieinfo.view.tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieinfo.R;
import com.example.movieinfo.model.ImagesResponse;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.VideosResponse;
import com.example.movieinfo.model.person.PersonDetailData;
import com.example.movieinfo.view.ImageDisplayActivity;
import com.example.movieinfo.view.adapter.ProfileImageAdapter;
import com.example.movieinfo.view.adapter.ThumbnailsAdapter;
import com.example.movieinfo.viewmodel.PersonDetailViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.common.base.Strings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.github.giangpham96.expandabletextview.ExpandableTextView;

public class PersonDetails_AboutTab extends Fragment implements ProfileImageAdapter.IProfileImageListener {

    private final String LOG_TAG = "PersonDetails_AboutTab";

    private Context context;

    private PersonDetailViewModel personDetailViewModel;

    private ExpandableTextView biographyTextView;
    private ViewGroup otherNamesGroup;
    private TextView ageTextView;
    private TextView birthdayTextView;
    private TextView deathdayTextView;
    private TextView placeFromTextView;
    private ViewGroup deathdayGroup;

    private ShimmerFrameLayout prfileImage_Shimmer;
    private RecyclerView profileImage_RcView;
    private ProfileImageAdapter profileImageAdapter;

    public PersonDetails_AboutTab() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

        // Get the same viewModel that created in parent activity, in order to share the data
        personDetailViewModel = new ViewModelProvider(getActivity()).get(PersonDetailViewModel.class);

        // Set personDetail observer
        personDetailViewModel.getPersonDetailLiveData().observe(this, getDataObserver());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_person_details_about_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        biographyTextView = view.findViewById(R.id.expandText_biography);
        otherNamesGroup = view.findViewById(R.id.group_names);
        ageTextView = view.findViewById(R.id.text_age);
        birthdayTextView = view.findViewById(R.id.text_birthday);
        deathdayTextView = view.findViewById(R.id.text_deathday);
        placeFromTextView = view.findViewById(R.id.text_placeFrom);
        deathdayGroup = view.findViewById(R.id.row_deathday);
        profileImage_RcView = view.findViewById(R.id.recycler_profile_images);
        prfileImage_Shimmer = view.findViewById(R.id.shimmer_profile_images);

        // Initialize Recycler Adapter
        profileImageAdapter = new ProfileImageAdapter(this);

        // Set adapter
        profileImage_RcView.setAdapter(profileImageAdapter);

        // Set layoutManager
        profileImage_RcView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        // show shimmer animation
        prfileImage_Shimmer.startShimmer();
        prfileImage_Shimmer.setVisibility(View.VISIBLE);
    }

    /**
     * Observe when PersonDetail LiveData changed
     */
    private Observer<PersonDetailData> getDataObserver() {
        return personDetail -> {
            if (personDetail != null){
                // populate data to UI
                populateUI(personDetail);
            }
        };
    }

    private void populateUI(PersonDetailData personDetail) {
        // hide shimmer animation
        prfileImage_Shimmer.stopShimmer();
        prfileImage_Shimmer.setVisibility(View.GONE);

        // get all information
        String biography = Strings.isNullOrEmpty(personDetail.getBiography()) ? getString(R.string.label_empty) : personDetail.getBiography();
        ArrayList<String> otherNames_List = personDetail.getAlsoKnownAs();
        String birthday = Strings.isNullOrEmpty(personDetail.getBirthday()) ? getString(R.string.label_empty) : personDetail.getBirthday();
        String placeFrom = Strings.isNullOrEmpty(personDetail.getPlaceFrom()) ? getString(R.string.label_empty) : personDetail.getPlaceFrom();

        // set age
        if (!Strings.isNullOrEmpty(personDetail.getBirthday())) {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date birthDate = parser.parse(personDetail.getBirthday());
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                calendar.setTime(birthDate);
                int birthYear = calendar.get(Calendar.YEAR);
                int age = currentYear - birthYear;
                ageTextView.setText(String.valueOf(age));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            ageTextView.setText(getString(R.string.label_empty));
        }

        // set birthday
        birthdayTextView.setText(birthday);

        // set deathday
        if (!Strings.isNullOrEmpty(personDetail.getDeathday())) {
            deathdayTextView.setText(personDetail.getDeathday());
            deathdayGroup.setVisibility(View.VISIBLE);
        } else {
            deathdayGroup.setVisibility(View.GONE);
        }

        // set placeFrom
        placeFromTextView.setText(placeFrom);

        // set biography - using ExpandableTextView library setOriginalText function to show contents, do not use setText
        biographyTextView.setOriginalText(biography);

        // set otherNames
        if (otherNames_List != null) {
            for (int i = 0; i < otherNames_List.size(); i++) {
                // add a TextView dynamically
                addTextViewToGroup(otherNames_List.get(i), otherNamesGroup);
            }
        }


        // set profile images recyclerView
        ImagesResponse imagesResponse = personDetail.getImagesResponse();
        if (imagesResponse != null) {
            ArrayList<ImagesResponse.ImageData> profiles = imagesResponse.getProfiles_list();
            profileImageAdapter.setImages(profiles);
        }

    }

    /**
     * Add a new TextView to Genres Group
     *
     * @param name  TextView text
     * @param group Container that contains multiple genre TextView
     */
    private void addTextViewToGroup(String name, ViewGroup group) {
        TextView genreTextView = new TextView(context);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMarginEnd(8);
        genreTextView.setLayoutParams(params);
        genreTextView.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Small);
        genreTextView.setBackgroundResource(R.drawable.rounded_corner);
        genreTextView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.lightGray));
        genreTextView.setText(name);
        group.addView(genreTextView);
    }


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
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    /**
     * Callback when profile image item get clicked
     *
     * @param imageData image Data
     */
    @Override
    public void onProfileImageClick(ImagesResponse.ImageData imageData) {
        displayImageFullScreen(imageData.getFilePath());
    }



}

