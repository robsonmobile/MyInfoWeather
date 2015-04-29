package com.pcr.myinfoweather.activities;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.gson.GsonBuilder;
import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.helpers.ConnectivityHelpers;
import com.pcr.myinfoweather.models.LocationData;
import com.pcr.myinfoweather.models.UserLocation;
import com.pcr.myinfoweather.models.WeatherData;
import com.pcr.myinfoweather.network.APIClient;
import com.pcr.myinfoweather.request.UserLocationRequest;
import com.pcr.myinfoweather.utils.CurrentDateAndTime;
import com.pcr.myinfoweather.utils.Intents;
import com.pcr.myinfoweather.utils.UserSessionManager;
import com.pcr.myinfoweather.utils.Validators;
import com.pcr.myinfoweather.utils.WeatherIconChooser;

import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Paula Rosa on 04/03/2015.
 */
public class Main extends BaseActivity implements UserLocationRequest.IListenerLocation {


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
    @InjectView(R.id.loadingMaxTemp) View loadingMaxTemp;
    @InjectView(R.id.loadingMinTemp) View loadingMinTemp;
    @InjectView(R.id.loadingImageWeather) View loadingWeatherImage;
    @InjectView(R.id.loadingWind) View loadingWind;
    @InjectView(R.id.loadingLocation) View loadingLocation;
    @InjectView(R.id.loadingCurrentDate) View loadingCurrentDate;

    // -----------------------------------------------------------------------------------
    // Weather Views
    // -----------------------------------------------------------------------------------
    @InjectView(R.id.weatherTempMax) TextView tempMax;
    @InjectView(R.id.weatherTempMin) TextView tempMin;
    @InjectView(R.id.weatherTitle) TextView weatherTitle;
    @InjectView(R.id.weatherIcon) ImageView weatherIcon;
    @InjectView(R.id.weatherWind) TextView weatherWind;
    @InjectView(R.id.weatherCurrentDate) TextView weatherCurrentDate;

    // -----------------------------------------------------------------------------------
    // TabBar Views
    // -----------------------------------------------------------------------------------
    @InjectView(R.id.weatherLocationText) TextView location;
    @InjectView(R.id.weatherCurrentDate) TextView currentDate;

    @Override
    protected int layoutToInflate() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean checkSessionOnStart() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            Intents.toPlaceholder(this);
            //show place holder
        }
//        if(UserLocationRequest.getInstance(this).isConnected()) {
//            getLocationData();
//        }

        UserLocationRequest.getInstance(this).connectClient();
    }

    @Override
    protected void onStop() {
        UserLocationRequest.getInstance(this).disconnectClient();
        callback = null;
        super.onStop();
    }

    private void getLocationData() {

        float lat = LocationData.getInstance().getLat();
        float lon = LocationData.getInstance().getLon();

        String unitType = UserSessionManager.getUnitTypePref(this);

        new APIClient().getWeatherByGPS().createWith(lat, lon, unitType, callback);
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
            //call placeholder
                stopLoading();
                Toast.makeText(Main.this, "failure call on callback", Toast.LENGTH_LONG).show();
                startActivity(Intents.toPlaceholder(Main.this));
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
        loadingLocation.setVisibility(View.VISIBLE);
        loadingCurrentDate.setVisibility(View.VISIBLE);

        //Temperature text and Icons Invisible
        tempMax.setVisibility(View.GONE);
        tempMin.setVisibility(View.GONE);
        weatherTitle.setVisibility(View.GONE);
        weatherIcon.setVisibility(View.GONE);
        weatherWind.setVisibility(View.GONE);
        location.setVisibility(View.GONE);
        currentDate.setVisibility(View.GONE);
    }

    private void stopLoading() {
        loadingMaxTemp.setVisibility(View.GONE);
        loadingMinTemp.setVisibility(View.GONE);
        loadingWeatherImage.setVisibility(View.GONE);
        loadingWind.setVisibility(View.GONE);
        loadingLocation.setVisibility(View.GONE);
        loadingCurrentDate.setVisibility(View.GONE);

        //Temperature text and Icons Invisible
        tempMax.setVisibility(View.VISIBLE);
        tempMin.setVisibility(View.VISIBLE);
        weatherTitle.setVisibility(View.VISIBLE);
        weatherIcon.setVisibility(View.VISIBLE);
        weatherWind.setVisibility(View.VISIBLE);
        location.setVisibility(View.VISIBLE);
        currentDate.setVisibility(View.VISIBLE);
    }

    private void setWeatherConditionsOnViews() {

        int weatherCode = weather.getWeather().get(0).getId();

        weatherCurrentDate.setText(CurrentDateAndTime.getInstance(this).getCurrentDate());
        tempMax.setText(Validators.formatDecimal(weather.getMain().getTempMax()) + getTemperaturePrefs());
        tempMin.setText(Validators.formatDecimal(weather.getMain().getTempMin()) + getTemperaturePrefs());
        weatherWind.setText(Validators.formatDecimal(weather.getWind().getSpeed()));
        weatherTitle.setText(weather.getWeather().get(0).getDescription());
        weatherIcon.setImageResource(new WeatherIconChooser(weatherCode).getImageResource());

        location.setText(UserLocationRequest.getInstance(this).getLocationByGPS());


    }

    private String getTemperaturePrefs() {
        if(UserSessionManager.hasTemperaturePref(this)) {
            return UserSessionManager.getSavedTemperaturePref(this);
        } else {
            return "°C"; //posteriormente retornar� a unidade dependendo do lugar
        }

    }

}
