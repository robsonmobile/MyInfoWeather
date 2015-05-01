package com.pcr.myinfoweather.models.currentweather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Paula Rosa on 31/03/2015.
 */
public class Response {
    @SerializedName("coord")
    private List<WeatherData> weatherDataList;

    public List<WeatherData> getWeatherDataList() {
        return weatherDataList;
    }

    public void setWeatherDataList(List<WeatherData> weatherDataList) {
        this.weatherDataList = weatherDataList;
    }
}
