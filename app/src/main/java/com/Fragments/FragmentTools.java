package com.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.Activities.ActivityWebView;
import com.Activities.MenuActivity;
import com.Activities.R;
import com.Classes.Favorite;
import com.Classes.Message;
import com.DatabaseHelper;
import com.Helper;
import com.Icon_Manager;
import com.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentTools extends android.support.v4.app.Fragment  {
    TextView tv;
    CheckBox cb;
    Button btn,btn2,mapid;
    EditText myEditText2;
    //BarCodeActivity barCodeActivity;
    Helper helper;
    TextView id1,id2,id3,id4,btn_reminders;
    DatabaseHelper db;
    Icon_Manager icon_manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        helper = new Helper();
        View v = inflater.inflate(R.layout.fragment_tools, container, false);
        setHasOptionsMenu(true);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        db = DatabaseHelper.getInstance(getContext());

        icon_manager = new Icon_Manager();
        helper = new Helper();

        id1 = (TextView) v.findViewById(R.id.id1);
        id2 = (TextView) v.findViewById(R.id.id2);
        id3 = (TextView) v.findViewById(R.id.id3);
        id4 = (TextView) v.findViewById(R.id.id4);
        btn_reminders= (TextView) v.findViewById(R.id.btn_reminders);


        id1.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));
        id2.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));
        id4.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));
        btn_reminders.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));


        id1.setTextSize(60);
        id2.setTextSize(60);
        id3.setTextSize(60);
        id4.setTextSize(60);
        btn_reminders.setTextSize(60);
        //lbldivuah.setTextSize(60);
        //id4.setTextSize(60);
        //btn_order.setTextSize(60);
        //idsyncproducts.setTextSize(60);

        id1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentNearestCustomersNew frag = new FragmentNearestCustomersNew();
                ft.replace(R.id.container, frag, "FragmentNearestCustomersNew");
                ft.addToBackStack("FragmentNearestCustomersNew");
                ft.commit();
                //Intent intent = new Intent(getContext(), ScannerActivity.class);
                //startActivity(intent);
            }
        });
        id3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.goToFragmentCreateAction(getContext());
            }
        });
        id2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                helper.goToFragmentSecret(getContext());
                //goTocustomers();
                //helper.goToCustomerFragment1(getContext());
            }
        });
        id4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                helper.goToFragmentAutoSMS(getContext());
                //goTocustomers();
                //helper.goToCustomerFragment1(getContext());
            }
        });
        btn_reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.goToMessagesFragment(getContext());
            }
        });

        return v;
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
