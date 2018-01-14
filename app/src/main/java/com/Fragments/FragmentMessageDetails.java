package com.Fragments;
import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.R;
import com.Classes.Message;
import com.DatabaseHelper;

import java.util.List;

public class FragmentMessageDetails extends android.support.v4.app.Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.message_details_activity, null);
        setHasOptionsMenu(true);

        //db = DatabaseHelper.getInstance(getContext());

        final TextView msgsubject = (TextView) v.findViewById(R.id.msgsubject);
        final TextView msgcomment = (TextView) v.findViewById(R.id.msgcomment);
        final TextView msgdate = (TextView) v.findViewById(R.id.msgdate);
        final TextView msgurl = (TextView) v.findViewById(R.id.msgurl);

        String id  = getArguments().getString("puId");
        Log.e("myTag","id arrived: " +id);

        Message msg = DatabaseHelper.getInstance(getActivity().getApplicationContext()).getMsgByKey(id);

        msgsubject.setText(msg.getMsgSubject());
        msgcomment.setText(msg.getMsgComment());
        //msgdate.setText(msg.getMsgDate());
        msgdate.setText("");
        //msgurl.setText(msg.getMsgUrl());
        msgurl.setText("");
        msgurl.setMovementMethod(LinkMovementMethod.getInstance());

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
//    msgSubject;
//    msgComment;
//    msgUrl;
//    msgDate ;
//    msgRead ;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.message_details_activity);
//
//        final TextView msgsubject = (TextView) findViewById(R.id.msgsubject);
//        final TextView msgcomment = (TextView) findViewById(R.id.msgcomment);
//        final TextView msgdate = (TextView) findViewById(R.id.msgdate);
//        final TextView msgurl = (TextView) findViewById(R.id.msgurl);
//
//
//        Intent intent = getIntent();
//        String id = intent.getExtras().getString("id");
//        Message msg = DatabaseHelper.getInstance(getApplicationContext()).getMsgByKey(id);
//
//        msgsubject.setText(msg.getMsgSubject());
//        msgcomment.setText(msg.getMsgComment());
//        msgdate.setText(msg.getMsgDate());
//        msgurl.setText(msg.getMsgUrl());
//        msgurl.setMovementMethod(LinkMovementMethod.getInstance());
//
//
//    }

