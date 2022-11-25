package com.example.movieinfo.model.tvshow;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * TvShow Data Model, using @SerializedName to map to json key
 */
public class TvShowData implements Parcelable {

    @SerializedName("adult")
    private boolean isAdult;

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("vote_average")
    private double rating;

    @SerializedName("first_air_date")
    private String onAirDate;

    @SerializedName("vote_count")
    private int voteCount;

    public TvShowData(long id, String title, String posterPath, double rating) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.rating = rating;
    }

    public TvShowData(long id, String title, String overview, String posterPath, String backdropPath, double rating, String onAirDate, int voteCount){
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
     * Get IsAdult or not
     *
     * @return
     */
    public boolean getIsAdult() {
        return isAdult;
    }

    /**
     * Get Id
     * @return Movie Id
     */
    public long getId() {
        return id;
    }

    /**
     * Get Title
     * @return Movie Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get Overview
     * @return Movie Overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * Get Poster Url
     * @return Movie Poster Url
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * Get Backdrop Url
     * @return Movie Backdrop Url
     */
    public String getBackdropPath() {
        return backdropPath;
    }

    /**
     * Get Rating
     * @return Movie Rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Set Rating
     * @param rating Movie Rating
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Get onAir Date
     * @return Release Date
     */
    public String getOnAirDate() {
        return onAirDate;
    }

    /**
     * Get Vote Count
     * @return
     */
    public int getVoteCount(){return voteCount;}



    /**
     * 新增的Parcelable部分，讀取參數，參數順序要和建構子一樣
     * @param in
     */
    protected TvShowData(Parcel in) {
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
    public static final Creator<TvShowData> CREATOR = new Creator<TvShowData>() {
        @Override
        public TvShowData createFromParcel(Parcel in) {
            return new TvShowData(in);
        }

        @Override
        public TvShowData[] newArray(int size) {
            return new TvShowData[size];
        }
    };


    /**
     * 新增的Parcelable部分，Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     * @return
     */
    @Override
    public int describeContents() {return 0;}


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
        dest.writeString(onAirDate);
        dest.writeInt(voteCount);
    }
}
