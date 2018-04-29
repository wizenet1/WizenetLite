package com.Fragments;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.os.Bundle;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.MenuActivity;
import com.Activities.R;
import com.Adapters.ActionsAdapter;
import com.Adapters.CallsAdapter;
import com.Classes.Call;
import com.Classes.IS_Action;
import com.Classes.Message;
import com.DatabaseHelper;
import com.Helper;
import com.model.Model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_actions, null);
        //setHasOptionsMenu(true);
        mSearchEdt = (EditText) v.findViewById(R.id.mSearchEdt);
        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);
        helper= new Helper();
        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnActionBarMissionsIconOn();

        db = DatabaseHelper.getInstance(getContext());
        Helper h = new Helper();

        myList = (ListView) v.findViewById(R.id.actions_list);
        myList.setClickable(true);
        myList.setLongClickable(true);

        if (helper.isNetworkAvailable(getContext())){
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Loading... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            chkIfOfflineActionsAndSendItToWizenet();
            Model.getInstance().Async_Wz_ACTIONS_retList_Listener(h.getMacAddr(), new Model.Wz_ACTIONS_retList_Listener() {
                @Override
                public void onResult(String str) {
                    refresh();
                    pDialog.dismiss();
                    Toast.makeText(getContext(), "received is_actions ", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            refresh();
        }

        //refresh();
        //adapter = new CustomAdapter();
        //myList.setAdapter(adapter);
        //myList.setBackgroundColor(Color.parseColor("#cdebf9"));
        //((MenuActivity)getActivity()).initialIcons();
        //ImageView message = (ImageView)getActivity().findViewById(R.id.arrows);
        //message.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(getActivity().getApplicationContext(),FragmentMessageDetails.class);
                //intent.putExtra("id",data2.get(position).getMsgID());
                //goToMSGDetailsFrag(data2.get(position).getMsgID().toString());
                //Toast.makeText(getActivity(), data2.get(position).getMsgID(), Toast.LENGTH_LONG).show();
                //startActivity(intent);
            }
        });

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

        int firstActionID = getFirstActionID();//chk if offline rows > 0
        //send it to wizenet like in openISAction
        if (firstActionID < 0){ //if id < 0 it means there are offline rows
            String json = "";
            json = DatabaseHelper.getInstance(getContext()).getJsonResultsFromTable("IS_Actions_Offline").toString();
            try{
                Model.getInstance().Async_Wz_createISAction(helper.getMacAddr(), json, new Model.Wz_getTasks_Listener() {
                    @Override
                    public void onResult(String str) {
                        if (str.contains("0")){
                            Model.getInstance().Async_Wz_ACTIONS_retList_Listener(helper.getMacAddr(), new Model.Wz_ACTIONS_retList_Listener() {
                                @Override
                                public void onResult(String str) {
                                    refresh();
                                    Toast.makeText(getContext(), "success to add is_actions ", Toast.LENGTH_LONG).show();
                                }
                            });
                            pDialog.dismiss();
                        }else{
                            pDialog.dismiss();
                        }
                    }
                });
            }catch(Exception e){
                pDialog.dismiss();
            }
        }
        DatabaseHelper.getInstance(getContext()).delete_IS_Actions_Rows("offline");//delete from db
    }
    public int getFirstActionID(){
        int ret = 0;
        List<IS_Action> actions = new ArrayList<IS_Action>() ;
        try {
            actions= DatabaseHelper.getInstance(getContext()).getISActions("top1");
            for (IS_Action a:actions) {
                ret = a.getActionID();
            }
            if (ret > 0){
                ret = 0;
            }else{

            }
        } catch (Exception e) {
            helper.LogPrintExStackTrace(e);
        }
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





    public void refresh(){
        //List<IS_Action> data = new ArrayList<IS_Action>() ;
        //List<IS_Action> cps=  db.getISActions("");  // getCustomersFromJson(myBundle);

        data2.clear();
        data2=getActionsList("");
        for (IS_Action a:data2) {
            Log.e("mytag", String.valueOf(a.getActionID()) +  " : " + a.getActionDesc());
        }
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
