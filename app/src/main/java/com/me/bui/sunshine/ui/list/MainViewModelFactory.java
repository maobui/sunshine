package com.me.bui.sunshine.ui.list;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.me.bui.sunshine.data.SunshineRepository;

/**
 * Created by mao.bui on 7/26/2018.
 */
public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final SunshineRepository mRepository;

    public MainViewModelFactory(SunshineRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(mRepository);
    }
}
