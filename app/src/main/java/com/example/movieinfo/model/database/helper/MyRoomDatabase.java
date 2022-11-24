package com.example.movieinfo.model.database.helper;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.movieinfo.model.database.dao.IMovieWatchlistDao;
import com.example.movieinfo.model.database.dao.ITvShowWatchlistDao;
import com.example.movieinfo.model.database.entity.MovieWatchlistEntity;
import com.example.movieinfo.model.database.entity.TvShowWatchlistEntity;

@Database(entities = {MovieWatchlistEntity.class, TvShowWatchlistEntity.class}, version = 1, exportSchema = false)
public abstract class MyRoomDatabase extends RoomDatabase {
    // Define database name
    private static final String DATABASE_NAME = "my_database";

    public abstract IMovieWatchlistDao movieWatchlistDao();
    public abstract ITvShowWatchlistDao tvShowWatchlistDao();

    //region Create the MyRoomDatabase as a singleton to prevent having multiple instances of the database opened at the same time
    private static MyRoomDatabase INSTANCE;

    public static MyRoomDatabase getDataBase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyRoomDatabase.class, DATABASE_NAME)
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            //.addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    //endregion

}
