package find.com.lostphone.helper;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * Created by Aurang Zeb on 02-Aug-17.
 */

public class AudioMangerHelper {
    AudioManager audioManager;

    public AudioMangerHelper(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        int mode = audioManager.getMode();

        Log.d("AudioMangerHelper", "ringerMode " + ringerMode + " mode " + mode);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        Log.d("AudioMangerHelper", "ringerMode " + ringerMode + " mode " + mode);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        Log.d("AudioMangerHelper", "ringerMode " + ringerMode + " mode " + mode);

    }


}
