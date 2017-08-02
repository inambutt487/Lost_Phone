package find.com.lostphone.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import find.com.lostphone.data.sharedPreference.LocalPrefManger;
import find.com.lostphone.helper.DeviceAdminManger;
import find.com.lostphone.ui.RingingActivity;

/**
 * Created by Aurang Zeb on 31-Jul-17.
 */

public class SMSReceiver extends BroadcastReceiver {

    // SmsManager class is responsible for all SMS related actions
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        DeviceAdminManger adminManger = new DeviceAdminManger(context);
        if (adminManger.isActiveAdmin()) {
            /*todo permission check*/
            SmsMessage sms;
            // Get the SMS message received
            final Bundle bundle = intent.getExtras();


            try {

                if (bundle != null) {

                    // A PDU is a "protocol data unit". This is the industrial standard for SMS message
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    if (pdusObj != null) {
                        for (Object aPdusObj : pdusObj) {
                            // This will create an SmsMessage object from the received pdu
                            sms = getIncomingMessage(aPdusObj, bundle);
                            String sender = sms.getDisplayOriginatingAddress();
                            String message = sms.getDisplayMessageBody();
                            if (message != null && !message.isEmpty()) {
                                message = message.replaceAll("\\s+", "")
                                        .toLowerCase().trim();
                                String secretCommand = LocalPrefManger.
                                        getSecretCommandSMS(context);
                                if (message.equals(secretCommand)) {
                                    /*Todo change Device password */
                                    adminManger.lockWithPIN(LocalPrefManger.
                                            getLockCodeSMS(context));
                                    this.abortBroadcast();
                                    break;
                                }
                                else if(message.equals("ringmyphone")){
                                    Intent ringingActivityIntent=
                                            new Intent(context, RingingActivity.class);
                                    ringingActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(ringingActivityIntent);

                                }
                            }

                        }

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);
        } else {
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject);
        }
        return currentSMS;
    }
}
