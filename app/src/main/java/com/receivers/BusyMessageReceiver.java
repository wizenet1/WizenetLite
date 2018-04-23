package com.receivers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.Activities.IncomingCallScreenActivity;
import com.DatabaseHelper;

import java.util.Date;

import static com.Activities.MainActivity.ctx;

/**
 * Created by bitro on 4/23/2018.
 */

public class BusyMessageReceiver extends PhoneCallReceiver {

    protected void onIncomingCallStarted(Context ctx, String number, Date start) {

        final Context context = ctx;
        final String msg = "Busy, call back later";
        final DatabaseHelper db = DatabaseHelper.getInstance(ctx);
        final String phoneNumber = number;

        boolean isBusy = db.getValueByKey("IS_BUSY").equals("1");

        // If the is_busy is checked, send sms.
        if (isBusy) {

            Uri uri = Uri.parse("smsto:" + phoneNumber);
            try {

                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", msg);
                context.startActivity(it);

            } catch (Exception e) {

                e.printStackTrace();
                Toast.makeText(context, "אין אפשרות לשלוח הודעה", Toast.LENGTH_SHORT).show();

            }

//        Thread pageTimer = new Thread() {
//
//            public void run() {
//                try {
//                    sleep(1500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//
//                    boolean isBusy = db.getValueByKey("IS_BUSY").equals("1");
//
//                    // If the is_busy is checked, send sms.
//                    if(isBusy) {
//
//                        Uri uri = Uri.parse("smsto:" + phoneNumber);
//                        try {
//
//                            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
//                            it.putExtra("sms_body", msg);
//                            context.startActivity(it);
//
//                        }
//                        catch (Exception e){
//
//                            e.printStackTrace();
//                            Toast.makeText(context, "אין אפשרות לשלוח הודעה", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                }
//            }
//        };

            //pageTimer.start();

        }

    }
}
