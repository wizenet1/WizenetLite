package com.Fragments;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
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
import com.CallSoap;
import com.Classes.Message;
import com.DatabaseHelper;
import com.File_;
import com.Helper;
import com.Icon_Manager;
import com.ProgressTaskAll;
import com.ProgressTaskClient;
import com.model.Model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 20/09/2016.
 */



public class FragmentMenuOffline extends android.support.v4.app.Fragment  {
    TextView tv;
    CheckBox cb;
    Button btn,btn2,mapid;
    EditText myEditText2;
    //BarCodeActivity barCodeActivity;
    private ProgressDialog pDialog;
    Helper helper;
    TextView id1,id2,id3,id4,id5,id6;
    TextView id1_text,id2_text,id3_text,id4_text,id5_text;
    DatabaseHelper db;
    Icon_Manager icon_manager;
    Context context;
    TextView lbl_percentage;
    ProgressBar progressBar;
    TextView sync_all_products1,sync_client_products1;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        context = this.getContext();
        View v = inflater.inflate(R.layout.menu_fragment_oflline, null);
        ((MenuActivity)getActivity()).initialIcons();

        db = DatabaseHelper.getInstance(getActivity().getApplicationContext());
        icon_manager = new Icon_Manager();
        helper = new Helper();
        progressBar = (ProgressBar) v.findViewById(R.id.progressbar);


        sync_all_products1 = (TextView) v.findViewById(R.id.sync_all_products1);
        sync_client_products1 = (TextView) v.findViewById(R.id.sync_client_products1);

        try{
            sync_all_products1.setText(db.getValueByKey("PRODUCTS_UPDATE"));
            sync_client_products1.setText(db.getValueByKey("CLIENTS_PRODUCTS_UPDATE"));

        }catch (Exception ex){

        }
        // Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/fontawesome-webfont.ttf");
        lbl_percentage = (TextView) v.findViewById(R.id.lbl_percentage);
        id1 = (TextView) v.findViewById(R.id.id1);
        id2 = (TextView) v.findViewById(R.id.id2);
        id3 = (TextView) v.findViewById(R.id.id3);
        id4 = (TextView) v.findViewById(R.id.id4);
        id5 = (TextView) v.findViewById(R.id.id5);
        //id6 = (TextView) v.findViewById(R.id.id6);
        id1_text = (TextView) v.findViewById(R.id.id1_text);
        id2_text = (TextView) v.findViewById(R.id.id2_text);
        id3_text = (TextView) v.findViewById(R.id.id3_text);
        id4_text = (TextView) v.findViewById(R.id.id4_text);
        id5_text = (TextView) v.findViewById(R.id.id5_text);


        id1.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));
        id2.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));
        id3.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));
        id4.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));
        id5.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));
//        id6.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",getContext()));
//
        id1.setTextSize(60);
        id2.setTextSize(60);
        id3.setTextSize(60);
        id4.setTextSize(60);
        id5.setTextSize(60);
        id1_text.setText("צור הזמנה אופליין");
        id2_text.setText("סנכרן לקוחות");
        id3_text.setText("סנכרן את כל המוצרים");
        id4_text.setText("סנכרן מוצרי לקוח");
        id5_text.setText("סנכרן קריאות");

//        id5.setTextSize(60);
//        id6.setTextSize(60);

        id1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.goToOrdersFragment(context);
            }
        });
        id2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.isNetworkAvailable(getContext())){
                    Toast.makeText(getActivity(),"מסנכרן לקוחות",Toast.LENGTH_SHORT).show();
                    Model.getInstance().Async_Get_Clients_Listener(helper.getMacAddr(), new Model.get_clients_Listener() {
                        @Override
                        public void onResult(String str) {
                            File_ f = new File_();
                            f.writeTextToFileExternal(context,"customers.txt",str);

                            //f.writeTextToFileInternal(context,"customers.txt",str);
                            //helper.writeTextToSpecificFile("","customers.txt",str);

                            Toast.makeText(getActivity(),"סונכרן בהצלחה",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(getActivity(),"network is Not available",Toast.LENGTH_SHORT).show();
                }
            }
        });
        id3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.mgnet_items_isEmpty("all")){
                    //new ProgressTaskAll(context).execute();
                    Model.getInstance().Async_Get_mgnet_items_Listener(helper.getMacAddr(), new Model.get_mgnet_items_Listener() {
                        @Override
                        public void onResult(String str) {
                            Log.e("myTag","str:" + str);
                            new ProgressTaskAll(getContext()).execute();
                        }
                    });

                }else{
                    //Toast.makeText(getContext(), "רשימת כל המוצרים סונכרנו", Toast.LENGTH_LONG).show();
                    // GIVE AN POSSIBLE TO ADD,
                    AlertDialogAll();
                    //WITH ALERT
                }
            }
        });

        id4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //-------------------------------------------------
                List<String> listCIDS = new ArrayList<String>();
                listCIDS=helper.getCIDSlist();

                int len = listCIDS.size();
                File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/client_products");
                File[] list = myFile.listFiles();
                int count = 0;
                try{
                    for (File f: list){
                        String name = f.getName();
                        if (name.endsWith(".txt"))
                            count++;
                    }
                }catch(Exception ex){

                }
                if (count == 0){
                    Toast.makeText(getActivity(), "sync started", Toast.LENGTH_SHORT).show();

                    String cardCodes = helper.getCusernamelist();
                    new ProgressTaskClient(context).execute();
//                    Model.getInstance().Async_Wz_Get_Client_Item_List_Listener(helper.getMacAddr(), cardCodes, new Model.Wz_Get_Client_Item_List_Listener() {
//                        @Override
//                        public void onResult(String str) {
//
//                        }
//                    });

                }else{
                    AlertDialogClientProducts(String.valueOf(len),String.valueOf(count));

                }
//-------------------------------------------------
            }
        });
        id5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().Async_Wz_Calls_List_Listener(context,helper.getMacAddr(), -2, new Model.Wz_Calls_List_Listener() {
                    @Override
                    public void onResult(String str) {
                        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //getView().setFocusableInTouchMode(true);
        //getView().requestFocus();

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
    }


//    protected void AlertDialogClient(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("סנכרון מוצרי לקוח");
//        builder.setMessage("האם תרצה לסנכרן שוב?");
//        builder.setIcon(android.R.drawable.ic_dialog_alert);
//        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int ii) {
//                boolean flag;
//                flag = db.delete_from_mgnet_items("client");
//                if (flag == true){
//                    if (db.mgnet_items_isEmpty("client")){
//                        new ProgressTaskAll().execute();
//
//                    }
//                }
//            }
//        });
//
//        builder.setNegativeButton("לא", new DialogInterface.OnClickListener()
//                {
//                    public void onClick(DialogInterface dialog, int ii) {
//                        dialog.dismiss();
//                    }
//                }
//        );
//        builder.show();
//
//    }

    protected void AlertDialogClientProducts(String lenCustomers,String countFiles){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("סנכרון רשימת מוצרי לקוח"+ "  "+countFiles+"/"+lenCustomers);
        builder.setMessage("האם תרצה לסנכרן שוב? , פעולה זו תאפס את מוצרי לקוח מחדש אם סנכרנת קודם");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int ii) {
                        String cardCodes = helper.getCusernamelist();
                helper.deleteProductsFiles();
                new ProgressTaskClient(context).execute();
//                        Model.getInstance().Async_Wz_Get_Client_Item_List_Listener(helper.getMacAddr(), cardCodes, new Model.Wz_Get_Client_Item_List_Listener() {
//                            @Override
//                            public void onResult(String str) {
//                                Log.e("mytag","step 1");
//
//                                Log.e("mytag","step 2");
//
//                            }
//                        });
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

    public class ProgressTaskClient1 extends AsyncTask<String, Integer, String> {
        private ProgressDialog dialog;
        List<Message> titles;
        private Context context;
        Helper helper;
        int len = 0;
        int count = 0;
        //private List<Message> messages;
        public ProgressTaskClient1(Context context) {
            this.context = context;
            dialog = new ProgressDialog(context);
            helper= new Helper();
            try{
                // ---------client products first time ------------
                File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/client_products");
                File[] list = myFile.listFiles();
                for (File f: list){
                    String name = f.getName();
                    if (name.endsWith(".txt"))
                        count++;
                }
            }catch (Exception ex){

            }
        }
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected void onPostExecute(final String success) {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
            //String s =String.format("%.2f", new BigDecimal(values[0] + "/" + len));
            float f1 =(float) values[0]/len;
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            lbl_percentage.setText((values[0]+count)+"/"+len);
            //lbl_percentage.setText(df.format(f1*100) + "%");
        }

        @Override
        protected String doInBackground(final String... args) {
            File_ f = new File_();
            Log.e("MYTAG","hello");
            List<String> listCIDS = new ArrayList<String>();
            listCIDS=helper.getCIDSlist();
            len = listCIDS.size();
            String rr = "";
            int i=0;
            for (String c: listCIDS) {
                final String cc = c;
                File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/client_products/"+"pl_" + cc +".txt");
                if(!myFile.exists()){
                    CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                    //String response = cs.Call(mac_address, memail, mpass);
                    try{
                        String response = cs.get_mgnet_client_items_list(helper.getMacAddr(),c);
                        String myResponse = response;
                        myResponse = myResponse.replaceAll("PRODUCTS_CLIENTS_ITEMS_LISTResponse", "");
                        myResponse = myResponse.replaceAll("PRODUCTS_CLIENTS_ITEMS_LISTResult=", "Orders:");
                        myResponse = myResponse.replaceAll(";", "");
                        f.writeTextToFileWithSubDirectory(getContext(),"client_products","pl_" + cc +".txt", myResponse);
                        //helper.writeTextToClientDirectory("client_products","pl_" + cc +".txt", myResponse);
                        Log.e("MYTAG","pl_" + cc +".txt ---" +myResponse);
                    }catch(Exception ex){
                        Log.e("MYTAG",ex.getMessage());
                    }

//                    Model.getInstance().Async_Get_mgnet_client_items_Listener(helper.getMacAddr(),c, new Model.get_mgnet_client_items_Listener() {
//                        @Override
//                        public void onResult(String str) {
//                            Log.e("MYTAG","pl_" + cc +".txt ---" +str);
//                            helper.writeTextToClientDirectory("client_products","pl_" + cc +".txt", str);
//                        };
//                        ;});
                    i++;
                    publishProgress(i);
                }


            };
            Log.e("MYTAG","FINISHED!!");

            //helper.ALLProductsSync(context);
            return "";
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(DatabaseHelper.getInstance(getContext()).getValueByKey("GPS").equals("1")) {
//            cb.setChecked(true);
//            cb.setText("GPS");
//            cb.setVisibility(View.VISIBLE);
//        }

    }
}
