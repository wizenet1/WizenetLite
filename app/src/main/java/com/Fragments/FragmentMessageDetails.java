package com.Fragments;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;
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
import com.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class FragmentMessageDetails extends android.support.v4.app.Fragment {
    TextView msgsubject;
    TextView msgcomment;
    TextView msgdate ;
    TextView msgurl ;
    String myurl = "";
    Helper h;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.message_details_activity, null);
        setHasOptionsMenu(true);

        //db = DatabaseHelper.getInstance(getContext());
        h = new Helper();
        msgsubject = (TextView) v.findViewById(R.id.msgsubject);
        msgcomment = (TextView) v.findViewById(R.id.msgcomment);
        msgdate = (TextView) v.findViewById(R.id.msgdate);
        msgurl = (TextView) v.findViewById(R.id.msgurl);

        String id  = getArguments().getString("puId");
        Log.e("myTag","id arrived: " +id);

        Message msg = DatabaseHelper.getInstance(getActivity().getApplicationContext()).getMsgByKey(id);

        msgsubject.setText(msg.getMsgSubject());

        //msgdate.setText(msg.getMsgDate());
        msgdate.setText("");
        //msgurl.setText(msg.getMsgUrl());
        try{
            myurl  = h.extractLinks(msg.getMsgComment())[0];
            if (myurl.trim() != "")
                msgurl.setText("לחץ כאן");
                msgurl.setTextColor(Color.parseColor("#0000FF"));
                msgurl.setPaintFlags(msgurl.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }catch(Exception e){
            msgurl.setText("");
        }

        msgcomment.setText(msg.getMsgComment().replace(myurl,""));
        //Log.d("mytag","url++: " +myurl);

        if (myurl != ""){
            msgurl.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String url = myurl;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        }

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

