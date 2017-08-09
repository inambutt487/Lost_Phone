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

public class RingSilentPhoneActivity extends AppCompatActivity
        implements View.OnClickListener {

    DeviceAdminManger deviceAdminManger;

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
                        LocalPrefManger.setRingPhoneEnable(RingSilentPhoneActivity.this,
                                isEnable);


                    }
                });

    }


    private void initData() {
        deviceAdminManger = new DeviceAdminManger(this);
    }

    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        enableRingPhone = findViewById(R.id.enableRingPhone);
        edSecretCommandRing = findViewById(R.id.edSecretCommandRing);
        btnSave = findViewById(R.id.btnSave);
        btnEmail = findViewById(R.id.btnEmail);
    }


    private void setData() {

        enableRingPhone.
                setChecked(
                        LocalPrefManger.getRingPhoneEnable(RingSilentPhoneActivity.this));
        edSecretCommandRing.
                setText(LocalPrefManger.getRingSecretCommand(RingSilentPhoneActivity.this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:

                /*Todo Check for validations*/
                String secretCommand = edSecretCommandRing.getText()
                        .toString().toLowerCase().trim();
                LocalPrefManger.setRingSecretCommand
                        (RingSilentPhoneActivity.this, secretCommand);
                finish();

                break;
            case R.id.btnEmail:
                break;
        }

    }
}
