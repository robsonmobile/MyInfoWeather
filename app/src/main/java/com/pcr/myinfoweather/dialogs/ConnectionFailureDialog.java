package com.pcr.myinfoweather.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.interfaces.IDialogConnectionFailure;

/**
 * Created by Paula on 19/11/2014.
 */
public class ConnectionFailureDialog extends DialogFragment {

    private IDialogConnectionFailure mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.message_connection_failure_dialog);
        builder.setPositiveButton(R.string.message_retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("dialog log: try button was clicked on the class");
                mListener.onPositiveClick(ConnectionFailureDialog.this);
            }
        });
        builder.setNegativeButton(R.string.message_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onNegativeClick(ConnectionFailureDialog.this);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (IDialogConnectionFailure) activity;
        } catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                " must implement listener");
        }

    }
}
