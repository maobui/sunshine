package com.me.bui.sunshine.utilities;

import android.content.Context;

import com.me.bui.sunshine.AppExecutors;
import com.me.bui.sunshine.data.SunshineRepository;
import com.me.bui.sunshine.data.db.SunshineDatabase;
import com.me.bui.sunshine.data.network.WeatherNetworkDataSource;
import com.me.bui.sunshine.ui.detail.DetailViewModelFactory;
import com.me.bui.sunshine.ui.list.MainViewModelFactory;

import java.util.Date;

/**
 * Created by mao.bui on 7/26/2018.
 */
public class InjectorUtils {

    public static SunshineRepository provideRepository(Context context) {
        SunshineDatabase database = SunshineDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        WeatherNetworkDataSource networkDataSource =
                WeatherNetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return SunshineRepository.getInstance(database.weatherDao(), networkDataSource, executors);
    }

    public static WeatherNetworkDataSource provideNetworkDataSource(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return WeatherNetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(Context context, Date date) {
        SunshineRepository repository = provideRepository(context.getApplicationContext());
        return new DetailViewModelFactory(repository, date);
    }

    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
        SunshineRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }

}
