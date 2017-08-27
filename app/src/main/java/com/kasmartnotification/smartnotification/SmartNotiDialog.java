package com.kasmartnotification.smartnotification;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by kiman on 27/8/17.
 */

public class SmartNotiDialog extends AppCompatDialog implements View.OnClickListener {

    private Button cancelBtn;
    private Button doneBtn;
    private ImageButton increaseBtn;
    private ImageButton decreaseBtn;
    private RadioGroup radioGroup;
    private RadioButton onUntilOffRadio;
    private RadioButton onForNhourRadio;
    private TextView onUntiFeedbackTv;
    private Context context;
    private static int smartNotiHour = 1;

    public SmartNotiDialog(Context context) {
        super(context);
        this.context = context;
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        cancelBtn = findViewById(R.id.dialog_smart_noti_cancel_btn);
        doneBtn = findViewById(R.id.dialog_smart_noti_done_btn);
        increaseBtn = findViewById(R.id.dialog_smart_noti_increase_hour_btn);
        decreaseBtn = findViewById(R.id.dialog_smart_noti_decrease_hour_btn);
        radioGroup = findViewById(R.id.dialog_smart_noti_radiogroup);
        onUntilOffRadio = findViewById(R.id.dialog_smart_noti_on_until_off_radio);
        onForNhourRadio = findViewById(R.id.dialog_smart_noti_on_for_radio);
        onUntiFeedbackTv = findViewById(R.id.dialog_smart_noti_on_until_feedback_tv);

        cancelBtn.setOnClickListener(this);
        doneBtn.setOnClickListener(this);
        increaseBtn.setOnClickListener(this);
        decreaseBtn.setOnClickListener(this);

        onUntilOffRadio.setChecked(true); //set default
        setTimeAndHint(smartNotiHour);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_smart_noti_cancel_btn: {
                dismiss();
            }
            break;
            case R.id.dialog_smart_noti_increase_hour_btn: {
                onForNhourRadio.setChecked(true);
                onUntilOffRadio.setChecked(false);
                increaseHour(true);
            }
            break;
            case R.id.dialog_smart_noti_decrease_hour_btn: {
                onForNhourRadio.setChecked(true);
                onUntilOffRadio.setChecked(false);
                increaseHour(false);
            }
            break;
            case R.id.dialog_smart_noti_done_btn: {
                context.startService(new Intent(context, SmartNotiService.class));
                dismiss();
            }
            break;
        }
    }

    private void increaseHour(boolean increase) {
        Resources res = context.getResources();
        if(increase) {
            smartNotiHour++;
        }else{
            smartNotiHour--;
        }
        if(smartNotiHour > 1 && smartNotiHour < 24) {
            decreaseBtn.setEnabled(true);
            increaseBtn.setEnabled(true);
        }else if(smartNotiHour <= 1){
            decreaseBtn.setEnabled(false);
        }else if(smartNotiHour >= 24){
            increaseBtn.setEnabled(false);
        }

        setTimeAndHint(smartNotiHour);

    }

    private void setTimeAndHint(int nHour){
        Resources res = context.getResources();

        int hour = Utility.getCurrentHour(smartNotiHour);
        String amPM = "";
        if(hour < 12){
            amPM = "AM";
        }else{
            hour = hour -12;
            amPM = "PM";
        }

        String onForNHours = String.format(res.getString(R.string.on_for_n_hours), nHour);
        String onUntilFeedback = String.format(res.getString(R.string.on_until_when), hour, Utility.getCurrentMinute(), amPM);
        onForNhourRadio.setText(onForNHours);
        onUntiFeedbackTv.setText(onUntilFeedback);
    }
}
