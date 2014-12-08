package com.pcr.myinfoweather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.pcr.myinfoweather.interfaces.INetworkConnection;

/**
 * Created by Paula on 05/12/2014.
 */
public class CheckInternetConnection {

    private static Context context;
    private static CheckInternetConnection mInstance;
    private static INetworkConnection callbackConnection;

    private CheckInternetConnection(Context ctx, INetworkConnection callback) {
        this.context = ctx;
        this.callbackConnection = callback;
    }

    public enum EnumStates {
        CONNECTED, DISCONNECTED;
    }

    public static CheckInternetConnection getInstance(Context context1, INetworkConnection callback1) {
        if(mInstance == null) {
            mInstance = new CheckInternetConnection(context1, callback1);
        }
        return mInstance;
    }

    public boolean getNetworkInfo() {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            callbackConnection.status(EnumStates.CONNECTED);
            return true;
        } else {
            callbackConnection.status(EnumStates.DISCONNECTED);
            return false;
        }
    }


}
