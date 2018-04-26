package com.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.Activities.MenuActivity;
import com.Activities.R;
import com.Adapters.CustomersAdapter;
import com.Classes.Ccustomer;
import com.File_;
import com.Helper;
import com.Icon_Manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

//import com.google.android.gms.maps.model.LatLng;

public class FragmentCustomer extends android.support.v4.app.Fragment {

    ListView myList; //mSearchNFilterLv
    private EditText mSearchEdt;
    CustomersAdapter customersAdapter; //to refresh the list
    ArrayList<Ccustomer> data2 = new ArrayList<Ccustomer>();
    private TextWatcher mSearchTw;
    //CustomersAdapter customersAdapter;
    String dataName, myString;
    //ImageButton goToTelephone, goToSms;
    TextView goToTelephone, goToSms, goToWaze;
    //EditText customer_search;
    CustomAdapter adapter; //for listview here
    String myBundle = "";

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.customer_fragment, null);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn the action bar customers icon on, and the rest off.
        ((MenuActivity) getActivity()).turnActionBarClientsIconsOn();

        mSearchEdt = (EditText) v.findViewById(R.id.mSearchEdt);
        //initUI
        Helper helper = new Helper();
        String mac = helper.getMacAddr();

//        ActionBar actionBar = getActivity().getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayUseLogoEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(false);
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);
//        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
//        View customNav = LayoutInflater.from(getContext()).inflate(R.layout.top_bar_back, null); // layout which contains your button.
//
//        actionBar.setCustomView(customNav, lp1);
//        Button iv = (Button) customNav.findViewById(R.id.back);
//        iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //finish();
//                //Toast.makeText(getApplicationContext(),"clicked", Toast.LENGTH_LONG).show();
//            }
//        });
        data2.clear();
        for (Ccustomer c : getCustomerList()) {
            data2.add(c);
            //data2.add(c.getCfname()+" "+c.getClname()+" "+c.getCcell());
        }
       // ((MenuActivity) getActivity()).initialIcons();
        myList = (ListView) v.findViewById(R.id.customer_list);


        customersAdapter = new CustomersAdapter(data2, getContext());
        myList.setAdapter(customersAdapter);

        mSearchTw = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                customersAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        };
        mSearchEdt.addTextChangedListener(mSearchTw);

        FloatingActionButton floatingActionButton = (FloatingActionButton) v.findViewById(R.id.floating_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentCreateCustomer frag = new FragmentCreateCustomer();
                ft.replace(R.id.container, frag, "FragmentCreateCustomer");
                ft.addToBackStack("FragmentCreateCustomer");
                ft.commit();
            }
        });

        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //View view = inflater.inflate(R.layout.top_bar, container, false);
        //View view1 = view.findViewById(R.id.top_action_bar);
        //view1.setVisibility(View.VISIBLE);

        return v;
    }

    ;

//     for(Datapoint d : dataPointList){
//        if(d.getName() != null && d.getName().contains(search))
//        //something here
//    }

    @Override
    public void onResume() {
        super.onResume();

        if (getView() == null) {
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//                    getFragmentManager().popBackStack();
//                    // handle back button's click listener
//                    return true;
//                }

                return false;
            }
        });

        //Turn the action bar customers icon on, and the rest off.
        ((MenuActivity) getActivity()).turnActionBarClientsIconsOn();
    }

    private Ccustomer[] getCustomerList() {
        Helper helper = new Helper();
        File_ f = new File_();
        //myString = f.readFromFileInternal(getContext(),"customers.txt");
        myString = f.readFromFileExternal(getContext(), "customers.txt");
        //Log.e("mytag", myString);
        JSONObject j = null;
        int length = 0;
        Ccustomer[] ccustomers;//= new Ccustomer[5];
        try {
            j = new JSONObject(myString);
            JSONArray jarray = j.getJSONArray("Wz_Clients_List");
            length = jarray.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ccustomers = new Ccustomer[length];
        ccustomers = helper.getCustomersFromJson2(myString);
        return ccustomers;
    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data2.size();
        }

        @Override
        public Object getItem(int position) {
            return data2.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            Icon_Manager icon_manager;
            icon_manager = new Icon_Manager();
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View rowView = inflater.inflate(R.layout.customer, parent, false);
                TextView name = (TextView) rowView.findViewById(R.id.customers_list_item_company);
                convertView = inflater.inflate(R.layout.customer, null);
                convertView.setTag(position);
                //צריך עכשיו לתפוס את הלייאאוט של קאסטומר ולתפוס את השם חברה


                goToTelephone = (TextView) convertView.findViewById(R.id.customers_list_item_mobile_call);
                //id1 = (TextView) v.findViewById(R.id.id1);
                goToTelephone.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", getContext()));

                goToTelephone.setTextSize(30);
                goToTelephone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + "0526561633"));
                        startActivity(callIntent);
                    }
                });
            }
            convertView.setTag(convertView.getId(), position);
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View rowView = inflater.inflate(R.layout.customer, parent, false);
            TextView name = (TextView) rowView.findViewById(R.id.customers_list_item_company);

            dataName = name.getText().toString();
            //ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
            TextView nickname = (TextView) convertView.findViewById(R.id.customers_list_item_company);

            convertView.setTag(position);
            nickname.setText(data2.get(position).getCcompany());//+" "+
            //data2.get(position).getClname()+" "
            //+data2.get(position).getCcell());
            //TextView tv = (TextView) convertView.findViewById(R.id.textView);
            //tv.setOnClickListener(new View.OnClickListener() {
            //   @Override
            //    public void onClick(View v) {
            //        alertDialog(data2.get(position));
//                    Uri uri = Uri.parse("smsto:"+data2.get(position).getCcell());
//                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
//                    startActivity(it);
            //    }
            // });
            goToSms = (TextView) convertView.findViewById(R.id.customer_list_item_sendsms);
            goToSms.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", getContext()));
            goToSms.setTextSize(30);
            goToSms.setTag(position);
            goToSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("smsto:" + data2.get(position).getCcell());
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    startActivity(it);
                }
            });

            return convertView;
        }
    }

    public void alertDialog(Ccustomer c) {
        LayoutInflater factory = LayoutInflater.from(getActivity());

//text_entry is an Layout XML file containing two text field to display in alert dialog
        final View textEntryView = factory.inflate(R.layout.text_entry, null);

        final EditText input1 = (EditText) textEntryView.findViewById(R.id.EditText1);
        final EditText input2 = (EditText) textEntryView.findViewById(R.id.EditText2);


        input1.setText(c.getCemail(), TextView.BufferType.EDITABLE);
        input2.setText(c.getCcell(), TextView.BufferType.EDITABLE);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        //alert.setIcon(R.drawable.message_icon);
        alert.setTitle("מייל וטלפון");
//        alert.setView(textEntryView).setPositiveButton("Save",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,
//                                        int whichButton) {
//
//
//                    }
//                });


        alert.setView(textEntryView).setNegativeButton("סגור",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                    }
                });
        alert.show();
    }

    //###################################
    //EXTRACT CUSTOMERS FROM JSON
    //###################################
//    public Ccustomer[] getCustomersFromJson(String json){
//        Ccustomer[] customersList = new Ccustomer[0];
//        JSONObject j = null;
//        try {
//            j = new JSONObject(json);
//            //get the array [...] in json
//            JSONArray jarray = j.getJSONArray("Customers");
//            customersList = new Ccustomer[jarray.length()];
//            //customersList = new Ccustomer[jarray.length()];
//
//            for (int i = 0; i < jarray.length(); i++) {
//                JSONObject object = jarray.getJSONObject(i);
//                String fname = jarray.getJSONObject(i).getString("Cfname");
//                String lname = jarray.getJSONObject(i).getString("Clname");
//                String email = jarray.getJSONObject(i).getString("Cemail");
//                String phone = jarray.getJSONObject(i).getString("Cphone");
//                String cell = jarray.getJSONObject(i).getString("Ccell");
//                String ccompany = jarray.getJSONObject(i).getString("Ccompany");
//                Ccustomer c = new Ccustomer(fname,lname,email,phone,cell,ccompany);
//                customersList[i] = c;
//            }
//        } catch (JSONException e1) {
//            e1.printStackTrace();
//        }
//        return customersList;
//    }
//    public String getMacAddress() {
//        WifiManager manager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = manager.getConnectionInfo();
//        String address = info.getMacAddress();
//        return address;
//    }
}
