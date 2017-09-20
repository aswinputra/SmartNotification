package com.kasmartnotification.smartnotification.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.kasmartnotification.smartnotification.Adapter.PlacesAdapter;
import com.kasmartnotification.smartnotification.LocationTools.LocationHelper;
import com.kasmartnotification.smartnotification.Model.Facet;
import com.kasmartnotification.smartnotification.Model.Place;
import com.kasmartnotification.smartnotification.R;

import java.util.List;

import static com.kasmartnotification.smartnotification.Constants.LOCATION_LOG;
import static com.kasmartnotification.smartnotification.Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE;

public class SettingsPlaces extends AppCompatActivity {

    private RecyclerView placesRecyclerView;
    private PlacesAdapter mPlacesAdapter;
    private List<Place> mPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_places);
        Toolbar toolbar = findViewById(R.id.activity_places_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPlaces = Place.listAll(Place.class);

        placesRecyclerView = findViewById(R.id.activity_settings_places_recycler_view);
        mPlacesAdapter = new PlacesAdapter(this, mPlaces);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placesRecyclerView.setAdapter(mPlacesAdapter);

        FloatingActionButton fab = findViewById(R.id.activity_settings_places_add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGooglePlacesAutoComplete();
            }
        });
    }

    private void showGooglePlacesAutoComplete(){
        LatLng userLocation = new LatLng(-33.859881, 151.096664);
        if(Facet.getUserLocation() != null) {
            userLocation = Facet.getUserLocation();
        }

        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setBoundsBias(LocationHelper.toBounds(userLocation))
                            .build(this);

            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlaceAutocomplete.getPlace(this, data);
                addPlace(place.getName(), place.getAddress(), place.getLatLng());
                Log.i(LOCATION_LOG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(LOCATION_LOG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void addPlace(CharSequence nameChar, CharSequence addressChar, LatLng latlng){
        String name = String.valueOf(nameChar);
        String address = String.valueOf(addressChar);
        if (!contains(name, latlng)){
            Place place = new Place(name, address, latlng);
            place.save();
            mPlaces.add(place);
            update();
        } else {
            Toast.makeText(this,"Place already exist", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean contains(String name, LatLng latlng) {
        for(Place place: mPlaces){
            if (place.is(name, latlng)){
                return true;
            }
        }
        return false;
    }

    private void update() {
        mPlacesAdapter.notifyDataSetChanged();
    }
}
