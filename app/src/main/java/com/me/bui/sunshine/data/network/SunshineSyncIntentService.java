package com.me.bui.sunshine.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.me.bui.sunshine.utilities.InjectorUtils;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SunshineSyncIntentService extends IntentService {
    private static final String LOG_TAG = SunshineSyncIntentService.class.getSimpleName();

    public SunshineSyncIntentService() {
        super("SunshineSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "Intent service started");
        WeatherNetworkDataSource networkDataSource =
                InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        networkDataSource.fetchWeather();
//        SunshineSyncTask.syncWeather(this);
    }
}
