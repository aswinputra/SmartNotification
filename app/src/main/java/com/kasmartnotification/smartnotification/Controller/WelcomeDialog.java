package com.kasmartnotification.smartnotification.Controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kasmartnotification.smartnotification.R;
import com.kasmartnotification.smartnotification.Tools.Utility;

/**
 * Created by aswinhartono on 21/9/17.
 */

public class WelcomeDialog extends AlertDialog {

    protected WelcomeDialog(@NonNull final Context context) {
        super(context);
        final Context mContext = context;

        FrameLayout container = new FrameLayout(context);
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMarginStart((int)context.getResources().getDimension(R.dimen.delete_dialog_margin));
        lp.setMarginEnd((int)context.getResources().getDimension(R.dimen.delete_dialog_margin));
        lp.setMargins(0,(int)context.getResources().getDimension(R.dimen.delete_dialog_margin),0, (int)context.getResources().getDimension(R.dimen.delete_dialog_margin));

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Welcome to Smart Notification");
        final TextView description = new TextView(context);
        description.setText(context.getString(R.string.welcome_dialog_description));
        description.setTextSize(16);
        description.setLineSpacing(1,(float)1.3);
        description.setLayoutParams(lp);

        container.addView(description);
        builder.setView(container);

        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mContext, Settings.class);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utility.getNotificationAccessPermission(mContext);
            }
        });
        builder.show();
    }
}
