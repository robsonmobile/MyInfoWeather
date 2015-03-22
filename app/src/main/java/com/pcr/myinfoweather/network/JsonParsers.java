package com.pcr.myinfoweather.network;

import com.pcr.myinfoweather.models.weather.Weatherx;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Paula on 23/02/2015.
 */
public class JsonParsers {

    public static Weatherx parseWeather(JSONObject weatherJson) throws JSONException {
        if(weatherJson != null) {
            return Weatherx.newBuilder()
                    .withTempMax((float) weatherJson.getJSONObject("main").getDouble("temp_max"))
                    .withTemp((float) weatherJson.getJSONObject("main").getDouble("temp"))
                    .withTempMin((float) weatherJson.getJSONObject("main").getDouble("temp_min"))
                    .withSpeed((float) weatherJson.getJSONObject("wind").getDouble("speed"))
                    .build();
        }

        return null;
    }



}
