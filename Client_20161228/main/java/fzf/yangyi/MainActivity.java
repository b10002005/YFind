package fzf.yangyi;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    // Initializing variables
    GPSTracker gps;
    String phoneNo;
    String message;
    private double latitude = 0; //y
    private double longitude = 0; //x

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button send = (Button) findViewById(R.id.button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*

                // create class object
                gps = new GPSTracker(MainActivity.this);
                //loadJSONFromAsset();
                //loadJSONFromAsset();
                // check if GPS enabled
                if(gps.canGetLocation()){

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    phoneNo = "0975318931"; //
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
*/
                // wakeup screen 2
                Intent tran = new Intent(getApplicationContext(), Wifi_FindMap.class);
                startActivity(tran);
            }
        });
    }
}
