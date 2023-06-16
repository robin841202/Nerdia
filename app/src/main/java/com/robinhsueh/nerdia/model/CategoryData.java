package com.robinhsueh.nerdia.model;


import com.robinhsueh.nerdia.model.movie.MovieData;
import com.robinhsueh.nerdia.model.tvshow.TvShowData;

import java.util.ArrayList;

/**
 * CategoryData Data Model
 */
public class CategoryData {


    private String title;

    private String subtitle;

    /**
     * Page for media recyclerView
     */
    private int currentMediaPage;

    /**
     * Defined in StaticParameter.HomeCategory
     */
    private int categoryType;

    public CategoryData(String title, String subtitle, int categoryType) {
        this.title = title;
        this.subtitle = subtitle;
        this.categoryType = categoryType;
        this.currentMediaPage = 1;
    }

    public CategoryData(String title, String subtitle, int categoryType, ArrayList<MovieData> movieList, ArrayList<TvShowData> tvShowList) {
        this.title = title;
        this.subtitle = subtitle;
        this.categoryType = categoryType;
        this.currentMediaPage = 1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public int getCurrentMediaPage() {
        return currentMediaPage;
    }

    public void setCurrentMediaPage(int currentMediaPage) {
        this.currentMediaPage = currentMediaPage;
    }
}
