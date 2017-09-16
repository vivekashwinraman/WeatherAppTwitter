package com.twitter.challenge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by edk763 on 9/16/17.
 */



public class Rain {

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

}
