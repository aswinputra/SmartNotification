package com.kasmartnotification.smartnotification.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.kasmartnotification.smartnotification.R;

public class SettingsKeywordsResponse extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout allowImportantSenders, importantSenders;
    private Switch allowImportantSendersSwitch;
    private LinearLayout allowKeywords, keywords;
    private Switch allowKeywordsSwitch;
    private LinearLayout automaticResponse, replyMessages;
    private Switch automaticResponseSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_keywords_response);

        allowImportantSenders = findViewById(R.id.activity_settings_keywords_response_allow_important_senders_linear_layout);
        importantSenders = findViewById(R.id.activity_settings_keywords_response_important_senders_linear_layout);
        allowImportantSendersSwitch = findViewById(R.id.activity_settings_keywords_response_allow_important_senders_switch);

        allowImportantSendersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //TODO:allow important senders through
                }else{
                    //TODO:do not allow anyone through
                }
            }
        });

        allowKeywords = findViewById(R.id.activity_settings_keywords_response_allow_keywords_linear_layout);
        keywords = findViewById(R.id.activity_settings_keywords_response_keywords_linear_layout);
        allowKeywordsSwitch = findViewById(R.id.activity_settings_keywords_response_allow_keywords_switch);

        allowKeywordsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //TODO: allow keywords through
                }else{
                    //TODO: do not allow keywords through
                }
            }
        });

        automaticResponse = findViewById(R.id.activity_settings_keywords_response_automatic_response_linear_layout);
        replyMessages = findViewById(R.id.activity_settings_keywords_response_reply_messages_linear_layout);
        automaticResponseSwitch = findViewById(R.id.activity_settings_keywords_response_automatic_response_switch);
        automaticResponseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //TODO: automatic response on
                }else{
                    //TODO: automatic response off
                }
            }
        });

        importantSenders.setOnClickListener(this);
        allowImportantSenders.setOnClickListener(this);
        allowKeywords.setOnClickListener(this);
        keywords.setOnClickListener(this);
        automaticResponse.setOnClickListener(this);
        replyMessages.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.activity_settings_keywords_response_allow_important_senders_linear_layout:
                if (allowImportantSendersSwitch.isChecked()){
                    allowImportantSendersSwitch.setChecked(false);
                }else{
                    allowImportantSendersSwitch.setChecked(true);
                }
                break;
            case R.id.activity_settings_keywords_response_important_senders_linear_layout:
                intent = new Intent(this, SettingsImportantSenders.class);
                startActivity(intent);
                break;
            case R.id.activity_settings_keywords_response_allow_keywords_linear_layout:
                if (allowKeywordsSwitch.isChecked()){
                    allowKeywordsSwitch.setChecked(false);
                }else {
                    allowKeywordsSwitch.setChecked(true);
                }
                break;
            case R.id.activity_settings_keywords_response_keywords_linear_layout:
                intent = new Intent(this, SettingsKeywords.class);
                startActivity(intent);
                break;
            case R.id.activity_settings_keywords_response_automatic_response_linear_layout:
                if (automaticResponseSwitch.isChecked()){
                    automaticResponseSwitch.setChecked(false);
                }else{
                    automaticResponseSwitch.setChecked(true);
                }
                break;
            case R.id.activity_settings_keywords_response_reply_messages_linear_layout:
                //TODO: automatic response page
                break;
        }
    }
}
