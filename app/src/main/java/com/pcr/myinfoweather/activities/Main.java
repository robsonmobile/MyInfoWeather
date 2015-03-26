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
import com.pcr.myinfoweather.utils.CurrentDateAndTime;
import com.pcr.myinfoweather.utils.UserSessionManager;
import com.pcr.myinfoweather.utils.Validators;
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
    @InjectView(R.id.loadingMaxTemp) View loadingMaxTempContainer;
    @InjectView(R.id.loadingMinTemp) View loadingMinTemp;
    @InjectView(R.id.loadingImageWeather) View loadingWeatherImage;
    @InjectView(R.id.loadingWind) View loadingWind;

    // -----------------------------------------------------------------------------------
    // Weather Views
    // -----------------------------------------------------------------------------------
    @InjectView(R.id.weatherTempMax) TextView tempMax;
    @InjectView(R.id.weatherTempMin) TextView tempMin;
    @InjectView(R.id.weatherTitle) TextView weatherTitle;
    @InjectView(R.id.weatherIcon) ImageView weatherIcon;
    @InjectView(R.id.weatherWind) TextView weatherWind;
    @InjectView(R.id.weatherCurrentDate) TextView weatherCurrentDate;


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
            //show place holder
        }
        if(UserLocationRequest.getInstance(this).isConnected()) {
            getLocationData();
        }
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
        loadingMaxTempContainer.setVisibility(View.VISIBLE);
//        loadingMinTemp.setVisibility(View.VISIBLE);
//        loadingWeatherImage.setVisibility(View.VISIBLE);
//        loadingWind.setVisibility(View.VISIBLE);

        //Temperature text and Icons Invisible
//        tempMax.setVisibility(View.GONE);
//        tempMin.setVisibility(View.GONE);
//        weatherTitle.setVisibility(View.GONE);
//        weatherIcon.setVisibility(View.GONE);
//        weatherWind.setVisibility(View.GONE);
    }

    private void stopLoading() {
        loadingMaxTempContainer.setVisibility(View.GONE);
//        loadingMinTemp.setVisibility(View.GONE);
//        loadingWeatherImage.setVisibility(View.GONE);
//        loadingWind.setVisibility(View.GONE);

        //Temperature text and Icons Invisible
//        tempMax.setVisibility(View.VISIBLE);
//        tempMin.setVisibility(View.VISIBLE);
//        weatherTitle.setVisibility(View.VISIBLE);
//        weatherIcon.setVisibility(View.VISIBLE);
//        weatherWind.setVisibility(View.VISIBLE);
    }

    private void setWeatherConditionsOnViews() {

        weatherCurrentDate.setText(CurrentDateAndTime.getInstance(this).getCurrentDate());
        tempMax.setText(Validators.formatDecimal(weather.getMain().getTempMax()) + getTemperaturePrefs());
        tempMax.setText(Validators.formatDecimal(weather.getMain().getTempMin()) + getTemperaturePrefs());
        weatherWind.setText(Validators.formatDecimal(weather.getWind().getSpeed()));
        weatherTitle.setText(weather.getWeather().get(0).getDescription());

    }

    private String getTemperaturePrefs() {
        if(UserSessionManager.hasTemperaturePref(this)) {
            return UserSessionManager.getSavedTemperaturePref(this);
        } else {
            return "ºC"; //posteriormente retornará a unidade dependendo do lugar
        }

    }

}
