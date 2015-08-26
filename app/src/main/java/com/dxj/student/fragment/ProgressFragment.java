package com.dxj.student.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by kings on 8/26/2015.
 */
public class ProgressFragment extends DialogFragment {

    public static ProgressFragment newInstance() {
        ProgressFragment frag = new ProgressFragment();
        frag.setRetainInstance(true);
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("登录中...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);

        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {


        super.onCancel(dialog);
    }
}

