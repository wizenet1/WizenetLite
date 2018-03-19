package com.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.ActivityCalls;
import com.Activities.ActivityWebView;
import com.Activities.R;
import com.Adapters.CustomersAdapter;
import com.Adapters.NamesAdapter;
import com.Adapters.OrdersAdapter;

import com.Classes.Order;
import com.DatabaseHelper;
import com.File_;
import com.Helper;
import com.Icon_Manager;

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



//CustomersAdapter customersAdapter;


    File_ f;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mid_calls, null);
        f = new File_();
        helper = new Helper();
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
        setTexts();

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
                ///iframe.aspx?control=modulesServices/dashboard_report&action=techgraph&calltype=&data=239610
            }
        });
        calls_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ActivityCalls.class);
//                intent.putExtra("choose", "total");
//                startActivity(intent);
            }
        });
        return v;
    };
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

    private void setTexts(){
       String txt1 = "";
        String txt2 = "";
        String txt3 = "";
        txt1 = DatabaseHelper.getInstance(getContext()).getScalarByCountQuery("SELECT count(CallID) FROM mgnet_calls");
        txt2 = DatabaseHelper.getInstance(getContext()).getScalarByCountQuery("SELECT count(CallID) FROM mgnet_calls where sla like '0'");
        txt3 = DatabaseHelper.getInstance(getContext()).getScalarByCountQuery("SELECT count(CallID) FROM mgnet_calls where sla like '1'");

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

