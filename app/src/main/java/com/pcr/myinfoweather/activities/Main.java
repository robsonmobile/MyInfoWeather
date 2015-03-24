package com.pcr.myinfoweather.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.gson.GsonBuilder;
import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.helpers.ConnectivityHelpers;
import com.pcr.myinfoweather.models.LocationData;
import com.pcr.myinfoweather.models.WeatherData;
import com.pcr.myinfoweather.network.APIClient;
import com.pcr.myinfoweather.request.UserLocationRequest;

import java.text.DecimalFormat;

import butterknife.InjectView;
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

    // -----------------------------------------------------------------------------------
    // Loading Views
    // -----------------------------------------------------------------------------------
    @InjectView(R.id.loadingMaxTemp) ProgressBar loadingMaxTemp;
    @InjectView(R.id.loadingMinTemp) ProgressBar loadingMinTemp;
    @InjectView(R.id.loadingImageWeather) ProgressBar loadingWeatherImage;
    @InjectView(R.id.loadingWind) ProgressBar loadingWind;

    // -----------------------------------------------------------------------------------
    // Weather Views
    // -----------------------------------------------------------------------------------
    @InjectView(R.id.weatherTempMax) TextView tempMax;
    @InjectView(R.id.weatherTempMin) TextView tempMin;
    @InjectView(R.id.weatherTitle) TextView weatherTitle;
    @InjectView(R.id.weatherIcon) ImageView weatherIcon;
    @InjectView(R.id.weatherWind) TextView weatherWind;


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
    protected void onResume() {
        super.onResume();

        startLoading();

        if(!ConnectivityHelpers.hasConnectivity(this)) {
            stopLoading();
            //show place holder
        }
    }

    @Override
    protected void onStop() {
        UserLocationRequest.getInstance(this).disconnectClient();
        callback = null;
        super.onStop();
    }

    private void getUserPreferences() {
        //SharedPreferencesData.getInstance(this).
    }

    private void getLocationData() {

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
                stopLoading();
                setWeatherConditionsOnViews();
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

    private void startLoading() {
        //Loading Visible
        loadingMaxTemp.setVisibility(View.VISIBLE);
        loadingMinTemp.setVisibility(View.VISIBLE);
        loadingWeatherImage.setVisibility(View.VISIBLE);
        loadingWind.setVisibility(View.VISIBLE);

        //Temperature text and Icons Invisible
        tempMax.setVisibility(View.GONE);
        tempMin.setVisibility(View.GONE);
        weatherTitle.setVisibility(View.GONE);
        weatherIcon.setVisibility(View.GONE);
        weatherWind.setVisibility(View.GONE);
    }

    private void stopLoading() {
        loadingMaxTemp.setVisibility(View.GONE);
        loadingMinTemp.setVisibility(View.GONE);
        loadingWeatherImage.setVisibility(View.GONE);
        loadingWind.setVisibility(View.GONE);

        //Temperature text and Icons Invisible
        tempMax.setVisibility(View.VISIBLE);
        tempMin.setVisibility(View.VISIBLE);
        weatherTitle.setVisibility(View.VISIBLE);
        weatherIcon.setVisibility(View.VISIBLE);
        weatherWind.setVisibility(View.VISIBLE);
    }

    private void setWeatherConditionsOnViews() {
        String maxTempFormat = new DecimalFormat("##.#").format(weather.getMain().getTempMax());

    }

}
