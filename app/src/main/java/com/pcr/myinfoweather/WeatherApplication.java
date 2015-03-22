package com.pcr.myinfoweather;

import android.app.Application;
import android.content.Context;

import com.pcr.myinfoweather.db.DBHelpers;

/**
 * Created by Paula on 07/03/2015.
 */
public class WeatherApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DBHelpers.configureDatabaseIfNeeded(this);
    }
}
