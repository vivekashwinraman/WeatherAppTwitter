package com.twitter.challenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by edk763 on 9/16/17.
 */

public class Coord implements Parcelable {
    @SerializedName("lon")
    @Expose
    private Double lon;

    @SerializedName("lat")
    @Expose
    private Double lat;

    /**
     * No args constructor for use in serialization
     */
    public Coord() {
    }

    /**
     * @param lon
     * @param lat
     */
    public Coord(Double lon, Double lat) {
        super();
        this.lon = lon;
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeDouble(lon);
        parcel.writeDouble(lat);
    }

    public static final Parcelable.Creator<Coord> CREATOR = new Creator<Coord>() {
        public Coord createFromParcel(Parcel source) {
            Coord coord = new Coord();
            coord.lon = source.readDouble();
            coord.lat = source.readDouble();
            return coord;
        }

        public Coord[] newArray(int size) {
            return new Coord[size];
        }

    };
}
