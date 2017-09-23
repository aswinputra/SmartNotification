package com.kasmartnotification.smartnotification.Controller;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.kasmartnotification.smartnotification.Adapter.KeywordsAdapter;
import com.kasmartnotification.smartnotification.Interfaces.OnDialogAddListener;
import com.kasmartnotification.smartnotification.Model.Keyword;
import com.kasmartnotification.smartnotification.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsKeywords extends AppCompatActivity implements OnDialogAddListener{

    private KeywordsAdapter mKeywordsAdapter;
    private RecyclerView KeywordRecyclerView;
    private Keyword mKeyword;
    private List<Keyword> mKeywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_keywords);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_keywords_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mKeywords = Keyword.listAll(Keyword.class);
        if (mKeywords == null) {
            mKeywords = new ArrayList<>();
        }

        KeywordRecyclerView = findViewById(R.id.activity_settings_keywords_recycler_view);
        mKeywordsAdapter = new KeywordsAdapter(this, mKeywords);
        KeywordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        KeywordRecyclerView.setAdapter(mKeywordsAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_settings_keywords_add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddKeywordDialog();
            }
        });
    }

    private void showAddKeywordDialog(){
        AddDialog addDialog=new AddDialog(this,"Keywords",this);
    }

    private void addKeyword(String keyword){
        if (!contains(keyword)){
            mKeyword = new Keyword(keyword);
            mKeyword.save();
            mKeywords.add(mKeyword);
            update();
        } else {
            Toast.makeText(this,"Keyword already exist", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean contains(String name) {
        for(Keyword keyword: mKeywords){
            if (keyword.is(name)){
                return true;
            }
        }
        return false;
    }

    private void update() {
        mKeywordsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onOK(String s) {
        addKeyword(s);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
