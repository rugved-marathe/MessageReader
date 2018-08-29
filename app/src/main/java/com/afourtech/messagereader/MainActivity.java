package com.afourtech.messagereader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afourtech.messagereader.services.MessageService;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity";
    private static final String INBOX_URI = "content://sms/inbox";
    private static MainActivity mainActivity;
    private TextView mSendersNumber;
    private TextView mMessageBody;
    private static final int PERMISSION_RECEIVE_SMS_REQUEST_CODE = 1;
    private static final int PERMISSION_READ_SMS_REQUEST_CODE = 2;
    private View mainLayout;
    private Intent intent;

    public static MainActivity instance() {
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.main_layout);
        mSendersNumber = findViewById(R.id.textViewSenderNumber);
        mMessageBody = findViewById(R.id.textViewMessageBody);
        Button btnClear = findViewById(R.id.buttonClear);

        askForPermissions();
        btnClear.setOnClickListener(this);

        intent = new Intent(MainActivity.this, MessageService.class);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mainActivity = MainActivity.this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.this.startService(intent);
    }

    private void askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.RECEIVE_SMS)) {
                    Snackbar.make(mainLayout, R.string.permission_receive_sms_rationale,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat
                                            .requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS},
                                                    PERMISSION_RECEIVE_SMS_REQUEST_CODE);
                                }
                            }).show();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, PERMISSION_RECEIVE_SMS_REQUEST_CODE);
                }
            }
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS)) {
                    Snackbar.make(mainLayout, R.string.permission_read_sms_rationale,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat
                                            .requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS},
                                                    PERMISSION_READ_SMS_REQUEST_CODE);
                                }
                            }).show();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, PERMISSION_READ_SMS_REQUEST_CODE);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        mSendersNumber.setText(null);
        mMessageBody.setText(null);
    }

    public void getMessageDetails(HashMap<String, String> messageObject) {
        if(!messageObject.isEmpty()){
            mSendersNumber.setText(messageObject.get("sender"));
            mMessageBody.setText(messageObject.get("message"));
        }
    }
}
