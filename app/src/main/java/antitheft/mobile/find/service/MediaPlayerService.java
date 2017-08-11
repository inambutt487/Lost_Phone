package antitheft.mobile.find.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import antitheft.mobile.find.R;

public class MediaPlayerService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    MediaPlayer player;

    public MediaPlayerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initMusicPlayer();

        return START_NOT_STICKY;
    }

    public void initMusicPlayer() {
        //set player properties
        player = MediaPlayer.create(MediaPlayerService.this, R.raw.alarm);

        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setLooping(true);
        //set listeners
        player.setOnPreparedListener(this);
        player.setOnErrorListener(this);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        initMusicPlayer();
        startMusic(player);
        return false;
    }

    private void startMusic(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null)
            mediaPlayer.start();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        startMusic(player);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }
    }
}
