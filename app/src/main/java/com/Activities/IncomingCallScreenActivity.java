package com.Activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IncomingCallScreenActivity extends Activity {

    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call_screen);

       fa = this;
       final String msg1 = "busy, call later";
       final String msg2 = "In class..";
       final String msg3 = "try met later yolo...";
       final String msg4 = "hows it going? im in the middle of something";

        TextView textView1 = (LinearLayout) findViewById(R.id.msg1);
        .setText(msg1);
        textView = (TextView) findViewById(R.id.msg2);
        textView.setText(msg2);


        //String number = getIntent().getExtras().getString("INCOMING_NUMBER");
        //textView.setText("Incoming call from: " + number);

    }
}
