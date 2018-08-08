package com.Fragments;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
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
import com.Activities.MainActivity;
import com.Activities.MenuActivity;
import com.Activities.R;
import com.Classes.Call;
import com.Classes.Call_offline;
import com.Classes.Calltime;
import com.Classes.Ccustomer;
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
import com.Json_;
import com.Notification_;
import com.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.Activities.MainActivity.ctx;

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
    TextView id1,id2,btn_offline_calls,id4,btn_action_time,btn_actions,btn_reminders,btn_notification,tv_chk_ws;
    Button btn_delete_table,btn_delete_rows,btn_show_rows;
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
        tv_chk_ws= (TextView) v.findViewById(R.id.tv_chk_ws) ;
        table = (EditText)   v.findViewById(R.id.table);
        id1 = (TextView) v.findViewById(R.id.id1) ;
        id2 = (TextView) v.findViewById(R.id.id2) ;
        btn_offline_calls = (TextView) v.findViewById(R.id.btn_offline_calls) ;
        id4 = (TextView) v.findViewById(R.id.id4) ;
        btn_actions = (TextView) v.findViewById(R.id.btn_actions) ;
        btn_action_time = (TextView) v.findViewById(R.id.btn_action_time) ;
        btn_delete_rows =(Button) v.findViewById(R.id.btn_delete_rows) ;

        btn_show_rows =(Button) v.findViewById(R.id.btn_show_rows) ;

        btn_reminders = (TextView) v.findViewById(R.id.btn_reminders) ;
        btn_notification = (TextView) v.findViewById(R.id.btn_notification) ;
        btn_action_time.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", getContext()));
        id4.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", getContext()));
        id2.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", getContext()));
        id1.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", getContext()));
        btn_offline_calls.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", getContext()));
        btn_notification.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", getContext()));

        tv_chk_ws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callWSnewCalls();
            }
        });

        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.isNetworkAvailable(getContext())){
                    try{

                        Model.getInstance().AsyncReminder(getMacAddr1(), new Model.ReminderListener() {
                            @Override
                            public void onResult(String str, String str2, int size,String msgID) {
                                if(size==1){
                                    pushNotification(str,str2,msgID);
                                }else{
                                    pushNotification("Wizenet",size+" new messages",msgID);
                                }
                            }
                        });
                    }catch (Exception e){
                        helper.LogPrintExStackTrace(e);
                    }

                }
            }
        });


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


        setShowRowsButton();

        return v;
    }
    private void callWSnewCalls(){
        String callsString = "";
        List<Call> callList = new ArrayList<Call>();
        callList = DatabaseHelper.getInstance(ctx).getCalls("");
        for (Call c:callList) {
            callsString += c.getCallID() + ",";
        }
        callsString = callsString.substring(0, callsString.lastIndexOf(","));
        helper = new Helper();
        Model.getInstance().Async_Wz_Json(helper.getMacAddr(ctx), callsString, "newCalls", new Model.Wz_Json_Listener() {
            @Override
            public void onResult(String str) {
                Log.e("mytag","newCalls: "+str);
                Notification_ n = new Notification_();
                Json_ j_ = new Json_();
                JSONArray jarray = new JSONArray();
                try{

                    jarray = j_.getJSONArrayFromString(str,ctx);

                }catch(Exception e){
                    helper.LogPrintExStackTrace(e);
                    jarray = null;
                }

                if (jarray != null){

                    if (jarray.length() > 1){ //-------------bigger than 1
                        n.pushNotificationNewCalls("Wizenet",String.valueOf(jarray.length()),j_.getElementValueInJarray(jarray,"subject"),j_.getElementValueInJarray(jarray,"CallID"),ctx);
                    }else{ //---------------------------------equals 1
                        n.pushNotificationNewCalls("Wizenet",String.valueOf(jarray.length()),j_.getElementValueInJarray(jarray,"subject"),j_.getElementValueInJarray(jarray,"CallID"),ctx);
                    }
                    //j_.addCallsFromJSONArray(jarray,ctx);
                }


                Toast.makeText(ctx, "success", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setShowRowsButton(){
        btn_show_rows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (table.getText().toString()){
                    case "Calltime":
                        drawCallTimes();
                        break;
                    case "IS_Actions":
                        drawActions();
                        break;
                    case "IS_ActionsTime":
                        drawActionsTimes();
                        break;
                    case "Messages":
                        drawMessagesReminders();
                        break;
                    case "Ccustomers":
                        Toast.makeText(getContext(), "Ccustomers", Toast.LENGTH_LONG).show();

                        drawCcustomers();
                        break;
                }
            }
        });
    }
    private void setBtnCalltime(){
        btn_offline_calls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCallTimes();
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
    private void drawCcustomers(){
        String txt1 = DatabaseHelper.getInstance(getContext()).getScalarByCountQuery("SELECT count(*) FROM Ccustomers ");
        Toast.makeText(getContext(), "customers count ->" + txt1, Toast.LENGTH_LONG).show();

        layout.removeAllViewsInLayout();
        List<Ccustomer> ccustomerList = new ArrayList<Ccustomer>();
        ccustomerList = DatabaseHelper.getInstance(getContext()).getCcustomers("");
        for (final Ccustomer f : ccustomerList) {
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
    private void drawCallTimes(){
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
    public  String getMacAddr1() {
        String device_id = "";
        try{
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            device_id = tm.getDeviceId();
            if (!(device_id.trim() == "")){
                return device_id;
            }else{
                device_id = getGUID();
                return device_id;
            }
        }catch(Exception e){
            Helper h = new Helper();
            h.LogPrintExStackTrace(e);
            device_id = getGUID();
            return device_id;
        }
    }
    private  String getGUID(){
        String device_id = "";
        try{
            File_ f= new File_();
            if (f.isFileExist("guid.txt") == true){
                device_id = f.readFromFileExternal(ctx,"guid.txt");
                return device_id;
            }else{
                UUID uuid = UUID.randomUUID();
                String uuidInString = uuid.toString();
                f.writeTextToFileExternal(ctx,"guid.txt",uuidInString);
                device_id = f.readFromFileExternal(ctx,"guid.txt");
                return device_id;
            }
        }catch (Exception e){
            return "";
        }
    }
    private void pushNotification(String title,String text,String msgID) {
        long[] vibrate = {400, 400, 200, 200, 200};
        Model.getInstance().init(getContext());
        Intent notificationIntent = new Intent(getContext(), MenuActivity.class);
// set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.addFlags(FLAG_UPDATE_CURRENT);
        notificationIntent.putExtra("puId", String.valueOf(msgID));
        PendingIntent intent = PendingIntent.getActivity(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification = new Notification.Builder(getContext());
        notification.setAutoCancel(true);
        notification.setContentIntent(intent);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String url = helper.extractLinks(text)[0];
        //builder.setTicker("this is ticker text");
        notification.setContentTitle(title);
        notification.setContentText(text.replace(url,""));
        notification.setSmallIcon(R.drawable.face);
        notification.setSound(alarmSound);
        notification.setOngoing(true);
        notification.setVibrate(vibrate);


        Notification notificationn = notification.getNotification();
        notificationManager.notify(0, notificationn);


    }


}
