package com.Activities;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DatabaseHelper;

import java.lang.reflect.Method;

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
        final String number = getIntent().getExtras().getString("INCOMING_NUMBER");;

        TextView textView1 = (TextView) findViewById(R.id.msg1);
        textView1.setText(msg1);
        TextView textView2 = (TextView) findViewById(R.id.msg2);
        textView2.setText(msg2);
        TextView textView3 = (TextView) findViewById(R.id.msg3);
        textView3.setText(msg3);
        TextView textView4 = (TextView) findViewById(R.id.msg4);
        textView4.setText(msg4);

        LinearLayout l1 = (LinearLayout) findViewById(R.id.msgLinearLayout1);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IncomingCallScreenActivity.this.sendMessage(number, IncomingCallScreenActivity.this, msg1);
            }
        });

        LinearLayout l2 = (LinearLayout) findViewById(R.id.msgLinearLayout2);
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IncomingCallScreenActivity.this.sendMessage(number, IncomingCallScreenActivity.this, msg2);
            }
        });

        LinearLayout l3 = (LinearLayout) findViewById(R.id.msgLinearLayout3);
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IncomingCallScreenActivity.this.sendMessage(number, IncomingCallScreenActivity.this, msg3);
            }
        });

        LinearLayout l4 = (LinearLayout) findViewById(R.id.msgLinearLayout4);
        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IncomingCallScreenActivity.this.sendMessage(number, IncomingCallScreenActivity.this, msg4);
            }
        });
    }

    public void sendMessage(String number, Context ctx, String message) {

        final Context context = ctx;
        final String msg = "Busy, call back later";
        final DatabaseHelper db = DatabaseHelper.getInstance(ctx);
        final String phoneNumber = number;

        Thread pageTimer = new Thread() {

            public void run() {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    boolean isBusy = db.getValueByKey("IS_BUSY").equals("1");

                    // If the is_busy is checked, send sms.
                    if(isBusy) {

                        Uri uri = Uri.parse("smsto:" + phoneNumber);
                        try {

                            // Send the short message.
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNumber, null, msg, null, null);

                            this.disconnectCall();

                        }
                        catch (Exception e){

                            e.printStackTrace();

                        }
                    }
                }
            }

            private void disconnectCall(){
                try {

                    String serviceManagerName = "android.os.ServiceManager";
                    String serviceManagerNativeName = "android.os.ServiceManagerNative";
                    String telephonyName = "com.android.internal.telephony.ITelephony";
                    Class<?> telephonyClass;
                    Class<?> telephonyStubClass;
                    Class<?> serviceManagerClass;
                    Class<?> serviceManagerNativeClass;
                    Method telephonyEndCall;
                    Object telephonyObject;
                    Object serviceManagerObject;
                    telephonyClass = Class.forName(telephonyName);
                    telephonyStubClass = telephonyClass.getClasses()[0];
                    serviceManagerClass = Class.forName(serviceManagerName);
                    serviceManagerNativeClass = Class.forName(serviceManagerNativeName);
                    Method getService = // getDefaults[29];
                            serviceManagerClass.getMethod("getService", String.class);
                    Method tempInterfaceMethod = serviceManagerNativeClass.getMethod("asInterface", IBinder.class);
                    Binder tmpBinder = new Binder();
                    tmpBinder.attachInterface(null, "fake");
                    serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder);
                    IBinder retbinder = (IBinder) getService.invoke(serviceManagerObject, "phone");
                    Method serviceMethod = telephonyStubClass.getMethod("asInterface", IBinder.class);
                    telephonyObject = serviceMethod.invoke(null, retbinder);
                    telephonyEndCall = telephonyClass.getMethod("endCall");
                    telephonyEndCall.invoke(telephonyObject);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        pageTimer.start();
    }
}
