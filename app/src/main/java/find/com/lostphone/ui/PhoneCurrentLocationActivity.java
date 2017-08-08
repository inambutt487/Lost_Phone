package find.com.lostphone.ui;

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

public class PhoneCurrentLocationActivity extends AppCompatActivity
        implements View.OnClickListener{

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
                        LocalPrefManger.setLocationPhoneEnable(
                                PhoneCurrentLocationActivity.this,
                                isEnable);

                    }
                });

    }


    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        enablePhoneLocation=findViewById(R.id.enablePhoneLocation);
        edSecretLocationCommand=findViewById(R.id.edSecretLocationCommand);
        btnSave=findViewById(R.id.btnSave);
    }
    private void setData() {
        enablePhoneLocation.setChecked(LocalPrefManger.getLocationPhoneEnable(this));
        edSecretLocationCommand.setText(LocalPrefManger.getLocationSecretCommand(this));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSave:

                /*Todo Check for validations*/
                String secretCommand = edSecretLocationCommand.getText()
                        .toString().toLowerCase().trim();
                LocalPrefManger.setLocationSecretCommand
                        (PhoneCurrentLocationActivity.this,secretCommand);
                finish();

                break;
            case R.id.btnEmail:
                break;
        }
    }
}
