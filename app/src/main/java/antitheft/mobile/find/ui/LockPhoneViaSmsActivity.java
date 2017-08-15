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

import java.util.List;

import antitheft.mobile.find.R;
import antitheft.mobile.find.data.sharedPreference.LocalPrefManger;
import antitheft.mobile.find.helper.DeviceAdminManger;
import antitheft.mobile.find.utils.LostPhoneConstant;
import antitheft.mobile.find.utils.LostPhoneUtil;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class LockPhoneViaSmsActivity extends AppCompatActivity
        implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private static final String TAG = LockPhoneViaSmsActivity.class.getSimpleName();

    private static final int REQUEST_CODE_ENABLE_ADMIN = 20;
    private static final int RC_SMS = 21;


    NativeExpressAdView adView;
    VideoController mVideoController;
    DeviceAdminManger deviceAdminManger;
    Toolbar toolbar;
    TextView txtSecretCommand, txtLockCode;
    ActionBar actionBar;
    SwitchCompat enableLockPhoneSms;
    EditText edSecretCommand, edLockCode;
    Button btnEmail, btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_phone_via_sms);
        initData();
        initUI();
        setData();
        loadAd();


        enableLockPhoneSms.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean isEnable) {

                        if (checkSMSPermission()) {

                            if (isEnable && !deviceAdminManger.isActiveAdmin()) {
                                LocalPrefManger.setEnableLockPhoneSMS(LockPhoneViaSmsActivity.this, isEnable);

                                Intent intent = new
                                        Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                                        deviceAdminManger.getComponentName());
                                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                        getString(R.string.extra_explanation_admin_rights));
                                startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
                            }

                        } else {
                            LocalPrefManger.setEnableLockPhoneSMS(LockPhoneViaSmsActivity.this, false);
                            enableLockPhoneSms.setChecked(false);

                        }

                    }
                });
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

    private void initData() {
        deviceAdminManger = new DeviceAdminManger(LockPhoneViaSmsActivity.this);
    }


    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        adView = findViewById(R.id.adView);
        txtSecretCommand = findViewById(R.id.txtSecretCommand);
        txtLockCode = findViewById(R.id.txtLockCode);
        setuptoolbar();
        enableLockPhoneSms = findViewById(R.id.enableLockPhoneSms);
        edSecretCommand = findViewById(R.id.edSecretCommand);
        edLockCode = findViewById(R.id.edLockCode);
        btnEmail = findViewById(R.id.btnEmail);
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
        boolean isLockEnable =
                LocalPrefManger.getEnableLockPhoneSMS(LockPhoneViaSmsActivity.this);

        enableLockPhoneSms.setChecked(checkSMSPermission() && deviceAdminManger.isActiveAdmin() && isLockEnable);

        edSecretCommand.setText(LocalPrefManger.
                getSecretCommandSMS(LockPhoneViaSmsActivity.this));
        edLockCode.setText(LocalPrefManger.
                getLockCodeSMS(LockPhoneViaSmsActivity.this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:

                /*Todo Check for validations*/
                String secretCommand = edSecretCommand.getText()
                        .toString().toLowerCase().trim();
                String lockCode = edLockCode.getText().toString().trim();

                if (isValidationSuccess(secretCommand, lockCode)) {
                    LocalPrefManger.setSecretCommandSMS(this, secretCommand);
                    LocalPrefManger.setLockCodeSMS(this, lockCode);
                    LocalPrefManger.setEnableLockPhoneSMS(this, true);
                    finish();
                }


                break;
            case R.id.btnEmail:
                String subject = "Lock phone via Sms Info";
                String body = "Secret Command: " +
                        LocalPrefManger.getSecretCommandSMS(LockPhoneViaSmsActivity.this)
                        + " \n\n" +
                        "Lock Code : " + LocalPrefManger.getLockCodeSMS(LockPhoneViaSmsActivity.this);

                LostPhoneUtil.sendEmail(this, subject, body);

                break;
            case R.id.txtSecretCommand:
                LostPhoneUtil.Alertdialog(LockPhoneViaSmsActivity.this, getString(R.string.secret_command),
                        getString(R.string.msg_secret_command), getString(R.string.ok));
                break;
            case R.id.txtLockCode:
                LostPhoneUtil.Alertdialog(LockPhoneViaSmsActivity.this, getString(R.string.lock_code),
                        getString(R.string.msg_lock_code), getString(R.string.ok));

                break;
        }
    }


    private boolean isValidationSuccess(String secretCommand, String lockCode) {

        boolean isSuccess = true;
        if (secretCommand == null || secretCommand.isEmpty()) {
            isSuccess = false;
            edSecretCommand.setError("Please write secret command ,this can not be empty");

        }

        if (secretCommand != null && secretCommand.length() < 5) {
            isSuccess = false;
            edSecretCommand.setError("Command should be greater or eqaul to five character");

        }

        if (secretCommand != null && secretCommand.length() > 20) {
            isSuccess = false;
            edSecretCommand.setError("Command should be less or eqaul to twenty character");

        }
        if (lockCode == null || lockCode.isEmpty()) {
            isSuccess = false;
            edLockCode.setError("Please write lock code ,this can not be empty");

        }

        if (lockCode != null && lockCode.length() < 4) {
            isSuccess = false;
            edLockCode.setError("Lock code should be greater or eqaul to four character");

        }

        if (lockCode != null && lockCode.length() > 6) {
            isSuccess = false;
            edLockCode.setError("Lock code should be less or eqaul to six character");

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

            helpDialoge(LockPhoneViaSmsActivity.this, "Ok");
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ENABLE_ADMIN) {
            boolean isActive = deviceAdminManger.isActiveAdmin();
            LocalPrefManger.setAntiTheftEnable(LockPhoneViaSmsActivity.this,
                    isActive);
            enableLockPhoneSms.setChecked(isActive);
            enableLockPhoneSms.setChecked(deviceAdminManger.isActiveAdmin());
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        } else {
            checkSMSPermission();
        }
    }

    private boolean checkSMSPermission() {

        boolean isHaveSmsPermission = true;
        String[] perms = {LostPhoneConstant.PARM_READ_SMS_PERMISSION};

        if (!EasyPermissions.hasPermissions(this, perms)) {
            isHaveSmsPermission = false;
            EasyPermissions.requestPermissions(this, getString(R.string.sms_phonestate_and_location_rationale),
                    RC_SMS, perms);
        }

        return isHaveSmsPermission;
    }

    private void helpDialoge(Context context, String ok) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_layout_lock_sms, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.show();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                ImageView image = dialog.findViewById(R.id.imgHelp);
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.lock_phone);
                try {

                    float imageWidthInPX = (float) image.getWidth();

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                            Math.round(imageWidthInPX * (float) icon.getHeight() / (float) icon.getWidth()));
                    image.setLayoutParams(layoutParams);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
