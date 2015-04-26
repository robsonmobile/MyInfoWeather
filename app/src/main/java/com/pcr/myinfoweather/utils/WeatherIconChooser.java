package com.pcr.myinfoweather.utils;

import android.content.Context;

import com.pcr.myinfoweather.R;

import java.util.Calendar;

/**
 * Created by Paula on 25/04/2015.
 */
public class WeatherIconChooser {

    private int isDayorNight() {
        //get Instances
        Calendar currentCalendar = Calendar.getInstance();
        long currentDateTime = currentCalendar.getTimeInMillis();

        //creating instances for day, month and year
        int day = currentCalendar.get(Calendar.DAY_OF_MONTH);
        int month = currentCalendar.get(Calendar.MONTH);
        int year = currentCalendar.get(Calendar.YEAR);

        //creating instance for day and night specif time
        Calendar dayCalendar = Calendar.getInstance();
        Calendar nightCalendar = Calendar.getInstance();

        //set specific time
        dayCalendar.set(year, month, day, 06, 00, 00);
        nightCalendar.set(year, month, day, 19, 30, 00);

        //getting datetime in millis to calculate
        long dayDate = dayCalendar.getTimeInMillis();
        long nightDate = nightCalendar.getTimeInMillis();

        //set
        if(currentDateTime >= dayDate && nightDate >= currentDateTime) {
            return Constants.TIME_DAY;
        } else {
            return Constants.TIME_NIGHT;
        }
    }

    public int setImageResource(int wearCode) {
        float result = (wearCode/2);
        if(result >= 100 && result <= 115.5) {
            return R.drawable.wc_thunderstorm;
        } else if ((result >= 150 && result <= 160.5) || (result >=260 && result <= 265.5)) {
            return R.drawable.wc_shower_rain;
        } else if(result >= 250 && result <= 252) {
            return R.drawable.wc_rain;
        } else if(result == 255.5 || (result >= 300 && result <= 311)) {
            return R.drawable.wc_snow;
        } else if(result == 400) {
            if(isDayorNight() == Constants.TIME_DAY) {
                return R.drawable.wc_clear_sky_day;
            } else {
                return R.drawable.wc_clear_sky_night;
            }
        } else if(result == 400.5) {
            if(isDayorNight() == Constants.TIME_NIGHT) {
                return R.drawable.wc_few_clouds_day;
            } else {
                return R.drawable.wc_few_clouds_night;
            }
        } else if(result == 401) {
            return R.drawable.wc_scattered_clouds;
        } else if(result == 401.5 || result == 402) {
            return R.drawable.wc_broken_clouds;
        } else if(result >= 350 && result <= 390.5) {
            return R.drawable.wc_mist;
        }
        return 0;
    }


}
