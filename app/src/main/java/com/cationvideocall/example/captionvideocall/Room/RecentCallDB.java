package com.cationvideocall.example.captionvideocall.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RecentCallEntity.class}, version = 1)
public abstract class RecentCallDB extends RoomDatabase {

    private static RecentCallDB INSTANCE = null;

    public abstract RecentCallDao recentCallDao();


    public static RecentCallDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    RecentCallDB.class, "recentcall.db")
                    .build();

        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
