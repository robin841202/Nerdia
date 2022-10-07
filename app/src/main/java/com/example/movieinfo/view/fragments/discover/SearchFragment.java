package com.example.movieinfo.view.fragments.discover;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieinfo.R;
import com.example.movieinfo.view.fragments.discover.tab.SearchMoviesTab;
import com.example.movieinfo.view.fragments.discover.tab.SearchTvShowsTab;
import com.example.movieinfo.view.adapter.CustomPagerAdapter;
import com.example.movieinfo.viewmodel.SearchKeywordViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SearchFragment extends Fragment {
    private final String LOG_TAG = "SearchFragment";

    private SearchKeywordViewModel searchKeywordViewModel;
    private NavController navController;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get navBackStackEntry to scope LiveData lifecycle
        navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getCurrentBackStackEntry();

        // region Menu
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.search_menu, menu);


                // Initialize keyword viewModel LiveData, data only survive after the current destination
                searchKeywordViewModel = new ViewModelProvider(backStackEntry).get(SearchKeywordViewModel.class);

                // Expand searchView by default
                SearchView searchView = (SearchView) menu.findItem(R.id.item_search).getActionView();
                searchView.setIconified(false);

                // Set max width
                searchView.setMaxWidth(Integer.MAX_VALUE);

                // Set hint text
                searchView.setQueryHint(getString(R.string.label_search_movies_tvshows));

                // Set initial search keyword if exists
                String lastKeyword = searchKeywordViewModel.getKeyWord().getValue();
                if (lastKeyword != null && !lastKeyword.isEmpty()) {
                    searchView.setQuery(lastKeyword, false);
                }

                // Set QueryTextListener
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // Do nothing
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String keyword) {
                        // Set keyword LiveData
                        searchKeywordViewModel.setKeyWord(keyword);
                        return false;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                // Do nothing, return false if you want parent activity handle onSupportNavigateUp()
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        // endregion

        // Initialize Views
        ViewPager2 viewPager = view.findViewById(R.id.viewpager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        // Initialize pagerAdapter
        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(this);

        // Create and bind tabs and viewpager together
        createTabContents(customPagerAdapter, viewPager, tabLayout);
    }

    /**
     * Create and bind tabs and viewpager together
     */
    private void createTabContents(CustomPagerAdapter pagerAdapter, ViewPager2 viewPager, TabLayout tabLayout) {
        /*
        Use custom CustomPagerAdapter class to manage page views in fragments.
        Each page is represented by its own fragment.
        */
        pagerAdapter.addFragment(new SearchMoviesTab(), getString(R.string.label_movies));
        pagerAdapter.addFragment(new SearchTvShowsTab(), getString(R.string.label_tvshows));
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

}