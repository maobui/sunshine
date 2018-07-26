package com.me.bui.sunshine.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.me.bui.sunshine.data.SunshineRepository;
import com.me.bui.sunshine.data.db.WeatherEntry;

import java.util.Date;

/**
 * Created by mao.bui on 7/26/2018.
 */
public class DetailActivityViewModel extends ViewModel{

    // Weather forecast the user is looking at
    private LiveData<WeatherEntry> mWeather;
    // Date for the weather forecast
    private final Date mDate;
    private final SunshineRepository mRepository;

    public DetailActivityViewModel(SunshineRepository repository, Date date) {
        mRepository = repository;
        mDate = date;
        mWeather = mRepository.getWeatherByDate(mDate);
    }

    public LiveData<WeatherEntry> getWeather() {
        return mWeather;
    }
}
