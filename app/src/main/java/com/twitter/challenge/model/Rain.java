package com.twitter.challenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rain implements Parcelable{

    @SerializedName("3h")
    @Expose
    private Integer _3h;

    /**
     * No args constructor for use in serialization
     *
     */
    public Rain() {
    }

    /**
     *
     * @param _3h
     */
    public Rain(Integer _3h) {
        super();
        this._3h = _3h;
    }

    public Integer get3h() {
        return _3h;
    }

    public void set3h(Integer _3h) {
        this._3h = _3h;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(_3h);
    }

    public static final Parcelable.Creator<Rain> CREATOR = new Creator<Rain>() {
        public Rain createFromParcel(Parcel source) {
            Rain rain = new Rain();
            rain._3h = source.readInt();
            return rain;
        }
        public Rain[] newArray(int size) {
            return new Rain[size];
        }

    };
}
