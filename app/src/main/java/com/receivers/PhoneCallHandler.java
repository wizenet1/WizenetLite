package com.receivers;

import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;
import android.widget.Toast;
import com.Activities.IncomingCallScreenActivity;
import java.util.Date;

/**
 * The class represents a specific call receiver handler.
 * It extends the PhoneCallReceiver to capture the different phone calls states, and handles them.
 */
public class PhoneCallHandler extends PhoneCallReceiver {
    @Override
    protected void onIncomingCallStarted(Context ctx, final String number, Date start) {

        Toast.makeText(ctx, "Incoming call started", Toast.LENGTH_SHORT).show();

        final Context context = ctx;


        //Open the incoming call screen.

        Thread pageTimer = new Thread() {
            public void run() {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent i = new Intent();
                    i.setClass(context, IncomingCallScreenActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.putExtra("INCOMING_NUMBER", number);
                    i.setAction(Intent.ACTION_MAIN);
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    context.startActivity(i);
                }
            }
        };
        pageTimer.start();
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        try {
            //Close the incoming call screen.
            IncomingCallScreenActivity.fa.finish();
        } catch (NullPointerException e) {
        }
        Toast.makeText(ctx, "Incoming call ended", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        try {
            //Close the incoming call screen.
            IncomingCallScreenActivity.fa.finish();
        } catch (NullPointerException e) {
        }

        Toast.makeText(ctx, "Call missed", Toast.LENGTH_SHORT).show();
    }
}
