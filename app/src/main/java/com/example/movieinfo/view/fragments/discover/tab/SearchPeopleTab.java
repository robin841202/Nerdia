package com.example.movieinfo.view.fragments.discover.tab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.person.PersonData;
import com.example.movieinfo.view.PersonDetailsActivity;
import com.example.movieinfo.view.adapter.PeopleAdapter;
import com.example.movieinfo.viewmodel.SearchKeywordViewModel;
import com.example.movieinfo.viewmodel.SearchViewModel;

import java.util.ArrayList;

public class SearchPeopleTab extends Fragment implements PeopleAdapter.IPeopleListener {


    private final String LOG_TAG = "SearchPeopleTab";

    private SearchViewModel searchViewModel;

    private RecyclerView mRcView;
    private SwipeRefreshLayout pullToRefresh;
    private PeopleAdapter peopleAdapter;
    private LinearLayoutManager mLayoutMgr;
    private int currentPage;

    private SearchKeywordViewModel searchKeywordViewModel;
    private String currentKeyword;

    private NavController navController;

    public SearchPeopleTab() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set default page
        currentPage = 1;

        // Initialize viewModel, data only survive this fragment lifecycle
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.init();

        // Set Observer
        searchViewModel.getPeopleLiveData().observe(this, getSearchPeopleObserver());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_people_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get Views
        mRcView = view.findViewById(R.id.recycler_search);
        pullToRefresh = view.findViewById(R.id.swiperefresh_search);

        // Initialize Adapter
        peopleAdapter = new PeopleAdapter(this);

        // Initialize LayoutManager
        mLayoutMgr = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // Set Adapter & LayoutManager to RecyclerView
        mRcView.setAdapter(peopleAdapter);
        mRcView.setLayoutManager(mLayoutMgr);

        // Set SwipeRefreshListener
        pullToRefresh.setOnRefreshListener(() -> {
            clearResults();
            searchPeople(currentKeyword, currentPage);
            Log.d(LOG_TAG, "onRefresh");
            pullToRefresh.setRefreshing(false);
        });

        // Get navBackStackEntry to scope LiveData lifecycle
        navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getCurrentBackStackEntry();

        if (backStackEntry != null) {
            // Initialize Keyword ViewModel
            searchKeywordViewModel = new ViewModelProvider(backStackEntry).get(SearchKeywordViewModel.class);

            // Keep monitoring search keyword changes
            searchKeywordViewModel.getKeyWord().observe(backStackEntry, new Observer<String>() {
                @Override
                public void onChanged(String keyword) {
                    // reset searched results
                    clearResults();

                    currentKeyword = keyword;

                    // begin searching
                    searchPeople(currentKeyword, currentPage);
                }
            });
        }
    }

    /**
     * Searching People
     *
     * @param keyword Searching keyword
     * @param page    result page
     */
    private void searchPeople(String keyword, int page) {
        if (keyword != null && !keyword.isEmpty()) {
            searchViewModel.searchPeople(keyword, page);
        }
    }

    /**
     * Observe when MovieData List LiveData changed
     */
    public Observer<ArrayList<PersonData>> getSearchPeopleObserver() {
        return people -> {

            if (people.size() > 0){
                // append data to adapter
                peopleAdapter.appendPeople(people);

                // attach onScrollListener to RecyclerView
                mRcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        // when scrolling up
                        if(dy > 0){
                            final int visibleThreshold = 5;

                            // get the number of all items in recyclerView
                            int totalItemCount = mLayoutMgr.getItemCount();
                            // get the last visible item's position
                            int lastVisibleItem = mLayoutMgr.findLastCompletelyVisibleItemPosition();

                            if (totalItemCount <= lastVisibleItem + visibleThreshold) {
                                // detach current OnScrollListener
                                mRcView.removeOnScrollListener(this);

                                // append nextPage data to recyclerView
                                currentPage++;
                                searchPeople(currentKeyword, currentPage);
                            }
                        }
                    }
                });
            }

            Log.d(LOG_TAG, "search people: data fetched successfully");
        };
    }

    /**
     * Reset search results
     */
    private void clearResults() {
        currentPage = 1;
        peopleAdapter.removeAll();
    }

    /**
     * Callback when person item get clicked
     *
     * @param person person data
     */
    @Override
    public void onPersonClick(PersonData person) {
        Intent intent = new Intent(getContext(), PersonDetailsActivity.class);
        intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_PERSON_ID_KEY, person.getId());
        startActivity(intent);
        // set the custom transition animation
        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}