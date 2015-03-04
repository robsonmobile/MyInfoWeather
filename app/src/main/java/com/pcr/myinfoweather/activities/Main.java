package com.pcr.myinfoweather.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.request.UserLocationRequest;

/**
 * Created by Paula Rosa on 04/03/2015.
 */
public class Main extends ActionBarActivity implements UserLocationRequest.IListenerLocation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserLocationRequest.getInstance(this).connectClient();
    }

    @Override
    protected void onStop() {
        UserLocationRequest.getInstance(this).disconnectClient();
        super.onStop();
    }

    @Override
    public void onFinishedLocationRequest(boolean isFinishedRequest) {
        System.out.println("isfinishedRequest: " + isFinishedRequest);
    }
}
