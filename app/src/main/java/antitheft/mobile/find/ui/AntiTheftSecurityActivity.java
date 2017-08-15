package antitheft.mobile.find.ui;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
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
import antitheft.mobile.find.helper.DeviceAdminManger;
import antitheft.mobile.find.utils.LostPhoneConstant;
import antitheft.mobile.find.utils.LostPhoneUtil;
import pub.devrel.easypermissions.EasyPermissions;

public class AntiTheftSecurityActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = AntiTheftSecurityActivity.class.getSimpleName();

    private static final int REQUEST_CODE_ENABLE_ADMIN = 20;
    private static final int RC_SMS_PHONE_STATE_AND_LOCATION = 50;

    NativeExpressAdView adView;
    VideoController mVideoController;
    DeviceAdminManger deviceAdminManger;
    ActionBar actionBar;
    Toolbar toolbar;
    TextView txtLockCode;
    SwitchCompat enableAntiTheft;
    EditText edContactFirst, edContactSecond, edMsgAntiTheft, edLockCode;
    Button btnSave, btnEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_theft_security);
        initData();
        initUI();
        setData();
        loadAd();
        enableAntiTheft.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean isEnable) {

                        if (isEnable) {
                            TelephonyManager tm = (TelephonyManager)
                                    getSystemService(Context.TELEPHONY_SERVICE);
                            String simSerialNumber = tm.getSimSerialNumber();
                            LocalPrefManger.setSimSerialNumber(
                                    AntiTheftSecurityActivity.this, simSerialNumber);
                            //    tm.getDeviceId();
                        }


                        LocalPrefManger.setAntiTheftEnable(AntiTheftSecurityActivity.this,
                                isEnable);

                        if (checkMultiPulPermission()) {

                            if (isEnable && !deviceAdminManger.isActiveAdmin()) {

                                Intent intent = new
                                        Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                                        deviceAdminManger.getComponentName());
                                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                        getString(R.string.extra_explanation_admin_rights));
                                startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
                            }
                        } else {
                            LocalPrefManger.setAntiTheftEnable(AntiTheftSecurityActivity.this,
                                    false);
                            enableAntiTheft.setChecked(false);
                        }


                    }
                });

    }

    private void initData() {
        deviceAdminManger = new DeviceAdminManger(this);
    }

    private void setData() {
        edLockCode.setText(LocalPrefManger.getLockCodeAntiTheft(this));
        String firstContact = LocalPrefManger.getTrustedContactFirst(this);
        String secContact = LocalPrefManger.getTrustedContactSecond(this);
        String message = LocalPrefManger.getMessageAntiTheft(this);
        if (firstContact != null && !firstContact.isEmpty()) {
            edContactFirst.setText(firstContact);
        }
        if (secContact != null && !secContact.isEmpty()) {
            edContactSecond.setText(secContact);
        }
        if (message != null && !message.isEmpty()) {
            edMsgAntiTheft.setText(message);
        }
        enableAntiTheft.setChecked(LocalPrefManger.getAntiTheftEnable(this));
    }

    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        setuptoolbar();
        adView = (NativeExpressAdView) findViewById(R.id.adView);
        enableAntiTheft = findViewById(R.id.enableAntiTheft);
        edContactFirst = findViewById(R.id.edContactFirst);
        edContactSecond = findViewById(R.id.edContactSecond);
        edMsgAntiTheft = findViewById(R.id.edMsgAntiTheft);
        edLockCode = findViewById(R.id.edLockCode);
        btnSave = findViewById(R.id.btnSave);
        btnEmail = findViewById(R.id.btnEmail);
        txtLockCode = findViewById(R.id.txtLockCode);
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

    private void loadAd() {
        // Locate the NativeExpressAdView.
        adView = findViewById(R.id.adView);

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
                adView.setVisibility(View.VISIBLE);
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

                String message = edMsgAntiTheft.getText().toString().trim();

                String firstContact = edContactFirst.getText().toString().trim();

                String secondContact = edContactSecond.getText().toString().trim();

                String lockCode = edLockCode.getText().toString().trim();

                if (isValidationSuccess(message, firstContact, secondContact, lockCode)) {
                    LocalPrefManger.setMessageAntiTheft(AntiTheftSecurityActivity.this,
                            message);
                    LocalPrefManger.setTrustedContactFirst(AntiTheftSecurityActivity.this,
                            firstContact);
                    LocalPrefManger.setTrustedContactSecond(AntiTheftSecurityActivity.this,
                            secondContact);

                    LocalPrefManger.setLockCodeAntiTheft(AntiTheftSecurityActivity.this,
                            lockCode);
                    finish();
                }


                break;
            case R.id.btnEmail:
                String subject = "Anti theft Info";
                String body = "Message: " +
                        LocalPrefManger.getMessageAntiTheft(AntiTheftSecurityActivity.this)
                        + " \n\n" +
                        "First Contact: " + LocalPrefManger.getTrustedContactFirst(AntiTheftSecurityActivity.this)
                        + "\n \n" +

                        "Second Contact: " + LocalPrefManger.getTrustedContactSecond(AntiTheftSecurityActivity.this) +
                        "\n \n" +

                        "Lock Code : " + LocalPrefManger.getLockCodeAntiTheft(AntiTheftSecurityActivity.this);

                LostPhoneUtil.sendEmail(this, subject, body);
                break;
            case R.id.txtLockCode:
                LostPhoneUtil.Alertdialog(AntiTheftSecurityActivity.this,
                        getString(R.string.lock_code),
                        getString(R.string.msg_anti_theft), getString(R.string.ok));

                break;


        }
    }

    private boolean isValidationSuccess(String message, String firstContact,
                                        String secondContact, String lockCode) {
        boolean isSuccess = true;
        if (lockCode == null || lockCode.isEmpty()) {
            isSuccess = false;
            edLockCode.setError("Please write lock code ,this can not be empty");

        }

        if (lockCode != null && lockCode.length() < 4) {
            isSuccess = false;
            edLockCode.setError("Command should be greater or eqaul to four character");

        }

        if (lockCode != null && lockCode.length() > 6) {
            isSuccess = false;
            edLockCode.setError("Command should be less or eqaul to six character");

        }

        if (message == null || message.isEmpty()) {
            isSuccess = false;
            edMsgAntiTheft.setError("Please write message ,this can not be empty");

        }

        if (message != null && message.length() < 4) {
            isSuccess = false;
            edMsgAntiTheft.setError("Message should be greater to four character");

        }

        if (message != null && message.length() > 1000) {
            isSuccess = false;
            edMsgAntiTheft.setError("Message should be less or eqaul to 1000 character");

        }
        if (firstContact == null || firstContact.isEmpty()) {
            isSuccess = false;
            edContactFirst.setError("Please write number ,this can not be empty");
        }
        if (firstContact != null && firstContact.length() < 5) {
            isSuccess = false;
            edContactFirst.setError("Please write true number ");
        }
        if (secondContact != null && secondContact.length() < 5) {
            isSuccess = false;
            edContactSecond.setError("Please write true number ");
        }


        return isSuccess;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ENABLE_ADMIN) {
            boolean isActive = deviceAdminManger.isActiveAdmin();
            LocalPrefManger.setAntiTheftEnable(AntiTheftSecurityActivity.this,
                    isActive);
            enableAntiTheft.setChecked(isActive);


        }
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

            helpDialoge(AntiTheftSecurityActivity.this,"Ok");
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean checkMultiPulPermission() {
        boolean isHaveSmsPermission = true;

        String[] perms = {LostPhoneConstant.PARM_READ_SMS_PERMISSION,
                LostPhoneConstant.PARM_FINE_LOCATION_PERMISSION, LostPhoneConstant.PARM_READ_PHONE_STATE_PERMISSION};

        if (!EasyPermissions.hasPermissions(this, perms)) {
            isHaveSmsPermission = false;

            EasyPermissions.requestPermissions(this, getString(R.string.sms_phonestate_and_location_rationale),
                    RC_SMS_PHONE_STATE_AND_LOCATION, perms);
        }
        return isHaveSmsPermission;
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
        View dialogLayout = inflater.inflate(R.layout.dialog_layout_anti_theft, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.show();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                ImageView image = dialog.findViewById(R.id.imgHelp);
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.anti_theft);
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

}
