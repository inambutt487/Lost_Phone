package find.com.lostphone.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import find.com.lostphone.helper.AntiTheftHelper;

/**
 * Created by Aurang Zeb on 09-Aug-17.
 */

public class SimChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.d("SimChangedReceiver", "--> SIM state changed <--");
        AntiTheftHelper.antiTheftCheck(context);

        // Most likely, checking if the SIM changed could be limited to
        // events where the intent's extras contains a key "ss" with value "LOADED".
        // But it is more secure to just always check if there was a change.


    }

}
