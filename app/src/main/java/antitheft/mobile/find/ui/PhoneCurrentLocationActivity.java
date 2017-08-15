package antitheft.mobile.find.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;

import antitheft.mobile.find.R;
import antitheft.mobile.find.data.sharedPreference.LocalPrefManger;
import antitheft.mobile.find.utils.LostPhoneConstant;
import antitheft.mobile.find.utils.LostPhoneUtil;
import pub.devrel.easypermissions.EasyPermissions;

public class PhoneCurrentLocationActivity extends AppCompatActivity
        implements View.OnClickListener {
    private static final String TAG = PhoneCurrentLocationActivity.class.getSimpleName();

    private static final int RC_SMS_location = 40;
    NativeExpressAdView adView;
    VideoController mVideoController;
    TextView txtSecretCommand;
    ActionBar actionBar;
    Toolbar toolbar;
    SwitchCompat enablePhoneLocation;
    EditText edSecretLocationCommand;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_current_location_activtiy);
        initUI();
        setData();
        enablePhoneLocation.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean isEnable) {

                        if (isEnable && checkSMSAndLocationPermission()) {
                            LocalPrefManger.setLocationPhoneEnable(
                                    PhoneCurrentLocationActivity.this,
                                    isEnable);

                        } else {
                            LocalPrefManger.setLocationPhoneEnable(
                                    PhoneCurrentLocationActivity.this,
                                    false);
                            enablePhoneLocation.setChecked(false);

                        }

                    }
                });

    }


    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        setuptoolbar();
        adView = (NativeExpressAdView) findViewById(R.id.adView);
        txtSecretCommand = findViewById(R.id.txtSecretCommand);
        enablePhoneLocation = findViewById(R.id.enablePhoneLocation);
        edSecretLocationCommand = findViewById(R.id.edSecretLocationCommand);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setuptoolbar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setData() {
        enablePhoneLocation.setChecked(checkSMSAndLocationPermission()
                && LocalPrefManger.getLocationPhoneEnable(this));
        edSecretLocationCommand.setText(LocalPrefManger.getLocationSecretCommand(this));
    }

    private void loadAd() {
        // Locate the NativeExpressAdView.
        adView = (NativeExpressAdView) findViewById(R.id.adView);

        // Set its video options.
        adView.setVideoOptions(new VideoOptions.Builder()
                .setStartMuted(true)
                .build());

        // The VideoController can be used to get lifecycle events and info about an ad's video
        // asset. One will always be returned by getVideoController, even if the ad has no video
        // asset.
        mVideoController = adView.getVideoController();
        mVideoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            @Override
            public void onVideoEnd() {
                Log.d(TAG, "Video playback is finished.");
                super.onVideoEnd();
            }
        });

        // Set an AdListener for the AdView, so the Activity can take action when an ad has finished
        // loading.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mVideoController.hasVideoContent()) {
                    Log.d(TAG, "Received an ad that contains a video asset.");
                } else {
                    Log.d(TAG, "Received an ad that does not contain a video asset.");
                }
            }
        });

        adView.loadAd(new AdRequest.Builder().
                addTestDevice("4566678A62E155DF738BB6C45B32AD4E").build());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:

                /*Todo Check for validations*/
                String secretCommand = edSecretLocationCommand.getText()
                        .toString().toLowerCase().trim();
                if (isValidationSuccess(secretCommand)) {
                    LocalPrefManger.setLocationSecretCommand
                            (PhoneCurrentLocationActivity.this, secretCommand);
                    finish();
                }


                break;
            case R.id.btnEmail:
                String subject = "Phone Location Info";
                String body = "Secret Command: " +
                        LocalPrefManger.getLocationSecretCommand(PhoneCurrentLocationActivity.this);

                LostPhoneUtil.sendEmail(this, subject, body);
                break;
            case R.id.txtSecretCommand:
                LostPhoneUtil.Alertdialog(PhoneCurrentLocationActivity.this,
                        getString(R.string.secret_command),
                        getString(R.string.msg_current_location), getString(R.string.ok));

                break;
        }
    }

    private boolean isValidationSuccess(String secretCommand) {

        boolean isSuccess = true;
        if (secretCommand == null || secretCommand.isEmpty()) {
            isSuccess = false;
            edSecretLocationCommand.setError("Please write secret  command ," +
                    "this can not be empty");

        }

        if (secretCommand != null && secretCommand.length() < 4) {
            isSuccess = false;
            edSecretLocationCommand.setError("Command should be greater or eqaul to four character");

        }

        if (secretCommand != null && secretCommand.length() > 20) {
            isSuccess = false;
            edSecretLocationCommand.setError("Command should be less or eqaul to twenty character");

        }
        return isSuccess;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.menu_help) {

            helpDialoge(PhoneCurrentLocationActivity.this,"Ok");
        }
        return super.onOptionsItemSelected(item);
    }

    private void helpDialoge(Context context,String ok) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_layout_phone_location, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.show();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                ImageView image = dialog.findViewById(R.id.imgHelp);
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.get_phone_location);
                try {

                    float imageWidthInPX = (float)image.getWidth();

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                            Math.round(imageWidthInPX * (float)icon.getHeight() / (float)icon.getWidth()));
                    image.setLayoutParams(layoutParams);


                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    private boolean checkSMSAndLocationPermission() {

        boolean isHaveSmsPermission = true;
        String[] perms = {LostPhoneConstant.PARM_READ_SMS_PERMISSION, LostPhoneConstant.PARM_FINE_LOCATION_PERMISSION};

        if (!EasyPermissions.hasPermissions(this, perms)) {
            isHaveSmsPermission = false;
            EasyPermissions.requestPermissions(this, getString(R.string.with_out_this_request),
                    RC_SMS_location, perms);
        }

        return isHaveSmsPermission;
    }


}
