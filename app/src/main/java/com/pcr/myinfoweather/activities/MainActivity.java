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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.dialogs.ConnectionFailureDialog;
import com.pcr.myinfoweather.interfaces.IDialogConnectionFailure;
import com.pcr.myinfoweather.models.LocationData;
import com.pcr.myinfoweather.models.WeatherData;
import com.pcr.myinfoweather.request.AppHttpClient;
import com.pcr.myinfoweather.response.WeatherHttpResponseHandler;
import com.pcr.myinfoweather.utils.Constants;
import com.google.gson.GsonBuilder;
import com.pcr.myinfoweather.utils.SharedPreferencesData;

import org.apache.http.client.HttpResponseException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ActionBarActivity implements View.OnClickListener, GooglePlayServicesClient.ConnectionCallbacks,
                                                               GooglePlayServicesClient.OnConnectionFailedListener,
                                                               IDialogConnectionFailure {

    private WeatherData weatherData;

    /******Ui Components******/
    private TextView tempMedium;
    private TextView tempMax;
    private TextView tempMin;
    private TextView weatherTitle;
    private TextView weatherLocationCity;
    private EditText cityField;
    private ProgressBar loadingMediumTemp;
    private ProgressBar loadingMaxTemp;
    private ProgressBar loadingMinTemp;
    private Button btnSearch;
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
    private String state;
    private String country;
    private ConnectionFailureDialog dialog;
    private int requestType;
    private String cityTypedSearch;
    private float weatherLat;
    private float weatherLon;
    private static final int SEARCH_FLAG_GEO = 0;
    private static final int SEARCH_FLAG_CITY = 1;
    private LocationData locationData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        btnSearch.setOnClickListener(this);
        cityField.setOnClickListener(this);
        cityField.clearFocus();
        btnSearch.requestFocus();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }

        //Getting the Preference for Temperature
        SharedPreferencesData prefs = new SharedPreferencesData(this);
        tempPreference = prefs.getTempPreferenceData();
        mLocationClient = new LocationClient(this, this, this);

        locationData = new LocationData();

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestType = Constants.PATH_FOR_GEOLOCATION;
        cityField.clearFocus();
        btnSearch.requestFocus();
        startLoading();
    }

    private void setupViews() {
        weatherTitle = (TextView) findViewById(R.id.weatherTitle);
        weatherLocationCity = (TextView) findViewById(R.id.weatherLocationText);
        cityField = (EditText) findViewById(R.id.cityField);
        tempMedium = (TextView) findViewById(R.id.weatherTemp);
        tempMax = (TextView) findViewById(R.id.weatherTempMax);
        tempMin = (TextView) findViewById(R.id.weatherTempMin);
        loadingMediumTemp = (ProgressBar) findViewById(R.id.loadingMediumTemp);
        loadingMaxTemp = (ProgressBar) findViewById(R.id.loadingMaxTemp);
        loadingMinTemp = (ProgressBar) findViewById(R.id.loadingMinTemp);
        btnSearch = (Button) findViewById(R.id.btnSearchCity);
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
        weatherLat = weatherData.getCoord().getLat();
        weatherLon = weatherData.getCoord().getLon();

        locationData.setLat(weatherLat);
        locationData.setLon(weatherLon);

        getLocation(locationData.getLat(), locationData.getLon());
        System.out.println("log getlat: " + locationData.getLat());
        System.out.println("log getlon: " + locationData.getLon());
        System.out.println("log getcity: " + locationData.getCity());
        System.out.println("log getstate: " + locationData.getState());
        System.out.println("log getcountry: " + locationData.getCountry());


    }

    private void setWeatherData() {

        //setar os dados do modelo (e nao de cada valor)
        tempMedium.setText(mediumTempText);
        tempMax.setText(maxTempText);
        tempMin.setText(minTempText);
        weatherTitle.setText(weatherTitleText);

        String completeLocation = locationData.getCity() + ", " + locationData.getState() +
                ", " + locationData.getCountry();

        weatherLocationCity.setText(completeLocation);
        System.out.println("log values: " + completeLocation);
    }

    private String geoLocationPath(float latitude, float longitude) {
        String latitudeValue = String.valueOf(latitude);
        String longitudeValue = String.valueOf(longitude);
        return Constants.LATITUDE_PATH + latitudeValue + Constants.LONGITUDE_PATH + longitudeValue;
    }

    private String cityLocationPath(String city) {
        URI uri = null;
        try {
            uri = new URI(city.replaceAll(" ", "%20"));
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("log uri: " + uri);

        if(uri != null) {
            return Constants.LOCAL_PATH + uri.toString() + Constants.COMMA_CHARACTER;
        } else {
            Toast.makeText(this, R.string.toast_invalid_city_name, Toast.LENGTH_LONG).show();
            return "";
        }
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

    private void getCurrentLocation() {
        if(mLocationClient.isConnected()) {
            mCurrentLocation = mLocationClient.getLastLocation();
            latitude = (float) mCurrentLocation.getLatitude();
            longitude = (float) mCurrentLocation.getLongitude();

            locationData.setLat(latitude);
            locationData.setLon(longitude);

            System.out.println("log currentlocation: " + "lat: " + latitude + " long: " + longitude);

        }
    }



    private void getLocation(float cityLat, float cityLong) {
        state = null;
        city = null;
        country = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(cityLat, cityLong, 1);
            if (list != null && list.size() > 0) {
                Address address = list.get(0);
                state = address.getAdminArea();
                city = address.getSubAdminArea();
                country = address.getCountryCode();

                locationData.setCity(city);
                locationData.setState(state);
                locationData.setCountry(country);
            }
        } catch (IOException e) {
                    e.printStackTrace();
        }

        System.out.println("log city data: " + city + " state: " + state + " country: " + country);


    }

    private String validateLocationString(String text) {
        String value;
        if(text == null) {
            value = " ";
        } else {
            value = text;
        }
        System.out.println("log string value: " + value);
        return value;

    }

    @Override
    public void onConnected(Bundle bundle) {

        getCurrentLocation();
        performRequest(requestType);
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
        cityField.clearFocus();
        btnSearch.requestFocus();
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
            requestType = Constants.PATH_FOR_CITY;
            cityTypedSearch = cityField.getText().toString();
            performRequest(requestType);
        } else if(v.getId() == R.id.cityField) {
            cityField.requestFocus();
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
                path = cityLocationPath(cityTypedSearch);
                break;
        }

        AppHttpClient.get(path, null, new WeatherHttpResponseHandler.ResourceParserHandler() {
            @Override
            public void onSuccess(Object resource) {
                System.out.println("log resource onsuccess" + resource);

               // HttpResponseException exception = (HttpResponseException) throwable;


                weatherData = new GsonBuilder().create()
                        .fromJson(resource.toString(), WeatherData.class);

                getWeatherData();
                stopLoading();
                setWeatherData();
            }

            @Override
            public void onFailure(Throwable e) {
                System.out.println("log resource failure");
                dialog = new ConnectionFailureDialog();
                dialog.show(getSupportFragmentManager(), "ConnectionFailureDialog");
            }

            @Override
            public void onFailure(Throwable e, String errorMessage) {
                dialog = new ConnectionFailureDialog();
                dialog.show(getSupportFragmentManager(), "ConnectionFailureDialog");
            }

            @Override
            public void onFailure(String errorMessage) {
                System.out.println("log erro onfailure: " + errorMessage);
                if(errorMessage.equals("404")) {
                    stopLoading();
                    Toast.makeText(MainActivity.this, "Wrong City please try again", Toast.LENGTH_LONG).show();
                    cityField.setText("");
                    cityField.setHint("Type the city name");
                }

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
        dialog.dismiss();
    }

    @Override
    public void setMessage(DialogFragment dialog) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(dialog != null) {
            dialog.dismiss();
        }

    }
}
