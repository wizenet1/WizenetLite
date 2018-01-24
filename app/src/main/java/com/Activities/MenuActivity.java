package com.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.Alarm_Receiver_Text_File;
import com.AlertBadgeEnum;
import com.DatabaseHelper;
import com.Alarm_Receiver;
import com.Fragments.FragmentCustomer;
import com.Fragments.FragmentMenu;
import com.GPSTracker;
import com.Helper;
import com.model.Model;
import com.nex3z.notificationbadge.NotificationBadge;
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuActivity extends FragmentActivity implements LocationListener {
    EditText myEditText5;
    Handler mHandler = new Handler();
    TextView tv;
    //public static boolean isTrack = false;
    GPSTracker gps;
    String s_longtitude = "";
    String s_latitude = "";

    // Alert badge. At the moment is static but in future will be dynamic.
    Map<AlertBadgeEnum, NotificationBadge> alertBadgeDictionary = new HashMap<AlertBadgeEnum, NotificationBadge>();
    int homepageCount = 1;

    private Context context;
    LocationManager manager = null;
    //ImageButton btn1, settingsBtn, btnMikum, control_db_id;
    DatabaseHelper db;
    Helper helper = new Helper();
    String strBundle = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("mytag","arrived to here");

        setContentView(R.layout.nav_bar);

        // Initialize all the badges.
        this.initializeBadgeDictionary();

        // Set the alert of homepage to 1.
        this.alertBadgeDictionary.get(AlertBadgeEnum.badge_homepage).setNumber(this.homepageCount);
        getCallStatuses();

        //setHasOptionsMenu(false);

        db = DatabaseHelper.getInstance(getApplicationContext());
        manager = (LocationManager)getSystemService(getApplicationContext().LOCATION_SERVICE);
        initilize();
        Model.getInstance().Wz_Call_Statuses_Listener(helper.getMacAddr(), new Model.Wz_Call_Statuses_Listener() {
            @Override
            public void onResult(String str) {

            }
        });

        goToMenuFragment();

    }

    /**
     * Initializes all the badges in the action bar and main menu.
     */
    private void initializeBadgeDictionary() {

        // Set all the badges objects.
        for(AlertBadgeEnum alert : AlertBadgeEnum.values()) {

            View currentObject;
            switch (alert){
                case badge_homepage:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge)findViewById(R.id.badge_homepage));
                    break;
                case badge_clients:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge)findViewById(R.id.badge_clients));
                    break;
                case badge_messages:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge)findViewById(R.id.badge_messages));
                    break;
                case badge_arrows:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge)findViewById(R.id.badge_arrows));
                    break;
                case badge_help:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge)findViewById(R.id.badge_help));
                    break;

            }

        }
    }

    private void initilize(){
        try{


            if (db.getValueByKey("BACKGROUND").equals("1")) {
                Intent alarm = new Intent(getApplicationContext(), Alarm_Receiver.class);
                boolean alarmRunning = (PendingIntent.getBroadcast(this.context, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
                if (alarmRunning == false) {
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    // TODO: 05/09/2016  just note the time is every 5 minutes
                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 240000, pendingIntent);
                }
            }
            //################################
            // FILE_TEXT
            //################################
            if (db.getValueByKey("BACKGROUND").equals("1")) {
                startService_text();
                Log.e("MYTAG","thread started");
            }else{
                stopService_text();
            }


            if(manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) && (db.getValueByKey("GPS").equals("1"))){
                startRepeatingTask();
            }else if((!manager.isProviderEnabled( LocationManager.GPS_PROVIDER )) && (db.getValueByKey("GPS").equals("1"))) {
                stopRepeatingTask();
                Toast.makeText(getApplicationContext(), "gps not available", Toast.LENGTH_LONG).show();
                db.updateValue("GPS", "0");
            }
        }catch (Exception ex){
            ex.printStackTrace();
            Log.e("mytag",ex.getMessage());
        }
    }
    private void getCallStatuses(){
        Model.getInstance().Wz_Call_Statuses_Listener(helper.getMacAddr(), new Model.Wz_Call_Statuses_Listener() {
            @Override
            public void onResult(String str) {

            }
        });
    }
    public void myFunc(){
        // CLEAR BACK STACK.
        final FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.getBackStackEntryCount() ==1) {
            fragmentManager.popBackStackImmediate();

        }
        //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        //((YourActivityClassName)getActivity()).yourPublicMethod();
    }
    private void stopService_text(){
        Intent intent = new Intent(getApplicationContext(), Alarm_Receiver_Text_File.class);
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    private void startService_text(){
        Intent alarm = new Intent(getApplicationContext(), Alarm_Receiver_Text_File.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmRunning == false) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            // TODO: 05/09/2016  just note the time is every 5 minutes
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),240000, pendingIntent);
        }
    }



    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {

            gps = null;
            gps = new GPSTracker(getApplicationContext());
            gps.getLocation();
            s_longtitude = Double.toString(gps.getLongitude());
            s_latitude = Double.toString(gps.getLatitude());
            //new AsynchCallSoap().execute();
            Model.getInstance().AsyncStatus(helper.getMacAddr(), s_longtitude, s_latitude, new Model.StatusListener() {
                @Override
                public void onResult(String str) {
                    //Log.e("myTag","return from asyncStatus: " +str);
                    if (str.equals("1")) {
                        Log.e("myTag","AsyncStatus: "+ s_latitude + ":" + s_longtitude);
                        writeTextToFileGPS(s_latitude + ":" + s_longtitude);
                        //Toast.makeText(getApplicationContext(), s_latitude + ":" + s_longtitude, Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("myTag", ":");
                        Toast.makeText(getApplicationContext(), "no long or lat", Toast.LENGTH_LONG).show();
                    }
                }
            });
            mHandler.postDelayed(mHandlerTask, 60000);
        }
    };

    public void goToMenuFragment(){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentMenu frag = new FragmentMenu();
        ft.replace(R.id.container,frag,"FragmentMenu");

        ft.addToBackStack("FragmentMenu");
        ft.commit();
    }



    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.container);
        Log.e("Mytag", String.valueOf(fm.getBackStackEntryCount()));
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment topFragment = fragmentManager.findFragmentById(R.id.container);

        if(fm.getBackStackEntryCount() == 2){
            //if(!topFragment.getTag().equals("xyz")){
            Log.e("mytag","fm.getBackStackEntryCount())==2: " +String.valueOf(fm.getBackStackEntryCount()));

            fm.popBackStack();
            //goToMenuFragment();
            //}else{
            //    finish();
            //}
            //fm.popBackStack();
        }else if (fm.getBackStackEntryCount()==1){
            Log.e("mytag","fm.getBackStackEntryCount())==1:"+String.valueOf(fm.getBackStackEntryCount()));
            alertDialogExit();
        }else{
            Log.e("mytag","fm.getBackStackEntryCount()else:"+String.valueOf(fm.getBackStackEntryCount()));
            fm.popBackStack();
            //goToMenuFragment();
        }
    }

    public void startRepeatingTask() {
        mHandler.removeCallbacks(mHandlerTask);
        mHandlerTask.run();
    }

    public void stopRepeatingTask() {
        mHandler.removeCallbacks(mHandlerTask);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        FragmentManager fm = getSupportFragmentManager();
//        Fragment f = fm.findFragmentById(R.id.container);
//        Log.e("MyLog", String.valueOf(fm.getBackStackEntryCount()));
//        if (fm.getBackStackEntryCount() == 1) {
//            goToLinkFragment("bla");
        //}
        //Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_LONG).show();
        Log.w("MainActivity", "onResume");
        //'if (getFragmentManager().getBackStackEntryCount() == 0) {
        //goToMenuFragment();
        //}
    }

    //       if (getFragmentManager().getBackStackEntryCount() == 0) {
//
//    }
    @Override
    protected void onRestart() {
        super.onRestart();
//        FragmentManager fm = getSupportFragmentManager();
//        Fragment f = fm.findFragmentById(R.id.container);
//        Log.e("MyLog", String.valueOf(fm.getBackStackEntryCount()));
//        if (fm.getBackStackEntryCount() == 1) {
//            goToLinkFragment("bla");
//        }

        // 'Toast.makeText(getApplicationContext(), "onRestart", Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onStop() {
        super.onStop();
        //mHandler.removeCallbacks(mHandlerTask);
        Log.e("myTag", "menu activity stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mHandlerTask);
        Log.e("myTag", "menu activity destroy");
    }

    @Override
    public void onLocationChanged(Location location) {
        //longtitude = Double.toString(location.getLongitude());
        //Toast.makeText(getApplicationContext(), longtitude + ":" + latitude, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        final Activity activity = this;
        //IntentIntegrator integrator;
        switch (item.getItemId()) {
//            case R.id.main_id:
//                break;
//            case R.id.cp_id:
//                    //goToCPFragment();
//                break;
//            case R.id.messages_list:
//            //goToMessagesFragment();
//
//                break;
//            case R.id.gridview:
//                //goTogridviewFragment();
//
//                break;
//            case R.id.QRCode:
//
//
//                break;
//            case R.id.BarCode:

            //getSupportFragmentManager().beginTransaction().
            //        remove(getSupportFragmentManager().findFragmentById(R.id.container)).commit();
            // break;
        }
        return (super.onOptionsItemSelected(item));
    }



    public void alertDialog(String content){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuActivity.this);
        alertDialog.setTitle("סריקת ברקוד");
        //alertDialog.setMessage("b c d");

        final EditText input = new EditText(MenuActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setText(content);
        alertDialog.setView(input);
        //alertDialog.setIcon(R.drawable.btn_login);

        //alertDialog.setPositiveButton("YES",
        // new DialogInterface.OnClickListener() {
        //    public void onClick(DialogInterface dialog, int which) {
        //password = input.getText().toString();
        //if (password.compareTo("") == 0) {
        //    if (pass.equals(password)) {
        //        Toast.makeText(getApplicationContext(),
        //                "Password Matched", Toast.LENGTH_SHORT).show();
        //        Intent myIntent1 = new Intent(view.getContext(),
        //                Show.class);
        //       startActivityForResult(myIntent1, 0);
        //    } else {
        //        Toast.makeText(getApplicationContext(),
        //               "Wrong Password!", Toast.LENGTH_SHORT).show();
        //   }
        //}
        //}
        // });

        alertDialog.setNegativeButton("סגור",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
    public void alertDialogExit(){

        int len = 0;
        int count = 0;
        try{
            // ---------client products first time ------------
            //-------------------------------------------------
            List<String> listCIDS = new ArrayList<String>();
            listCIDS=helper.getCIDSlist();
            len = listCIDS.size();
            //-------------------------------------------------
            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/client_products");
            File[] list = myFile.listFiles();
            if (list.length == 0) {
                count = 0;
            }else
            {
                for (File ff: list){
                    String name = ff.getName();
                    if (name.endsWith(".txt"))
                        count++;
                }
            }

        }catch (Exception ex){

        }
        Log.e("mytag","count:" + count + " len:" + len );
        if (count >0 && (count != len)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuActivity.this);
            alertDialog.setTitle("יציאה");
            alertDialog.setMessage("פעולת הסנכרון לא הושלמה, "+count+ "מתוך" +len + "סונכרנו," +"האם תרצ/י לצאת בכל זאת?");

            final EditText input = new EditText(MenuActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            //input.setLayoutParams(lp);
            //input.setText(content);
            //alertDialog.setView(input);
            //alertDialog.setIcon(R.drawable.btn_login);

            alertDialog.setPositiveButton("כן",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    });

            alertDialog.setNegativeButton("לא",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog.show();
        }else{
            finish();
        }
        //


    }


    //###################################
    //WRITE URL TO FILE
    //###################################
    public void writeTextToFileGPS(String param){
        FileWriter f;
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/GPS_RECORDS.txt");
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


//control_db_id = (ImageButton) findViewById(R.id.control_db_id);
//btnMikum = (ImageButton) findViewById(R.id.button2);
//        btn1 = (ImageButton) findViewById(R.id.button1);
//        settingsBtn = (ImageButton) findViewById(R.id.settings_id);
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), CustomersActivity.class);
//                startActivity(intent);
//            }
//        });

//        btnMikum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
//                startActivity(intent);
//            }
//        });
//        settingsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), ControlPanelActivity.class);
//                startActivityForResult(intent,REQUEST_CODE);
//            }
//        });
//        control_db_id.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), ControlMyDb.class);
//                startActivity(intent);
//            }
//        });


//    public boolean checkIfGPSEnable() {
//        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("GPS").equals("0")) {
//            return false;
//        } else if ((!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) &
//                DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("GPS").equals("1")) {
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
//            return false;
//            // TODO: 21/08/2016 check if it works
//        } else if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(getBaseContext(), "GPS Tracker is off", Toast.LENGTH_SHORT).show();
//            return false;
//        } else {
//            return true;
//        }
//
//    }
