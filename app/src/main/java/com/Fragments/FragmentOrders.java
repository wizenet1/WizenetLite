package com.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.R;
import com.Adapters.CustomersAdapter;
import com.Alarm_Receiver_sync_client_products;
import com.Classes.Order;
import com.DatabaseHelper;
import com.File_;
import com.Helper;
import com.Adapters.NamesAdapter;
import com.Adapters.OrdersAdapter;
import com.Icon_Manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

//import com.google.android.gms.maps.model.LatLng;


/**
 * Created by doron on 05/03/2016.
 */
public class FragmentOrders extends android.support.v4.app.Fragment{
    Helper helper;
    String strJson = "";
    Button scan;
    EditText txt_serial;
    CustomAdapter adapter;

    //auto_complete_id;
    ListView myList; //mSearchNFilterLv
    private EditText mSearchEdt;
     CustomersAdapter customersAdapter; //to refresh the list
    ArrayList<Order> data2 = new ArrayList<Order>() ;

//CustomersAdapter customersAdapter;
    String dataName,myString;
    String selectionItem;
    Button delete;
    String pmakat;
    String _CID;
    //ImageButton goToTelephone, goToSms;
    EditText amount_id,price_id;
    EditText comments_id,lblPoprice;
    //EditText customer_search;
    String myBundle= "";
    File_ f;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orders, null);
        f = new File_();
        _CID = "0";
        helper = new Helper();
        lblPoprice= (EditText) v.findViewById(R.id.lblPoprice);
        comments_id = (EditText) v.findViewById(R.id.comments_id);
        Button btn_add = (Button) v.findViewById(R.id.btn_add);
        Button btn_submit = (Button) v.findViewById(R.id.btn_submit);
        //data2.add(new Order("asdf","adfg"));

        amount_id = (EditText) v.findViewById(R.id.amount_id);
        price_id = (EditText) v.findViewById(R.id.price_id);
        amount_id.setSelectAllOnFocus(true);
        price_id.setSelectAllOnFocus(true);
        AlretIfNotFinishSync();


        List<String> responseList2 = new ArrayList<String>();
        responseList2 = getSpecialCustomerList();


        final AutoCompleteTextView textViewClients = (AutoCompleteTextView)
                v.findViewById(R.id.auto_complete_id);
        final AutoCompleteTextView textView_products_all = (AutoCompleteTextView)
                v.findViewById(R.id.auto_complete_id2);
        final AutoCompleteTextView textView_products_client = (AutoCompleteTextView)
                v.findViewById(R.id.auto_complete_id_client);

        List<String> namesList = responseList2; //your names list;
        NamesAdapter namesAdapter = new NamesAdapter(
                getContext(),
                R.layout.search_auto_complete,
                R.id.name_search,
                namesList
        );

        textView_products_all.setAdapter(namesAdapter);


        List<Order> responseList = new ArrayList<Order>();
        List<Order> responseListClient = new ArrayList<Order>();


        if (DatabaseHelper.getInstance(getContext()).mgnet_items_isEmpty("all")){
            try{

                //strJson = helper.readTextFromFile3("productss.txt");
                strJson = f.readFromFileExternal(getContext(),"productss.txt");
                strJson=strJson.replace("PRODUCTS_ITEMS_LISTResponse","");
                strJson=strJson.replace("PRODUCTS_ITEMS_LISTResult=","Orders:");
                JSONObject j = null;
                JSONArray jarray = null;
                j = null;
                jarray = null;
                try {
                    j = new JSONObject(strJson);
                    jarray= j.getJSONArray("Orders");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jarray.length(); i++) {
                    final JSONObject e;
                    String name = "";
                    try {
                        e = jarray.getJSONObject(i);
                        name = e.getString("Pname")+'|'+e.getString("Pmakat");
                        Order order = new Order(
                                e.getString("Pname"),
                                e.getString("Pmakat"),
                                (String.format("%.2f", Double.parseDouble( e.getString("Pprice")))),
                                (String.format("%.2f", Double.parseDouble( e.getString("Poprice"))))
                        );
                        responseList.add(order);

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                }
            }catch(Exception e){
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }else{
            responseList = DatabaseHelper.getInstance(getContext()).get_mgnet_items("all");
            //Toast.makeText(getActivity(),"responseList size: "+ responseList.size(), Toast.LENGTH_LONG).show();
        }
        // FOR CLIENT

        if (_CID == "0"){
            Log.e("MYTAG","is emptyyyyyyyyyyyyyyy");
        }else{
            //responseListClient =getClientOrderList(_CID);
            //responseListClient = DatabaseHelper.getInstance(getContext()).get_mgnet_items("client");
            Toast.makeText(getActivity(),"responseList size: "+ responseList.size(), Toast.LENGTH_LONG).show();
            Log.e("MYTAG","responseList size: "+ responseList.size());
        }

        List<Order> ordersList = new ArrayList<Order>();
        ordersList = responseList; //your names list;
        OrdersAdapter ordersAdapter = new OrdersAdapter(
                getContext(),
                R.layout.search_auto_complete,
                R.id.name_search,
                ordersList,
                "all"
        );

        //String s = textView.getText().subSequence(textView.getText().in)



        myList = (ListView) v.findViewById(R.id.customer_list);
        setListViewHeightBasedOnChildren(myList);


        adapter = new CustomAdapter();
        myList.setAdapter(adapter);
        //set adapter into listStudent

        textViewClients.setAdapter(ordersAdapter);




        // #### GET THE SELECTED ITEM ###

        textViewClients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String)parent.getItemAtPosition(position);
                pmakat = selection.substring(selection.lastIndexOf("|")+1,selection.lastIndexOf("~"));
                price_id.setText(selection.substring(selection.lastIndexOf("~")+1, (selection.lastIndexOf("*"))));
                lblPoprice.setText(selection.substring(selection.lastIndexOf("*")+1,selection.length()));
//                price_id.setText(selection.substring(selection.lastIndexOf("~")+1,selection.length()));
                amount_id.setText("1");
                selectionItem = selection.substring(0,selection.lastIndexOf("|"));


//                List<Order> ordersListClient = new ArrayList<Order>();
//                ordersListClient =getClientOrderList(_CID);
//                Toast.makeText(getContext(),"_CID: " +_CID, Toast.LENGTH_LONG).show();
//                OrdersAdapter ordersAdapter2 = new OrdersAdapter(
//                        getContext(),
//                        R.layout.search_auto_complete,
//                        R.id.name_search,
//                        ordersListClient,
//                        selectionItem
//                );
//                textView3.setAdapter(ordersAdapter2);

                //Toast.makeText(getActivity(), selection + " success", Toast.LENGTH_LONG).show();
                Log.e("MYTAG",selection.toString());
            }
        });
        textView_products_client.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String)parent.getItemAtPosition(position);
                pmakat = selection.substring(selection.lastIndexOf("|")+1,selection.lastIndexOf("~"));
                price_id.setText(selection.substring(selection.lastIndexOf("~")+1, (selection.lastIndexOf("*"))));
                lblPoprice.setText(selection.substring(selection.lastIndexOf("*")+1,selection.length()));
                amount_id.setText("1");
                selectionItem = selection.substring(0,selection.lastIndexOf("|"));
                //Toast.makeText(getActivity(), selection + " success", Toast.LENGTH_LONG).show();
                Log.e("MYTAG",selection.toString());
            }
        });

        textView_products_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String)parent.getItemAtPosition(position);
                _CID = selection.substring(selection.lastIndexOf("|")+1,selection.length());
                String _Cusername = selection.substring(selection.lastIndexOf("~")+1,selection.length());
                Log.e("MYTAG",selection.toString());
                Log.e("MYTAG","_CID : " + _CID);
            try{
                List<Order> ordersListClient = new ArrayList<Order>();
                ordersListClient =helper.getClientOrderList(_Cusername,getContext());
                //Toast.makeText(getContext(),"_CID: " +_CID + "__Cusername: " +_Cusername, Toast.LENGTH_LONG).show();
                //Toast.makeText(getContext(),ordersListClient.toString(), Toast.LENGTH_LONG).show();
                OrdersAdapter ordersAdapter2 = new OrdersAdapter(
                        getContext(),
                        R.layout.search_auto_complete,
                        R.id.name_search,
                        ordersListClient,
                        _Cusername
                );
                textView_products_client.setAdapter(ordersAdapter2);
            }catch (Exception ex){
                Toast.makeText(getContext(),"מוצרי לקוח עדיין לא התווספו", Toast.LENGTH_LONG).show();
            }

            }
             });

        textViewClients.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textViewClients.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    textViewClients.setText("");
                    amount_id.setText("");
                    price_id.setText("");
                    pmakat = "";
                    selectionItem = "";
                }
                return false;
            }
        });
        textView_products_client.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    textView_products_client.setText("");
                    amount_id.setText("");
                    price_id.setText("");
                    pmakat = "";
                    selectionItem = "";
                }
                return false;
            }
        });

        lblPoprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!lblPoprice.getText().equals("")){
                    price_id.setText(lblPoprice.getText());
                }
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((textViewClients.getText().toString() != "" && (textViewClients.getText().toString().contains("|"))
                ||
                        (textView_products_client.getText().toString() != "" && (textView_products_client.getText().toString().contains("|")))
                )) {
                    //Order(String pname,String pmakat,String pprice,String poprice,String pq)
                    data2.add(new Order(selectionItem,pmakat,price_id.getText().toString(),"",amount_id.getText().toString()));
                    adapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(myList);
                    textViewClients.setText("");
                    textView_products_client.setText("");
                    amount_id.setText("");
                    pmakat = "";
                    price_id.setText("");
                    selectionItem = "";
                    hideKeyboardFrom(getContext(),v);
                //}
                //else if(textView3.getText().toString() != "" && (textView3.getText().toString().contains("|")))
                //{
                //    //Order(String pname,String pmakat,String pprice,String poprice,String pq)
               //     data2.add(new Order(selectionItem,pmakat,price_id.getText().toString(),"",amount_id.getText().toString()));
                //    adapter.notifyDataSetChanged();
                //    setListViewHeightBasedOnChildren(myList);
                //    textView.setText("");
                //    textView3.setText("");
                //    amount_id.setText("");
                //    pmakat = "";
                 //   price_id.setText("");
                 //   selectionItem = "";
                 //   hideKeyboardFrom(getContext(),v);
                }else{
                    Toast.makeText(getActivity(), "יש לבחור מוצר", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView_products_all.getText().toString() == "") {
                    Toast.makeText(getActivity(), "יש לבחור לקוח", Toast.LENGTH_LONG).show();
                }else{
                    if(textView_products_all.getText().toString().contains("|")){
                        String real_cid =textView_products_all.getText().toString()
                                .substring(textView_products_all.getText().toString().lastIndexOf("|")+1,
                                        textView_products_all.getText().toString().lastIndexOf("~"));
                       // Toast.makeText(getActivity(), "real_cid: " + real_cid, Toast.LENGTH_LONG).show();
                        createOrder(real_cid);
                        comments_id.setText("");
                        data2.clear();
                        adapter.notifyDataSetChanged();
                        hideKeyboardFrom(getContext(),v);
                    }else{
                        Toast.makeText(getActivity(), "יש לבחור לקוח", Toast.LENGTH_LONG).show();
                        hideKeyboardFrom(getContext(),v);
                    }
                }
            }
        });

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });
        return v;
    };
    protected List<String> getSpecialCustomerList(){
        List<String> ret = new ArrayList<String>();
        File_ f = new File_();
        //strJson = helper.readTextFromFileCustomers();
        strJson = f.readFromFileExternal(getContext(),"customers.txt");
        JSONObject j = null;
        JSONArray jarray = null;
        try {
            j = new JSONObject(strJson);
            jarray= j.getJSONArray("Customers");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < jarray.length(); i++) {
                final JSONObject e;
                String name = null;
                try {
                    e = jarray.getJSONObject(i);
                    name = e.getString("Ccompany")+'|'+e.getString("CID")+"~"+e.getString("Cusername");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                ret.add(name);
            }
        } catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return ret;
    }
    protected void AlretIfNotFinishSync(){
        int len = 0;
        int count=0;
        try{
            //-------------------------------------------------
            List<String> listCIDS = new ArrayList<String>();
            listCIDS=helper.getCIDSlist();
            len = listCIDS.size();
            //-------------------------------------------------
            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/client_products");
            File[] list = myFile.listFiles();
            for (File f: list){
                String name = f.getName();
                if (name.endsWith(".txt"))
                    count++;
            }
        }catch (Exception ex){

        }
        Intent alarm = new Intent(getContext(), Alarm_Receiver_sync_client_products.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getContext(), 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmRunning == true || ((len != count) || (count == 0))) {
            String msg = "";
            if (alarmRunning == true){msg="<font color='#FF7F27'>סנכרון מוצרי לקוח רץ ברקע  "+count+"/"+len+"</font>";}
            else{msg="<font color='#FF7F27'>"+String.valueOf(count) + "סונכרנו מתוך"+ String.valueOf(len)+"</font>";}
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(Html.fromHtml(msg));
            builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>אישור</font>"), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {

                }
            });
            builder.create();
            builder.show();
        }

//        if ((len != count) || (count == 0)) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//            builder.setTitle(Html.fromHtml("<font color='#FF7F27'>סנכרון מוצרי לקוח עדיין לא הושלם  "+count+"/"+len+"</font>"));
//            builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>אישור</font>"), new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int arg1) {
//
//                }
//            });
//            builder.create();
//            builder.show();
//        }

    }


    public void createOrder(String cid){
        JSONObject resultMain = new JSONObject();
        JSONArray resultHeader     = new JSONArray();
        JSONArray resultItems = new JSONArray();

                  try
                    {

                        for (int i=0;i<data2.size();i++){

                            JSONObject rowObject = new JSONObject();
                            Log.d("TAG_NAME",  data2.get(i).getPmakat() );
                            rowObject.put("Pmakat" , data2.get(i).getPmakat() );
                            rowObject.put("Pname" , data2.get(i).getPname() );
                            rowObject.put("PQ" , data2.get(i).getPq() );
                            rowObject.put("Pprice" , data2.get(i).getPprice() );
                            resultItems.put(rowObject);
                        }
                        JSONObject rowObjectHeader = new JSONObject();
                        rowObjectHeader.put("CID" , cid );
                        rowObjectHeader.put("COMMENT" , comments_id.getText().toString() );
                        rowObjectHeader.put("TYPE" ,"ORDER" );
                        resultHeader.put(rowObjectHeader);


                        //resultMain.put("header",resultHeader);
                        //resultMain.put("content",resultItems);
                        writeTextToFileCustom(cid,resultHeader + "#" + resultItems);
                        Toast.makeText(getActivity(),"Order is created", Toast.LENGTH_LONG).show();
                        data2.clear();

                    }
                    catch( Exception e )
                    {
                        Log.d("TAG_NAME", e.getMessage()  );
                    }



        //cursor.close();
        Log.d("TAG_NAME", resultMain.toString() );
        //return resultSet;
    }

    public boolean writeTextToFileCustom(String prefix,String stringparameter){

        File pathOrders = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/offline");
        if (!pathOrders.exists())
            pathOrders.mkdir();

        // get the path to sdcard
        String myUrl;
        if(stringparameter.length()>0){
            myUrl = stringparameter;
        }else{
            myUrl = null;//DEMOURL;
        }
        try {
            int num = 0;
            String path = Environment.getExternalStorageDirectory().getPath()+"/wizenet/offline/"+prefix + ".txt";
            File myFile = new File(path);
            while(myFile.exists()) {
                path = Environment.getExternalStorageDirectory().getPath() + "/wizenet/offline/" + prefix + "_"+ (num++) + ".txt";
                myFile = new File(path);
            }
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.write(myUrl);
            myOutWriter.close();
            fOut.close();
            //Toast.makeText(getApplicationContext(), "File_ Created", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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
                View rowView = inflater.inflate(R.layout.item_order, parent, false);
                TextView name = (TextView) rowView.findViewById(R.id.textView);
                convertView = inflater.inflate(R.layout.item_order,null);
                convertView.setTag(position);
                //צריך עכשיו לתפוס את הלייאאוט של קאסטומר ולתפוס את השם חברה


//                goToTelephone = (TextView) convertView.findViewById(R.id.item_call);
//                //id1 = (TextView) v.findViewById(R.id.id1);
//                goToTelephone.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));
//
//                goToTelephone.setTextSize(30);
//                goToTelephone.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent callIntent = new Intent(Intent.ACTION_CALL);
//                        callIntent.setData(Uri.parse("tel:" + "0526561633"));
//                        startActivity(callIntent);
                    //}
               // });
            }
            convertView.setTag(convertView.getId(),position);
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View rowView = inflater.inflate(R.layout.customer, parent, false);
            TextView name = (TextView) rowView.findViewById(R.id.textView);

            dataName = name.getText().toString();
            //ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
            TextView nickname = (TextView) convertView.findViewById(R.id.textView);

            convertView.setTag(position);
            nickname.setText(data2.get(position).getPname() + " "+data2.get(position).getPmakat());//+" "+
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
            delete = (Button) convertView.findViewById(R.id.delete);
            delete.setTag(position);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data2.remove(position);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }
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

