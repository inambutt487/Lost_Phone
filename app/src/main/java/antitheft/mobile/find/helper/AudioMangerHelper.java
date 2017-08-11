package antitheft.mobile.find.helper;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by Aurang Zeb on 02-Aug-17.
 */

public class AudioMangerHelper {
    private AudioManager audioManager;
    private int oldRingerMode;
    private int oldStreamVolume;

    public AudioMangerHelper(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        oldRingerMode = audioManager.getRingerMode();
        oldStreamVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }


    public void setRingerModeNormal() {
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setStreamVolume
                (AudioManager.STREAM_MUSIC,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 10);

    }

    public void setOldRingerModeAndStreamVolume() {
        audioManager.setRingerMode(oldRingerMode);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                oldStreamVolume, 0);
    }


}
