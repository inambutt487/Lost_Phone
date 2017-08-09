package find.com.lostphone.ui;

import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import find.com.lostphone.R;
import find.com.lostphone.data.sharedPreference.LocalPrefManger;
import find.com.lostphone.helper.DeviceAdminManger;

public class LockPhoneViaSmsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_ENABLE_ADMIN = 20;

    DeviceAdminManger deviceAdminManger;
    Toolbar toolbar;
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

        enableLockPhoneSms.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean isEnable) {

                        if (isEnable) {
                            Intent intent = new
                                    Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                                    deviceAdminManger.getComponentName());
                            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                    getString(R.string.extra_explanation_admin_rights));
                            startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);

                        } else {
                            /*deviceAdminManger.removeAdminRights();*/
                        }
                    }
                });
    }

    private void initData() {
        deviceAdminManger = new DeviceAdminManger(LockPhoneViaSmsActivity.this);
    }


    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        enableLockPhoneSms = findViewById(R.id.enableLockPhoneSms);
        edSecretCommand = findViewById(R.id.edSecretCommand);
        edLockCode = findViewById(R.id.edLockCode);
        btnEmail = findViewById(R.id.btnEmail);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setData() {
        boolean isLockEnable =
                LocalPrefManger.getEnableLockPhoneSMS(LockPhoneViaSmsActivity.this);

        enableLockPhoneSms.setChecked(deviceAdminManger.isActiveAdmin());

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
                LocalPrefManger.setSecretCommandSMS(this, secretCommand);
                LocalPrefManger.setLockCodeSMS(this, lockCode);
                LocalPrefManger.setEnableLockPhoneSMS(this, true);
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
            enableLockPhoneSms.setChecked(deviceAdminManger.isActiveAdmin());
        }
    }
}
