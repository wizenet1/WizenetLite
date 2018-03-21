package com.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.ActivityCalls;
import com.Activities.ActivityWebView;
import com.Activities.R;
import com.Adapters.CallsAdapter;
import com.Adapters.CustomersAdapter;
import com.Adapters.NamesAdapter;
import com.Adapters.OrdersAdapter;

import com.Classes.Call;
import com.Classes.Call_offline;
import com.Classes.Order;
import com.DatabaseHelper;
import com.File_;
import com.Helper;
import com.Icon_Manager;
import com.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

//import com.google.android.gms.maps.model.LatLng;


/**
 * Created by doron on 05/03/2016.
 */
public class FragmentMidCalls extends android.support.v4.app.Fragment{
    Helper helper;
    TextView t1,t2,t3,t4,t5;
    LinearLayout calls_total,calls_open,calls_sla,calls_closed,calls_time;
    public ProgressDialog pDialog;



//CustomersAdapter customersAdapter;


    File_ f;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mid_calls, null);

        f = new File_();
        helper = new Helper();
        try{
            //send and update offline when online back
            ArrayList<Call_offline> arr_Call_offline = new ArrayList<>();
            arr_Call_offline = initOnline();
            sendCallsOffline(getContext(),arr_Call_offline);
        }catch(Exception e){
            helper.LogPrintExStackTrace(e);
        }

        runDialog();
        //spinner.setVisibility(View.VISIBLE);


        t1 = (TextView)  v.findViewById(R.id.t1);
        t2 = (TextView)  v.findViewById(R.id.t2);
        t3 = (TextView)  v.findViewById(R.id.t3);
        t4 = (TextView)  v.findViewById(R.id.t4);
        t5 = (TextView)  v.findViewById(R.id.t5);
        calls_total = (LinearLayout) v.findViewById(R.id.calls_total);
        calls_open = (LinearLayout) v.findViewById(R.id.calls_open);
        calls_sla = (LinearLayout) v.findViewById(R.id.calls_sla);
        calls_closed = (LinearLayout) v.findViewById(R.id.calls_closed);
        calls_time = (LinearLayout) v.findViewById(R.id.calls_time);
        setDBcurrentCalls();//first add from service and then load from db
        if (helper.isNetworkAvailable(getContext())) {
            call_Async_Wz_calls_Summary_Listener();
        }else{
            setTexts();
        }

        pDialog.dismiss();

        calls_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityCalls.class);
                intent.putExtra("choose", "total");

                startActivity(intent);
            }
        });
        calls_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityCalls.class);
                intent.putExtra("choose", "open");
                startActivity(intent);
            }
        });
        calls_sla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityCalls.class);
                intent.putExtra("choose", "sla");
                startActivity(intent);
            }
        });
        calls_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDashboardTechCalls();
            }
        });
        calls_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCallTimeDetails();
            }
        });
        return v;
    };
    public void runDialog(){
        pDialog = new ProgressDialog(getContext());
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Loading... ");//Please wait...
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.wizenet_logo_big, null));

        pDialog.show();
    }
    public void setDBcurrentCalls(){
        if (helper.isNetworkAvailable(getContext())){
            Model.getInstance().Async_Wz_Calls_List_Listener(getContext(),helper.getMacAddr(), -2, new Model.Wz_Calls_List_Listener() {
                @Override
                public void onResult(String str) {
                    setTexts();
                    pDialog.dismiss();
                }
            });
        }else{
            Toast.makeText(getContext(),"אינטרנט לא זמין", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendCallsOffline(final Context ctx1,ArrayList<Call_offline> arr_Call_offline){
        if (helper.isNetworkAvailable(ctx1)){
            Log.e("mytag","size: " + arr_Call_offline.size());
            if (arr_Call_offline.size()>0){
                Model.getInstance().Async_Wz_Send_Call_Offline_Listener(helper.getMacAddr(), DatabaseHelper.getInstance(ctx1).getJsonResults().toString(), new Model.Wz_Send_Call_Offline_Listener() {
                    @Override
                    public void onResult(String str) {
                        if (str.contains("0")){
                            DatabaseHelper.getInstance(ctx1).deleteAllCall_offline();
                            //change();
                        }
                        Log.e("mytag","return:" +str);
                    }
                });
            }else{
                //change();
            }
        }
    }
    private ArrayList<Call_offline> initOnline(){
        ArrayList<Call_offline> arr_Call_offline = new ArrayList<>();
        try{
            arr_Call_offline = new ArrayList<Call_offline>(DatabaseHelper.getInstance(getContext()).getCall_offline());
            for (Call_offline co:arr_Call_offline) {
                Log.e("mytag","Call_offline: " + co.toString());
            }
            if (arr_Call_offline.size() > 0 ){
                Log.e("mytag","json: " + DatabaseHelper.getInstance(getContext()).getJsonResults());

            }
        }catch (Exception e){
            helper.LogPrintExStackTrace(e);
            Log.e("mytag","11:" +e.getMessage());
        }
        return arr_Call_offline;
    }
    public void call_Async_Wz_calls_Summary_Listener(){
        try{
            Model.getInstance().Async_Wz_calls_Summary_Listener(helper.getMacAddr(), new Model.Wz_calls_Summary_Listener() {
                @Override
                public void onResult(String str) {
                    try {
                        JSONObject j = null;
                        Log.e("mytag","str:" + str);
                        j = new JSONObject(str);
                        //get the array [...] in json
                        JSONArray jarray = j.getJSONArray("Wz_calls_Summary");
                        //JSONObject object = jarray.getJSONObject(0);
                        String closedCalls = jarray.getJSONObject(0).getString("closedCalls");
                        String techcalltime = jarray.getJSONObject(0).getString("techcalltime");
                        if (techcalltime.contains("null")){
                            techcalltime = "0";
                        }
                        t4.setText(closedCalls);
                        t5.setText(techcalltime);

                    } catch (JSONException e1) {
                        helper.LogPrintExStackTrace(e1);
                    }
                }
            });
        }catch(Exception e){
            helper.LogPrintExStackTrace(e);
        }
    }
    private void goToDashboardTechCalls(){
        Intent intent = new Intent(getContext(), ActivityWebView.class);
        Bundle b = new Bundle();
        b.putInt("callid",-1);
        b.putInt("cid", -1);
        b.putInt("technicianid",Integer.valueOf(DatabaseHelper.getInstance(getContext()).getValueByKey("CID")));
        b.putString("action","dashboard");
        intent.putExtras(b);
        startActivity(intent);
    }
    private void goToCallTimeDetails(){
        Intent intent = new Intent(getContext(), ActivityWebView.class);
        Bundle b = new Bundle();
        b.putInt("callid",-1);
        b.putInt("cid", -1);
        b.putInt("technicianid",Integer.valueOf(DatabaseHelper.getInstance(getContext()).getValueByKey("CID")));
        b.putString("action","calltimedetails");
        intent.putExtras(b);
        startActivity(intent);
    }

    public void setTexts(){
       String txt1 = "";
        String txt2 = "";
        String txt3 = "";
        txt1 = DatabaseHelper.getInstance(getContext()).getScalarByCountQuery("SELECT count(CallID) FROM mgnet_calls where statusid <> -1");
        txt2 = DatabaseHelper.getInstance(getContext()).getScalarByCountQuery("SELECT count(CallID) FROM mgnet_calls where sla like '0' and  statusid <> -1");
        txt3 = DatabaseHelper.getInstance(getContext()).getScalarByCountQuery("SELECT count(CallID) FROM mgnet_calls where sla like '1' and  statusid <> -1");

        t1.setText(txt1);
        t2.setText(txt2);
        t3.setText(txt3);

    }
    public  void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}











    //###################################
    //EXTRACT CUSTOMERS FROM JSON
    //###################################
//    public Ccustomer[] getCustomersFromJson(String json){
//        Ccustomer[] customersList = new Ccustomer[0];
//        JSONObject j = null;
//        try {
//            j = new JSONObject(json);
//            //get the array [...] in json
//            JSONArray jarray = j.getJSONArray("Customers");
//            customersList = new Ccustomer[jarray.length()];
//            //customersList = new Ccustomer[jarray.length()];
//
//            for (int i = 0; i < jarray.length(); i++) {
//                JSONObject object = jarray.getJSONObject(i);
//                String fname = jarray.getJSONObject(i).getString("Cfname");
//                String lname = jarray.getJSONObject(i).getString("Clname");
//                String email = jarray.getJSONObject(i).getString("Cemail");
//                String phone = jarray.getJSONObject(i).getString("Cphone");
//                String cell = jarray.getJSONObject(i).getString("Ccell");
//                String ccompany = jarray.getJSONObject(i).getString("Ccompany");
//                Ccustomer c = new Ccustomer(fname,lname,email,phone,cell,ccompany);
//                customersList[i] = c;
//            }
//        } catch (JSONException e1) {
//            e1.printStackTrace();
//        }
//        return customersList;
//    }
//    public String getMacAddress() {
//        WifiManager manager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = manager.getConnectionInfo();
//        String address = info.getMacAddress();
//        return address;
//    }

