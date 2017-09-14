package com.kasmartnotification.smartnotification.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kasmartnotification.smartnotification.Model.Place;
import com.kasmartnotification.smartnotification.R;

import java.util.ArrayList;

/**
 * Created by aswinhartono on 14/9/17.
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    private Context mContext;
    private ArrayList<Place> mPlaces;


    public PlacesAdapter(Context context, ArrayList<Place> place){
        mContext = context;
        mPlaces = place;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_settings_places,parent,false);
        PlaceViewHolder holder = new PlaceViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        Place places = mPlaces.get(position);

        holder.place.setText(places.getName());
        holder.address.setText(places.getAddress());
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {

        private TextView place;
        private TextView address;
        private LinearLayout placesLinearLayout;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            place = itemView.findViewById(R.id.activity_settings_location_places_adapter_places);
            address = itemView.findViewById(R.id.activity_settings_location_places_adapter_address);
            placesLinearLayout = itemView.findViewById(R.id.adapter_places_linear_layout);
        }
    }
}
