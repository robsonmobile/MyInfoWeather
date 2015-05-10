package com.pcr.myinfoweather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Paula Rosa on 24/03/2015.
 */
public class UserSessionManager {

    private static final String UNIT_PREF = "unit_pref";


    public static void  registerTemperaturePref(Context context, String unit) {
        appPrefs(context).edit().remove(UNIT_PREF).commit();
        appPrefs(context).edit().putString(UNIT_PREF, unit).commit();

    }

    public static String getSavedTemperaturePref(Context context) {
        return appPrefs(context).getString(UNIT_PREF, "C");
    }

    public static boolean hasTemperaturePref(Context context) {
        return appPrefs(context).contains(UNIT_PREF);
    }

    public static String getUnitTypePref(Context context) {
        if(getSavedTemperaturePref(context).equals(Constants.UNIT_TYPE_CELSIUS)) {
            return Constants.CELSIUS_TEMP;
        } else if(getSavedTemperaturePref(context).equals(Constants.UNIT_TYPE_FAHRENHEIT)) {
            return Constants.FAHRENHEIT_TEMP;
        } else {
            return Constants.CELSIUS_TEMP;
        }
    }

    private static SharedPreferences appPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }
}
