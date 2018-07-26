package com.me.bui.sunshine.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.me.bui.sunshine.data.SunshineRepository;

import java.util.Date;

/**
 * Created by mao.bui on 7/26/2018.
 */
public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final SunshineRepository mRepository;
    private final Date mDate;

    public DetailViewModelFactory(SunshineRepository repository, Date date) {
        this.mRepository = repository;
        this.mDate = date;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailActivityViewModel(mRepository, mDate);
    }
}
