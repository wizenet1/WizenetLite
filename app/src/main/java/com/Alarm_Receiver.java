package com;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.Activities.MainActivity;
import com.Activities.MenuActivity;
import com.Activities.R;
import com.Helper;
import com.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.Activities.MainActivity.ctx;

/**
 * this class built to set push notifications trough alarm manager
 */

public class Alarm_Receiver extends BroadcastReceiver {
    NotificationManager nm;
    Notification myNotication;

    long[] vibrate = { 400, 400, 200,200,200 };
    Calendar calendar = Calendar.getInstance();
    Helper helper;
    int hours = calendar.get(Calendar.HOUR_OF_DAY);
    int minutes = calendar.get(Calendar.MINUTE);
    int seconds = calendar.get(Calendar.SECOND);
    private Context _context;

    //String msgID="",msgSubject="",msgComment="",msgUrl="",msgDate="",msgRead="",msgType= "";

    @Override
    public void onReceive(Context context, Intent intent) {
        this._context=context;
        helper = new Helper();
        //new AsynchCallSoap().execute(); subject,comment
        if (helper.isNetworkAvailable(context)){
           try{
               //Model.getInstance().init(_context);
               Model.getInstance().AsyncReminder(getMacAddr(_context), new Model.ReminderListener() {
                   @Override
                   public void onResult(String str, String str2, int size,String msgID) {
                       if(size==1){
                           //str2 is content with url
                           String url = helper.extractLinks(str2)[0];
                           pushNotification(str,str2.replace(url,""),msgID);
                       }else{
                           pushNotification("Wizenet",size+" new messages",msgID);
                       }
                   }
               });
           }catch (Exception e){
               helper.LogPrintExStackTrace(e);
           }

        }

    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public  String getMacAddr(Context ctx) {
        try{
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = tm.getDeviceId();
            return device_id;
        }catch(Exception e){
            Helper h = new Helper();
            h.LogPrintExStackTrace(e);
            return "";
        }
    }


    //###################################
    //WRITE URL TO FILE
    //###################################
    public void writeTextToFile(String param){
        FileWriter f;
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/serviceTime.txt");
        try {
            f = new FileWriter(file,true);
            if(!file.exists()) {
                file.createNewFile();
            }
            f.write("\r\n"+param);
            f.flush();
            f.close();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("myTag",e.toString());
        }
    }
    private void pushNotification(String title,String text,String msgID){
        long[] vibrate = {400, 400, 200, 200, 200};
        Model.getInstance().init(_context);
        Intent notificationIntent = new Intent(_context, MenuActivity.class);
// set intent so it does not start a new activity
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
    private void pushNotification1(String title,String text,String msgID){
//        PendingIntent contentIntent = PendingIntent.getActivity(_context, 0,
//                new Intent(_context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Intent aint = new Intent(_context, MenuActivity.class);
        aint.putExtra("puId", String.valueOf(msgID));

        PendingIntent contentIntent = PendingIntent.getBroadcast(
                _context,
                0,
                aint,
                // as stated in the comments, this flag is important!
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(_context);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setAutoCancel(true);
        //builder.setTicker("this is ticker text");
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.face);
        builder.setContentIntent(contentIntent);
        builder.setSound(alarmSound);
        builder.setOngoing(true);
        builder.setVibrate(vibrate);

        //mBuilder.setContentIntent(contentIntent);
        //builder.setSubText("This is subtext...");   //API level 16
        myNotication = builder.build();

        nm = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(11, myNotication);
    }
}

