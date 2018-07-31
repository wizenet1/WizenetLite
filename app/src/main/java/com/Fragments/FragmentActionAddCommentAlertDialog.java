package com.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.R;
import com.Classes.IS_Status;
import com.File_;
import com.Helper;
import com.Json_;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentActionAddCommentAlertDialog extends DialogFragment {

    //The drop down list of opportunity categories.
    private Spinner spinner;

    //The comments text area.
    private EditText comments;
    private TextView add_comment_alert_dialog_comments,tv_actionid;
    //The update button.
    private Button updateButton;
    private Helper h;
    private String oStatusSelected;
    private int positiveCounter =0;
    private int counter = 0;
    ArrayList<IS_Status> is_statuses;
    JSONArray jsonArray;
    Json_ json_ ;
    public FragmentActionAddCommentAlertDialog() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        h = new Helper();
        //Getting bundle arguments.
        Bundle bundle = getArguments();
        String groupId = bundle.getString("HeaderId");
        //The headers list.
        final String actionID = bundle.getString("actionID");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Setting the view.
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_comment_alert_dialog, null);
        builder.setView(view);

        //Setting the category that will appear selected in the spinner.
        this.add_comment_alert_dialog_comments = (TextView)view.findViewById(R.id.add_comment_alert_dialog_comments);
        this.tv_actionid = (TextView)view.findViewById(R.id.tv_actionid);
        tv_actionid.setText(actionID);



          json_ = new Json_();

        this.spinner = (Spinner) view.findViewById(R.id.action_status_alert_dialog_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, json_.getISStatusString(getContext()));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinner.setAdapter(arrayAdapter);

        //Setting the category that will appear selected in the spinner.


        this.spinner.setSelection(getIndexSelected(groupId));
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("mytag",parent.getItemAtPosition(position).toString().trim());
                setOstatusSelected(parent.getItemAtPosition(position).toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        this.updateButton = (Button)view.findViewById(R.id.add_comment_alert_dialog_update_button);
        this.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (h.isNetworkAvailable(getContext())){//macAddress,actionid,field,value

                    if (h.isNetworkAvailable(getContext())){
                        String updateString = "cast(comments as nvarchar(max))";
                        if (!(add_comment_alert_dialog_comments.getText().toString().trim() == "")){
                            updateString =  "cast(comments as nvarchar(max)) +'<br>'+'" + add_comment_alert_dialog_comments.getText().toString() + "'";
                        }
                        Model.getInstance().Async_Wz_Update_Action_Field_Listener(h.getMacAddr(), actionID, "comments",updateString, new Model.Wz_Update_Action_Field_Listener() {
                            @Override
                            public void onResult(String str) {
                                counter++;
                                if (str.contains("0"))
                                    positiveCounter++;
                                chkIfBothAsynchTasksFinieshed();
                                }
                        });
                        Model.getInstance().Async_Wz_Update_Action_Field_Listener(h.getMacAddr(), actionID, "statusID",oStatusSelected , new Model.Wz_Update_Action_Field_Listener() {
                            @Override
                            public void onResult(String str) {
                                counter++;
                                if (str.contains("0"))
                                    positiveCounter++;
                                chkIfBothAsynchTasksFinieshed();
                            }
                        });


                    }else{
                        Toast.makeText(getContext(),"לא ניתן לעדכן, אינטרנט לא זמין", Toast.LENGTH_SHORT).show();

                    }



                    Model.getInstance().Async_Wz_Update_Action_Field_Listener(h.getMacAddr(), actionID, "comments", "cast(comments as nvarchar(max)) +'<br>'+'" + add_comment_alert_dialog_comments.getText().toString() + "'", new Model.Wz_Update_Action_Field_Listener() {
                        @Override
                        public void onResult(String str) {
                            counter++;
                            if (str.contains("0")){
                                //Toast.makeText(getContext(),"success", Toast.LENGTH_SHORT).show();
                                //FragmentManager fm = getFragmentManager();
                                //FragmentActions fragm = (FragmentActions)fm.findFragmentByTag("FragmentActions");
                                //fragm.retNewActions();
                                //dismiss();
                            }

                               // positiveCounter++;
                            //chkIfBothAsynchTasksFinieshed();
                        }
                    });
                }else{
                    Toast.makeText(getContext(),"לא ניתן לעדכן, אינטרנט לא זמין", Toast.LENGTH_SHORT).show();

                }
            }
        });


        return builder.create();
    }
    private int getIndexSelected(String name){
        int counter1 = 0;
        for (String s: json_.getISStatusString(getContext())) {
            //Log.e("mytag","s: " +s + "compare" + groupId + " = " + s.contains(groupId));
            if(s.contains(name)){
                break;
            }
            counter1++;
        }
        return counter1;
    }
    private void chkIfBothAsynchTasksFinieshed(){
        if(counter == 2 && positiveCounter==2){
            Toast.makeText(getContext(),"עודכן בהצלחה", Toast.LENGTH_SHORT).show();

            FragmentManager fm = getFragmentManager();
            FragmentActions fragm = (FragmentActions)fm.findFragmentByTag("FragmentActions");
            fragm.retNewActions();
            this.dismiss();
        }else if(counter == 2 && positiveCounter<2){
            Toast.makeText(getContext(),"שגיאה בעדכון", Toast.LENGTH_SHORT).show();
        }
    }

    private void setOstatusSelected(String statusName){
        File_ f = new File_();
        String json = f.readFromFileExternal(getContext(),"is_status.txt");
        JSONObject j = null;
        try {
            Json_ ja = new Json_();
            JSONArray jarray = ja.getJSONArrayFromFile("is_status.txt",getContext());;
            for (int i = 0; i < jarray.length(); i++) {
                if(jarray.getJSONObject(i).getString("statusName").contains(statusName)){
                    oStatusSelected = String.valueOf(jarray.getJSONObject(i).getInt("statusID"));
                }
            }
        } catch (JSONException e1) {
            h.LogPrintExStackTrace(e1);
            e1.printStackTrace();
        }
        Log.e("mytag","oStatusSelected: " + oStatusSelected);

    }

}
