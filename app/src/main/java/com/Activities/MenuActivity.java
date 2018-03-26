package com.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.Adapters.SideNavigationMenuAdapter;
import com.Alarm_Receiver_Text_File;
//import com.AlertBadgeEnum;
import com.AlertBadgeEnum;
import com.DatabaseHelper;
import com.Alarm_Receiver;
import com.Fragments.FragmentActions;
//import com.Fragments.FragmentCalls;
import com.Fragments.FragmentClientReports;
import com.Fragments.FragmentCustomer;
import com.Fragments.FragmentMenu;
import com.Fragments.FragmentMenuOffline;
import com.Fragments.FragmentMessage;
import com.Fragments.FragmentMidCalls;
import com.Fragments.FragmentOrders;
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

    private DrawerLayout drawerLayout;
    private ListView sideNavigationListView;
    // Alert badge. At the moment is static but in future will be dynamic.
    //Map<AlertBadgeEnum, NotificationBadge> alertBadgeDictionary = new HashMap<AlertBadgeEnum, NotificationBadge>();
    //int homepageCount = 1;
    //int customerCount = 2;

    private Context context;
    LocationManager manager = null;
    //ImageButton btn1, settingsBtn, btnMikum, control_db_id;
    DatabaseHelper db;
    Helper helper = new Helper();
    String strBundle = "";
    ImageView homepage;
    ImageView clients;
    ImageView orders;
    ImageView missions;
    ImageView calls;

    private void popFragment(String fTag) {
        FragmentManager fm = getSupportFragmentManager();
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            // Get the back stack fragment id.
            int backStackId = getSupportFragmentManager().getBackStackEntryAt(i).getId();
            String tag = getSupportFragmentManager().getBackStackEntryAt(i).getName();
            Log.e("mytag", "fragName:" + tag);
            if (tag.equals("FragmentMenu") || tag.indexOf("f2,f3,f4,f5") > -1) {
            } else {
                fm.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

        }
        //getFragmentManager().popBackStack();
        Log.e("mytag", "fragName compare:" + fTag);
        Log.e("mytag", "fragmentManager.getBackStackEntryCount():" + fm.getBackStackEntryCount());
//        FragmentManager fm = getSupportFragmentManager();
//        List<Fragment> frags = getSupportFragmentManager().getFragments();
//        for(Fragment f : frags) {
//            if (f != null){
//                Log.e("mytag","fragments55555555555:" + f.getTag());
//                if (f.getTag().equals(fTag) || f.getTag().equals("FragmentMenu")){
//                }else{
//                    getSupportFragmentManager().beginTransaction().remove(f).commit();
//                    //fm.popBackStack(String.valueOf(f), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                }
//            }
//        }
//        Log.e("mytag","fragmentManager.getBackStackEntryCount():" + fm.getBackStackEntryCount());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("mytag", "arrived to here");

        setContentView(R.layout.top_bar);

        // Initialize all the badges.
        //this.initializeBadgeDictionary();

        // Set the alert of homepage to 1.
        //this.alertBadgeDictionary.get(AlertBadgeEnum.badge_homepage).setNumber(this.homepageCount);
        //getCallStatuses();

        //setHasOptionsMenu(false);

        db = DatabaseHelper.getInstance(getApplicationContext());
        manager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);

        this.drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);

        //THe list of items which are displayed in the side navigation menu.
        this.sideNavigationListView = (ListView) findViewById(R.id.side_nav_list);

        //Add the side navigation menu adapter.
        SideNavigationMenuAdapter sideNavMenuAdapter = new SideNavigationMenuAdapter(this,
                getSupportFragmentManager(), drawerLayout);
        sideNavigationListView.setAdapter(sideNavMenuAdapter);

        initilize();
        goToMenuFragment();
        this.initImageButtons();

        View action_bar = (View) findViewById(R.id.top_action_bar);
        action_bar.setVisibility(View.GONE);
    }

    protected void initImageButtons() {


        // Get the nav_bar view.
        final RelativeLayout outer = (RelativeLayout) findViewById(R.id.nav_bar);
        // Init the homepage button.
        homepage = (ImageView) outer.findViewById(R.id.homepage);
        // Init the clients button.
        clients = (ImageView) outer.findViewById(R.id.clients);
        // Init the message.
        orders = (ImageView) outer.findViewById(R.id.messages);
        // Init the missions.
        missions = (ImageView) outer.findViewById(R.id.arrows);
        // Init the calls.
        calls = (ImageView) outer.findViewById(R.id.help);
        // Init side menu image.
        ImageView sideMenu = (ImageView) outer.findViewById(R.id.action_image);

        //Turn all the action bar icons to their original color.
        this.turnAllActionBarIconsOff();

        sideMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();

                onMenuBarOptionsClick(v);

            }
        });

        ImageView sideNavHeaderOptionsImg = (ImageView) findViewById(R.id.side_nav_header_options);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        sideNavHeaderOptionsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSideNavOptionsClick(view);
            }
        });

        homepage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popFragment("");
                //findViewById(R.id.top_action_bar).setVisibility(View.GONE); TODO remove if not needed, I transferred it to onResume
            }
        });


        clients.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //fragmentManager.popBackStack();
                //fragmentManager.popBackStack ("f2", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                popFragment("f2");
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentCustomer fragCustomer = new FragmentCustomer();
                ft.replace(R.id.container, fragCustomer, "f2");
                ft.addToBackStack("f2");

                ft.commit();
                //Log.e("mytag","fragmentManager.getBackStackEntryCount():" + fragmentManager.getBackStackEntryCount());


            }
        });


        orders.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //fragmentManager.popBackStack ("f3", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                popFragment("f3");
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentMenuOffline fragOrders = new FragmentMenuOffline();
                ft.replace(R.id.container, fragOrders, "f3");
                ft.addToBackStack("f3");
                ft.commit();
            }
        });

        missions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popFragment("f4");
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentActions fragMessage = new FragmentActions();
                ft.replace(R.id.container, fragMessage, "f4");
                ft.addToBackStack("f4");
                ft.commit();
                //Log.e("mytag","fragmentManager.getBackStackEntryCount():" + fragmentManager.getBackStackEntryCount());

            }
        });


        calls.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean isCallsSummary = db.getValueByKey("APPS_CALLS_SUMMARY").equals("1");
                if (isCallsSummary == true) {
                    popFragment("f5");
                    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                    FragmentMidCalls fragCalls = new FragmentMidCalls();
                    ft.replace(R.id.container, fragCalls, "f5");
                    ft.addToBackStack("f5");
                    ft.commit();

                } else {
                    Intent intent = new Intent(getApplicationContext(), ActivityCalls.class);
                    intent.putExtra("choose", "total");
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * Turns all the action bar icons color off to the original color.
     */
    public void turnAllActionBarIconsOff() {

        homepage.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        calls.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        clients.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        missions.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        orders.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
    }

    /**
     * Turns the action bar calls icon on, and the rest off.
     */
    public void turnActionBarCallsIconsOn() {

        homepage.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        calls.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        clients.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        missions.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        orders.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
    }

    /**
     * Turns the action bar clients icon on, and the rest off.
     */
    public void turnActionBarClientsIconsOn() {

        homepage.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        calls.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        clients.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        missions.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        orders.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
    }

    /**
     * Turns the action bar missions icon on, and the rest off.
     */
    public void turnActionBarMissionsIconOn() {

        homepage.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        calls.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        clients.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        missions.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        orders.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
    }

    /**
     * Turns the action bar orders icon on, and the rest off.
     */
    public void turnActionBarOrdersIconOn() {

        homepage.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        calls.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        clients.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        missions.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        orders.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
    }

    public void goToOrdersManually() {
        //fragmentManager.popBackStack ("f3", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        popFragment("f3");
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentMenuOffline fragOrders = new FragmentMenuOffline();
        ft.replace(R.id.container, fragOrders, "f3");
        ft.addToBackStack("f3");
        ft.commit();
    }

//    public void setOrderGray() {
//        orders.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
//    }

    /**
     * Initializes all the badges in the action bar and main menu.
     */
//    private void initializeBadgeDictionary() {
//
//        // Set all the badges objects.
//        for(AlertBadgeEnum alert : AlertBadgeEnum.values()) {
//
//            // Get nav_bar layout.
//            View myLayout = findViewById(R.id.nav_bar);
//
//            // Initialize the nav_bar badges.
//            switch (alert){
//                case badge_homepage:
//                    this.alertBadgeDictionary.put
//                            (alert, (NotificationBadge)myLayout.findViewById(R.id.badge_homepage));
//                    break;
//                case badge_clients:
//                    this.alertBadgeDictionary.put
//                            (alert, (NotificationBadge)myLayout.findViewById(R.id.badge_clients));
//                    break;
//                case badge_messages:
//                    this.alertBadgeDictionary.put
//                            (alert, (NotificationBadge)myLayout.findViewById(R.id.badge_messages));
//                    break;
//                case badge_arrows:
//                    this.alertBadgeDictionary.put
//                            (alert, (NotificationBadge)myLayout.findViewById(R.id.badge_arrows));
//                    break;
//                case badge_help:
//                    this.alertBadgeDictionary.put
//                            (alert, (NotificationBadge)myLayout.findViewById(R.id.badge_help));
//                    break;
//
//            }
//
//        }
//    }

    /**
     * Opens the side navigation menu from the left.
     *
     * @param view view
     */
    public void onMenuBarOptionsClick(View view) {

        this.drawerLayout.openDrawer(Gravity.START);
    }

    /**
     * Closes the side navigation menu.
     *
     * @param view view
     */
    public void onSideNavOptionsClick(View view) {

        this.drawerLayout.closeDrawer(Gravity.START);
    }

    private void initilize() {
        try {
            try {
                if (db.getValueByKey("BACKGROUND").equals("1")) {
                    Intent alarm = new Intent(MenuActivity.this, Alarm_Receiver.class);
                    boolean alarmRunning = (PendingIntent.getBroadcast(MenuActivity.this, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
                    if (alarmRunning == false) {
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm, 0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        // TODO: 05/09/2016  just note the time is every 5 minutes
                        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 240000, pendingIntent);
                    }
                }
            } catch (Exception ex) {
                helper.LogPrintExStackTrace(ex);
            }


            //################################
            // FILE_TEXT
            //################################
            if (db.getValueByKey("BACKGROUND").equals("1")) {
                startService_text();
                Log.e("MYTAG", "thread started");
            } else {
                stopService_text();
            }


            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && (db.getValueByKey("GPS").equals("1"))) {
                startRepeatingTask();
            } else if ((!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) && (db.getValueByKey("GPS").equals("1"))) {
                stopRepeatingTask();
                Toast.makeText(getApplicationContext(), "gps not available", Toast.LENGTH_LONG).show();
                db.updateValue("GPS", "0");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            helper.LogPrintExStackTrace(ex);
            Log.e("mytag", ex.getMessage());
        }
    }

    public void myFunc() {
        // CLEAR BACK STACK.
        final FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.getBackStackEntryCount() == 1) {
            fragmentManager.popBackStackImmediate();

        }
        //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        //((YourActivityClassName)getActivity()).yourPublicMethod();
    }

    private void stopService_text() {
        Intent intent = new Intent(getApplicationContext(), Alarm_Receiver_Text_File.class);
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private void startService_text() {
        try {
            Intent alarm = new Intent(getApplicationContext(), Alarm_Receiver_Text_File.class);
            boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
            if (alarmRunning == false) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                // TODO: 05/09/2016  just note the time is every 5 minutes
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 240000, pendingIntent);
            }
        } catch (Exception e) {
            helper.LogPrintExStackTrace(e);
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
                        Log.e("myTag", "AsyncStatus: " + s_latitude + ":" + s_longtitude);
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

    public void goToMenuFragment() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentMenu frag = new FragmentMenu();
        ft.replace(R.id.container, frag, "FragmentMenu");

        ft.addToBackStack("FragmentMenu");
        ft.commit();
    }


    @Override
    public void onBackPressed() {

        //final FragmentClientReports ReportFragment = (FragmentClientReports) getSupportFragmentManager().findFragmentByTag("FragmentClientReports");
        //ReportFragment.onBackPressed();


        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.container);
        Log.e("Mytag", String.valueOf(fm.getBackStackEntryCount()));
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment topFragment = fragmentManager.findFragmentById(R.id.container);

        if (fm.getBackStackEntryCount() == 2) {
            //if(!topFragment.getTag().equals("xyz")){
            //findViewById(R.id.top_action_bar).setVisibility(View.GONE);
            Log.e("mytag", "fm.getBackStackEntryCount())==2: " + String.valueOf(fm.getBackStackEntryCount()));
            //fm.popBackStack();
            popFragment("");
            //TODO remove line below if not needed
            findViewById(R.id.top_action_bar).setVisibility(View.GONE);
            //goToMenuFragment();
            //}else{
            //    finish();
            //}
            //fm.popBackStack();
        } else if (fm.getBackStackEntryCount() == 1) {
            Log.e("mytag", "fm.getBackStackEntryCount())==1:" + String.valueOf(fm.getBackStackEntryCount()));
            //findViewById(R.id.top_action_bar).setVisibility(View.GONE);

            finish();
            //AlertDialoLogOut();
            //alertDialogExit();
        } else {
            Log.e("mytag", "fm.getBackStackEntryCount()else:" + String.valueOf(fm.getBackStackEntryCount()));
            fm.popBackStack();
            int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
            for (int i = 0; i < backStackCount; i++) {
                // Get the back stack fragment id.
                int backStackId = getSupportFragmentManager().getBackStackEntryAt(i).getId();
                String tag = getSupportFragmentManager().getBackStackEntryAt(i).getName();
                Log.e("mytag", "fragName:" + tag);
//                if (tag.equals("FragmentMenu") || tag.indexOf("f2,f3,f4,f5") > -1){
//                }else{
//                    fm.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                }

            }
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
        this.initImageButtons();
        popFragment("");
        View action_bar = (View) findViewById(R.id.top_action_bar);
        if (getFragmentManager().getBackStackEntryCount() > 2) {
            action_bar.setVisibility(View.VISIBLE);
        } else {
            action_bar.setVisibility(View.GONE);
        }

        initImageButtons();
        findViewById(R.id.top_action_bar).setVisibility(View.GONE);
//        FragmentManager fm = getSupportFragmentManager();
//        Fragment f = fm.findFragmentById(R.id.container);
//        Log.e("MyLog", String.valueOf(fm.getBackStackEntryCount()));
//        if (fm.getBackStackEntryCount() == 1) {
//            goToLinkFragment("bla");
        //}
        //Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_LONG).show();
        Log.e("mytag", "onResume MenuActivity");
        FragmentMidCalls myFragment = (FragmentMidCalls) getSupportFragmentManager().findFragmentByTag("FragmentMidCalls");
        if (myFragment != null && myFragment.isVisible()) {
            myFragment.setDBcurrentCalls();
            if (helper.isNetworkAvailable(getApplicationContext())) {
                myFragment.call_Async_Wz_calls_Summary_Listener();
                myFragment.runDialog();
                myFragment.pDialog.dismiss();
            } else {
                myFragment.setTexts();
            }

        }
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

    public String toString(StackTraceElement[] stackTraceElements) {
        if (stackTraceElements == null)
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement element : stackTraceElements)
            stringBuilder.append(element.toString()).append("\n");
        return stringBuilder.toString();
    }

    protected void AlertDialoLogOut() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setTitle("יציאה");
            builder.setMessage("האם ברצונך לצאת?");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int ii) {
//                int pid = android.os.Process.myPid();
//                android.os.Process.killProcess(pid);
                }
            });
            builder.setNegativeButton("לא", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int ii) {
                            dialog.dismiss();
                        }
                    }
            );

            builder.show();
        } catch (Exception e) {
            Log.e("mytag", e.getMessage());
            Log.e("mytag", toString(e.getStackTrace()));
        }

    }


    public void alertDialog(String content) {
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

    public void alertDialogExit() {

        int len = 0;
        int count = 0;
        try {
            // ---------client products first time ------------
            //-------------------------------------------------
            List<String> listCIDS = new ArrayList<String>();
            listCIDS = helper.getCIDSlist();
            len = listCIDS.size();
            //-------------------------------------------------
            File myFile = new File(Environment.getExternalStorageDirectory().getPath() + "/wizenet/client_products");
            File[] list = myFile.listFiles();
            if (list.length == 0) {
                count = 0;
            } else {
                for (File ff : list) {
                    String name = ff.getName();
                    if (name.endsWith(".txt"))
                        count++;
                }
            }

        } catch (Exception ex) {

        }
        Log.e("mytag", "count:" + count + " len:" + len);
        if (count > 0 && (count != len)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuActivity.this);
            alertDialog.setTitle("יציאה");
            alertDialog.setMessage("פעולת הסנכרון לא הושלמה, " + count + "מתוך" + len + "סונכרנו," + "האם תרצ/י לצאת בכל זאת?");

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
        } else {
            finish();
        }
        //


    }


    //###################################
    //WRITE URL TO FILE
    //###################################
    public void writeTextToFileGPS(String param) {
        FileWriter f;
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/wizenet/GPS_RECORDS.txt");
        try {
            f = new FileWriter(file, true);
            if (!file.exists()) {
                file.createNewFile();
            }
            f.write("\r\n" + param);
            f.flush();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("myTag", e.toString());
        }
    }

//    public void initialIcons() {
//        final RelativeLayout outer = (RelativeLayout) findViewById(R.id.nav_bar);
//        // Init the homepage button.
//        final ImageView homepage = (ImageView) outer.findViewById(R.id.homepage);
//        // Init the clients button.
//        final ImageView clients = (ImageView) outer.findViewById(R.id.clients);
//        // Init the message.
//        final ImageView orders = (ImageView) outer.findViewById(R.id.messages);
//        // Init the missions.
//        final ImageView missions = (ImageView) outer.findViewById(R.id.arrows);
//        // Init the calls.
//        final ImageView calls = (ImageView) outer.findViewById(R.id.help);
//        // Init the arrows.
//        final ImageView arrows = (ImageView) outer.findViewById(R.id.arrows);
//        // Init side menu image.
//        final ImageView sideMenu = (ImageView) outer.findViewById(R.id.action_image);
//
//        homepage.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
//        clients.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
//        orders.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
//        calls.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
//        arrows.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
//    }


}



