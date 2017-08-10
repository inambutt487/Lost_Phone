package find.com.lostphone.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;

import find.com.lostphone.data.sharedPreference.LocalPrefManger;
import find.com.lostphone.service.LocationService;
import find.com.lostphone.utils.LostPhoneConstant;

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

            if (simSerialNumber == null) {
                if (tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
                    DeviceAdminManger adminManger = new DeviceAdminManger(context);
                    adminManger.lockWithPIN(LocalPrefManger.getLockCodeAntiTheft(context));
                }
            }
            if (simSerialNumber != null && !saveSimSerialNumber.isEmpty()
                    && !simSerialNumber.equals(saveSimSerialNumber)) {
                DeviceAdminManger adminManger = new DeviceAdminManger(context);
                adminManger.lockWithPIN(LocalPrefManger.getLockCodeAntiTheft(context));
                String Imei = "";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Imei = tm.getImei();
                } else {
                    Imei = tm.getDeviceId();
                }
                Intent intent = new Intent(context, LocationService.class);
                intent.putExtra(LostPhoneConstant.EXTRA_ANTI_THEFT, "AntiThef");
                intent.putExtra(LostPhoneConstant.EXTRA_IMEI,Imei);
                context.startService(intent);
            }

        }
    }

}
