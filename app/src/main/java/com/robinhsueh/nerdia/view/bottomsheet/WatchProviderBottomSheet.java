package com.robinhsueh.nerdia.view.bottomsheet;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.google.common.base.Strings;
import com.robinhsueh.nerdia.R;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.WatchProvidersResponse;
import com.robinhsueh.nerdia.viewmodel.MediaDetailViewModel;

import java.util.ArrayList;
import java.util.Locale;


public class WatchProviderBottomSheet extends BottomSheetDialogFragment {
    private final String LOG_TAG = "WatchProviderBottomSheet";

    private final String mediaType;
    private final long mediaId;
    private final String mediaTitle;

    private View nfxGroup;
    private View disneyGroup;
    private View catchplayGroup;
    private View primeVideoGroup;
    private View tmdbGroup;
    private View justwatchGroup;

    private MediaDetailViewModel viewModel;

    public WatchProviderBottomSheet(String mediaType, long mediaId, String mediaTitle) {
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.mediaTitle = mediaTitle;
    }


    @Override
    public void onStart() {
        super.onStart();
        //this forces the sheet to appear at max height even on landscape
        BottomSheetBehavior.from((View) requireView().getParent()).setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the same viewModel that created in parent activity, in order to share the data
        viewModel = new ViewModelProvider(getActivity()).get(MediaDetailViewModel.class);

        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:
                // Set movieWatchProvider observer
                viewModel.getMovieWatchProvidersLiveData().observe(this, getMovieWatchProviderObserver());
                break;
            case StaticParameter.MediaType.TV:
                // Set tvShowWatchProvider observer
                viewModel.getTvShowWatchProvidersLiveData().observe(this, getMovieWatchProviderObserver());
                break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_watch_provider, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        nfxGroup = view.findViewById(R.id.group_nfx);
        disneyGroup = view.findViewById(R.id.group_disney);
        catchplayGroup = view.findViewById(R.id.group_catchplay);
        primeVideoGroup = view.findViewById(R.id.group_prime_video);
        tmdbGroup = view.findViewById(R.id.group_tmdb);
        justwatchGroup = view.findViewById(R.id.group_justwatch);

        // Start getting watchProviders
        switch (mediaType) {
            case StaticParameter.MediaType.MOVIE:
                viewModel.getWatchProviderByMovie(mediaId);
                break;
            case StaticParameter.MediaType.TV:
                viewModel.getWatchProviderByTvShow(mediaId);
                break;
        }

    }


    /**
     * Observe when MovieWatchProvider LiveData changed
     */
    public Observer<WatchProvidersResponse> getMovieWatchProviderObserver() {
        return watchProvidersResponse -> {
            WatchProvidersResponse.WatchProviderGroup watchProviderGroup = watchProvidersResponse.regions.taiwan;
            if (watchProviderGroup != null) {
                ArrayList<WatchProvidersResponse.WatchProviderData> watchProviderList = new ArrayList<>();
                watchProviderList.addAll(watchProviderGroup.getSubscriptions());
                watchProviderList.addAll(watchProviderGroup.getRents());
                watchProviderList.addAll(watchProviderGroup.getBuys());
                boolean isNfxExist = watchProviderList.stream().anyMatch(data -> data.id == StaticParameter.WatchProvidersID.NetflixID);
                boolean isDisneyExist = watchProviderList.stream().anyMatch(data -> data.id == StaticParameter.WatchProvidersID.DisneyPlusID);
                boolean isCatchplayExist = watchProviderList.stream().anyMatch(data -> data.id == StaticParameter.WatchProvidersID.CatchPlayID);
                boolean isPrimeVideoExist = watchProviderList.stream().anyMatch(data -> data.id == StaticParameter.WatchProvidersID.PrimeVideoID);
                boolean isTMDBLinkExist = !Strings.isNullOrEmpty(watchProviderGroup.tmdbLink);

                // Check if Netflix Provide this media
                if (isNfxExist) {
                    nfxGroup.setAlpha(1.0f);

                    // Set Netflix app on click
                    nfxGroup.setOnClickListener(v -> {
                        // open Netflix App
                        openOtherApp(StaticParameter.ThirdPartyAppsPkgName.Netflix);
                    });

                }

                // Check if DisneyPlus Provide this media
                if (isDisneyExist) {
                    disneyGroup.setAlpha(1.0f);

                    // Set DisneyPlus app on click
                    disneyGroup.setOnClickListener(v -> {
                        // open DisneyPlus App
                        openOtherApp(StaticParameter.ThirdPartyAppsPkgName.DisneyPlus);
                    });
                }

                // Check if CatchPlay Provide this media
                if (isCatchplayExist) {
                    catchplayGroup.setAlpha(1.0f);

                    // Set CatchPlay app on click
                    catchplayGroup.setOnClickListener(v -> {
                        // open CatchPlay App
                        openOtherApp(StaticParameter.ThirdPartyAppsPkgName.CatchPlay);
                    });
                }

                // Check if PrimeVideo Provide this media
                if (isPrimeVideoExist) {
                    primeVideoGroup.setAlpha(1.0f);

                    // Set PrimeVideo app on click
                    primeVideoGroup.setOnClickListener(v -> {
                        // open PrimeVideo App
                        openOtherApp(StaticParameter.ThirdPartyAppsPkgName.PrimeVideo);
                    });
                }

                // Check if TMDB link to media exist
                if (isTMDBLinkExist) {
                    tmdbGroup.setAlpha(1.0f);

                    // Set TMDB web page link on click
                    tmdbGroup.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(watchProviderGroup.tmdbLink));
                        /*
                        Use the resolveActivity() method and the Android package manager to find an Activity that can handle your implicit Intent. Make sure that the request resolved successfully.
                        Make sure there is at least one Activity can handle your request.
                        Remember to add <queries><intent><action> in AndroidManifest since Android 11 needs that
                         */
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            Log.d(LOG_TAG, "no Activity on this device can handle this action!");
                        }
                    });

                }

            }

            // region Set JustWatch onClick event navigate to web page
            justwatchGroup.setOnClickListener(v -> {
                String justwatchUrl = "";
                switch (mediaType) {
                    case StaticParameter.MediaType.MOVIE:
                        justwatchUrl = String.format(Locale.TAIWAN, StaticParameter.JustWatchSearchFormatUrl, "movie", mediaTitle);
                        break;
                    case StaticParameter.MediaType.TV:
                        justwatchUrl = String.format(Locale.TAIWAN, StaticParameter.JustWatchSearchFormatUrl, "show", mediaTitle);
                        break;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(justwatchUrl));
                        /*
                        Use the resolveActivity() method and the Android package manager to find an Activity that can handle your implicit Intent. Make sure that the request resolved successfully.
                        Make sure there is at least one Activity can handle your request.
                        Remember to add <queries><intent><action> in AndroidManifest since Android 11 needs that
                         */
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Log.d(LOG_TAG, "no Activity on this device can handle this action!");
                }
            });

            // endregion
        };
    }

    /**
     * Open other apps by packageName if installed, navigate to play store if not installed
     *
     * @param appPackageName target app package name
     */
    public void openOtherApp(String appPackageName) {
        // Remember to add app package name in AndroidManifest.xml <queries></queries for Android 11 or higher
        Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(appPackageName);
        if (intent != null) {
            // clear task flag clear the stack on target app
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            String playStoreUrl = String.format(StaticParameter.PlayStoreDetailFormatUrl, appPackageName);
            intent.setData(Uri.parse(playStoreUrl));
        }
        startActivity(intent);
    }


}
