package com.pcr.myinfoweather.interfaces;

import android.support.v4.app.DialogFragment;

/**
 * Created by Paula on 19/11/2014.
 */
public interface IDialog {

    public void onPositiveClick(DialogFragment dialog);
    public void onNegativeClick(DialogFragment dialog);
    public String setMessage(DialogFragment dialog);
    public String setTitle(DialogFragment dialog);
    public CharSequence[] setItems(DialogFragment dialog);
    public void onSelectedItem(int position);

}
