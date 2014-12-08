package com.pcr.myinfoweather.request;

import android.widget.Toast;

import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.activities.MainActivity;
import com.pcr.myinfoweather.models.LocationData;
import com.pcr.myinfoweather.response.WeatherHttpResponseHandler;
import com.pcr.myinfoweather.utils.Constants;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Paula on 05/12/2014.
 */
public class PerformRequestWeather {

    private LocationRequest requestLocation = new LocationRequest();

    private String createPath(int requestType, String cityField) {
        String path = null;
        switch (requestType) {
            case Constants.PATH_FOR_GEOLOCATION:
                path = geoLocationPath(LocationData.getInstance().getLat(), LocationData.getInstance().getLon());
                break;
            case Constants.PATH_FOR_CITY:
                path = cityLocationPath(cityField);
//                requestLocation.getCityLocation(formatCityString(cityField.getText().toString()));
                break;

        }
        return path;
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
            //Toast.makeText(Ma, R.string.toast_invalid_city_name, Toast.LENGTH_LONG).show();
            return "";
        }
    }

    private String geoLocationPath(float latitude, float longitude) {
        String latitudeValue = String.valueOf(latitude);
        String longitudeValue = String.valueOf(longitude);
        return Constants.LATITUDE_PATH + latitudeValue + Constants.LONGITUDE_PATH + longitudeValue;
    }


    public void performRequest(int requestType, String cityField) {
        makeRequest(createPath(requestType, cityField));
    }

    private void makeRequest(String path) {
        //Make the request based on the request type

        AppHttpClient.get(path, null, new WeatherHttpResponseHandler.ResourceParserHandler() {
            @Override
            public void onSuccess(Object resource) {

            }

            @Override
            public void onFailure(Throwable e) {

            }

            @Override
            public void onFailure(Throwable e, String errorMessage) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });


    }

}
