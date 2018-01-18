package com.Activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class IncomingCallScreenActivity extends Activity {

    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call_screen);

       fa = this;

        TextView textView = (TextView) findViewById(R.id.phoneNumber_txt);

        String number = getIntent().getExtras().getString("INCOMING_NUMBER");
        textView.setText("Incoming call from: " + number);
    }
}
