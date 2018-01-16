package com.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.DatabaseHelper;
import com.Fragments.FragmentMenuOffline;
import com.GPSTracker;
import com.Helper;
import com.model.Model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class LoginActivity extends FragmentActivity {

    Helper helper ;
    DatabaseHelper db;

    private static final String marshmallowMacAddress = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    String memail, mpass, mac_address;
    private TextView sign_in,reset,view_url;
    EditText email, pass;
    Button write, read;
    GPSTracker gps = null;
    Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        db = DatabaseHelper.getInstance(getApplicationContext());
        helper= new Helper();
        setContentView(R.layout.activity_login);
        boolean flag = helper.isNetworkAvailable(ctx);




//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayUseLogoEnabled(false);
//        actionBar.setDisplayHomeAsUpEnabled(false);
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(false);
//
//        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
//        View customNav = LayoutInflater.from(this).inflate(R.layout.top_bar, null); // layout which contains your button.
//
//        actionBar.setCustomView(customNav, lp1);
//        Button iv = (Button) customNav.findViewById(R.id.back);
//        iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"clicked", Toast.LENGTH_LONG).show();
//            }
//        });

        if (flag){
            Toast.makeText(getApplicationContext(),"internet valid", Toast.LENGTH_LONG).show();


        }else{
            Toast.makeText(getApplicationContext(),"internet invalid", Toast.LENGTH_LONG).show();
            goToOfflineFragment();
        }
        final CheckBox login_checkbox_remember = (CheckBox) findViewById(R.id.login_checkbox_remember);
        login_checkbox_remember.setChecked(true);
        TextView login_forgotPassword = (TextView) findViewById(R.id.login_forgotPassword);
        TextView update_url = (TextView) findViewById(R.id.update_url);
        sign_in = (TextView) findViewById(R.id.sign_in_button);

        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        try{
        if(!db.getValueByKey("username").equals("")){
            email.setText(db.getValueByKey("username").toString());
        }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }




        login_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().Async_Wz_Forgot_Listener(helper.getMacAddr(), email.getText().toString(), new Model.Wz_Forgot_Listener() {
                    @Override
                    public void onResult(String str) {
                        if (str.equals("0")){
                            Toast.makeText(getApplicationContext(), "נשלח בהצלחה", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "כתובת לא נמצאה", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        update_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                memail = email.getText().toString();
                mpass = pass.getText().toString();
                mac_address = helper.getMacAddr();//

                if (checkEmail(memail)) {

                    //Toast.makeText(getApplicationContext(),"mac_address:" + mac_address, Toast.LENGTH_LONG).show();

                    try{
                        Model.getInstance().AsyncLogin(mac_address, memail, mpass, new Model.LoginListener() {
                            @Override
                            public void onResult(String str) {
                                if(str.equals("incorrect")){
                                    Toast.makeText(getApplicationContext(), "incorrect URL", Toast.LENGTH_LONG).show();
                                }else if(str.equals("1")){
                                    //chk if is not the same user,
                                    //if is not the same user - delete files because they not belong him.
                                    if (!db.getValueByKey("username").toString().equals(memail)) {
                                        helper.deleteAllFiles();
                                    }
                                    if (login_checkbox_remember.isChecked()){
                                        db.updateValue("username",memail);
                                    }
                                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getApplicationContext(), "username or password incorrect", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }catch(Exception ex){
                        Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "invalid mail , try again", Toast.LENGTH_LONG).show();
                }
            }
        });

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

    //###################################
//CHECK EMAIL
//###################################
    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return (super.onOptionsItemSelected(item));
    }


}
