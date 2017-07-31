package find.com.lostphone.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import find.com.lostphone.R;

/**
 * Created by Aurang Zeb on 31-Jul-17.
 */

public class SMSReceiver extends BroadcastReceiver {

    // SmsManager class is responsible for all SMS related actions
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {
        SmsMessage sms;
        // Get the SMS message received
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                // A PDU is a "protocol data unit". This is the industrial standard for SMS message
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {
                    // This will create an SmsMessage object from the received pdu
                    sms= SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String sender = sms.getDisplayOriginatingAddress();
                    String message = sms.getDisplayMessageBody();


                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
