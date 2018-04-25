package com.Fragments;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
import com.Classes.IS_Project;
import com.Classes.IS_Task;
import com.DatabaseHelper;
import com.File_;
import com.Helper;
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
    Spinner dynamicSpinner,dynamicSpinner2,spinnerProjects,spinnerTasks,stat1,stat2,stat3;
    private ActionsAdapter actionsAdapter;
    Calendar myCalendar;
    Button btn_add;
    DatePickerDialog.OnDateSetListener date,date2;
    EditText txt_destination,txt_assignment,txt_action_desc,txt_action_comments;
    Map<String, IS_Task> tasks_map;
    Map<String, IS_Project> projects_map;
    Map<String, Ccustomer> ccustomers_map;

    String selectedProject,selectedTask,selectedCcustomer,selectedReminder,selectedFromHoure,selectedToHour;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_action, null);
        //setHasOptionsMenu(true);
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);
        helper= new Helper();
        //Turn all the action bar icons off to their original color.
        //((MenuActivity) getActivity()).turnActionBarMissionsIconOn();
        db = DatabaseHelper.getInstance(getContext());
        btn_add = (Button) v.findViewById(R.id.btn_add);
        dynamicSpinner = (Spinner) v.findViewById(R.id.spinner);
        dynamicSpinner2 = (Spinner) v.findViewById(R.id.spinner2);
        spinnerProjects = (Spinner) v.findViewById(R.id.spinnerProjects);
        spinnerTasks = (Spinner) v.findViewById(R.id.spinnerTasks);
        txt_action_desc= (EditText) v.findViewById(R.id.txt_action_desc);
        txt_action_comments= (EditText) v.findViewById(R.id.txt_action_comments);
        txt_destination= (EditText) v.findViewById(R.id.txt_destination);
        txt_assignment= (EditText) v.findViewById(R.id.txt_assignment);
        selectedReminder="";
        selectedFromHoure="";
        selectedToHour ="";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c_week = Calendar.getInstance();
        //c_week.add(Calendar.DAY_OF_YEAR, 7);
        String formatted = df.format(c_week.getTime());
        txt_destination.setText(formatted);
        txt_assignment.setText(formatted);

        stat1 = (Spinner) v.findViewById(R.id.spinner3);
        stat2 = (Spinner) v.findViewById(R.id.spinner4);
        stat3 = (Spinner) v.findViewById(R.id.spinner5);
        setCcustomerSpinner();
        setDialog();
        setStaticSpinners();
        setProjectSpinner();
        setCtypeSpinner();
        setTaskSpinner(0);
        setBtn();


        return v;
    };
    private void setBtn(){
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean flag = false;
                flag = addAction();
                if (flag == true){
                    String json = "";
                    json = DatabaseHelper.getInstance(getContext()).getJsonResultsFromTable("IS_Actions_Offline").toString();
                    Model.getInstance().Async_Wz_createISAction(helper.getMacAddr(), json, new Model.Wz_getTasks_Listener() {
                        @Override
                        public void onResult(String str) {
                            if (str.contains("0")){
                                Toast.makeText(getContext(), "success uploaded", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }
    private void setDialog(){
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();
            }

        };
        txt_destination.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        txt_assignment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void setStaticSpinners(){
        String[] arraySpinner = new String[] {
                "ללא", "5", "10", "20", "30","40","שעה","יום"
        };
        ArrayList<String> ar = new ArrayList<String>();
        //String[] arraySpinner2 = new String[] {
        //        "ללא", "5", "10", "20", "30","40","שעה","יום"
        //};
        for (int i=0; i < 24;i++){
            for(int j=0 ; j < 2 ; j++){
                String s = "";
                if(i<10 && j==0){
                    s = "0"+ i + ":00";
                }else if(i<10 && j==1){
                    s = "0"+ i + ":30";
                }else if(i>10 && j==0){
                    s =  i + ":00";
                }else{
                    s =  i + ":30";
                }
                ar.add(s);
            }
        }
        String[] stringArray = ar.toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arraySpinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, stringArray);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, stringArray);

        stat1.setAdapter(adapter);
        stat2.setAdapter(adapter2);
        stat3.setAdapter(adapter3);
        DateFormat df = new SimpleDateFormat("HH");
        Calendar c_week = Calendar.getInstance();
        //c_week.add(Calendar.DAY_OF_YEAR, 7);
        String formatted = df.format(c_week.getTime());
        selectedReminder = "ללא";
        selectedFromHoure =formatted + ":00";
        selectedToHour =formatted + ":30";

        try{
            int pos1 = adapter2.getPosition(formatted + ":00");
            stat2.setSelection(pos1);
            int pos2 = adapter3.getPosition(formatted + ":30");

            stat3.setSelection(pos2);
        }catch(Exception e){

        }
        stat1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedReminder =(String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        stat2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFromHoure =(String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        stat3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedToHour =(String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

         txt_destination.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel2() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txt_assignment.setText(sdf.format(myCalendar.getTime()));
    }

    private void setCtypeSpinner(){
        try{
            List<Ctype> ctypeList = new ArrayList<Ctype>();
            ctypeList = getCtypeList();
            String[] items1 = new String[ctypeList.size()+1];
            for (int i = 0; i < ctypeList.size(); i++) {
                items1[i] = ctypeList.get(i).getCtypeName();
                //Log.e("mytag",ctypeList.get(i).getCtypeName());
            }
            items1[ctypeList.size()]="";
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



        }catch (Exception e){

        }
    }
    private void setProjectSpinner(){
        try{
            List<IS_Project> projectsList = new ArrayList<IS_Project>();
            projectsList = getProjectsList();

            String[] items3 = new String[projectsList.size()+1];

            for (int i = 0; i < projectsList.size(); i++) {
                items3[i] = projectsList.get(i).getProjectSummery();
                //Log.e("mytag",ccustomerList.get(i).getCtypeName());
            }
            items3[projectsList.size()]="";

            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items3);
            spinnerProjects.setAdapter(adapter2);
            int selectionPosition2 = adapter2.getPosition("");
            spinnerProjects.setSelection(selectionPosition2);
            spinnerProjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    String s = "";
                    IS_Project myp   = new IS_Project();
                    myp =(IS_Project) projects_map.get((String) parent.getItemAtPosition(position)) ;
                    try{
                        Log.e("mytag",String.valueOf( myp.getProjectID()));
                        selectedProject = String.valueOf( myp.getProjectID());


                        setTaskSpinner(myp.getProjectID());
                    }catch(Exception e){
                        helper.LogPrintExStackTrace(e);
                    }
                    Log.e("mytag", (String) parent.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
        }catch (Exception e){

        }
    }
    private void setTaskSpinner(int PID){
        try{
            List<IS_Task> projectsList = new ArrayList<IS_Task>();
            projectsList = getTasksList(PID);
            //for (Map.Entry<String, IS_Task> entry : tasks_map.entrySet())
            //{
                //Log.e("mytag",(entry.getKey() + "/" + entry.getValue()));
            //}

            String[] items3 = new String[projectsList.size()+1];

            for (int i = 0; i < projectsList.size(); i++) {
                items3[i] = projectsList.get(i).getTaskSummery();
                //Log.e("mytag",ccustomerList.get(i).getCtypeName());
            }
            items3[projectsList.size()]="";

            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items3);
            spinnerTasks.setAdapter(adapter2);
            int selectionPosition2 = adapter2.getPosition("");
            spinnerTasks.setSelection(selectionPosition2);
            spinnerTasks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    String s = "";
                    //selectedTask = (String) parent.getItemAtPosition(position);f
                    IS_Task myp   = new IS_Task();
                    //for (Map.Entry<String, IS_Task> entry : tasks_map.entrySet())
                    //{
                    //    Log.e("mytag",(entry.getKey() + "/" + entry.getValue()));
                    //}

                        try{
                            myp =(IS_Task) tasks_map.get((String) parent.getItemAtPosition(position)) ;
                            if (myp != null){
                                selectedTask = String.valueOf( myp.getTaskID());
                                Log.e("mytag",myp.toString() + " | " + selectedTask);
                            }
                        }catch(Exception e){
                            helper.LogPrintExStackTrace(e);
                        }



                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
        }catch (Exception e){
            helper.LogPrintExStackTrace(e);
        }
    }

    private void setCcustomerSpinner(){

        List<Ccustomer> ccustomerList = new ArrayList<Ccustomer>();
        ccustomerList = getCcustomerList();



        String[] items2 = new String[ccustomerList.size()+1];


        //----------customers -----------------
        for (int i = 0; i < ccustomerList.size(); i++) {
            items2[i] = ccustomerList.get(i).getCfname();
            //Log.e("mytag",ccustomerList.get(i).getCtypeName());
        }
        items2[ccustomerList.size()]="";

        //items3[ccustomerList.size()]="";



        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items2);
        dynamicSpinner2.setAdapter(adapter2);
        int selectionPosition2 = adapter2.getPosition("");
        dynamicSpinner2.setSelection(selectionPosition2);
        dynamicSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String s = "";

                Ccustomer myp   = new Ccustomer();
                try{
                    myp =(Ccustomer) ccustomers_map.get((String) parent.getItemAtPosition(position)) ;
                    if (myp != null){
                        selectedCcustomer = String.valueOf( myp.getCID());
                        Log.e("mytag",myp.toString() + " | " + selectedCcustomer);
                    }
                }catch(Exception e){
                    helper.LogPrintExStackTrace(e);
                }

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
            ccustomers_map = new HashMap<String, Ccustomer>(jarray.length());

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
                ccustomers_map.put(e.getString("Name"),c);
                list.add(c);
            } catch (JSONException e1) {
                helper.LogPrintExStackTrace(e1);
                e1.printStackTrace();
                return list;
            }
        }
        return list;
    }
    private List<IS_Project> getProjectsList(){
        List<IS_Project> list = new ArrayList<IS_Project>() ;
        JSONArray jarray = null;
        try {
            String strJson = "";
            File_ f = new File_();
            strJson = f.readFromFileExternal(getContext(),"projects.txt");
            JSONObject j = new JSONObject(strJson);
            //get the array [...] in json
             jarray = j.getJSONArray("Wz_getProjects");
            projects_map = new HashMap<String, IS_Project>(jarray.length());
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
                IS_Project c = new IS_Project(e.getInt("projectID"),e.getString("projectSummery"));
                projects_map.put(String.valueOf(e.getString("projectSummery")),c);
                list.add(c);
            } catch (JSONException e1) {
                helper.LogPrintExStackTrace(e1);
                e1.printStackTrace();
                return list;
            }
        }
        return list;
    }
    private List<IS_Task> getTasksList(int PID){
        List<IS_Task> list = new ArrayList<IS_Task>() ;
        JSONArray jarray = null;
        try {
            String strJson = "";
            File_ f = new File_();
            strJson = f.readFromFileExternal(getContext(),"tasks.txt");
            JSONObject j = new JSONObject(strJson);
            //get the array [...] in json
            jarray = j.getJSONArray("Wz_getTasks");
            tasks_map = new HashMap<String, IS_Task>(jarray.length());
            //jarray =  new JSONArray("Wz_getProjects");
        } catch (JSONException e) {
            helper.LogPrintExStackTrace(e);
            e.printStackTrace();
            return list;
        }

        for (int i = 0; i < jarray.length(); i++) {
            final JSONObject e;
            try {
                e = jarray.getJSONObject(i);
                if (PID== e.getInt("projectID") ||  PID==0){
                    IS_Task c = new IS_Task(e.getInt("taskID"),e.getInt("projectID"),e.getString("taskSummery"));
                    tasks_map.put(String.valueOf(e.getString("taskSummery")),c);
                    list.add(c);
                }
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
    private boolean addAction(){

        Boolean ret = false;

        try {
            IS_Action action= new IS_Action();//Integer.valueOf(cursor.getString(cursor.getColumnIndex("CallID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("AID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("CID"))), cursor.getString(cursor.getColumnIndex("CreateDate")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("statusID"))), cursor.getString(cursor.getColumnIndex("CallPriority")), cursor.getString(cursor.getColumnIndex("subject")), cursor.getString(cursor.getColumnIndex("comments")), cursor.getString(cursor.getColumnIndex("CallUpdate")), cursor.getString(cursor.getColumnIndex("cntrctDate")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("TechnicianID"))), cursor.getString(cursor.getColumnIndex("statusName")), cursor.getString(cursor.getColumnIndex("internalSN")), cursor.getString(cursor.getColumnIndex("Pmakat")), cursor.getString(cursor.getColumnIndex("Pname")), cursor.getString(cursor.getColumnIndex("contractID")), cursor.getString(cursor.getColumnIndex("Cphone")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("OriginID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("ProblemTypeID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("CallTypeID"))), cursor.getString(cursor.getColumnIndex("priorityID")), cursor.getString(cursor.getColumnIndex("OriginName")), cursor.getString(cursor.getColumnIndex("problemTypeName")), cursor.getString(cursor.getColumnIndex("CallTypeName")), cursor.getString(cursor.getColumnIndex("Cname")), cursor.getString(cursor.getColumnIndex("Cemail")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("contctCode"))), cursor.getString(cursor.getColumnIndex("callStartTime")), cursor.getString(cursor.getColumnIndex("callEndTime")), cursor.getString(cursor.getColumnIndex("Ccompany")), cursor.getString(cursor.getColumnIndex("Clocation")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("callOrder"))), cursor.getString(cursor.getColumnIndex("Caddress")), cursor.getString(cursor.getColumnIndex("Ccity")), cursor.getString(cursor.getColumnIndex("Ccomments")), cursor.getString(cursor.getColumnIndex("Cfname")), cursor.getString(cursor.getColumnIndex("Clname")), cursor.getString(cursor.getColumnIndex("techName")), cursor.getString(cursor.getColumnIndex("Aname")), cursor.getString(cursor.getColumnIndex("ContctName")), cursor.getString(cursor.getColumnIndex("ContctAddress")), cursor.getString(cursor.getColumnIndex("ContctCity")), cursor.getString(cursor.getColumnIndex("ContctCell")), cursor.getString(cursor.getColumnIndex("ContctPhone")), cursor.getString(cursor.getColumnIndex("ContctCity")), cursor.getString(cursor.getColumnIndex("Ccell")), cursor.getString(cursor.getColumnIndex("techColor")), cursor.getString(cursor.getColumnIndex("ContctCemail")), cursor.getString(cursor.getColumnIndex("CallParentID")));

            action.setActionID(generateActionID());
            action.setTaskID(Integer.valueOf(selectedTask.toString()));//Integer.valueOf(selectedTask.toString())

            action.setOwnerID(0);
            action.setUserID(Integer.valueOf(selectedCcustomer));//Integer.valueOf(ccustomers_map.get(selectedCcustomer).getCID())
            action.setProjectID(Integer.valueOf(selectedProject));//projects_map.get(selectedProject).getProjectID()
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");

            Calendar c_week = Calendar.getInstance();
            //c_week.add(Calendar.DAY_OF_YEAR, 7);
            String formatted = df.format(c_week.getTime());
            String formatted2 = df2.format(c_week.getTime());
            action.setActionDate(txt_destination.getText().toString() +" 00:00:00");
            action.setActionStartDate(formatted);
            action.setActionDue("9999-01-01 00:00:00"); //today

            action.setActionDesc(String.valueOf(txt_action_desc.getText()));
            action.setComments(String.valueOf(txt_action_comments.getText()));
            action.setPriorityID("1");
            action.setReminderID("1");
            if (selectedReminder != "ללא"){

                action.setRemindertime(selectedReminder);

            }
            action.setCreate(formatted);
            action.setActionRef("0");
            action.setActionType("-1");
            if (true){
                action.setActionSdate(txt_assignment.getText().toString() + " "+ selectedFromHoure + ":00");
                action.setActionEdate(txt_assignment.getText().toString() + " "+ selectedToHour + ":00");
            }
            ret =DatabaseHelper.getInstance(getContext()).addISAction(action);
            Log.e("mytag",DatabaseHelper.getInstance(getContext()).getJsonResultsFromTable("IS_Actions_Offline").toString());
            //action.setStatusID(e.getInt("statusID"));
            //action.setDepID(e.getInt("depID"));
            //action.setUserCtypeID(e.getInt("userCtypeID"));
            //action.setOwnerCtypeID(e.getInt("ownerCtypeID"));

//            if (e.getString("ParentActionID").contains("null")){
//            }else{
//                action.setParentActionID(Integer.valueOf(e.getString("ParentActionID")));
//            }

            //action.setWorkHours(e.getString("WorkHours"));
            //action.setWorkEstHours(e.getString("WorkEstHours"));
            //action.setLastUpdate(e.getString("LastUpdate"));
            //action.setActionLink(e.getString("actionLink"));

            //action.setUserCfname(e.getString("userCfname"));
            //action.setUserClname(e.getString("userClname"));
            //action.setUserCemail(e.getString("userCemail"));

            //action.setOwnerCfname(e.getString("ownerCfname"));
            //action.setOwnerClname(e.getString("ownerClname"));
            //action.setOwnerCemail(e.getString("ownerCemail"));

            //action.setStatusName(e.getString("statusName"));
            //action.setPriorityName(e.getString("PriorityName"));


            //action.setWorkHoursM(e.getString("WorkHoursM"));
            //action.setWorkEstHoursM(e.getString("WorkEstHoursM"));
            //action.setActionPrice(e.getString("actionPrice"));
            //action.setStatusColor(e.getString("statusColor"));
            //action.setTaskSummery(e.getString("taskSummery"));
            //action.setProjectSummery(e.getString("projectSummery"));
            //action.setProjectType(e.getString("projectType"));
            //action.setActionNum(e.getString("actionNum"));
            //action.setActionFrom(e.getString("actionFrom"));
            //action.setActionDays(e.getString("actionDays"));
            //action.setExpr1(e.getString("Expr1"));
            //action.setProjectDesc(e.getString("projectDesc"));

        } catch (Exception e1) {
            helper.LogPrintExStackTrace(e1);

        }
        return ret;
    }
    private int generateActionID(){
        int ret = 0;

        List<IS_Action> actions = new ArrayList<IS_Action>() ;
        try {
            actions= DatabaseHelper.getInstance(getContext()).getISActions("top1");
            for (IS_Action a:actions) {
                //Log.e("mytag","list " +a.getActionID());
                ret = a.getActionID();
            }
            if (ret > 0){
                ret = -1;
            }else{
                ret = ret-1;
            }
        } catch (Exception e) {
            Log.e("mytag","sdf " +e.getMessage());
            e.printStackTrace();
            helper.LogPrintExStackTrace(e);
        }
        Log.e("mytag","first actionID:" + String.valueOf(ret) );
        return ret;
    }



}
