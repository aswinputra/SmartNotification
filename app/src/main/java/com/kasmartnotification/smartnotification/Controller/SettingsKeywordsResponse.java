package com.kasmartnotification.smartnotification.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Model.ImportantSender;
import com.kasmartnotification.smartnotification.Model.Keyword;
import com.kasmartnotification.smartnotification.Model.Setting;
import com.kasmartnotification.smartnotification.R;
import com.kasmartnotification.smartnotification.Tools.SugarHelper;
import com.kasmartnotification.smartnotification.Tools.Utility;

import java.util.List;

public class SettingsKeywordsResponse extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout allowImportantSenders, importantSenders;
    private Switch allowImportantSendersSwitch;
    private TextView importantSendersTextView;
    private LinearLayout allowKeywords, keywords;
    private Switch allowKeywordsSwitch;
    private TextView keywordsTextView;
    private LinearLayout automaticResponse, replyMessages;
    private Switch automaticResponseSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_keywords_response);

        allowImportantSenders = findViewById(R.id.activity_settings_keywords_response_allow_important_senders_linear_layout);
        importantSenders = findViewById(R.id.activity_settings_keywords_response_important_senders_linear_layout);
        allowImportantSendersSwitch = findViewById(R.id.activity_settings_keywords_response_allow_important_senders_switch);
        importantSendersTextView = findViewById(R.id.activity_settings_keywords_response_important_senders_text_view);
        boolean booleanSender = false;
        boolean booleanKeyword = false;
        boolean booleanAutoResponse = false;

        Setting allowImportantSndrSetting = SugarHelper.findFromDB(Setting.class, Constants.IMPORTANT_SENDER);
        booleanSender = Utility.getSwitchValue(allowImportantSndrSetting, Constants.IMPORTANT_SENDER);
        //remove the listener so it does not go to Change Listener when the activity start running
        allowImportantSendersSwitch.setOnCheckedChangeListener(null);
        allowImportantSendersSwitch.setChecked(booleanSender);

        if (booleanSender){
            importantSenders.setVisibility(View.VISIBLE);
        }else {
            importantSenders.setVisibility(View.GONE);
        }

        List<ImportantSender> senders = ImportantSender.listAll(ImportantSender.class);

        if (!senders.isEmpty()){
            importantSendersTextView.setText(Utility.getNames(senders));
        }else{
            importantSendersTextView.setText("Add your Important Senders here");
        }

        allowImportantSendersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked){
                    //TODO:allow important senders through
                    importantSenders.setVisibility(View.VISIBLE);
                }else{
                    //TODO:do not allow anyone through
                    importantSenders.setVisibility(View.GONE);
                }
                SugarHelper.createOrSetDBObject(Setting.class, Constants.IMPORTANT_SENDER, null, null, null, 0, checked);
            }
        });

        allowKeywords = findViewById(R.id.activity_settings_keywords_response_allow_keywords_linear_layout);
        keywords = findViewById(R.id.activity_settings_keywords_response_keywords_linear_layout);
        allowKeywordsSwitch = findViewById(R.id.activity_settings_keywords_response_allow_keywords_switch);
        keywordsTextView = findViewById(R.id.activity_settings_keywords_response_keywords_text_view);

        Setting allowKeywordsSetting = SugarHelper.findFromDB(Setting.class, Constants.IMPORTANT_KEYWORDS);
        booleanKeyword = Utility.getSwitchValue(allowKeywordsSetting, Constants.IMPORTANT_KEYWORDS);
        //remove the listener so it does not go to Change Listener when the activity start running
        allowKeywordsSwitch.setOnCheckedChangeListener(null);
        allowKeywordsSwitch.setChecked(booleanKeyword);

        if (booleanKeyword){
            keywords.setVisibility(View.VISIBLE);
        }else{
            keywords.setVisibility(View.GONE);
        }

        List<Keyword> keywordsList = Keyword.listAll(Keyword.class);
        if (!keywordsList.isEmpty()){
            keywordsTextView.setText(Utility.getNames(keywordsList));
        }else{
            keywordsTextView.setText("Add your Keywords here");
        }

        allowKeywordsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //TODO: allow keywords through
                    keywords.setVisibility(View.VISIBLE);
                }else{
                    //TODO: do not allow keywords through
                    keywords.setVisibility(View.GONE);
                }
                SugarHelper.createOrSetDBObject(Setting.class, Constants.IMPORTANT_KEYWORDS, null, null, null, 0, b);
            }
        });

//        automaticResponse = findViewById(R.id.activity_settings_keywords_response_automatic_response_linear_layout);
//        replyMessages = findViewById(R.id.activity_settings_keywords_response_reply_messages_linear_layout);
//        automaticResponseSwitch = findViewById(R.id.activity_settings_keywords_response_automatic_response_switch);
//
//        Setting automaticResponseSetting = SugarHelper.findFromDB(Setting.class, Constants.AUTOMATIC_RESPONSE);
//        booleanAutoResponse = Utility.getSwitchValue(automaticResponseSetting, Constants.AUTOMATIC_RESPONSE);
        //remove the listener so it does not go to Change Listener when the activity start running
//        automaticResponseSwitch.setOnCheckedChangeListener(null);
//        automaticResponseSwitch.setChecked(booleanAutoResponse);
//
//        automaticResponseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b){
//                    //TODO: automatic response on
//                }else{
//                    //TODO: automatic response off
//                }
//                SugarHelper.createOrSetDBObject(Setting.class, Constants.AUTOMATIC_RESPONSE, null, null, null, 0, b);
//            }
//        });

        importantSenders.setOnClickListener(this);
        allowImportantSenders.setOnClickListener(this);
        allowKeywords.setOnClickListener(this);
        keywords.setOnClickListener(this);
//        automaticResponse.setOnClickListener(this);
//        replyMessages.setOnClickListener(this);
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
//            case R.id.activity_settings_keywords_response_automatic_response_linear_layout:
//                if (automaticResponseSwitch.isChecked()){
//                    automaticResponseSwitch.setChecked(false);
//                }else{
//                    automaticResponseSwitch.setChecked(true);
//                }
//                break;
//            case R.id.activity_settings_keywords_response_reply_messages_linear_layout:
//                //TODO: automatic response page
//                break;
        }
    }
}
