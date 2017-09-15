package com.kasmartnotification.smartnotification.Controller;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kasmartnotification.smartnotification.R;

import java.text.DecimalFormat;

import static com.kasmartnotification.smartnotification.Constants.INCREMENT_BY_HOUR;
import static com.kasmartnotification.smartnotification.Constants.INCREMENT_BY_MINUTE;

public class SettingsTime extends AppCompatActivity {

    private SeekBar focusPeriod, breakDuration;
    private TextView focusPeriodTime, focusPeriodHour, breakDurationTime, breakDurationMin;
    private static DecimalFormat timeFormat = new DecimalFormat(".#");

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
        focusPeriod.setProgress(0);
        String focusPeriodProgress = Integer.toString(focusPeriodValue(focusPeriod.getProgress()));
        focusPeriodTime.setText(focusPeriodProgress);
        focusPeriodHour.setText(R.string.minute);
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
            }
        });

        breakDuration = findViewById(R.id.activity_settings_time_break_duration_seekBar);
        breakDuration.setMax(55);
        breakDuration.setProgress(0);
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
            }
        });

    }

    private double getFocusPeriodTime(double seekBarValue){
        double theTime = seekBarValue/60;
        return theTime;
    }

    private int focusPeriodValue(int seekBarValue){
        int theTime = seekBarValue + INCREMENT_BY_HOUR;
        return theTime;
    }

    private int breakDurationValue(int seekBarValue){
        int theTime = seekBarValue + INCREMENT_BY_MINUTE;
        return theTime;
    }
}
