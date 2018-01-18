package com;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


import static com.Activities.MainActivity.ctx;
import static java.security.AccessController.getContext;

/**
 * Created by User on 31/07/2016.
 */
public class Helper {
    public Helper() {
    }

    public void addInitialfirst(Context ctx){
        DatabaseHelper.getInstance(ctx).addControlPanel("username","");
        DatabaseHelper.getInstance(ctx).addControlPanel("BACKGROUND","1");
        DatabaseHelper.getInstance(ctx).addControlPanel("GPS","0");
        DatabaseHelper.getInstance(ctx).addControlPanel("URL","");// url.getText().toString());
        DatabaseHelper.getInstance(ctx).addControlPanel("CLIENT_SYNC_PRODUCTS","0");


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
                    db = DatabaseHelper.getInstance(ctx);
                    Boolean flag = true;
                    flag = f.deleteFile(ctx,"productss.txt");
                    if (flag == true){
                        f.writeTextToFileInternal(ctx,"productss.txt", str);
                        flag = db.delete_from_mgnet_items("all"); //true if success to delete
                        if (flag == true){
                            flag = writeProductsItems();
                            if (flag==true){
                                Toast.makeText(ctx, "נוספו בהצלחה", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    Log.e("myTag", str);
                }
            });
        } else {
            //writeTextToSpecificFile("","log.txt",  "network is Not available"+getcurrentDateString().toString());

             Toast.makeText(ctx, "network is Not available", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean writeProductsItems(){
        DatabaseHelper db = DatabaseHelper.getInstance(ctx);
        if (db.mgnet_items_isEmpty("all")) {
            try {
                String strJson = readTextFromFile3("productss.txt");
                strJson = strJson.replace("PRODUCTS_ITEMS_LISTResponse", "");
                strJson = strJson.replace("PRODUCTS_ITEMS_LISTResult=", "Orders:");
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
        strJson = readTextFromFileCustomers();
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
        writeTextToFileORDER(sdt + isNetwork);
    }

    /**
     * loop over directory files and create order for each one of them, and then delete the file if success.
     * @param dir
     * @param ctx
     */
    public void traverse (File dir, final Context ctx) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; ++i) {
                File file = files[i];
                if (file.isDirectory()) {
                    //traverse(file);
                } else {
                    String fileContect = "";
                    fileContect = readTextFromFile(file);
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
    public String readTextFromFile() {
        String ret = "";

        try {
            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/myurl.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow;
            }
            ret = aBuffer.toString().trim();
            myReader.close();
            //Toast.makeText(getApplicationContext(), ret, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * delete files from products directory
     */
    public void deleteAllFiles() {
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
        try{
            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/productss.txt");
            if(myFile.exists())
                myFile.delete();
        }catch(Exception e){

        }
        try{
            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/customers.txt");
            if(myFile.exists())
                myFile.delete();
        }catch(Exception e){

        }
    }

    /**
     * get json text from file/ read the file
     * @return
     */
    public String readTextFromFileCustomers() {
        String ret = "";

        try {
            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/customers.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow;
            }
            ret = aBuffer.toString().trim();
            myReader.close();
            //Toast.makeText(getApplicationContext(), ret, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
    public String readTextFromFile3(String preffixNsuffix) {
        String ret = "";

        try {
            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/" +preffixNsuffix);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow;
            }
            ret = aBuffer.toString().trim();
            myReader.close();
            //Toast.makeText(getApplicationContext(), ret, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
    public void writeTextToFileORDER(String param){

        FileWriter f;
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/test_file_service.txt");
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
    public String readTextFromFile(File file) {
        String ret = "";

        try {
            File myFile = file;
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow;
            }
            ret = aBuffer.toString().trim();
            myReader.close();
            //Toast.makeText(getApplicationContext(), ret, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
    //Android 6.0 : Access to mac address from WifiManager forbidden
    private static final String marshmallowMacAddress = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";

    public static String recupAdresseMAC(WifiManager wifiMan) {
        WifiInfo wifiInf = wifiMan.getConnectionInfo();

        if(wifiInf.getMacAddress().equals(marshmallowMacAddress)){
            String ret = null;
            try {
                ret= getAdressMacByInterface();
                if (ret != null){
                    return ret;
                } else {
                    ret = getAddressMacByFile(wifiMan);
                    return ret;
                }
            } catch (IOException e) {
                Log.e("MobileAccess", "Erreur lecture propriete Adresse MAC");
            } catch (Exception e) {
                Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
            }
        } else{
            return wifiInf.getMacAddress();
        }
        return marshmallowMacAddress;
    }
//region get mac address functions

    public static String getAdressMacByInterface(){
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:",b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }

        } catch (Exception e) {
            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
        }
        return null;
    }

    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        String ret;
        int wifiState = wifiMan.getWifiState();

        wifiMan.setWifiEnabled(true);
        File fl = new File(fileAddressMac);
        FileInputStream fin = new FileInputStream(fl);
        ret = convertStreamToString(fin);
        fin.close();

        boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
        wifiMan.setWifiEnabled(enabled);
        return ret;
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


//endregion



    public void goToCPFragment(Context context){

        //mydb.getInstance(getApplicationContext());
        ControlPanelFragment controlPanelFragment = new ControlPanelFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,controlPanelFragment,"CPFragment");
        fragmentTransaction.addToBackStack("CPFragment");
        //tv.setVisibility(TextView.GONE);
        fragmentTransaction.commit();

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
    public void goToCallsFragment(Context context){
        android.support.v4.app.FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentCalls frag = new FragmentCalls();
        ft.replace(R.id.container,frag,"FragmentCalls");
        //tv.setVisibility(TextView.GONE);
        ft.addToBackStack("FragmentCalls");
        ft.commit();
    }
    public void goToCallDetailsFrag(Context context,String puId)
    {
        android.support.v4.app.FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        Bundle bundle = new Bundle();
        FragmentCallDetails frag = new FragmentCallDetails();
        //bundle.putString("receiver", dataName);
        bundle.putString("puId",puId);
        frag.setArguments(bundle);
        ft.replace(R.id.container,frag,"FragmentCallDetails");
        ft.addToBackStack("FragmentCallDetails");
        ft.commit();
    }
    public void goToCallDetailsFragNew(Context context,String puId)
    {
        android.support.v4.app.FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        Bundle bundle = new Bundle();
        FragmentCallDetails frag = new FragmentCallDetails();
        //bundle.putString("receiver", dataName);
        bundle.putString("puId",puId);
        frag.setArguments(bundle);
        ft.replace(R.id.container,frag,"FragmentCallDetails");
        ft.addToBackStack("FragmentCallDetails");
        ft.commit();
    }
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

    public Ccustomer[] getCustomersFromJson(String json){
        Ccustomer[] customersList = new Ccustomer[0];
        JSONObject j = null;
        try {
            j = new JSONObject(json);
            //get the array [...] in json
            JSONArray jarray = j.getJSONArray("Customers");
            customersList = new Ccustomer[jarray.length()];
            //customersList = new Ccustomer[jarray.length()];

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);
                String fname = jarray.getJSONObject(i).getString("Cfname");
                String lname = jarray.getJSONObject(i).getString("Clname");
                String email = jarray.getJSONObject(i).getString("Cemail");
                String phone = jarray.getJSONObject(i).getString("Cphone");
                String cell = jarray.getJSONObject(i).getString("Ccell");
                String ccompany = jarray.getJSONObject(i).getString("Ccompany");
                Ccustomer c = new Ccustomer(fname,lname,email,phone,cell,ccompany);
                customersList[i] = c;
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return customersList;
    }
    public Ccustomer[] getCustomersFromJson2(String json){
        Ccustomer[] customersList = new Ccustomer[0];
        JSONObject j = null;
        try {
            j = new JSONObject(json);
            //get the array [...] in json
            JSONArray jarray = j.getJSONArray("Customers");
            customersList = new Ccustomer[jarray.length()];
            //customersList = new Ccustomer[jarray.length()];

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);
                String fname = jarray.getJSONObject(i).getString("Cfname");
                String lname = jarray.getJSONObject(i).getString("Clname");
                String email = jarray.getJSONObject(i).getString("Cemail");
                String phone = jarray.getJSONObject(i).getString("Cphone");
                String cell = jarray.getJSONObject(i).getString("Ccell");
                String ccompany = jarray.getJSONObject(i).getString("Ccompany");
                String cid = jarray.getJSONObject(i).getString("CID");
                Ccustomer c = new Ccustomer(fname,lname,email,phone,cell,ccompany,cid);
                customersList[i] = c;
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return customersList;
    }
    public Ccustomer[] getCustomersFromJson3(String json){
        Ccustomer[] customersList = new Ccustomer[0];
        JSONObject j = null;
        try {
            j = new JSONObject(json);
            //get the array [...] in json
            JSONArray jarray = j.getJSONArray("Customers");
            customersList = new Ccustomer[jarray.length()];
            //customersList = new Ccustomer[jarray.length()];

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);
                String fname = jarray.getJSONObject(i).getString("Cfname");
                String lname = jarray.getJSONObject(i).getString("Clname");
                String email = jarray.getJSONObject(i).getString("Cemail");
                String phone = jarray.getJSONObject(i).getString("Cphone");
                String cell = jarray.getJSONObject(i).getString("Ccell");
                String ccompany = jarray.getJSONObject(i).getString("Ccompany");
                String cid = jarray.getJSONObject(i).getString("CID");
                Ccustomer c = new Ccustomer(fname,lname,email,phone,cell,ccompany,cid);
                customersList[i] = c;
            }
        } catch (JSONException e1) {
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
    public void goTocustomers(Context context){
        android.support.v4.app.FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentCustomer frag = new FragmentCustomer();
        ft.replace(R.id.container,frag,"FragmentCustomer");
        //tv.setVisibility(TextView.GONE);
        ft.addToBackStack("FragmentCustomer");
        ft.commit();
    }

}
