package com.pcr.myinfoweather.activities;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.dialogs.AlertDialogBuilder;
import com.pcr.myinfoweather.dialogs.OptionsDialogBuilder;
import com.pcr.myinfoweather.interfaces.IDialog;
import com.pcr.myinfoweather.interfaces.ILocationListener;
import com.pcr.myinfoweather.interfaces.INetworkConnection;
import com.pcr.myinfoweather.models.LocationData;
import com.pcr.myinfoweather.models.WeatherData;
import com.pcr.myinfoweather.request.AppHttpClient;
import com.pcr.myinfoweather.response.WeatherHttpResponseHandler;
import com.pcr.myinfoweather.utils.CheckInternetConnection;
import com.pcr.myinfoweather.utils.Constants;
import com.google.gson.GsonBuilder;
import com.pcr.myinfoweather.utils.CurrentDate;
import com.pcr.myinfoweather.utils.SharedPreferencesData;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ActionBarActivity implements View.OnClickListener, IDialog, ILocationListener, INetworkConnection {

    private WeatherData weatherData;

    /******Ui Components******/
    private TextView tempMax;
    private TextView tempMin;
    private TextView weatherTitle;
    private TextView weatherLocationCity;
    private TextView weatherWind;
    private TextView weatherCurrentDate;
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
    com.pcr.myinfoweather.request.LocationRequest requestLocation;
    private FloatingActionsMenu fabButton;
    private LinearLayout searchLayout;
    private boolean fabIsClicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        btnSearch.setOnClickListener(this);
        cityField.setOnClickListener(this);
        fabButton.setOnClickListener(this);
        cityField.clearFocus();
        btnSearch.requestFocus();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }

        //Getting the Preference for Temperature
        SharedPreferencesData prefs = new SharedPreferencesData(this);
        tempPreference = prefs.getTempPreferenceData();
        dialogAlert = new AlertDialogBuilder();
        dialogOptions = new OptionsDialogBuilder();

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        requestLocation = new com.pcr.myinfoweather.request.LocationRequest(this);
        System.out.println("log city: " + LocationData.getInstance().getCity());
        requestLocation.setListener(MainActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestType = Constants.PATH_FOR_GEOLOCATION;

        startLoading();
        if(requestLocation.isConnected()) {
            performRequest(requestType);
        }
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
        weatherCurrentDate = (TextView) findViewById(R.id.weatherCurrentDate);
        fabButton = (FloatingActionsMenu) findViewById(R.id.fab);
        searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
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

        String completeLocation = LocationData.getInstance().getCity() + ", " + LocationData.getInstance().getState() +
                      ", " + LocationData.getInstance().getCountry();


        weatherTitle.setText(weatherData.getWeather().get(0).getMain());
        weatherLocationCity.setText(completeLocation);
        weatherWind.setText(String.valueOf(weatherData.getWind().getSpeed()));
        weatherIcon.setImageResource(R.drawable.weather_rain_icon);
        weatherCurrentDate.setText(CurrentDate.getInstance(this).getCurrentDate());
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
                finalTemp = (temp - 273.15f);
                break;
            case Constants.TEMP_FAHRENHEIT:
                finalTemp = ((1.8f * (temp - 273.15f)) + 32);
                break;
        }
        return finalTemp;
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestLocation.connectClient();

    }

    @Override
    protected void onStop() {
        requestLocation.disconnectClient();
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
        } else if(v.getId() == R.id.fab) {
            fabIsClicked = true;
            searchLayout.setVisibility(View.VISIBLE);
            //set the scroll here
        }
    }

    private String createPath(int requestType) {
        //Make the request based on the request type
        String path = null;
        switch (requestType) {
            case Constants.PATH_FOR_GEOLOCATION:
                path = geoLocationPath(LocationData.getInstance().getLat(), LocationData.getInstance().getLon());
                break;
            case Constants.PATH_FOR_CITY:
                path = cityLocationPath(cityTypedSearch);
                requestLocation.getCityLocation(formatCityString(cityField.getText().toString()));
                break;
        }
        return path;
    }

    private void performRequest(int requestType) {
        AppHttpClient.get(createPath(requestType), null, new WeatherHttpResponseHandler.ResourceParserHandler() {
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
                dialogAlert.show(getSupportFragmentManager(), Constants.DIALOG_SERVER_CONN_FAILURE);
            }

            @Override
            public void onFailure(Throwable e, String errorMessage) {
                dialogAlert.show(getSupportFragmentManager(), Constants.DIALOG_SERVER_CONN_FAILURE);
            }

            @Override
            public void onFailure(String errorMessage) {
                System.out.println("log erro onfailure: " + errorMessage);
                if(errorMessage.equals("404")) {
                    stopLoading();
                    Toast.makeText(MainActivity.this, "Wrong City please try again", Toast.LENGTH_LONG).show();

                    dialogOptions.show(getSupportFragmentManager(), Constants.DIALOG_SERVER_CONN_FAILURE);
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
            checkInternetConnection();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPositiveClick(DialogFragment dialog) {
        if(dialog.getTag().equals(Constants.DIALOG_INTERNET_CONN_FAILURE)) {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        } else if(dialog.getTag().equals(Constants.DIALOG_SERVER_CONN_FAILURE)) {
            performRequest(requestType);
        }
    }

    @Override
    public void onNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public String setMessage(DialogFragment dialog) {
        if (dialog.getTag().equals(Constants.DIALOG_INTERNET_CONN_FAILURE)) {
            return "Unable to connect to the internet";
        } else if(dialog.getTag().equals(Constants.DIALOG_SERVER_CONN_FAILURE)) {
            return "Unable to contact the server, try again";
        } else {
            return "teste";
        }

    }

    @Override
    public String setTitle(DialogFragment dialog) {
        if(dialog.getTag().equals(Constants.DIALOG_INTERNET_CONN_FAILURE)) {
            return "Alert";
        } else if(dialog.getTag().equals(Constants.DIALOG_SERVER_CONN_FAILURE)) {
            return "Alert";
        }
        else {
            return "teste2";
        }


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

        LocationData.getInstance().setCity(address1.getSubAdminArea());
        LocationData.getInstance().setState(address1.getAdminArea());
        LocationData.getInstance().setCountry(address1.getCountryCode());
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

    @Override
    public void onFinishedRequest(boolean isFinishedRequest) {
        if(isFinishedRequest) {
            city = LocationData.getInstance().getCity();
            state = LocationData.getInstance().getState();
            country = LocationData.getInstance().getCountry();

            performRequest(requestType);
        }
    }

    @Override
    public void onCitiesToChoose(ArrayList<String> cities, List<Address> selectedCity) {
        System.out.println("log cities to choose");
        listCity = cities;
        listCityPosition = selectedCity;
        dialogOptions.show(getSupportFragmentManager(), "cityOptionsDialog");
    }

    @Override
    public void status(CheckInternetConnection.EnumStates status) {
        System.out.println("log status: " + status);
    }

    private void checkInternetConnection() {
        if(!CheckInternetConnection.getInstance(this, this).getNetworkInfo()) {
            dialogAlert.show(getSupportFragmentManager(), Constants.DIALOG_INTERNET_CONN_FAILURE);
        }
    }
}
