package com.twitter.challenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by edk763 on 9/16/17.
 */

public class WeatherCondition implements Parcelable {
    @SerializedName("coord")
    @Expose
    private Coord coord;
    @SerializedName("weather")
    @Expose
    private Weather weather;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("rain")
    @Expose
    private Rain rain;
    @SerializedName("clouds")
    @Expose
    private Clouds clouds;
    @SerializedName("name")
    @Expose
    private String name;

    private int day;

    /**
     * No args constructor for use in serialization
     */
    public WeatherCondition() {
    }

    /**
     * @param clouds
     * @param coord
     * @param wind
     * @param name
     * @param weather
     * @param rain
     */
    public WeatherCondition(Coord coord, Weather weather, Wind wind, Rain rain, Clouds clouds, String name) {
        super();
        this.coord = coord;
        this.weather = weather;
        this.wind = wind;
        this.rain = rain;
        this.clouds = clouds;
        this.name = name;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeInt(day);
        parcel.writeParcelable(clouds, flags);
        parcel.writeParcelable(rain, flags);
        parcel.writeParcelable(wind, flags);
        parcel.writeParcelable(weather, flags);
        parcel.writeParcelable(coord, flags);
    }

    public static final Parcelable.Creator<WeatherCondition> CREATOR = new Creator<WeatherCondition>() {
        public WeatherCondition createFromParcel(Parcel source) {
            WeatherCondition weatherCondition = new WeatherCondition();
            weatherCondition.name = source.readString();
            weatherCondition.day = source.readInt();
            weatherCondition.clouds = source.readParcelable(getClass().getClassLoader());
            weatherCondition.rain = source.readParcelable(getClass().getClassLoader());
            weatherCondition.wind = source.readParcelable(getClass().getClassLoader());
            weatherCondition.weather = source.readParcelable(getClass().getClassLoader());
            weatherCondition.coord = source.readParcelable(getClass().getClassLoader());
            return weatherCondition;
        }

        public WeatherCondition[] newArray(int size) {
            return new WeatherCondition[size];
        }

    };
}
