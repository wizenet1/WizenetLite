package com.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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

import com.Activities.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 31/08/2016.
 */
public class FragmentLoginReport extends android.support.v4.app.Fragment {

    Button btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_report_fragment, null);
        setHasOptionsMenu(true);
        btn = (Button) v.findViewById(R.id.push_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Resources res = getResources();
                //Drawable drawable = res.getDrawable(R.drawable.myimage);
                if (btn.getText().equals("יציאה")) {
                    btn.setBackgroundResource(R.drawable.btn_login);
                    btn.setText("כניסה");
                }else{
                    btn.setBackgroundResource(R.drawable.btn_logout);
                    btn.setText("יציאה");
                }


            }
        });

        //db = DatabaseHelper.getInstance(getContext());

//        final TextView msgsubject = (TextView) v.findViewById(R.id.msgsubject);
//        final TextView msgcomment = (TextView) v.findViewById(R.id.msgcomment);
//        final TextView msgdate = (TextView) v.findViewById(R.id.msgdate);
//        final TextView msgurl = (TextView) v.findViewById(R.id.msgurl);
//
//        String id  = getArguments().getString("puId");
//        Log.e("myTag","id arrived: " +id);
//
//        Message msg = DatabaseHelper.getInstance(getActivity().getApplicationContext()).getMsgByKey(id);
//
//        msgsubject.setText(msg.getMsgSubject());
//        msgcomment.setText(msg.getMsgComment());
//        msgdate.setText(msg.getMsgDate());
//        msgurl.setText(msg.getMsgUrl());
//        msgurl.setMovementMethod(LinkMovementMethod.getInstance());

        return v;
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



}
