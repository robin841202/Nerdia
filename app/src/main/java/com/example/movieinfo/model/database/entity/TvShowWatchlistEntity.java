package com.example.movieinfo.model.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.movieinfo.model.database.helper.DateConverter;

import java.util.Date;

@Entity(tableName = "TvShowWatchlistTable")
@TypeConverters(DateConverter.class)
public class TvShowWatchlistEntity {

    @PrimaryKey
    @NonNull
    private long tvShowId;

    @NonNull
    private String title;

    private String posterPath;

    private double rating;

    private boolean isAdult;

    private Date createTime;

    public TvShowWatchlistEntity(@NonNull long tvShowId, @NonNull String title, String posterPath, double rating, boolean isAdult, Date createTime) {
        this.tvShowId = tvShowId;
        this.title = title;
        this.posterPath = posterPath;
        this.rating = rating;
        this.isAdult = isAdult;
        this.createTime = createTime;
    }

    public long getTvShowId() {
        return tvShowId;
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
