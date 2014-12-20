package com.pcr.myinfoweather.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.pcr.myinfoweather.R;

/**
 * Created by user on 20/12/2014.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, DialogPreference.OnPreferenceClickListener {

    //strings Preferences
    public static final String KEY_PREFS_TEMPERATURE = "temp_celcius";
    Preference pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_layout);

        pref = (Preference) findPreference("pref");
        pref.setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if(preference == pref) {
            System.out.println("log preference tree click clicked ok");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Choose the unit type");
            CharSequence[] items = {"Celsius", "Fahrenheit"};
            //get preference from sharedprefs and set position if user already typed

            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.out.println("log which: " + which);
                    switch (which) {
                        case 0:
                            pref.setSummary("Temperature unit Celsius");
                            break;
                        case 1:
                            pref.setSummary("Temperature unit Fahrenheit");
                            break;
                    }
                }
            });

            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    dialog.dismiss();
                }
            });

            builder.create();
            builder.show();
        }

        return true;

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals(KEY_PREFS_TEMPERATURE)) {
            System.out.println("log key for celsius");
            Preference tempPrefs = findPreference(key);
            CheckBoxPreference tempFahrenheit = (CheckBoxPreference) tempPrefs;


        }

        if(key.equals("temp_fahrenheit")) {
            Preference tempPrefs = findPreference(key);
            CheckBoxPreference tempCelsius = (CheckBoxPreference) tempPrefs;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.equals("pref")) {
            System.out.println("log pref pref is clicked");
        }
        return false;
    }
}
