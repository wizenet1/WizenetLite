package com.Activities;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Classes.Call;
import com.Classes.CallStatus;
import com.DatabaseHelper;
import com.Helper;
import com.Adapters.CallsAdapter;
import com.Icon_Manager;
import com.model.Model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class ActivityCalls extends FragmentActivity {

    Helper helper ;
    Context ctx;
TextView lblcount;
    DatabaseHelper db;
    ListView myList;
    LocationManager manager = null;
    boolean result = false;
    private EditText mSearchEdt;
    CallsAdapter callsAdapter; //to refresh the list
    ArrayList<Call> data2 = new ArrayList<Call>() ;
    private TextWatcher mSearchTw;
    private  String ascOrDesc = "asc";
    private  String sortState = "CallID";

    String s = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("mytag","fdgsaghgh");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        ctx = this;
        db = DatabaseHelper.getInstance(getApplicationContext());
        helper= new Helper();
        mSearchEdt = (EditText) findViewById(R.id.mSearchEdt);
        lblcount = (TextView) findViewById(R.id.lblcount);
        TextView lblcallhistory = (TextView) findViewById(R.id.lblcallhistory);
        Icon_Manager icon_manager = new Icon_Manager();

        final Spinner dynamicSpinner = (Spinner) findViewById(R.id.spinner);
        String[] items = new String[] { "מס' קריאה","מס' קריאה desc" ,"פתיחת קריאה", "Black Tea" };
        List<CallStatus> statusList = new ArrayList<CallStatus>();
        statusList = DatabaseHelper.getInstance(this).getCallStausList();
        String[] items1 = new String[statusList.size()];
        for (int i = 0; i < statusList.size(); i++) {
            items1[i] = statusList.get(i).getCallStatusName();
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dynamicSpinner.setAdapter(adapter);
        int selectionPosition = adapter.getPosition("מס' קריאה");
        dynamicSpinner.setSelection(selectionPosition);



        lblcallhistory.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        lblcallhistory.setTextSize(30);
        lblcallhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityWebView.class);
                Bundle b = new Bundle();
                b.putInt("callid", 0);
                b.putInt("cid", 0);
                b.putInt("technicianid", Integer.valueOf(db.getValueByKey("CID")));
                Log.e("mytag","cid="+db.getValueByKey("CID"));
                b.putString("action","mycalls");
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        Model.getInstance().Async_Wz_Calls_List_Listener(getApplicationContext(),helper.getMacAddr(), -2, new Model.Wz_Calls_List_Listener() {
            @Override
            public void onResult(String str) {
                Log.e("mytag","arrived here");

                //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                //helper.goToCallsFragment(context);
                data2.clear();
                for (Call c : getCallsList("")){
                    data2.add(c);
                }
                lblcount.setText(" נמצאו " + String.valueOf(data2.size()) + " קריאות ");
                myList = (ListView) findViewById(R.id.calls_list);

                callsAdapter=new CallsAdapter(data2,getBaseContext());
                myList.setAdapter(callsAdapter);
            }
        });

        Helper helper = new Helper();
        String mac = helper.getMacAddr();



        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String s = "";
                s=(String) parent.getItemAtPosition(position);
                if (s.equals("מס' קריאה")){
                    if (sortState.equals("CallID")){
                        if ( ascOrDesc.equals("desc")){
                            sortState = "CallID";
                            ascOrDesc = "asc";
                            getFilteredList("CallID asc");
                            Log.e("mytag","sortState:" + sortState + "  ascOrDesc:" + ascOrDesc);
                        }else{
                            sortState = "CallID";
                            ascOrDesc = "desc";
                            getFilteredList("CallID desc");
                            Log.e("mytag","sortState:" + sortState + "  ascOrDesc:" + ascOrDesc);
                        }

                    }
                }else if(s.equals("מס' קריאה desc")){
                    getFilteredList("CallID desc");

                }else if(s.equals("פתיחת קריאה")){
                    getFilteredList("CreateDate");
                }
                //comparatorCallsByCcompany

                //s = String.valueOf(db.getCallStatusByCallStatusName((String) parent.getItemAtPosition(position)).getCallStatusID());
                //statusID = db.getCallStatusByCallStatusName((String) parent.getItemAtPosition(position)).getCallStatusID();
                //statusName = db.getCallStatusByCallStatusName((String) parent.getItemAtPosition(position)).getCallStatusName();
                //Toast.makeText(getApplication(), "status: " + s, Toast.LENGTH_LONG).show();
                //Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("mytag","no");
                // TODO Auto-generated method stub
            }
        });

        mSearchTw=new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                callsAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        };
        mSearchEdt.addTextChangedListener(mSearchTw);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Toast.makeText(getBaseContext(),"onRestart", Toast.LENGTH_SHORT).show();
        Log.e("mytag","onRestart");
        change();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("mytag","onResume");
        //Toast.makeText(getBaseContext(),"onResume", Toast.LENGTH_SHORT).show();

        change();
    }
    public  void change(){

        Model.getInstance().Async_Wz_Calls_List_Listener(getApplicationContext(),helper.getMacAddr(), -2, new Model.Wz_Calls_List_Listener() {
            @Override
            public void onResult(String str) {
                ArrayList<Call> arrlistofOptions = new ArrayList<Call>(getCallsList(""));
                data2.clear();
                data2.addAll(arrlistofOptions);
                callsAdapter=new CallsAdapter(data2,getBaseContext());
                myList = (ListView) findViewById(R.id.calls_list);
                myList.setAdapter(callsAdapter);
                callsAdapter.notifyDataSetChanged();
                lblcount.setText(" נמצאו " + String.valueOf(data2.size()) + " קריאות ");
            }
        });
    }
    public void getFilteredList(String orderby){
        ArrayList<Call> arrlistofOptions = new ArrayList<Call>(getCallsList(orderby));
        data2.clear();
        data2.addAll(arrlistofOptions);
        callsAdapter=new CallsAdapter(data2,getBaseContext());
        myList = (ListView) findViewById(R.id.calls_list);
        myList.setAdapter(callsAdapter);
        callsAdapter.notifyDataSetChanged();
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

    private List<Call> getCallsList(String sortby){
        JSONObject j = null;
        int length = 0;

        List<Call> calls = new ArrayList<Call>() ;
        try {
            calls= DatabaseHelper.getInstance(this).getCalls(sortby);
            length = calls.size();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return calls;
    }
}
