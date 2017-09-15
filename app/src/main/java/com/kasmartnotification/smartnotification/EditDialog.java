package com.kasmartnotification.smartnotification;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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

        FrameLayout container = new FrameLayout(mContext);
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMarginStart((int)mContext.getResources().getDimension(R.dimen.activity_margin));
        lp.setMarginEnd((int)mContext.getResources().getDimension(R.dimen.activity_margin));


        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        if (object instanceof ImportantSender) {
            builder.setTitle("Edit name");
        } else if (object instanceof Keyword) {
            builder.setTitle("Edit keyword");
        }

        final EditText input = new EditText(mContext);
        input.setLayoutParams(lp);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        if (object instanceof ImportantSender) {
            input.setText(((ImportantSender) object).getName());
        } else if (object instanceof Keyword) {
            input.setText(((Keyword) object).getName());
        }
        container.addView(input);
        builder.setView(container);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onOK(object, input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNeutralButton("Delete", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                listener.onDelete(object);
            }
        });
        builder.show();
    }

}
