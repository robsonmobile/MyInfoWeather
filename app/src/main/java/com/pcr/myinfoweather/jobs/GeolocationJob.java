package com.pcr.myinfoweather.jobs;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.pcr.myinfoweather.models.currentweather.UserLocation;

/**
 * Created by Paula on 07/03/2015.
 */
public class GeolocationJob extends Job implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private static final int MAX_RETRYS = 5;
    private static final int PRIORITY = 1;
    private static final long DELAY = 2000;
    public GoogleApiClient mClient;

    private static final Params PARAMS = new Params(PRIORITY).persist().requireNetwork().delayInMs(DELAY);

    private UserLocation userLocation;
    private Context context;

    public GeolocationJob(UserLocation userLocation, Context context) {
        super(PARAMS);
        this.userLocation = userLocation;
        this.context = context;
    }

    @Override
    public void onAdded() {
        userLocation.save();
    }

    @Override
    public void onRun() throws Throwable {

    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void createGoogleApiClient(Context context) {
        mClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void connectClient(Context context) {
        createGoogleApiClient(context);
        mClient.connect();

    }

    public void disconnectClient() {
        mClient.disconnect();
    }
}
