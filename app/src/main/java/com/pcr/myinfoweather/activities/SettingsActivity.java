package com.pcr.myinfoweather.activities;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.fragments.SettingsFragment;
import com.pcr.myinfoweather.utils.Constants;
import com.pcr.myinfoweather.utils.UserSessionManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by Paula on 12/12/2014.
 */
public class SettingsActivity extends ActionBarActivity implements Preference.OnPreferenceClickListener{


    public static final String KEY_PREFS_TEMPERATURE = "temp_celcius";
    private CheckBoxPreference celsiusPreference;
    private CheckBoxPreference fahrenheitPreference;
    private SettingsFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //adding the preference fragment
        mFragment = new SettingsFragment();
        getFragmentManager().beginTransaction().add(android.R.id.content, mFragment).commit();
        PreferenceManager.setDefaultValues(this, R.xml.settings_layout, false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        celsiusPreference = (CheckBoxPreference) mFragment.findPreference("temp_celsius");
        fahrenheitPreference = (CheckBoxPreference) mFragment.findPreference("temp_fahrenheit");


        celsiusPreference.setOnPreferenceClickListener(this);
        fahrenheitPreference.setOnPreferenceClickListener(this);

        Log.i("Preferences", "getPrefs--> " + UserSessionManager.getSavedTemperaturePref(this));

        //Initial values based on UserPreferences
        if(isCelsiusPreference()) {
            celsiusPreference.setChecked(true);
            fahrenheitPreference.setChecked(false);
            celsiusPreference.setSummary("Celsius unit type");

        } else {
            celsiusPreference.setChecked(false);
            fahrenheitPreference.setChecked(true);
            fahrenheitPreference.setSummary("Fahrenheit unit type");
        }

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private boolean isCelsiusPreference() {
        return getPreference().equals(Constants.UNIT_TYPE_CELSIUS);
    }

    private String getPreference() {
        return UserSessionManager.getSavedTemperaturePref(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey().equals("temp_celsius")) {
            if(celsiusPreference.isChecked()) {
                fahrenheitPreference.setChecked(false);
                celsiusPreference.setChecked(true);
                celsiusPreference.setSummary("Celsius unit type");
                fahrenheitPreference.setSummary("Set temperature for Fahrenheit");
                saveTemperaturePreference(Constants.UNIT_TYPE_CELSIUS);
            }

        } else if(preference.getKey().equals("temp_fahrenheit")) {
            if(fahrenheitPreference.isChecked()) {
                fahrenheitPreference.setChecked(true);
                celsiusPreference.setChecked(false);
                fahrenheitPreference.setSummary("Fahrenheit unit type");
                celsiusPreference.setSummary("Set temperature for Celsius");
                saveTemperaturePreference(Constants.UNIT_TYPE_FAHRENHEIT);
            }
        }
        return false;
    }

    private void saveTemperaturePreference(String unit) {
        UserSessionManager.registerTemperaturePref(this, unit);
    }
}
