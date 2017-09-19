package com.kasmartnotification.smartnotification.Controller;


import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kasmartnotification.smartnotification.Interfaces.OnDialogAddListener;
import com.kasmartnotification.smartnotification.Interfaces.OnDialogClickedListener;
import com.kasmartnotification.smartnotification.Model.Place;
import com.kasmartnotification.smartnotification.R;

public class DeleteDialog extends AlertDialog {

    private Context mContext;

    public DeleteDialog(@NonNull Context context, final Object object, final OnDialogClickedListener listener) {
        super(context);
        mContext = context;

        FrameLayout container = new FrameLayout(mContext);
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = (int)mContext.getResources().getDimension(R.dimen.delete_dialog_margin);
        lp.setMargins(margin, margin, margin, margin);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if(object instanceof Place) {
            builder.setTitle("Remove Place");
        }

        final TextView input = new TextView(mContext);
        if(object instanceof Place) {
            input.setText(((Place) object).getName());
        }
        input.setLayoutParams(lp);
        container.addView(input);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(container);

        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDelete(object);
            }
        });

        builder.show();
    }
}