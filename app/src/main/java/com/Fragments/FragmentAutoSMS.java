package com.Fragments;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.ActivityWebView;
import com.Activities.MenuActivity;
import com.Activities.R;
import com.Classes.Favorite;
import com.Classes.Message;
import com.DatabaseHelper;
import com.Helper;
import com.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentAutoSMS extends android.support.v4.app.Fragment {


   DatabaseHelper db;

    boolean result = false;
    LinearLayout layout;
    Helper helper;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnDisplay;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        helper = new Helper();
        View v = inflater.inflate(R.layout.fragment_auto_sms, container, false);
        setHasOptionsMenu(true);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        db = DatabaseHelper.getInstance(getContext());
        layout = (LinearLayout) v.findViewById(R.id.placeHolderFragment);
        radioGroup = (RadioGroup) v.findViewById(R.id.radio);
        setRadioButtonCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.opt1:
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        radioButton = (RadioButton) group.findViewById(selectedId);
                        setIS_BUSY_OPTION(radioButton.getText().toString());
                        Toast.makeText(getContext(),
                                radioButton.getText(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.opt2:
                        radioButton = (RadioButton) group.findViewById(R.id.opt2);
                        setIS_BUSY_OPTION(radioButton.getText().toString());
                        Toast.makeText(getContext(),
                                radioButton.getText(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.opt3:
                        radioButton = (RadioButton) group.findViewById(R.id.opt3);
                        setIS_BUSY_OPTION(radioButton.getText().toString());
                        Toast.makeText(getContext(),
                                radioButton.getText(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.opt4:
                        radioButton = (RadioButton) group.findViewById(R.id.opt4);
                        setIS_BUSY_OPTION(radioButton.getText().toString());
                        Toast.makeText(getContext(),
                                radioButton.getText(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        return v;
    }
    public void setIS_BUSY_OPTION(String msg) {
        db.updateValue("IS_BUSY_OPTION",msg);
    }
    private void setRadioButtonCheck(){
        String opt = db.getValueByKey("IS_BUSY_OPTION");
        radioButton = (RadioButton) radioGroup.findViewById(R.id.opt1);
        if (radioButton.getText().toString().contains(opt)){
            radioButton.setChecked(true);
        }
        radioButton = (RadioButton) radioGroup.findViewById(R.id.opt2);
        if (radioButton.getText().toString().contains(opt)){
            radioButton.setChecked(true);
        }
        radioButton = (RadioButton) radioGroup.findViewById(R.id.opt3);
        if (radioButton.getText().toString().contains(opt)){
            radioButton.setChecked(true);
        }
        radioButton = (RadioButton) radioGroup.findViewById(R.id.opt4);
        if (radioButton.getText().toString().contains(opt)){
            radioButton.setChecked(true);
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



}
