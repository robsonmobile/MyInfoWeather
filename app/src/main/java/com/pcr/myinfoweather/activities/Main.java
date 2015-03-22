package com.pcr.myinfoweather.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.gson.GsonBuilder;
import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.models.LocationData;
import com.pcr.myinfoweather.models.UserLocation;
import com.pcr.myinfoweather.models.Weather;
import com.pcr.myinfoweather.models.WeatherData;
import com.pcr.myinfoweather.network.APIClient;
import com.pcr.myinfoweather.request.UserLocationRequest;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Paula Rosa on 04/03/2015.
 */
public class Main extends ActionBarActivity implements UserLocationRequest.IListenerLocation {

    private boolean isFinishedLocationRequest;
    private Bundle params;
    private GoogleApiClient mClient;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    private Location mCurrentLocation;
    private Callback<String> callback;
    private WeatherData weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserLocationRequest.getInstance(this).setListener(this);
        params = new Bundle();
    }

    @Override
    protected void onStart() {
        super.onStart();
        configureWeatherCallbackByLocation();
        UserLocationRequest.getInstance(this).connectClient();


    }

    @Override
    protected void onStop() {
        UserLocationRequest.getInstance(this).disconnectClient();
        callback = null;
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getLocationData() {

//            mCurrentLocation = fusedLocationProviderApi.getLastLocation(mClient);
//
//            float lat = (float) mCurrentLocation.getLatitude();
//            float lon = (float) mCurrentLocation.getLongitude();

        float lat = LocationData.getInstance().getLat();
        float lon = LocationData.getInstance().getLon();

            new APIClient().getWeatherByGPS().createWith(lat, lon, "metric", callback);
            System.out.println("log weatherAPIClient: " + weather);
    }

    private void configureWeatherCallbackByLocation() {
        callback = new Callback<String>() {

            @Override
            public void success(String s, Response response) {
                System.out.println("log callback " + s);
                weather = new GsonBuilder().create().fromJson(s, WeatherData.class);
                System.out.println("log weather gsonbuilder: " + weather.getMain().getTemp());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        };
    }

    @Override
    public void onFinishedLocationRequest(boolean isFinishedRequest) {
        if(isFinishedRequest) {
            getLocationData();
        }
    }
}
