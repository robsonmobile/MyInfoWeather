package com.pcr.myinfoweather.request;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.pcr.myinfoweather.exceptions.UserLocationException;
import com.pcr.myinfoweather.models.user.UserAdress;
import com.pcr.myinfoweather.network.GoogleClient;
import com.pcr.myinfoweather.utils.Constants;
import com.pcr.myinfoweather.utils.Validators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        Log.i("google play services: " , "..>" + getFusedLocationProviderApi().getLastLocation(GoogleClient.getInstance().getGoogleApiClient(mContext)));
        return getFusedLocationProviderApi().getLastLocation(GoogleClient.getInstance().getGoogleApiClient(mContext));
    }

    public Location getGPSInformation() {
        Location lastLocation = getLastKnownLocation();
        if(lastLocation != null) {
            double lat = lastLocation.getLatitude();
            double lon = lastLocation.getLongitude();

            lastLocation.setLatitude(lat);
            lastLocation.setLongitude(lon);
        } else {
            lastLocation = null;
        }
        return lastLocation;
    }

    public ArrayList<String> getAddress(com.pcr.myinfoweather.models.currentweather.Location gpsData) {
        ArrayList<String> address = new ArrayList<>();

        int occurrencesNumber = 1;
        int occurrencesPosition = occurrencesNumber -1;
        try {
            List<Address> listLocation = getGeocoder().getFromLocation(gpsData.getLatitude(), gpsData.getLongitude(),
                    occurrencesNumber);

            Address addresses = null;

            if (listLocation != null && listLocation.size() == 1) {
                addresses = listLocation.get(occurrencesPosition);

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

    private Geocoder getGeocoder() {
        return new Geocoder(mContext, Locale.getDefault());
    }

    public ArrayList<UserAdress> getAddress(String typedAddress) {
        typedAddress = "sao car";
        typedAddress = Validators.removeAccents(typedAddress);

        ArrayList<UserAdress> address = new ArrayList<>();
        int occurrencesNumber = 20;

        try {
            List<Address> listLocation = getGeocoder().getFromLocationName(typedAddress,
                    occurrencesNumber);

            Address addresses = null;

            if(listLocation.size() == 0) {
                //erro - generate a toast or message for user like "address not found"
                //address.add();
                Log.i("Log Address", "--> " + address);
            }
            if (listLocation != null && listLocation.size() > 0) {
                for (int i = 0; i < listLocation.size(); i++) {
                    addresses = listLocation.get(i);

                    String x = addresses.getSubAdminArea();
                    UserAdress user = new UserAdress();
                    user.setCity(x);
                    address.add(i, user);//tem que ser passado um objeto do tipo user
                    address.get(i).setCity(addresses.getSubAdminArea());
                    address.get(i).setState(addresses.getAdminArea());
                    address.get(i).setCountry(addresses.getCountryCode());
                    Log.i("Log Address", "--> " + address);



                }
                Log.i("Log Address final", "--> " + address);
                return address;

            }
        } catch (Exception e) {
            e.printStackTrace();
            //address.add("Unavailable location");
            return address;
        }

        return null;

    }
}


