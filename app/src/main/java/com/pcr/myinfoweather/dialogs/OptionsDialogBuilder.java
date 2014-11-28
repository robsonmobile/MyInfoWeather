package com.pcr.myinfoweather.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.interfaces.IDialog;

/**
 * Created by Paula on 27/11/2014.
 */
public class OptionsDialogBuilder extends DialogFragment{

    private IDialog mListener;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(mListener.setTitle(OptionsDialogBuilder.this));
        builder.setItems(mListener.setItems(OptionsDialogBuilder.this), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("log which: " + which);
                mListener.onSelectedItem(which);
            }
        });

        builder.setNegativeButton(R.string.message_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onNegativeClick(OptionsDialogBuilder.this);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IDialog) activity;
        } catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement listener");
        }
    }
}
