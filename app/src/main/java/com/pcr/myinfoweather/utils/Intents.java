package com.pcr.myinfoweather.utils;

import android.content.Context;
import android.content.Intent;
import com.pcr.myinfoweather.activities.PlaceholderActivity;
import com.pcr.myinfoweather.activities.SettingsActivity;

/**
 * Created by Paula Rosa on 24/04/2015.
 */
public class Intents {

    public static final Intent toPlaceholder(Context packageContext, String chooser) {
        Intent intent = new Intent(packageContext, PlaceholderActivity.class);
        intent.putExtra("type", chooser);
        return intent;
    }

    public static final Intent toSettingsActivity(Context packageContext) {
        return new Intent(packageContext, SettingsActivity.class);
    }
}
