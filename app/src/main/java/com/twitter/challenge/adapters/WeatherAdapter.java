package com.twitter.challenge.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.challenge.R;
import com.twitter.challenge.model.WeatherCondition;

import java.util.List;

/**
 * Created by edk763 on 9/16/17.
 */


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private List<WeatherCondition> fiveDayWeatherList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView temperature;
        public TextView windView;
        public ImageView cloudView;


        public ViewHolder(View view) {
            super(view);
            cloudView = (ImageView) view.findViewById(R.id.weatherIcon);
            temperature = (TextView) view.findViewById(R.id.temperature);
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
        holder.temperature.setText(String.valueOf(fiveDayWeatherList.get(position).getWeather().getTemp()));
        holder.windView.setText(String.valueOf(fiveDayWeatherList.get(position).getWind().getSpeed()));
    }

    @Override
    public int getItemCount() {
        return fiveDayWeatherList.size();
    }
}
