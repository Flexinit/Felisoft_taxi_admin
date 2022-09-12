package com.example.drupp_driver.ui.dialogs;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.IDialogButtonClickListener;

public class CompletedDialog extends DialogFragment {
    IDialogButtonClickListener iDialogButtonClickListener;
    int resourceId;
    public CompletedDialog(IDialogButtonClickListener iDialogButtonClickListener, int resourceId){
        this.iDialogButtonClickListener=iDialogButtonClickListener;
        this.resourceId=resourceId;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


                View view = inflater.inflate(resourceId, container, false);
                iDialogButtonClickListener.setUpDialogViews(view);
                Button btnComplete=view.findViewById(R.id.btnComplete);
                btnComplete.setOnClickListener(v -> {
                    dismiss();

                    iDialogButtonClickListener.onButtonClick();
                });



        return view;
    }


}
