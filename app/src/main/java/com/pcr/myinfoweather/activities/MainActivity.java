package com.pcr.myinfoweather.activities;


import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.dialogs.ConnectionFailureDialog;
import com.pcr.myinfoweather.interfaces.IDialogConnectionFailure;
import com.pcr.myinfoweather.models.WeatherData;
import com.pcr.myinfoweather.request.AppHttpClient;
import com.pcr.myinfoweather.response.WeatherHttpResponseHandler;
import com.pcr.myinfoweather.utils.Constants;
import com.google.gson.GsonBuilder;
import com.pcr.myinfoweather.utils.SharedPreferencesData;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ActionBarActivity implements View.OnClickListener, GooglePlayServicesClient.ConnectionCallbacks,
                                                               GooglePlayServicesClient.OnConnectionFailedListener,
                                                               IDialogConnectionFailure {

    private WeatherData weatherData;
    private TextView tempMedium;
    private TextView tempMax;
    private TextView tempMin;
    private TextView weatherTitle;
    private TextView weatherLocationCity;
    private ImageView maxTempIcon;
    private ImageView minTempIcon;
    private ProgressBar loadingMediumTemp;
    private ProgressBar loadingMaxTemp;
    private ProgressBar loadingMinTemp;
    private String weatherCity;
    private String weatherCountry;
    private String weatherTitleText;
    private float mediumTemp;
    private float maxTemp;
    private float minTemp;
    private String mediumTempText;
    private String maxTempText;
    private String minTempText;
    private static final String UNITY_TEMP_CELSIUS = " °C";
    private static final String UNITY_TEMP_FAHRENHEIT = " °F";
    private int tempPreference;
    private LocationClient mLocationClient;
    private Location mCurrentLocation;
    private float latitude;
    private float longitude;
    private LocationRequest mLocationRequest;
    private String city;
    private ConnectionFailureDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.btnSearchCity);
        btn.setOnClickListener(this);

        setupViews();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }

        //Getting the Preference for Temperature
        SharedPreferencesData prefs = new SharedPreferencesData(this);
        tempPreference = prefs.getTempPreferenceData();
        mLocationClient = new LocationClient(this, this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLoading();
    }

    private void setupViews() {
        weatherTitle = (TextView) findViewById(R.id.weatherTitle);
        weatherLocationCity = (TextView) findViewById(R.id.weatherLocationText);
        tempMedium = (TextView) findViewById(R.id.weatherTemp);
        tempMax = (TextView) findViewById(R.id.weatherTempMax);
        tempMin = (TextView) findViewById(R.id.weatherTempMin);
        maxTempIcon = (ImageView) findViewById(R.id.weatherMaxTempIcon);
        minTempIcon = (ImageView) findViewById(R.id.weatherMinTempIcon);
        loadingMediumTemp = (ProgressBar) findViewById(R.id.loadingMediumTemp);
        loadingMaxTemp = (ProgressBar) findViewById(R.id.loadingMaxTemp);
        loadingMinTemp = (ProgressBar) findViewById(R.id.loadingMinTemp);
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

        weatherTitleText = weatherData.getWeather().get(0).getMain();
        weatherCity = weatherData.getName();
        weatherCountry = weatherData.getSys().getCountry();



    }

    private void setWeatherData() {

        tempMedium.setText(String.valueOf(mediumTempText));
        tempMax.setText(String.valueOf(maxTempText));
        tempMin.setText(String.valueOf(minTempText));
        weatherTitle.setText(weatherTitleText);

        String completeLocation = weatherCity + ", " + city + " (" + weatherCountry + ")";
        weatherLocationCity.setText(completeLocation);
    }

    private String geoLocationPath(float latitude, float longitude) {
        String latitudeValue = String.valueOf(latitude);
        String longitudeValue = String.valueOf(longitude);
        return Constants.LATITUDE_PATH + latitudeValue + Constants.LONGITUDE_PATH + longitudeValue;
    }

    private String cityLocationPath(String city) {
        return city;
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

    @Override
    public void onConnected(Bundle bundle) {
        if(mLocationClient.isConnected()) {
            mCurrentLocation = mLocationClient.getLastLocation();
            latitude = (float) mCurrentLocation.getLatitude();
            longitude = (float) mCurrentLocation.getLongitude();
        }

        System.out.println("log long: " + longitude);
        System.out.println("log lat: " + latitude);


        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        city = null;
        try {
            List<Address> list = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);

            if(list != null && list.size() > 0) {
                Address address  = list.get(0);
                city = address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        performRequest(Constants.PATH_FOR_GEOLOCATION);
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
            //performRequest(Constants.PATH_FOR_GEOLOCATION);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setExitTransition(new Explode());
            }
            startLoading();
            mLocationClient.connect();
        }
    }

    private void performRequest(int requestType) {
        //Make the request based on the request type
        String path = null;
        switch (requestType) {
            case Constants.PATH_FOR_GEOLOCATION:
                path = geoLocationPath(latitude, longitude);
                break;
            case Constants.PATH_FOR_CITY:
                path = cityLocationPath("");
                break;
        }


        AppHttpClient.get(path, null, new WeatherHttpResponseHandler.ResourceParserHandler() {
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
                stopLoading();
                setWeatherData();

                dialog = new ConnectionFailureDialog();
                dialog.show(getSupportFragmentManager(), "ConnectionFailureDialog");
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
        //Loading Visible
        loadingMediumTemp.setVisibility(View.VISIBLE);
        loadingMaxTemp.setVisibility(View.VISIBLE);
        loadingMinTemp.setVisibility(View.VISIBLE);

        //Temperature text and Icons Invisible
        tempMedium.setVisibility(View.GONE);
        tempMax.setVisibility(View.GONE);
        tempMin.setVisibility(View.GONE);

    }

    private void stopLoading() {
        loadingMediumTemp.setVisibility(View.GONE);
        loadingMaxTemp.setVisibility(View.GONE);
        loadingMinTemp.setVisibility(View.GONE);

        //Temperature text and Icons Invisible
        tempMedium.setVisibility(View.VISIBLE);
        tempMax.setVisibility(View.VISIBLE);
        tempMin.setVisibility(View.VISIBLE);
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


    @Override
    public void onPositiveClick(DialogFragment dialog) {
        Toast.makeText(this, "positive button clicked" , Toast.LENGTH_LONG).show();
        System.out.println("log dialog: OK");

    }

    @Override
    public void onNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void setMessage(DialogFragment dialog) {

    }
}
