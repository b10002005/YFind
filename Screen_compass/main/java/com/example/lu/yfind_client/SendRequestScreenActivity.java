package com.example.lu.yfind_client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Lu on 2016/12/21.
 */


public class SendRequestScreenActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen2);

        ImageButton btnClose = (ImageButton) findViewById(R.id.imageButtonSend);

        Intent i = getIntent();
        // Receiving the Data
        String phone = i.getStringExtra("phone");
        Log.e("Second Screen", phone + "." );


        // Binding Click event to Button
        btnClose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Closing SecondScreen Activity
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), FindWiFiScreenActivity.class);
                startActivity(nextScreen);

            }
        });

    }
}