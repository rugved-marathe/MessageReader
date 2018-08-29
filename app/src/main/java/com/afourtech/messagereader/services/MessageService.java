package com.afourtech.messagereader.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.afourtech.messagereader.receiver.CheckIncomingSms;

public class MessageService extends Service {

    private static final String TAG = "services.MessageService";
    CheckIncomingSms smsReceiver = new CheckIncomingSms();

    public MessageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i(TAG,"in onStartCommand()");
        registerReceiver(smsReceiver,new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        return Service.START_STICKY;
    }
}
