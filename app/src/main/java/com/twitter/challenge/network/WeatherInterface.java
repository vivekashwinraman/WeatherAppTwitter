package com.twitter.challenge.network;

import com.twitter.challenge.model.WeatherCondition;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by edk763 on 9/16/17.
 */

public interface WeatherInterface {
    @GET("/current.json")
    Call<WeatherCondition> getCurrent();

    // for async
    @GET("/future_{day}.json")
    Call<WeatherCondition> getFuture(@Path("day") String day);
}
