package com.robinhsueh.movieinfo.view.fragments.discover;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robinhsueh.movieinfo.R;
import com.robinhsueh.movieinfo.model.GenreData;
import com.robinhsueh.movieinfo.model.StaticParameter;
import com.robinhsueh.movieinfo.view.adapter.GenresAdapter;
import com.robinhsueh.movieinfo.viewmodel.DiscoverViewModel;

import java.util.ArrayList;

public class GenresListFragment extends Fragment implements GenresAdapter.IGenreListener {
    private final String LOG_TAG = "GenresListFragment";

    private DiscoverViewModel viewModel;

    private String mediaType;

    private RecyclerView genre_RcView;
    private GenresAdapter genresAdapter;

    public GenresListFragment() {
        // Required empty public constructor
    }

    public static GenresListFragment newInstance() {
        Bundle args = new Bundle();
        GenresListFragment fragment = new GenresListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize viewModel, data only survive this fragment lifecycle
        viewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);
        viewModel.init();

        // Set observer
        viewModel.getGenresLiveData().observe(this, getGenresObserver());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genres_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        genre_RcView = view.findViewById(R.id.recycler_genres);

        // Initialize Recycler Adapter
        genresAdapter = new GenresAdapter(this);

        // Set Adapter
        genre_RcView.setAdapter(genresAdapter);

        // Set layoutManager
        genre_RcView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));

        // Get homeCategory argument from HomeFragment
        if (getArguments() != null) {
            mediaType = getArguments().getString(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY, StaticParameter.MediaType.MOVIE);
        }

        // Start getting data
        switch (mediaType){
            case StaticParameter.MediaType.MOVIE:
                fetchMovieGenres();
                break;
            case StaticParameter.MediaType.TV:
                fetchTvShowGenres();
                break;
        }

    }

    /**
     * Fetching movie genres
     */
    private void fetchMovieGenres() {
        viewModel.getMovieGenres();
    }

    /**
     * Fetching tvShow genres
     */
    private void fetchTvShowGenres() {
        viewModel.getTvShowGenres();
    }

    /**
     * Observe when GenreData List LiveData changed
     */
    public Observer<ArrayList<GenreData>> getGenresObserver() {
        return genres -> {
            if (genres != null){
                if (genres.size() > 0) {
                    // set data to adapter
                    genresAdapter.setGenres(genres);
                }
                Log.d(LOG_TAG, "genres: data fetched successfully");
            }
        };
    }

    /**
     * Callback when genre item get clicked
     *
     * @param genre genre data
     */
    @Override
    public void onGenreClick(GenreData genre) {
        String includeGenres = String.valueOf(genre.getId());
        Bundle arguments = new Bundle();
        arguments.putString(StaticParameter.ExtraDataKey.EXTRA_DATA_MEDIA_TYPE_KEY, mediaType);
        arguments.putString(StaticParameter.ExtraDataKey.EXTRA_DATA_INCLUDE_GENRES_KEY, includeGenres);
        arguments.putString(StaticParameter.ExtraDataKey.EXTRA_DATA_ACTIONBAR_TITLE_KEY, genre.getName());
        // navigate to genreResultsFragment
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_genresListFragment_to_genreResultFragment, arguments);
    }
}