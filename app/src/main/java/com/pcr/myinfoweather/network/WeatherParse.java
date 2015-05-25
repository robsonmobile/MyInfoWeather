package com.pcr.myinfoweather.network;

import android.content.Context;
import android.util.Log;

import com.pcr.myinfoweather.models.currentweather.Location;
import com.pcr.myinfoweather.models.currentweather.WeatherData;
import com.pcr.myinfoweather.models.user.User;
import com.pcr.myinfoweather.models.user.UserAdress;
import com.pcr.myinfoweather.utils.Constants;
import com.pcr.myinfoweather.utils.CurrentDateAndTime;
import com.pcr.myinfoweather.utils.WeatherIconChooser;

import java.util.ArrayList;

/**
 * Created by Paula Rosa on 30/04/2015.
 */
public class WeatherParse {

    public static User parseWeather(Context context, WeatherData weather, Location userLocation, UserAdress userAddress) {
        if(weather != null) {
            //log
            Log.i("image weathercode", "> " + weather.getWeather().get(0).getId());
            Log.i("image weatherimage", "> " + new WeatherIconChooser(weather.getWeather().get(0).getId()).getImageResource());

            return User.newBuilder()
                    .withWeatherCode(weather.getWeather().get(0).getId())
                    .withTitle(weather.getWeather().get(0).getDescription())
                    .withImage(new WeatherIconChooser(weather.getWeather().get(0).getId()).getImageResource())
                    .withTempMax(weather.getMain().getTempMin())
                    .withTempMin(weather.getMain().getTempMin())
                    .withWindSpeed(weather.getWind().getSpeed())
                    .withDate(CurrentDateAndTime.getInstance(context).getCurrentDate())
                    .withGeoLocation(userLocation)
                    .withAddress(userAddress)
                    .build();



        }

        return null;
    }

    public static Location parseGeoLocation(android.location.Location geoLocation) {
        if(geoLocation != null) {
            return Location.newBuilder()
                    .withLatitude(geoLocation.getLatitude())
                    .withLongitude(geoLocation.getLongitude())
                    .build();
        }

        return null;
    }

    public static UserAdress parseAddress(ArrayList<String> address) {
        if(address != null) {
            //quando um dos itens de endereço estiver nulo , deverá ser feita uma nova requisição por
            //pelo menos 3x
            return UserAdress.newBuilder()
                    .withCity(address.get(Constants.CITY))
                    .withState(address.get(Constants.STATE))
                    .withCountry(address.get(Constants.COUNTRY))
                    .build();
        }
        return null;
    }

}
