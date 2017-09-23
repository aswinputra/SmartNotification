package com.kasmartnotification.smartnotification.Controller;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.kasmartnotification.smartnotification.Adapter.ReminderMessageAdapter;
import com.kasmartnotification.smartnotification.Interfaces.OnDialogAddListener;
import com.kasmartnotification.smartnotification.Model.Keyword;
import com.kasmartnotification.smartnotification.Model.ReminderMessage;
import com.kasmartnotification.smartnotification.R;

import java.util.List;

import static com.kasmartnotification.smartnotification.Constants.TURN_ON;

public class SettingsReminderMessages extends AppCompatActivity implements OnDialogAddListener{

    private RecyclerView mReminderMessageRV;
    private ReminderMessageAdapter mReminderMessageAdapter;
    private List<ReminderMessage> mReminderMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_reminder_messages);
        Toolbar toolbar = findViewById(R.id.activity_reminder_messages_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // this will show users and let them edit to TURN ON reminder, because we are out of time to implement both TURN ON and TURN OFF
        mReminderMessages = ReminderMessage.find(ReminderMessage.class, "toturnon = ?", TURN_ON);
        mReminderMessageRV = findViewById(R.id.activity_settings_reminder_messages_recycler_view);
        mReminderMessageAdapter = new ReminderMessageAdapter(this, mReminderMessages);
        mReminderMessageRV.setLayoutManager(new LinearLayoutManager(this));
        mReminderMessageRV.setAdapter(mReminderMessageAdapter);

        FloatingActionButton fab = findViewById(R.id.activity_settings_reminder_messages_add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddKeywordDialog();
            }
        });
    }

    private void showAddKeywordDialog(){
        new AddDialog(this,"Reminder messages",this);
    }

    @Override
    public void onOK(String s) {
        addReminderMessage(s);
    }

    private void addReminderMessage(String name){
        if (!contains(name)){
            ReminderMessage reminderMessage = new ReminderMessage(name, TURN_ON);
            reminderMessage.save();
            mReminderMessages.add(reminderMessage);
            update();
        } else {
            Toast.makeText(this,"Reminder message already exist", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean contains(String name) {
        for(ReminderMessage reminderMessage: mReminderMessages){
            if (reminderMessage.is(name)){
                return true;
            }
        }
        return false;
    }

    private void update() {
        mReminderMessageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
