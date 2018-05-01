package com.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.Activities.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOpportunityAlertDialog extends DialogFragment {

    //The drop down list of opportunity categories.
    private Spinner spinner;

    //The comments text area.
    private EditText comments;

    //The update button.
    private Button updateButton;

    public FragmentOpportunityAlertDialog() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Getting bundle arguments.
        Bundle bundle = getArguments();

        //The group id that represents to which category the current opportunity belongs.
        int groupId = bundle.getInt("HeaderId");

        //The headers list.
        String headersInJson = bundle.getString("HeadersInJson");
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

        //TODO take the text from here
        this.comments = (EditText)view.findViewById(R.id.opportunities_status_alert_dialog_comments);

        this.updateButton = (Button)view.findViewById(R.id.opportunities_status_alert_dialog_update_button);
        this.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO implement
            }
        });

        return builder.create();
    }
}
