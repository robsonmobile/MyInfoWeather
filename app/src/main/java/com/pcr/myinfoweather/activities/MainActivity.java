package com.pcr.myinfoweather.activities;


import android.content.Intent;
import android.location.Address;
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
import android.widget.ScrollView;
import android.widget.TextView;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.dialogs.AlertDialogBuilder;
import com.pcr.myinfoweather.dialogs.OptionsDialogBuilder;
import com.pcr.myinfoweather.interfaces.IDialog;
import com.pcr.myinfoweather.interfaces.ILocationListener;
import com.pcr.myinfoweather.interfaces.INetworkConnection;
import com.pcr.myinfoweather.models.LocationData;
import com.pcr.myinfoweather.models.WeatherData;
import com.pcr.myinfoweather.request.AppHttpClient;
import com.pcr.myinfoweather.request.UserLocationRequest;
import com.pcr.myinfoweather.response.WeatherHttpResponseHandler;
import com.pcr.myinfoweather.utils.CheckInternetConnection;
import com.pcr.myinfoweather.utils.Constants;
import com.google.gson.GsonBuilder;
import com.pcr.myinfoweather.utils.CurrentDateAndTime;
import com.pcr.myinfoweather.utils.DigitSeparator;
import com.pcr.myinfoweather.utils.GeneratePathRequest;
import com.pcr.myinfoweather.utils.SharedPreferencesData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
    private AlertDialogBuilder dialogAlert;
    private OptionsDialogBuilder dialogOptions;
    private int requestType;
    private List<String> listCity;
    private List<Address> listCityPosition;
    private FloatingActionsMenu fabButton;
    private LinearLayout searchLayout;
    private boolean fabIsClicked;
    private ScrollView scrollViewWeather;


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

        dialogAlert = new AlertDialogBuilder();
        dialogOptions = new OptionsDialogBuilder();

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        UserLocationRequest.getInstance(this).setListener(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternetConnection();
        requestType = Constants.PATH_FOR_GEOLOCATION;
        startLoading();

        if(UserLocationRequest.getInstance(this).isConnected()) {
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
        scrollViewWeather = (ScrollView) findViewById(R.id.scrollViewWeather);
    }

    private void getWeatherData() {
        float maxTemp = weatherData.getMain().getTempMax();
        float minTemp = weatherData.getMain().getTempMin();

        String maxTempFormat = new DecimalFormat("##.#").format(maxTemp);
        String minTempFormat = new DecimalFormat("##.#").format(minTemp);

        if(SharedPreferencesData.getInstance(this).getTempPreferenceDataStr().equalsIgnoreCase(Constants.CELSIUS_TEMP)) {
            tempMax.setText(String.valueOf(maxTempFormat)  + Constants.UNIT_TEMP_CELSIUS);
            tempMin.setText(String.valueOf(minTempFormat)  + Constants.UNIT_TEMP_CELSIUS);
        } else {
            tempMax.setText(String.valueOf(maxTempFormat)  + Constants.UNIT_TEMP_FAHRENHEIT);
            tempMin.setText(String.valueOf(minTempFormat)  + Constants.UNIT_TEMP_FAHRENHEIT);
        }

        String completeLocation = LocationData.getInstance().getCity() + ", " + LocationData.getInstance().getState() +
                      ", " + LocationData.getInstance().getCountry();
        String windFormatted = new DecimalFormat("##.#").format(weatherData.getWind().getSpeed());

        weatherLocationCity.setText(completeLocation);
        weatherWind.setText(String.valueOf(windFormatted + Constants.UNIT_WIND_METRIC));
        weatherCurrentDate.setText(CurrentDateAndTime.getInstance(this).getCurrentDate());
        weatherTitle.setText(weatherData.getWeather().get(0).getDescription());

        int idWeatherCondition = weatherData.getWeather().get(0).getId();
        int digit = DigitSeparator.getInstance(this).getDigit(idWeatherCondition, 1);

        switch (digit) {
            case 2:
                weatherIcon.setImageResource(R.drawable.wc_thunderstorm);
                break;
            case 3:
                weatherIcon.setImageResource(R.drawable.wc_shower_rain);
                break;
            case 5:
                int secondDigit = DigitSeparator.getInstance(this).getDigit(idWeatherCondition, 2);
                switch (secondDigit) {
                    case 0:
                        weatherIcon.setImageResource(R.drawable.wc_rain);
                        break;
                    case 1:
                        weatherIcon.setImageResource(R.drawable.wc_snow);
                        break;
                    case 2:
                        weatherIcon.setImageResource(R.drawable.wc_shower_rain);
                        break;
                }
                break;
            case 6:
                weatherIcon.setImageResource(R.drawable.wc_snow);
                break;
            case 7:
                weatherIcon.setImageResource(R.drawable.wc_mist);
                break;
            case 8:
                int lastDigit = DigitSeparator.getInstance(this).getDigit(idWeatherCondition, 3);
                switch(lastDigit) {
                    case 0:
                        if(CurrentDateAndTime.getInstance(this).isDayOrNight() == Constants.TIME_DAY) {
                            weatherIcon.setImageResource(R.drawable.wc_clear_sky_day);
                        } else {
                            weatherIcon.setImageResource(R.drawable.wc_clear_sky_night);
                        }
                        break;
                    case 1:
                        if(CurrentDateAndTime.getInstance(this).isDayOrNight() == Constants.TIME_DAY) {
                            weatherIcon.setImageResource(R.drawable.wc_few_clouds_day);
                        } else {
                            weatherIcon.setImageResource(R.drawable.wc_few_clouds_night);
                        }
                        break;
                    case 2:
                        weatherIcon.setImageResource(R.drawable.wc_scattered_clouds);
                        break;
                    case 3:
                        weatherIcon.setImageResource(R.drawable.wc_broken_clouds);
                        break;
                    case 4:
                        weatherIcon.setImageResource(R.drawable.wc_broken_clouds);
                        break;
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserLocationRequest.getInstance(this).connectClient();
    }

    @Override
    protected void onStop() {
        UserLocationRequest.getInstance(this).disconnectClient();
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
            performRequest(requestType);
        } else if(v.getId() == R.id.cityField) {
            cityField.requestFocus();
            scrollViewWeather.post(new Runnable() {
                @Override
                public void run() {
                    scrollViewWeather.fullScroll(ScrollView.FOCUS_UP);
                }
            });
        } else if(v.getId() == R.id.fab) {
            fabIsClicked = true;
            System.out.println("fab is clicked");
            searchLayout.setVisibility(View.VISIBLE);
            //set the scroll here
        }
    }

    private void performRequest(int requestType) {
        String unity = SharedPreferencesData.getInstance(this).getTempPreferenceDataStr();
        AppHttpClient.get(GeneratePathRequest.getInstance(this).createPath(requestType, cityField.getText().toString(), unity),
                null, new WeatherHttpResponseHandler.ResourceParserHandler() {
            @Override
            public void onSuccess(Object resource) {
                System.out.println("log resource onsuccess" + resource);
                weatherData = new GsonBuilder().create().fromJson(resource.toString(), WeatherData.class);
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
                    dialogAlert.show(getSupportFragmentManager(), Constants.DIALOG_SERVER_ERROR);
                    System.out.println("log tag dialog: " + dialogAlert.getTag());
                    cityField.setText("");
                    cityField.setHint("Type the city name");

                }

            }
        });
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
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //checkInternetConnection();
            Intent it = new Intent(this, SettingsActivity.class);
            startActivity(it);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPositiveClick(DialogFragment dialog) {
        if(dialog.getTag().equals(Constants.DIALOG_INTERNET_CONN_FAILURE)) {
            dialog.dismiss();
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        } else if(dialog.getTag().equals(Constants.DIALOG_SERVER_CONN_FAILURE)) {
            dialog.dismiss();
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
        } else if(dialog.getTag().equals(Constants.DIALOG_SERVER_ERROR)) {
            return "Wrong city, please try again";
        } else {
            return "Unknown Error";
        }

    }

    @Override
    public String setTitle(DialogFragment dialog) {
        if(dialog.getTag().equals(Constants.DIALOG_INTERNET_CONN_FAILURE)) {
            return "Alert";
        } else if(dialog.getTag().equals(Constants.DIALOG_SERVER_CONN_FAILURE)) {
            return "Alert";
        } else if(dialog.getTag().equals(Constants.DIALOG_SERVER_ERROR)) {
            return "Error";
        }
        else {
            return "teste2";
        }
    }

    @Override
    public CharSequence[] setItems(DialogFragment dialog) {
        CharSequence charSequence[] = null;
        if(dialog.getTag().equalsIgnoreCase("cityOptionsDialog")) {
            CharSequence[] cityOptionsDialog = listCity.toArray(new CharSequence[listCity.size()]);
            charSequence = cityOptionsDialog;
        }

        return charSequence;
    }

    @Override
    public void onSelectedItem(int position) {
        Address address1 = listCityPosition.get(position);

        LocationData.getInstance().setCity(address1.getSubAdminArea());
        LocationData.getInstance().setState(address1.getAdminArea());
        LocationData.getInstance().setCountry(address1.getCountryCode());
        getWeatherData();
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
            String city = LocationData.getInstance().getCity();
            String state = LocationData.getInstance().getState();
            String country = LocationData.getInstance().getCountry();

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
