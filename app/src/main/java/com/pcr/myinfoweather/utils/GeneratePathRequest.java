package com.pcr.myinfoweather.utils;

import android.content.Context;
import android.widget.Toast;

import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.models.currentweather.LocationData;
import com.pcr.myinfoweather.request.UserLocationRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by Paula on 12/12/2014.
 */
public class GeneratePathRequest {

    private static GeneratePathRequest instance;
    private Context mContext;

    public static GeneratePathRequest getInstance(Context context) {
        if(instance == null) {
            instance = new GeneratePathRequest(context);
        }
        return instance;
    }

    private GeneratePathRequest(Context ctx) {
        this.mContext = ctx;
    }

    public String createPath(int requestType, String textFieldCity, String tempPreference) {
        //Make the request based on the request type
        String path = null;
        switch (requestType) {
            case Constants.PATH_FOR_GEOLOCATION:
                path = geoLocationPath(LocationData.getInstance().getLat(), LocationData.getInstance().getLon(), tempPreference);
                System.out.println("log path: " + path);
                break;
            case Constants.PATH_FOR_CITY:
                path = cityLocationPath(textFieldCity, tempPreference);
                UserLocationRequest.getInstance(mContext).getCityLocation(removeSpaceString(textFieldCity));
                System.out.println("log path: " + path);
                break;
        }
        return path;
    }


    private String geoLocationPath(float latitude, float longitude, String unity) {
        String latitudeValue = String.valueOf(latitude);
        String longitudeValue = String.valueOf(longitude);
        return Constants.LATITUDE_PATH + latitudeValue + Constants.LONGITUDE_PATH + longitudeValue + Constants.UNITS_PATH + unity;
    }

    private String cityLocationPath(String city, String unity) {
        URI uri = null;
        try {
            String nfdNormalizedString = Normalizer.normalize(city, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            String str = pattern.matcher(nfdNormalizedString).replaceAll("");

            uri = new URI(str.replaceAll(" ", "%20"));
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("log uri: " + uri);

        if(uri != null) {
            return Constants.LOCAL_PATH + uri.toString() + Constants.UNITS_PATH + unity;
        } else {
            Toast.makeText(mContext, R.string.toast_invalid_city_name, Toast.LENGTH_LONG).show();
            return "";
        }
    }

    private String removeSpaceString(String citySearch) {
        return citySearch.replace(" ", "");
    }
}
