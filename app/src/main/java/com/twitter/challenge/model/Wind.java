package com.twitter.challenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind implements Parcelable{
    @SerializedName("speed")
    @Expose
    private String speed;
    @SerializedName("deg")
    @Expose
    private Integer deg;

    /**
     * No args constructor for use in serialization
     *
     */
    public Wind() {
    }

    /**
     *
     * @param speed
     * @param deg
     */
    public Wind(String speed, Integer deg) {
        super();
        this.speed = speed;
        this.deg = deg;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public Integer getDeg() {
        return deg;
    }

    public void setDeg(Integer deg) {
        this.deg = deg;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(speed);
        parcel.writeInt(deg);
    }

    public static final Parcelable.Creator<Wind> CREATOR = new Creator<Wind>() {
        public Wind createFromParcel(Parcel source) {
            Wind wind = new Wind();
            wind.speed = source.readString();
            wind.deg = source.readInt();
            return wind;
        }
        public Wind[] newArray(int size) {
            return new Wind[size];
        }

    };

}
