package com.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DatabaseHelper;
import com.Fragments.ControlPanelFragment;
import com.Fragments.FragmentCustomer;
import com.Fragments.FragmentMenu;
import com.Fragments.FragmentMenuOffline;
import com.Fragments.FragmentMessage;
import com.GPSTracker;

import com.Helper;
import com.model.Model;

import java.io.File;
import java.io.FileWriter;
import java.util.Timer;
import java.util.TimerTask;

public class MenuOfflineActivity extends FragmentActivity {
    EditText myEditText5;
    Handler mHandler = new Handler();
    TextView tv;
    //public static boolean isTrack = false;
    GPSTracker gps;
    String s_longtitude = "";
    String s_latitude = "";
    private Context context;
    LocationManager manager = null;
    //ImageButton btn1, settingsBtn, btnMikum, control_db_id;
    DatabaseHelper db;
    Helper helper = new Helper();
    String strBundle = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_offline);
        myEditText5 = (EditText) findViewById(R.id.myEditText);


        //setHasOptionsMenu(false);


        this.context = this;



        setContentView(R.layout.activity_menu_offline);
        goToOfflineFragment();

    }
    public void goToOfflineFragment(){
        //Bundle bundle=new Bundle();
        //bundle.putString("name", strBundle);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentMenuOffline frag = new FragmentMenuOffline();
        //frag.setArguments(bundle);
        ft.replace(R.id.container,frag,"FragmentMenuOffline");

        ft.addToBackStack("FragmentMenuOffline");
        ft.commit();
    }

    public void goToMessagesFragment(){
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentMessage frag = new FragmentMessage();
        ft.replace(R.id.container,frag,"FragmentMessage");
        //tv.setVisibility(TextView.GONE);
        ft.addToBackStack("FragmentMessage");
        ft.commit();
    }

    public void goToMenuFragment(){
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentMenu frag = new FragmentMenu();
        ft.replace(R.id.container,frag,"FragmentMenu");

        ft.addToBackStack("FragmentMenu");
        ft.commit();
    }
    public void goTogridviewFragment(){
        Bundle bundle=new Bundle();
        bundle.putString("name", strBundle);
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentCustomer frag = new FragmentCustomer();
        frag.setArguments(bundle);
        ft.replace(R.id.container,frag,"CustomGridFragment");

        ft.addToBackStack("CustomGridFragment");
        ft.commit();
    }
    public void goToCPFragment(){

        //mydb.getInstance(getApplicationContext());
        ControlPanelFragment controlPanelFragment = new ControlPanelFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,controlPanelFragment,"CPFragment");
        fragmentTransaction.addToBackStack("CPFragment");
        //tv.setVisibility(TextView.GONE);
        fragmentTransaction.commit();

    }
    public void goToLinkFragment(String result){
        Bundle bundle = new Bundle();
        bundle.putString("DocNum", result);   //parameters are (key, value).
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();;
        //android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
//        FragmentLink frag = new FragmentLink();
//        frag.setArguments(bundle);
//        fragmentTransaction.add(R.id.container,frag,"FragmentLink");
//        fragmentTransaction.addToBackStack("FragmentLink");
//        fragmentTransaction.commit();
    }
   @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();

       if (helper.isNetworkAvailable(context)){
           fm.popBackStack();
           if(fm.getBackStackEntryCount() == 1){
               fm.popBackStack();
           }else{
               fm.popBackStack();
           }
       }else{
           fm.popBackStack();
       }
//        Fragment f = fm.findFragmentById(R.id.container);
//        Log.e("MyLog", String.valueOf(fm.getBackStackEntryCount()));
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        FragmentMenu frag = new FragmentMenu();
//        ft.replace(R.id.container,frag,"FragmentMenu");
//
//        ft.addToBackStack("FragmentMenu");
//        ft.commit();
        //Fragment topFragment = fragmentManager.findFragmentById(R.id.container);
        //if(topFragment!= null) {
            //Toast.makeText(getApplicationContext(), topFragment.getTag(), Toast.LENGTH_LONG).show();
//        for (int i=0; i<getSupportFragmentManager().getFragments().size();i++) {
//            Fragment topFragment = fragmentManager.findFragmentById(R.id.container);
//            if(topFragment!= null) {
//                Toast.makeText(getApplicationContext(), topFragment.getTag(), Toast.LENGTH_LONG).show();
//                //fragmentTransaction.remove(topFragment);
//            }
//        }
//        if(fm.getBackStackEntryCount() == 2){
//            if(!topFragment.getTag().equals("FragmentMenu")){
//                fm.popBackStack();
//                //goToMenuFragment();
//            }else{
//                finish();
//            }
//            //fm.popBackStack();
//        }else if (fm.getBackStackEntryCount()==1){
//            finish();
//        }else{
//            fm.popBackStack();
//            //goToMenuFragment();
//        }
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
       // mHandler.removeCallbacks(mHandlerTask);
        Log.e("myTag", "menu activity destroy");
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
//                    goToCPFragment();
//                break;
//            case R.id.messages_list:
//            goToMessagesFragment();
//
//                break;
//            case R.id.gridview:
//                goTogridviewFragment();
//
//                break;
//            case R.id.QRCode:
//
//                if(fm.getBackStackEntryCount() > 1) {
//                    fm.popBackStack();
//                }
//                integrator = new IntentIntegrator(activity);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//                integrator.setPrompt("צלם לאורך");
//                integrator.setCameraId(0);
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(false);
//                integrator.initiateScan();
//
//                break;
//            case R.id.BarCode:
                //while (fm.getBackStackEntryCount() == 1){
                 //   fm.popBackStack();
                //}
                //if(fm.getBackStackEntryCount() > 1) {
                //   fm.popBackStack();
               //}
//                integrator = new IntentIntegrator(activity);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//                integrator.setPrompt("צלם לרוחב");
//                integrator.setCameraId(0);
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(false);
//                integrator.initiateScan();
                //getSupportFragmentManager().beginTransaction().
                //        remove(getSupportFragmentManager().findFragmentById(R.id.container)).commit();
                //break;
        }
        return (super.onOptionsItemSelected(item));
    }

    public boolean setAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int ii) {

                //DatabaseHelper.getInstance(getApplicationContext()).deleteRowByMsgID(data2.get(po).getMsgID());
                Toast.makeText(getApplicationContext(),"clicked yes", Toast.LENGTH_LONG).show();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int ii) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();

        return true;
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


