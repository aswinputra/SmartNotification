package com.kasmartnotification.smartnotification;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

import com.kasmartnotification.smartnotification.Interfaces.OnDialogClickedListener;
import com.kasmartnotification.smartnotification.Model.ImportantSender;
import com.kasmartnotification.smartnotification.Model.Keyword;

/**
 * Created by aswinhartono on 15/9/17.
 */

public class EditDialog extends AlertDialog {

    private Context mContext;

    public EditDialog(@NonNull Context context, final Object object, final OnDialogClickedListener listener) {
        super(context);
        mContext = context;

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        if (object instanceof ImportantSender) {
            builder.setTitle("Edit name");
        } else if (object instanceof Keyword) {
            builder.setTitle("Edit keyword");
        }

        final EditText input = new EditText(mContext);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        if (object instanceof ImportantSender) {
            input.setText(((ImportantSender) object).getName());
        } else if (object instanceof Keyword) {
            input.setText(((Keyword) object).getName());
        }
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onOK(object, input.getText().toString());
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDelete(object);
            }
        });
        builder.setNeutralButton("Cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        builder.show();
    }

}
