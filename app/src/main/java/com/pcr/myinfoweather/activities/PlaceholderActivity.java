package com.pcr.myinfoweather.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.helpers.ConnectivityHelpers;
import com.pcr.myinfoweather.utils.Constants;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Paula Rosa on 24/04/2015.
 */
public class PlaceholderActivity extends BaseActivity {

    private String choicePlaceholder;

    @InjectView(R.id.retryBtn) Button btnRetry;
    @InjectView(R.id.placeholder_image) ImageView image;
    @InjectView(R.id.placeholder_text) TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                choicePlaceholder= null;
            } else {
                choicePlaceholder= extras.getString("type");
            }
        } else {
            choicePlaceholder= (String) savedInstanceState.getSerializable("type");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPlaceholderImage(choicePlaceholder);
    }

    @Override
    protected int layoutToInflate() {
        return R.layout.placeholder;
    }

    @Override
    protected boolean checkSessionOnStart() {
        return false;
    }

    @OnClick(R.id.retryBtn) void retrySession() {
        if(isGPSPlaceholder(choicePlaceholder)) {
            if(hasGPSConnection()) {
                finish();
            } else {
                Toast.makeText(this, Constants.TURN_ON_GPS, Toast.LENGTH_LONG).show();
            }
        } else if(isBadConnectionPlaceholder(choicePlaceholder)) {
            if(hasInternetConnection()) {
                finish();
            } else {
                Toast.makeText(this, Constants.TURN_ON_INTERNET, Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean hasInternetConnection() {
        return ConnectivityHelpers.hasConnectivity(this);
    }

    private boolean hasGPSConnection() {
        return ConnectivityHelpers.hasGPS(this);
    }

    private boolean isBadConnectionPlaceholder(String choicePlaceholder) {
        return choicePlaceholder.equals(Constants.BAD_CONNECTION);
    }

    private boolean isGPSPlaceholder(String choicePlaceholder) {
        return choicePlaceholder.equals(Constants.GPS_OFF);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void setPlaceholderImage(String chooser){
        if(isGPSPlaceholder(chooser)) {
            image.setImageResource(R.drawable.no_gps_image);
            text.setText(Constants.GPS_CONNECTION_FAILURE);
        } else if(isBadConnectionPlaceholder(chooser)) {
            image.setImageResource(R.drawable.no_connection_image);
            text.setText(Constants.INTERNET_CONNECTION_FAILURE);
        }

    }
}
