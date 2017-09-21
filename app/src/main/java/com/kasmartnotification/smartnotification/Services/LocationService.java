package com.kasmartnotification.smartnotification.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.LocationTools.LocationHelper;
import com.kasmartnotification.smartnotification.LocationTools.MyGoogleLocationApiClient;
import com.kasmartnotification.smartnotification.Model.Facet;
import com.kasmartnotification.smartnotification.Tools.NotificationHelper;
import com.kasmartnotification.smartnotification.Tools.SugarHelper;
import com.kasmartnotification.smartnotification.Tools.Utility;

/**
 * Created by kiman on 18/9/17.
 */

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mLocationClient;
    private LocationRequest mRequest;
    private LocationListener mListener;
    private LatLng mCurrentLatLng;
    private static boolean previouslyAround = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(Constants.SERVICE_LOG, "Location Service started");
        MyGoogleLocationApiClient client = new MyGoogleLocationApiClient(getApplicationContext(), this, this);
        mLocationClient = client.getLocationClient();
        mRequest = client.getRequest();
        mLocationClient.connect();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (!mLocationClient.isConnected()) {
            mLocationClient.disconnect();
            Log.i(Constants.LOCATION_LOG, "mLocationClient disconnect is called");
        }
        Log.i(Constants.SERVICE_LOG, "Location Service destroyed");
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(Constants.LOCATION_LOG, "on Connected");
        setUpListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mRequest, mListener);
    }

    private void setUpListener() {
        mListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    Facet.setUserLocation(mCurrentLatLng);
                    Log.i(Constants.LOCATION_LOG, "onLocationChanged: " + location.getLatitude() + ", " + location.getLongitude());

                    boolean aroundFocusedPlaces = LocationHelper.isAroundAnyFocusPlace(mCurrentLatLng);
                    if(aroundFocusedPlaces) {
                        if(!previouslyAround && !SugarHelper.isSmartNotiInUse()) {
                            NotificationHelper.remindHeadsUp(getApplicationContext(), Constants.SMART_NOTIFICATION_ON_CODE);
                        }
                    }else{
                        if(previouslyAround && SugarHelper.isSmartNotiInUse()) {
                            NotificationHelper.remindHeadsUp(getApplicationContext(), Constants.SMART_NOTIFICATION_OFF_CODE);
                        }
                    }
                    previouslyAround = aroundFocusedPlaces;
                }
            }
        };
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(Constants.LOCATION_LOG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(Constants.LOCATION_LOG, "Connection failed");
    }
}
