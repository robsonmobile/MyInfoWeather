package com.pcr.myinfoweather.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pcr.myinfoweather.BuildConfig;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Paula on 24/01/2015.
 */
public class APIClient {

    private static RestAdapter REST_ADAPTER;
    private static Gson gson = new Gson();

    private static void createAdapterIfNeeded() {

        Gson gson = new GsonBuilder()

                .create();

        if(REST_ADAPTER == null) {
            REST_ADAPTER = new RestAdapter.Builder()

                    .setEndpoint(BuildConfig.BASE_URL + "/data/2.5")

                    .setLogLevel((BuildConfig.DEBUG) ?
                            RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)

                    .setConverter(new GsonConverter(gson))
                    .setClient(new OkClient())
                    .build();
        }
    }

    public APIClient() {
        createAdapterIfNeeded();
    }

    public getWeatherbyLocationInterface getWeatherByLocation() {
        return REST_ADAPTER.create(getWeatherbyLocationInterface.class);
    }

    public getWeatherByGPSInterface getWeatherByGPS() {
        return REST_ADAPTER.create(getWeatherByGPSInterface.class);
    }

    public interface getWeatherbyLocationInterface {
        @GET("/weather") void createWith(
                @Query("q") String cityName,
                @Query("units") String units,
                Callback<String> callback
        );

    }

    public interface getWeatherByGPSInterface {
        @GET("/weather") void createWith(
                @Query("lat") float latitude,
                @Query("lon") float longitude,
                @Query("units") String units,
                Callback<String> callback
        );
    }


}
