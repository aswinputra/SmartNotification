package com.kasmartnotification.smartnotification.Model;

import com.google.android.gms.maps.model.LatLng;
import com.orm.SugarRecord;

/**
 * Created by aswinhartono on 14/9/17.
 */

public class Place extends SugarRecord{
    private String name;
    private String address;
    private double lat;
    private double lng;

    public Place() {
    }

    public Place(String name, String address, LatLng latLng) {
        this.name = name;
        this.address = address;
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    public boolean is(String name, LatLng latlng){
        return this.name.equals(name) && this.getLatLng() == latlng;
    }
}
