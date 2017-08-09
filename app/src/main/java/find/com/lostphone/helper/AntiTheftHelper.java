package find.com.lostphone.helper;

import android.content.Context;
import android.telephony.TelephonyManager;

import find.com.lostphone.data.sharedPreference.LocalPrefManger;

/**
 * Created by Aurang Zeb on 09-Aug-17.
 */

public class AntiTheftHelper {

    public static void antiTheftCheck(Context context) {
        if (LocalPrefManger.getAntiTheftEnable(context)) {
            TelephonyManager tm = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = tm.getSimSerialNumber();
            String saveSimSerialNumber = LocalPrefManger.getSimSerialNumber(context);

            if (simSerialNumber != null && !saveSimSerialNumber.isEmpty()&& !simSerialNumber.equals(saveSimSerialNumber)) {
                DeviceAdminManger adminManger = new DeviceAdminManger(context);
                adminManger.lockWithPIN(LocalPrefManger.getLockCodeAntiTheft(context));
                // TODO: 09-Aug-17 send SMS
            }
        }
    }

}
