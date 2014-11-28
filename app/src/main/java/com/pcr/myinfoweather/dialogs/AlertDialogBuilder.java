package com.pcr.myinfoweather.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.interfaces.IDialog;

/**
 * Created by Paula on 19/11/2014.
 */
public class AlertDialogBuilder extends DialogFragment {

    private IDialog mListener;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(mListener.setTitle(AlertDialogBuilder.this));
        builder.setMessage(mListener.setMessage(AlertDialogBuilder.this));
        builder.setPositiveButton(R.string.message_retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("dialog log: try button was clicked on the class");
                mListener.onPositiveClick(AlertDialogBuilder.this);
            }
        });
        builder.setNegativeButton(R.string.message_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onNegativeClick(AlertDialogBuilder.this);
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
