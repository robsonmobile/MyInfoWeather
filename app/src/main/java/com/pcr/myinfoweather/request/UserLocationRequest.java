package com.pcr.myinfoweather.request;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.pcr.myinfoweather.interfaces.ILocationListener;
import com.pcr.myinfoweather.models.LocationData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Paula on 04/12/2014.
 */
public class UserLocationRequest implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private static UserLocationRequest instance;
    private LocationClient mLocationClient;
    private Context mContext;
    private Location mCurrentLocation;
    private UserLocationRequest mInstance;
    private ILocationListener mListener;


    public static UserLocationRequest getInstance(Context context) {
        if(instance == null) {
            instance = new UserLocationRequest(context);
        }
        return instance;
    }


    private UserLocationRequest(Context ctx) {
        this.mContext = ctx;
    }

    public void setListener(ILocationListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
        mListener.onFinishedRequest(true);

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void getCurrentLocation() {
        if(mLocationClient.isConnected()) {
            mCurrentLocation = mLocationClient.getLastLocation();
            float lat = (float) mCurrentLocation.getLatitude();
            float lon = (float) mCurrentLocation.getLongitude();

            LocationData.getInstance().setLat(lat);
            LocationData.getInstance().setLon(lon);
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

            try {
                List<Address> listLocation = geocoder.getFromLocation(lat, lon, 1);
                if (listLocation != null && listLocation.size() > 0) {
                    Address address = listLocation.get(0);
                    String state = address.getAdminArea();
                    String city = address.getSubAdminArea();
                    String country = address.getCountryCode();

                    //set the data on Model
                    LocationData.getInstance().setCity(city);
                    LocationData.getInstance().setState(state);
                    LocationData.getInstance().setCountry(country);

                    //getWeatherData();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void getCityLocation(String cityName) {
        String stateStr = null;
        String cityStr = null;
        String countryStr = null;
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> listLocation = geocoder.getFromLocationName(cityName, 10);
            ArrayList<String> listCity = new ArrayList<String>();
            ArrayList<Address> listCityPosition = new ArrayList<Address>();
            if (listLocation != null && listLocation.size() > 0) {
                Address address;
                if(listLocation.size() > 1) {
                    for(int i = 0; i < listLocation.size(); i++) {
                        address = listLocation.get(i);
                        stateStr = address.getAdminArea();
                        cityStr = address.getSubAdminArea();
                        countryStr = address.getCountryCode();

                        String addressCity = cityStr + " - " + stateStr + " - " + countryStr;
                        if(stateStr != null && cityStr != null && countryStr != null) {
                            listCity.add(addressCity);
                            listCityPosition.add(listLocation.get(i));
                        }

                        System.out.println("log citydata: " + listCity);
                    }
                   // OptionsDialogBuilder dialogOptions = new OptionsDialogBuilder();
                   // dialogOptions.show(getSupportFragmentManager(), "cityOptionsDialog");
                    mListener.onCitiesToChoose(listCity, listCityPosition);
                } else {
                    System.out.println("log listaddress: " + listLocation);
                    cityStr = null;
                    stateStr = null;
                    countryStr = null;
                    address = listLocation.get(0);
                    stateStr = validateNullString(address.getAdminArea());
                    cityStr = validateNullString(address.getSubAdminArea());
                    countryStr = validateNullString(address.getCountryCode());

                    LocationData.getInstance().setCity(cityStr);
                    LocationData.getInstance().setState(stateStr);
                    LocationData.getInstance().setCountry(countryStr);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectClient() {
        mLocationClient = new LocationClient(mContext, this, this);
        mLocationClient.connect();
    }

    public void disconnectClient() {
        mLocationClient.disconnect();
    }

    public boolean isConnected() {
        return mLocationClient.isConnected();
    }

    private String validateNullString(String input) {
        if(input == null) {
            return "";
        } else {
            return input;
        }
    }
}
