package find.com.lostphone.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import find.com.lostphone.R;
import find.com.lostphone.helper.AudioMangerHelper;
import find.com.lostphone.service.MediaPlayerService;

public class RingingActivity extends AppCompatActivity {
    AudioMangerHelper mangerHelper;

    RelativeLayout ringLayout;
    /*MediaPlayer mediaPlayer;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringing);
        initData();
        initUI();

    }

    private void initUI() {
        ringLayout = findViewById(R.id.ringLayout);
        ringLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopServiceBackPreviousM();
            }
        });
    }

    private void initData() {
        mangerHelper = new AudioMangerHelper(RingingActivity.this);
        mangerHelper.setRingerModeNormal();
        startService(new Intent(RingingActivity.this, MediaPlayerService.class));
    }

    @Override
    public void onBackPressed() {
        stopServiceBackPreviousM();
        Intent intent = new Intent(RingingActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }

    private void stopServiceBackPreviousM() {
        mangerHelper.setOldRingerModeAndStreamVolume();
        stopService(new Intent(RingingActivity.this, MediaPlayerService.class));
    }

}
