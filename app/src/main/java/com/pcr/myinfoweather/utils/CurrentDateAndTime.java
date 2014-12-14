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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = new GregorianCalendar();
        long currentTime = calendar.get(Calendar.MILLISECOND);
        long timeInMilliDay = 0;
        long timeInMilliNight = 0;
        try {
            Date mDateDay = simpleDateFormat.parse("06:00:00");
            Date mDateNight = simpleDateFormat.parse("19:00:00");
            timeInMilliDay = mDateDay.getTime();
            timeInMilliNight = mDateNight.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if((currentTime >= timeInMilliDay) && (timeInMilliNight >= currentTime)) {
            return Constants.TIME_DAY;
        } else {
            return Constants.TIME_NIGHT;
        }
    }

}
