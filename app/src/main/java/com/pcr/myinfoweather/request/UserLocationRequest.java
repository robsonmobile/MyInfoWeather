package com.pcr.myinfoweather.request;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.pcr.myinfoweather.interfaces.ILocationListener;
import com.pcr.myinfoweather.models.LocationData;
import com.pcr.myinfoweather.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Paula on 04/12/2014.
 */
public class UserLocationRequest implements  GoogleApiClient.OnConnectionFailedListener,
                                            GoogleApiClient.ConnectionCallbacks {

    private static UserLocationRequest instance;
    private Context mContext;
    private Location mCurrentLocation;
    private UserLocationRequest mInstance;
    private IListenerLocation mListener;
    private GoogleApiClient mClient;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    GoogleMap test;



    public static UserLocationRequest getInstance(Context context) {
        if(instance == null) {
            instance = new UserLocationRequest(context);
        }
        return instance;
    }


    private UserLocationRequest(Context ctx) {
        this.mContext = ctx;
    }

    public interface IListenerLocation {
        public void onFinishedLocationRequest(boolean isFinishedRequest);
    }

    public void setListener(IListenerLocation mListener) {
        this.mListener = mListener;
    }

    private void createGoogleApiClient() {
        mClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        getLocationData();
        mListener.onFinishedLocationRequest(true);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

//    @Override
//    public void onDisconnected() {
//
//    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

//    private void getLocation() {
//        if(mClient.isConnected()) {
//            mCurrentLocation = fusedLocationProviderApi.getLastLocation(mClient);
//            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
//
//            try {
//
//            }
//        }
//    }

    private void getCurrentLocationData(int locationType) {
        if(isConnected()) {
            mCurrentLocation = fusedLocationProviderApi.getLastLocation(mClient);
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

            switch (locationType) {
                case Constants.PATH_FOR_GEOLOCATION:
                    try {
                        List<Address> addressList = geocoder.getFromLocation(LocationData.getInstance().getLat(),
                                LocationData.getInstance().getLon(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                case Constants.PATH_FOR_CITY:
                    try {
                        List<Address> addressList = geocoder.getFromLocationName(LocationData.getInstance().getCity(), 10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }


    private void getCurrentLocation() {
        if(mClient.isConnected()) {
            mCurrentLocation = fusedLocationProviderApi.getLastLocation(mClient);
            float lat = (float) mCurrentLocation.getLatitude();
            float lon = (float) mCurrentLocation.getLongitude();
            System.out.println("log passou aqui");
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

    private void getLocationData() {
        if(mClient.isConnected()) {
            mCurrentLocation = fusedLocationProviderApi.getLastLocation(mClient);
            float lat = (float) mCurrentLocation.getLatitude();
            float lon = (float) mCurrentLocation.getLongitude();
            System.out.println("log passou aqui");
            LocationData.getInstance().setLat(lat);
            LocationData.getInstance().setLon(lon);
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
                    //mListener.onCitiesToChoose(listCity, listCityPosition);
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
        createGoogleApiClient();
        mClient.connect();
    }

    public void disconnectClient() {
        mClient.disconnect();
    }

    public boolean isConnected() {
        return mClient.isConnected();
                //mLocationClient.isConnected();
    }

    private String validateNullString(String input) {
        if(input == null) {
            return "";
        } else {
            return input;
        }
    }
}
