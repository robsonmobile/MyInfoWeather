package com.pcr.myinfoweather.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by Paula on 15/11/2014.
 */
public class ApplicationContext extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getmContext() {
        return mContext;
    }
}
