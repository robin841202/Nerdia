package com.example.movieinfo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.movieinfo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = "MainActivity";
    private Context context;

    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private Toolbar toolbar;

    private BottomNavigationView bottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Views
        context = this;
        bottomNavView = findViewById(R.id.bottomNavView);
        toolbar = findViewById(R.id.toolbar);

        // Set actionBar
        setSupportActionBar(toolbar);

        navController = Navigation.findNavController(this, R.id.fragment_container);
        // set actionBar using nav_graph
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // set bottomNavigationView using nav_graph
        NavigationUI.setupWithNavController(bottomNavView, navController);

        // show and hide toolbar depends on destination fragments
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()){
                case R.id.homeFragment:
                case R.id.discoverFragment:
                case R.id.watchlistFragment:
                    // Hide Toolbar
                    toolbar.setVisibility(View.GONE);
                    break;
                default:
                    // Show Toolbar
                    toolbar.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}