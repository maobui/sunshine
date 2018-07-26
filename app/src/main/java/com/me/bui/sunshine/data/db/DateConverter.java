package com.me.bui.sunshine.data.db;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by mao.bui on 7/26/2018.
 */

class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
