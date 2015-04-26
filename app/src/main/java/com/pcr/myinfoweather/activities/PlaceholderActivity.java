package com.pcr.myinfoweather.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.pcr.myinfoweather.R;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Paula Rosa on 24/04/2015.
 */
public class PlaceholderActivity extends BaseActivity implements View.OnClickListener {


    @InjectView(R.id.retryBtn) Button btnRetry;

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
    public void onClick(View v) {
        if(v.getId() == R.id.retryBtn) {

        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
