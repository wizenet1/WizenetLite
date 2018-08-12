package com.Activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Classes.Call;
import com.Classes.CallStatus;
import com.Classes.Call_offline;
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
    private CheckBox chk_calls_today,chk_calls_work;
    private TextView lblopencall,lblpriority,lblcalltype;
    String ss = "";
    Icon_Manager icon_manager;
    String s = "";
    Bundle extras;
    String condition = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //connect between behind and front
        setContentView(R.layout.activity_call);

        ctx = this;

        //call DB class
        db = DatabaseHelper.getInstance(getApplicationContext());
        //this class for general
        helper= new Helper();

        //varibles defenitions
        lblopencall = (TextView)   findViewById(R.id.lblopencall);
        chk_calls_today = (CheckBox) findViewById(R.id.chk_calls_today);
        chk_calls_work = (CheckBox) findViewById(R.id.chk_calls_work);
        mSearchEdt = (EditText) findViewById(R.id.mSearchEdt);
        lblcount = (TextView) findViewById(R.id.lblcount);
        TextView lblcallhistory = (TextView) findViewById(R.id.lblcallhistory);
        //this class to translate to fonts.
        icon_manager = new Icon_Manager();

        //delay to wait the async task 2.5 secs
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                helper.transferJsonCallTime(ctx);
            }
        }, 2500);
        //-------------------------------------
        //spinner = dropdown
        final Spinner spinner =(Spinner) findViewById(R.id.spinner);
        String[] items = {"מס' קריאה ↑","מס' קריאה ↓" ,"פתיחת קריאה ↑","פתיחת קריאה ↓","עדיפות ↑","עדיפות ↓","עיר ↑","עיר ↓","חברה ↑","חברה ↓","מס סריאלי ↑","מס סריאלי ↓","שיבוץ ↑","שיבוץ ↓"};
        spinner.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_item, items));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Log.e("mytag",(String) parent.getItemAtPosition(position));
                String s=(String) parent.getItemAtPosition(position);
                if (s.equals("מס' קריאה ↑")){
                    getFilteredList(condition+"CallID asc");
                }else if(s.equals("מס' קריאה ↓")){
                    getFilteredList(condition+"CallID desc");
                }else if(s.equals("פתיחת קריאה ↓")){
                    getFilteredList(condition+"CreateDate desc");
                }else if(s.equals("פתיחת קריאה ↑")){
                    getFilteredList(condition+"CreateDate asc");
                }else if(s.equals("עדיפות ↑")){
                    getFilteredList(condition+"OriginName asc");
                }else if(s.equals("עדיפות ↓")){
                    getFilteredList(condition+"OriginName desc");
                }else if(s.equals("עיר ↑")){
                    getFilteredList(condition+"Ccity asc");
                }else if(s.equals("עיר ↓")){
                    getFilteredList(condition+"Ccity desc");
                }else if(s.equals("חברה ↑")){
                    getFilteredList(condition+"Ccompany asc");
                }else if(s.equals("חברה ↓")){
                    getFilteredList(condition+"Ccompany desc");
                }else if(s.equals("מס סריאלי ↑")){
                    getFilteredList(condition+"internalSN asc");
                }else if(s.equals("מס סריאלי ↓")){
                    getFilteredList(condition+"internalSN desc");
                }else if(s.equals("שיבוץ ↑")){
                    getFilteredList(condition+"callStartTime asc");
                }else if(s.equals("שיבוץ ↓")){
                    getFilteredList(condition+"callStartTime desc");

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        //setTypeFace - set the icon for label.
        lblcallhistory.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        lblcallhistory.setTextSize(30);
        //move to webView frame send parameters to
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
        setChkShibutz();
        setlblopencall();
        setChkWork();
    }
    private void setlblopencall(){
        lblopencall.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", this));
        lblopencall.setTextSize(30);
        lblopencall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityWebView.class);
                Bundle b = new Bundle();
                b.putInt("callid", -1);
                b.putInt("cid", -1);
                b.putInt("technicianid", Integer.parseInt(String.valueOf(DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("CID"))));
                b.putString("action", "dynamic");
                b.putString("specialurl", "/mobile/control.aspx?control=modulesService/newCall");
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

   private void setChkShibutz(){
       chk_calls_today.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               change();
           }
       });
   }
    private void setChkWork(){
        chk_calls_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                change();
            }
        });
    }
//region notinuse
    //endregion

    private void init(){
        data2.clear();
        for (Call c : getCallsList("")){
            Log.e("mytag",c.toString());
            data2.add(c);
        }
        lblcount.setText(" נמצאו " + String.valueOf(data2.size()) + " קריאות ");

        myList = (ListView) findViewById(R.id.calls_list);
        callsAdapter=new CallsAdapter(data2,getBaseContext());
        myList.setAdapter(callsAdapter);
    }

    public class SpinnerAdapter extends ArrayAdapter<String>
    {
        String[] objects;
        public SpinnerAdapter(Context context, int textViewResourceId, String[] objects)
        {
            super(context, textViewResourceId, objects);
            this.objects=objects;
        }
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent)
        {
            return getCustomView(position, convertView, parent);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
        public View getCustomView(final int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.simple_spinner_item, parent, false);
            final TextView label=(TextView)row.findViewById(R.id.tv_spinnervalue);
            label.setText(objects[position]);
            return row;
        }
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        try{
            ss = (extras != null)? extras.getString("choose") : "";
            Log.e("mytag"," myss :" + ss.toString());
        }catch(Exception ex){
           // Log.e("mytag"," asdasd :" + ex.getMessage());
        }
        try{
            if (ss.contains("total")){
                condition = "  order by ";
            }else if(ss.contains("open")){
                condition = " and CAST(sla AS INTEGER) >= 0  order by callid";
            }else if(ss.contains("sla")){
                condition = " and CAST(sla AS INTEGER) < 0 order by callid";
            }else{
                condition = " order by  " ;
            }
        }catch(Exception ex){
            Log.e("mytag"," asdasd :" + ex.getMessage());
        }
        //Log.e("mytag","condition: " + condition);
        ArrayList<Call_offline> arr_Call_offline = new ArrayList<>();
        arr_Call_offline = initOnline();
        //first init the list from db! - always
        init();
        //check if there's any calls_offline and send them to wizenet if network works.
        //
        sendCallsOffline(ctx,arr_Call_offline);

    }
    private void sendCallsOffline(final Context ctx1,ArrayList<Call_offline> arr_Call_offline){
                    if (helper.isNetworkAvailable(ctx)){
                        Log.e("mytag","size: " + arr_Call_offline.size());
                        if (arr_Call_offline.size()>0){
                            Model.getInstance().Async_Wz_Send_Call_Offline_Listener(helper.getMacAddr(getApplicationContext()), DatabaseHelper.getInstance(ctx).getJsonResults().toString(), new Model.Wz_Send_Call_Offline_Listener() {
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
                            change();
            }
        }
    }
    private ArrayList<Call_offline> initOnline(){
        ArrayList<Call_offline> arr_Call_offline = new ArrayList<>();
        try{
            arr_Call_offline = new ArrayList<Call_offline>(DatabaseHelper.getInstance(ctx).getCall_offline());
            for (Call_offline co:arr_Call_offline) {
                Log.e("mytag","Call_offline: " + co.toString());
            }
            if (arr_Call_offline.size() > 0 ){
                Log.e("mytag","json: " + DatabaseHelper.getInstance(ctx).getJsonResults());

            }
        }catch (Exception e){
            helper.LogPrintExStackTrace(e);
            Log.e("mytag","11:" +e.getMessage());
        }
        return arr_Call_offline;
    }
    public  void change(){
        //first check if internet avilable.
        if (helper.isNetworkAvailable(ctx)){
            //
            Model.getInstance().Async_Wz_Calls_List_Listener(getApplicationContext(),helper.getMacAddr(getApplicationContext()), -2, new Model.Wz_Calls_List_Listener() {
                @Override
                public void onResult(String str) {
                    Log.e("mytag","ret calls from ws:"+ str);
                    ArrayList<Call> arrlistofOptions = (ss != null)? new ArrayList<Call>(getCallsList(setCondition(ss))): new ArrayList<Call>(getCallsList(""));
                    data2.clear();
                    data2.addAll(arrlistofOptions);
                    callsAdapter=new CallsAdapter(data2,getBaseContext());
                    myList = (ListView) findViewById(R.id.calls_list);
                    myList.setAdapter(callsAdapter);
                    callsAdapter.notifyDataSetChanged();
                    lblcount.setText(" נמצאו " + String.valueOf(data2.size()) + " קריאות ");
                }
            });
        }else{
            Toast.makeText(getBaseContext(),"אינטרנט לא זמין", Toast.LENGTH_SHORT).show();
        }
    }
    private  String setCondition(String ss1){
        String ret = "";
        if (ss1.contains("total")){
            ret = "  order by callid";
        }else if(ss1.contains("open")){
            ret = " and CAST(sla AS INTEGER) >=0 order by callid";
        }else if(ss1.contains("sla")){
            ret = " and  CAST(sla AS INTEGER) < 0  order by callid";
        }else{
            ret = "   " ;
        }
        return ret;
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
        //Log.e("mytag","step 1");
        JSONObject j = null;
        int length = 0;

        List<Call> calls = new ArrayList<Call>() ;
        try {
            if (chk_calls_today.isChecked() == true) {
                String date = helper.getDate("yyyy-MM-dd");
                calls = DatabaseHelper.getInstance(getApplicationContext()).getCalls("and callStartTime like '%" + date + "%' " + sortby);
            }else if (chk_calls_work.isChecked() == true){
                String callsInWork = DatabaseHelper.getInstance(getBaseContext()).getCallsInWork();
                calls = DatabaseHelper.getInstance(getApplicationContext()).getCalls("and callid in (" + callsInWork + ")");
            }else{
                calls= DatabaseHelper.getInstance(getApplicationContext()).getCalls(sortby);
            }
            length = calls.size();
            Log.e("mytag","calls.size: " +length);
        } catch (Exception e) {
            Log.e("mytag","sdf " +e.getMessage());
            e.printStackTrace();
            helper.LogPrintExStackTrace(e);
        }

        return calls;
    }
}
