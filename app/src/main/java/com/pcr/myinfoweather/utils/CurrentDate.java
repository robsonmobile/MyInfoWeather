package com.pcr.myinfoweather.utils;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Paula on 05/12/2014.
 */
public class CurrentDate {
    private static CurrentDate ourInstance;
    private Context context;

    public static CurrentDate getInstance(Context ctx) {
        if(ourInstance == null) {
            ourInstance = new CurrentDate(ctx);
        }
        return ourInstance;
    }

    private CurrentDate(Context mContext) {
        this.context = mContext;
    }

    public String getCurrentDate() {
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);


//        Calendar calendar = Calendar.getInstance();
//        DateFormat dff = DateFormat.getDateInstance();
//        dff.format(calendar.getTime());
//        System.out.println("log dff: " + dff.format(calendar.getTime()));
//        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(currentDate);
    }
}
