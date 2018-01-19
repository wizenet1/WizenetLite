package com;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.model.Model;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 23/08/2016.
 */

/**
 * this class send the files (orders) to wizenet one by one in interval of time (5 mins)
 */
public class Alarm_Receiver_Text_File extends BroadcastReceiver {
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
        //Toast.makeText(context, "succeed", Toast.LENGTH_LONG).show();

        //new AsynchCallSoap().execute(); subject,comment
        //Model.getInstance().init(_context);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        String sdt = df.format(new Date(System.currentTimeMillis()));
        Log.e("MYTAG","chk: "+sdt);
        String isNetwork = "";
        if (helper.isNetworkAvailable(_context)){
            String path = Environment.getExternalStorageDirectory().getPath()+"/wizenet/offline/";
            File myDirectory = new File(path);
            traverse(myDirectory);
        }else{
            isNetwork = " on Network";
        }
        //helper.writeTextToFileORDER(sdt + isNetwork);


//        Model.getInstance().AsyncReminder(helper.getMacAddr(), new Model.ReminderListener() {
//            @Override
//            public void onResult(String str, String str2, int size) {
//                if(size==1){
//                    pushNotification(str,str2);
//                }else{
//                    pushNotification("Wizenet",size+" new messages");
//                }
//            }
//        });
    }
    public static void deleteFile(final File file) {
        if (file != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    file.delete();
                    Log.e("MYTAG","file has been deleted");
                }
            }).start();
        }
    }
    public void traverse (File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; ++i) {
                final File file = files[i];
                if (file.isDirectory()) {
                    //traverse(file);
                } else {
                    File_ f = new File_();
                    String fileContect = "";
                    fileContect = f.readFromCurrentFileInternal(_context,file);
                    //fileContect = helper.readTextFromFile(file);
                    Model.getInstance().Async_CREATE_OFFLINE_Listener(helper.getMacAddr().toString(), fileContect, new Model.CREATE_OFFLINE_Listener() {
                        @Override
                        public void onResult(String str) {
                            Toast.makeText(_context, str, Toast.LENGTH_LONG).show();
                            //File_ file = new File_(selectedFilePath);
                            if (str.trim().equals("1")){
                                deleteFile(file);
                            }
                            Log.e("MYTAG",str);
                        }
                    });
                    // do something here with the file
                }
            }
        }
    }

    //###################################
    //get mac address
    //###################################
//    public String getMacAddress() {
//        WifiManager manager = (WifiManager) this._context.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = manager.getConnectionInfo();
//        String address = info.getMacAddress();
//        return address;
//    }

    //###################################
    //WRITE URL TO FILE
    //###################################
//    public void writeTextToFile(String param){
//
//        FileWriter f;
//        File_ file = new File_(Environment.getExternalStorageDirectory().getPath()+"/wizenet/test_file_service.txt");
//        try {
//            f = new FileWriter(file,true);
//            if(!file.exists()) {
//                file.createNewFile();
//            }
//            f.write("\r\n"+param);
//            f.flush();
//            f.close();
//        }catch (Exception e){
//            e.printStackTrace();
//            Log.e("myTag",e.toString());
//        }
//    }
    public long FileSize(String fileName) {
        File file = new File(fileName);
        long filesize = file.length();
        return  filesize / 1024;
    }

}

