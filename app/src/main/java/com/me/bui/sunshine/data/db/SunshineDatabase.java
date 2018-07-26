package com.me.bui.sunshine.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

/**
 * Created by mao.bui on 7/26/2018.
 */

@Database(entities = {WeatherEntry.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class SunshineDatabase extends RoomDatabase{

    private static final String DATABASE_NAME = "weather";

    // For Singleton instantiation
    private static final Object LOCK  = new Object();
    private static volatile SunshineDatabase sInstance;

    public static SunshineDatabase getInstance (Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            SunshineDatabase.class, SunshineDatabase.DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }

    public abstract WeatherDao mWeatherDao();
}
