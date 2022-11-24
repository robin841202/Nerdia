package com.example.movieinfo.model.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.movieinfo.model.database.entity.MovieWatchlistEntity;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface IMovieWatchlistDao {

    /**
     * Insert movie in watchlist (Ignore new added duplicated data, keep the old one)
     * @param movieWatchlistEntity
     * @return number of row inserted
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    ListenableFuture<Long> insert(MovieWatchlistEntity movieWatchlistEntity);

    @Update
    void update(MovieWatchlistEntity movieWatchlistEntity);

    @Query("SELECT * FROM MovieWatchlistTable ORDER BY createTime DESC")
    LiveData<List<MovieWatchlistEntity>> getAll();

    @Query("SELECT EXISTS(SELECT * FROM MovieWatchlistTable WHERE movieId = :id)")
    LiveData<Boolean> checkExistById(long id);

    @Query("DELETE FROM MovieWatchlistTable")
    void deleteAll();

    /**
     * Delete movie in watchlist by movie Id
     * @param id movie Id
     * @return number of row deleted
     */
    @Query("DELETE FROM MovieWatchlistTable WHERE movieId = :id")
    ListenableFuture<Integer> deleteById(long id);

}
