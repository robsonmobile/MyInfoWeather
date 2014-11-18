package com.pcr.myinfoweather.activities;


import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.models.weather.WeatherData;
import com.pcr.myinfoweather.request.AppHttpClient;
import com.pcr.myinfoweather.response.WeatherHttpResponseHandler;
import com.pcr.myinfoweather.utils.Constants;
import com.google.gson.GsonBuilder;
import com.pcr.myinfoweather.utils.SharedPreferencesData;
import java.util.Locale;

public class MainActivity extends ActionBarActivity implements View.OnClickListener, GooglePlayServicesClient.ConnectionCallbacks,
                                                               GooglePlayServicesClient.OnConnectionFailedListener {

    private WeatherData weatherData;
    private TextView tempMedium;
    private TextView tempMax;
    private TextView tempMin;
    private TextView weatherTitle;
    private ProgressBar loadingLocation;
    private ImageView locationIcon;
    private float mediumTemp;
    private float maxTemp;
    private float minTemp;
    private String mediumTempText;
    private String maxTempText;
    private String minTempText;
    private static final String UNITY_TEMP_CELSIUS = " °C";
    private static final String UNITY_TEMP_FAHRENHEIT = " °F";
    int tempPreference;
    private LocationClient mLocationClient;
    private Location mCurrentLocation;
    private double latitude;
    private double longitude;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.btnSearchCity);
        btn.setOnClickListener(this);

        //Getting the Preference for Temperature
        SharedPreferencesData prefs = new SharedPreferencesData(this);
        tempPreference = prefs.getTempPreferenceData();
        mLocationClient = new LocationClient(this, this, this);

        setupViews();
        startLoading();
        getLocation();
    }

    private void setupViews() {
        weatherTitle = (TextView) findViewById(R.id.weatherTitle);
        tempMedium = (TextView) findViewById(R.id.weatherTemp);
        tempMax = (TextView) findViewById(R.id.weatherTempMax);
        tempMin = (TextView) findViewById(R.id.weatherTempMin);
        loadingLocation = (ProgressBar) findViewById(R.id.loadingLocation);
        locationIcon = (ImageView) findViewById(R.id.location_icon);
    }

    private void getWeatherData() {
        mediumTemp = weatherData.getMain().getTemp();
        maxTemp = weatherData.getMain().getTempMax();
        minTemp = weatherData.getMain().getTempMin();

        mediumTemp = convertTemperature(tempPreference, mediumTemp);
        maxTemp = convertTemperature(tempPreference, maxTemp);
        minTemp = convertTemperature(tempPreference, minTemp);

        if(tempPreference == Constants.TEMP_CELSIUS) {
            mediumTempText = String.valueOf(mediumTemp) + UNITY_TEMP_CELSIUS;
            maxTempText = String.valueOf(maxTemp)  + UNITY_TEMP_CELSIUS;
            minTempText = String.valueOf(minTemp)  + UNITY_TEMP_CELSIUS;
        } else {
            mediumTempText = String.valueOf(mediumTemp) + UNITY_TEMP_FAHRENHEIT;
            maxTempText = String.valueOf(maxTemp)  + UNITY_TEMP_FAHRENHEIT;
            minTempText = String.valueOf(minTemp)  + UNITY_TEMP_FAHRENHEIT;
        }


    }

    private void setWeatherData() {

        tempMedium.setText(String.valueOf(mediumTemp));
        tempMax.setText(String.valueOf(maxTemp));
        tempMin.setText(String.valueOf(minTemp));
    }

    private float convertTemperature(int type, float temperature) {
        float calculatedTemp = calcTemp(type, temperature);
        calculatedTemp = Float.valueOf(formatFloatNumber(calculatedTemp));
        return calculatedTemp;
    }


    private float calcTemp (int type, float temp) {
        float finalTemp = 0;
        switch (type) {
            case Constants.TEMP_CELSIUS:
                finalTemp = (temp - 273);
                break;
            case Constants.TEMP_FAHRENHEIT:
                finalTemp = ((1.8f * (temp - 273)) + 32);
                break;
        }
        return finalTemp;
    }


    private void getLocation() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000 * 10);


    }

    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation = mLocationClient.getLastLocation();
        latitude = mCurrentLocation.getLatitude();
        longitude = mCurrentLocation.getLongitude();
        System.out.println("log long: " + longitude);
        System.out.println("log lat: " + latitude);
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        mLocationClient.disconnect();
        super.onStop();

    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSearchCity){
            performRequest();
        }
    }

    private void performRequest() {
        AppHttpClient.get(Constants.LOCAL_PATH, null, new WeatherHttpResponseHandler.ResourceParserHandler() {
            @Override
            public void onSuccess(Object resource) {
                System.out.println("log resource" + resource);
                weatherData = new GsonBuilder().create()
                        .fromJson(resource.toString(), WeatherData.class);

                System.out.println("log resource 2: " + resource.toString());
                System.out.println("log: weather data city : " + weatherData.getName());
                System.out.println("log: weather data temp : " + weatherData.getMain().getTemp());
                System.out.println("log: weather data lat : " + weatherData.getCoord().getLat());
                System.out.println("log: weather data country : " + weatherData.getSys().getCountry());
                System.out.println("log: weather data Id : " + weatherData.getId());
                System.out.println("log maxtemp: " + weatherData.getMain().getTempMax());
                System.out.println("log mintemp: " + weatherData.getMain().getTempMin());

                getWeatherData();
                setWeatherData();
            }

            @Override
            public void onFailure(Throwable e) {
                System.out.println("log resource failure");
            }

            @Override
            public void onFailure(Throwable e, String errorMessage) {

            }
        });
    }


    private String formatFloatNumber(float number) {
        return String.format(Locale.US, "%.1f", number);
    }

    private void startLoading() {
        loadingLocation.setVisibility(View.VISIBLE);
        locationIcon.setVisibility(View.GONE);
    }

    private void stopLoading() {
        loadingLocation.setVisibility(View.GONE);
        locationIcon.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
