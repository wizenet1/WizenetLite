package com.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.R;
import com.Classes.Ccustomer;
import com.Helper;
import com.Icon_Manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//import com.google.android.gms.maps.model.LatLng;


/**
 * Created by doron on 05/03/2016.
 */
public class FragmentCustomerContacts extends android.support.v4.app.Fragment{

    ListView myList; //mSearchNFilterLv
    private EditText mSearchEdt;
     //CustomersAdapter customersAdapter; //to refresh the list
    ArrayList<Ccustomer> data2 = new ArrayList<Ccustomer>() ;
    private TextWatcher mSearchTw;
//CustomersAdapter customersAdapter;
    String dataName,myString;
    //ImageButton goToTelephone, goToSms;
    TextView goToTelephone, goToSms;
    //EditText customer_search;
    CustomAdapter adapter; //for listview here
    String myBundle= "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.customer_fragment, null);
        mSearchEdt = (EditText) v.findViewById(R.id.mSearchEdt);
        //initUI
        Helper helper = new Helper();
        String mac = helper.getMacAddr();

        String strCID = "";
        try{
            strCID = this.getArguments().getString("CID");
        }catch (Exception ex){
            Toast.makeText(getActivity(),ex.getMessage().toString() , Toast.LENGTH_LONG).show();
        }

        try{
            for (Ccustomer c : getCustomersFromJson3()){
                if (strCID==c.getCParentID().toString()){
                    Toast.makeText(getActivity(),strCID + " " +c.getCParentID().toString() .toString() , Toast.LENGTH_LONG).show();
                    data2.add(c);
                }
                //data2.add(c.getCfname()+" "+c.getClname()+" "+c.getCcell());
            }
        }catch(Exception ex){
            Toast.makeText(getActivity(),ex.getMessage().toString() , Toast.LENGTH_LONG).show();
        }

        myList = (ListView) v.findViewById(R.id.customer_list);
        adapter = new CustomAdapter();
        myList.setAdapter(adapter);

//        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String u = data2.get(position).toString();
//                //Toast.makeText(getActivity(), "item click " + u, Toast.LENGTH_LONG).show();
//                alertDialog(data2.get(position));
//                dataName = "";
//            }
//        });
        //init

        //customersAdapter=new CustomersAdapter(data2,getContext());
        //myList.setAdapter(customersAdapter);
//        mSearchTw=new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                customersAdapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//
//            }
//        };
//        mSearchEdt.addTextChangedListener(mSearchTw);
        return v;
    };
//     for(Datapoint d : dataPointList){
//        if(d.getName() != null && d.getName().contains(search))
//        //something here
//    }
    //"contacts.txt")
//    private Ccustomer[] getcontctList(){
//        Helper helper = new Helper();
//        myString=helper.readTextFromFile3("contacts.txt");
//        JSONObject j = null;
//        int length = 0;
//        Ccustomer[] ccustomers ;//= new Ccustomer[5];
//        try {
//            j = new JSONObject(myString);
//            JSONArray jarray = j.getJSONArray("Contacts");
//            length = jarray.length();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        ccustomers = new Ccustomer[length];
//        ccustomers=     getCustomersFromJson3(myString);
//        return ccustomers;
//    }
//
    public Ccustomer[] getCustomersFromJson3(){
        Helper helper = new Helper();
        myString=helper.readTextFromFile3("contacts.txt");
        Ccustomer[] customersList = new Ccustomer[0];
        JSONObject j = null;
        try {
            j = new JSONObject(myString);
            //get the array [...] in json
            JSONArray jarray = j.getJSONArray("Contacts");
            customersList = new Ccustomer[jarray.length()];

            //customersList = new Ccustomer[jarray.length()];
            Toast.makeText(getActivity(),jarray.length() , Toast.LENGTH_LONG).show();
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);
                String fname = jarray.getJSONObject(i).getString("Cfname");
                String lname = jarray.getJSONObject(i).getString("Clname");
                String email = jarray.getJSONObject(i).getString("Cemail");
                String phone = jarray.getJSONObject(i).getString("Cphone");
                String cell = jarray.getJSONObject(i).getString("Ccell");
                String ccompany = jarray.getJSONObject(i).getString("Ccompany");
                String cid = jarray.getJSONObject(i).getString("CID");
                String cparent = jarray.getJSONObject(i).getString("CParentID");
                Toast.makeText(getActivity(),cparent , Toast.LENGTH_LONG).show();

                Ccustomer c = new Ccustomer(fname,lname,email,phone,cell,ccompany,cid,cparent);
                customersList[i] = c;
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return customersList;
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
                TextView name = (TextView) rowView.findViewById(R.id.textView);
                convertView = inflater.inflate(R.layout.customer,null);
                convertView.setTag(position);
                //צריך עכשיו לתפוס את הלייאאוט של קאסטומר ולתפוס את השם חברה


                goToTelephone = (TextView) convertView.findViewById(R.id.call);
                //id1 = (TextView) v.findViewById(R.id.id1);
                goToTelephone.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));

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
            convertView.setTag(convertView.getId(),position);
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View rowView = inflater.inflate(R.layout.customer, parent, false);
            TextView name = (TextView) rowView.findViewById(R.id.textView);

            dataName = name.getText().toString();
            //ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
            TextView nickname = (TextView) convertView.findViewById(R.id.textView);

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
            goToSms = (TextView) convertView.findViewById(R.id.sendsms);
            goToSms.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));
            goToSms.setTextSize(30);
            goToSms.setTag(position);
            goToSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("smsto:"+data2.get(position).getCcell());
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    startActivity(it);
                }
            });
            return convertView;
        }
    }
    public void alertDialog(Ccustomer c){
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
