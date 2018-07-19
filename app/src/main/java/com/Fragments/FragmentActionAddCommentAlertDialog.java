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
import com.File_;
import com.Helper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentActionAddCommentAlertDialog extends DialogFragment {

    //The drop down list of opportunity categories.
    private Spinner spinner;

    //The comments text area.
    private EditText comments;
    private TextView opportunities_status_alert_dialog_previous_comments;
    //The update button.
    private Button updateButton;
    private Helper h;
    private String oStatusSelected;
    private int positiveCounter =0;
    private int counter = 0;
    public FragmentActionAddCommentAlertDialog() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        h = new Helper();
        //Getting bundle arguments.
        Bundle bundle = getArguments();
        try{

        }catch(Exception ex){

        }
        //The group id that represents to which category the current opportunity belongs.
        int groupId = bundle.getInt("HeaderId");

        //The headers list.
        String headersInJson = bundle.getString("HeadersInJson");

        final String OID = String.valueOf(bundle.getInt("OID"));
        String Ocomment = bundle.getString("Ocomment");

        List<String> listDataHeaders = new Gson().fromJson(headersInJson,
                new TypeToken<List<String>>(){}.getType());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Setting the view.
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_opportunity_alert_dialog, null);
        builder.setView(view);

        //Creating the spinner.
        this.spinner = (Spinner) view.findViewById(R.id.opportunities_status_alert_dialog_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, listDataHeaders.toArray(new String[0]));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(arrayAdapter);

        //Setting the category that will appear selected in the spinner.
        this.spinner.setSelection(groupId);
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


        //TODO take the text from here
        this.opportunities_status_alert_dialog_previous_comments = (TextView)view.findViewById(R.id.opportunities_status_alert_dialog_previous_comments);
        opportunities_status_alert_dialog_previous_comments.setText(Ocomment);
        this.comments = (EditText)view.findViewById(R.id.opportunities_status_alert_dialog_comments);


        this.updateButton = (Button)view.findViewById(R.id.opportunities_status_alert_dialog_update_button);
        this.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (h.isNetworkAvailable(getContext())){
                    Model.getInstance().Async_Wz_Update_Lead_Field(h.getMacAddr(), OID, "Ostatus", oStatusSelected, new Model.Wz_Update_Lead_Field_Listener() {
                        @Override
                        public void onResult(String str) {
                            counter++;
                            if (str.contains("0"))
                                positiveCounter++;
                            chkIfBothAsynchTasksFinieshed();
                        }
                    });
                    Model.getInstance().Async_Wz_Update_Lead_Field(h.getMacAddr(), OID, "ocomment", comments.getText().toString(), new Model.Wz_Update_Lead_Field_Listener() {
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
                //Model.getInstance().Async_Wz_Update_Lead_Field();
            }
        });

        return builder.create();
    }
    private void chkIfBothAsynchTasksFinieshed(){
        if(counter == 2 && positiveCounter==2){
            Toast.makeText(getContext(),"עודכן בהצלחה", Toast.LENGTH_SHORT).show();

            FragmentManager fm = getFragmentManager();
            FragmentOpportunitiesStatus fragm = (FragmentOpportunitiesStatus)fm.findFragmentByTag("FragmentOpportunitiesStatus");
            fragm.updatedInitData();
            this.dismiss();
        }else if(counter == 2 && positiveCounter<2){
            Toast.makeText(getContext(),"שגיאה בעדכון", Toast.LENGTH_SHORT).show();
        }
    }
    private void setOstatusSelected(String statusName){
        File_ f = new File_();
        String json = f.readFromFileExternal(getContext(),"ostatus.txt");
        JSONObject j = null;
        try {
            j = new JSONObject(json);
            //get the array [...] in json
            JSONArray jarray = j.getJSONArray("Wz_getOstatusList");
            for (int i = 0; i < jarray.length(); i++) {
                if(jarray.getJSONObject(i).getString("OstatusName").contains(statusName)){
                    oStatusSelected = String.valueOf(jarray.getJSONObject(i).getInt("OstatusID"));
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        Log.e("mytag","oStatusSelected: " + oStatusSelected);

    }

}
