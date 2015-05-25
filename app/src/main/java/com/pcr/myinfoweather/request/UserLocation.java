package com.pcr.myinfoweather.request;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.pcr.myinfoweather.models.user.UserAdress;
import com.pcr.myinfoweather.network.GoogleClient;
import com.pcr.myinfoweather.utils.Validators;

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

        typedAddress = removeAccents(typedAddress);

        ArrayList<UserAdress> address = new ArrayList<>();
        int occurrencesNumber = 20;
        Address addresses = null;

        try {
            List<Address> listLocation = getGeocoder().getFromLocationName(typedAddress,
                    occurrencesNumber);

            if(listLocation.size() == 0) {
                Log.i("Log Address", "--> " + address);
            }
            if (listLocation != null && listLocation.size() > 0) {
                for (int i = 0; i < listLocation.size(); i++) {
                    addresses = listLocation.get(i);

                    UserAdress user = new UserAdress();

                    String city = addresses.getSubAdminArea();
                    String state = addresses.getAdminArea();
                    String country = addresses.getCountryCode();

                    if(!isAddressNull(city) && !isAddressNull(state) && !isAddressNull(country)) {
                        user.setCity(city);
                        user.setState(state);
                        user.setCountry(country);

                        address.add(user);
                    } else {
                        //user.setCompleteAdress("Address not found");
                    }
                }
                return address;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //address.add("Unavailable location");
            return address;
        }

        return null;

    }

    private String removeAccents(String typedAddress) {
        return Validators.removeAccents(typedAddress);
    }

    private boolean isAddressNull(String city) {
        return Validators.isNull(city);
    }
}


