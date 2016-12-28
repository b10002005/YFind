package fzf.yangyi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;


public class Wifi_FindMap extends Activity {

    private IntentFilter receiveFilter;
    private SMSReceiver smsReceiver;
    private String fullMessage = "";
    double latitude = 0;
    double longitude = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_findmap);

        receiveFilter = new IntentFilter();
        receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        smsReceiver = new SMSReceiver();
        registerReceiver(smsReceiver, receiveFilter);


        ImageButton btn = (ImageButton) findViewById(R.id.imageView2);
        btn.getBackground().setAlpha(0);
        btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //重新设置按下时的背景图片
                    ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.wifi_pressed));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //再修改为抬起时的正常图片
                    ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.wifi_unpressed));
                }
                return false;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent jump = new Intent(getApplicationContext(), Map.class);
                jump.putExtra("lat",25.013304);
                jump.putExtra("long",121.541619);

                startActivity(jump);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);

    }

    public class SMSReceiver extends BroadcastReceiver {

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
                fullMessage = "";

                for (SmsMessage message : messages) {
                    fullMessage += message.getMessageBody(); //獲取簡訊內容
                }

                final String[] getxy = fullMessage.split(",");
                if (getxy[0].equals("CMD:YFIND")) {
                    latitude = Double.parseDouble(getxy[2]);
                    longitude = Double.parseDouble(getxy[3]);

                    Intent jump = new Intent(getApplicationContext(), Map.class);
                    jump.putExtra("x",latitude);
                    jump.putExtra("y",longitude);

                    startActivity(jump);
                }
            }
        }

    }
}