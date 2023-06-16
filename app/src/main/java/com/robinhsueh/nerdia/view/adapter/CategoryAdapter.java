package com.robinhsueh.nerdia.view.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.facebook.shimmer.ShimmerFrameLayout;
import com.robinhsueh.nerdia.R;
import com.robinhsueh.nerdia.model.CategoryData;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.model.tvshow.TvShowData;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private ArrayList<CategoryData> category_list;
    private final Fragment context;
    private final CategoryAdapter.ICategoryListener listener;

    /**
     * Interface implements in HomeFragment
     */
    public interface ICategoryListener {
        /**
         * Get Upcoming Movies
         *
         * @param page current media page
         */
        void getUpcomingMovies(int page);

        /**
         * Get NowPlaying Movies
         *
         * @param page current media page
         */
        void getNowPlayingMovies(int page);

        /**
         * Get Trending Movies
         *
         * @param page current media page
         */
        void getTrendingMovies(int page);

        /**
         * Get Trending TvShows
         *
         * @param page current media page
         */
        void getTrendingTvShows(int page);

        /**
         * Get Popular Movies
         *
         * @param page current media page
         */
        void getPopularMovies(int page);

        /**
         * Get Popular TvShows
         *
         * @param page current media page
         */
        void getPopularTvShows(int page);

        /**
         * Get Netflix Movies
         *
         * @param page current media page
         */
        void getNetflixMovies(int page);

        /**
         * Get Netflix TvShows
         *
         * @param page current media page
         */
        void getNetflixTvShows(int page);

        /**
         * Get Disney Moives
         *
         * @param page current media page
         */
        void getDisneyMovies(int page);

        /**
         * Get Disney TvShows
         *
         * @param page current media page
         */
        void getDisneyTvShows(int page);

        /**
         * Get CatchPlay Movies
         *
         * @param page current media page
         */
        void getCatchplayMovies(int page);

        /**
         * Get CatchPlay TvShows
         *
         * @param page current media page
         */
        void getCatchplayTvShows(int page);

        /**
         * Get Prime Movies
         *
         * @param page current media page
         */
        void getPrimeMovies(int page);

        /**
         * Get Prime TvShows
         *
         * @param page current media page
         */
        void getPrimeTvShows(int page);
    }

    public CategoryAdapter(Fragment context, ICategoryListener listener) {
        this.category_list = new ArrayList<>();
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(itemView, context, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(category_list.get(position));
    }


    @Override
    public int getItemCount() {
        return category_list.size();
    }

    /**
     * Set category items
     * @param items category list
     */
    public void setItems(ArrayList<CategoryData> items) {
        category_list = items;
        notifyDataSetChanged();
    }

    /**
     * Get Specific Category Data in list by categoryType
     *
     * @param categoryType category type, defined in StaticParameter.HomeCategory
     * @return Specific category data
     */
    public CategoryData getCategoryByType(int categoryType) {
        return category_list.stream().filter(data -> data.getCategoryType() == categoryType).findFirst().orElse(null);
    }

    /**
     * Remove all category items
     */
    public void removeAllItems() {
        category_list.clear();
        notifyDataSetChanged();
    }


    /**
     * ViewHolder which set data to views in one itemView
     */
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView subtitle;
        private final RecyclerView media_RcView;
        private final ShimmerFrameLayout media_Shimmer;
        private final View itemView;
        private final ICategoryListener listener;
        private View clickableLayout;

        private final Fragment context;

        public CategoryViewHolder(@NonNull View itemView, Fragment context, ICategoryListener listener) {
            super(itemView);
            this.itemView = itemView;

            this.title = itemView.findViewById(R.id.text_item_category_title);
            this.subtitle = itemView.findViewById(R.id.text_item_category_subtitle);
            this.media_RcView = itemView.findViewById(R.id.recycler_media);
            this.media_Shimmer = itemView.findViewById(R.id.shimmer_media);
            this.listener = listener;
            this.clickableLayout = itemView.findViewById(R.id.clickableLayout);

            this.context = context;
        }

        /**
         * Start showing loading shimmer
         */
        private void startShimmering() {
            // show shimmer animation
            media_Shimmer.startShimmer();
            media_Shimmer.setVisibility(View.VISIBLE);
        }

        /**
         * Hide loading shimmer
         */
        public void stopShimmering() {
            // hide shimmer animation
            media_Shimmer.stopShimmer();
            media_Shimmer.setVisibility(View.GONE);
        }

        /**
         * Add movie items to media adapter
         * @param movies movie list
         */
        public void appendMoviesToMediaRcView(ArrayList<MovieData> movies) {
            MoviesAdapter mediaAdapter = (MoviesAdapter) media_RcView.getAdapter();
            if (mediaAdapter != null) {
                mediaAdapter.appendMovies(movies);
            }
        }

        /**
         * Add tvShow items to media adapter
         * @param tvShows tvShow list
         */
        public void appendTvShowsToMediaRcView(ArrayList<TvShowData> tvShows) {
            TvShowsAdapter mediaAdapter = (TvShowsAdapter) media_RcView.getAdapter();
            if (mediaAdapter != null) {
                mediaAdapter.appendTvShows(tvShows);
            }
        }

        /**
         * Update media RecyclerView onScroll event
         *
         * @param targetCategoryData target category
         */
        public void updateMediaRcViewScrollEvent(CategoryData targetCategoryData) {

            switch (targetCategoryData.getCategoryType()) {
                case StaticParameter.HomeCategory.UPCOMING_MOVIES:

                    //region attach onScrollListener to RecyclerView
                    media_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            LinearLayoutManager mLayoutMgr = (LinearLayoutManager) media_RcView.getLayoutManager();

                            // when scrolling right
                            if (dx > 0) {
                                // get the number of all items in recyclerView
                                int totalItemCount = mLayoutMgr.getItemCount();
                                // get the number of current items attached to recyclerView
                                int visibleItemCount = mLayoutMgr.getChildCount();
                                // get the first visible item's position
                                int firstVisibleItem = mLayoutMgr.findFirstVisibleItemPosition();

                                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                                    // detach current OnScrollListener
                                    media_RcView.removeOnScrollListener(this);

                                    // get nextPage data then append to recyclerView
                                    int nextPage = targetCategoryData.getCurrentMediaPage() + 1;
                                    targetCategoryData.setCurrentMediaPage(nextPage);
                                    listener.getUpcomingMovies(nextPage);
                                }
                            }
                        }
                    });
                    // endregion

                    break;
                case StaticParameter.HomeCategory.NOWPLAYING_MOVIES:

                    // region attach onScrollListener to RecyclerView
                    media_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            LinearLayoutManager mLayoutMgr = (LinearLayoutManager) media_RcView.getLayoutManager();

                            // when scrolling right
                            if (dx > 0) {
                                // get the number of all items in recyclerView
                                int totalItemCount = mLayoutMgr.getItemCount();
                                // get the number of current items attached to recyclerView
                                int visibleItemCount = mLayoutMgr.getChildCount();
                                // get the first visible item's position
                                int firstVisibleItem = mLayoutMgr.findFirstVisibleItemPosition();

                                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                                    // detach current OnScrollListener
                                    media_RcView.removeOnScrollListener(this);

                                    // get nextPage data then append to recyclerView
                                    int nextPage = targetCategoryData.getCurrentMediaPage() + 1;
                                    targetCategoryData.setCurrentMediaPage(nextPage);
                                    listener.getNowPlayingMovies(nextPage);
                                }
                            }
                        }
                    });
                    // endregion

                    break;
                case StaticParameter.HomeCategory.TRENDING_MOVIES:

                    // region attach onScrollListener to RecyclerView
                    media_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            LinearLayoutManager mLayoutMgr = (LinearLayoutManager) media_RcView.getLayoutManager();

                            // when scrolling right
                            if (dx > 0) {
                                // get the number of all items in recyclerView
                                int totalItemCount = mLayoutMgr.getItemCount();
                                // get the number of current items attached to recyclerView
                                int visibleItemCount = mLayoutMgr.getChildCount();
                                // get the first visible item's position
                                int firstVisibleItem = mLayoutMgr.findFirstVisibleItemPosition();

                                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                                    // detach current OnScrollListener
                                    media_RcView.removeOnScrollListener(this);

                                    // get nextPage data then append to recyclerView
                                    int nextPage = targetCategoryData.getCurrentMediaPage() + 1;
                                    targetCategoryData.setCurrentMediaPage(nextPage);
                                    listener.getTrendingMovies(nextPage);
                                }
                            }
                        }
                    });
                    // endregion

                    break;
                case StaticParameter.HomeCategory.POPULAR_MOVIES:

                    // region attach onScrollListener to RecyclerView
                    media_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            LinearLayoutManager mLayoutMgr = (LinearLayoutManager) media_RcView.getLayoutManager();

                            // when scrolling right
                            if (dx > 0) {
                                // get the number of all items in recyclerView
                                int totalItemCount = mLayoutMgr.getItemCount();
                                // get the number of current items attached to recyclerView
                                int visibleItemCount = mLayoutMgr.getChildCount();
                                // get the first visible item's position
                                int firstVisibleItem = mLayoutMgr.findFirstVisibleItemPosition();

                                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                                    // detach current OnScrollListener
                                    media_RcView.removeOnScrollListener(this);

                                    // get nextPage data then append to recyclerView
                                    int nextPage = targetCategoryData.getCurrentMediaPage() + 1;
                                    targetCategoryData.setCurrentMediaPage(nextPage);
                                    listener.getPopularMovies(nextPage);
                                }
                            }
                        }
                    });
                    // endregion


                    break;
                case StaticParameter.HomeCategory.POPULAR_TVSHOWS:

                    // region attach onScrollListener to RecyclerView
                    media_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            LinearLayoutManager mLayoutMgr = (LinearLayoutManager) media_RcView.getLayoutManager();

                            // when scrolling right
                            if (dx > 0) {
                                // get the number of all items in recyclerView
                                int totalItemCount = mLayoutMgr.getItemCount();
                                // get the number of current items attached to recyclerView
                                int visibleItemCount = mLayoutMgr.getChildCount();
                                // get the first visible item's position
                                int firstVisibleItem = mLayoutMgr.findFirstVisibleItemPosition();

                                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                                    // detach current OnScrollListener
                                    media_RcView.removeOnScrollListener(this);

                                    // get nextPage data then append to recyclerView
                                    int nextPage = targetCategoryData.getCurrentMediaPage() + 1;
                                    targetCategoryData.setCurrentMediaPage(nextPage);
                                    listener.getPopularTvShows(nextPage);
                                }
                            }
                        }
                    });
                    // endregion

                    break;
                case StaticParameter.HomeCategory.TRENDING_TVSHOWS:

                    // region attach onScrollListener to RecyclerView
                    media_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            LinearLayoutManager mLayoutMgr = (LinearLayoutManager) media_RcView.getLayoutManager();

                            // when scrolling right
                            if (dx > 0) {
                                // get the number of all items in recyclerView
                                int totalItemCount = mLayoutMgr.getItemCount();
                                // get the number of current items attached to recyclerView
                                int visibleItemCount = mLayoutMgr.getChildCount();
                                // get the first visible item's position
                                int firstVisibleItem = mLayoutMgr.findFirstVisibleItemPosition();

                                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                                    // detach current OnScrollListener
                                    media_RcView.removeOnScrollListener(this);

                                    // get nextPage data then append to recyclerView
                                    int nextPage = targetCategoryData.getCurrentMediaPage() + 1;
                                    targetCategoryData.setCurrentMediaPage(nextPage);
                                    listener.getTrendingTvShows(nextPage);
                                }
                            }
                        }
                    });
                    // endregion

                    break;
                case StaticParameter.HomeCategory.NETFLIX_MOVIES:

                    // region attach onScrollListener to RecyclerView
                    media_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            LinearLayoutManager mLayoutMgr = (LinearLayoutManager) media_RcView.getLayoutManager();

                            // when scrolling right
                            if (dx > 0) {
                                // get the number of all items in recyclerView
                                int totalItemCount = mLayoutMgr.getItemCount();
                                // get the number of current items attached to recyclerView
                                int visibleItemCount = mLayoutMgr.getChildCount();
                                // get the first visible item's position
                                int firstVisibleItem = mLayoutMgr.findFirstVisibleItemPosition();

                                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                                    // detach current OnScrollListener
                                    media_RcView.removeOnScrollListener(this);

                                    // get nextPage data then append to recyclerView
                                    int nextPage = targetCategoryData.getCurrentMediaPage() + 1;
                                    targetCategoryData.setCurrentMediaPage(nextPage);
                                    listener.getNetflixMovies(nextPage);
                                }
                            }
                        }
                    });
                    // endregion

                    break;
                case StaticParameter.HomeCategory.NETFLIX_TVSHOWS:

                    // region attach onScrollListener to RecyclerView
                    media_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            LinearLayoutManager mLayoutMgr = (LinearLayoutManager) media_RcView.getLayoutManager();

                            // when scrolling right
                            if (dx > 0) {
                                // get the number of all items in recyclerView
                                int totalItemCount = mLayoutMgr.getItemCount();
                                // get the number of current items attached to recyclerView
                                int visibleItemCount = mLayoutMgr.getChildCount();
                                // get the first visible item's position
                                int firstVisibleItem = mLayoutMgr.findFirstVisibleItemPosition();

                                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                                    // detach current OnScrollListener
                                    media_RcView.removeOnScrollListener(this);

                                    // get nextPage data then append to recyclerView
                                    int nextPage = targetCategoryData.getCurrentMediaPage() + 1;
                                    targetCategoryData.setCurrentMediaPage(nextPage);
                                    listener.getNetflixTvShows(nextPage);
                                }
                            }
                        }
                    });
                    // endregion

                    break;
                case StaticParameter.HomeCategory.DISNEY_MOVIES:

                    // region attach onScrollListener to RecyclerView
                    media_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            LinearLayoutManager mLayoutMgr = (LinearLayoutManager) media_RcView.getLayoutManager();

                            // when scrolling right
                            if (dx > 0) {
                                // get the number of all items in recyclerView
                                int totalItemCount = mLayoutMgr.getItemCount();
                                // get the number of current items attached to recyclerView
                                int visibleItemCount = mLayoutMgr.getChildCount();
                                // get the first visible item's position
                                int firstVisibleItem = mLayoutMgr.findFirstVisibleItemPosition();

                                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                                    // detach current OnScrollListener
                                    media_RcView.removeOnScrollListener(this);

                                    // get nextPage data then append to recyclerView
                                    int nextPage = targetCategoryData.getCurrentMediaPage() + 1;
                                    targetCategoryData.setCurrentMediaPage(nextPage);
                                    listener.getDisneyMovies(nextPage);
                                }
                            }
                        }
                    });
                    // endregion

                    break;
                case StaticParameter.HomeCategory.DISNEY_TVSHOWS:

                    // region attach onScrollListener to RecyclerView
                    media_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            LinearLayoutManager mLayoutMgr = (LinearLayoutManager) media_RcView.getLayoutManager();

                            // when scrolling right
                            if (dx > 0) {
                                // get the number of all items in recyclerView
                                int totalItemCount = mLayoutMgr.getItemCount();
                                // get the number of current items attached to recyclerView
                                int visibleItemCount = mLayoutMgr.getChildCount();
                                // get the first visible item's position
                                int firstVisibleItem = mLayoutMgr.findFirstVisibleItemPosition();

                                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                                    // detach current OnScrollListener
                                    media_RcView.removeOnScrollListener(this);

                                    // get nextPage data then append to recyclerView
                                    int nextPage = targetCategoryData.getCurrentMediaPage() + 1;
                                    targetCategoryData.setCurrentMediaPage(nextPage);
                                    listener.getDisneyTvShows(nextPage);
                                }
                            }
                        }
                    });
                    // endregion

                    break;
                case StaticParameter.HomeCategory.CATCHPLAY_MOVIES:

                    // region attach onScrollListener to RecyclerView
                    media_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            LinearLayoutManager mLayoutMgr = (LinearLayoutManager) media_RcView.getLayoutManager();

                            // when scrolling right
                            if (dx > 0) {
                                // get the number of all items in recyclerView
                                int totalItemCount = mLayoutMgr.getItemCount();
                                // get the number of current items attached to recyclerView
                                int visibleItemCount = mLayoutMgr.getChildCount();
                                // get the first visible item's position
                                int firstVisibleItem = mLayoutMgr.findFirstVisibleItemPosition();

                                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                                    // detach current OnScrollListener
                                    media_RcView.removeOnScrollListener(this);

                                    // get nextPage data then append to recyclerView
                                    int nextPage = targetCategoryData.getCurrentMediaPage() + 1;
                                    targetCategoryData.setCurrentMediaPage(nextPage);
                                    listener.getCatchplayMovies(nextPage);
                                }
                            }
                        }
                    });
                    // endregion

                    break;
                case StaticParameter.HomeCategory.CATCHPLAY_TVSHOWS:

                    // region attach onScrollListener to RecyclerView
                    media_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            LinearLayoutManager mLayoutMgr = (LinearLayoutManager) media_RcView.getLayoutManager();

                            // when scrolling right
                            if (dx > 0) {
                                // get the number of all items in recyclerView
                                int totalItemCount = mLayoutMgr.getItemCount();
                                // get the number of current items attached to recyclerView
                                int visibleItemCount = mLayoutMgr.getChildCount();
                                // get the first visible item's position
                                int firstVisibleItem = mLayoutMgr.findFirstVisibleItemPosition();

                                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                                    // detach current OnScrollListener
                                    media_RcView.removeOnScrollListener(this);

                                    // get nextPage data then append to recyclerView
                                    int nextPage = targetCategoryData.getCurrentMediaPage() + 1;
                                    targetCategoryData.setCurrentMediaPage(nextPage);
                                    listener.getCatchplayTvShows(nextPage);
                                }
                            }
                        }
                    });
                    // endregion

                    break;
                case StaticParameter.HomeCategory.PRIME_MOVIES:

                    // region attach onScrollListener to RecyclerView
                    media_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            LinearLayoutManager mLayoutMgr = (LinearLayoutManager) media_RcView.getLayoutManager();

                            // when scrolling right
                            if (dx > 0) {
                                // get the number of all items in recyclerView
                                int totalItemCount = mLayoutMgr.getItemCount();
                                // get the number of current items attached to recyclerView
                                int visibleItemCount = mLayoutMgr.getChildCount();
                                // get the first visible item's position
                                int firstVisibleItem = mLayoutMgr.findFirstVisibleItemPosition();

                                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                                    // detach current OnScrollListener
                                    media_RcView.removeOnScrollListener(this);

                                    // get nextPage data then append to recyclerView
                                    int nextPage = targetCategoryData.getCurrentMediaPage() + 1;
                                    targetCategoryData.setCurrentMediaPage(nextPage);
                                    listener.getPrimeMovies(nextPage);
                                }
                            }
                        }
                    });
                    // endregion

                    break;
                case StaticParameter.HomeCategory.PRIME_TVSHOWS:

                    // region attach onScrollListener to RecyclerView
                    media_RcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            LinearLayoutManager mLayoutMgr = (LinearLayoutManager) media_RcView.getLayoutManager();

                            // when scrolling right
                            if (dx > 0) {
                                // get the number of all items in recyclerView
                                int totalItemCount = mLayoutMgr.getItemCount();
                                // get the number of current items attached to recyclerView
                                int visibleItemCount = mLayoutMgr.getChildCount();
                                // get the first visible item's position
                                int firstVisibleItem = mLayoutMgr.findFirstVisibleItemPosition();

                                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                                    // detach current OnScrollListener
                                    media_RcView.removeOnScrollListener(this);

                                    // get nextPage data then append to recyclerView
                                    int nextPage = targetCategoryData.getCurrentMediaPage() + 1;
                                    targetCategoryData.setCurrentMediaPage(nextPage);
                                    listener.getPrimeTvShows(nextPage);
                                }
                            }
                        }
                    });
                    // endregion

                    break;
            }
        }

        public void bind(CategoryData categoryData) {

            // set category title
            title.setText(categoryData.getTitle());

            // set category subtitle
            subtitle.setText(categoryData.getSubtitle());

            // show shimmer animation
            startShimmering();

            // set adapter for media recyclerView
            switch (categoryData.getCategoryType()) {
                case StaticParameter.HomeCategory.UPCOMING_MOVIES:
                case StaticParameter.HomeCategory.NOWPLAYING_MOVIES:
                case StaticParameter.HomeCategory.TRENDING_MOVIES:
                case StaticParameter.HomeCategory.POPULAR_MOVIES:
                case StaticParameter.HomeCategory.NETFLIX_MOVIES:
                case StaticParameter.HomeCategory.DISNEY_MOVIES:
                case StaticParameter.HomeCategory.CATCHPLAY_MOVIES:
                case StaticParameter.HomeCategory.PRIME_MOVIES:
                    MoviesAdapter moviesAdapter = new MoviesAdapter((AppCompatActivity) context.getActivity());
                    media_RcView.setAdapter(moviesAdapter);

                    break;
                case StaticParameter.HomeCategory.POPULAR_TVSHOWS:
                case StaticParameter.HomeCategory.TRENDING_TVSHOWS:
                case StaticParameter.HomeCategory.NETFLIX_TVSHOWS:
                case StaticParameter.HomeCategory.DISNEY_TVSHOWS:
                case StaticParameter.HomeCategory.CATCHPLAY_TVSHOWS:
                case StaticParameter.HomeCategory.PRIME_TVSHOWS:
                    TvShowsAdapter tvShowsAdapter = new TvShowsAdapter((AppCompatActivity) context.getActivity());
                    media_RcView.setAdapter(tvShowsAdapter);

                    break;
            }

            // set layout manager for media recyclerView
            media_RcView.setLayoutManager(new LinearLayoutManager(context.getContext(), LinearLayoutManager.HORIZONTAL, false));

            // start getting media data
            switch (categoryData.getCategoryType()) {
                case StaticParameter.HomeCategory.UPCOMING_MOVIES:

                    listener.getUpcomingMovies(categoryData.getCurrentMediaPage());

                    break;
                case StaticParameter.HomeCategory.NOWPLAYING_MOVIES:

                    listener.getNowPlayingMovies(categoryData.getCurrentMediaPage());

                    break;
                case StaticParameter.HomeCategory.TRENDING_MOVIES:

                    listener.getTrendingMovies(categoryData.getCurrentMediaPage());

                    break;
                case StaticParameter.HomeCategory.POPULAR_MOVIES:

                    listener.getPopularMovies(categoryData.getCurrentMediaPage());

                    break;
                case StaticParameter.HomeCategory.POPULAR_TVSHOWS:

                    listener.getPopularTvShows(categoryData.getCurrentMediaPage());

                    break;
                case StaticParameter.HomeCategory.TRENDING_TVSHOWS:

                    listener.getTrendingTvShows(categoryData.getCurrentMediaPage());

                    break;
                case StaticParameter.HomeCategory.NETFLIX_MOVIES:

                    listener.getNetflixMovies(categoryData.getCurrentMediaPage());

                    break;
                case StaticParameter.HomeCategory.NETFLIX_TVSHOWS:

                    listener.getNetflixTvShows(categoryData.getCurrentMediaPage());

                    break;
                case StaticParameter.HomeCategory.DISNEY_MOVIES:

                    listener.getDisneyMovies(categoryData.getCurrentMediaPage());

                    break;
                case StaticParameter.HomeCategory.DISNEY_TVSHOWS:

                    listener.getDisneyTvShows(categoryData.getCurrentMediaPage());

                    break;
                case StaticParameter.HomeCategory.CATCHPLAY_MOVIES:

                    listener.getCatchplayMovies(categoryData.getCurrentMediaPage());

                    break;
                case StaticParameter.HomeCategory.CATCHPLAY_TVSHOWS:

                    listener.getCatchplayTvShows(categoryData.getCurrentMediaPage());

                    break;
                case StaticParameter.HomeCategory.PRIME_MOVIES:

                    listener.getPrimeMovies(categoryData.getCurrentMediaPage());

                    break;
                case StaticParameter.HomeCategory.PRIME_TVSHOWS:

                    listener.getPrimeTvShows(categoryData.getCurrentMediaPage());

                    break;
            }

            // Set ClickableLayout Listener
            clickableLayout.setOnClickListener(v -> {
                navigateToVerticalBrowsePage(categoryData.getCategoryType());
            });


        }


        /**
         * Navigate to VerticalBrowseFragment
         *
         * @param homeCategory homeCategory from StaticParameter.HomeCategory
         */
        private void navigateToVerticalBrowsePage(int homeCategory) {
            Bundle arguments = new Bundle();
            arguments.putInt(StaticParameter.ExtraDataKey.EXTRA_DATA_VERTICAL_BROWSE_KEY, homeCategory);
            // navigate to verticalBrowseFragment
            NavHostFragment.findNavController(context)
                    .navigate(R.id.action_homeFragment_to_verticalBrowseFragment, arguments);
        }
    }


}
