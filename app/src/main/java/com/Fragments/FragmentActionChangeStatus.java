package com.Fragments;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.ActivityWebView;
import com.Activities.MenuActivity;
import com.Activities.R;
import com.Adapters.ActionsAdapter;
import com.Classes.Ccustomer;
import com.Classes.Ctype;
import com.Classes.IS_Action;
import com.Classes.IS_Project;
import com.Classes.IS_Status;
import com.Classes.IS_Task;
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
import java.util.Locale;
import java.util.Map;

public class FragmentActionChangeStatus extends android.support.v4.app.Fragment {

    String selected="";
    String statusID = "";
    String statusName = "";

    Helper helper;
    Spinner dynamicSpinner;
    private ActionsAdapter actionsAdapter;
    Button btn_add;
    Map<String, String> isStatus_map;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.change_status, null);
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);
        helper= new Helper();
        final TextView txt_actionid = (TextView) v.findViewById(R.id.txt_actionid);
        Bundle bundle = getArguments();
        try{
            String title = bundle.getString("actionid");
            txt_actionid.setText(title);
        }catch(Exception e){
        }


        btn_add = (Button) v.findViewById(R.id.btn_add);
        dynamicSpinner = (Spinner) v.findViewById(R.id.spinner_ctype);

        setCtypeSpinner();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IS_Action action = new IS_Action();
                action.setActionID(Integer.valueOf(txt_actionid.getText().toString().trim()));
                action.setStatusID(Integer.valueOf(selected));
                action.setStatusName(statusName.trim());
                action.setExpr1("0");
                Log.e("mytag","doron test: " +action.toString());
                boolean flag = DatabaseHelper.getInstance(getContext()).updateISAction(action);
                if (flag == true){
                    Log.e("mytag","action " + txt_actionid.getText().toString() + " changed");

                    getFragmentManager().popBackStack();
                }

            }
        });
        return v;
    };







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
            return list;
        }
        for (int i = 0; i < jarray.length(); i++) {
            final JSONObject e;
            try {
                e = jarray.getJSONObject(i);
                list.add(new IS_Status(e.getString("statusID"),e.getString("statusName")));
                isStatus_map.put(e.getString("statusName"),e.getString("statusID"));
            } catch (JSONException e1) {
                helper.LogPrintExStackTrace(e1);
                return list;
            }
        }
        return list;
    }
    private void setCtypeSpinner(){
        try{
            List<IS_Status> ctypeList = new ArrayList<IS_Status>();

            ctypeList = getISStatusList();
            String[] items1 = new String[ctypeList.size()+1];
            for (int i = 0; i < ctypeList.size(); i++) {
                items1[i+1] = ctypeList.get(i).getStatusName();
            }
            items1[0]="בחר";
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items1);
            dynamicSpinner.setAdapter(adapter);
            int selectionPosition = adapter.getPosition("");
            dynamicSpinner.setSelection(selectionPosition);
            dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    //String s = "";
                    //s = (String) parent.getItemAtPosition(position);
                    try{
                        String s = "";
                        //Log.e("mytag","(String) parent.getItemAtPosition(position):"+(String)  parent.getItemAtPosition(position).toString().trim());
                        if (parent.getItemAtPosition(position).toString().trim().equals("בחר")){
                            s = "-1";
                        }else{
                            s = String.valueOf(isStatus_map.get(parent.getItemAtPosition(position).toString().trim()));
                            selected = s;
                            statusName = parent.getItemAtPosition(position).toString().trim();
                            Log.e("mytag",s);
                        }
                    }catch(Exception e){
                        helper.LogPrintExStackTrace(e);
                    }
                    Log.e("mytag", (String) parent.getItemAtPosition(position));
                    //getFragmentManager().popBackStack();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });



        }catch (Exception e){

        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
////                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
////                    getFragmentManager().popBackStack();
////                    // handle back button's click listener
////                    return true;
////                }
//
//                return false;
//            }
//        });
//
//        //Turn the action bar customers icon on, and the rest off.
//        ((MenuActivity) getActivity()).turnActionBarMissionsIconOn();
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





}
