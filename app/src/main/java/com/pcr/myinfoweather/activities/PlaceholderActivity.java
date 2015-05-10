package com.pcr.myinfoweather.activities;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.enums.PlaceholderChooser;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Paula Rosa on 24/04/2015.
 */
public class PlaceholderActivity extends BaseActivity {

    private ImageView placeholderImage;
    private PlaceholderChooser placeholderChooser;

    public PlaceholderActivity(PlaceholderChooser chooser) {
        this.placeholderChooser = chooser;
    }

    @InjectView(R.id.retryBtn) Button btnRetry;
    @InjectView(R.id.placeholder_image) ImageView image;

    @Override
    protected int layoutToInflate() {
        return R.layout.placeholder;
    }

    @Override
    protected boolean checkSessionOnStart() {
        return false;
    }

    @OnClick(R.id.retryBtn) void retrySession() {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

//    private void showPlaceholderImage(PlaceholderChooser choosed){
//        if( == 0) {
//            image.setImageResource(R.drawable.ic_launcher);
//        } else if(imageChooser == 1) {
//            image.setImageResource(R.drawable.ic_launcher);
//        }
//
//    }
}
