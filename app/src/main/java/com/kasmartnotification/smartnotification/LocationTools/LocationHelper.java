package com.kasmartnotification.smartnotification.LocationTools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;
import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Model.Place;

import java.util.List;

import static com.kasmartnotification.smartnotification.Constants.BOUND_BIAS_RADIUS_METERS;
import static com.kasmartnotification.smartnotification.Constants.PERMISSION_ACCESS_COARSE_LOCATION;

/**
 * Created by kiman on 18/9/17.
 */

public class LocationHelper {

    public static void getLocationPermission(Context context, Activity activity){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }
    }

    private static boolean isAroundPlace(LatLng currentLatLng, LatLng place) {
        float[] distance = new float[1];
        Location.distanceBetween(currentLatLng.latitude, currentLatLng.longitude, place.latitude, place.longitude, distance);

        if (distance[0] > Constants.LOCATION_ACCURACY) {
            Log.i(Constants.LOCATION_LOG, "Point is not in circle");
            return false;
        } else {
            Log.i(Constants.LOCATION_LOG, "Point is in circle");
            return true;
        }
    }

    public static boolean isAroundAnyFocusPlace(LatLng currentLatLng){

        List<Place> places = Place.listAll(Place.class);
        for(Place place: places){
            if(isAroundPlace(currentLatLng, place.getLatLng())){
                return true;
            }
        }
        return false;
    }

    public static LatLngBounds toBounds(LatLng center) {

        double distanceFromCenterToCorner = BOUND_BIAS_RADIUS_METERS * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }
}
