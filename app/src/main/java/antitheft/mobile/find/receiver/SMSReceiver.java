package antitheft.mobile.find.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import antitheft.mobile.find.data.sharedPreference.LocalPrefManger;
import antitheft.mobile.find.helper.DeviceAdminManger;
import antitheft.mobile.find.service.LocationService;
import antitheft.mobile.find.ui.RingingActivity;
import antitheft.mobile.find.utils.LostPhoneConstant;

/**
 * Created by Aurang Zeb on 31-Jul-17.
 */

public class SMSReceiver extends BroadcastReceiver {

    // SmsManager class is responsible for all SMS related actions


    public void onReceive(Context context, Intent intent) {



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

                            if (message.equals(LocalPrefManger.
                                    getSecretCommandSMS(context))) {
                                    /*Todo change Device password */
                                DeviceAdminManger adminManger = new DeviceAdminManger(context);
                                if (adminManger.isActiveAdmin()) {
                                    adminManger.lockWithPIN(LocalPrefManger.
                                            getLockCodeSMS(context));
                                    this.abortBroadcast();
                                }

                                break;
                            } else if (message.equals(LocalPrefManger.
                                    getRingSecretCommand(context)) &&
                                    LocalPrefManger.getRingPhoneEnable(context)) {

                                Intent ringingActivityIntent =
                                        new Intent(context, RingingActivity.class);
                                ringingActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(ringingActivityIntent);
                                this.abortBroadcast();
                            } else if (message.
                                    equals(LocalPrefManger.getLocationSecretCommand(context))
                                    && LocalPrefManger.getLocationPhoneEnable(context)) {
                                try {

                                    Intent serviceIntent = new Intent(context, LocationService.class);
                                    serviceIntent.putExtra(LostPhoneConstant.EXTRA_PHONE_NUMBER, sender);
                                    context.startService(serviceIntent);

                               /*     String messageToSend="Mobile location";
                                    sendSMS(context,sender,messageToSend);*/
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
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

    public static void sendSMS(final Context context, String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
     /*   context.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
       context.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));*/

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
}
