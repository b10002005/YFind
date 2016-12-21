package com.example.fohn.myapplication;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;



public class MainActivity extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;

    GPSTracker gps;
    Button sendBtn;
    //EditText txtphoneNo;
    //EditText txtMessage;
    String phoneNo;
    String message;
    double latitude = 0;
    double longitude = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendBtn = (Button) findViewById(R.id.btnSendSMS);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // create class object
                gps = new GPSTracker(MainActivity.this);
                //loadJSONFromAsset();
                //loadJSONFromAsset();
                // check if GPS enabled
                if(gps.canGetLocation()){

                     latitude = gps.getLatitude();
                     longitude = gps.getLongitude();

                    phoneNo = "0975318931";
                    message = "CMD:YFIND,"+Double.toString(latitude)+","+Double.toString(longitude);


                    SmsManager smsManager = SmsManager.getDefault();
                    try {
                        smsManager.sendTextMessage(phoneNo,
                                null, message,
                                PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(), 0),
                                null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

               //     getWiFi(latitude, longitude);
                    // \n is for new line
                   Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude , Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
        });
    }


}



