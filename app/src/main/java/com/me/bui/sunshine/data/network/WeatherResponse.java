package com.me.bui.sunshine.data.network;

import android.support.annotation.NonNull;

import com.me.bui.sunshine.data.db.WeatherEntry;

/**
 * Created by mao.bui on 7/26/2018.
 */
public class WeatherResponse {
    @NonNull
    private final WeatherEntry[] mWeatherForecast;

    public WeatherResponse(@NonNull final WeatherEntry[] weatherForecast) {
        mWeatherForecast = weatherForecast;
    }

    public WeatherEntry[] getWeatherForecast() {
        return mWeatherForecast;
    }
}
