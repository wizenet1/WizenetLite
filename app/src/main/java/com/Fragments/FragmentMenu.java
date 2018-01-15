package com.Fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.ActivityCalls;
import com.Activities.MenuActivity;
import com.Activities.R;
import com.DatabaseHelper;
import com.Helper;
import com.Icon_Manager;
import com.ProgressTaskAll;
import com.ProgressTaskClient;
import com.model.Model;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 20/09/2016.
 */



public class FragmentMenu extends android.support.v4.app.Fragment  {
    TextView tv_username;
    CheckBox cb;
    Button btn2,mapid;
    EditText myEditText2;
    //BarCodeActivity barCodeActivity;
    Helper helper;
    TextView id1,id2,id3,id4,id5,id6,id7,id8,id9;
    TextView id1_text,id2_text,id3_text,id4_text,id5_text,id6_text,id7_text,id8_text,id9_text;
    DatabaseHelper db;
    Icon_Manager icon_manager;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        //context = this.getContext();
        View v = inflater.inflate(R.layout.menu_fragment, null);

        ImageView id_customers = (ImageView) v.findViewById(R.id.id_customers);
        ImageView id_calls = (ImageView) v.findViewById(R.id.id_calls);

        //db = DatabaseHelper.getInstance(getActivity().getApplicationContext());
       // icon_manager = new Icon_Manager();
        //helper = new Helper();

            id_customers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager2 = getFragmentManager();
                    FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                    FragmentCustomer fragment2 = new FragmentCustomer();
                    fragmentTransaction2.addToBackStack("xyz");
                    fragmentTransaction2.hide(FragmentMenu.this);
                    fragmentTransaction2.add(R.id.container, fragment2);
                    fragmentTransaction2.commit();
                }
            });
       try{
           id_calls.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
//                   Intent intent = new Intent(getContext(), ActivityCalls.class);
//                   startActivity(intent);
                   Intent intent = new Intent(getActivity(), ActivityCalls.class);
                   startActivity(intent);
               }
           });
       }catch (Exception ex){

           Log.e("mytag",ex.getMessage());
       }


//        int len = 0;
//        int count = 0;
//        try{
//            // ---------client products first time ------------
//            //-------------------------------------------------
//            List<String> listCIDS = new ArrayList<String>();
//            listCIDS=helper.getCIDSlist();
//            len = listCIDS.size();
//            //-------------------------------------------------
//            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/client_products");
//            File[] list = myFile.listFiles();
//            if (list.length == 0) {
//                len = 0;
//            }else
//                {
//                for (File f: list){
//                    String name = f.getName();
//                    if (name.endsWith(".txt"))
//                        count++;
//                }
//            }
//
//        }catch (Exception ex){
//
//        }
//        // continue to sync products if he back to app.
//        if (count == 0) {
//            //Log.e("chkchk","doron1");
//            //helper.deleteProductsFiles();
//            helper.startService_sync_products(getContext());
//        }
//        if ((len != count) && (len != 0)) {
//            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/customers.txt");
//            if (myFile.exists()){
//                //helper.deleteProductsFiles();
//                //Log.e("chkchk","doron2");
//                helper.startService_sync_products(getContext());
//
//                Toast.makeText(getContext(), "products sync run in the background", Toast.LENGTH_LONG).show();
//            }
//        }
//
//        if (db.getValueByKey("CLIENT_SYNC_PRODUCTS").toString().equals("1")){
//            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/productss.txt");
//            if (db.mgnet_items_isEmpty("all") || (!myFile.exists())){
//                //AlertDialogAllFirstTime();
//            }
//            // ---------ALL products first time ------------
//            //syncCustomersFirstTime();
//
//            //chkUpdateProducts();
//        }




        //id7.setTextSize(60);
//        id1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                helper.goToMessagesFragment(context);
//                }
//        //}
//        });
//        id2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                helper.goTocustomers(context);
//            }
//        });
//        id3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {helper.goToLoginReportFragment(context);}});
//        id4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                helper.goToOfflineMenuFragment(context);
//            }
//        });
//        id5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                helper.goToCPFragment(context);
//            }
//        });
//        id5.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                //helper.goToCPFragment2(context);
//                //Toast.makeText(getActivity().getApplicationContext(),"w2" , Toast.LENGTH_LONG).show();
//                return true;
//            }
//        });
//         id6.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 helper.goToToolsFragment(context);
//             }
//         });
//        id7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ActivityCalls.class);
//                startActivity(intent);
            //}
        //});

//        id8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String url = db.getInstance(getActivity().getApplicationContext()).getValueByKey("URL").toString();
//                if(url.equals("")) {
//                    Toast.makeText(getActivity().getApplicationContext(),"wrong url" , Toast.LENGTH_LONG).show();
//                }
//                else{
//                    Uri uri = Uri.parse(url + "/mobile"); // missing 'http://' will cause crashed
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
//                }
//            }
//        });
//        id7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                helper.goToOfflineMenuFragment(context);
//            }
//        });

//        mapid = (Button) v.findViewById(R.id.mapid) ; TODO add uncomment if needed
//
//        //btn2 = (Button) v.findViewById(R.id.customers) ;
//        tv_username = (TextView) v.findViewById(R.id.tv_username);
//        cb = (CheckBox) v.findViewById(R.id.checkBox3);
//        if(DatabaseHelper.getInstance(getContext()).getValueByKey("GPS").equals("1")){
//            cb.setChecked(true);
//            cb.setText("GPS");
//        }else{
//            cb.setVisibility(View.GONE);
//        }



//        try{
//            Bundle bundle = getArguments();
//            String mDocNum = bundle.getString("DocNum");
//            myEditText2.setText(mDocNum);
//        }catch (Exception ex){
//            Toast.makeText(getActivity(),ex.toString(),Toast.LENGTH_SHORT).show();
//        }



//        cb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(cb.isChecked()){
//                    DatabaseHelper.getInstance(getContext()).updateValue("GPS","0");
//                    ((MenuActivity)getActivity()).stopRepeatingTask();
//                    cb.setVisibility(View.GONE);
//                }
//
//            }
//        });


        return v;
    }
    public void syncCustomersFirstTime(){
        File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/customers.txt");
        if (!myFile.exists()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("אנשי קשר לא קיימים");
            builder.setMessage("האם תרצה לסנכרן אנשי קשר?");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int ii) {
                    if (helper.isNetworkAvailable(getContext())){
                        Toast.makeText(getActivity(),"מסנכרן לקוחות",Toast.LENGTH_SHORT).show();


                            Model.getInstance().Async_Get_Clients_Listener(helper.getMacAddr(), new Model.get_clients_Listener() {
                                @Override
                                public void onResult(String str) {
                                    //Toast.makeText(getApplication(), str.toString(), Toast.LENGTH_SHORT).show();
                                    //strBundle = str;
                                    helper.writeTextToFile2(str);
                                }
                            });
                    }else{
                        Toast.makeText(getActivity(),"network is Not available",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("לא", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int ii) {
                            dialog.dismiss();
                        }
                    }
            );
            builder.show();
        }

    }
    protected void AlertDialogAllFirstTime(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("רשימת כל המוצרים");
        builder.setMessage("האם תרצה לסנכרן את כל המוצרים?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int ii) {
                boolean flag;
                flag = db.delete_from_mgnet_items("all");
                if (flag == true){
                    if (db.mgnet_items_isEmpty("all")){
                        new ProgressTaskAll(context).execute();
                    }
                }
            }
        });
        builder.setNegativeButton("לא", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int ii) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }
    protected void AlertDialogClientFirstTime(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("רשימת מוצרי לקוח לא הושלמה");
        builder.setMessage("האם תרצה לסנכרן מוצרי לקוח?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int ii) {

//                backServices.startService(new Intent(getContext(), BackServices.class));
                helper.startService_sync_products(getContext());
                Toast.makeText(getContext(), "startService_sync_products", Toast.LENGTH_LONG).show();
                       // new ProgressTaskClient(getContext()).execute();
                    }
        });
        builder.setNegativeButton("לא", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int ii) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }


    protected  void setUsername(){
        try{
            Model.getInstance().Async_User_Details_Listener(helper.getMacAddr(), new Model.User_Details_Listener() {
                @Override
                public void onResult(String str) {
                    tv_username.setText("שלום "+str);
                    //Log.e("myTag",str);
                }
            });
        }catch (Exception ex){
            Log.e("MYTAG",ex.getMessage());
        }
    }
    public void chkUpdateProducts(){


//------------------------
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sdt = df.format(new Date(System.currentTimeMillis()));
        String date_CLIENTS_PRODUCTS_UPDATE = db.getValueByKey("CLIENTS_PRODUCTS_UPDATE");
        String date_ALL_PRODUCTS_UPDATE = db.getValueByKey("PRODUCTS_UPDATE");


        Date convertedDate_all = new Date();
        Date convertedDate_client = new Date();
        Date currentTime = Calendar.getInstance().getTime();
        try {
            convertedDate_all = df.parse(date_ALL_PRODUCTS_UPDATE);
            convertedDate_client = df.parse(date_CLIENTS_PRODUCTS_UPDATE);
            //### to delete #####
            //SimpleDateFormat dfTry = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //Calendar c_week = Calendar.getInstance();
            //c_week.add(Calendar.MINUTE, 3);
            //Log.e("MYTAG","current:"+ currentTime.toString()+ " is after -try try- :"+c_week.getTime().toString() + "?");

            //##################
            Log.e("MYTAG","current:"+ currentTime.toString()+ " is after convertedDate_all:"+convertedDate_all.toString() + "?");
            if (currentTime.after((convertedDate_all))) {
                AlertDialogAll();
            }
            Log.e("MYTAG","current:"+ currentTime.toString()+ " is after convertedDate_all:"+convertedDate_client.toString() + "?");
            if (currentTime.after(convertedDate_client)) {
                AlertDialogClient();
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        Log.e("MYTAG","client:"+ convertedDate_client.toString()+
//                " \nnow:"+currentTime.toString() +
//                "\n all:"+ convertedDate_all.toString());

//            Toast.makeText(getActivity().getApplicationContext(),
//                    "yesterday-client"+ client.toString()+
//                            " \nnow:"+now.toString() +
//                            "\n all:"+ all.toString() , Toast.LENGTH_LONG).show();

//---------------------------


    }
    protected void AlertDialogAll(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("סנכרון רשימת כל המוצרים");
        builder.setMessage("לא בוצע סנכרון כבר 7 ימים, האם לסנכרן?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int ii) {
                boolean flag;
                flag = db.delete_from_mgnet_items("all");
                if (flag == true){
                    if (db.mgnet_items_isEmpty("all") == true){

                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar c_week = Calendar.getInstance();
                        c_week.add(Calendar.DAY_OF_YEAR, 7);
                        String formatted = df.format(c_week.getTime());
                        db.updateValue("PRODUCTS_UPDATE",formatted);


                        new ProgressTaskAll(context).execute();
                    }
                }
            }
        });
        builder.setNegativeButton("לא", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int ii) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }
    protected void DeleteForExample(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("סנכרון רשימת כל המוצרים");
        builder.setMessage("מחק מוצרי לקוח?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int ii) {
                try{
                    File dir = new File(Environment.getExternalStorageDirectory()+"/wizenet/client_products");
                    if (dir.isDirectory())
                    {
                        String[] children = dir.list();
                        for (int i = 0; i < children.length; i++)
                        {
                            new File(dir, children[i]).delete();
                        }
                    }
                }catch(Exception ex){
                        Log.e("MYTAG","failed to delete");
                }

            }
        });
        builder.setNegativeButton("לא", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int ii) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }

    protected void AlertDialogClient(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("סנכרון רשימת מוצרי לקוח");
        builder.setMessage("לא בוצע סנכרון היום, האם לסנכרן?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int ii) {


                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar c_week = Calendar.getInstance();
                        c_week.add(Calendar.DAY_OF_YEAR, 7);
                        String formatted = df.format(c_week.getTime());
                        db.updateValue("CLIENTS_PRODUCTS_UPDATE",formatted);

                        helper.deleteProductsFiles();
                        new ProgressTaskClient(context).execute();


            }
        });
        builder.setNegativeButton("לא", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int ii) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }
//    public class ProgressTaskAll extends AsyncTask<String, String, String> {
//        private ProgressDialog dialog;
//        List<Message> titles;
//
//        //private List<Message> messages;
//        public ProgressTaskAll() {
//            dialog = new ProgressDialog(getContext());
//        }
//        @Override
//        protected void onPreExecute() {
//            //this.working_dialog = ProgressDialog.show(getContext(), "","Working please wait...", true);
//
//            this.dialog.setMessage("Working please wait...");
//            this.dialog.show();
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    dialog.dismiss();
//                }
//            }, 5000);
//        }
//        @Override
//        protected void onPostExecute(final String success) {
//        }
//        @Override
//        protected String doInBackground(final String... args) {
//            helper.ALLProductsSync(getContext());
//            return "";
//        }
//    }




    @Override
    public void onResume() {
        super.onResume();
        //setUsername();
//        if(DatabaseHelper.getInstance(getContext()).getValueByKey("GPS").equals("1")) {
//            cb.setChecked(true);
//            cb.setText("GPS");
//            cb.setVisibility(View.VISIBLE);
//        }

//        try{
//            myEditText2.setText(((MenuActivity)getActivity()).getMyString());
//        }catch (Exception ex){
//            Toast.makeText(getActivity(),ex.toString(),Toast.LENGTH_SHORT).show();
//        }
    }
    public String getEditText(){
        return myEditText2.getText().toString();
    }



    public void goToMapFragment(){
        android.support.v4.app.FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        //MapFragment frag = new MapFragment();
       // ft.replace(R.id.container,frag,"MapFragment");
        //tv.setVisibility(TextView.GONE);
        ft.addToBackStack("MapFragment");
        ft.commit();
    }

//    public String getMacAddress() {
//        WifiManager manager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = manager.getConnectionInfo();
//        String address = info.getMacAddress();
//        return address;
    //}
}
