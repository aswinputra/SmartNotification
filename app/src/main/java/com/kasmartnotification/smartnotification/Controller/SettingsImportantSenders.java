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
import android.widget.Toast;

import com.kasmartnotification.smartnotification.Adapter.ImportantSendersAdapter;
import com.kasmartnotification.smartnotification.AddDialog;
import com.kasmartnotification.smartnotification.Interfaces.OnDialogAddListener;
import com.kasmartnotification.smartnotification.Model.ImportantSender;
import com.kasmartnotification.smartnotification.R;
import com.kasmartnotification.smartnotification.Utility;

import java.util.ArrayList;
import java.util.List;

import static com.kasmartnotification.smartnotification.Constants.PICK_CONTACT;

public class SettingsImportantSenders extends AppCompatActivity implements OnDialogAddListener{

    private RecyclerView importantSendersRecyclerView;
    private ImportantSendersAdapter mSenderAdapter;
    private List<ImportantSender> mSenders;
    private ImportantSender sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_important_senders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_important_senders_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSenders = ImportantSender.listAll(ImportantSender.class);
        if (mSenders == null){
            mSenders = new ArrayList<>();
        }

        importantSendersRecyclerView = findViewById(R.id.activity_settings_important_senders_recycler_view);
        mSenderAdapter = new ImportantSendersAdapter(this, mSenders);
        importantSendersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        importantSendersRecyclerView.setAdapter(mSenderAdapter);

        FloatingActionButton fab = findViewById(R.id.activity_settings_important_senders_add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddSenderDialog();
            }
        });

    }

    private void showAddSenderDialog(){
        AddDialog addDialog = new AddDialog(this,"Sender", this);
    }

    private void addNewSender(String name){
        if (!contains(name)) {
            sender = new ImportantSender(name);
            sender.save();
            mSenders.add(sender);
            update();
        }else {
            Toast.makeText(this, "Name already exists",Toast.LENGTH_SHORT).show();
        }

    }

    private boolean contains(String name){
        for(ImportantSender sender: mSenders){
            if (sender.is(name)){
                return true;
            }
        }
        return false;
    }

    private void update() {
        mSenderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onOK(String s) {
        addNewSender(s);
    }
}
