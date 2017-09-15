package com.kasmartnotification.smartnotification;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.kasmartnotification.smartnotification.Interfaces.OnDialogAddListener;

/**
 * Created by aswinhartono on 15/9/17.
 */

public class AddDialog extends AlertDialog{

    private Context mContext;

    public AddDialog(@NonNull Context context, String type, final OnDialogAddListener listener) {
        super(context);
        mContext = context;

        FrameLayout container = new FrameLayout(mContext);
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMarginStart((int)mContext.getResources().getDimension(R.dimen.activity_margin));
        lp.setMarginEnd((int)mContext.getResources().getDimension(R.dimen.activity_margin));

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Add " + type);

        final EditText input = new EditText(mContext);
        input.setLayoutParams(lp);
        container.addView(input);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(container);

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onOK(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
