package com.example.movieinfo.model.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.movieinfo.model.database.entity.TvShowWatchlistEntity;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface ITvShowWatchlistDao {

    /**
     * Insert tvShow in watchlist (Ignore new added duplicated data, keep the old one)
     *
     * @param tvShowWatchlistEntity
     * @return number of row inserted
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    ListenableFuture<Long> insert(TvShowWatchlistEntity tvShowWatchlistEntity);

    @Update
    void update(TvShowWatchlistEntity tvShowWatchlistEntity);

    @Query("SELECT * FROM TvShowWatchlistTable ORDER BY createTime DESC")
    LiveData<List<TvShowWatchlistEntity>> getAll();

    @Query("SELECT EXISTS(SELECT * FROM TvShowWatchlistTable WHERE tvShowId = :id)")
    ListenableFuture<Boolean> checkExistById(long id);

    @Query("DELETE FROM TvShowWatchlistTable")
    void deleteAll();

    /**
     * Delete tvShow in watchlist by tvShow Id
     *
     * @param id tvShow Id
     * @return number of row deleted
     */
    @Query("DELETE FROM TvShowWatchlistTable WHERE tvShowId = :id")
    ListenableFuture<Integer> deleteById(long id);

}
