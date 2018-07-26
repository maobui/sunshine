package com.me.bui.sunshine.ui.detail;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.me.bui.sunshine.data.db.WeatherEntry;

/**
 * Created by mao.bui on 7/26/2018.
 */
public class DetailActivityViewModel extends ViewModel{

    // Weather forecast the user is looking at
    private MutableLiveData<WeatherEntry> mWeather;

    public DetailActivityViewModel() {
        mWeather = new MutableLiveData<>();

    }

    public MutableLiveData<WeatherEntry> getWeather() {
        return mWeather;
    }

    public void setWeather(WeatherEntry weatherEntry) {
        mWeather.postValue(weatherEntry);
    }
}
