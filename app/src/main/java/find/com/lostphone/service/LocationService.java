package find.com.lostphone.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import find.com.lostphone.R;
import find.com.lostphone.data.sharedPreference.LocalPrefManger;
import find.com.lostphone.receiver.SMSReceiver;
import find.com.lostphone.utils.LostPhoneConstant;


public class LocationService extends Service {
    private static final String TAG = LocationService.class.getSimpleName();

    LocationManager locationManager;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */


    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

    String sender = "", IMEI = "";
    boolean isAntiTheft;

    public LocationService() {
    }

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        /*createLocationRequest();*/

    }

    private void getLastLocation() {
        /*todo check permission for mashmellow*/
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {


                                stopSelf();
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.hasExtra(LostPhoneConstant.EXTRA_PHONE_NUMBER)) {
                sender = intent.getStringExtra(LostPhoneConstant.EXTRA_PHONE_NUMBER);
                isAntiTheft = false;
            } else if (intent.hasExtra(LostPhoneConstant.EXTRA_ANTI_THEFT)) {
                IMEI = intent.getStringExtra(LostPhoneConstant.EXTRA_IMEI);
                isAntiTheft = true;
            }

            boolean isGpsEnable = false;
            try {
                isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isGpsEnable) {
                createLocationCallback();
                createLocationRequest();
                startLocationUpdates();

            } else {

                if (isAntiTheft) {
                    AntiTheftMessage();
                }

                stopSelf();
            }


            /* else {

            if (!checkLocationPermission()) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        0, 0, new android.location.LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                Log.d(TAG,"location" + location.getLongitude());
                            }

                            @Override
                            public void onStatusChanged(String s, int i, Bundle bundle) {
                                Log.d(TAG,"location" + s);
                            }

                            @Override
                            public void onProviderEnabled(String s) {
                                Log.d(TAG,"location" + s);
                            }

                            @Override
                            public void onProviderDisabled(String s) {
                                Log.d(TAG,"location" + s);

                            }
                        });

            }


        }*/

        }


        return START_NOT_STICKY;
    }


    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void createLocationCallback() {

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();

                if (sender.isEmpty()) {
                    String message = "Mobile Location \n" +
                            LostPhoneConstant.BASIC_MAP_URL;
                    message = message + mCurrentLocation.getLatitude() + ","
                            + mCurrentLocation.getLongitude();

                    SMSReceiver.sendSMS(LocationService.this, sender, message);
                } else if (isAntiTheft) {

                    AntiTheftMessage();

                }

                stopLocationUpdates();
            }
        };

    }

    private void AntiTheftMessage() {
        String customMessage = LocalPrefManger.
                getMessageAntiTheft(LocationService.this);

        if (customMessage.isEmpty()) {
            customMessage = getResources().getString(R.string.custom_msg_for_friend);
        }
        String firstContact =
                LocalPrefManger.getTrustedContactFirst(LocationService.this);
        String secondContact =
                LocalPrefManger.getTrustedContactSecond(LocationService.this);
        String[] turstedContacts = {firstContact, secondContact};
        for (int i = 0; i < turstedContacts.length; i++) {

            if (turstedContacts[i] != null && !turstedContacts[i].isEmpty()) {

                String message = customMessage + "\n\n\n" + "Mobile Location: \n" +
                        LostPhoneConstant.BASIC_MAP_URL;
                message = message + mCurrentLocation.getLatitude() + ","
                        + mCurrentLocation.getLongitude();
                message = message + "\n" + "IMEI :" + IMEI;
                message = message + "\n" + "Lock Code :" +
                        LocalPrefManger.getLockCodeAntiTheft(LocationService.this);

                SMSReceiver.sendSMS(LocationService.this,
                        turstedContacts[i], message);
            }
        }
    }

    private void stopLocationUpdates() {

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        stopSelf();
                    }
                });
    }


    private void startLocationUpdates() {
        if (checkLocationPermission()) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback, Looper.myLooper());

        // Begin by checking if the device has the necessary location settings.
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
