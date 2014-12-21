package com.pcr.myinfoweather.interfaces;

import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;

/**
 * Created by Paula on 19/11/2014.
 */
public interface IDialog {

    public void onPositiveClick(DialogFragment dialog);
    public void onNegativeClick(DialogFragment dialog);
    public String setMessage(DialogFragment dialog);
    public String setTitle(DialogFragment dialog);
    public int setIcon(DialogFragment dialog);
    public CharSequence[] setItems(DialogFragment dialog);
    public void onSelectedItem(DialogFragment dialog, int position);
    public int singleChoiceSelectedPosition();
    public CharSequence[] setSingleChoice(DialogFragment dialog);


}
