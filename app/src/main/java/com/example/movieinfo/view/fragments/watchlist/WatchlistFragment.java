package com.example.movieinfo.view.fragments.watchlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieinfo.R;
import com.example.movieinfo.view.adapter.CustomPagerAdapter;
import com.example.movieinfo.view.fragments.watchlist.tab.HistoryTab;
import com.example.movieinfo.view.fragments.watchlist.tab.WatchlistTab;
import com.example.movieinfo.view.tab.PersonDetails_AboutTab;
import com.example.movieinfo.view.tab.PersonDetails_MovieTab;
import com.example.movieinfo.view.tab.PersonDetails_TvShowTab;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class WatchlistFragment extends Fragment {

    private final String LOG_TAG = "WatchlistFragment";

    public WatchlistFragment() {
        // Required empty public constructor
    }

    public static WatchlistFragment newInstance() {
        WatchlistFragment fragment = new WatchlistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watch_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize View
        ViewPager2 viewPager = view.findViewById(R.id.viewpager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        // Initialize pagerAdapter
        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(getChildFragmentManager(), getLifecycle());

        createTabContents(customPagerAdapter, viewPager, tabLayout);
    }




    // region Create Tabs & Contents

    /**
     * Create and bind tabs and viewpager together (For Movie)
     */
    private void createTabContents(CustomPagerAdapter pagerAdapter, ViewPager2 viewPager, TabLayout tabLayout) {
        /*
        Use custom CustomPagerAdapter class to manage page views in fragments.
        Each page is represented by its own fragment.
        */
        pagerAdapter.addFragment(new WatchlistTab(), getString(R.string.label_watchlist));
        pagerAdapter.addFragment(new HistoryTab(), getString(R.string.label_history));
        viewPager.setAdapter(pagerAdapter);
        // Disable swiping viewpager
        viewPager.setUserInputEnabled(false);

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
}