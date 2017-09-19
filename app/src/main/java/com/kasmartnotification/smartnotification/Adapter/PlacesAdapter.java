package com.kasmartnotification.smartnotification.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kasmartnotification.smartnotification.Controller.DeleteDialog;
import com.kasmartnotification.smartnotification.Controller.EditDialog;
import com.kasmartnotification.smartnotification.Interfaces.OnDialogClickedListener;
import com.kasmartnotification.smartnotification.Model.Place;
import com.kasmartnotification.smartnotification.Model.ReminderMessage;
import com.kasmartnotification.smartnotification.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aswinhartono on 14/9/17.
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    private Context mContext;
    private List<Place> mPlaces;

    public PlacesAdapter(Context context, List<Place> place){
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

    public class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, OnDialogClickedListener {

        private TextView place;
        private TextView address;
        private LinearLayout placesLinearLayout;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            place = itemView.findViewById(R.id.activity_settings_location_places_adapter_places);
            address = itemView.findViewById(R.id.activity_settings_location_places_adapter_address);
            placesLinearLayout = itemView.findViewById(R.id.adapter_places_linear_layout);
            placesLinearLayout.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            if(view == placesLinearLayout){
                showDeleteDialog(getAdapterPosition());
                return true;
            }
            return false;
        }

        private void showDeleteDialog(int adapterPosition) {
            new DeleteDialog(mContext, mPlaces.get(adapterPosition),this);
        }

        @Override
        public void onOK(Object object, String newString) {
        }

        @Override
        public void onDelete(Object object) {
            ((Place)object).delete();
            mPlaces.remove(getAdapterPosition());
            notifyDataSetChanged();
        }
    }


}
