package com.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.ActivityWebView;
import com.Activities.MenuActivity;
import com.Activities.R;
import com.Adapters.ActionsAdapter;
import com.Adapters.ActionsAdapter2;
import com.Classes.Favorite;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentActions2 extends android.support.v4.app.Fragment {


    EditText key_et, val_et;
    Button addmem_btn, remove_btn;
    DatabaseHelper db;
    ListView myList;
    List<IS_Action> data2 = new ArrayList<IS_Action>();
    String dataName;
    CheckBox cb;
    private ActionsAdapter actionsAdapter;
    LocationManager manager = null;
    String firstname = "";
    String lastname = "";
    boolean result = false;
    LinearLayout layout;
    CheckBox chk_actions_today;
    Helper helper;
    private EditText mSearchEdt;
    private TextWatcher mSearchTw;
    private TextView lblsearch;
    Icon_Manager iconManager;
    private FloatingActionButton fab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iconManager = new Icon_Manager();
        helper = new Helper();
        View v = inflater.inflate(R.layout.fragment_actions2, container, false);
        setHasOptionsMenu(true);
        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();
        ((MenuActivity) getActivity()).turnActionBarMissionsIconOn();

        db = DatabaseHelper.getInstance(getContext());
        chk_actions_today = (CheckBox) v.findViewById(R.id.chk_actions_today);
        lblsearch = (TextView)  v.findViewById(R.id.lblsearch);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        mSearchEdt = (EditText) v.findViewById(R.id.mSearchEdt);
        myList = (ListView) v.findViewById(R.id.actions_list);
        helper.sendChangedActionsToWizenet(getContext());
        setLblSearch();
        setChkActionsToday();
        setFloutingButton();
        setTextWatcher();

        refresh();
        return v;
    }

    private void setTextWatcher(){
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
    }
    private void setFloutingButton(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.goToFragmentCreateAction(getContext());
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
                b.putString("specialurl", "iframe.aspx?control=modulesProjects/mytasks&mobile=1");
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
    private void setChkActionsToday(){

        chk_actions_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chk_actions_today.isChecked()){
                    refresh();
                }else{
                    String dateWithFormat = helper.getDate("yyyy-MM-dd");
                    refresh(" and actionSdate like '%"+ dateWithFormat +"%'");
                }
            }
        });
    }
    public void refresh(String param){
        chkIfOfflineActionsAndSendItToWizenet(); // send offline actions
        helper.sendAsyncActionsTime(getContext()); // send offline actionsTimes
        data2.clear();
        data2=getActionsList(param);
        actionsAdapter=new ActionsAdapter(data2,getContext(),FragmentActions2.this);
        myList.setAdapter(actionsAdapter);
        actionsAdapter.notifyDataSetChanged();
        retNewActions();
    }
    public void refresh(){
        chkIfOfflineActionsAndSendItToWizenet(); // send offline actions
        helper.sendAsyncActionsTime(getContext()); // send offline actionsTimes
        data2.clear();
        String additional = "";
        String dateWithFormat = helper.getDate("yyyy-MM-dd");
        //if(chk_actions_today.isChecked()) {
        //
        //    additional =" and actionSdate like '%" + dateWithFormat + "%'";
        //}
        data2=getActionsList(additional);
        data2= chk_actions_today.isChecked()==true? getActionsList(" and actionSdate like '%"+ dateWithFormat +"%'"):getActionsList("") ;
        actionsAdapter=new ActionsAdapter(data2,getContext(),FragmentActions2.this);
        myList.setAdapter(actionsAdapter);
        actionsAdapter.notifyDataSetChanged();
        retNewActions();
    }
    private void retNewActions(){
        if (helper.isNetworkAvailable(getContext())){
            try{
                Model.getInstance().Async_Wz_ACTIONS_retList_Listener(helper.getMacAddr(getContext()), new Model.Wz_ACTIONS_retList_Listener() {
                    @Override
                    public void onResult(String str) {
                        data2.clear();
                        String dateWithFormat = helper.getDate("yyyy-MM-dd");
                        data2= chk_actions_today.isChecked()==true? getActionsList(" and actionSdate like '%"+ dateWithFormat +"%'"):getActionsList("") ;
                        actionsAdapter=new ActionsAdapter(data2,getContext(),FragmentActions2.this);
                        myList.setAdapter(actionsAdapter);
                        actionsAdapter.notifyDataSetChanged();
                    }
                });
            }catch(Exception e){
                helper.LogPrintExStackTrace(e);
            }
        }

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
    public void chkIfOfflineActionsAndSendItToWizenet(){
        String json = "";
        json = DatabaseHelper.getInstance(getContext()).getJsonResultsFromTable("IS_Actions_Offline").toString();
        if ((!(json.contains("[]"))) && helper.isNetworkAvailable(getContext()) == true){ //if id < 0 it means there are offline rows
                try{
                    Model.getInstance().Async_Wz_createISAction(helper.getMacAddr(getContext()), json, new Model.Wz_createISAction_Listener() {
                        @Override
                        public void onResult(String str) {
                            if (str.contains("0"))
                                DatabaseHelper.getInstance(getContext()).delete_IS_Actions_Rows("offline");//delete from db
                        }
                    });
                }catch(Exception e){helper.LogPrintExStackTrace(e);}
        }
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
        } catch (Exception e) {
            Log.e("mytag","get 0, exception " +e.getMessage());
            helper.LogPrintExStackTrace(e);
        }
        Log.e("mytag","getFirstActionID():" + String.valueOf(ret) );
        return ret;
    }

    private List<IS_Status> getISStatusList(){
        List<IS_Status> list = new ArrayList<IS_Status>() ;
        JSONArray jarray = null;
        try {
            String strJson = "";
            File_ f = new File_();
            strJson = f.readFromFileExternal(getContext(),"is_status.txt");
            jarray =  new JSONArray(strJson);
            //isStatus_map = new HashMap<String, String>(jarray.length());

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
                //isStatus_map.put(e.getString("statusName"),e.getString("statusID"));
                list.add(new IS_Status(e.getString("statusID"),e.getString("statusName")));
            } catch (JSONException e1) {
                helper.LogPrintExStackTrace(e1);
                e1.printStackTrace();
                return list;
            }
        }
        //for (String s:isStatus_map.keySet()){
        //    Log.e("mytag",s);
        //}
        return list;
    }

    ;
//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        MenuItem item = menu.findItem(R.menu.menu_main);
//        item.setVisible(false);
//    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setEnabled(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }





}
