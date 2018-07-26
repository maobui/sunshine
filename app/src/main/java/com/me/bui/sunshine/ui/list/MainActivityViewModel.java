package com.me.bui.sunshine.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.me.bui.sunshine.data.SunshineRepository;
import com.me.bui.sunshine.data.db.WeatherEntry;

import java.util.List;

/**
 * Created by mao.bui on 7/26/2018.
 */
public class MainActivityViewModel extends ViewModel{

    private final SunshineRepository mRepository;
    private final LiveData<List<WeatherEntry>> mForecast;

    public MainActivityViewModel(SunshineRepository repository) {
        mRepository = repository;
        mForecast = mRepository.getCurrentWeatherForecasts();
    }

    public LiveData<List<WeatherEntry>> getForecast() {
        return mForecast;
    }
}
