package com.example.movieinfo.model;

/**
 * Movie Data Model
 */
public class MovieData {

    private long id;
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private double rating;
    private String releaseDate;

    public MovieData(long id, String title, String overview, String posterPath, String backdropPath, double rating, String releaseDate){
        this.id = id;
        this.title = title;
        this.overview = overview;


        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    /**
     * Get Movie Id
     * @return Movie Id
     */
    public long getId() {
        return id;
    }

    /**
     * Get Movie Title
     * @return Movie Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get Movie Overview
     * @return Movie Overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * Get Movie Poster Url
     * @return Movie Poster Url
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * Get Movie Backdrop Url
     * @return Movie Backdrop Url
     */
    public String getBackdropPath() {
        return backdropPath;
    }

    /**
     * Get Movie Rating
     * @return Movie Rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Set Movie Rating
     * @param rating Movie Rating
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Get Movie Release Date
     * @return Release Date
     */
    public String getReleaseDate() {
        return releaseDate;
    }
}
