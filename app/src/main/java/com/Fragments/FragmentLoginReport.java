package com.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Pair;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.ActivityWebView;
import com.Activities.MenuActivity;
import com.Activities.R;
import com.DatabaseHelper;
import com.GPSTracker;
import com.Helper;
import com.model.Model;

import java.util.ArrayList;
import java.util.List;

public class FragmentLoginReport extends android.support.v4.app.Fragment {
    private String s_longtitude;
    private String s_latitude;
    Helper h = null;
    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_report_fragment, container, false);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        s_longtitude = "";
        s_latitude = "";
        setHasOptionsMenu(true);
        h = new Helper();
       // ((MenuActivity) getActivity()).initialIcons();

        final GPSTracker gps = new GPSTracker(getContext());


        LocationManager manager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                gps.getLocation();
                s_longtitude = Double.toString(gps.getLongitude());
                s_latitude = Double.toString(gps.getLatitude());
            } catch (Exception e) {
                h.LogPrintExStackTrace(e);
            }
        }


        TextView userhistory = (TextView) v.findViewById(R.id.userhistory);
        userhistory.setPaintFlags(userhistory.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btn = (Button) v.findViewById(R.id.push_button);
        btn.setVisibility(View.INVISIBLE);
        Model.getInstance().Async_Wz_getState_Listener(h.getMacAddr(getContext()).toString(), new Model.Wz_getState_Listener() {
            @Override
            public void onResult(String str) {
                Log.e("mytag", str);
                Pair<String, String> p = h.getJsonKeyValue(str, "Wz_getState");
                Log.e("mytag", p.first + " " + p.second);
                if (p.first.trim().equals("out")) {
                    btn.setVisibility(View.VISIBLE);
                    setExit();
                } else {
                    btn.setVisibility(View.VISIBLE);
                    setEntrance();
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btn.getText().equals("יציאה")) {
                    setInOut(h.getMacAddr(getContext()), "out", s_latitude, s_longtitude);
                } else {
                    setInOut(h.getMacAddr(getContext()), "in", s_latitude, s_longtitude);
                }


            }
        });
        userhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUserHistory();
            }
        });


        return v;
    }

    private void goToUserHistory() {
        Intent intent = new Intent(getContext(), ActivityWebView.class);
        Bundle b = new Bundle();
        b.putInt("callid", 0);
        b.putInt("cid", 0);
        b.putInt("technicianid", Integer.valueOf(DatabaseHelper.getInstance(getContext()).getValueByKey("CID")));
        b.putString("action", "goToUserHistory");
        intent.putExtras(b);
        startActivity(intent);
    }

    private void setInOut(String macaddress, String action, String latitude, String longtitude) {

        Model.getInstance().Async_Wz_timeReport_Listener(macaddress, action, latitude, longtitude, new Model.Wz_timeReport_Listener() {
            @Override
            public void onResult(String str) {
                Log.e("mytag", "Async_Wz_timeReport result: " + str);
                //Helper h = new h
                Pair<String, String> p = h.getJsonKeyValue(str, "Wz_timeReport");
                Log.e("mytag", p.first + " " + p.second);

                if (p.first.trim().equals("out")) {

                    setExit();
                } else {
                    setEntrance();
                }
            }
        });

    }

    private void setEntrance() {
        btn.setBackgroundResource(R.drawable.btn_login);
        btn.setText("כניסה");
    }

    private void setExit() {
        btn.setBackgroundResource(R.drawable.btn_logout);
        btn.setText("יציאה");
    }

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
