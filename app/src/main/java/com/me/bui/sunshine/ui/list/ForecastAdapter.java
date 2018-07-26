package com.me.bui.sunshine.ui.list;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.bui.sunshine.R;
import com.me.bui.sunshine.data.db.ListWeatherEntry;
import com.me.bui.sunshine.data.db.WeatherEntry;
import com.me.bui.sunshine.utilities.SunshineDateUtils;
import com.me.bui.sunshine.utilities.SunshineWeatherUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by mao.bui on 5/27/2018.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private final Context mContext;
    private ForecastAdapterOnClickHandler mClickHandler;

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    private boolean mUseTodayLayout;
    private List<WeatherEntry> mForecast;

    public ForecastAdapter(Context context, ForecastAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        mUseTodayLayout = mContext.getResources().getBoolean(R.bool.use_today_layout);
    }

    public interface ForecastAdapterOnClickHandler {
        public void onClick(Date date);
    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        int layoutId;
        switch (viewType) {
            case VIEW_TYPE_TODAY:
                layoutId = R.layout.list_item_forecast_today;
                break;
            case VIEW_TYPE_FUTURE_DAY:
                layoutId = R.layout.forecast_list_item;
                break;
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
        View view = LayoutInflater.from(mContext)
                .inflate(layoutId, viewGroup, false);
        view.setFocusable(true);
        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {
        WeatherEntry currentWeather = mForecast.get(position);

        int weatherId = currentWeather.getId();
        int weatherImageId;
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_TODAY:
                weatherImageId = SunshineWeatherUtils
                        .getLargeArtResourceIdForWeatherCondition(weatherId);
                break;
            case VIEW_TYPE_FUTURE_DAY:
                weatherImageId = SunshineWeatherUtils
                        .getSmallArtResourceIdForWeatherCondition(weatherId);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        forecastAdapterViewHolder.iconView.setImageResource(weatherImageId);
        long dateInMillis = currentWeather.getDate().getTime();
        String dateString = SunshineDateUtils.getFriendlyDateString(mContext, dateInMillis, false);
        forecastAdapterViewHolder.dateView.setText(dateString);
        String description = SunshineWeatherUtils.getStringForWeatherCondition(mContext, weatherId);
        String descriptionA11y = mContext.getString(R.string.a11y_forecast, description);
        forecastAdapterViewHolder.descriptionView.setText(description);
        forecastAdapterViewHolder.descriptionView.setContentDescription(descriptionA11y);

        double highInCelsius = currentWeather.getMax();
        String highString = SunshineWeatherUtils.formatTemperature(mContext, highInCelsius);
        String highA11y = mContext.getString(R.string.a11y_high_temp, highString);
        forecastAdapterViewHolder.highTempView.setText(highString);
        forecastAdapterViewHolder.highTempView.setContentDescription(highA11y);

        double lowInCelsius = currentWeather.getMin();
        String lowString = SunshineWeatherUtils.formatTemperature(mContext, lowInCelsius);
        String lowA11y = mContext.getString(R.string.a11y_low_temp, lowString);
        forecastAdapterViewHolder.lowTempView.setText(lowString);
        forecastAdapterViewHolder.lowTempView.setContentDescription(lowA11y);
    }

    @Override
    public int getItemViewType(int position) {
        if (mUseTodayLayout && position == 0) {
            return VIEW_TYPE_TODAY;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    @Override
    public int getItemCount() {
        if (null == mForecast) return 0;
        return mForecast.size();
    }

    public void swapForecast(final List<WeatherEntry> newForecast) {
        // If there was no forecast data, then recreate all of the list
        if (mForecast == null) {
            mForecast = newForecast;
            notifyDataSetChanged();
        } else {
            /*
             * Otherwise we use DiffUtil to calculate the changes and update accordingly. This
             * shows the four methods you need to override to return a DiffUtil callback. The
             * old list is the current list stored in mForecast, where the new list is the new
             * values passed in from the observing the database.
             */

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mForecast.size();
                }

                @Override
                public int getNewListSize() {
                    return newForecast.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mForecast.get(oldItemPosition).getId() ==
                            newForecast.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    WeatherEntry newWeather = newForecast.get(newItemPosition);
                    WeatherEntry oldWeather = mForecast.get(oldItemPosition);
                    return newWeather.getId() == oldWeather.getId()
                            && newWeather.getDate().equals(oldWeather.getDate());
                }
            });
            mForecast = newForecast;
            result.dispatchUpdatesTo(this);
        }
    }

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView dateView;
        final TextView descriptionView;
        final TextView highTempView;
        final TextView lowTempView;
        final ImageView iconView;


        public ForecastAdapterViewHolder(View view) {
            super(view);
            iconView = (ImageView) view.findViewById(R.id.weather_icon);
            dateView = (TextView) view.findViewById(R.id.date);
            descriptionView = (TextView) view.findViewById(R.id.weather_description);
            highTempView = (TextView) view.findViewById(R.id.high_temperature);
            lowTempView = (TextView) view.findViewById(R.id.low_temperature);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Date date = mForecast.get(adapterPosition).getDate();
            mClickHandler.onClick(date);
        }
    }
}
