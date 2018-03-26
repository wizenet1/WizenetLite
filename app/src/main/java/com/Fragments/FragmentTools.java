package com.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.MenuActivity;
import com.Activities.R;
import com.Activities.ScannerActivity;
import com.Classes.Message;
import com.DatabaseHelper;
import com.File_;
import com.Helper;
import com.Icon_Manager;
import com.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 20/09/2016.
 */



public class FragmentTools extends android.support.v4.app.Fragment  {
    TextView tv;
    CheckBox cb;
    Button btn,btn2,mapid;
    EditText myEditText2;
    //BarCodeActivity barCodeActivity;
    Helper helper;
    TextView id1,id2,id3,id4,id5,id6,idsyncproducts,btn_order,iddeleteproducts,iddeleteclientproducts,idsyncclientproducts,lbldivuah;
    DatabaseHelper db;
    Icon_Manager icon_manager;
    private ProgressDialog working_dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tools, null);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        db = DatabaseHelper.getInstance(getActivity().getApplicationContext());
        icon_manager = new Icon_Manager();
        helper = new Helper();
        iddeleteproducts = (TextView) v.findViewById(R.id.iddeleteproducts);
        lbldivuah = (TextView) v.findViewById(R.id.lbldivuah);
        id1 = (TextView) v.findViewById(R.id.id1);
        id2 = (TextView) v.findViewById(R.id.id2);
        //id3 = (TextView) v.findViewById(R.id.id3);//ENTRANCE TO OFFLINE
        id4 = (TextView) v.findViewById(R.id.idsynccustomers);
        id5 = (TextView) v.findViewById(R.id.id5);
        btn_order= (TextView) v.findViewById(R.id.btn_order);
        idsyncproducts = (TextView) v.findViewById(R.id.idsyncproducts);
        iddeleteclientproducts = (TextView) v.findViewById(R.id.iddeleteclientproducts);
        idsyncclientproducts = (TextView) v.findViewById(R.id.idsyncclientproducts);




        id1.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));
        id2.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));
//        iddeleteproducts.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));
        id4.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));
        btn_order.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));
        idsyncproducts.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));

        id1.setTextSize(60);
        id2.setTextSize(60);
        lbldivuah.setTextSize(60);
        id4.setTextSize(60);
        btn_order.setTextSize(60);
        idsyncproducts.setTextSize(60);

        id1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScannerActivity.class);
                startActivity(intent);
            }
        });
        id2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goTocustomers();
                //helper.goToCustomerFragment1(getContext());
            }
        });
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.SendOrderToWizenet(getContext());
//                DateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
//                String sdt = df.format(new Date(System.currentTimeMillis()));
//                Log.e("MYTAG","chk: "+sdt);
//                String isNetwork = "";
//                if (helper.isNetworkAvailable(getContext())){
//                    String path = Environment.getExternalStorageDirectory().getPath()+"/wizenet/offline/";
//                    File_ myDirectory = new File_(path);
//                    traverse(myDirectory);
//                }else{
//                    isNetwork = " nop Network";
//                }
//                helper.writeTextToFileORDER(sdt + isNetwork);
            }
        });

        lbldivuah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goTocustomers();
               // helper.goToOfflineMenuFragment(getContext());
                helper.goToLoginReportFragment(getContext());
            }
        });

        id4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.isNetworkAvailable(getContext())){
                    Toast.makeText(getActivity(),"מסנכרן לקוחות",Toast.LENGTH_SHORT).show();

                    Model.getInstance().Async_Get_Clients_Contacts_Listener(helper.getMacAddr(), new Model.Call_getClientsContactsListener() {
                        @Override
                        public void onResult(String str) {
                            File_ f = new File_();
                            f.writeTextToFileInternal(getContext(),"customers.txt",str);
                            //helper.writeTextToSpecificFile("","customers.txt",str);
                            Toast.makeText(getActivity(),"סונכרן בהצלחה",Toast.LENGTH_SHORT).show();
                            //tv.setText(str);
                            //Log.e("myTag",str);
                        }
                    });
                }else{
                    Toast.makeText(getActivity(),"network is Not available",Toast.LENGTH_SHORT).show();
                }

            }
        });
        iddeleteproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag;
                flag = db.delete_from_mgnet_items("all");
                if (flag == true){
                    Toast.makeText(getActivity(),"נמחקו בהצלחה",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getActivity(),"לא הצליח למחוק",Toast.LENGTH_SHORT).show();

                }
            }
        });
        iddeleteclientproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag;
                flag = db.delete_from_mgnet_items("client");
                if (flag == true){
                    Toast.makeText(getActivity(),"נמחקו בהצלחה",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getActivity(),"לא הצליח למחוק",Toast.LENGTH_SHORT).show();

                }
            }
        });
        idsyncproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.mgnet_items_isEmpty("all")){
                    new ProgressTaskAll().execute();
                }else{
                    Toast.makeText(getContext(), "רשימת כל המוצרים סונכרנו", Toast.LENGTH_LONG).show();
                    // GIVE AN POSSIBLE TO ADD,
                    AlertDialogAll();
                    //WITH ALERT
                }
            }
        });
        idsyncclientproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.mgnet_items_isEmpty("client")){
                    //new ProgressTaskClient().execute();
                }else{
                    Toast.makeText(getContext(), "רשימת  מוצרי לקוח סונכרנו", Toast.LENGTH_LONG).show();
                    // GIVE AN POSSIBLE TO ADD,
                    AlertDialogClient();
                    //WITH ALERT
                }
            }
        });

        return v;
    }
    protected void AlertDialogClient(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("סנכרון מוצרי לקוח");
        builder.setMessage("האם תרצה לסנכרן שוב?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int ii) {
                boolean flag;
                flag = db.delete_from_mgnet_items("client");
                if (flag == true){
                    if (db.mgnet_items_isEmpty("client")){
                        new ProgressTaskAll().execute();

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
    protected void AlertDialogAll(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("סנכרון רשימת כל המוצרים");
        builder.setMessage("האם תרצה לסנכרן שוב?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int ii) {
                boolean flag;
                flag = db.delete_from_mgnet_items("all");
                if (flag == true){
                    if (db.mgnet_items_isEmpty("all")){
                        new ProgressTaskAll().execute();

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
    public class ProgressTaskAll extends AsyncTask<String, String, String> {
        private ProgressDialog dialog;
        List<Message> titles;

        //private List<Message> messages;
        public ProgressTaskAll() {

            dialog = new ProgressDialog(getContext());
        }
        @Override
        protected void onPreExecute() {
            //this.working_dialog = ProgressDialog.show(getContext(), "","Working please wait...", true);

            this.dialog.setMessage("Working please wait...");
            this.dialog.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            }, 5000);
        }
        @Override
        protected void onPostExecute(final String success) {
        }
        @Override
        protected String doInBackground(final String... args) {
            helper.ALLProductsSync(getContext());
            return "";
        }
    }
//    public class ProgressTaskClient extends AsyncTask<String, String, String> {
//        private ProgressDialog dialog;
//        List<Message> titles;
//
//        //private List<Message> messages;
//        public ProgressTaskClient() {
//
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
//            List<String> listCIDS = new ArrayList<String>();
//            listCIDS=helper.getCIDSlist();
//
//            for (String c: listCIDS) {
//                helper.CLIENTProductsSync(getContext(),c);
//            }
//
//
//            return "";
//        }
    //}







//
//public List<String> getCIDSlist(){
//    List<String> ret = new ArrayList<String>();
//    String strJson = "";
//    strJson = helper.readTextFromFileCustomers();
//    JSONObject j = null;
//    JSONArray jarray = null;
//    try {
//        j = new JSONObject(strJson);
//        jarray= j.getJSONArray("Customers");
//    } catch (JSONException e) {
//        e.printStackTrace();
//    }
//
//
//    for (int i = 0; i < jarray.length(); i++) {
//        final JSONObject e;
//        String name = null;
//        try {
//            e = jarray.getJSONObject(i);
//            name = e.getString("CID");
//            //name = e.getString("Ccompany")+'|'+e.getString("CID");
//
//        } catch (JSONException e1) {
//            e1.printStackTrace();
//        }
//        ret.add(name);
//    }
//    return  ret;
//}

    private void showWorkingDialog() {
        working_dialog = ProgressDialog.show(getContext(), "","Working please wait...", true);
    }

    private void removeWorkingDialog() {
        if (working_dialog != null) {
            working_dialog.dismiss();
            working_dialog = null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(DatabaseHelper.getInstance(getContext()).getValueByKey("GPS").equals("1")) {
            cb.setChecked(true);
            cb.setText("GPS");
            cb.setVisibility(View.VISIBLE);
        }

//        try{
//            myEditText2.setText(((MenuActivity)getActivity()).getMyString());
//        }catch (Exception ex){
//            Toast.makeText(getActivity(),ex.toString(),Toast.LENGTH_SHORT).show();
//        }
    }
    public String getEditText(){
        return myEditText2.getText().toString();
    }



//    public String getMacAddress() {
//        WifiManager manager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = manager.getConnectionInfo();
//        String address = info.getMacAddress();
//        return address;
    //}
}
