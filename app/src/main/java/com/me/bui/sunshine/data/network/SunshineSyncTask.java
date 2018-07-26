package com.me.bui.sunshine.data.network;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.text.format.DateUtils;

import com.me.bui.sunshine.data.pref.SunshinePreferences;
import com.me.bui.sunshine.data.db.WeatherContract;

import java.net.URL;

/**
 * Created by mao.bui on 6/4/2018.
 */
public class SunshineSyncTask {

    synchronized public static void syncWeather(Context context) {

        try {
            URL weatherRequestUrl = NetworkUtils.getUrl(context);

            /* Use the URL to retrieve the JSON */
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

            /* Parse the JSON into a list of weather values */
            ContentValues[] weatherValues = OpenWeatherJsonParser
                    .getWeatherContentValuesFromJson(context, jsonWeatherResponse);

            if (weatherValues != null && weatherValues.length != 0) {
                /* Get a handle on the ContentResolver to delete and insert data */
                ContentResolver sunshineContentResolver = context.getContentResolver();

                /* Delete old weather data because we don't need to keep multiple days' data */
                sunshineContentResolver.delete(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        null,
                        null);

                /* Insert our new weather data into Sunshine's ContentProvider */
                sunshineContentResolver.bulkInsert(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        weatherValues);

                boolean notificationsEnabled = SunshinePreferences.areNotificationsEnabled(context);
                long timeSinceLastNotification = SunshinePreferences
                        .getEllapsedTimeSinceLastNotification(context);

                boolean oneDayPassedSinceLastNotification = false;
                if (timeSinceLastNotification >= DateUtils.DAY_IN_MILLIS) {
                    oneDayPassedSinceLastNotification = true;
                }
                if (notificationsEnabled && oneDayPassedSinceLastNotification) {
                    NotificationUtils.notifyUserOfNewWeather(context);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
