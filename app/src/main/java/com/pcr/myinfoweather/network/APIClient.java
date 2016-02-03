package com.pcr.myinfoweather.network;

import com.google.gson.Gson;
import com.pcr.myinfoweather.BuildConfig;
import com.pcr.myinfoweather.interfaces.WeatherApi;
import com.pcr.myinfoweather.models.currentweather.Response;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Paula on 24/01/2015.
 */
public class APIClient {

    private static RestAdapter REST_ADAPTER;
    private WeatherApi weatherApi;
    private static Gson gson = new Gson();

    private static void createAdapterIfNeeded() {

        if(REST_ADAPTER == null) {
            REST_ADAPTER = new RestAdapter.Builder()

                    .setEndpoint(BuildConfig.BASE_URL + "/data/2.5")

                    .setLogLevel((BuildConfig.DEBUG) ?
                            RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)

                    .setConverter(new HandlerConverter())
                    .setClient(new OkClient())
                    .build();
        }
    }

    private void createWeatherApi() {
        weatherApi = REST_ADAPTER.create(WeatherApi.class);
    }

    public void requestWeatherByAddress(String city, String unit, String appId, Callback<Response> callback) {
        createWeatherApi();
        weatherApi.requestWeatherByAddress(city, unit, appId, callback);
    }

    public void requestWeatherLocation(double lat, double lon, String unit, String appId, Callback<String>callback) {
        createWeatherApi();
        weatherApi.requestWeatherByLocation(lat, lon, unit, appId, callback);
    }

    public APIClient() {
        createAdapterIfNeeded();
    }

    public getWeatherbyAddressInterface getWeatherByLocation() {
        return REST_ADAPTER.create(getWeatherbyAddressInterface.class);
    }

    public getWeatherByGPSInterface getWeatherByGPS() {
        return REST_ADAPTER.create(getWeatherByGPSInterface.class);
    }

    public interface getWeatherbyAddressInterface {
        @GET("/weather") void createWith(
                @Query("q") String cityName,
                @Query("units") String units,
                Callback<String> callback
        );

    }

    public interface getWeatherByGPSInterface {
        @GET("/weather") void createWith(
                @Query("lat") double latitude,
                @Query("lon") double longitude,
                @Query("units") String units,
                Callback<String> callback
        );
    }

}
