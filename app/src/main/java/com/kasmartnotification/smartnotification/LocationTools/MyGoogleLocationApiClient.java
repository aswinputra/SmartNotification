package com.kasmartnotification.smartnotification.LocationTools;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.kasmartnotification.smartnotification.Constants;

public class MyGoogleLocationApiClient {

    private GoogleApiClient mLocationClient;
    private LocationRequest mRequest;

    public MyGoogleLocationApiClient(Context context, GoogleApiClient.ConnectionCallbacks callbacks, GoogleApiClient.OnConnectionFailedListener listener){
        mLocationClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(callbacks)
                .addOnConnectionFailedListener(listener)
                .addApi(LocationServices.API)
                .build();
        setUpLocationRequest();
    }

    public MyGoogleLocationApiClient(Context context){
        mLocationClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();
        setUpLocationRequest();
    }

    private void setUpLocationRequest(){
        mRequest = LocationRequest.create();
        mRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mRequest.setInterval(Constants.LOCATION_REFRESH_TIME);
        mRequest.setFastestInterval(Constants.LOCATION_FAST_REFRESH_TIME);
    }

    //needs to call mLocationClient.connect(); to get the location
    public GoogleApiClient getLocationClient() {
        return mLocationClient;
    }

    public LocationRequest getRequest() {
        return mRequest;
    }

    public void setUpGPS(final Activity activity, final MyGPSCallback callback) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mRequest);
        builder.setAlwaysShow(true);

        PendingResult result1 = LocationServices.SettingsApi.checkLocationSettings(
                mLocationClient, builder.build());

        result1.setResultCallback(new ResultCallback() {
            @Override
            public void onResult(@NonNull Result result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        callback.OnGPSResult(LocationSettingsStatusCodes.SUCCESS);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult(activity, Constants.GPS_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(Constants.EXCEPTION, e.getMessage());
                        }break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        callback.OnGPSResult(LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE);
                        break;

                }
            }
        });
    }
}
