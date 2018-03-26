package com.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.ActivityWebView;
import com.Activities.MenuActivity;
import com.Activities.R;
import com.Alarm_Receiver;
import com.Classes.ControlPanel;
import com.Classes.Favorite;
import com.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11/08/2016.
 */
public class ControlPanelFragment extends android.support.v4.app.Fragment  {

    EditText key_et, val_et;
    //Button addmem_btn,remove_btn;
    DatabaseHelper db;
    ListView myList;
    CustomAdapter adapter;
    List<ControlPanel> data2 = new ArrayList<ControlPanel>() ;
    String dataName;
    CheckBox cb,running_cb,chk_sync_products,chk_remember,chk_calls_summary;
    LocationManager manager = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = DatabaseHelper.getInstance(getContext());
        getFavorite();
        View v = inflater.inflate(R.layout.panel_control_fragment, container, false);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        setHasOptionsMenu(true);
        manager = (LocationManager)getActivity().getSystemService(getActivity().LOCATION_SERVICE);

        cb = (CheckBox) v.findViewById(R.id.checkBox2);
        running_cb = (CheckBox) v.findViewById(R.id.running_id) ;
        db = DatabaseHelper.getInstance(getContext());
        chk_sync_products = (CheckBox) v.findViewById(R.id.chk_sync_products) ;
        chk_remember = (CheckBox) v.findViewById(R.id.chk_remember) ;
        chk_calls_summary= (CheckBox) v.findViewById(R.id.chk_calls_summary) ;

        boolean isBackground = db.getValueByKey("BACKGROUND").equals("1");
        boolean isGPS = db.getValueByKey("GPS").equals("1");
        boolean isSyncProducts = db.getValueByKey("CLIENT_SYNC_PRODUCTS").equals("1");
        boolean isRemember = db.getValueByKey("AUTO_LOGIN").equals("1");
        boolean isCallsSummary = db.getValueByKey("APPS_CALLS_SUMMARY").equals("1");



        running_cb.setChecked(isBackground);
        cb.setChecked(isGPS);
        chk_sync_products.setChecked(isSyncProducts);
        chk_remember.setChecked(isRemember);
        chk_calls_summary.setChecked(isCallsSummary);

        chk_calls_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chk_calls_summary.isChecked()){ //if true (running)
                    db.updateValue("APPS_CALLS_SUMMARY","0");
                    Toast.makeText(getActivity(),"stop APPS_CALLS_SUMMARY",Toast.LENGTH_LONG).show();
                    Log.e("myTag","stop APPS_CALLS_SUMMARY");
                }else{
                    db.updateValue("APPS_CALLS_SUMMARY","1");
                    Log.e("myTag","start APPS_CALLS_SUMMARY");
                    Toast.makeText(getActivity(),"Start APPS_CALLS_SUMMARY",Toast.LENGTH_LONG).show();
                    //}
                }
            }
        });
        running_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!running_cb.isChecked()){ //if true (running)
                    stopService1();
                    db.updateValue("BACKGROUND","0");
                    Toast.makeText(getActivity(),"stop service",Toast.LENGTH_LONG).show();
                    Log.e("myTag","stop service");
                }else{
                    startService();
                    db.updateValue("BACKGROUND","1");
                    Log.e("myTag","start service");
                    Toast.makeText(getActivity(),"Start Service",Toast.LENGTH_LONG).show();
                    //}
                }
            }
        });

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb.isChecked() == true) {

                    if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        db.getInstance(getContext()).updateValue("GPS","1");
                        startActivity(intent);

                    }else{
                        db.getInstance(getContext()).updateValue("GPS","1");
                        ((MenuActivity)getActivity()).startRepeatingTask();
                        Toast.makeText(getActivity(),"start tracking",Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }

                }else{
                    db.getInstance(getContext()).updateValue("GPS","0");
                    ((MenuActivity)getActivity()).stopRepeatingTask();
                    Log.e("myTag","stop tracking");
                    Toast.makeText(getActivity(),"stop tracking",Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().popBackStack();

                }
            }
        });
        chk_sync_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_sync_products.isChecked() == true) {
                    db.getInstance(getContext()).updateValue("CLIENT_SYNC_PRODUCTS","1");
                    Toast.makeText(getActivity(), "updated", Toast.LENGTH_LONG).show();

                }else{
                    db.getInstance(getContext()).updateValue("CLIENT_SYNC_PRODUCTS","0");
                    Toast.makeText(getActivity(), "updated", Toast.LENGTH_LONG).show();

                }
            }
        });
        chk_remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_remember.isChecked() == true) {
                    db.getInstance(getContext()).updateValue("AUTO_LOGIN","1");
                    Toast.makeText(getActivity(), "auto login on", Toast.LENGTH_LONG).show();

                }else{
                    db.getInstance(getContext()).updateValue("AUTO_LOGIN","0");
                    Toast.makeText(getActivity(), "auto login off", Toast.LENGTH_LONG).show();

                }
            }
        });

        List<ControlPanel> cps=  db.getAllKeysAndValues();  // getCustomersFromJson(myBundle);
        for (ControlPanel c : cps){
            data2.add(c);
        }
        myList = (ListView) v.findViewById(R.id.panel_control_list);
        adapter = new CustomAdapter();
        myList.setAdapter(adapter);
        //myList.setBackgroundColor(Color.parseColor("#cdebf9"));


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String u = data2.get(position).toString();
                Toast.makeText(getActivity(), "item click " + u, Toast.LENGTH_LONG).show();

                dataName = "";
            }
        });

        //getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);
        return v;
    };

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
//    private void isServiceRunning(){
//        Intent alarm = new Intent(getContext(), Alarm_Receiver.class);
//        boolean alarmRunning = (PendingIntent.getBroadcast(getContext(), 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
//        if(alarmRunning == false) {
//
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("myLog","this is on resume");

        if(!cb.isChecked()) {

        } else if(!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) && db.getValueByKey("GPS").equals("0")) {
            //if()
            cb.setChecked(false);
            //_switch.setChecked(false);
            //db.getInstance(getContext()).updateValue("GPS","0");
        }else if(!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) && db.getValueByKey("GPS").equals("1")) {
            cb.setChecked(false);
            // _switch.setChecked(false);
            db.getInstance(getContext()).updateValue("GPS", "0");
        }else if(manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) && db.getValueByKey("GPS").equals("1")) {
            cb.setChecked(true);
            // _switch.setChecked(true);
            db.getInstance(getContext()).updateValue("GPS", "1");
            ((MenuActivity)getActivity()).startRepeatingTask();
            Log.e("myTag","stop tracking");
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        //if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
        //   db.updateValue("GPS","0");
        //}

    }

    public void getFavorite(){




        String jsonArray = String.valueOf(DatabaseHelper.getInstance(getContext()).getJsonResultsFromTable("ControlPanel"));
        Log.e("mytag",jsonArray);
        return ;
        //JSONObject j = null;
//        try {
//            j = new JSONObject();
//            //get the array [...] in json
//            JSONArray jarray = j.getJSONArray(jsonArray);//"Wz_retClientFavorites");
//            //FavoriteList = new Favorite[jarray.length()];
//            //customersList = new Ccustomer[jarray.length()];
//            for (int i = 0; i < jarray.length(); i++) {
//                //JSONObject object = jarray.getJSONObject(i);
//                String FID = jarray.getJSONObject(i).getString("FID");
//                String CID = jarray.getJSONObject(i).getString("CID");
//                String pageTitle = jarray.getJSONObject(i).getString("pageTitle");
//                String pageUrl = jarray.getJSONObject(i).getString("pageUrl");
//
//                Favorite c = new Favorite(FID,CID,pageTitle,pageUrl);
//                //FavoriteList[i] = c;
//            }
//        } catch (JSONException e1) {
//            //helper.LogPrintExStackTrace(e1);
//            e1.printStackTrace();
//        }
//
//        final int N = FavoriteList.length ; // total number of textviews to add
//
//        //final TextView[] myTextViews = new TextView[N]; // create an empty array;
//
//        for (final Favorite f: FavoriteList) {
//            //Log.e("mytag",f.toString());
//            final TextView rowTextView = new TextView(getContext());
//            rowTextView.setText(f.getPageTitle());
//            rowTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
//            rowTextView.setTextSize(20);
//            //rowTextView.setHeight(30);
//
//            // add the textview to the linearlayout
//            layout.addView(rowTextView);
//            rowTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getContext(), ActivityWebView.class);
//                    Bundle b = new Bundle();
//                    b.putInt("callid", -1);
//                    b.putInt("cid", -1);
//                    b.putInt("technicianid", Integer.parseInt(String.valueOf(DatabaseHelper.getInstance(getContext()).getValueByKey("CID"))));
//                    b.putString("action","dynamic");
//                    b.putString("specialurl", f.getPageUrl());
//                    intent.putExtras(b);
//                    startActivity(intent);
//                }
//            });
//        }
//
//
//        return FavoriteList;
    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data2.size();
        }

        @Override
        public Object getItem(int position) {
            return data2.get(position).toString();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                convertView = inflater.inflate(R.layout.panel_control,null);

            }
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View rowView = inflater.inflate(R.layout.panel_control, parent, false);

            final TextView key = (TextView) convertView.findViewById(R.id.key_text);
            EditText value = (EditText) convertView.findViewById(R.id.value_text);
            //Button remove_btn = (Button) convertView.findViewById(R.id.btn_remove);

            key.setText(data2.get(position).getKey().toString());
            value.setText(data2.get(position).getValue());


            convertView.setTag(position);
            return convertView;
        }
    }

    private void stopService1(){
        Intent intent = new Intent(getContext(), Alarm_Receiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    private void startService(){
        Intent alarm = new Intent(getContext(), Alarm_Receiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getContext(), 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmRunning == false) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            // TODO: 05/09/2016  just note the time is every 5 minutes
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),240000, pendingIntent);
        }
    }
}
