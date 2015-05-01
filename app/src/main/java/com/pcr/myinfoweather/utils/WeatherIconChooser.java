package com.pcr.myinfoweather.utils;


import android.util.Log;

import com.pcr.myinfoweather.R;
import java.util.Calendar;

/**
 * Created by Paula on 25/04/2015.
 */
public class WeatherIconChooser {

    public WeatherIconChooser(int weatherCode)
    {
        this.currentWeatherCode = weatherCode;
    }

    private int currentWeatherCode = 0;

    private boolean isDay() {
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
        return currentDateTime >= dayDate && nightDate >= currentDateTime;
    }

    public int getImageResource() {
        if(isThunderstormWeather()) {
            return R.drawable.wc_thunderstorm;
        } else if (isShowerRainWeather()) {
            return R.drawable.wc_shower_rain;
        } else if(isRainWeather()) {
            return R.drawable.wc_rain;
        } else if(isSnowWeather()) {
            return R.drawable.wc_snow;
        } else if(isClearSkyDayWeather()) {
            return R.drawable.wc_clear_sky_day;
        } else if(isClearSkyNightWeather()) {
            return R.drawable.wc_clear_sky_night;
        } else if(isFewCloudsDayWeather()) {
            return R.drawable.wc_few_clouds_day;
        } else if(isFewCloudsNightWeather()) {
            return R.drawable.wc_few_clouds_night;
        }else if(isScatteredCloudsWeather()) {
            return R.drawable.wc_scattered_clouds;
        } else if(isBrokenCloudsWeather()) {
            Log.i("image int number", "> " + R.drawable.wc_broken_clouds);
            return R.drawable.wc_broken_clouds;
        } else if(isMistWeather()) {
            return R.drawable.wc_mist;
        }
        return 0;
    }

    private boolean isFewCloudsNightWeather() {
        return currentWeatherCode == 801 && !isDay();
    }

    private boolean isFewCloudsDayWeather() {
        return currentWeatherCode == 801 && isDay();
    }

    private boolean isClearSkyNightWeather() {
        return currentWeatherCode == 800 && !isDay();
    }

    private boolean isClearSkyDayWeather() {
        return currentWeatherCode == 800 && isDay();
    }

    private boolean isMistWeather() {
        return currentWeatherCode >= 700 && currentWeatherCode <= 781;
    }

    private boolean isBrokenCloudsWeather() {
        return currentWeatherCode == 803 || currentWeatherCode == 804;
    }

    private boolean isScatteredCloudsWeather() {
        return currentWeatherCode == 802;
    }

    private boolean isSnowWeather() {
        return currentWeatherCode == 511 || (currentWeatherCode >= 600 && currentWeatherCode <= 622);
    }

    private boolean isRainWeather() {
        return currentWeatherCode >= 500 && currentWeatherCode <= 504;
    }

    private boolean isShowerRainWeather() {
        return (currentWeatherCode >= 300 && currentWeatherCode <= 321) || (currentWeatherCode >=520 && currentWeatherCode <= 531);
    }

    private boolean isThunderstormWeather() {
        return currentWeatherCode >= 200 && currentWeatherCode <= 231;
    }


}
