package com.pcr.myinfoweather.utils;

import android.content.Context;
import android.content.Intent;

import com.pcr.myinfoweather.activities.SettingsActivity;

/**
 * Created by Paula Rosa on 24/04/2015.
 */
public class Intents {

    public static final Intent toSettingsActivity(Context packageContext) {
        return new Intent(packageContext, SettingsActivity.class);
    }
}
