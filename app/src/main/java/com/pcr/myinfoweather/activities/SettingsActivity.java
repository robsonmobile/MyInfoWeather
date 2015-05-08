package com.pcr.myinfoweather.activities;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.fragments.SettingsFragment;
import com.pcr.myinfoweather.utils.Constants;
import com.pcr.myinfoweather.utils.SharedPreferencesData;

import java.util.concurrent.TimeUnit;

/**
 * Created by Paula on 12/12/2014.
 */
public class SettingsActivity extends ActionBarActivity {


    public static final String KEY_PREFS_TEMPERATURE = "temp_celcius";
    private Preference temperaturePref;
    private Preference updateIntervalPref;
    private SettingsFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //adding the preference fragment
        mFragment = new SettingsFragment();
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, mFragment)
                .commit();

        PreferenceManager.setDefaultValues(this, R.xml.settings_layout, false);
    }

    @Override
    protected void onResume() {
        super.onResume();


        temperaturePref = mFragment.findPreference("temperaturePref");
        updateIntervalPref = mFragment.findPreference("updateInterval");

        if(getPreference(Constants.PREF_TYPE_TEMPERATURE) == 0) {
            temperaturePref.setSummary("Celsius");
        } else if(getPreference(Constants.PREF_TYPE_TEMPERATURE) == 1) {
            temperaturePref.setSummary("Fahrenheit");
        }

        long prefUpdateInterval = getPreference(Constants.PREF_TYPE_UPDATE_INTERVAL);

        String updateInterval = convertUpdateInterval(prefUpdateInterval);
        System.out.println("log interval: " + updateInterval);
        updateIntervalPref.setSummary(updateInterval);

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private long getPreference(int type) {
        long result = -1;
        switch (type) {
            case Constants.PREF_TYPE_TEMPERATURE:
                String tempPreference = SharedPreferencesData.getInstance(this).getTempPreferenceDataStr();
                if (tempPreference.equals(Constants.CELSIUS_TEMP)) {
                    result = 0;
                } else {
                    result = 1;
                }
                break;
            case Constants.PREF_TYPE_UPDATE_INTERVAL:
                result = SharedPreferencesData.getInstance(this).getPreferenceUpdateInterval();
            break;
        }

        return result;

    }


    private String convertUpdateInterval(long interval) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(interval);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(interval);
        long hours = TimeUnit.MILLISECONDS.toHours(interval);

        if(minutes > 0) {
            if(hours > 0) {
                return hours + " Hours and " + minutes + " minutes";
            } else {
                return minutes + " minutes";
            }
        } else {
            return "";
        }
    }
}
