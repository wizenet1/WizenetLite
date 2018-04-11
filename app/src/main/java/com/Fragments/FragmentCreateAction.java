package com.Fragments;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.Activities.MenuActivity;
import com.Activities.R;
import com.Adapters.ActionsAdapter;
import com.Classes.CallStatus;
import com.Classes.Ccustomer;
import com.Classes.Ctype;
import com.Classes.IS_Action;
import com.DatabaseHelper;
import com.File_;
import com.Helper;
import com.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 31/08/2016.
 */
public class FragmentCreateAction extends android.support.v4.app.Fragment {


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
    Spinner dynamicSpinner,dynamicSpinner2;
    private ActionsAdapter actionsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_create_actions, null);
        //setHasOptionsMenu(true);
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);
        helper= new Helper();
        //Turn all the action bar icons off to their original color.
        //((MenuActivity) getActivity()).turnActionBarMissionsIconOn();
        db = DatabaseHelper.getInstance(getContext());

        dynamicSpinner = (Spinner) v.findViewById(R.id.spinner);
        dynamicSpinner2 = (Spinner) v.findViewById(R.id.spinner2);

        setSpinners();




        return v;
    };
    private void setSpinners(){
        List<Ctype> ctypeList = new ArrayList<Ctype>();
        ctypeList = getCtypeList();
        List<Ccustomer> ccustomerList = new ArrayList<Ccustomer>();
        ccustomerList = getCcustomerList();

        String[] items1 = new String[ctypeList.size()+1];
        String[] items2 = new String[ccustomerList.size()+1];

        for (int i = 0; i < ctypeList.size(); i++) {
            items1[i] = ctypeList.get(i).getCtypeName();
            //Log.e("mytag",ctypeList.get(i).getCtypeName());
        }
        items1[ctypeList.size()]="";
        //----------customers -----------------
        for (int i = 0; i < ccustomerList.size(); i++) {
            items2[i] = ccustomerList.get(i).getCfname();
            //Log.e("mytag",ccustomerList.get(i).getCtypeName());
        }
        items2[ccustomerList.size()]="";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items1);
        dynamicSpinner.setAdapter(adapter);
        int selectionPosition = adapter.getPosition("");
        dynamicSpinner.setSelection(selectionPosition);
        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String s = "";
                s = String.valueOf(db.getCallStatusByCallStatusName((String) parent.getItemAtPosition(position)).getCallStatusID());
                //statusID = db.getCallStatusByCallStatusName((String) parent.getItemAtPosition(position)).getCallStatusID();
                //statusName = db.getCallStatusByCallStatusName((String) parent.getItemAtPosition(position)).getCallStatusName();
                //Toast.makeText(getApplication(), "status: " + s, Toast.LENGTH_LONG).show();
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items2);
        dynamicSpinner2.setAdapter(adapter2);
        int selectionPosition2 = adapter.getPosition("");
        dynamicSpinner2.setSelection(selectionPosition2);
        dynamicSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String s = "";
                s = String.valueOf(db.getCallStatusByCallStatusName((String) parent.getItemAtPosition(position)).getCallStatusID());
                //statusID = db.getCallStatusByCallStatusName((String) parent.getItemAtPosition(position)).getCallStatusID();
                //statusName = db.getCallStatusByCallStatusName((String) parent.getItemAtPosition(position)).getCallStatusName();
                //Toast.makeText(getApplication(), "status: " + s, Toast.LENGTH_LONG).show();
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }
    private List<Ccustomer> getCcustomerList(){
        List<Ccustomer> list = new ArrayList<Ccustomer>() ;
        JSONArray jarray = null;
        try {
            String strJson = "";
            File_ f = new File_();
            strJson = f.readFromFileExternal(getContext(),"sons.txt");
            jarray =  new JSONArray(strJson);
        } catch (JSONException e) {
            helper.LogPrintExStackTrace(e);
            e.printStackTrace();
            return list;
        }
        for (int i = 0; i < jarray.length(); i++) {
            final JSONObject e;
            String name = "";
            try {
                e = jarray.getJSONObject(i);
                Ccustomer c = new Ccustomer(e.getString("CID"),e.getString("Name"));
                list.add(c);
            } catch (JSONException e1) {
                helper.LogPrintExStackTrace(e1);
                e1.printStackTrace();
                return list;
            }
        }
        return list;
    }
    private List<Ctype> getCtypeList(){
        List<Ctype> list = new ArrayList<Ctype>() ;
        JSONArray jarray = null;
        try {
            String strJson = "";
            File_ f = new File_();
            strJson = f.readFromFileExternal(getContext(),"ctype.txt");
            jarray =  new JSONArray(strJson);
        } catch (JSONException e) {
            helper.LogPrintExStackTrace(e);
            e.printStackTrace();
            return list;
        }
        for (int i = 0; i < jarray.length(); i++) {
            final JSONObject e;
            String name = "";
            try {
                e = jarray.getJSONObject(i);
                Ctype c = new Ctype(e.getInt("CTypeID"),e.getString("CTypeName"));
                list.add(c);
            } catch (JSONException e1) {
                helper.LogPrintExStackTrace(e1);
                e1.printStackTrace();
                return list;
            }
        }
        return list;
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





    public void refresh(){
        //List<IS_Action> data = new ArrayList<IS_Action>() ;
        //List<IS_Action> cps=  db.getISActions("");  // getCustomersFromJson(myBundle);

        data2.clear();
        data2=getActionsList("");
        actionsAdapter=new ActionsAdapter(data2,getContext());
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
