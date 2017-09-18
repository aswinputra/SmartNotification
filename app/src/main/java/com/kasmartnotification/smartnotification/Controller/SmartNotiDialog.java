package com.kasmartnotification.smartnotification.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kasmartnotification.smartnotification.Tools.CalendarHelper;
import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Interfaces.OnSmartNotiStartListener;
import com.kasmartnotification.smartnotification.Model.Setting;
import com.kasmartnotification.smartnotification.Model.Status;
import com.kasmartnotification.smartnotification.R;
import com.kasmartnotification.smartnotification.Services.FocusPeriodService;
import com.kasmartnotification.smartnotification.Services.NotificationListener;
import com.kasmartnotification.smartnotification.Services.SmartNotiService;
import com.kasmartnotification.smartnotification.Tools.SugarHelper;

import java.util.Calendar;

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
    private OnSmartNotiStartListener smartNotiStartListener;

    public SmartNotiDialog(Context context, OnSmartNotiStartListener smartNotiStartListener) {
        super(context);
        this.context = context;
        this.smartNotiStartListener = smartNotiStartListener;
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
                //check if it's unlimited or bounded by time
                if(onUntilOffRadio.isChecked()){
                    SugarHelper.createOrSetDBObject(Status.class, Constants.SMART_NOTIFICATION_UNBOUNDED, true, null, null);
                }else if(onForNhourRadio.isChecked()){
                    saveSmartNotiEndTime();
                    context.startService(new Intent(context, SmartNotiService.class));
                }
                context.startService(new Intent(context, FocusPeriodService.class));
                context.startService(new Intent(context, NotificationListener.class));
                smartNotiStartListener.onSmartNotiStart();
                dismiss();
            }
            break;
        }
    }

    private void increaseHour(boolean increase) {
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

    //TODO: should we think about when the minute change and users haven't decided yet
    //because if that's the case, then endTime shown and the endTime saved to DB will be different

    private void setTimeAndHint(int nHour){
        Resources res = context.getResources();

        String endTime = CalendarHelper.getTimeString(CalendarHelper.getAddedCalendar(smartNotiHour));
        String onForNHours = String.format(res.getString(R.string.on_for_n_hours), nHour);
        String onUntilFeedback = String.format(res.getString(R.string.on_until_when), endTime);

        onForNhourRadio.setText(onForNHours);
        onUntiFeedbackTv.setText(onUntilFeedback);
    }

    private void saveSmartNotiEndTime(){
        Calendar endTime = CalendarHelper.getAddedCalendar(smartNotiHour);
//        for testing
//        Calendar endTime = Calendar.getInstance();
//        endTime.add(Calendar.MINUTE, 3);
        SugarHelper.createOrSetDBObject(Setting.class, Constants.SMART_NOTIFICATION_END_TIME, null, null, endTime);
    }
}
