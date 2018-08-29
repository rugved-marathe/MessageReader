package com.afourtech.messagereader.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.afourtech.messagereader.MainActivity;

import java.util.HashMap;

public class CheckIncomingSms extends BroadcastReceiver {

    SmsManager smsManager = SmsManager.getDefault();
    private String format;
    private String phoneNumber;
    private String messageBody;
    private static final String TAG = "CheckIncomingSms";

    public CheckIncomingSms() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("SmsReceiver : onReceive", "Inside onReceive() method.");
        Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
//                A PDU is Protocol Data Unit. This is the industrial standard for SMS Message.
                final Object[] pduObject = (Object[]) bundle.get("pdus");
                format = bundle.getString("format");
                SmsMessage message = SmsMessage.createFromPdu((byte[]) pduObject[0], format);

                phoneNumber = message.getDisplayOriginatingAddress();
                messageBody = message.getDisplayMessageBody();

                HashMap<String, String> messageObject = new HashMap<>();
                messageObject.put("sender", phoneNumber);
                messageObject.put("message", messageBody);
                MainActivity inst = MainActivity.instance();
                Toast.makeText(context, "" + phoneNumber, Toast.LENGTH_LONG).show();
                Log.i(TAG, "In OnReceive() method's end");
                inst.getMessageDetails(messageObject);
                showNotification(context);
            }
        } catch (NullPointerException e) {
            Log.i("SmsReceiver : onReceive", " This method to call some function.");
            e.printStackTrace();
        }
    }

    private void showNotification(Context context) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "default")
//                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("New Message")
                .setContentText("New message received")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
//                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

    private SmsMessage getIncomingMessage(Object object, Bundle bundle) {
        SmsMessage smsMessage;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            smsMessage = SmsMessage.createFromPdu((byte[]) object, format);
        } else {
            smsMessage = SmsMessage.createFromPdu((byte[]) object);
        }

        return smsMessage;

    }
}
