package find.com.lostphone.ui;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;

import find.com.lostphone.R;
import find.com.lostphone.utils.LostPhoneConstant;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    VideoController videoController;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView nvView;
    NativeExpressAdView adView;
    private ActionBarDrawerToggle drawerToggle;
    TextView txtLockPhoneViaSms, txtRingSilentPhone,
            txtGetLocation, txtAntiTheftSecurity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        loadAdd();

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
                startActivityForResult(new Intent(MainActivity.this,
                                RingSilentPhoneActivity.class),
                        LostPhoneConstant.RING_SILENT_PHONE);

                break;
            case R.id.txtGetLocation:
                startActivityForResult(new Intent(MainActivity.this,
                                PhoneCurrentLocationActivtiy.class),
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

