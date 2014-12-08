package com.pcr.myinfoweather.request;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.pcr.myinfoweather.activities.MainActivity;
import com.pcr.myinfoweather.dialogs.OptionsDialogBuilder;
import com.pcr.myinfoweather.interfaces.ILocationListener;
import com.pcr.myinfoweather.models.LocationData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Paula on 04/12/2014.
 */
public class LocationRequest implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private LocationClient mLocationClient;
    private Context ctx;
    private Location mCurrentLocation;
    private LocationRequest mInstance;
    private ILocationListener mListener;


    public LocationRequest() {

    }

    public LocationRequest(Context context) {
        this.ctx = context;
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
            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());

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
        String state = null;
        String city = null;
        String country = null;
        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        try {
            List<Address> listLocation = geocoder.getFromLocationName(cityName, 10);
            ArrayList<String> listCity = new ArrayList<String>();
            ArrayList<Address> listCityPosition = new ArrayList<Address>();
            if (listLocation != null && listLocation.size() > 0) {
                Address address;
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
                   // OptionsDialogBuilder dialogOptions = new OptionsDialogBuilder();
                   // dialogOptions.show(getSupportFragmentManager(), "cityOptionsDialog");
                    mListener.onCitiesToChoose(listCity, listCityPosition);
                } else {
                    System.out.println("log listaddress: " + listLocation);
                    city = null;
                    state = null;
                    country = null;
                    address = listLocation.get(0);
                    state = address.getAdminArea();
                    city = address.getSubAdminArea();
                    country = address.getCountryCode();

                    LocationData.getInstance().setCity(city);
                    LocationData.getInstance().setState(state);
                    LocationData.getInstance().setCountry(country);
                    //getWeatherData();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectClient() {
        mLocationClient = new LocationClient(ctx, this, this);
        mLocationClient.connect();
    }

    public void disconnectClient() {
        mLocationClient.disconnect();
    }

    public boolean isConnected() {
        return mLocationClient.isConnected();
    }

}
