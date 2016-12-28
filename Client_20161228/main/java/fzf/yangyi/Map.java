package fzf.yangyi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


/**
 * Created by Lu on 2016/12/21.
 */
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;



public class Map extends Activity implements SensorEventListener{
    /** Called when the activity is first created. */
    double latitude = 0; //y
    double longitude = 0; //x
    double dynamiclatitude = 0; //dy
    double dynamiclongitude = 0; //dx
    GPSTracker gps;

    // define the display assembly compass picture
    private ImageView image;

    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;

    TextView tvHeading;
    TextView tvDistanse;

    private static double EARTH_RADIUS = 6378137.0;

    private double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }


    //private static double EARTH_RADIUS = 6378.137;
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        gps = new GPSTracker(Map.this);

        //
        image = (ImageView) findViewById(R.id.imageViewCompass);

        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        tvDistanse = (TextView) findViewById(R.id.tvDistanse);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Intent i = getIntent();
        latitude =  i.getDoubleExtra("lat",0.0);
        longitude = i.getDoubleExtra("long",0.0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {



        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);
        if(gps.canGetLocation()) {
            dynamiclatitude = gps.getLatitude();
            dynamiclongitude = gps.getLongitude();
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + dynamiclatitude + "\nLong: " + dynamiclongitude , Toast.LENGTH_LONG).show();
        } else{
        // can't get location
        // GPS or Network is not enabled
        // Ask user to enable GPS/network in settings
        gps.showSettingsAlert();
    }

        tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);


       double distance =  GetDistance(latitude,longitude,dynamiclatitude,dynamiclongitude );
       double degree_nothern =  gps2m(latitude,longitude,dynamiclatitude,dynamiclongitude );
        //System.out.println("degree: " + currentDegree + "   Data: " + event.values[0] );
        tvDistanse.setText("Distance: " + Double.toString(distance) + " meters");
        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree + (float)degree_nothern - 45;



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

}