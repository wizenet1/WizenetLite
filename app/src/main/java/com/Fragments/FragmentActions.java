package com.Fragments;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.ActivityWebView;
import com.Activities.MenuActivity;
import com.Activities.R;
import com.Adapters.ActionsAdapter;
import com.Adapters.CallsAdapter;
import com.Classes.Call;
import com.Classes.Ccustomer;
import com.Classes.Ctype;
import com.Classes.IS_Action;
import com.Classes.IS_Status;
import com.Classes.Message;
import com.DatabaseHelper;
import com.File_;
import com.Helper;
import com.Icon_Manager;
import com.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentActions extends android.support.v4.app.Fragment {


    EditText key_et, val_et;
    Button addmem_btn,remove_btn;
    DatabaseHelper db;
    ListView myList;
    //CustomAdapter adapter;
    List<IS_Action> data2 = new ArrayList<IS_Action>() ;
    String dataName;
    CheckBox cb;
    LocationManager manager = null;
    String firstname="";
    String lastname="";
    boolean result = false;
    private EditText mSearchEdt;
    private TextWatcher mSearchTw;
    Helper helper;
    private ProgressDialog pDialog;
    private ActionsAdapter actionsAdapter;
    private FloatingActionButton fab;
    public Map<String, String> isStatus_map;
    private TextView lblsearch;
    public String[] is_status_list;
    Icon_Manager iconManager;
    CheckBox chk_actions_today;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        iconManager = new Icon_Manager();
        View v = inflater.inflate(R.layout.fragment_actions, null);
        //setHasOptionsMenu(true);
        mSearchEdt = (EditText) v.findViewById(R.id.mSearchEdt);
        lblsearch = (TextView)  v.findViewById(R.id.lblsearch);
        setLblSearch();
        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);
        helper= new Helper();
        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnActionBarMissionsIconOn();

        db = DatabaseHelper.getInstance(getContext());
        Helper h = new Helper();
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        chk_actions_today = (CheckBox) v.findViewById(R.id.chk_actions_today);
        setChkActionsToday();
        setFloutingButton();
        getISStatusList();
        myList = (ListView) v.findViewById(R.id.actions_list);
        myList.setClickable(true);
        myList.setLongClickable(true);

        if (helper.isNetworkAvailable(getContext())){
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Loading... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            //1. send new actions to wizenet
            //2. send actionstimes to wizenet
            chkIfOfflineActionsAndSendItToWizenet();
            h.sendAsyncActionsTime(getContext());

            Model.getInstance().Async_Wz_ACTIONS_retList_Listener(h.getMacAddr(), new Model.Wz_ACTIONS_retList_Listener() {
                @Override
                public void onResult(String str) {
                    refresh();
                    pDialog.dismiss();
                    Log.e("mytag","received is_actions ");
                    //Toast.makeText(getContext(), "received is_actions ", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            refresh();
        }

        mSearchTw=new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actionsAdapter.getFilter().filter(s);
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
        return v;
    };
    private void setChkActionsToday(){

        chk_actions_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(!chk_actions_today.isChecked()){
                //cancel
                refresh();
                Log.e("myTag","stop APPS_CALLS_SUMMARY");
            }else{
                //active
                //SELECT * FROM IS_Actions where 1=1
                String dateWithFormat = helper.getDate("yyyy-MM-dd");
                refresh(" and actionSdate like '%"+ dateWithFormat +"%'");
                Log.e("myTag","start APPS_CALLS_SUMMARY");
                //}
            }
            }
        });
    }
    private void setLblSearch(){
        lblsearch.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", getContext()));
        lblsearch.setTextSize(30);
        lblsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //iframe.aspx?control=modulesProjects/mytasks
                Intent intent = new Intent(getContext(), ActivityWebView.class);
                Bundle b = new Bundle();
                b.putInt("callid", -1);
                b.putInt("cid", -1);
                b.putInt("technicianid", Integer.parseInt(String.valueOf(DatabaseHelper.getInstance(getContext()).getValueByKey("CID"))));
                b.putString("action", "dynamic");
                b.putString("specialurl", "iframe.aspx?control=modulesProjects/mytasks");
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
    private void setFloutingButton(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.goToFragmentCreateAction(getContext());
            }
        });
    }
    private List<IS_Action> getActionsList(String sortby){

        JSONObject j = null;
        int length = 0;

        List<IS_Action> actions = new ArrayList<IS_Action>() ;
        try {
            actions= DatabaseHelper.getInstance(getContext()).getISActions(sortby);
            length = actions.size();
            Log.e("mytag","chk is_actions length: " +length);
        } catch (Exception e) {
            Log.e("mytag","sdf " +e.getMessage());
            e.printStackTrace();
            helper.LogPrintExStackTrace(e);
        }

        return actions;
    }
    public Map<String,String> getIS_Status(){
        return this.isStatus_map;
    }
   //public String[] getIS_StatusArray(){
   //    String[] arraySpinner = new String[] {
   //            "שינוי סטטוס",""
   //    };
   //    List<IS_Status> is_statusList = new ArrayList<IS_Status>();
   //    is_statusList = getISStatusList();
   //    String[] items1 = new String[is_statusList.size()+1];
   //    for (int i = 0; i < is_statusList.size(); i++) {
   //        items1[i+1] = is_statusList.get(i).getStatusName();
   //        //Log.e("mytag",ctypeList.get(i).getCtypeName());
   //    }
   //    for (String s:items1){
   //        Log.e("mytag",s);
   //    }
   //    return items1;
   //}
    private List<IS_Status> getISStatusList(){
        List<IS_Status> list = new ArrayList<IS_Status>() ;
        JSONArray jarray = null;
        try {
            String strJson = "";
            File_ f = new File_();
            strJson = f.readFromFileExternal(getContext(),"is_status.txt");
            jarray =  new JSONArray(strJson);
            isStatus_map = new HashMap<String, String>(jarray.length());

        } catch (JSONException e) {
            helper.LogPrintExStackTrace(e);
            Toast.makeText(getContext(), "hello", Toast.LENGTH_LONG).show();
            return list;
        }
        for (int i = 0; i < jarray.length(); i++) {
            final JSONObject e;
            String name = "";
            try {
                e = jarray.getJSONObject(i);
                    isStatus_map.put(e.getString("statusName"),e.getString("statusID"));
                    list.add(new IS_Status(e.getString("statusID"),e.getString("statusName")));
            } catch (JSONException e1) {
                helper.LogPrintExStackTrace(e1);
                e1.printStackTrace();
                return list;
            }
        }
        for (String s:isStatus_map.keySet()){
            Log.e("mytag",s);

        }
        return list;
    }


    public void chkIfOfflineActionsAndSendItToWizenet(){

        int firstActionID = getFirstActionID();//chk if offline rows > 0
        //send it to wizenet like in openISAction
        Log.e("mytag","firstActionID: " +firstActionID);
        String json = "";
        json = DatabaseHelper.getInstance(getContext()).getJsonResultsFromTable("IS_Actions_Offline").toString();
        Log.e("mytag","json.contains([])?: " +json.contains("[]"));
        //if (!(firstActionID>0) || json.contains("[]")){ //if id < 0 it means there are offline rows
        if (!(json.contains("[]"))){ //if id < 0 it means there are offline rows
            Log.e("mytag","inside the function");
            //Log.e("mytag","json sent from Async_Wz_createISAction: " +json);
            try{
                Model.getInstance().Async_Wz_createISAction(helper.getMacAddr(), json, new Model.Wz_createISAction_Listener() {
                    @Override
                    public void onResult(String str) {
                        if (str.contains("0")){
                            Log.e("mytag","offline actions created--------------------------------------------------");
                            boolean flag = DatabaseHelper.getInstance(getContext()).delete_IS_Actions_Rows("offline");//delete from db
                            Log.e("mytag","offline actions deleted from db after sent?  "+ flag);
                            returnListAndRefresh();
                            pDialog.dismiss();
                        }else{
                            pDialog.dismiss();
                        }
                    }
                });
            }catch(Exception e){
                pDialog.dismiss();
            }
        }else{
            returnListAndRefresh();
        }

    }
    public void returnListAndRefresh(){
        Model.getInstance().Async_Wz_ACTIONS_retList_Listener(helper.getMacAddr(), new Model.Wz_ACTIONS_retList_Listener() {
            @Override
            public void onResult(String str) {
                refresh();
                //Log.e("mytag","arrived new actions");
                Log.e("mytag","success to add is_actions from ws");
                //Toast.makeText(getContext(), "success to add is_actions ", Toast.LENGTH_LONG).show();
            }
        });
    }
    public int getFirstActionID(){
            int ret = 0;

            List<IS_Action> actions = new ArrayList<IS_Action>() ;
            try {
                actions= DatabaseHelper.getInstance(getContext()).getISActions("top1");
                for (IS_Action a:actions) {
                    //Log.e("mytag","list " +a.getActionID());
                    ret = a.getActionID();
                }
                if (ret > 0){
                    //return ret;
                }else{
                    ret = ret;
                }
            } catch (Exception e) {
                Log.e("mytag","sdf " +e.getMessage());
                helper.LogPrintExStackTrace(e);
            }
            Log.e("mytag","getFirstActionID():" + String.valueOf(ret) );
            return ret;
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//                    getFragmentManager().popBackStack();
//                    // handle back button's click listener
//                    return true;
//                }

                return false;
            }
        });

        //Turn the action bar customers icon on, and the rest off.
        ((MenuActivity) getActivity()).turnActionBarMissionsIconOn();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        for(int i=0; i<menu.size(); i++){
            menu.getItem(i).setEnabled(false);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }




    public void refresh(String param){
        data2.clear();
        data2=getActionsList(param);
        actionsAdapter=new ActionsAdapter(data2,getContext(),FragmentActions.this);
        myList.setAdapter(actionsAdapter);
        actionsAdapter.notifyDataSetChanged();
    }
    public void refresh(){
        data2.clear();
        String additional = "";
        if(chk_actions_today.isChecked()) {
            String dateWithFormat = helper.getDate("yyyy-MM-dd");
            additional =" and actionSdate like '%" + dateWithFormat + "%'";

        }

        data2=getActionsList(additional);
        actionsAdapter=new ActionsAdapter(data2,getContext(),FragmentActions.this);
        myList.setAdapter(actionsAdapter);
        actionsAdapter.notifyDataSetChanged();
    }

    private void goToMSGDetailsFrag(String puId)

    {

        FragmentMessageDetails fr = new FragmentMessageDetails();
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();



        Bundle bundle = new Bundle();
        //bundle.putString("receiver", dataName);
        bundle.putString("puId",puId);

        fr.setArguments(bundle);

        ft.replace(R.id.container,fr,"FragmentMessageDetails");
        ft.addToBackStack("FragmentMessageDetails");
        ft.commit();
    }



}
