package com.Activities;

import com.Classes.Call_offline;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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
import android.graphics.Typeface;
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
  //ffgd
public class ActivityCallDetails extends FragmentActivity {

    //Testing

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
    EditText txt_internalsn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_details);
        db = DatabaseHelper.getInstance(this);
        final LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getApplicationContext(), "gps לא מופעל", Toast.LENGTH_LONG).show();
        }
        //if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            //startActivity(intent);
        //}
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
        if (call.getPriorityID().toLowerCase().contains("h")){
            txtpriority.setTextColor(Color.parseColor("#FF0000"));
            txtpriority.setTypeface(txtpriority.getTypeface(), Typeface.BOLD);

        }
        //LinearLayout layout_comment = (LinearLayout) findViewById(R.id.layout_comment);
        TextView calltime = (TextView) findViewById(R.id.calltime);
        LinearLayout layout_internalsn = (LinearLayout) findViewById(R.id.layout_internalsn);
        txt_internalsn = (EditText) findViewById(R.id.txt_internalsn);

        if ((call.getInternalSN().toLowerCase().contains("null") || call.getInternalSN().toLowerCase().trim().equals(""))) {
            txt_internalsn.setText("");
            //layout_internalsn.setVisibility(View.GONE);
            //txt_internalsn.setVisibility(View.GONE);
        } else {
            txt_internalsn.setText(call.getInternalSN().trim());
        }
        Log.e("mytag","ccomments:" + call.getCcomments().trim());
        txtcomments.setText(call.getCcomments().trim());
//        if ((call.getCcomments().toLowerCase().contains("null") || call.getCcomments().toLowerCase().trim().equals(""))) {
//            //layout_comment.setVisibility(View.GONE);
//            //txtcomments.setVisibility(View.GONE);
//        } else {
//            txtcomments.setText(call.getCcomments().trim());
//        }


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

        id1.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        id2.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        id3.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));

        id1.setTextSize(40);
        id1_text.setText("נסיעה");
        id2.setTextSize(40);
        id2_text.setText("עבודה");
        id3.setTextSize(40);
        id3_text.setText("סיום");

        TextView ccompany_telephone = (TextView) findViewById(R.id.ccompany_telephone);
        TextView ccompany_mobile = (TextView) findViewById(R.id.ccompany_mobile);
        TextView contct_telephone = (TextView) findViewById(R.id.contct_telephone);
        TextView contct_mobile = (TextView) findViewById(R.id.contct_mobile);

        ccompany_telephone.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        ccompany_mobile.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        contct_telephone.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        contct_mobile.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));

        ccompany_telephone.setTextSize(30);
        ccompany_mobile.setTextSize(30);
        contct_telephone.setTextSize(30);
        contct_mobile.setTextSize(30);
        //---------------------------------------
        TextView customerfiles = (TextView) findViewById(R.id.customerfiles);
        TextView customercase = (TextView) findViewById(R.id.customercase);
        TextView history = (TextView) findViewById(R.id.history);
        TextView callfiles = (TextView) findViewById(R.id.callfiles);
        mobile = (TextView) findViewById(R.id.mobile);
        sign = (TextView) findViewById(R.id.sign);
        location = (TextView) findViewById(R.id.location);

        txtcallid.setText(callid);

        customerfiles.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        customercase.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        history.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        callfiles.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        mobile.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        sign.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        location.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        parts.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        calltime.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));

//        customerfiles.setBackgroundResource(R.drawable.btn_circle2);
//        customercase.setBackgroundResource(R.drawable.btn_circle2);
//        history.setBackgroundResource(R.drawable.btn_circle2);
//        callfiles.setBackgroundResource(R.drawable.btn_circle2);
//        mobile.setBackgroundResource(R.drawable.btn_circle2);
//        sign.setBackgroundResource(R.drawable.btn_circle2);
//        parts.setBackgroundResource(R.drawable.btn_circle2);
//        calltime.setBackgroundResource(R.drawable.btn_circle2);
        customerfiles.setTextSize(30);
        customercase.setTextSize(30);
        history.setTextSize(30);
        callfiles.setTextSize(30);
        mobile.setTextSize(30);
        sign.setTextSize(30);
        location.setTextSize(30);
        parts.setTextSize(30);


        calltime.setTextSize(30);
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


        id1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(getApplicationContext(), "gps לא מופעל", Toast.LENGTH_LONG).show();
                }
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
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(getApplicationContext(), "gps לא מופעל", Toast.LENGTH_LONG).show();
                }
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
                if (id1.getCurrentTextColor() == Color.parseColor("#E94E1B") == true &&
                        id2.getCurrentTextColor() == Color.parseColor("#E94E1B") == true ) {
                    Async(Integer.valueOf(callid), "stop", "", "");
                    Toast.makeText(getApplicationContext(), "stop", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "אינך יכול לעצור במצב זה", Toast.LENGTH_LONG).show();
                }

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
        customercase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCustomerCase();
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHistory();
            }
        });
        callfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCallFiles();
            }
        });


        calltime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCalltime();
            }
        });
        parts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToParts();
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               goToSign();
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             goToWaze();
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusID == -1){
                    Async(Integer.valueOf(callid), "stop", "", "");
                }
                DatabaseHelper.getInstance(getApplicationContext()).updateSpecificValueInTable2("mgnet_calls","CallID",String.valueOf(callid),"statusID","'" +String.valueOf(statusID) + "'");
                DatabaseHelper.getInstance(getApplicationContext()).updateSpecificValueInTable2("mgnet_calls","CallID",String.valueOf(callid),"statusName","'" +statusName + "'");
                DatabaseHelper.getInstance(getApplicationContext()).updateSpecificValueInTable2("mgnet_calls","CallID",String.valueOf(callid),"internalSN","'" +txt_internalsn.getText().toString() + "'");
                if (helper.isNetworkAvailable(getApplicationContext())){
                    Model.getInstance().Async_Wz_Update_Call_Field_Listener(helper.getMacAddr(), (callid), "internalSN", "'" + txt_internalsn.getText().toString() + "'", new Model.Wz_Update_Call_Field_Listener() {
                        @Override
                        public void onResult(String str) {

                        }
                    });
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
                                        finish();
                                        //txttechanswer.setText("");
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
                }else{
                    Call_offline co = new Call_offline(Integer.valueOf(callid),statusID,txt_internalsn.getText().toString(),txttechanswer.getText().toString());
                    DatabaseHelper.getInstance(getApplicationContext()).add_call_offline(co);
                    //DatabaseHelper.getInstance(getApplicationContext()).updateSpecificValueInTable2("mgnet_calls","CallID",String.valueOf(callid),"offline","'1'");
                    Toast.makeText(getApplicationContext(), "internet invalid", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
        final Activity  activity = this;
        TextView sign1 = (TextView) findViewById(R.id.sign1);
        sign1.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        sign1.setTextSize(30);
        //sign1.setVisibility(View.GONE);
        sign1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                IntentIntegrator integrator;
                integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("צלם לרוחב");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });


    }
      @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
          super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //goToMenuFragment();
                Log.d("MainActivity", "Scanned");
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                txt_internalsn.setText(result.getContents());

            }
        } else {

        }
      }



    private void goToCustomerCase(){
          Intent intent = new Intent(getApplicationContext(), ActivityWebView.class);
          Bundle b = new Bundle();
          b.putInt("callid", call.getCallID());
          b.putInt("cid", call.getCID());
          b.putInt("technicianid", call.getTechnicianID());
          b.putString("action","customercase");
          intent.putExtras(b);
          startActivity(intent);
      }
    private void goToHistory(){
          Intent intent = new Intent(getApplicationContext(), ActivityWebView.class);
          Bundle b = new Bundle();
          b.putInt("callid", call.getCallID());
          b.putInt("cid", call.getCID());
          b.putInt("technicianid", call.getTechnicianID());
          b.putString("action","history");
          intent.putExtras(b);
          startActivity(intent);
      }
    private void goToCallFiles(){
//        Intent intent = new Intent(getApplicationContext(), ActivityWebView.class);
//        Bundle b = new Bundle();
//        b.putInt("callid", call.getCallID());
//        b.putInt("cid", call.getCID());
//        b.putInt("technicianid", call.getTechnicianID());
//        b.putString("action","callfiles");
//        intent.putExtras(b);
//        startActivity(intent);
          String url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL")
                  + "/iframe.aspx?control=/modulesServices/CallsFiles&CallID=" + String.valueOf(call.getCallID()) + "&class=CallsFiles_appCell&mobile=True";
          Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
          startActivity(browserIntent);
      }
    private void goToCalltime(){
        Intent intent = new Intent(getApplicationContext(), ActivityWebView.class);
        Bundle b = new Bundle();
        b.putInt("callid", call.getCallID());
        b.putInt("cid", call.getCID());
        b.putInt("technicianid", call.getTechnicianID());
        b.putString("action","calltime");
        intent.putExtras(b);
        startActivity(intent);
    }
    private void goToWaze(){
        try
        {
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
    private void goToParts(){
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
    private void goToSign(){
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
//    private List<Call> getCallsList(){
//        JSONObject j = null;
//        int length = 0;
//
//        List<Call> calls = new ArrayList<Call>() ;
//        try {
//            calls= DatabaseHelper.getInstance(this).getCalls("");
//            length = calls.size();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        return calls;
//    }


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
