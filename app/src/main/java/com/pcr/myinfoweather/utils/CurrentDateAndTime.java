package com.pcr.myinfoweather.utils;

import android.content.Context;
import android.text.format.Time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Paula on 05/12/2014.
 */
public class CurrentDateAndTime {

    private static CurrentDateAndTime ourInstance;
    private Context context;

    public static CurrentDateAndTime getInstance(Context ctx) {
        if(ourInstance == null) {
            ourInstance = new CurrentDateAndTime(ctx);
        }
        return ourInstance;
    }

    private CurrentDateAndTime(Context mContext) {
        this.context = mContext;
    }

    public String getCurrentDate() {
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        return dateFormat.format(currentDate);
    }

    public int isDayOrNight() {
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

}
