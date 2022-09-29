package com.example.movieinfo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.movieinfo.R;
import com.example.movieinfo.ui.home.HomeFragment;
import com.example.movieinfo.ui.search.SearchFragment;
import com.example.movieinfo.ui.watchlist.WatchlistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = "MainActivity";
    private Context context;

    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    private BottomNavigationView bottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Views
        context = this;
        bottomNavView = findViewById(R.id.bottomNavView);

        // set actionBar
        setSupportActionBar(findViewById(R.id.toolbar));

        navController = Navigation.findNavController(this, R.id.fragment_container);
        // set actionBar using nav_graph
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // set bottomNavigationView using nav_graph
        NavigationUI.setupWithNavController(bottomNavView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}