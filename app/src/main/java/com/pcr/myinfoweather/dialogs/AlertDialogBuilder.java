package com.pcr.myinfoweather.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.interfaces.IDialog;
import com.pcr.myinfoweather.utils.Constants;

/**
 * Created by Paula on 19/11/2014.
 */
public class AlertDialogBuilder extends DialogFragment {

    private int mNum;

    private IDialog mListener;

    public static AlertDialogBuilder newInstance(int dialogType) {
        AlertDialogBuilder alertDialogBuilder = new AlertDialogBuilder();
        Bundle args = new Bundle();
        args.putInt("dialogType", dialogType);

        System.out.println("log args: " + args.getInt("dialogType"));
        alertDialogBuilder.setArguments(args);

        return alertDialogBuilder;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        mNum = getArguments().getInt("dialogType");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(mListener.setTitle(AlertDialogBuilder.this));
        builder.setNegativeButton(R.string.message_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onNegativeClick(AlertDialogBuilder.this);
            }
        });

        if(mNum == Constants.DIALOG_TYPE_ALERT) {
            builder.setMessage(mListener.setMessage(AlertDialogBuilder.this));
            builder.setPositiveButton(R.string.message_retry, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.out.println("dialog log: try button was clicked on the class");
                    mListener.onPositiveClick(AlertDialogBuilder.this);
                }
            });

        } else if(mNum == Constants.DIALOG_TYPE_OPTIONS) {
            builder.setItems(mListener.setItems(AlertDialogBuilder.this), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.out.println("log which: " + which);
                    mListener.onSelectedItem(AlertDialogBuilder.this, which);
                }
            });

        } else if(mNum == Constants.DIALOG_TYPE_CUSTOM) {
            builder.setIcon(mListener.setIcon(AlertDialogBuilder.this));

        } else if(mNum == Constants.DIALOG_TYPE_SINGLECHOICE) {
            int selectedPosPref = mListener.singleChoiceSelectedPosition();
            builder.setSingleChoiceItems(mListener.setSingleChoice(AlertDialogBuilder.this),
                    selectedPosPref, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mListener.onSelectedItem(AlertDialogBuilder.this, which);
                        }
                    });
            builder.setNegativeButton(R.string.message_close, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mListener.onNegativeClick(AlertDialogBuilder.this);
                }
            });
        }
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
