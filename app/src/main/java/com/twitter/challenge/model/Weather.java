package com.twitter.challenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather implements Parcelable {
    @SerializedName("temp")
    @Expose
    private Float temp;
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
    public Weather(Float temp, Integer pressure, Integer humidity) {
        super();
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
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


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeFloat(temp);
        parcel.writeInt(pressure);
        parcel.writeInt(humidity);
    }

    public static final Parcelable.Creator<Weather> CREATOR = new Creator<Weather>() {
        public Weather createFromParcel(Parcel source) {
            Weather weather = new Weather();
            weather.temp = source.readFloat();
            weather.pressure = source.readInt();
            weather.humidity = source.readInt();
            return weather;
        }
        public Weather[] newArray(int size) {
            return new Weather[size];
        }

    };
}
