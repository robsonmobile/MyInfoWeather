package com.pcr.myinfoweather.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Paula Rosa on 24/03/2015.
 */
public class ConnectivityHelpers {

    public static ConnectivityManager from(Context context) {
        return (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static boolean hasConnectivity(Context context) {
        ConnectivityManager cm = from(context);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isWifi(NetworkInfo activeNetwork) {
        return activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean isMobile(NetworkInfo activeNetwork) {
        return activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
    }
}
