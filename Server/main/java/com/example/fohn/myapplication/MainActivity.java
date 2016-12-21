package com.example.fohn.myapplication;

import java.lang.Object;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;
import android.telephony.SmsMessage;
import android.app.PendingIntent;
import android.telephony.SmsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    String Name = "";
    private IntentFilter receiveFilter;
    private MessageReceiver messageReceiver;
    private TextView sender;
    private TextView content;
    private String fullMessage = "";
    private String phoneNumber = "";
    private String returnMessage = "";


    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("itaiwan.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
            //System.out.println(json);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public void getWiFi(double x1, double y1) {
        try {
            double DistanceMin = Double.MAX_VALUE;
            double Latitude = 0, Longitude = 0, Distance = 0;

            String in = loadJSONFromAsset();
            JSONObject reader = new JSONObject(in);
            JSONArray sys  = reader.getJSONArray("wifi");
            for (int i = 0; i < sys.length(); i++) {
                JSONObject c = sys.getJSONObject(i);
                String Administration = c.getString("Administration");
                String Region = c.getString("Region");
                String HotspotName = c.getString("HotspotName");
                String Address = c.getString("Address");
                double x2 = Double.parseDouble(c.getString("Latitude"));
                double y2 = Double.parseDouble(c.getString("Longitude"));

                Distance = Math.sqrt(Math.pow(x2-x1,2)+ Math.pow(y2-y1,2));
                if(Distance < DistanceMin) {
                    Latitude    = x2;
                    Longitude   = y2;
                    Name        = HotspotName;
                    DistanceMin = Distance;
                    System.out.println("Find the better one : " + Name + " ,x : " + Latitude + " ,y : " + Longitude + " ,D : " + DistanceMin);
                }


            }
            returnMessage = "CMD:YFIND,"+ Name +","+Double.toString(Latitude)+","+Double.toString(Longitude);

            SmsManager smsManager = SmsManager.getDefault();
            try {
                smsManager.sendTextMessage(phoneNumber,
                        null, returnMessage,
                        PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(), 0),
                        null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (JSONException e) {
            System.out.println("Can't read the json file!");
            e.printStackTrace();
        } { }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sender = (TextView) findViewById(R.id.sender);
        content = (TextView) findViewById(R.id.content);
        receiveFilter = new IntentFilter();
        receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        messageReceiver = new MessageReceiver();
        registerReceiver(messageReceiver, receiveFilter);
    }

    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle myBundle = intent.getExtras();
            SmsMessage[] messages = null;


            if (myBundle != null) {
                Object[] pdus = (Object[]) myBundle.get("pdus");
                messages = new SmsMessage[pdus.length];
                for (int i = 0; i < messages.length; i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = myBundle.getString("format");
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    } else {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                }
                phoneNumber = messages[0].getOriginatingAddress(); //獲取發送方號碼
                fullMessage = "";

                for (SmsMessage message : messages) {
                    fullMessage += message.getMessageBody(); //獲取簡訊內容
                }
                sender.setText(phoneNumber);
                content.setText(fullMessage);
                // if(phoneNumber.equals("0980963616") ) {

                final String[] getxy = fullMessage.split(",");
                if (getxy[0].equals("CMD:YFIND")){
                    getWiFi(Double.parseDouble(getxy[1]),Double.parseDouble(getxy[2]));

                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(messageReceiver);
    }

}