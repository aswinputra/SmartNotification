package com.kasmartnotification.smartnotification.Controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.R;

public class NotificationAccessDialog extends AlertDialog {

    public NotificationAccessDialog(@NonNull Context context) {
        super(context);

        final Context ctx = context;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Allow Notification Access");
        
        LayoutInflater factory = LayoutInflater.from(context);
        final View view = factory.inflate(R.layout.notification_access_dialog, null);
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Constants.NOTIFICATION_ACCESS_SETTING);
                ctx.startActivity(intent);
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}