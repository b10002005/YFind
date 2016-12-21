package com.example.lu.findgps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    Button btnShowLocation;

    // GPSTracker class
    GPSTracker gps;

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
            String Name = "";
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
            System.out.println("The best one : " + Name + " ,x : " + Latitude + " ,y : " + Longitude + " ,D : " + DistanceMin);
            System.out.println("XDDDD");

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

        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(MainActivity.this);
                //loadJSONFromAsset();
                //loadJSONFromAsset();
                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    getWiFi(latitude, longitude);
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
