package com.robinhsueh.movieinfo.view.fragments.discover;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robinhsueh.movieinfo.R;
import com.robinhsueh.movieinfo.model.StaticParameter;

public class DiscoverFragment extends Fragment {

    private final String LOG_TAG = "DiscoverFragment";

    private View searchGroupBtn;
    private View cardMovieGenre;
    private View cardTvShowGenre;

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

        // Initialize Views
        searchGroupBtn = root.findViewById(R.id.group_search_btn);
        cardMovieGenre = root.findViewById(R.id.card_movie_genres);
        cardTvShowGenre = root.findViewById(R.id.card_tv_genres);

        searchGroupBtn.setOnClickListener(v -> {
            showSearchFragment();
        });

        cardMovieGenre.setOnClickListener(v -> {
            showGenresList(StaticParameter.MediaType.MOVIE);
        });

        cardTvShowGenre.setOnClickListener(v -> {
            showGenresList(StaticParameter.MediaType.TV);
        });

        return root;
    }


    /**
     * Navigate to SearchFragment
     */
    private void showSearchFragment() {
        // navigate to SearchFragment
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_discoverFragment_to_searchFragment);
    }


    /**
     * Navigate to GenresListFragment
     *
     * @param mediaType mediaType from StaticParameter.MediaType
     */
    private void showGenresList(String mediaType) {
        Bundle arguments = new Bundle();
        arguments.putString(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY, mediaType);
        // navigate to GenresListFragment
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_discoverFragment_to_genresListFragment, arguments);
    }
}