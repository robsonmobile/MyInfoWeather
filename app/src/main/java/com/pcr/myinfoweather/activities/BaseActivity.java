package com.pcr.myinfoweather.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.inputmethod.InputMethodManager;

import com.pcr.myinfoweather.utils.UserSessionManager;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Paula Rosa on 26/03/2015.
 */
public abstract class BaseActivity extends ActionBarActivity {

    private InputMethodManager imm;

    protected abstract int layoutToInflate();

    protected abstract boolean checkSessionOnStart();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setContentView(layoutToInflate());
        ButterKnife.inject(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override protected void onStart() {
        super.onStart();
    }


    protected void hideKeyboard() {

        if (imm.isActive()) {
            if (android.os.Build.VERSION.SDK_INT < 11) {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            } else {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
