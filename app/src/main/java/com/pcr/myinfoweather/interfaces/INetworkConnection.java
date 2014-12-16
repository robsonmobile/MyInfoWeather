package com.pcr.myinfoweather.interfaces;

import com.pcr.myinfoweather.utils.CheckInternetConnection;

/**
 * Created by Paula on 05/12/2014.
 */
public interface INetworkConnection {

    public void onStatusResult (CheckInternetConnection.EnumStates status);
}
