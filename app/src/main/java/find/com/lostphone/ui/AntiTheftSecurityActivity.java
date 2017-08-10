package find.com.lostphone.ui;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import find.com.lostphone.R;
import find.com.lostphone.data.sharedPreference.LocalPrefManger;
import find.com.lostphone.helper.DeviceAdminManger;

public class AntiTheftSecurityActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_ENABLE_ADMIN = 20;
    DeviceAdminManger deviceAdminManger;
    Toolbar toolbar;
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
                        if (isEnable && !deviceAdminManger.isActiveAdmin()) {

                            Intent intent = new
                                    Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                                    deviceAdminManger.getComponentName());
                            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                    getString(R.string.extra_explanation_admin_rights));
                            startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
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
        enableAntiTheft = findViewById(R.id.enableAntiTheft);
        edContactFirst = findViewById(R.id.edContactFirst);
        edContactSecond = findViewById(R.id.edContactSecond);
        edMsgAntiTheft = findViewById(R.id.edMsgAntiTheft);
        edLockCode = findViewById(R.id.edLockCode);
        btnSave = findViewById(R.id.btnSave);
        btnEmail = findViewById(R.id.btnEmail);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSave:

                LocalPrefManger.setMessageAntiTheft(AntiTheftSecurityActivity.this,
                        edMsgAntiTheft.getText().toString());
                LocalPrefManger.setTrustedContactFirst(AntiTheftSecurityActivity.this,
                        edContactFirst.getText().toString());
                LocalPrefManger.setTrustedContactSecond(AntiTheftSecurityActivity.this,
                        edContactSecond.getText().toString());
                LocalPrefManger.setLockCodeAntiTheft(AntiTheftSecurityActivity.this,
                        edLockCode.getText().toString());
                finish();
                break;
            case R.id.btnEmail:
                break;


        }
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
}
