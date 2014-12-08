package com.pcr.myinfoweather.interfaces;

import android.location.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paula on 04/12/2014.
 */
public interface ILocationListener {

    public void onFinishedRequest(boolean isFinishedRequest);

    public void onCitiesToChoose(ArrayList<String> cities, List<Address> selectedCity);
}
