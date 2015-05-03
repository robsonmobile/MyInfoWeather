package com.pcr.myinfoweather.activities;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.GsonBuilder;
import com.pcr.myinfoweather.R;

import com.pcr.myinfoweather.helpers.ConnectivityHelpers;
import com.pcr.myinfoweather.models.currentweather.WeatherData;
import com.pcr.myinfoweather.models.user.User;
import com.pcr.myinfoweather.models.user.UserAdress;
import com.pcr.myinfoweather.network.APIClient;
import com.pcr.myinfoweather.network.GoogleClient;
import com.pcr.myinfoweather.network.WeatherParse;
import com.pcr.myinfoweather.request.UserLocation;
import com.pcr.myinfoweather.utils.Constants;
import com.pcr.myinfoweather.utils.Intents;
import com.pcr.myinfoweather.utils.UserSessionManager;
import com.pcr.myinfoweather.utils.Validators;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Paula Rosa on 04/03/2015.
 */
public class Main extends BaseActivity implements GoogleClient.IListenerLocation, View.OnFocusChangeListener {

    private Bundle params;
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
    @InjectView(R.id.weatherContainer) LinearLayout weatherContainerLayout;

    // -----------------------------------------------------------------------------------
    // Search Views
    // -----------------------------------------------------------------------------------
    @InjectView(R.id.searchAddressField) EditText searchAddressField;
    @InjectView(R.id.searchAddressButton) Button searchAddressButton;

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


        searchAddressField.setOnFocusChangeListener(this);

        GoogleClient.getInstance().setListener(this);
        GoogleClient.getInstance().getGoogleApiClient(this);

        params = new Bundle();

    }

    @Override
    protected void onStart() {
        super.onStart();
        configureWeatherCallbackByLocation();
        GoogleClient.getInstance().connectClient();
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
    }

    @Override
    protected void onStop() {
        GoogleClient.getInstance().disconnectClient();
        callback = null;
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if(weatherContainerLayout.getVisibility() == View.GONE) {
            weatherContainerLayout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.searchAddressButton) void searchAddress() {


    }

    private String getTypedAddress() {
        return searchAddressField.getText().toString();
    }

    private java.util.List<android.location.Address> performAddressesList() {
        return UserLocation.getInstance(this).getAddress(getTypedAddress(), Constants.NUMBER_OF_OCCURRENCES);
    }

    private String getUserPreferences() {
        return UserSessionManager.getUnitTypePref(this);
    }

    private void performRequestByLocation() {
        double lat = UserLocation.getInstance(this).getGPSInformation().getLatitude();
        double lon = UserLocation.getInstance(this).getGPSInformation().getLongitude();

        new APIClient().getWeatherByGPS().createWith(lat, lon, getUserPreferences(), callback);
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
                parseData();
            }

            @Override
            public void failure(RetrofitError error) {
                //fazer exception para erro// dispara mensagem ou dialog
            //call placeholder
                stopLoading();
                Toast.makeText(Main.this, "failure call on callback", Toast.LENGTH_LONG).show();
                startActivity(Intents.toPlaceholder(Main.this));
            }
        };
    }

    @Override
    public void onGoogleClientConnected(boolean isFinishedRequest) {
        if(isFinishedRequest) {
            performRequestByLocation();
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

    private void setViews(User user) {

        weatherCurrentDate.setText(user.getDate());
        tempMax.setText(getMaxTemperature(user));
        tempMin.setText(getMinTemperature(user));
        weatherWind.setText(getWindSpeed(user));
        weatherTitle.setText(user.getTitle());
        weatherIcon.setImageResource(user.getImage());
        location.setText(user.getAddress().getCompleteAdress());


    }

    private String getWindSpeed(User user) {
        return Validators.formatDecimal(user.getWindSpeed());
    }

    private String getMinTemperature(User user) {
        return Validators.formatDecimal(user.getTemp_min()) + getTemperaturePrefs();
    }

    private String getMaxTemperature(User user) {
        return Validators.formatDecimal(user.getTemp_max()) + getTemperaturePrefs();
    }

    private void parseData() {
        com.pcr.myinfoweather.models.currentweather.Location userLocation = WeatherParse.parseGeoLocation(getGeoLocation());
        UserAdress userAddress = WeatherParse.parseAddress(getAddress());
        User user = WeatherParse.parseWeather(this, weather, userLocation, userAddress);

        setViews(user);
    }

    private Location getGeoLocation() {
        return UserLocation.getInstance(this).getGPSInformation();
    }

    private ArrayList<String> getAddress() {
        com.pcr.myinfoweather.models.currentweather.Location userLocation = WeatherParse.parseGeoLocation(getGeoLocation());
        return UserLocation.getInstance(this).getAddress(userLocation);
    }

    private String getTemperaturePrefs() {
        if(UserSessionManager.hasTemperaturePref(this)) {
            return UserSessionManager.getSavedTemperaturePref(this);
        } else {
            return "°C"; //posteriormente retornar� a unidade dependendo do lugar
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if(v.getId() == R.id.searchAddressField && hasFocus) {
            weatherContainerLayout.setVisibility(View.GONE);
        }
    }
}
