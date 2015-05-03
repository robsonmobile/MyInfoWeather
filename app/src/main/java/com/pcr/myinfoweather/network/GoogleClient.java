package com.pcr.myinfoweather.network;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

/**
 * Created by Paula on 23/02/2015.
 */
public class GoogleClient implements GoogleApiClient.OnConnectionFailedListener,
                                        GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mGoogleClient;
    private static GoogleClient instance;
    private IListenerLocation mListener;

    public static GoogleClient getInstance() {
        if(instance == null) {
            instance = new GoogleClient();
        }
        return instance;
    }

    public GoogleClient() {
    }

    public interface IListenerLocation {
        void onGoogleClientConnected(boolean isFinishedRequest);
    }

    public void setListener(IListenerLocation mListener) {
        this.mListener = mListener;
    }

    public GoogleApiClient createGoogleApiClientIfNeeded(Context mContext) {
        if(mGoogleClient == null) {

            mGoogleClient = new GoogleApiClient.Builder(mContext)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        return mGoogleClient;
    }

    public GoogleApiClient getGoogleApiClient(Context context) {
        if(mGoogleClient == null) {
            createGoogleApiClientIfNeeded(context);
        }
        return mGoogleClient;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mListener.onGoogleClientConnected(true);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void connectClient() {
        mGoogleClient.connect();
    }

    public void disconnectClient() {
        mGoogleClient.disconnect();
    }
}
