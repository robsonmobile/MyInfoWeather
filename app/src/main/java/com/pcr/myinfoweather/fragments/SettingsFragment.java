package com.pcr.myinfoweather.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.pcr.myinfoweather.R;

/**
 * Created by user on 20/12/2014.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_layout);
    }
}
