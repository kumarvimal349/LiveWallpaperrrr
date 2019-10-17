package com.vimal.me.wallpaper.Database.LocalDatabase;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;


import com.vimal.me.wallpaper.Database.Recents;

import static com.vimal.me.wallpaper.Database.LocalDatabase.LocalDatabase.DATABASE_VERSION;


@Database(entities = Recents.class,version = DATABASE_VERSION,exportSchema = false)
public abstract class  LocalDatabase extends RoomDatabase {

    public static final int DATABASE_VERSION = 3;

    public static final String DATABASE_NAME = "Wallpaper HD";

    public abstract RecentsDAO recentsDAO();


    private static LocalDatabase instance;

    public static LocalDatabase getInstance(Context context) {
        if (instance == null) {
           instance  = Room.databaseBuilder(context,LocalDatabase.class,DATABASE_NAME)
                   .fallbackToDestructiveMigration()
                   .build();
        }
        return instance;
    }
}