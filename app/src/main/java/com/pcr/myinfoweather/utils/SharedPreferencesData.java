package com.pcr.myinfoweather.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by Paula on 15/11/2014.
 */
public class SharedPreferencesData {

    private SharedPreferences prefData;
    private SharedPreferences.Editor editor;
    private double tempPreference;
    private static SharedPreferencesData mInstance = null;
    public static Context mContext;

    /***Preferences Temperature***/
    private static final String PREFS_TEMP = "PREFS_TEMP";
    private static final int MODE_PRIVATE = Context.MODE_PRIVATE;
    private String prefName;

    /***Unity Types***/
    private static final int TYPE_CELSIUS = 0;
    private static final int TYPE_FAHRENHEIT = 1;
    private static final String TYPE_TEMP_UNITY = "TYPE_TEMP_UNITY";
    private int unityTempType;


    public SharedPreferencesData(Context context) {
        this.mContext = context;
    }

    public double getTempPreference() {
        return tempPreference;
    }

    public void setTempPreference(double tempPreference) {
        this.tempPreference = tempPreference;
    }

    public int getTempPreferenceData () {
        prefData = mContext.getSharedPreferences(PREFS_TEMP, MODE_PRIVATE);
        unityTempType = prefData.getInt(TYPE_TEMP_UNITY, Constants.TEMP_CELSIUS);
        return unityTempType;
    }

    public void setTempPreferenceData (int unityType) {
        prefData = mContext.getSharedPreferences(PREFS_TEMP, MODE_PRIVATE);
        editor = prefData.edit();
        editor.putInt(TYPE_TEMP_UNITY, unityType);
        editor.apply();
    }

}
