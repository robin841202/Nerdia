package com.example.movieinfo.model.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Movie Data Model, using @SerializedName to map to json key
 */
public class MovieData implements Parcelable {

    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("vote_average")
    private double rating;

    @SerializedName("release_date")
    private String releaseDate;

    public MovieData(long id, String title, String overview, String posterPath, String backdropPath, double rating, String releaseDate) {
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
     *
     * @return Movie Id
     */
    public long getId() {
        return id;
    }

    /**
     * Get Movie Title
     *
     * @return Movie Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get Movie Overview
     *
     * @return Movie Overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * Get Movie Poster Url
     *
     * @return Movie Poster Url
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * Get Movie Backdrop Url
     *
     * @return Movie Backdrop Url
     */
    public String getBackdropPath() {
        return backdropPath;
    }

    /**
     * Get Movie Rating
     *
     * @return Movie Rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Set Movie Rating
     *
     * @param rating Movie Rating
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Get Movie Release Date
     *
     * @return Release Date
     */
    public String getReleaseDate() {
        return releaseDate;
    }





    /**
     * 新增的Parcelable部分，讀取參數，參數順序要和建構子一樣
     * @param in
     */
    protected MovieData(Parcel in) {
        id = in.readLong();
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();
    }


    /**
     * 新增的Parcelable部分
     */
    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };


    /**
     * 新增的Parcelable部分，Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }


    /**
     * 新增的Parcelable部分，將物件攤平成Parcel，參數順序要和建構子一樣
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeDouble(rating);
        dest.writeString(releaseDate);
    }
}
