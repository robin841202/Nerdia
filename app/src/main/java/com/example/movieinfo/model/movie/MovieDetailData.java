package com.example.movieinfo.model.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.movieinfo.model.Genre;
import com.example.movieinfo.model.ProductionCompany;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Movie Detail Data Model, using @SerializedName to map to json key
 */
public class MovieDetailData implements Parcelable {


    @SerializedName("adult")
    private final boolean isAdult;

    @SerializedName("budget")
    private final int budget;

    @SerializedName("genres")
    private ArrayList<Genre> genres;

    @SerializedName("production_companies")
    private ArrayList<ProductionCompany> productionCompanies;

    @SerializedName("status")
    private final String status;

    @SerializedName("revenue")
    private final int revenue;

    @SerializedName("id")
    private final long id;

    @SerializedName("title")
    private final String title;

    @SerializedName("overview")
    private final String overview;

    @SerializedName("poster_path")
    private final String posterPath;

    @SerializedName("backdrop_path")
    private final String backdropPath;

    @SerializedName("vote_average")
    private double rating;

    @SerializedName("release_date")
    private final String releaseDate;

    @SerializedName("vote_count")
    private final int voteCount;

    public MovieDetailData(boolean isAdult, int budget, ArrayList<Genre> genres, ArrayList<ProductionCompany> productionCompanies, String status, int revenue, long id, String title, String overview, String posterPath, String backdropPath, double rating, String releaseDate, int voteCount) {
        this.isAdult = isAdult;
        this.budget = budget;
        this.genres = genres;
        this.productionCompanies = productionCompanies;
        this.status = status;
        this.revenue = revenue;
        this.id = id;
        this.title = title;
        this.overview = overview;


        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.voteCount = voteCount;
    }


    /**
     * Get IsAdult or not
     *
     * @return
     */
    public boolean getIsAdult() {
        return isAdult;
    }

    /**
     * Get Budget
     *
     * @return
     */
    public int getBudget() {
        return budget;
    }

    /**
     * Get Genres
     *
     * @return
     */
    public ArrayList<Genre> getGenres() {
        return genres;
    }

    /**
     * Get Production Companies
     *
     * @return
     */
    public ArrayList<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }


    /**
     * Get Id
     *
     * @return Movie Id
     */
    public long getId() {
        return id;
    }

    /**
     * Get Title
     *
     * @return Movie Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get Overview
     *
     * @return Movie Overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * Get Poster Url
     *
     * @return Movie Poster Url
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * Get Backdrop Url
     *
     * @return Movie Backdrop Url
     */
    public String getBackdropPath() {
        return backdropPath;
    }

    /**
     * Get Rating
     *
     * @return Movie Rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Set Rating
     *
     * @param rating Movie Rating
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Get Release Date
     *
     * @return Release Date
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Get Vote Count
     *
     * @return
     */
    public int getVoteCount() {
        return voteCount;
    }


    /**
     * 新增的Parcelable部分，讀取參數，參數順序要和建構子一樣
     *
     * @param in
     */
    protected MovieDetailData(Parcel in) {
        isAdult = in.readByte() != 0;
        budget = in.readInt();
        status = in.readString();
        revenue = in.readInt();
        id = in.readLong();
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();
        voteCount = in.readInt();
    }

    /**
     * 新增的Parcelable部分
     */
    public static final Creator<MovieDetailData> CREATOR = new Creator<MovieDetailData>() {
        @Override
        public MovieDetailData createFromParcel(Parcel in) {
            return new MovieDetailData(in);
        }

        @Override
        public MovieDetailData[] newArray(int size) {
            return new MovieDetailData[size];
        }
    };


    /**
     * 新增的Parcelable部分，Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     *
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 新增的Parcelable部分，將物件攤平成Parcel，參數順序要和建構子一樣
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isAdult ? 1 : 0));
        dest.writeInt(budget);
        dest.writeString(status);
        dest.writeInt(revenue);
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeDouble(rating);
        dest.writeString(releaseDate);
        dest.writeInt(voteCount);
    }


}
