package com.pcr.myinfoweather.network;

import com.pcr.myinfoweather.models.weather.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Paula on 23/02/2015.
 */
public class JsonParsers {

    public static User parseWeather(JSONObject weatherJson) throws JSONException {
        if(weatherJson != null) {
            return User.newBuilder()
                    //.withTitle(weatherJson.getJSONObject("main")
                    .withTempMax((float) weatherJson.getJSONObject("main").getDouble("temp_max"))
                    .withTempMin((float) weatherJson.getJSONObject("main").getDouble("temp_min"))
                    .withWindSpeed((float) weatherJson.getJSONObject("wind").getDouble("speed"))



                    .build();
        }

        return null;
    }





}
