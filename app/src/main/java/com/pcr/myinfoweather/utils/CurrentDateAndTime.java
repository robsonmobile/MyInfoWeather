package com.pcr.myinfoweather.utils;

import android.content.Context;
import android.text.format.Time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public String getCurrentTime() {
        int hours =  new Time(String.valueOf(System.currentTimeMillis())).hour;
        System.out.println("log hour: " + hours);

        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(context);
        String formattedDate = dateFormat.format(currentDate);
        return "";

    }

    public static String getCurrentTimeStamp(){
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date
            System.out.println("log current time: " + currentTimeStamp);
            return currentTimeStamp;
    }
}
