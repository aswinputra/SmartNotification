package com.kasmartnotification.smartnotification.Controller;

import android.content.DialogInterface;
import android.os.Bundle;
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

import com.kasmartnotification.smartnotification.Adapter.KeywordsAdapter;
import com.kasmartnotification.smartnotification.Model.Keyword;
import com.kasmartnotification.smartnotification.R;

import java.util.ArrayList;

public class SettingsKeywords extends AppCompatActivity {

    private KeywordsAdapter mKeywordsAdapter;
    private RecyclerView KeywordRecyclerView;
    private Keyword mKeyword;
    private ArrayList<Keyword> mKeywords;
    private String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_keywords);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mKeywords = new ArrayList<>();

        KeywordRecyclerView = findViewById(R.id.activity_settings_keywords_recycler_view);
        mKeywordsAdapter = new KeywordsAdapter(this, mKeywords);
        KeywordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        KeywordRecyclerView.setAdapter(mKeywordsAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_settings_keywords_add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //TODO:add keyword
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsKeywords.this);
                builder.setTitle("Add Keyword");

                final EditText input = new EditText(SettingsKeywords.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        keyword = input.getText().toString();
                        mKeyword = new Keyword(keyword);
                        mKeywords.add(mKeyword);
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
        mKeywordsAdapter.notifyDataSetChanged();
    }

}
