package com.Activities;

import com.model.*;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Classes.Call;
import com.Classes.CallStatus;
import com.DatabaseHelper;
import com.GPSTracker;
import com.Helper;
import com.Icon_Manager;
import com.Adapters.CallsAdapter;
import com.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityCallDetails extends FragmentActivity {

    TextView mobile, sign, location;
    EditText txttechanswer;
    Button btnupdate;
    TextView txtcallid, id1, id2, id3, parts;
    TextView id1_text, id2_text, id3_text;
    TextView txtccompany, txtpriority, txtorigin, txtsubject, txtcomments, txtcreatedate, txtcallstarttime, txtccity, txtcalltypename;
    Icon_Manager icon_manager;
    Helper helper;
    Call call;
    LinearLayout assigmentlayout;
    DatabaseHelper db;
    int statusID;
    String statusName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_details);
        db = DatabaseHelper.getInstance(this);
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        final GPSTracker gps = new GPSTracker(this);
        helper = new Helper();
        db = DatabaseHelper.getInstance(this);
        icon_manager = new Icon_Manager();
        String s = getIntent().getStringExtra("EXTRA_SESSION_ID");
        final String callid = s;// = getArguments().getString("puId");
        Log.e("myTag", "id arrived: " + callid);
        //call = new Call(Integer.valueOf(cursor.getString(cursor.getColumnIndex("CallID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("AID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("CID"))), cursor.getString(cursor.getColumnIndex("CreateDate")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("statusID"))), cursor.getString(cursor.getColumnIndex("CallPriority")), cursor.getString(cursor.getColumnIndex("subject")), cursor.getString(cursor.getColumnIndex("comments")), cursor.getString(cursor.getColumnIndex("CallUpdate")), cursor.getString(cursor.getColumnIndex("cntrctDate")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("TechnicianID"))), cursor.getString(cursor.getColumnIndex("statusName")), cursor.getString(cursor.getColumnIndex("internalSN")), cursor.getString(cursor.getColumnIndex("Pmakat")), cursor.getString(cursor.getColumnIndex("Pname")), cursor.getString(cursor.getColumnIndex("contractID")), cursor.getString(cursor.getColumnIndex("Cphone")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("OriginID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("ProblemTypeID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("CallTypeID"))), cursor.getString(cursor.getColumnIndex("priorityID")), cursor.getString(cursor.getColumnIndex("OriginName")), cursor.getString(cursor.getColumnIndex("problemTypeName")), cursor.getString(cursor.getColumnIndex("CallTypeName")), cursor.getString(cursor.getColumnIndex("Cname")), cursor.getString(cursor.getColumnIndex("Cemail")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("contctCode"))), cursor.getString(cursor.getColumnIndex("callStartTime")), cursor.getString(cursor.getColumnIndex("callEndTime")), cursor.getString(cursor.getColumnIndex("Ccompany")), cursor.getString(cursor.getColumnIndex("Clocation")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("callOrder"))), cursor.getString(cursor.getColumnIndex("Caddress")), cursor.getString(cursor.getColumnIndex("Ccity")), cursor.getString(cursor.getColumnIndex("Ccomments")), cursor.getString(cursor.getColumnIndex("Cfname")), cursor.getString(cursor.getColumnIndex("Clname")), cursor.getString(cursor.getColumnIndex("techName")), cursor.getString(cursor.getColumnIndex("Aname")), cursor.getString(cursor.getColumnIndex("ContctName")), cursor.getString(cursor.getColumnIndex("ContctAddress")), cursor.getString(cursor.getColumnIndex("ContctCity")), cursor.getString(cursor.getColumnIndex("ContctCell")), cursor.getString(cursor.getColumnIndex("ContctPhone")), cursor.getString(cursor.getColumnIndex("ContctCity")), cursor.getString(cursor.getColumnIndex("Ccell")), cursor.getString(cursor.getColumnIndex("techColor")), cursor.getString(cursor.getColumnIndex("ContctCemail")), cursor.getString(cursor.getColumnIndex("CallParentID")));
        call = new Call();
        call = db.getCallDetailsByCallID(Integer.valueOf(callid));

        mobile = (TextView) findViewById(R.id.mobile);
        sign = (TextView) findViewById(R.id.sign);
        location = (TextView) findViewById(R.id.location);

        btnupdate = (Button) findViewById(R.id.btnupdate);
        assigmentlayout = (LinearLayout) findViewById(R.id.assigmentlayout);
        txtpriority = (TextView) findViewById(R.id.call_priority);
        txtorigin = (TextView) findViewById(R.id.call_origin);
        txtcallid = (TextView) findViewById(R.id.txtcallid);
        txtcalltypename = (TextView) findViewById(R.id.txtcalltypename);
        txtccompany = (TextView) findViewById(R.id.txtccompany);
        txtsubject = (TextView) findViewById(R.id.txtsubject);
        txtcomments = (TextView) findViewById(R.id.txtcomments);
        txtcreatedate = (TextView) findViewById(R.id.txtcreatedate);
        txtcallstarttime = (TextView) findViewById(R.id.txtcallstarttime);
        txtccity = (TextView) findViewById(R.id.txt_ccity);
        txttechanswer = (EditText) findViewById(R.id.txttechanswer);
        parts = (TextView) findViewById(R.id.parts);
        //Toast.makeText(getApplication(),"getCallTypeName : " +call.getCallTypeName().trim(), Toast.LENGTH_LONG).show();
        txtccompany.setText(call.getCcompany().trim());
        txtccompany.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtsubject.setText(call.getSubject().trim());
        txtorigin.setText(isContainNull(call.getOriginName().trim()));
        txtpriority.setText(isContainNull(call.getPriorityID().trim()));
        LinearLayout layout_comment = (LinearLayout) findViewById(R.id.layout_comment);
        Button calltime = (Button) findViewById(R.id.calltime);

        if ((call.getCcomments().toLowerCase().contains("null") || call.getCcomments().toLowerCase().trim().equals(""))) {
            layout_comment.setVisibility(View.GONE);
            txtcomments.setVisibility(View.GONE);
        } else {
            txtcomments.setText(call.getCcomments().trim());
        }


        txtcreatedate.setText(call.getCreateDate().trim());
        txtcallstarttime.setText(call.getCallStartTime().trim());
        txtccity.setText(isContainNull(call.getCcity().trim()));
        txtcalltypename.setText(isContainNull(call.getCallTypeName().trim()));

        id1 = (TextView) findViewById(R.id.id1);
        id2 = (TextView) findViewById(R.id.id2);
        id3 = (TextView) findViewById(R.id.id3);
        id1_text = (TextView) findViewById(R.id.id1_text);
        id2_text = (TextView) findViewById(R.id.id2_text);
        id3_text = (TextView) findViewById(R.id.id3_text);
        //id1.setBackgroundColor(Color.parseColor("#E94E1B"));
        id1.setBackgroundResource(R.drawable.btn_circle4);
        id2.setBackgroundResource(R.drawable.btn_circle4);
        id3.setBackgroundResource(R.drawable.btn_circle4);

        TextView ccompany_telephone = (TextView) findViewById(R.id.ccompany_telephone);
        TextView ccompany_mobile = (TextView) findViewById(R.id.ccompany_mobile);
        TextView contct_telephone = (TextView) findViewById(R.id.contct_telephone);
        TextView contct_mobile = (TextView) findViewById(R.id.contct_mobile);

        ccompany_telephone.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        ccompany_mobile.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        contct_telephone.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        contct_mobile.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        parts.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));

        ccompany_telephone.setTextSize(30);
        ccompany_mobile.setTextSize(30);
        contct_telephone.setTextSize(30);
        contct_mobile.setTextSize(30);




        id1.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        id2.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        id3.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));

        txtcallid.setText(callid);

        mobile.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        mobile.setTextSize(30);
        sign.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        sign.setTextSize(40);
        location.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        location.setTextSize(30);
        parts.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        parts.setTextSize(40);
        calltime.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        calltime.setTextSize(40);
        if ((call.getCallStartTime() + "-" + call.getCallEndTime()).equals("null-null")) {
            assigmentlayout.setVisibility(View.GONE);
        } else {
            txtcallstarttime.setText(call.getCallStartTime().substring(0,16) + " - " + call.getCallEndTime().substring(11,16));
            txtcallstarttime.setTextColor(Color.parseColor("#E94E1B"));
        }


        Spinner dynamicSpinner = (Spinner) findViewById(R.id.spinner);
        //String[] items = new String[] { "Chai Latte", "Green Tea", "Black Tea" };
        List<CallStatus> statusList = new ArrayList<CallStatus>();
        statusList = DatabaseHelper.getInstance(this).getCallStausList();
        String[] items1 = new String[statusList.size()];
        for (int i = 0; i < statusList.size(); i++) {
            items1[i] = statusList.get(i).getCallStatusName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        dynamicSpinner.setAdapter(adapter);
        int selectionPosition = adapter.getPosition(call.getStatusName());
        dynamicSpinner.setSelection(selectionPosition);
        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String s = "";
                s = String.valueOf(db.getCallStatusByCallStatusName((String) parent.getItemAtPosition(position)).getCallStatusID());
                statusID = db.getCallStatusByCallStatusName((String) parent.getItemAtPosition(position)).getCallStatusID();
                statusName = db.getCallStatusByCallStatusName((String) parent.getItemAtPosition(position)).getCallStatusName();
                //Toast.makeText(getApplication(), "status: " + s, Toast.LENGTH_LONG).show();
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        Model.getInstance().Async_Wz_Call_getTime_Listener(helper.getMacAddr(), Integer.valueOf(callid), "", new Model.Wz_Call_getTime_Listener() {
            @Override
            public void onResult(String str) {
                JSONObject j = null;
                try {
                    j = new JSONObject(str);
                    JSONArray jarray = j.getJSONArray("Wz_Call_getTime");
                    String statuses = jarray.getJSONObject(0).getString("Status");
                    //Toast.makeText(getActivity(),"statuses: " + statuses, Toast.LENGTH_LONG).show();

                    if (isContain(statuses, "ride")) {
                        id1.setTextColor(Color.parseColor("#E94E1B"));
                    } else {
                        id1.setTextColor(Color.parseColor("black"));
                    }
                    if (isContain(statuses, "work")) {
                        id2.setTextColor(Color.parseColor("#E94E1B"));
                        id1.setTextColor(Color.parseColor("#E94E1B"));
                    } else {
                        id2.setTextColor(Color.parseColor("black"));
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });
        id1.setTextSize(40);
        id1_text.setText("נסיעה");
        id2.setTextSize(40);
        id2_text.setText("עבודה");
        id3.setTextSize(40);
        id3_text.setText("סיום");

        id1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id1.getCurrentTextColor() == Color.parseColor("#E94E1B") == true) {
                    Toast.makeText(getApplicationContext(), "הינך במצב נסיעה", Toast.LENGTH_LONG).show();
                } else {
                    String s_longtitude = "";
                    String s_latitude = "";
                    try {
                        gps.getLocation();
                        s_longtitude = Double.toString(gps.getLongitude());
                        s_latitude = Double.toString(gps.getLatitude());
                        //Toast.makeText(getActivity(),"s_longtitude:"+s_longtitude+"\ns_latitude:"+s_latitude, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "ex:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    Async(Integer.valueOf(callid), "ride", s_latitude, s_longtitude);
                    Toast.makeText(getApplicationContext(), "ride", Toast.LENGTH_LONG).show();
                }
            }
        });
        id2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id2.getCurrentTextColor() == Color.parseColor("#E94E1B") == true) {
                    Toast.makeText(getApplicationContext(), "הינך במצב עבודה", Toast.LENGTH_LONG).show();
                } else {
                    String s_longtitude = "";
                    String s_latitude = "";
                    try {
                        gps.getLocation();
                        s_longtitude = Double.toString(gps.getLongitude());
                        s_latitude = Double.toString(gps.getLatitude());
                        //Toast.makeText(getActivity(),"s_longtitude:"+s_longtitude+"\ns_latitude:"+s_latitude, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "ex:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    Async(Integer.valueOf(callid), "work", s_latitude, s_longtitude);
                    Toast.makeText(getApplicationContext(), "work", Toast.LENGTH_LONG).show();
                }
            }
        });
        id3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Async(Integer.valueOf(callid), "stop", "", "");
                Toast.makeText(getApplicationContext(), "stop", Toast.LENGTH_LONG).show();
            }
        });
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (call.getCcell().trim().contains("null") || call.getCcell().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "no cell", Toast.LENGTH_LONG).show();
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + call.getCcell()));//String.valueOf(callsArrayList.get(pos).getCallID())
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    getApplicationContext().startActivity(callIntent);
                }

                //Uri uri = Uri.parse("smsto:"+callsArrayList.get(pos).getCcell());
                //Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                //c.startActivity(it);
            }
        });
        //final WebView  mWebview  = new WebView(this);
        //final Activity activity = this;
        calltime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///iframe.aspx?control=/modulesServices/CallRepHistory&CallID=46713&class=tdCallRepHistory&mobile=True
                Intent intent = new Intent(getApplicationContext(), ActivityWebView.class);
                Bundle b = new Bundle();
                b.putInt("callid", call.getCallID());
                b.putInt("cid", call.getCID());
                b.putInt("technicianid", call.getTechnicianID());
                b.putString("action","calltime");
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        parts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Intent intent = new Intent(getApplicationContext(), ActivityWebView.class);
                    Bundle b = new Bundle();
                    b.putInt("callid", call.getCallID());
                    b.putInt("cid", call.getCID());
                    b.putInt("technicianid", call.getTechnicianID());
                    b.putString("action","callparts");
                    intent.putExtras(b);
                    startActivity(intent);
                }
                catch ( ActivityNotFoundException ex  )
                {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    String url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL") + "/modulesSign/sign.aspx?callID=" + String.valueOf(call.getCallID());

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    //AlertDialogWeb(String.valueOf(callsArrayList.get(pos).getCallID()));
                }
                catch ( ActivityNotFoundException ex  )
                {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    // Launch Waze to look for Hawaii:
                    String url = "waze://?q=" + call.getCaddress() + " " + call.getCcity();
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity( intent );
                }
                catch ( ActivityNotFoundException ex  )
                {
                    // If Waze is not installed, open it in Google Play:
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
                    getApplicationContext().startActivity(intent);
                }
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().Async_Wz_Call_Update_Listener(helper.getMacAddr(), Integer.valueOf(callid), statusID,
                        txttechanswer.getText().toString(), new Model.Wz_Call_Update_Listener() {
                            @Override
                            public void onResult(String str) {
                                try {
                                    JSONObject j = null;
                                    j = new JSONObject(str);
                                    Log.e("MYTAG",str);
                                    //get the array [...] in json
                                    JSONArray jarray = j.getJSONArray("Wz_Call_Update");
                                    String status = jarray.getJSONObject(0).getString("Status");
                                    if (status.equals("0")){
                                        Toast.makeText(getApplicationContext(),"successfully updated", Toast.LENGTH_LONG).show();
                                        //DatabaseHelper.getInstance(getContext()).updateSpecificValueInTable("mgnet_calls","CallID",callid,"StatusName",statusName);
                                        //DatabaseHelper.getInstance(getContext()).updateSpecificValueInTable("mgnet_calls","CallID",callid,"StatusID",String.valueOf(statusID));
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
            }
        });

    }
    public void Async(int callid,String action,String latitude,String longtitude) {
        Model.getInstance().Async_Wz_Call_setTime_Listener(helper.getMacAddr(), Integer.valueOf(callid), action,latitude,longtitude, new Model.Wz_Call_setTime_Listener() {
            @Override
            public void onResult(String str) {
                try {
                    JSONObject j = null;
                    j = new JSONObject(str);
                    //get the array [...] in json
                    JSONArray jarray = j.getJSONArray("Wz_Call_setTime");
                    String statuses = jarray.getJSONObject(0).getString("Status");

                    if (isContain(statuses,"ride")){id1.setTextColor(Color.parseColor("black"));}else{id1.setTextColor(Color.parseColor("#E94E1B"));}
                    if (isContain(statuses,"work")){id2.setTextColor(Color.parseColor("black"));}else{id2.setTextColor(Color.parseColor("#E94E1B"));}

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
    public  boolean isContain(String inputStr, String item)
    {
        if(inputStr.contains(item)){
            return true;
        }else{
            return false;
        }
    }
    public  String isContainNull(String inputStr)
    {
        if(inputStr.contains("null")){
            return "";
        }else{
            return inputStr;
        }
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
//            case R.id.add:
//                //add the function to perform here
//                return (true);
            //case R.id.action_filter:
            //add the function to perform here
            // return (true);
//            case R.id.about:
//                //add the function to perform here
//                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }
    private List<Call> getCallsList(){
        JSONObject j = null;
        int length = 0;

        List<Call> calls = new ArrayList<Call>() ;
        try {
            calls= DatabaseHelper.getInstance(this).getCalls();
            length = calls.size();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return calls;
    }


    public void AlertDialogWeb(String callid){



        AlertDialog.Builder alert = new AlertDialog.Builder(ActivityCallDetails.this);
        alert.setTitle("Title here");
        WebView wv = new WebView(this.getApplicationContext());
        wv.getSettings().setJavaScriptEnabled(true); // enable javascript
        wv.setWebChromeClient(new WebChromeClient());

        String url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL")
                + "/iframe.aspx?control=modulesServices%2fCallParts&CallID=" + callid + "&type=customer&val=" + String.valueOf(call.getCID()) + "";
                //+ "/iframe.aspx?control=/modulesServices/CallsFiles&CallID=" + callid + "&class=CallsFiles_appCell&mobile=True";

        wv.loadUrl(url);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        //alert.show();
        AlertDialog alertDialogMain = alert.create();

// Showing Alert Message
        alertDialogMain.show();
    }
    public void goToWebFragment(){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        //FragmentWebView frag = new FragmentWebView();
        //ft.replace(R.id.container,frag,"FragmentWebView");
        //tv.setVisibility(TextView.GONE);
        ft.addToBackStack("FragmentWebView");
        ft.commit();
    }

}
