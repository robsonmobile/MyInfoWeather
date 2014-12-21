package com.pcr.myinfoweather.activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.dialogs.AlertDialogBuilder;
import com.pcr.myinfoweather.fragments.SettingsFragment;
import com.pcr.myinfoweather.interfaces.IDialog;
import com.pcr.myinfoweather.utils.Constants;
import com.pcr.myinfoweather.utils.SharedPreferencesData;

import java.util.concurrent.TimeUnit;

/**
 * Created by Paula on 12/12/2014.
 */
public class SettingsActivity extends ActionBarActivity implements IDialog, Preference.OnPreferenceClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener{


    public static final String KEY_PREFS_TEMPERATURE = "temp_celcius";
    private Preference temperaturePref;
    private Preference updateIntervalPref;
    private SettingsFragment mFragment= new SettingsFragment();
    private AlertDialogBuilder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //adding the preference fragment
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, mFragment)
                .commit();

        PreferenceManager.setDefaultValues(this, R.xml.settings_layout, false);
        alertDialog = AlertDialogBuilder.newInstance(Constants.DIALOG_TYPE_SINGLECHOICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFragment.getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        temperaturePref = mFragment.findPreference("temperaturePref");
        updateIntervalPref = mFragment.findPreference("updateInterval");

        temperaturePref.setOnPreferenceClickListener(this);
        updateIntervalPref.setOnPreferenceClickListener(this);

        if(getPreference(Constants.PREF_TYPE_TEMPERATURE) == 0) {
            temperaturePref.setSummary("Celsius");
        } else if(getPreference(Constants.PREF_TYPE_TEMPERATURE) == 1) {
            temperaturePref.setSummary("Fahrenheit");
        }

        long prefUpdateInterval = getPreference(Constants.PREF_TYPE_UPDATE_INTERVAL);

        String updateInterval = convertUpdateInterval(prefUpdateInterval);
        System.out.println("log interval: " + updateInterval);

    }

    @Override
    public void onPause() {
        super.onPause();
        mFragment.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
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



    @Override
    public void onPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onNegativeClick(DialogFragment dialog) {

    }

    @Override
    public String setMessage(DialogFragment dialog) {
        return "";
    }

    @Override
    public String setTitle(DialogFragment dialog) {
        if(dialog.getTag().equals("dialogUpdateInterval")) {
            return "Update Interval";
        } else if(dialog.getTag().equals("dialogTemperaturePref")) {
            return "Temperature Unit";
        } else {
            return "";
        }
    }

    @Override
    public int setIcon(DialogFragment dialog) {
        if(dialog.getTag().equals("dialogUpdateInterval")) {
            return R.drawable.timer_icon;
        } else if(dialog.getTag().equals("dialogTemperaturePref")) {
            return R.drawable.thermometer_icon;
        } else {
            return 0;
        }
    }

    @Override
    public CharSequence[] setItems(DialogFragment dialog) {
        if(dialog.getTag().equals("dialogTemperaturePref")) {
            return new CharSequence[]{"Celsius", "Fahrenheit"};
        } else {
            return new CharSequence[0];
        }
    }

    @Override
    public void onSelectedItem(DialogFragment dialog, int position) {
        if(dialog.getTag().equals("dialogTemperaturePref")) {
            switch (position) {
                case 0:
                    temperaturePref.setSummary("Temperature unit Celsius");
                    SharedPreferencesData.getInstance(SettingsActivity.this)
                            .setTempPreferenceDataStr(Constants.CELSIUS_TEMP);
                    break;
                case 1:
                    temperaturePref.setSummary("Temperature unit Fahrenheit");
                    SharedPreferencesData.getInstance(SettingsActivity.this)
                            .setTempPreferenceDataStr(Constants.FAHRENHEIT_TEMP);
                    break;
            }
        }

    }

    @Override
    public int singleChoiceSelectedPosition() {
        return (int) getPreference(Constants.PREF_TYPE_TEMPERATURE);
    }

    @Override
    public CharSequence[] setSingleChoice(DialogFragment dialog) {
        if(dialog.getTag().equals("dialogTemperaturePref")) {
            return new CharSequence[]{"Celsius", "Fahrenheit"};
        } else {
            return new CharSequence[0];
        }

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference == temperaturePref) {
            System.out.println("log preference tree click clicked ok");
            alertDialog.show(getSupportFragmentManager(), "dialogTemperaturePref");
        } else if(preference == updateIntervalPref) {
//            dialog = new CustomDialog();
//            dialog.show(this.getSupportFragmentManager(), "dialogUpdateInterval");
        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

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
