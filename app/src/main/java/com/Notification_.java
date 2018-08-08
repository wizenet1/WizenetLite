package com;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.Activities.ActivityCallDetails;
import com.Activities.ActivityCalls;
import com.Activities.MenuActivity;
import com.Activities.R;
import com.model.Model;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * Created by WIZE02 on 08/08/2018.
 */

public class Notification_ {

    public void pushNotification(String title,String text,String msgID,Context _context){
        long[] vibrate = {400, 400, 200, 200, 200};
        Model.getInstance().init(_context);
        //here we targeting the
        Intent notificationIntent = new Intent(_context, MenuActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.addFlags(FLAG_UPDATE_CURRENT);
        notificationIntent.putExtra("puId", String.valueOf(msgID));

        PendingIntent intent = PendingIntent.getActivity(_context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) _context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification = new Notification.Builder(_context);
        notification.setAutoCancel(true);
        notification.setContentIntent(intent);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //builder.setTicker("this is ticker text");
        notification.setContentTitle(title);
        notification.setContentText(text);
        notification.setSmallIcon(R.drawable.face);
        notification.setSound(alarmSound);
        notification.setOngoing(true);
        notification.setVibrate(vibrate);
        Notification notificationn = notification.getNotification();
        notificationManager.notify(0, notificationn);
    }
    public void pushNotificationNewCalls(String title,String count,String firstSubject,String callid,Context _context){
        long[] vibrate = {400, 400, 200, 200, 200};
        Model.getInstance().init(_context);
        //here we targeting the
        Intent notificationIntent ;
        if (count.equals("1")){
            notificationIntent= new Intent(_context, ActivityCallDetails.class);
        }else{
            notificationIntent= new Intent(_context, ActivityCalls.class);
        }

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.addFlags(FLAG_UPDATE_CURRENT);
        Log.e("mytag","count: "+ String.valueOf(count) + " callid:"+ String.valueOf(callid));
        notificationIntent.putExtra("EXTRA_SESSION_ID", callid);
        //notificationIntent.putExtra("callid", String.valueOf(callid));

        PendingIntent intent = PendingIntent.getActivity(_context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) _context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification = new Notification.Builder(_context);
        notification.setAutoCancel(true);
        notification.setContentIntent(intent);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //builder.setTicker("this is ticker text");
        notification.setContentTitle(title);
        if (Integer.valueOf(count)>1){
            notification.setContentText(count + " קריאות חדשות");
        }else{
            notification.setContentText( " קריאה חדשה: " + firstSubject);
        }

        notification.setSmallIcon(R.drawable.face);
        notification.setSound(alarmSound);
        notification.setOngoing(true);
        notification.setVibrate(vibrate);
        Notification notificationn = notification.getNotification();
        notificationManager.notify(0, notificationn);
    }
}
