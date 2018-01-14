package com.Fragments;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.Classes.Message;
import com.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 31/08/2016.
 */
public class FragmentMessage extends android.support.v4.app.Fragment {


    EditText key_et, val_et;
    Button addmem_btn,remove_btn;
    DatabaseHelper db;
    ListView myList;
    CustomAdapter adapter;
    List<Message> data2 = new ArrayList<Message>() ;
    String dataName;
    CheckBox cb;
    LocationManager manager = null;
    String firstname="";
    String lastname="";
    boolean result = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.message_fragment, null);
        setHasOptionsMenu(true);
        db = DatabaseHelper.getInstance(getContext());


        for(int i =0;i < data2.size();i++)
        {
            data2.remove(i);
        }
        data2 = new ArrayList<Message>() ;
        List<Message> cps=  db.getAllMessages();  // getCustomersFromJson(myBundle);
        for (Message c : cps){
            data2.add(c);
        }
        myList = (ListView) v.findViewById(R.id.messages_list);
        myList.setClickable(true);
        myList.setLongClickable(true);
        adapter = new CustomAdapter();
        myList.setAdapter(adapter);
        //myList.setBackgroundColor(Color.parseColor("#cdebf9"));


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(getActivity().getApplicationContext(),FragmentMessageDetails.class);
                //intent.putExtra("id",data2.get(position).getMsgID());
                goToMSGDetailsFrag(data2.get(position).getMsgID().toString());
                //Toast.makeText(getActivity(), data2.get(position).getMsgID(), Toast.LENGTH_LONG).show();
                //startActivity(intent);
            }
        });
        return v;
    };
//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        MenuItem item = menu.findItem(R.menu.menu_main);
//        item.setVisible(false);
//    }


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


    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data2.size();
        }

        @Override
        public Object getItem(int position) {
            return data2.get(position).toString();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                //convertView = inflater.inflate(R.layout.item_message,null);
            }
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            convertView = inflater.inflate(R.layout.item_message,null);
            //View rowView = inflater.inflate(R.layout.item_message, parent, false);

            TextView key = (TextView) convertView.findViewById(R.id.key_text);
            //EditText value = (EditText) convertView.findViewById(R.id.value_text);
            //Button remove_btn = (Button) convertView.findViewById(R.id.btn_remove);

            key.setText(data2.get(position).getMsgSubject().toString());


            myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(final AdapterView<?> p, View v, final int po, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Delete");
                    builder.setMessage("Are you sure you want to delete?");
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int ii) {
                            DatabaseHelper.getInstance(getContext()).deleteRowByMsgID(data2.get(po).getMsgID());
                            Toast.makeText(getActivity(), data2.get(po).getMsgID().toString()+",\nMsgName:"+data2.get(po).getMsgComment().toString(), Toast.LENGTH_LONG).show();

                            refresh();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int ii) {
                                    dialog.dismiss();
                                }
                            }
                    );
                    builder.show();

                    return true;
                }
            });
            //value.setText(data2.get(position).getMsgComment().toString());

            convertView.setTag(position);
            //nickname.setText(data2.get(position).getKey());

            return convertView;
        }
    }


    public void refresh(){
        List<Message> data = new ArrayList<Message>() ;
        List<Message> cps=  db.getAllMessages();  // getCustomersFromJson(myBundle);
        for (Message c : cps){
            data.add(c);
        }
        data2.clear();
        data2=data;
        adapter = new CustomAdapter();
        myList.setAdapter(adapter);
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



}
