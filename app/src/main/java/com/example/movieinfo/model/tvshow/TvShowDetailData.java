package com.example.movieinfo.model.tvshow;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.movieinfo.model.Genre;
import com.example.movieinfo.model.ProductionCompany;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * TvShow Detail Data Model, using @SerializedName to map to json key
 */
public class TvShowDetailData implements Parcelable {

    @SerializedName("last_air_date")
    private final String lastAirDate;

    @SerializedName("adult")
    private final boolean isAdult;

    @SerializedName("genres")
    private ArrayList<Genre> genres;

    @SerializedName("production_companies")
    private ArrayList<ProductionCompany> productionCompanies;

    @SerializedName("status")
    private final String status;

    @SerializedName("id")
    private final long id;

    @SerializedName("name")
    private final String title;

    @SerializedName("overview")
    private final String overview;

    @SerializedName("poster_path")
    private final String posterPath;

    @SerializedName("backdrop_path")
    private final String backdropPath;

    @SerializedName("vote_average")
    private double rating;

    @SerializedName("first_air_date")
    private final String onAirDate;

    @SerializedName("vote_count")
    private final int voteCount;

    public TvShowDetailData(String lastAirDate, boolean isAdult, String status, long id, String title, String overview, String posterPath, String backdropPath, double rating, String onAirDate, int voteCount) {
        this.lastAirDate = lastAirDate;
        this.isAdult = isAdult;
        this.status = status;
        this.id = id;
        this.title = title;
        this.overview = overview;


        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.rating = rating;
        this.onAirDate = onAirDate;
        this.voteCount = voteCount;
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
     * Get onAir Date
     *
     * @return Release Date
     */
    public String getOnAirDate() {
        return onAirDate;
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
     * Get Last-air Date
     *
     * @return
     */
    public String getLastAirDate() {
        return lastAirDate;
    }

    /**
     * Get IsAdult or not
     *
     * @return
     */
    public boolean isAdult() {
        return isAdult;
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
     * Get Status
     *
     * @return
     */
    public String getStatus() {
        return status;
    }


    /**
     * 新增的Parcelable部分，讀取參數，參數順序要和建構子一樣
     *
     * @param in
     */
    protected TvShowDetailData(Parcel in) {
        lastAirDate = in.readString();
        isAdult = in.readByte() != 0;
        status = in.readString();
        id = in.readLong();
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        rating = in.readDouble();
        onAirDate = in.readString();
        voteCount = in.readInt();
    }

    /**
     * 新增的Parcelable部分
     */
    public static final Creator<TvShowDetailData> CREATOR = new Creator<TvShowDetailData>() {
        @Override
        public TvShowDetailData createFromParcel(Parcel in) {
            return new TvShowDetailData(in);
        }

        @Override
        public TvShowDetailData[] newArray(int size) {
            return new TvShowDetailData[size];
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
        dest.writeString(lastAirDate);
        dest.writeByte((byte) (isAdult ? 1 : 0));
        dest.writeString(status);
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeDouble(rating);
        dest.writeString(onAirDate);
        dest.writeInt(voteCount);
    }

}
