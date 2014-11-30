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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.dialogs.AlertDialogBuilder;
import com.pcr.myinfoweather.dialogs.OptionsDialogBuilder;
import com.pcr.myinfoweather.interfaces.IDialog;
import com.pcr.myinfoweather.models.LocationData;
import com.pcr.myinfoweather.models.WeatherData;
import com.pcr.myinfoweather.request.AppHttpClient;
import com.pcr.myinfoweather.response.WeatherHttpResponseHandler;
import com.pcr.myinfoweather.utils.Constants;
import com.google.gson.GsonBuilder;
import com.pcr.myinfoweather.utils.SharedPreferencesData;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ActionBarActivity implements View.OnClickListener, GooglePlayServicesClient.ConnectionCallbacks,
                                                               GooglePlayServicesClient.OnConnectionFailedListener,
        IDialog {

    private WeatherData weatherData;

    /******Ui Components******/
    private TextView tempMax;
    private TextView tempMin;
    private TextView weatherTitle;
    private TextView weatherLocationCity;
    private TextView weatherWind;
    private EditText cityField;
    private ProgressBar loadingMaxTemp;
    private ProgressBar loadingMinTemp;
    private ProgressBar loadingWeatherImage;
    private ProgressBar loadingWind;
    private Button btnSearch;
    private ImageView weatherIcon;
    private String weatherTitleText;
    private float maxTemp;
    private float minTemp;
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
    private AlertDialogBuilder dialogAlert;
    private OptionsDialogBuilder dialogOptions;
    private int requestType;
    private String cityTypedSearch;
    private static final int SEARCH_FLAG_GEO = 0;
    private static final int SEARCH_FLAG_CITY = 1;
    private LocationData locationData;
    private CharSequence[] cityOptionsDialog;
    private int dialogPosition;
    private Address address;
    private List<String> listCity;
    private List<Address> listCityPosition;
    private List<Address> listLocation;


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
        dialogAlert = new AlertDialogBuilder();
        dialogOptions = new OptionsDialogBuilder();

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
        tempMax = (TextView) findViewById(R.id.weatherTempMax);
        tempMin = (TextView) findViewById(R.id.weatherTempMin);
        loadingMaxTemp = (ProgressBar) findViewById(R.id.loadingMaxTemp);
        loadingMinTemp = (ProgressBar) findViewById(R.id.loadingMinTemp);
        loadingWeatherImage = (ProgressBar) findViewById(R.id.loadingImageWeather);
        loadingWind = (ProgressBar) findViewById(R.id.loadingWind);
        btnSearch = (Button) findViewById(R.id.btnSearchCity);
        weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
        weatherWind = (TextView) findViewById(R.id.weatherWind);
    }

    private void getWeatherData() {
        maxTemp = convertTemperature(tempPreference, weatherData.getMain().getTempMax());
        minTemp = convertTemperature(tempPreference, weatherData.getMain().getTempMin());

        if(tempPreference == Constants.TEMP_CELSIUS) {
            tempMax.setText(String.valueOf(maxTemp)  + UNITY_TEMP_CELSIUS);
            tempMin.setText(String.valueOf(minTemp)  + UNITY_TEMP_CELSIUS);
        } else {
            tempMax.setText(String.valueOf(maxTemp)  + UNITY_TEMP_FAHRENHEIT);
            tempMin.setText(String.valueOf(minTemp)  + UNITY_TEMP_FAHRENHEIT);
        }

        String completeLocation = locationData.getCity() + ", " + locationData.getState() +
                ", " + locationData.getCountry();

        weatherTitle.setText(weatherData.getWeather().get(0).getMain());
        weatherLocationCity.setText(completeLocation);
        weatherWind.setText(String.valueOf(weatherData.getWind().getSpeed()));
        weatherIcon.setImageResource(R.drawable.weather_rain_icon);
    }

    private String formatCityString(String citySearch) {
        return citySearch.replace(" ", "");
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
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                listLocation = geocoder.getFromLocation(latitude, longitude, 1);
                if (listLocation != null && listLocation.size() > 0) {
                    address = listLocation.get(0);
                    state = address.getAdminArea();
                    city = address.getSubAdminArea();
                    country = address.getCountryCode();

                    locationData.setCity(city);
                    locationData.setState(state);
                    locationData.setCountry(country);

                    getWeatherData();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }



    private void getLocation(String cityName) {
        state = null;
        city = null;
        country = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            listLocation = geocoder.getFromLocationName(cityName, 10);
            listCity = new ArrayList<String>();
            listCityPosition = new ArrayList<Address>();
            if (listLocation != null && listLocation.size() > 0) {
                if(listLocation.size() > 1) {
                    for(int i = 0; i < listLocation.size(); i++) {
                        address = listLocation.get(i);
                        state = address.getAdminArea();
                        city = address.getSubAdminArea();
                        country = address.getCountryCode();

                        String addressCity = city + " - " + state + " - " + country;
                        if(state != null && city != null && country != null) {
                            listCity.add(addressCity);
                            listCityPosition.add(listLocation.get(i));
                        }

                        System.out.println("log citydata: " + listCity);
                    }
                    dialogOptions.show(getSupportFragmentManager(), "cityOptionsDialog");
                } else {
                    System.out.println("log listaddress: " + listLocation);
                    city = null;
                    state = null;
                    country = null;
                    address = listLocation.get(0);
                    state = address.getAdminArea();
                    city = address.getSubAdminArea();
                    country = address.getCountryCode();

                    locationData.setCity(city);
                    locationData.setState(state);
                    locationData.setCountry(country);
                    getWeatherData();
                }
            }
        } catch (IOException e) {
                    e.printStackTrace();
        }
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
                getLocation(formatCityString(cityField.getText().toString()));
                break;
        }

        AppHttpClient.get(path, null, new WeatherHttpResponseHandler.ResourceParserHandler() {
            @Override
            public void onSuccess(Object resource) {
                System.out.println("log resource onsuccess" + resource);
                weatherData = new GsonBuilder().create()
                        .fromJson(resource.toString(), WeatherData.class);
                stopLoading();
                getWeatherData();
            }

            @Override
            public void onFailure(Throwable e) {
                System.out.println("log resource failure");
                dialogAlert.show(getSupportFragmentManager(), "DialogConnectionFailure");
            }

            @Override
            public void onFailure(Throwable e, String errorMessage) {
                dialogAlert.show(getSupportFragmentManager(), "DialogConnectionFailure");
            }

            @Override
            public void onFailure(String errorMessage) {
                System.out.println("log erro onfailure: " + errorMessage);
                if(errorMessage.equals("404")) {
                    stopLoading();
                    Toast.makeText(MainActivity.this, "Wrong City please try again", Toast.LENGTH_LONG).show();

                    dialogOptions.show(getSupportFragmentManager(), "DialogCitySearchFailure");
                    System.out.println("log tag dialog: " + dialogOptions.getTag());
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
    public String setMessage(DialogFragment dialog) {
        return "teste";
    }

    @Override
    public String setTitle(DialogFragment dialog) {
        return "teste2";

    }

    @Override
    public CharSequence[] setItems(DialogFragment dialog) {
        CharSequence charSequence[] = null;
        if(dialog.getTag().equalsIgnoreCase("cityOptionsDialog")) {
            cityOptionsDialog = listCity.toArray(new CharSequence[listCity.size()]);
            charSequence = cityOptionsDialog;
        }

        return charSequence;
    }

    @Override
    public int onSelectedItem(int position) {
        Address address1 = listCityPosition.get(position);

        locationData.setCity(address1.getSubAdminArea());
        locationData.setState(address1.getAdminArea());
        locationData.setCountry(address1.getCountryCode());

        getWeatherData();

        return 0;
    }

    public int getDialogPosition() {
        return dialogPosition;
    }

    public void setDialogPosition(int dialogPosition) {
        this.dialogPosition = dialogPosition;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(dialogAlert != null) {
            dialogAlert.dismiss();
        }
        if(dialogOptions != null) {
            dialogOptions.dismiss();
        }

    }
}
