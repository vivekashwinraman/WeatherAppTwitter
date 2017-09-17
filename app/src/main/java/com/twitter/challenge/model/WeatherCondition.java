package com.twitter.challenge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by edk763 on 9/16/17.
 */

public class WeatherCondition {
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
     *
     */
    public WeatherCondition() {
    }

    /**
     *
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

}
