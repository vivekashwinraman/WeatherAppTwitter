package com.twitter.challenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Clouds implements Parcelable{
    @SerializedName("cloudiness")
    @Expose
    private Integer cloudiness;

    /**
     * No args constructor for use in serialization
     *
     */
    public Clouds() {
    }

    /**
     *
     * @param cloudiness
     */
    public Clouds(Integer cloudiness) {
        super();
        this.cloudiness = cloudiness;
    }

    public Integer getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(Integer cloudiness) {
        this.cloudiness = cloudiness;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(cloudiness);
    }

    public static final Parcelable.Creator<Clouds> CREATOR = new Creator<Clouds>() {
        public Clouds createFromParcel(Parcel source) {
            Clouds clouds = new Clouds();
            clouds.cloudiness = source.readInt();
            return clouds;
        }
        public Clouds[] newArray(int size) {
            return new Clouds[size];
        }

    };

}
