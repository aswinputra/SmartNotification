package com.kasmartnotification.smartnotification.Controller;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Model.Setting;
import com.kasmartnotification.smartnotification.R;
import com.kasmartnotification.smartnotification.Tools.SugarHelper;

import java.text.DecimalFormat;

import static com.kasmartnotification.smartnotification.Constants.INCREMENT_BY_HOUR;
import static com.kasmartnotification.smartnotification.Constants.INCREMENT_BY_MINUTE;

public class SettingsTime extends AppCompatActivity {

    private SeekBar focusPeriod, breakDuration;
    private TextView focusPeriodTime, focusPeriodHour, breakDurationTime, breakDurationMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_time);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        focusPeriodTime = findViewById(R.id.activity_settings_time_focus_period_time_text_view);
        focusPeriodHour = findViewById(R.id.activity_settings_time_focus_period_hour_text_view);
        breakDurationTime = findViewById(R.id.activity_settings_time_break_duration_time_text_view);
        breakDurationMin = findViewById(R.id.activity_settings_time_break_duration_minute_text_view);

        focusPeriod = findViewById(R.id.activity_settings_time_focus_period_seekBar);
        focusPeriod.setMax(330);
        int settingFocusTime;
        Setting TimeSetting = SugarHelper.findFromDB(Setting.class, Constants.FOCUS_TIME);
        settingFocusTime = getProgressBar(TimeSetting, INCREMENT_BY_HOUR, Constants.FOCUS_TIME);
        focusPeriod.setProgress(settingFocusTime-INCREMENT_BY_HOUR);
        if (settingFocusTime<60){
            String progress = Integer.toString(settingFocusTime);
            focusPeriodTime.setText(progress);
            focusPeriodHour.setText(R.string.minute);
        }else {
            String time = Double.toString(getFocusPeriodTime(settingFocusTime));
            focusPeriodTime.setText(time);
            focusPeriodHour.setText(R.string.hours);
        }
        focusPeriod.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (focusPeriodValue(i)<60){
                    String progress = Integer.toString(focusPeriodValue(i));
                    focusPeriodTime.setText(progress);
                    focusPeriodHour.setText(R.string.minute);
                }else {
                    String time = Double.toString(getFocusPeriodTime(focusPeriodValue(i)));
                    focusPeriodTime.setText(time);
                    focusPeriodHour.setText(R.string.hours);
                }
                seekBar.setProgress(i-(i%INCREMENT_BY_HOUR));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (focusPeriodValue(seekBar.getProgress())<60){
                    String progress = Integer.toString(focusPeriodValue(seekBar.getProgress()));
                    focusPeriodTime.setText(progress);
                    focusPeriodHour.setText(R.string.minute);
                }else {
                    String time = Double.toString(getFocusPeriodTime(focusPeriodValue(seekBar.getProgress())));
                    focusPeriodTime.setText(time);
                    focusPeriodHour.setText(R.string.hours);
                }
                long focusTimeInMilliseconds = focusPeriodValue(seekBar.getProgress());
                SugarHelper.createOrSetDBObject(Setting.class,Constants.FOCUS_TIME,null,null, null, focusTimeInMilliseconds);
            }
        });

        breakDuration = findViewById(R.id.activity_settings_time_break_duration_seekBar);
        breakDuration.setMax(55);
        Setting breakDurationSetting = SugarHelper.findFromDB(Setting.class, Constants.BREAK_DURATION);
        int settingBreakDuration = getProgressBar(breakDurationSetting, INCREMENT_BY_MINUTE, Constants.BREAK_DURATION);
        breakDuration.setProgress(settingBreakDuration-INCREMENT_BY_MINUTE);
        String breakDurationProgress = Integer.toString(breakDurationValue(breakDuration.getProgress()));
        breakDurationTime.setText(breakDurationProgress);
        breakDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String progress = Integer.toString(breakDurationValue(i));
                breakDurationTime.setText(progress);
                seekBar.setProgress(i-(i%INCREMENT_BY_MINUTE));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String progress = Integer.toString(breakDurationValue(seekBar.getProgress()));
                breakDurationTime.setText(progress);
                long breakTimeInMilliseconds = breakDurationValue(seekBar.getProgress());
                SugarHelper.createOrSetDBObject(Setting.class, Constants.BREAK_DURATION, null, null, null,breakTimeInMilliseconds);
            }
        });

    }

    private <T> int getProgressBar(T setting,int type, final String stringType) {
        int settingTime;
        if (setting!=null){
            settingTime = (int)((Setting)setting).getTime();
            return settingTime;
        }else{
            settingTime = type;
            SugarHelper.createOrSetDBObject(Setting.class,stringType, null,null, null, settingTime);
            return settingTime;
        }
    }

    private double getFocusPeriodTime(double seekBarValue){
        return seekBarValue/60;
    }

    private int focusPeriodValue(int seekBarValue){
        return seekBarValue + INCREMENT_BY_HOUR;
    }

    private int breakDurationValue(int seekBarValue){
        return seekBarValue + INCREMENT_BY_MINUTE;
    }
}
