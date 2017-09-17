package com.twitter.challenge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by edk763 on 9/16/17.
 */

public class Wind {
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
}
