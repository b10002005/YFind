package com.example.lu.yfind_client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Lu on 2016/12/21.
 */


public class LoginScreenActivity extends Activity {
    // Initializing variables
    EditText inputPhone;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen1);

        inputPhone = (EditText) findViewById(R.id.phoneNumber);
        Button btnPhoneCheck = (Button) findViewById(R.id.buttonCheck);

        //Listening to button event
        btnPhoneCheck.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), SendRequestScreenActivity.class);

                //Sending data to another Activity
                nextScreen.putExtra("phone", inputPhone.getText().toString());
                Log.e("n", inputPhone.getText()+".");
                startActivity(nextScreen);

            }
        });
    }
}