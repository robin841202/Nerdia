package com.robinhsueh.movieinfo.model.database.entity;

import androidx.annotation.NonNull;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.robinhsueh.movieinfo.model.database.helper.DateConverter;

import java.util.Date;

@Entity(tableName = "MovieWatchlistTable")
@TypeConverters(DateConverter.class)
public class MovieWatchlistEntity {

    @PrimaryKey
    @NonNull
    private long movieId;

    @NonNull
    private String title;

    private String posterPath;

    private double rating;

    private boolean isAdult;

    private Date createTime;

    public MovieWatchlistEntity(@NonNull long movieId, @NonNull String title, String posterPath, double rating, boolean isAdult, Date createTime) {
        this.movieId = movieId;
        this.title = title;
        this.posterPath = posterPath;
        this.rating = rating;
        this.isAdult = isAdult;
        this.createTime = createTime;
    }

    public long getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public double getRating() {
        return rating;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public Date getCreateTime() {
        return createTime;
    }
}
