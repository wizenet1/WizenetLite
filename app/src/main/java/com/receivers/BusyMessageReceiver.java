package com.receivers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.DatabaseHelper;

import java.util.Date;

import static com.Activities.MainActivity.ctx;

/**
 * Created by bitro on 4/23/2018.
 */

public class BusyMessageReceiver extends PhoneCallReceiver {

    public DatabaseHelper db;
    private String msg;

    public BusyMessageReceiver() {
        this.db = DatabaseHelper.getInstance(ctx);
        this.msg = "Busy, cannot answer now";
    }

    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        boolean isBusy = this.db.getValueByKey("IS_BUSY").equals("1");
        
        if(isBusy) {
            this.sendMessageToCaller(ctx, number);
        }
    }

    private void sendMessageToCaller(Context ctx, String number) {

        // TODO: Check which message was selected to send.

        // Send the sms.
        Uri uri = Uri.parse("smsto:" + number);
        try {

            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
            it.putExtra("sms_body", this.msg);
            ctx.startActivity(it);

        }
        catch (Exception e){

            e.printStackTrace();
            Toast.makeText(ctx, "אין אפשרות לשלוח הודעה", Toast.LENGTH_SHORT).show();

        }

    }
}
