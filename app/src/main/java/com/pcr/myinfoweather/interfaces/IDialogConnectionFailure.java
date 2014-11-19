package com.pcr.myinfoweather.interfaces;

import android.support.v4.app.DialogFragment;

/**
 * Created by Paula on 19/11/2014.
 */
public interface IDialogConnectionFailure {

    public void onPositiveClick(DialogFragment dialog);
    public void onNegativeClick(DialogFragment dialog);
    public void setMessage(DialogFragment dialog);

}
