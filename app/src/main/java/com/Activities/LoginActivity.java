package com.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
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
        mac_address = helper.getMacAddr();
        db = DatabaseHelper.getInstance(getApplicationContext());
        helper= new Helper();
        setContentView(R.layout.activity_login);
        boolean flag = helper.isNetworkAvailable(ctx);
        if (db.getValueByKey("AUTO_LOGIN").equals("1")){
            goToMenu();
        }
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
            //Toast.makeText(getApplicationContext(),"internet valid", Toast.LENGTH_LONG).show();


        }else{
            Toast.makeText(getApplicationContext(),"internet invalid", Toast.LENGTH_LONG).show();
           // try{
           //     goToOfflineFragment();
           // }

        }
        final CheckBox login_checkbox_remember = (CheckBox) findViewById(R.id.login_checkbox_remember);
        login_checkbox_remember.setChecked(true);
        TextView login_forgotPassword = (TextView) findViewById(R.id.login_forgotPassword);
        TextView update_url = (TextView) findViewById(R.id.update_url);
        update_url.setPaintFlags(update_url.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
                Model.getInstance().Async_Wz_Forgot_Listener(helper.getMacAddr(getApplicationContext()), email.getText().toString(), new Model.Wz_Forgot_Listener() {
                    @Override
                    public void onResult(String str) {
                        Log.e("mytag",str.trim());
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
                //

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
                                        db.updateValue("AUTO_LOGIN","1");

                                    }
                                    goToMenu();

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

        //Set logo image.
        setTopLogoImage();
    }

    /**
     * TODO move this method to the MainActivity and save it somewhere so there won't be a need to download the image on every access
     * Sets the top image to the user's company logo.
     */
    private void setTopLogoImage() {

        //Performing network operation, need to run in a thread.
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ImageView topLogo = (ImageView) findViewById(R.id.login_topLogo);

                    //Get the user url.
                    String userUrl = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL");
                    String logoUrl = userUrl + "/data/logo.png";

                    //Get image from url.
                    URL url = new URL(logoUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    InputStream is = connection.getInputStream();
                    Bitmap img = BitmapFactory.decodeStream(is);

                    //Set image.
                    topLogo.setImageBitmap(img);

                } catch (Exception e) {
                    e.printStackTrace();
                    helper.LogPrintExStackTrace(e);
                }
            }
        });

        thread.start();
    }

    private void goToMenu(){
        try{
            Model.getInstance().Async_Wz_getProjects_Listener(mac_address, new Model.Wz_getProjects_Listener() {
                @Override
                public void onResult(String str) {
                    if(!str.contains("error")){
                        //Toast.makeText(getApplicationContext(),"success load project", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"error load project", Toast.LENGTH_LONG).show();

                    }
                }
            });
            Model.getInstance().Async_Wz_getTasks_Listener(mac_address, new Model.Wz_getTasks_Listener() {
                @Override
                public void onResult(String str) {
                    if(!str.contains("error")){
                        //Toast.makeText(getApplicationContext(),"success load tasks", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"error load tasks", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }catch(Exception e){
            helper.LogPrintExStackTrace(e);
        }

        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivityForResult(intent, 1);
        startActivity(intent);
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Toast.makeText(getApplicationContext(), String.valueOf(requestCode), Toast.LENGTH_LONG).show();
//        if (requestCode == 1) {
//            if(resultCode == Activity.RESULT_OK){
//                String result=data.getStringExtra("result");
//                Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
//                if (result == "close"){
//                    finish();
//                }
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(getApplicationContext(),"hello", Toast.LENGTH_LONG).show();
//                //Write your code if there's no result
//            }
//        }
//    }//onActivityResult


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
