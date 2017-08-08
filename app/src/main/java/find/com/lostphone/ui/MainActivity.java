package find.com.lostphone.ui;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import find.com.lostphone.R;
import find.com.lostphone.helper.AudioMangerHelper;
import find.com.lostphone.service.LocationService;
import find.com.lostphone.utils.LostPhoneConstant;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;

    VideoController videoController;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView nvView;
    NativeExpressAdView adView;
    private ActionBarDrawerToggle drawerToggle;
    TextView txtLockPhoneViaSms, txtRingSilentPhone,
            txtGetLocation, txtAntiTheftSecurity;
    boolean gps_enabled = false;
    boolean network_enabled = false;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initUI();
        /*loadAdd();*/
        dialogLocation();
    }
    private void dialogLocation() {
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle(R.string.improve_location_accuracy);
            dialog.setMessage(R.string.ensure_location_service);
            dialog.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.show();
        }
    }

    private void initData() {
        mSettingsClient = LocationServices.getSettingsClient(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }

    private void initUI() {
        adView = findViewById(R.id.adView);
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nvView = findViewById(R.id.nvView);
        txtLockPhoneViaSms = findViewById(R.id.txtLockPhoneViaSms);
        txtRingSilentPhone = findViewById(R.id.txtRingSilentPhone);
        txtGetLocation = findViewById(R.id.txtGetLocation);
        txtAntiTheftSecurity = findViewById(R.id.txtAntiTheftSecurity);
        setupNavDrawerContent(nvView);
        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        drawerLayout.addDrawerListener(drawerToggle);
        buildLocationSettingsRequest();
        startLocationUpdates();

        startService(new Intent(MainActivity.this, LocationService.class));

    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        /*builder.addLocationRequest(mLocationRequest);*/
        mLocationSettingsRequest = builder.build();
    }

    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission
                       /* mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateUI();*/
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this, 22);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();

                        }

                        /*updateUI();*/
                    }
                });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_open);

    }

    private void loadAdd() {

        AdRequest request = new AdRequest.Builder()
                .build();
        adView.setVideoOptions(new VideoOptions.Builder()
                .setStartMuted(true)
                .build());

        // The VideoController can be used to get lifecycle events and info about an ad's video
        // asset. One will always be returned by getVideoController, even if the ad has no video
        // asset.
        videoController = adView.getVideoController();
        videoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            @Override
            public void onVideoEnd() {
                super.onVideoEnd();
            }
        });

        // Set an AdListener for the AdView, so the Activity can take action when an ad has finished
        // loading.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (videoController.hasVideoContent()) {
                    Log.d(TAG, "Received an ad that contains a video asset.");
                } else {
                    Log.d(TAG, "Received an ad that does not contain a video asset.");
                }
            }
        });

        adView.loadAd(request);
    }

    public void setupNavDrawerContent(NavigationView nvView) {
        nvView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });

    }

    private void selectDrawerItem(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                break;
            case R.id.nav_second_fragment:
                break;
            default:
        }


        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtLockPhoneViaSms:

                startActivityForResult(new Intent(MainActivity.this,
                                LockPhoneViaSmsActivity.class),
                        LostPhoneConstant.LOCK_PHONE_VIA_SMS);
                break;
            case R.id.txtRingSilentPhone:
                AudioMangerHelper mangerHelper = new AudioMangerHelper(MainActivity.this);
                startActivityForResult(new Intent(MainActivity.this,
                                RingSilentPhoneActivity.class),
                        LostPhoneConstant.RING_SILENT_PHONE);

                break;
            case R.id.txtGetLocation:
                startActivityForResult(new Intent(MainActivity.this,
                                PhoneCurrentLocationActivity.class),
                        LostPhoneConstant.GET_CURRENT_LOCATION_PHONE);

                break;
            case R.id.txtAntiTheftSecurity:
                startActivityForResult(new Intent(MainActivity.this,
                                AntiTheftSecurityActivity.class),
                        LostPhoneConstant.ANTI_THEFT_SECURITY);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LostPhoneConstant.LOCK_PHONE_VIA_SMS:
            case LostPhoneConstant.RING_SILENT_PHONE:
            case LostPhoneConstant.GET_CURRENT_LOCATION_PHONE:
            case LostPhoneConstant.ANTI_THEFT_SECURITY:
                break;
        }
    }
}

