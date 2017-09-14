package com.kasmartnotification.smartnotification.Controller;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kasmartnotification.smartnotification.Adapter.PlacesAdapter;
import com.kasmartnotification.smartnotification.Model.Place;
import com.kasmartnotification.smartnotification.R;

import java.util.ArrayList;

public class SettingsPlaces extends AppCompatActivity {

    private RecyclerView placesRecyclerView;
    private PlacesAdapter mPlacesAdapter;
    private ArrayList<Place> mPlaces;
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_places);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPlaces = new ArrayList<>();
        place = new Place("Home", "Homebush");
        mPlaces.add(place);

        placesRecyclerView = findViewById(R.id.activity_settings_places_recycler_view);
        mPlacesAdapter = new PlacesAdapter(this, mPlaces);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placesRecyclerView.setAdapter(mPlacesAdapter);

        FloatingActionButton fab = findViewById(R.id.activity_settings_places_add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //TODO: Add new places
            }
        });
    }

}
