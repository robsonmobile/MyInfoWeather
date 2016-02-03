package com.pcr.myinfoweather.interfaces;

import com.pcr.myinfoweather.models.currentweather.Response;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by paularosa on 2/2/16.
 */
public interface WeatherApi {

    String ENDPOINT_WEATHER = "/weather";

    @GET(ENDPOINT_WEATHER)
    void requestWeatherByAddress(
            @Query("q") String cityName,
            @Query("units") String units,
            @Query("APPID") String appId,
            Callback<Response> callback
    );

    @GET(ENDPOINT_WEATHER)
    void requestWeatherByLocation(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("units") String units,
            @Query("APPID") String appId,
            Callback<String> callback
    );
}
