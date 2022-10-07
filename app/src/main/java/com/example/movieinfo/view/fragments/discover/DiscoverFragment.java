package com.example.movieinfo.view.fragments.discover;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieinfo.R;

public class DiscoverFragment extends Fragment {

    private final String LOG_TAG = "DiscoverFragment";

    private View searchGroupBtn;

    public DiscoverFragment() {
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
        View root = inflater.inflate(R.layout.fragment_discover, container, false);

        searchGroupBtn = root.findViewById(R.id.group_search_btn);
        searchGroupBtn.setOnClickListener(v -> {
            showSearchFragment();
        });

        return root;
    }


    /**
     * navigate to SearchFragment
     */
    private void showSearchFragment() {
        // navigate to SearchFragment
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_discoverFragment_to_searchFragment);
    }
}