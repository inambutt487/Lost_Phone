package antitheft.mobile.find.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.List;

import antitheft.mobile.find.R;
import antitheft.mobile.find.data.sharedPreference.LocalPrefManger;
import antitheft.mobile.find.helper.DeviceAdminManger;
import antitheft.mobile.find.utils.LostPhoneConstant;
import antitheft.mobile.find.utils.LostPhoneUtil;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class RingSilentPhoneActivity extends AppCompatActivity
        implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private static final int RC_SMS = 30;
    DeviceAdminManger deviceAdminManger;
    ActionBar actionBar;
    Toolbar toolbar;

    SwitchCompat enableRingPhone;
    EditText edSecretCommandRing;
    Button btnSave, btnEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_silent_phone);
        initData();
        initUI();
        setData();

        enableRingPhone.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean isEnable) {
                        if (checkSMSPermission()) {
                            LocalPrefManger.setRingPhoneEnable(RingSilentPhoneActivity.this,
                                    isEnable);

                        } else {
                            LocalPrefManger.setRingPhoneEnable(RingSilentPhoneActivity.this,
                                    false);
                            enableRingPhone.setChecked(false);
                        }
                    }
                });

    }


    private void initData() {
        deviceAdminManger = new DeviceAdminManger(this);
    }

    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        setuptoolbar();
        enableRingPhone = findViewById(R.id.enableRingPhone);
        edSecretCommandRing = findViewById(R.id.edSecretCommandRing);
        btnSave = findViewById(R.id.btnSave);
        btnEmail = findViewById(R.id.btnEmail);
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

        enableRingPhone.
                setChecked(checkSMSPermission()&&
                        LocalPrefManger.getRingPhoneEnable(RingSilentPhoneActivity.this));
        edSecretCommandRing.
                setText(LocalPrefManger.getRingSecretCommand(RingSilentPhoneActivity.this));
    }

    private boolean checkSMSPermission() {

        boolean isHaveSmsPermission = true;
        String[] perms = {LostPhoneConstant.PARM_READ_SMS_PERMISSION};

        if (!EasyPermissions.hasPermissions(this, perms)) {
            isHaveSmsPermission = false;
            EasyPermissions.requestPermissions(this, getString(R.string.with_out_this_request),
                    RC_SMS, perms);
        }

        return isHaveSmsPermission;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:

                /*Todo Check for validations*/
                String secretCommand = edSecretCommandRing.getText()
                        .toString().toLowerCase().trim();

                if (isValidationSuccess(secretCommand)) {
                    LocalPrefManger.setRingSecretCommand
                            (RingSilentPhoneActivity.this, secretCommand);
                    finish();
                }

                break;
            case R.id.btnEmail:
                String subject = "Ring Silent Phone";
                String body = "Secret Command: " +
                        LocalPrefManger.getRingSecretCommand(RingSilentPhoneActivity.this);

                LostPhoneUtil.sendEmail(this, subject, body);
                break;
        }

    }

    private boolean isValidationSuccess(String secretCommand) {

        boolean isSuccess = true;
        if (secretCommand == null || secretCommand.isEmpty()) {
            isSuccess = false;
            edSecretCommandRing.setError("Please write secret ring command ,this can not be empty");

        }

        if (secretCommand != null && secretCommand.length() < 4) {
            isSuccess = false;
            edSecretCommandRing.setError("Command should be greater or eqaul to four character");

        }

        if (secretCommand != null && secretCommand.length() > 20) {
            isSuccess = false;
            edSecretCommandRing.setError("Command should be less or eqaul to twenty character");

        }
        return isSuccess;
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
