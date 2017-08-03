package find.com.lostphone.ui;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import find.com.lostphone.R;
import find.com.lostphone.helper.AudioMangerHelper;

public class RingingActivity extends AppCompatActivity {

    RelativeLayout ringLayout;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringing);
        ringLayout = findViewById(R.id.ringLayout);

        AudioMangerHelper mangerHelper=
                new AudioMangerHelper(RingingActivity.this);
        mangerHelper.setRingerModeNormal();
        mediaPlayer = MediaPlayer.create(RingingActivity.this, R.raw.alarm);

        mediaPlayer.setLooping(true);
        mediaPlayer.start();


        ringLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RingingActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
}
