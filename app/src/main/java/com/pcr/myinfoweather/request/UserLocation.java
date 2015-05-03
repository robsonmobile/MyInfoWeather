package com.pcr.myinfoweather.request;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.pcr.myinfoweather.network.GoogleClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Paula on 04/12/2014.
 */
public class UserLocation {

    private static UserLocation instance;
    private Context mContext;

    public static UserLocation getInstance(Context context) {
        if(instance == null) {
            instance = new UserLocation(context);
        }
        return instance;
    }


    private UserLocation(Context ctx) {
        this.mContext = ctx;
    }

    private FusedLocationProviderApi getFusedLocationProviderApi() {
        return LocationServices.FusedLocationApi;
    }

    private Location getLastKnownLocation() {
        return getFusedLocationProviderApi().getLastLocation(GoogleClient.getInstance().getGoogleApiClient(mContext));
    }

    public Location getGPSInformation() {
        Location lastLocation = getLastKnownLocation();
        double lat = lastLocation.getLatitude();
        double lon = lastLocation.getLongitude();

        lastLocation.setLatitude(lat);
        lastLocation.setLongitude(lon);
        return lastLocation;
    }

    public LatLngBounds getLatLngBounds() {
        double lat = getGPSInformation().getLatitude();
        double lon = getGPSInformation().getLongitude();
        return new LatLngBounds(new LatLng(lat, lon), new LatLng(lat, lon));
    }

    public ArrayList<String> getAddress(com.pcr.myinfoweather.models.currentweather.Location gpsData) {
        ArrayList<String> address = new ArrayList<String>();
        Geocoder geocoder = getGeocoder();

        int occurrences = 1;
        int occurrencesPosition = occurrences -1;
        try {
            List<Address> listLocation = getGeocoder().getFromLocation(gpsData.getLatitude(), gpsData.getLongitude(),
                    occurrences);

            if (listLocation != null && listLocation.size() > 0) {
                Address addresses = listLocation.get(occurrencesPosition);

                address.add(addresses.getSubAdminArea());
                address.add(addresses.getAdminArea());
                address.add(addresses.getCountryCode());

                return address;
            }
        } catch (Exception e) {
            e.printStackTrace();
            address.add("Unavailable location");
            return address;
        }

        return null;
    }

    private Geocoder getGeocoder() {//metodo para listar 20 ocorrencias de cidades
        return new Geocoder(mContext, Locale.getDefault());
    }

    public List<Address> getAddress(String typedAddress, int occurences) {
        List<Address> listLocation = null;

        try {
            listLocation = getGeocoder().getFromLocationName(typedAddress, occurences);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listLocation;
    }

}


