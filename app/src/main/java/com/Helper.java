package com;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.Activities.R;
import com.Classes.Ccustomer;
import com.Classes.Order;
import com.Fragments.*;

import com.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.NetworkInterface;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


import static com.Activities.MainActivity.ctx;
import static java.security.AccessController.getContext;

/**
 * Created by User on 31/07/2016.
 */
public class Helper {
    public Helper() {
    }


    public void addInitialfirst(Context ctx){
        DatabaseHelper.getInstance(ctx).addControlPanel("Cfname","");
        DatabaseHelper.getInstance(ctx).addControlPanel("AUTO_LOGIN","0");
        DatabaseHelper.getInstance(ctx).addControlPanel("CID","");
        DatabaseHelper.getInstance(ctx).addControlPanel("CtypeID","");
        DatabaseHelper.getInstance(ctx).addControlPanel("CtypeName","");
        DatabaseHelper.getInstance(ctx).addControlPanel("dropHTTP","http://");
        DatabaseHelper.getInstance(ctx).addControlPanel("username","");
        DatabaseHelper.getInstance(ctx).addControlPanel("BACKGROUND","1");
        DatabaseHelper.getInstance(ctx).addControlPanel("GPS","0");
        DatabaseHelper.getInstance(ctx).addControlPanel("URL","");// url.getText().toString());
        DatabaseHelper.getInstance(ctx).addControlPanel("CLIENT_SYNC_PRODUCTS","0");
        DatabaseHelper.getInstance(ctx).addControlPanel("APPS_CALLS_SUMMARY","1");
            DatabaseHelper.getInstance(ctx).addControlPanel("IS_BUSY","1");


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c_week = Calendar.getInstance();
        c_week.add(Calendar.DAY_OF_YEAR, 7);
        String formatted = df.format(c_week.getTime());

        DatabaseHelper.getInstance(ctx).addControlPanel("PRODUCTS_UPDATE",formatted);
        DatabaseHelper.getInstance(ctx).addControlPanel("CLIENTS_PRODUCTS_UPDATE",formatted);

        Toast.makeText(ctx,"url does not exists", Toast.LENGTH_SHORT).show();
//        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/wizenet/");
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
    }
    public  long getDateDiff(String oldDate, String newDate) { //now - new date - old the old one
        Helper h = new Helper();
        long ret = 0;
        try {
            Log.e("mytag"," dateOld: " +oldDate.toString()+ " dateNew: " +newDate.toString()+" mins: ");
            long dateNew =  Long.valueOf(newDate);
            long dateOld = Long.valueOf(oldDate);
            long diff = dateNew - dateOld;
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            ret = minutes;
            //long days = hours / 24;
            //Log.e("mytag"," dateOld: " +oldDate.toString()+ " dateNew: " +newDate.toString()+" mins: "+minutes);
            return minutes;
        } catch (Exception e) {
            h.LogPrintExStackTrace(e);
            //e.printStackTrace();
        }
        return ret;
    }
    public boolean writeCtypeIDandSons(final Context ctx){
        try{
            Model.getInstance().Async_Wz_getCtypeIDandSons_Listener(getMacAddr(), new Model.Wz_getCtypeIDandSons_Listener() {
                @Override
                public void onResult(String str) {
                    String ctypeids = "";
                    String sons = "";

                    File_ f = new File_();
                    try {
                        if (str.contains("#")){
                            ctypeids = str.substring(22,str.indexOf("#"));
                            sons = str.substring(str.indexOf("#")+1,str.length()-1).trim();
                            JSONArray jsonArr = new JSONArray(ctypeids);
                            JSONArray jsonArr2 = new JSONArray(sons);
                            //Log.e("mytag","jsonArr:" +jsonArr);
                            //Log.e("mytag","jsonArr2:" +jsonArr2);
                            f.writeTextToFileExternal(ctx,"ctype.txt",ctypeids);
                            f.writeTextToFileExternal(ctx,"sons.txt",sons);
                        }
                    } catch (JSONException e) {
                        LogPrintExStackTrace(e);
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e){
            Toast.makeText(ctx, "שגיאה בwriteCtypeIDandSons", Toast.LENGTH_LONG).show();
        }

        return true;
    }
    public void transferJsonCallTime(final Context ctx){
        Helper h = new Helper();
        if (h.isNetworkAvailable(ctx)){
            String countUnClosed = "";
            countUnClosed = DatabaseHelper.getInstance(ctx).getScalarByCountQuery("SELECT count(*) from Calltime where ctq='-2'");
            int rowCount = 0;
            rowCount = DatabaseHelper.getInstance(ctx).getJsonResultsFromTable("Calltime").length();
            if(rowCount > 0 && countUnClosed=="0"){
                String jsonString = "";
                jsonString = String.valueOf(DatabaseHelper.getInstance(ctx).getJsonResultsFromTable("Calltime"));
                Toast.makeText(ctx, "משדר", Toast.LENGTH_LONG).show();
                try{
                    Model.getInstance().Async_Wz_Call_setTime_Offline_Listener(getMacAddr(), jsonString, new Model.Wz_Call_setTime_Offline_Listener() {
                        @Override
                        public void onResult(String str) {
                           // Toast.makeText(ctx,"response:"+ str, Toast.LENGTH_LONG).show();

                            //Toast.makeText(ctx,"response:"+ str, Toast.LENGTH_LONG).show();
                            if (str.toLowerCase().contains("java.net")){
                                Toast.makeText(ctx, "שידור לא הצליח, נא נסה שנית", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ctx,"נשלח בהצלחה", Toast.LENGTH_LONG).show();
                                DatabaseHelper.getInstance(ctx).delete_call_time();
                            }
                        }
                    });
                }catch (Exception e){
                    h.LogPrintExStackTrace(e);
                }
            }else{
                //Toast.makeText(ctx, "rowCount:" + rowCount + " countUnClosed:"+countUnClosed, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(ctx, "internet invalid, cannot send calltime rows.", Toast.LENGTH_LONG).show();
        }

    }



    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
    public Pair<String, String> getJsonKeyValue (String jsonstr,String ws_name) {

        JSONObject j = null;
        String Status = null;
        String Msg = null;
        try {
            j = new JSONObject(jsonstr);
            //get the array [...] in json
            JSONArray jarray = j.getJSONArray(ws_name);
            Status = jarray.getJSONObject(0).getString("Status");
            Msg = jarray.getJSONObject(0).getString("Msg");



            //myStr=(jarray.getJSONObject(0).getString("Cfname"));//.concat(" ");//1 or 0
            //myStr.concat(jarray.getJSONObject(0).getString("Clname"));//

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return new Pair<>(Status, Msg);
    }

    public String getcurrentDateString(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        String sdt = df.format(new Date(System.currentTimeMillis()));
        return sdt;
    }

    public  final String DEMOURL = "http://main.wizenet.co.il/webservices/freelance.asmx";//default
    //region sync all products with conditions
    public void ALLProductsSync (final Context ctx){
        if (isNetworkAvailable(ctx)) {
            final File_ f = new File_();

            Model.getInstance().Async_Get_mgnet_items_Listener(getMacAddr(), new Model.get_mgnet_items_Listener() {
                @Override
                public void onResult(String str) {

                    DatabaseHelper db;
                    //db = DatabaseHelper.getInstance(ctx);
                    //Boolean flag = true;
                    //flag = f.deleteFileExternal(ctx,"productss.txt");
//                    if (true){
//                        try{
//                            f.writeTextToFileExternal(ctx,"productss.txt", str);
//                            flag = db.delete_from_mgnet_items("all"); //true if success to delete
//                            flag = writeProductsItems();
//                            Toast.makeText(ctx, "נוספו בהצלחה", Toast.LENGTH_LONG).show();
//                        }catch (Exception e){
//                            Log.e("mytag","err del from db:" + e.getMessage());
//                        }
//                    }
                    //else{Log.e("myTag", "deleted file productss");}
                    Log.e("myTag","str:" + str);
                }
            });
        } else {
            //writeTextToSpecificFile("","log.txt",  "network is Not available"+getcurrentDateString().toString());

            Toast.makeText(ctx, "network is Not available", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean writeProductsItems(){
        Log.e("myTag", "arrived here");
        DatabaseHelper db = DatabaseHelper.getInstance(ctx);
        if (db.mgnet_items_isEmpty("all")) {
            try {
                File_ f = new File_();
                String strJson ="";
                strJson += "{Orders:";
                strJson += f.readFromFileExternal(ctx,"productss.txt");
                strJson += "}";

                Log.e("mytag","*strJson*: " + strJson);
                //strJson = strJson.replace("PRODUCTS_ITEMS_LISTResponse", "");
                //strJson = strJson.replace("PRODUCTS_ITEMS_LISTResult=", "Orders:");
                JSONObject j = null;
                JSONArray jarray = null;
                j = null;
                jarray = null;
                try {
                    j = new JSONObject(strJson);
                    jarray = j.getJSONArray("Orders");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }

                for (int i = 0; i < jarray.length(); i++) {
                    final JSONObject e;
                    String name = "";
                    try {
                        e = jarray.getJSONObject(i);
                        db.add_mgnet_items(
                                e.getString("Pname"),
                                e.getString("Pmakat"),
                                e.getString("Pprice"),
                                e.getString("Poprice"),"all");

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        return false;
                    }

                }
                return true;
                //writeTextToSpecificFile("","log.txt","פריטים התווספו בהצלחה" + getcurrentDateString());
                //Toast.makeText(getActivity(), "פריטים התווספו בהצלחה", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                //writeTextToSpecificFile("","log.txt", e.getMessage().toString()+getcurrentDateString().toString());
                //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            //writeTextToSpecificFile("","log.txt",  "הפריטים כבר סונכרנו"+getcurrentDateString().toString());

            Toast.makeText(ctx, "הפריטים כבר סונכרנו", Toast.LENGTH_LONG).show();
            return false;

        }
        return true;
    }
    //endregion


    //    private void stopService_text(Context ctx){
//        Intent intent = new Intent(getApplicationContext(), Alarm_Receiver_Text_File.class);
//        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(sender);
//    }
    public void startService_sync_products(Context ctx){
        try{

        }catch(Exception ex){

        }
        Intent alarm = new Intent(ctx, Alarm_Receiver_sync_client_products.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(ctx, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmRunning == false) {
            Log.e("mytag","Alarm is running.");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
        }
    }

    //**
    //
    // this function call the web service and then write it into text file each cid have been sent.
    //*/
    public void CLIENTProductsSync (final Context ctx,final String cid){

        if (isNetworkAvailable(ctx)) {
            final File_ f = new File_();
            Model.getInstance().Async_Get_mgnet_client_items_Listener(getMacAddr(),cid, new Model.get_mgnet_client_items_Listener() {
                @Override
                public void onResult(String str) {

                    //writeTextToSpecificFile("client_products","pl_" + cid +".txt", str);
                    f.writeTextToFileWithSubDirectory(ctx,"client_products","pl_" + cid +".txt", str);
                    Log.e("myTag", str);
                }
            });
        } else {
            //writeTextToSpecificFile("","log.txt",  "network is Not available"+getcurrentDateString().toString());

            Toast.makeText(ctx, "network is Not available", Toast.LENGTH_SHORT).show();
        }
    }

    public File makeAndGetProfileDirectory(Context c, String profileName) {
        // determine the profile directory
        File profileDirectory = new File(c.getFilesDir(), profileName);
        File file = c.getFileStreamPath("wizenet");
        Log.e("mytag","exist?:"+ String.valueOf(file.exists()));
        // creates the directory if not present yet
        profileDirectory.mkdir();

        return profileDirectory;
    }


    public void wrtieFileOnInternalStorage(Context mcoContext,String sFileName, String sBody){
        File file = new File(mcoContext.getFilesDir(),"mydir");
        if(!file.exists()){
            file.mkdir();
        }

        try{
            File gpxfile = new File(file, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Log.e("mytag"," success!~ ");

        }catch (Exception e){
            Log.e("mytag"," sdfsdfsdfd"+ e.getMessage());
            e.printStackTrace();

        }
    }





    /**
     * //this function get all cid string array from the json file
     * @return
     */
    public List<String> getCIDSlist(){
        List<String> ret = new ArrayList<String>();
        String strJson = "";
        File_ f = new File_();
        strJson = f.readFromFileExternal(ctx,"customers.txt");
        //strJson = readTextFromFileCustomers();
        JSONObject j = null;
        JSONArray jarray = null;
        try {
            j = new JSONObject(strJson);
            jarray= j.getJSONArray("Customers");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < jarray.length(); i++) {
                final JSONObject e;
                String name = null;
                try {
                    e = jarray.getJSONObject(i);
                    name = e.getString("Cusername");
                    //name = e.getString("Ccompany")+'|'+e.getString("CID");

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                ret.add(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ret;
    }
    public String getCusernamelist(){
        String ret = "";
        String strJson = "";
        File_ f = new File_();
        strJson = f.readFromFileExternal(ctx,"customers.txt");
        //strJson = readTextFromFileCustomers();
        JSONObject j = null;
        JSONArray jarray = null;
        try {
            j = new JSONObject(strJson);
            jarray= j.getJSONArray("Wz_Clients_List");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < jarray.length(); i++) {
                final JSONObject e;
                String name = null;
                try {
                    e = jarray.getJSONObject(i);
                    name = e.getString("Cusername");
                    //name = e.getString("Ccompany")+'|'+e.getString("CID");

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                ret += name + ",";
            }
            ret = ret.substring(0, ret.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ret;
    }

    public List<Order> getClientOrderList(String strInputCID,Context c){
        File_ f = new File_();
        List<Order> responseList = new ArrayList<>();
        String strJson = "";
        //strJson = helper.readTextFromFile3("client_products/pl_"+ strInputCID +".txt");
        strJson += "{Orders:";
        strJson +=  f.readFromFileWithSubDirectory(c,"client_products","pl_"+ strInputCID +".txt");
        strJson +="}";

        //strJson=strJson.replace("PRODUCTS_ITEMS_LISTResponse","");
        //strJson=strJson.replace("PRODUCTS_ITEMS_LISTResult=","Orders:");
        JSONObject j = null;
        JSONArray jarray = null;
        j = null;
        jarray = null;
        try {
            j = new JSONObject(strJson);
            jarray= j.getJSONArray("Orders");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MYTAG",e.getMessage());
        }
        for (int i = 0; i < jarray.length(); i++) {
            final JSONObject e;
            //String name = "";
            try {
                e = jarray.getJSONObject(i);
                //name = e.getString("Pname")+'|'+e.getString("Pmakat");
                Order order = new Order(
                        e.getString("Pname"),
                        e.getString("Pmakat"),
                        e.getString("Pprice"),
                        e.getString("POprice"));
                responseList.add(order);

            } catch (JSONException e1) {
                e1.printStackTrace();
                Log.e("MYTAG",e1.getMessage());
            }

        }
        return responseList;
    }

    /**
     * in wizenet app we create order, the order are have send to the web service,
     * the two functions below loop over the order file (json building) and send each of them to webservice
     * if web service say that he got successfully , remove the file, it's not necessary anymore.
     * @param ctx
     */
    //region SendOrderToWizenet
    public void SendOrderToWizenet(Context ctx) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        String sdt = df.format(new Date(System.currentTimeMillis()));
        Log.e("MYTAG","chk: "+sdt);
        String isNetwork = "";
        if (isNetworkAvailable(ctx)){
            //String path = Environment.getExternalStorageDirectory().getPath()+"/wizenet/offline/";
            File myDirectory = null;//new File(path);
            File_ f = new File_();
            myDirectory = f.retSubDir(ctx,"offline");
            traverse(myDirectory,ctx);
        }else{
            isNetwork = " nop Network";
        }
        //writeTextToFileORDER(sdt + isNetwork);
    }

    /**
     * loop over directory files and create order for each one of them, and then delete the file if success.
     * @param dir
     * @param ctx
     */
    public void traverse (File dir, final Context ctx) {
        File_ f = new File_();
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; ++i) {

                File file = files[i];
                if (file.isDirectory()) {
                    //traverse(file);
                } else {
                    String fileContect = "";
                    //fileContect = readTextFromFile(file);
                    fileContect = f.readFromCurrentFileExternal(ctx,file);
                    Model.getInstance().Async_CREATE_OFFLINE_Listener(getMacAddr().toString(), fileContect, new Model.CREATE_OFFLINE_Listener() {
                        @Override
                        public void onResult(String str) {
                            Toast.makeText(ctx, str + " Order has been created", Toast.LENGTH_LONG).show();
                            Log.e("MYTAG",str + " Order has been created");
                        }
                    });
                    // do something here with the file
                }
            }
        }
    }
    //endregion




//    public boolean writeTextToClientDirectory(String subDirectory,String fileandsuffix,String input){
//
//        // get the path to sdcard
//        String myInput;
//        if(input.length()>0){
//            myInput = input;
//        }else{
//            myInput = null;//DEMOURL;
//        }
//        //create subdirectory if needed
//        if (!subDirectory.equals("")){ //check if exist
//            File mySubDirectory = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/"+subDirectory);
//            if(!mySubDirectory.exists()) {
//                mySubDirectory.mkdir();
//            }
//        }
//        String mySub = subDirectory+"/";
//        try {
//            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/"+mySub+fileandsuffix);
//            myFile.createNewFile();
//            FileOutputStream fOut = new FileOutputStream(myFile);
//            OutputStreamWriter myOutWriter =
//                    new OutputStreamWriter(fOut);
//            myOutWriter.write(myInput);
//            myOutWriter.close();
//            fOut.close();
//            //Toast.makeText(getApplicationContext(), "File_ Created", Toast.LENGTH_LONG).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return true;
//    }
//    public boolean deleteFile(String file){
//        boolean flag = false;
//        try{
//            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/" + file);
//            if(myFile.exists()){
//                myFile.delete();
//                flag = true;
//                Log.e("MYTAG","file is deleted");
//            }
//
//
//
//        }catch(Exception e){
//
//        }
//        return flag;
//    }
//    public boolean writeTextToSpecificFile(String subDirectory,String fileandsuffix,String input){
//
//        // get the path to sdcard
//        String myInput;
//        if(input.length()>0){
//            myInput = input;
//        }else{
//            myInput = null;//DEMOURL;
//        }
//        //create subdirectory if needed
//        if (!subDirectory.equals("")){ //check if exist
//            File mySubDirectory = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/"+subDirectory);
//            if(!mySubDirectory.exists()) {
//                mySubDirectory.mkdir();
//            }
//        }
//        String mySub = subDirectory+"/";
//        try {
//            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/"+mySub+fileandsuffix);
//            Log.e("mytag",Environment.getExternalStorageDirectory().getPath()+"/wizenet/"+mySub+fileandsuffix);
//            myFile.createNewFile();
//            FileOutputStream fOut = new FileOutputStream(myFile);
//            OutputStreamWriter myOutWriter =
//                    new OutputStreamWriter(fOut);
//            myOutWriter.write(myInput);
//            myOutWriter.close();
//            fOut.close();
//            Log.e("MYTAG","success to write file");
//            //Toast.makeText(getApplicationContext(), "File_ Created", Toast.LENGTH_LONG).show();
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            Log.e("MYTAG",e.getMessage());
//            return false;
//        }
//        return true;
//    }

    public boolean writeTextToFile2(String myurlParameter){

        // get the path to sdcard
        String myUrl;
        if(myurlParameter.length()>0){
            myUrl = myurlParameter;
        }else{
            myUrl = null;//DEMOURL;
        }

        try {
            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/customers.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.write(myUrl);
            myOutWriter.close();
            fOut.close();
            //Toast.makeText(getApplicationContext(), "File_ Created", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public void deleteProductsFiles () {
        try{
            File dir = new File(Environment.getExternalStorageDirectory()+"/wizenet/client_products");
            if (dir.isDirectory())
            {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++)
                {
                    new File(dir, children[i]).delete();
                }
            }
        }catch(Exception ex){
            Log.e("MYTAG","failed to delete");
        }
    }
    //###################################
    //READ URL FROM FILE
    //###################################


    /**
     * delete files from products directory
     */
    public void deleteAllFiles() {
        File_ f = new File_();
        try{

            File dir = f.retSubDir(ctx,"client_products");//= new File(Environment.getExternalStorageDirectory()+"/wizenet/client_products");
            if (dir.isDirectory())
            {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++)
                {
                    new File(dir, children[i]).delete();
                }
            }
        }catch(Exception ex){
            Log.e("MYTAG","failed to delete");
        }
        try{
            //File myFile =
            f.deleteFileExternal(ctx,"productss.txt");//new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/productss.txt");
            //if(myFile.exists())
            //myFile.delete();
        }catch(Exception e){

        }
        try{
            f.deleteFileExternal(ctx,"customers.txt");
            //File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/customers.txt");
            //if(myFile.exists())
            //    myFile.delete();
        }catch(Exception e){

        }
    }



    //Android 6.0 : Access to mac address from WifiManager forbidden
    private static final String marshmallowMacAddress = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";


//region get mac address functions




    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static String getMacAddr() {
        try{
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = tm.getDeviceId();
            return device_id;
        }catch(Exception e){
            Helper h = new Helper();
            h.LogPrintExStackTrace(e);
            return "";
        }

//        try {
//            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface nif : all) {
//                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
//
//                byte[] macBytes = nif.getHardwareAddress();
//                if (macBytes == null) {
//                    return "";
//                }
//
//                StringBuilder res1 = new StringBuilder();
//                for (byte b : macBytes) {
//                    res1.append(String.format("%02X:",b));
//                }
//
//                if (res1.length() > 0) {
//                    res1.deleteCharAt(res1.length() - 1);
//                }
//                return res1.toString();
//            }
//        } catch (Exception ex) {
//        }
//        return "02:00:00:00:00:00";
    }


//endregion




    public void goToOfflineMenuFragment(Context context){
        android.support.v4.app.FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentMenuOffline frag = new FragmentMenuOffline();
        ft.replace(R.id.container,frag,"FragmentMenuOffline");
        //tv.setVisibility(TextView.GONE);
        ft.addToBackStack("FragmentMenuOffline");
        ft.commit();
    }
    public boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    //###################################
    //EXTRACT CUSTOMERS FROM JSON
    //###################################

    /**
     * iterate over json file (text file) and extract the json to an objects
     * @param json
     * @return
     */
    //region get customers objects from json file
    public Ccustomer[] getCustomersFromJson2(String json){
        Ccustomer[] customersList = new Ccustomer[0];
        JSONObject j = null;
        try {
            j = new JSONObject(json);
            //get the array [...] in json
            JSONArray jarray = j.getJSONArray("Wz_Clients_List");
            customersList = new Ccustomer[jarray.length()];
            //customersList = new Ccustomer[jarray.length()];

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);
                Ccustomer c = new Ccustomer(
                        jarray.getJSONObject(i).getString("CstatusName"),
                        jarray.getJSONObject(i).getString("Cage"),
                        jarray.getJSONObject(i).getString("CID"),
                        jarray.getJSONObject(i).getString("CParentID"),
                        jarray.getJSONObject(i).getString("TargetWeight"),
                        jarray.getJSONObject(i).getString("Cusername"),
                        jarray.getJSONObject(i).getString("Cpassword"),
                        jarray.getJSONObject(i).getString("Cfname"),
                        jarray.getJSONObject(i).getString("Clname"),
                        jarray.getJSONObject(i).getString("Cemail"),
                        jarray.getJSONObject(i).getString("Caddress"),
                        jarray.getJSONObject(i).getString("Ccity"),
                        jarray.getJSONObject(i).getString("CArea"),
                        jarray.getJSONObject(i).getString("Ccountry"),
                        jarray.getJSONObject(i).getString("Cstate"),
                        jarray.getJSONObject(i).getString("Czip"),
                        jarray.getJSONObject(i).getString("Cphone"),
                        jarray.getJSONObject(i).getString("Ccell"),
                        jarray.getJSONObject(i).getString("Cfax"),
                        jarray.getJSONObject(i).getString("Cdate"),
                        jarray.getJSONObject(i).getString("CBirthDate"),
                        jarray.getJSONObject(i).getString("CStartDate"),
                        jarray.getJSONObject(i).getString("CEndDate"),
                        jarray.getJSONObject(i).getString("CFemilyStatus"),
                        jarray.getJSONObject(i).getString("CBuySum"),
                        jarray.getJSONObject(i).getString("Ccompany"),
                        jarray.getJSONObject(i).getString("Cprofession"),
                        jarray.getJSONObject(i).getString("LNG"),
                        jarray.getJSONObject(i).getString("CTypeID"),
                        jarray.getJSONObject(i).getString("CCodeID"),
                        jarray.getJSONObject(i).getString("CConfirm"),
                        jarray.getJSONObject(i).getString("CInterest"),
                        jarray.getJSONObject(i).getString("Cduty"),
                        jarray.getJSONObject(i).getString("CPoints"),
                        jarray.getJSONObject(i).getString("SendEmail"),
                        jarray.getJSONObject(i).getString("ccName"),
                        jarray.getJSONObject(i).getString("ccID"),
                        jarray.getJSONObject(i).getString("ccType"),
                        jarray.getJSONObject(i).getString("ccNo"),
                        jarray.getJSONObject(i).getString("ccExp"),
                        jarray.getJSONObject(i).getString("ccpayment"),
                        jarray.getJSONObject(i).getString("CStatus"),
                        jarray.getJSONObject(i).getString("CIMG"),
                        jarray.getJSONObject(i).getString("CSex"),
                        jarray.getJSONObject(i).getString("Ccomments"),
                        jarray.getJSONObject(i).getString("CJoinerID"),
                        jarray.getJSONObject(i).getString("Cweb"),
                        jarray.getJSONObject(i).getString("CstatusID"),
                        jarray.getJSONObject(i).getString("CstatusDate"),
                        jarray.getJSONObject(i).getString("CstatusID2"),
                        jarray.getJSONObject(i).getString("CstatusDate2"),
                        jarray.getJSONObject(i).getString("IsActive"),
                        jarray.getJSONObject(i).getString("Herb_ID"),
                        jarray.getJSONObject(i).getString("Age"),
                        jarray.getJSONObject(i).getString("Weight"),
                        jarray.getJSONObject(i).getString("LooseWeight"),
                        jarray.getJSONObject(i).getString("Height"),
                        jarray.getJSONObject(i).getString("AgentID"),
                        jarray.getJSONObject(i).getString("LastLogin"),
                        jarray.getJSONObject(i).getString("SlpCode"),
                        jarray.getJSONObject(i).getString("LastPassword"),
                        jarray.getJSONObject(i).getString("CTypeName"));

//                //TODO add image to the constructor
//                Ccustomer c = new Ccustomer(fname,lname,email,phone,cell,ccompany, address, "",cid);
                customersList[i] = c;
            }
        } catch (JSONException e1) {
            LogPrintExStackTrace(e1);
            e1.printStackTrace();
            return new Ccustomer[0];
        }
        return customersList;
    }
    public Ccustomer[] getWizenetClientsFromJson(String json){
        Ccustomer[] customersList = new Ccustomer[0];
        JSONObject j = null;
        try {
            j = new JSONObject(json);
            //get the array [...] in json
            JSONArray jarray = j.getJSONArray("Wz_ret_ClientsAddressesByActions");
            customersList = new Ccustomer[jarray.length()];
            //customersList = new Ccustomer[jarray.length()];

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);
                Ccustomer c = new Ccustomer(
                        jarray.getJSONObject(i).getString("CstatusName"),
                        jarray.getJSONObject(i).getString("Cage"),
                        jarray.getJSONObject(i).getString("CID"),
                        jarray.getJSONObject(i).getString("CParentID"),
                        jarray.getJSONObject(i).getString("TargetWeight"),
                        jarray.getJSONObject(i).getString("Cusername"),
                        jarray.getJSONObject(i).getString("Cpassword"),
                        jarray.getJSONObject(i).getString("Cfname"),
                        jarray.getJSONObject(i).getString("Clname"),
                        jarray.getJSONObject(i).getString("Cemail"),
                        jarray.getJSONObject(i).getString("Caddress"),
                        jarray.getJSONObject(i).getString("Ccity"),
                        jarray.getJSONObject(i).getString("CArea"),
                        jarray.getJSONObject(i).getString("Ccountry"),
                        jarray.getJSONObject(i).getString("Cstate"),
                        jarray.getJSONObject(i).getString("Czip"),
                        jarray.getJSONObject(i).getString("Cphone"),
                        jarray.getJSONObject(i).getString("Ccell"),
                        jarray.getJSONObject(i).getString("Cfax"),
                        jarray.getJSONObject(i).getString("Cdate"),
                        jarray.getJSONObject(i).getString("CBirthDate"),
                        jarray.getJSONObject(i).getString("CStartDate"),
                        jarray.getJSONObject(i).getString("CEndDate"),
                        jarray.getJSONObject(i).getString("CFemilyStatus"),
                        jarray.getJSONObject(i).getString("CBuySum"),
                        jarray.getJSONObject(i).getString("Ccompany"),
                        jarray.getJSONObject(i).getString("Cprofession"),
                        jarray.getJSONObject(i).getString("LNG"),
                        jarray.getJSONObject(i).getString("CTypeID"),
                        jarray.getJSONObject(i).getString("CCodeID"),
                        jarray.getJSONObject(i).getString("CConfirm"),
                        jarray.getJSONObject(i).getString("CInterest"),
                        jarray.getJSONObject(i).getString("Cduty"),
                        jarray.getJSONObject(i).getString("CPoints"),
                        jarray.getJSONObject(i).getString("SendEmail"),
                        jarray.getJSONObject(i).getString("ccName"),
                        jarray.getJSONObject(i).getString("ccID"),
                        jarray.getJSONObject(i).getString("ccType"),
                        jarray.getJSONObject(i).getString("ccNo"),
                        jarray.getJSONObject(i).getString("ccExp"),
                        jarray.getJSONObject(i).getString("ccpayment"),
                        jarray.getJSONObject(i).getString("CStatus"),
                        jarray.getJSONObject(i).getString("CIMG"),
                        jarray.getJSONObject(i).getString("CSex"),
                        jarray.getJSONObject(i).getString("Ccomments"),
                        jarray.getJSONObject(i).getString("CJoinerID"),
                        jarray.getJSONObject(i).getString("Cweb"),
                        jarray.getJSONObject(i).getString("CstatusID"),
                        jarray.getJSONObject(i).getString("CstatusDate"),
                        jarray.getJSONObject(i).getString("CstatusID2"),
                        jarray.getJSONObject(i).getString("CstatusDate2"),
                        jarray.getJSONObject(i).getString("IsActive"),
                        jarray.getJSONObject(i).getString("Herb_ID"),
                        jarray.getJSONObject(i).getString("Age"),
                        jarray.getJSONObject(i).getString("Weight"),
                        jarray.getJSONObject(i).getString("LooseWeight"),
                        jarray.getJSONObject(i).getString("Height"),
                        jarray.getJSONObject(i).getString("AgentID"),
                        jarray.getJSONObject(i).getString("LastLogin"),
                        jarray.getJSONObject(i).getString("SlpCode"),
                        jarray.getJSONObject(i).getString("LastPassword"),
                        jarray.getJSONObject(i).getString("CTypeName"));

//                //TODO add image to the constructor
//                Ccustomer c = new Ccustomer(fname,lname,email,phone,cell,ccompany, address, "",cid);
                customersList[i] = c;
            }
        } catch (JSONException e1) {
            LogPrintExStackTrace(e1);
            e1.printStackTrace();
        }
        return customersList;
    }

    //endregion

    public void goToLoginReportFragment(Context context){

        android.support.v4.app.FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentLoginReport frag = new FragmentLoginReport();
        ft.replace(R.id.container,frag,"FragmentLoginReport");
        //tv.setVisibility(TextView.GONE);
        ft.addToBackStack("FragmentLoginReport");
        ft.commit();
    }
    public void goToFragmentSecret(Context context){

        android.support.v4.app.FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentSecret frag = new FragmentSecret();
        ft.replace(R.id.container,frag,"FragmentSecret");
        //tv.setVisibility(TextView.GONE);
        ft.addToBackStack("FragmentSecret");
        ft.commit();
    }
    public void goTocustomers(Context context){

        android.support.v4.app.FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentCustomer frag = new FragmentCustomer();
        ft.replace(R.id.container,frag,"FragmentCustomer");
        ft.addToBackStack("FragmentCustomer");
        ft.commit();

    }
    public void goToCPFragment(Context context){

        //mydb.getInstance(getApplicationContext());
        ControlPanelFragment controlPanelFragment = new ControlPanelFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,controlPanelFragment,"CPFragment");
        fragmentTransaction.addToBackStack("CPFragment");
        //tv.setVisibility(TextView.GONE);
        fragmentTransaction.commit();

    }

    public void goToMenuFragment(Context context){
        android.support.v4.app.FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentMenu frag = new FragmentMenu();
        ft.replace(R.id.container,frag,"FragmentMenu");
        //tv.setVisibility(TextView.GONE);
        ft.addToBackStack("FragmentMenu");
        ft.commit();
    }

    public void goToToolsFragment(Context context){
        android.support.v4.app.FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentTools frag = new FragmentTools();
        ft.replace(R.id.container,frag,"FragmentTools");
        //tv.setVisibility(TextView.GONE);
        ft.addToBackStack("FragmentTools");
        ft.commit();
    }
//    public void goToCallsFragment(Context context){
//        android.support.v4.app.FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
//        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
//        FragmentCalls frag = new FragmentCalls();
//        ft.replace(R.id.container,frag,"FragmentCalls");
//        //tv.setVisibility(TextView.GONE);
//        ft.addToBackStack("FragmentCalls");
//        ft.commit();
//    }
//    public void goToCallDetailsFrag(Context context,String puId)
//    {
//        android.support.v4.app.FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
//        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
//        Bundle bundle = new Bundle();
//        FragmentCallDetails frag = new FragmentCallDetails();
//        //bundle.putString("receiver", dataName);
//        bundle.putString("puId",puId);
//        frag.setArguments(bundle);
//        ft.replace(R.id.container,frag,"FragmentCallDetails");
//        ft.addToBackStack("FragmentCallDetails");
//        ft.commit();
//    }
//    public void goToCallDetailsFragNew(Context context,String puId)
//    {
//        android.support.v4.app.FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
//        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
//        Bundle bundle = new Bundle();
//        FragmentCallDetails frag = new FragmentCallDetails();
//        //bundle.putString("receiver", dataName);
//        bundle.putString("puId",puId);
//        frag.setArguments(bundle);
//        ft.replace(R.id.container,frag,"FragmentCallDetails");
//        ft.addToBackStack("FragmentCallDetails");
//        ft.commit();
//    }

    public void goToOrdersFragment(Context context){
        android.support.v4.app.FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentOrders frag = new FragmentOrders();
        ft.replace(R.id.container,frag,"FragmentOrders");
        //tv.setVisibility(TextView.GONE);
        ft.addToBackStack("FragmentOrders");
        ft.commit();
    }
    public void goToMessagesFragment(Context context){
        android.support.v4.app.FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentMessage frag = new FragmentMessage();
        ft.replace(R.id.container,frag,"FragmentMessage");
        //tv.setVisibility(TextView.GONE);
        ft.addToBackStack("FragmentMessage");
        ft.commit();
    }
    public void LogPrintExStackTrace(Exception e){
        Log.e("mytag",e.getMessage());
        Log.e("mytag",toString(e.getStackTrace()));
    }
    public  String toString(StackTraceElement[] stackTraceElements) {
        if (stackTraceElements == null)
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement element : stackTraceElements)
            stringBuilder.append(element.toString()).append("\n");
        return stringBuilder.toString();
    }


}
