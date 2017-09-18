package com.twitter.challenge.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.challenge.R;
import com.twitter.challenge.model.WeatherCondition;
import com.twitter.challenge.utils.TemperatureConverter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by edk763 on 9/16/17.
 */


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private List<WeatherCondition> fiveDayWeatherList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView temperatureView;
        public TextView windView;
        public ImageView cloudView;

        public ViewHolder(View view) {
            super(view);
            cloudView = (ImageView) view.findViewById(R.id.weatherIcon);
            temperatureView = (TextView) view.findViewById(R.id.temperature);
            windView = (TextView) view.findViewById(R.id.wind);
        }
    }

    public WeatherAdapter(List<WeatherCondition> fiveDayWeatherList) {
        this.fiveDayWeatherList = fiveDayWeatherList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(fiveDayWeatherList.get(position).getClouds().getCloudiness() > 50) {
            holder.cloudView.setImageResource(R.mipmap.rain);
        } else {
            holder.cloudView.setImageResource(R.mipmap.sun);
        }
        DecimalFormat df = new DecimalFormat("##.#");

        String temperatureString =  String.format(holder.itemView.getResources().getString(R.string.temperature_c),
                df.format(fiveDayWeatherList.get(position).getWeather().getTemp()),
                df.format(TemperatureConverter.celsiusToFahrenheit(fiveDayWeatherList.get(position).getWeather().getTemp())));

        holder.temperatureView.setText(temperatureString);
        holder.windView.setText(fiveDayWeatherList.get(position).getWind().getSpeed());
    }

    @Override
    public int getItemCount() {
        return fiveDayWeatherList.size();
    }
}
