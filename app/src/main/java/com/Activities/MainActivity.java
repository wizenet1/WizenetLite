package com.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.DatabaseHelper;
import com.File_;
import com.Helper;

import java.io.File;

public class MainActivity extends Activity {



    private BroadcastReceiver mRegistrationBroadcastReceiver;

    //public final String DEMOURL = "http://main.wizenet.co.il/webservices/freelance.asmx";//default
    EditText url;
    Button button;

    //String msgID="",msgSubject="",msgComment="",msgUrl="",msgDate="",msgRead="",msgType= "";
    Helper helper;
    AlarmManager alarmManager;
    private PendingIntent pending_intent;
    private TimePicker alarmTimePicker;
    private Context context;
    DatabaseHelper db;
    public static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        boolean flag = isNetworkAvailable(context);
        File_ file = new File_();
        file.createWizenetDir(getApplicationContext());
        url = (EditText) findViewById(R.id.edittext) ;

        helper = new Helper();






        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/wizenet/");
        if (!dir.exists()) {
            dir.mkdir();
        }

        if(!DatabaseHelper.getInstance(getApplicationContext()).verification("URL")) {
            helper.addInitialfirst(this.context );
        }else{
            url.setText(DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL"));
            //Toast.makeText(getBaseContext(),"url is exists", Toast.LENGTH_SHORT).show();
        }
        //if file does not exist



        button = (Button) findViewById(R.id.continuebutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString = url.getText().toString().trim().toLowerCase();
                DatabaseHelper.getInstance(getApplicationContext()).updateValue("URL",urlString);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }

        });


        if(!DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL").equals("")){
            //Intent intent = new Intent(getApplicationContext(), MenuActivity.class);

            if (flag){
                Toast.makeText(getApplicationContext(),"internet valid", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(), MenuOfflineActivity.class);

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }else{
                Toast.makeText(getApplicationContext(),"internet invalid", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }


        }
    }
    protected void checkPermissionForReadWriteStorage() {

        final int writeExternalStorage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        final int readExternalStorage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (writeExternalStorage == PackageManager.PERMISSION_GRANTED && readExternalStorage == PackageManager.PERMISSION_GRANTED) {


        } else {
            boolean requestPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale((Activity) getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
            if (requestPermissionRationale) {

                Toast.makeText((Activity) getApplicationContext(), "Please provide  permission for reading images from gallery.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",  getApplicationContext().getPackageName(), null);
                intent.setData(uri);
                getApplicationContext().startActivity(intent);
            } else {
                ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    getLocationAndSaveInDatabaseOrEnableGPS();


                }
                break;
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "MainActivity destroyed...", Toast.LENGTH_LONG).show();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
//        case R.id.add:
//            //add the function to perform here
//            return(true);
//        case R.id.reset:
//            //add the function to perform here
//            return(true);
//        case R.id.about:
//            //add the function to perform here
//            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }



}
