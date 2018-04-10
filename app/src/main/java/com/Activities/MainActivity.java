package com.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.Classes.CallStatus;
import com.DatabaseHelper;
import com.File_;
import com.Helper;
import com.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    String httpDropSelected;
    EditText txt_enter_code;
    Spinner dynamicSpinner;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        ctx = this;
        TextView btnupdateversion = (TextView) findViewById(R.id.btnupdateversion);
        btnupdateversion.setPaintFlags(btnupdateversion.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        TextView btndownloadversion = (TextView) findViewById(R.id.btndownloadversion);
        btndownloadversion.setPaintFlags(btndownloadversion.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        btnupdateversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///////////////////
                //update db dynamiclly
                Boolean isCallOfllineEsixts = DatabaseHelper.getInstance(ctx).isTableExists("call_offline") ? true : false;
                Boolean isCallsEsixts = DatabaseHelper.getInstance(ctx).isTableExists("mgnet_calls") ? true : false;
                Boolean isControlPanelEsixts = DatabaseHelper.getInstance(ctx).isTableExists("ControlPanel") ? true : false;
                Boolean isCalltimeEsixts = DatabaseHelper.getInstance(ctx).isTableExists("Calltime") ? true : false;
                Boolean isISActionsEsixts = DatabaseHelper.getInstance(ctx).isTableExists("IS_Actions") ? true : false;

                Log.e("mytag","is is_actions table exists? "+String.valueOf(isISActionsEsixts));
                DatabaseHelper.getInstance(ctx).getTableColumns();
                //--------- add tables -------------
                if (!isCallOfllineEsixts)
                    DatabaseHelper.getInstance(ctx).createColumnToCalls_Offline("",isCallOfllineEsixts);
                if (!isCallsEsixts)
                    DatabaseHelper.getInstance(ctx).createColumnToCalls("",isCallsEsixts);
                if (!isCalltimeEsixts)
                    DatabaseHelper.getInstance(ctx).createColumnToCalltime("",isCalltimeEsixts);
                if (!isISActionsEsixts)
                    DatabaseHelper.getInstance(ctx).createColumnToISActions("",isISActionsEsixts);
                if (!isControlPanelEsixts){
                    DatabaseHelper.getInstance(ctx).createColumnToCP("",isControlPanelEsixts);
                    helper.addInitialfirst(ctx);
                }
                //---------- add columns ----------
                Toast.makeText(getBaseContext(),"עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                Log.e("mytag", String.valueOf(DatabaseHelper.getInstance(ctx).columnExistsInTable("mgnet_calls","sla")));
                //-----------add new column in mgnet_calls if not exist
                boolean isSlaColAdded = false;
                isSlaColAdded = addColumnToTable("mgnet_calls","sla",isCallsEsixts);
                if (isSlaColAdded == true){
                    Log.e("mytag","isSlaColAdded success");
                }else{
                    Log.e("mytag","isSlaColAdded already in");
                }
                //--column --ReminderID--- createColumnToISActions
                boolean isReminderIDColAdded = false;
                isReminderIDColAdded = addColumnToTable("IS_Actions","reminderID",isISActionsEsixts);
                if (isReminderIDColAdded == true){
                    Log.e("mytag","isReminderIDColAdded success");
                }else{
                    Log.e("mytag","isReminderIDColAdded already in");
                }
                boolean isAppsColAdded = DatabaseHelper.getInstance(getApplicationContext()).checkIfKeyExistsCP("APPS_CALLS_SUMMARY");
                Log.e("mytag","isAppsColAdded : " + isAppsColAdded);
                if (isAppsColAdded == false){
                    DatabaseHelper.getInstance(ctx).addControlPanel("APPS_CALLS_SUMMARY","1");
                    Log.e("mytag","isAppsColAdded success");
                }



            }
        });
        btndownloadversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url =
                         "http://main.wizenet.co.il/data/wizenet.apk";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });



        boolean flag = isNetworkAvailable(context);
        File_ f = new File_();
        f.createWizenetDir(getApplicationContext());
        url = (EditText) findViewById(R.id.edittext) ;
        txt_enter_code= (EditText) findViewById(R.id.txt_enter_code) ;
        helper = new Helper();

         dynamicSpinner = (Spinner) findViewById(R.id.spinner);
        String[] items = new String[] { "http://", "https://" };
        String s = "";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dynamicSpinner.setAdapter(adapter);
        int selectionPosition = adapter.getPosition(DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("dropHTTP"));

        dynamicSpinner.setSelection(selectionPosition);
        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                httpDropSelected =(String) parent.getItemAtPosition(position);

                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        //http://



//        if (!f.isSubDirectoryExist(context,"client_products") == true){
//            f.createSubDirectory(context,"client_products");
//        }

        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/wizenet/");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File dir1 = new File(Environment.getExternalStorageDirectory().getPath() + "/wizenet/client_products/");
        if (!dir1.exists()) {
            dir1.mkdir();
        }
        if(!DatabaseHelper.getInstance(getApplicationContext()).verification("URL")) {
            helper.addInitialfirst(this.context );
        }else{
            url.setText(DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL").replace("https://","").replace("http://",""));
            //Toast.makeText(getBaseContext(),"url is exists", Toast.LENGTH_SHORT).show();
        }
        //if file does not exist



        button = (Button) findViewById(R.id.continuebutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString = httpDropSelected+url.getText().toString().trim().toLowerCase();
                //check if is the same url, if isn't, do not auto login
                if (!DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL").equals(urlString)){
                    DatabaseHelper.getInstance(getApplicationContext()).updateValue("AUTO_LOGIN","0");
                }

                DatabaseHelper.getInstance(getApplicationContext()).updateValue("dropHTTP",httpDropSelected);
                DatabaseHelper.getInstance(getApplicationContext()).updateValue("URL",urlString);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }

        });

        Button button2 = (Button) findViewById(R.id.continuebutton2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Model.getInstance().Async_Wz_getUrl_Listener(helper.getMacAddr(), txt_enter_code.getText().toString(), new Model.Wz_getUrl_Listener() {
                    @Override
                    public void onResult(String str) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(str);
                            JSONArray jarray = j.getJSONArray("Wz_getUrl");
                            String url = jarray.getJSONObject(0).getString("Status");

                            if (url.contains("https")){
                                DatabaseHelper.getInstance(getApplicationContext()).updateValue("dropHTTP","https://");
                            }else{
                                DatabaseHelper.getInstance(getApplicationContext()).updateValue("dropHTTP","http://");
                            }

                            Toast.makeText(getApplicationContext(),"url: " + url, Toast.LENGTH_LONG).show();
                            if (url.length()>5){
                                DatabaseHelper.getInstance(getApplicationContext()).updateValue("AUTO_LOGIN","0");
                                DatabaseHelper.getInstance(getApplicationContext()).updateValue("URL",url);
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),"an error has occurred", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e1) {
                            helper.LogPrintExStackTrace(e1);
                            e1.printStackTrace();
                        }
                    }
                });

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
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }


        }
    }
    private boolean addColumnToTable(String table,String column,boolean isTableExist){
        boolean flag = false;
        if (!DatabaseHelper.getInstance(ctx).columnExistsInTable(table,column)){
            if (table == "mgnet_calls"){
                flag = DatabaseHelper.getInstance(ctx).createColumnToCalls(column,isTableExist);
            }else if(table == "ControlPanel"){
                flag = DatabaseHelper.getInstance(ctx).createColumnToCP(column,isTableExist);
            }

            Log.e("mytag","" + column + " exists?: "+ flag);
            if (flag == true){
                Log.e("mytag","success add " + column + "");
            }else{Log.e("mytag","failed");}
            //Log.e("mytag","");//DatabaseHelper.getInstance(ctx).getJsonResultsStatuses().toString()

    }
        return flag;
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
        Log.e("mytag", "MainActivity onResume");
        url.setText(DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL").replace("https://","").replace("http://",""));

        String[] items = new String[] { "http://", "https://" };
        String s = "";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dynamicSpinner.setAdapter(adapter);
        int selectionPosition = adapter.getPosition(DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("dropHTTP"));

        dynamicSpinner.setSelection(selectionPosition);
        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                httpDropSelected =(String) parent.getItemAtPosition(position);

                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity", "onPause");
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
