package com.twitter.challenge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by edk763 on 9/16/17.
 */

public class Weather {
    @SerializedName("temp")
    @Expose
    private Double temp;
    @SerializedName("pressure")
    @Expose
    private Integer pressure;
    @SerializedName("humidity")
    @Expose
    private Integer humidity;

    /**
     * No args constructor for use in serialization
     *
     */
    public Weather() {
    }

    /**
     *
     * @param humidity
     * @param pressure
     * @param temp
     */
    public Weather(Double temp, Integer pressure, Integer humidity) {
        super();
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }
}
