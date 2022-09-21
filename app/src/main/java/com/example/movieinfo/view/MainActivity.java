package com.example.movieinfo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

    // Define FragmentType Enum
    public enum FragmentType{
        home, search, watchlist
    }

    private FragmentManager fManager;
    private FragmentTransaction fTransaction;
    private BottomNavigationView bottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        context = this;
        bottomNavView = findViewById(R.id.bottomNavView);

        // initial fragmentManager
        fManager = getSupportFragmentManager();

        // set bottom navigation bar selected listener
        bottomNavView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navItem_home:
                    changeFragmentTo(FragmentType.home);
                    return true;
                case R.id.navItem_search:
                    changeFragmentTo(FragmentType.search);
                    return true;
                case R.id.navItem_watchlist:
                    changeFragmentTo(FragmentType.watchlist);
                    return true;
                default:
                    return false;
            }
        });

        // start from Home fragment
        changeFragmentTo(FragmentType.home);
    }

    private void changeFragmentTo(FragmentType type){
        // setup the fragment transaction
        fTransaction = fManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        switch (type){
            case home:
                fTransaction.replace(R.id.fragment_container,HomeFragment.class, null);
                break;
            case search:
                fTransaction.replace(R.id.fragment_container, SearchFragment.class, null);
                break;
            case watchlist:
                fTransaction.replace(R.id.fragment_container, WatchlistFragment.class, null);
                break;
        }
        // commit transaction
        fTransaction.commit();
    }

}