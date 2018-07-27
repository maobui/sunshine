package com.me.bui.sunshine.data.network;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.me.bui.sunshine.data.db.WeatherEntry;
import com.me.bui.sunshine.ui.detail.DetailActivity;
import com.me.bui.sunshine.R;
import com.me.bui.sunshine.data.pref.SunshinePreferences;
import com.me.bui.sunshine.utilities.SunshineDateUtils;
import com.me.bui.sunshine.utilities.SunshineWeatherUtils;

/**
 * Created by mao.bui on 6/8/2018.
 */
public class NotificationUtils {

    private static final int WEATHER_NOTIFICATION_ID = 3004;
    private static final String CHANEL_ID = "sunshine_chanel";
    private static final CharSequence CHANEL_NAME = "sunshine_chanel_name";

    public static void notifyUserOfNewWeather(Context context, WeatherResponse response) {
        WeatherEntry todayWeatherEntry = response.getWeatherForecast()[0];

        if (todayWeatherEntry != null) {

            /* Weather ID as returned by API, used to identify the icon to be used */
            int weatherId = todayWeatherEntry.getWeatherIconId();
            double high = todayWeatherEntry.getMax();
            double low = todayWeatherEntry.getMin();

            Resources resources = context.getResources();
            int largeArtResourceId = SunshineWeatherUtils
                    .getLargeArtResourceIdForWeatherCondition(weatherId);

            Bitmap largeIcon = BitmapFactory.decodeResource(
                    resources,
                    largeArtResourceId);

            String notificationTitle = context.getString(R.string.app_name);

            String notificationText = getNotificationText(context, weatherId, high, low);

            int smallArtResourceId = SunshineWeatherUtils
                    .getSmallArtResourceIdForWeatherCondition(weatherId);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANEL_ID)
                    .setColor(ContextCompat.getColor(context,R.color.colorPrimary))
                    .setSmallIcon(smallArtResourceId)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setAutoCancel(true);

            Intent detailIntentForToday = new Intent(context, DetailActivity.class);
            long timestamp = SunshineDateUtils.getNormalizedUtcDateForToday().getTime();
            detailIntentForToday.putExtra(DetailActivity.WEATHER_ID_EXTRA, timestamp);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addNextIntentWithParentStack(detailIntentForToday);
            PendingIntent resultPendingIntent = taskStackBuilder
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(resultPendingIntent);

            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANEL_ID,
                        CHANEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(WEATHER_NOTIFICATION_ID, notificationBuilder.build());

            SunshinePreferences.saveLastNotificationTime(context, System.currentTimeMillis());
        }
    }

    private static String getNotificationText(Context context, int weatherId, double high, double low) {
        String shortDescription = SunshineWeatherUtils
                .getStringForWeatherCondition(context, weatherId);

        String notificationFormat = context.getString(R.string.format_notification);

        String notificationText = String.format(notificationFormat,
                shortDescription,
                SunshineWeatherUtils.formatTemperature(context, high),
                SunshineWeatherUtils.formatTemperature(context, low));

        return notificationText;
    }
}
