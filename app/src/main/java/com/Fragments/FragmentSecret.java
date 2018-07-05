package com.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.ActivityWebView;
import com.Activities.MenuActivity;
import com.Activities.R;
import com.Classes.Call_offline;
import com.Classes.Calltime;
import com.Classes.ControlPanel;
import com.Classes.Ctype;
import com.Classes.Favorite;
import com.Classes.IS_Action;
import com.Classes.IS_ActionTime;
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
import java.util.List;

public class FragmentSecret extends android.support.v4.app.Fragment {


    EditText key_et, val_et;
    Button addmem_btn, remove_btn;
    DatabaseHelper db;
    ListView myList;
    List<Message> data2 = new ArrayList<Message>();
    String dataName;
    CheckBox cb;
    LocationManager manager = null;
    String firstname = "";
    String lastname = "";
    boolean result = false;
    LinearLayout layout;
    Helper helper;
    EditText table;
    TextView id1,id2,btn_offline_calls,id4,btn_action_time,btn_actions,btn_reminders;
    Button btn_delete_table,btn_delete_rows;
    Boolean flag = false;
    Spinner dynamicSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        helper = new Helper();
        View v = inflater.inflate(R.layout.fragment_secret, container, false);
        setHasOptionsMenu(true);
        Icon_Manager iconManager = new Icon_Manager();
        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);
        dynamicSpinner = (Spinner) v.findViewById(R.id.spinner);
        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();
        table = (EditText)   v.findViewById(R.id.table);
        id1 = (TextView) v.findViewById(R.id.id1) ;
        id2 = (TextView) v.findViewById(R.id.id2) ;
        btn_offline_calls = (TextView) v.findViewById(R.id.btn_offline_calls) ;
        id4 = (TextView) v.findViewById(R.id.id4) ;
        btn_actions = (TextView) v.findViewById(R.id.btn_actions) ;
        btn_action_time = (TextView) v.findViewById(R.id.btn_action_time) ;
        btn_delete_rows =(Button) v.findViewById(R.id.btn_delete_rows) ;
        btn_reminders = (TextView) v.findViewById(R.id.btn_reminders) ;
        btn_action_time.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", getContext()));
        id4.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", getContext()));
        id2.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", getContext()));
        id1.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", getContext()));
        btn_offline_calls.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", getContext()));

        table.setText("IS_Actions");
        btn_delete_table = (Button) v.findViewById(R.id.btn_delete_table);
        db = DatabaseHelper.getInstance(getContext());

        layout = (LinearLayout) v.findViewById(R.id.placeHolderFragment);

        id1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isISActionsEsixts = DatabaseHelper.getInstance(getContext()).isTableExists(table.getText().toString()) ? true : false;
                if (isISActionsEsixts == true){
                    Toast.makeText(getContext(), " " + table.getText().toString() +" קיים", Toast.LENGTH_LONG).show();
                }
            }
        });

        id2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("GPS is settings");
                alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
                alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //flag =DatabaseHelper.getInstance(getContext()).delete_IS_Actions_Table();
                        if (flag == true){
                            Toast.makeText(getContext(), "deleted successfully", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();

            }
        });
        id4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String d = "";
                d = String.valueOf(DatabaseHelper.getInstance(getContext()).getJsonResultsFromTable("ControlPanel"));
                File_ f = new File_();
                boolean flag1 = f.writeTextToFileExternal(getContext(),"controlpanel.txt",d);
                Toast.makeText(getContext(), "is created? ->" + flag1, Toast.LENGTH_LONG).show();
            }
        });
        btn_delete_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("האם אתה בטוח שברצונך למחוק את טבלת" + table.getText().toString() + "?");
                alertDialog.setMessage("?");
                alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        flag =DatabaseHelper.getInstance(getContext()).deleteTableByName(table.getText().toString());
                        if (flag == true){
                            Toast.makeText(getContext(), "deleted successfully", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getContext(), "did not deleted", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
        btn_delete_rows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("האם אתה בטוח שברצונך למחוק שורות מ" + table.getText().toString() + "?");
                alertDialog.setMessage("?");
                alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        flag =DatabaseHelper.getInstance(getContext()).delete_Table_Rows(table.getText().toString());
                        if (flag == true){
                            Toast.makeText(getContext(), "deleted successfully from " + table.getText().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
        btn_action_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawActionsTimes();
            }
        });
        btn_actions.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawActions();
            }
        });
        setSpinner();
        setBtnCalltime();
        btn_reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawMessagesReminders();
            }
        });
        return v;
    }
    private void setBtnCalltime(){
        btn_offline_calls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeAllViewsInLayout();
                List<Calltime> calltimeList = new ArrayList<Calltime>();
                calltimeList = DatabaseHelper.getInstance(getContext()).getCalltimes("");
                for (final Calltime f : calltimeList) {
                    //Log.e("mytag",f.toString());
                    final TextView rowTextView = new TextView(getContext());
                    rowTextView.setText(f.toString());
                    rowTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    rowTextView.setTextSize(20);
                    //rowTextView.setHeight(30);
                    // add the textview to the linearlayout
                    layout.addView(rowTextView);
                    rowTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            }
        });
    }
    private void setSpinner(){
        try{

            List<String> tablesList = new ArrayList<String>();
            tablesList = DatabaseHelper.getInstance(getContext()).getTablesNames();
            String[] items1 = new String[tablesList.size()];
            int ii = 0;
            for (String tablename:tablesList) {
                items1[ii] = tablename;
                ii++;
            }
            //items1[ctypeList.size()]="";
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items1);
            dynamicSpinner.setAdapter(adapter);
            int selectionPosition = adapter.getPosition("");
            dynamicSpinner.setSelection(selectionPosition);
            dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    String s = "";
                    table.setText((String) parent.getItemAtPosition(position));
                    //s = String.valueOf(db.getCallStatusByCallStatusName((String) parent.getItemAtPosition(position)).getCallStatusID());
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



        }catch (Exception e){

        }
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
    private void drawActionsTimes(){
        layout.removeAllViewsInLayout();
        List<IS_ActionTime> actionsTime = new ArrayList<IS_ActionTime>();
        actionsTime = DatabaseHelper.getInstance(getContext()).getISActionsTime("");
        for (final IS_ActionTime f : actionsTime) {
            //Log.e("mytag",f.toString());
            final TextView rowTextView = new TextView(getContext());
            rowTextView.setText(f.toString());
            rowTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            rowTextView.setTextSize(20);
            //rowTextView.setHeight(30);
            // add the textview to the linearlayout
            layout.addView(rowTextView);
            rowTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
    private void drawControlPanel(){
        layout.removeAllViewsInLayout();
        //layout.
        List<IS_Action> actions = new ArrayList<IS_Action>();
        actions = DatabaseHelper.getInstance(getContext()).getISActions("");
        for (final IS_Action f : actions) {
            //Log.e("mytag",f.toString());
            final TextView rowTextView = new TextView(getContext());
            rowTextView.setText(f.toString());
            rowTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            rowTextView.setTextSize(20);
            //rowTextView.setHeight(30);
            // add the textview to the linearlayout
            layout.addView(rowTextView);
            rowTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
    private void drawActions(){
        layout.removeAllViewsInLayout();
        //layout.
        List<ControlPanel> actions = new ArrayList<ControlPanel>();
        actions = DatabaseHelper.getInstance(getContext()).getAllKeysAndValues();
        for (final ControlPanel f : actions) {
            //Log.e("mytag",f.toString());
            final TextView rowTextView = new TextView(getContext());
            rowTextView.setText(f.toString());
            rowTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            rowTextView.setTextSize(20);
            //rowTextView.setHeight(30);
            // add the textview to the linearlayout
            layout.addView(rowTextView);
            rowTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
    private void drawMessagesReminders(){
        layout.removeAllViewsInLayout();
        //layout.
        List<Message> actions = new ArrayList<Message>();
        actions = DatabaseHelper.getInstance(getContext()).getAllMessages();
        for (final Message f : actions) {
            //Log.e("mytag",f.toString());
            final TextView rowTextView = new TextView(getContext());
            rowTextView.setText(f.toString());
            rowTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            rowTextView.setTextSize(20);
            //rowTextView.setHeight(30);
            // add the textview to the linearlayout
            layout.addView(rowTextView);
            rowTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
    public Favorite[] getFavorite(String json) {


        Favorite[] FavoriteList = new Favorite[0];
        JSONObject j = null;
        try {
            j = new JSONObject(json);
            //get the array [...] in json
            JSONArray jarray = j.getJSONArray("Wz_retClientFavorites");
            FavoriteList = new Favorite[jarray.length()];
            //customersList = new Ccustomer[jarray.length()];
            for (int i = 0; i < jarray.length(); i++) {
                //JSONObject object = jarray.getJSONObject(i);
                String FID = jarray.getJSONObject(i).getString("FID");
                String CID = jarray.getJSONObject(i).getString("CID");
                String pageTitle = jarray.getJSONObject(i).getString("pageTitle");
                String pageUrl = jarray.getJSONObject(i).getString("pageUrl");

                Favorite c = new Favorite(FID, CID, pageTitle, pageUrl);
                FavoriteList[i] = c;
            }
        } catch (JSONException e1) {
            helper.LogPrintExStackTrace(e1);
            e1.printStackTrace();
        }

        final int N = FavoriteList.length; // total number of textviews to add

        //final TextView[] myTextViews = new TextView[N]; // create an empty array;

        for (final Favorite f : FavoriteList) {
            //Log.e("mytag",f.toString());
            final TextView rowTextView = new TextView(getContext());
            rowTextView.setText(f.getPageTitle());
            rowTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            rowTextView.setTextSize(20);
            //rowTextView.setHeight(30);

            // add the textview to the linearlayout
            layout.addView(rowTextView);
            rowTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ActivityWebView.class);
                    Bundle b = new Bundle();
                    b.putInt("callid", -1);
                    b.putInt("cid", -1);
                    b.putInt("technicianid", Integer.parseInt(String.valueOf(DatabaseHelper.getInstance(getContext()).getValueByKey("CID"))));
                    b.putString("action", "dynamic");
                    b.putString("specialurl", f.getPageUrl());
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }


        return FavoriteList;
    }


}
