package com.kasmartnotification.smartnotification.Controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.kasmartnotification.smartnotification.Adapter.ImportantSendersAdapter;
import com.kasmartnotification.smartnotification.Model.ImportantSender;
import com.kasmartnotification.smartnotification.R;

import java.util.ArrayList;

import static com.kasmartnotification.smartnotification.Constants.PICK_CONTACT;

public class SettingsImportantSenders extends AppCompatActivity {

    private RecyclerView importantSendersRecyclerView;
    private ImportantSendersAdapter mSenderAdapter;
    private ArrayList<ImportantSender> mSenders;
    private ImportantSender sender;
    private String senderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_important_senders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSenders = new ArrayList<>();

        importantSendersRecyclerView = findViewById(R.id.activity_settings_important_senders_recycler_view);
        mSenderAdapter = new ImportantSendersAdapter(this, mSenders);
        importantSendersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        importantSendersRecyclerView.setAdapter(mSenderAdapter);

        FloatingActionButton fab = findViewById(R.id.activity_settings_important_senders_add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //TODO: add new important senders

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsImportantSenders.this);
                builder.setTitle("Add Sender Name");

                final EditText input = new EditText(SettingsImportantSenders.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        senderName = input.getText().toString();
                        sender = new ImportantSender(senderName);
                        mSenders.add(sender);
                        update();
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
        });

    }

    private void update() {
        mSenderAdapter.notifyDataSetChanged();
    }
}
