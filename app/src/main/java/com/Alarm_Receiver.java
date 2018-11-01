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
import com.Classes.Call;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.Activities.MainActivity.ctx;

/**
 * this class built to set push notifications trough alarm manager
 */

public class Alarm_Receiver extends BroadcastReceiver {
    Notification_ n = new Notification_();
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
        Log.e("mytag_onReceive","onReceiveStart");
        this._context=context;
        helper = new Helper();
        //new AsynchCallSoap().execute(); subject,comment

        if (helper.isNetworkAvailable(context)){
           try{
               callWSreminders();
               callWSnewCalls(context);

           }catch (Exception e){
               //Log.e("mytag_onReceive",e.getMessage());
               helper.LogPrintExStackTrace(e);
           }

        }

    }
    private void callWSreminders(){
        Model.getInstance().AsyncReminder(getMacAddr(_context), new Model.ReminderListener() {
            @Override
            public void onResult(String str, String str2, int size,String msgID) {
                if(size==1){
                    //str2 is content with url
                    String url = helper.extractLinks(str2)[0];
                    n.pushNotification(str,str2.replace(url,""),msgID,_context);
                }else{
                    n.pushNotification("Wizenet",size+" new messages",msgID,_context);
                }
            }
        });
    }
    private void callWSnewCalls(Context context){
          String callsString = "";
        List<Call> callList = new ArrayList<Call>();
        callList = DatabaseHelper.getInstance(context).getCalls("");
        Log.e("mytag", "callList.size():" +callList.size());
        final Context context_ = context;
        for (Call c:callList) {
            callsString += c.getCallID() + ",";
        }
        if (callsString.length()>0)
            callsString = callsString.substring(0, callsString.lastIndexOf(","));

        final String callsStringFinal = callsString;
        Model.getInstance().Async_Wz_Json(getMacAddr(context), callsString, "newCalls", new Model.Wz_Json_Listener() {
            @Override
            public void onResult(String str) {
                Log.e("mytag","newCalls: "+str);

                Json_ j_ = new Json_();
                JSONArray jarray ;
                jarray = j_.getJSONArrayFromString(str,context_);

                if (jarray != null){
                    boolean isSuccess = false;
                    if (j_.addCallsFromJSONArray(jarray,context_) == true){
                        Log.e("mytag","CallID: " + j_.getElementValueInJarray(jarray,"CallID"));
                        if (jarray.length() > 1){ //-------------bigger than 1
                             n.pushNotificationNewCalls("Wizenet",String.valueOf(jarray.length()),j_.getElementValueInJarray(jarray,"subject"),j_.getElementValueInJarray(jarray,"CallID"),callsStringFinal,context_);
                        }else{ //---------------------------------equals 1
                             n.pushNotificationNewCalls("Wizenet",String.valueOf(jarray.length()),j_.getElementValueInJarray(jarray,"subject"),j_.getElementValueInJarray(jarray,"CallID"),callsStringFinal,context_);
                        }
                    }



                }


                Log.e("mytag", "success");
            }
        });
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


}

