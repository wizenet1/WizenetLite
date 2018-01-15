package com.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.Classes.*;
import com.CallSoap;
import com.DatabaseHelper;
import com.Helper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 05/09/2016.
 */
public class Model {
    private final static Model instance = new Model();
    Context context;
    Helper helper = new Helper();
    //ModelSql model = new ModelSql();
    //ModelParse model = new ModelParse();

    private Model() {
    }

    public static Model getInstance() {
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        //model.init(context);
    }

    //###################################
    //SYNCH LOGIN CALL
    //###################################
    public String syncLogin(final String macAddress,final String username,final String pass) {

        CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
        String response = cs.Call(macAddress, username, pass);//login
        //Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_LONG).show();

        String myResponse = response;
        if (!myResponse.startsWith("incorrect")) {
            //my regex changes
            myResponse = myResponse.replaceAll("USER_LoginResponse", "");
            myResponse = myResponse.replaceAll("USER_LoginResult=", "Login:");
            myResponse = myResponse.replaceAll(";", "");

        } else {
            return "incorrect URL";
        }
        String myNewResponse = myResponse;

        JSONObject j = null;

        boolean status = false;
        String msg = null;
        try {
            j = new JSONObject(myNewResponse);
            //get the array [...] in json
            JSONArray jarray = j.getJSONArray("Login");
            JSONObject object = jarray.getJSONObject(0);
            String estatus = jarray.getJSONObject(0).getString("Status");
            msg = jarray.getJSONObject(0).getString("Msg");
            if (estatus.equals("1")) {
                status = true;
            } else {
                status = false;
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        String result = "";
        if (status) {
            //Toast.makeText(context, "success", Toast.LENGTH_LONG).show();
            result = "1";
        } else {
            result = "0";
            //Toast.makeText(getApplicationContext(), "username or password incorrect", Toast.LENGTH_LONG).show();
        }

        //#####   UPDATE USERNAME IN DATABASE   #####
        if (DatabaseHelper.getInstance(context).getValueByKey("username").equals("")) {
            DatabaseHelper.getInstance(context).updateValue("username", username);
        }
        return result;
    }


    public interface LoginListener{
        public void onResult(String str);
    }
    //###################################
    //ASYNCH LOGIN CALL
    //###################################
    public void AsyncLogin(final String macAddress,final String username,final String pass, final LoginListener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {

                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Call(macAddress,username,pass);//login
                //Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_LONG).show();

                String myResponse = response;
                if (!myResponse.startsWith("incorrect")){
                    //my regex changes
                    myResponse = myResponse.replaceAll("USER_LoginResponse", "");
                    myResponse = myResponse.replaceAll("USER_LoginResult=", "Login:");
                    myResponse = myResponse.replaceAll(";", "");
                    return myResponse.toString();
                }
                return "incorrect URL";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                JSONObject j = null;

                boolean status = false;
                String msg = null;
                try {
                    j = new JSONObject(result);
                    //get the array [...] in json
                    JSONArray jarray = j.getJSONArray("Login");
                    JSONObject object = jarray.getJSONObject(0);
                    String estatus = jarray.getJSONObject(0).getString("Status");
                    msg = jarray.getJSONObject(0).getString("Msg");
                    if (estatus.equals("1")) {
                        status = true;
                    } else {
                        status = false;
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                if (result.startsWith("incorrect")) {
                    result = "incorrect";
                } else if (status) {
                    //Toast.makeText(context, "success", Toast.LENGTH_LONG).show();
                   result="1";
                } else {
                    result="0";
                    //Toast.makeText(getApplicationContext(), "username or password incorrect", Toast.LENGTH_LONG).show();
                }

                //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                if(DatabaseHelper.getInstance(context).getValueByKey("username").equals("")){
                    DatabaseHelper.getInstance(context).updateValue("username",username);
                }
                listener.onResult(result);
            }
        };
        task.execute();
    }

    public interface StatusListener{
        public void onResult(String str);
    }
    //###################################
    //ASYNCH STATUS CALL
    //###################################
    public void AsyncStatus(final String macAddress,final String longtitude,final String latitude, final StatusListener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {

                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Call3(macAddress,longtitude,latitude);//GPS
                //Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_LONG).show();

                String myResponse = response;//GET STATUS JSON
                //my regex changes
                myResponse = myResponse.replaceAll("USER_StatusResponse", "");
                myResponse = myResponse.replaceAll("USER_StatusResult=", "Status:");
                myResponse = myResponse.replaceAll(";", "");
                return myResponse.toString();
            }

            @Override
            protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    JSONObject j = null;

                    boolean mystatus = false;
                    String msg = null;
                    try {
                        j = new JSONObject(result);
                        //get the array [...] in json
                        JSONArray jarray = j.getJSONArray("Status");
                        String estatus = jarray.getJSONObject(0).getString("Status");//1 or 0
                        msg = jarray.getJSONObject(0).getString("Msg");//
                        if (estatus.equals("1")) {
                            mystatus = true;
                            result = "1";
                        } else {
                            //Toast.makeText(getApplicationContext(),"wrong username or password", Toast.LENGTH_LONG).show();
                            mystatus = false;
                            result = "0";
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }


                listener.onResult(result);
            }
        };
        task.execute();
    }

    public interface ReminderListener{
        public void onResult(String str,String str2,int size);
    }
    //###################################
    //ASYNCH REMINDER CALL
    //###################################
    public void AsyncReminder(final String macAddress, final ReminderListener listener) {

        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {

                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//databaseHelper.getControlPanel(1).getUrl());
                //String mac_address = getMacAddress();
                String response = cs.Call4(macAddress);
                //Log.e("myTag",response.toString());
                String myResponse = response;//GET STATUS JSON
                if(!response.startsWith("Err")){
                    //my regex changes
                    myResponse = myResponse.replaceAll("REMINDERS_retAlertResponse", "");
                    myResponse = myResponse.replaceAll("REMINDERS_retAlertResult=", "retAlertResult:");
                    myResponse = myResponse.replaceAll(";", "");
                }

                return myResponse.toString();
            }

            @Override
            protected void onPostExecute(String result) {
                String msgID="",msgSubject="",msgComment="",msgUrl="",msgDate="",msgRead="",msgType= "";
                super.onPostExecute(result);

                if(!result.startsWith("Err")){
                    JSONObject j = null;
                    try {
                        j = new JSONObject(result);

                        JSONArray jarray = j.getJSONArray("retAlertResult");

                        Log.e("myTag",jarray.toString()+"  Size = "+jarray.length());
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                        Date now = new Date();
                        String content = formatter.format(now);
                        //writeTextToFile(content+"+  Size:"+jarray.length()+"\n");
                        //String e_retAlert = jarray.getJSONObject(0).getString("retAlertResult");//1 or 0
                        int mySize=0;
                        //JSONArray array = new JSONArray(string_of_json_array);
                        if((jarray.length() > 0)){
                            //flag = true;
                            mySize = jarray.length();
                            for (int i = 0; i < jarray.length(); i++) {
                                msgID = jarray.getJSONObject(i).getString("msgID");
                                msgSubject = jarray.getJSONObject(i).getString("msgSubject");
                                msgComment = jarray.getJSONObject(i).getString("msgComment");
                                msgUrl = jarray.getJSONObject(i).getString("msgUrl");
                                msgDate = jarray.getJSONObject(i).getString("msgDate");
                                msgRead = jarray.getJSONObject(i).getString("msgRead");
                                msgType = jarray.getJSONObject(i).getString("msgType");
                                Message m = new Message(msgID,msgSubject,msgComment,msgUrl,msgDate,msgRead,msgType);
                                DatabaseHelper.getInstance(context).addMessage(m);
                            }
                            if(mySize == 1){
                                listener.onResult(msgSubject,msgComment,1);
                                //pushNotification(msgSubject,msgComment);
                            }else{
                                listener.onResult("Wizenet",mySize+" new messages",mySize);
                                //pushNotification("Wizenet",mySize+" new messages");
                            }

                        }else{
                            //    flag = false;
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();

                    }
                }
                //listener.onResult(result);
            }
        };
        task.execute();
    }


    public interface User_Details_Listener{
        public void onResult(String str);
    }
    //###################################
    //ASYNCH User_Details_Listener
    //###################################
    public void Async_User_Details_Listener(final String macAddress, final User_Details_Listener listener) {

        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {

                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//databaseHelper.getControlPanel(1).getUrl());
                //String mac_address = getMacAddress();
                String response = cs.Call_USER_Details(macAddress);
                //Log.e("myTag",response.toString());
                String myResponse = response;//GET STATUS JSON
                //my regex changes
                myResponse = myResponse.replaceAll("USER_DetailsResponse", "");
                myResponse = myResponse.replaceAll("USER_DetailsResult=", "DetailsResult:");
                myResponse = myResponse.replaceAll(";", "");
                return myResponse.toString();
                //return response.toString();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                JSONObject j = null;
                String myStr="";
                String fname,lname;
                try {
                    j = new JSONObject(result);
                    //get the array [...] in json
                    JSONArray jarray = j.getJSONArray("DetailsResult");
                    fname = jarray.getJSONObject(0).getString("Cfname");
                    lname = jarray.getJSONObject(0).getString("Clname");
                    myStr = fname+" "+lname;
                    //myStr=(jarray.getJSONObject(0).getString("Cfname"));//.concat(" ");//1 or 0
                    //myStr.concat(jarray.getJSONObject(0).getString("Clname"));//

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


                listener.onResult(myStr);//result);
            }
        };
        task.execute();
    }

    public interface get_clients_Listener{
        public void onResult(String str);
    }

    public void Async_Get_Clients_Listener(final String macAddress, final get_clients_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);
                String response = cs.Call2(macAddress);
                String myResponse = response;
                myResponse = myResponse.replaceAll("USER_ClientsResponse", "");
                myResponse = myResponse.replaceAll("USER_ClientsResult=", "Customers:");
                myResponse = myResponse.replaceAll(";", "");


//            FileWriter fw = null;
//            try {
//                fw = new FileWriter(myFile);
//                BufferedWriter bw = new BufferedWriter(fw);
//                bw.write(myResponse);
//                bw.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


                return myResponse.toString();
            }

            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);


            }
        };
        task.execute();
    }

    public interface Call_getClientsContactsListener{
        public void onResult(String str);
    }

    public void Async_Get_Clients_Contacts_Listener(final String macAddress, final Call_getClientsContactsListener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);
                String response = cs.Call_getClientsContacts(macAddress);
                String myResponse = response;
                myResponse = myResponse.replaceAll("getClientsContactsResponse", "");
                myResponse = myResponse.replaceAll("getClientsContactsResult=", "Contacts:");
                myResponse = myResponse.replaceAll(";", "");

                return myResponse.toString();
            }

            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);
            }
        };
        task.execute();
    }

    public interface get_mgnet_items_Listener{
        public void onResult(String str);
    }

    //PRODUCTS_ITEMS_LIST
    public void Async_Get_mgnet_items_Listener(final String macAddress, final get_mgnet_items_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);
                String response = cs.getOrdersProducts(macAddress);
                String myResponse = response;
                myResponse = myResponse.replaceAll("getOrdersProductsResponse", "");
                myResponse = myResponse.replaceAll("getOrdersProductsResult=", "Orders:");
                myResponse = myResponse.replaceAll(";", "");

                return myResponse.toString();
            }

            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);
            }
        };
        task.execute();
    }

//region get_mgnet_client_item
public interface get_mgnet_client_items_Listener{
    public void onResult(String str);
}
    //PRODUCTS_CLIENTS_ITEMS_LIST
    public void Async_Get_mgnet_client_items_Listener(final String macAddress,final String cid, final get_mgnet_client_items_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);

                String response = cs.get_mgnet_client_items_list(macAddress,cid);

                String myResponse = response;
                myResponse = myResponse.replaceAll("PRODUCTS_CLIENTS_ITEMS_LISTResponse", "");
                myResponse = myResponse.replaceAll("PRODUCTS_CLIENTS_ITEMS_LISTResult=", "Orders:");
                myResponse = myResponse.replaceAll(";", "");

                return myResponse.toString();
            }

            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);
            }
        };
        task.execute();
    }


    //endregion


    //region CREATE_OFFLINE
    public interface CREATE_OFFLINE_Listener{
        public void onResult(String str);
    }
    public void Async_CREATE_OFFLINE_Listener(final String macAddress,final String json, final CREATE_OFFLINE_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);
                String response = cs.CREATE_OFFLINE(macAddress,json);
                try{
                    String myResponse = response;

                    myResponse = myResponse.replaceAll("CREATE_OFFLINEResponse", "");
                    myResponse = myResponse.replaceAll("CREATE_OFFLINEResult=", "");
                    myResponse = myResponse.replaceAll("\\{", "");
                    myResponse = myResponse.replaceAll("\\}", "");
                    myResponse = myResponse.replaceAll(";", "");
                    myResponse= myResponse.replaceAll("\\<[^>]*>","");
                    return myResponse.toString();
                }catch(Exception e){
                    return "nothing? "+e.getMessage();
                }



            }

            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);
            }
        };
        task.execute();
    }
    //endregion


    //region GET_CALLS_LIST
    public interface GET_CALLS_LIST_Listener{
        public void onResult(String str);
    }
    public void Async_GET_CALLS_LIST_Listener(final String macAddress, final GET_CALLS_LIST_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.GET_CALLS_LIST(macAddress);
                try{
                    String myResponse = response;

                    myResponse = myResponse.replaceAll("CALLS_ListResponse", "");
                    myResponse = myResponse.replaceAll("CALLS_ListResult=", "Calls:");
                    //myResponse = myResponse.replaceAll("\\{", "");
                    //myResponse = myResponse.replaceAll("\\}", "");
                    myResponse = myResponse.replaceAll(";", "");
                    myResponse= myResponse.replaceAll("\\<[^>]*>","");
                    return myResponse.toString();
                }catch(Exception e){
                    return "nothing? "+e.getMessage();
                }
            }

            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);
            }
        };
        task.execute();
    }
    //endregion
    //region Wz_Calls_List
    public interface Wz_Calls_List_Listener{
        public void onResult(String str);
    }
    public void Async_Wz_Calls_List_Listener(final String macAddress,final int CallStatusID, final  Wz_Calls_List_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################

            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Wz_Calls_List(macAddress,CallStatusID);
                try{
                    String myResponse = response;

                    myResponse = myResponse.replaceAll("Wz_Calls_ListResponse", "");
                    myResponse = myResponse.replaceAll("Wz_Calls_ListResult=", "Calls:");
                    myResponse = myResponse.replaceAll(";", "");
                    myResponse= myResponse.replaceAll("\\<[^>]*>","");


                    boolean flag = false;
                    helper.deleteFile("calls.txt");
                    DatabaseHelper.getInstance(context).deleteAllCalls();
                    flag =helper.writeTextToSpecificFile("","calls.txt",myResponse);
                    Log.e("mytag", String.valueOf(flag));
                    if (flag == true){
                        //public List<String> getCIDSlist(){
                            List<Call> ret = new ArrayList<Call>();
                            String strJson = "";
                            strJson = helper.readTextFromFile3("calls.txt");
                            DatabaseHelper.getInstance(context).deleteAllCalls();
                            JSONObject j = null;
                            JSONArray jarray = null;
                            try {
                                j = new JSONObject(strJson);
                                jarray= j.getJSONArray("Calls");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("MYTAG","_Wz_Calls_List "+ e.getMessage());
                                return "";
                            }
                            for (int i = 0; i < jarray.length(); i++) {
                                final JSONObject e;

                                try {

                                    e = jarray.getJSONObject(i);
                                    boolean isExist =DatabaseHelper.getInstance(context).IsExistCallID(e.getInt("CallID"));
                                    if (isExist == true)
                                    {
                                      continue;
                                    }
                                    Call call= new Call();//Integer.valueOf(cursor.getString(cursor.getColumnIndex("CallID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("AID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("CID"))), cursor.getString(cursor.getColumnIndex("CreateDate")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("statusID"))), cursor.getString(cursor.getColumnIndex("CallPriority")), cursor.getString(cursor.getColumnIndex("subject")), cursor.getString(cursor.getColumnIndex("comments")), cursor.getString(cursor.getColumnIndex("CallUpdate")), cursor.getString(cursor.getColumnIndex("cntrctDate")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("TechnicianID"))), cursor.getString(cursor.getColumnIndex("statusName")), cursor.getString(cursor.getColumnIndex("internalSN")), cursor.getString(cursor.getColumnIndex("Pmakat")), cursor.getString(cursor.getColumnIndex("Pname")), cursor.getString(cursor.getColumnIndex("contractID")), cursor.getString(cursor.getColumnIndex("Cphone")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("OriginID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("ProblemTypeID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("CallTypeID"))), cursor.getString(cursor.getColumnIndex("priorityID")), cursor.getString(cursor.getColumnIndex("OriginName")), cursor.getString(cursor.getColumnIndex("problemTypeName")), cursor.getString(cursor.getColumnIndex("CallTypeName")), cursor.getString(cursor.getColumnIndex("Cname")), cursor.getString(cursor.getColumnIndex("Cemail")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("contctCode"))), cursor.getString(cursor.getColumnIndex("callStartTime")), cursor.getString(cursor.getColumnIndex("callEndTime")), cursor.getString(cursor.getColumnIndex("Ccompany")), cursor.getString(cursor.getColumnIndex("Clocation")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("callOrder"))), cursor.getString(cursor.getColumnIndex("Caddress")), cursor.getString(cursor.getColumnIndex("Ccity")), cursor.getString(cursor.getColumnIndex("Ccomments")), cursor.getString(cursor.getColumnIndex("Cfname")), cursor.getString(cursor.getColumnIndex("Clname")), cursor.getString(cursor.getColumnIndex("techName")), cursor.getString(cursor.getColumnIndex("Aname")), cursor.getString(cursor.getColumnIndex("ContctName")), cursor.getString(cursor.getColumnIndex("ContctAddress")), cursor.getString(cursor.getColumnIndex("ContctCity")), cursor.getString(cursor.getColumnIndex("ContctCell")), cursor.getString(cursor.getColumnIndex("ContctPhone")), cursor.getString(cursor.getColumnIndex("ContctCity")), cursor.getString(cursor.getColumnIndex("Ccell")), cursor.getString(cursor.getColumnIndex("techColor")), cursor.getString(cursor.getColumnIndex("ContctCemail")), cursor.getString(cursor.getColumnIndex("CallParentID")));
                                    call.setCallID(e.getInt("CallID"));
                                    call.setAID(e.getInt("AID"));
                                    call.setCID(e.getInt("CID"));
                                   call.setCreateDate(e.getString("createdate"));
                                  call.setStatusID(e.getInt("statusID"));
                                  call.setCallPriority(e.getString("CallPriority"));
                                    call.setSubject(e.getString("subject"));
                                  call.setComments(e.getString("comments"));
                                  call.setCallUpdate(e.getString("CallUpdate"));
                                  call.setCntrctDate(e.getString("cntrctDate"));
                                   call.setTechnicianID(e.getInt("TechnicianID"));
                                   call.setStatusName(e.getString("statusName"));
                                     call.setInternalSN(e.getString("internalSN"));
                                    call.setPmakat(e.getString("Pmakat"));
                                    call.setPname(e.getString("Pname"));
                                     call.setContractID(e.getString("contractID"));
                                    call.setCphone(e.getString("Cphone"));
                                    call.setOriginID(e.getInt("OriginID"));
                                     call.setProblemTypeID(e.getInt("ProblemTypeID"));
                                     call.setCallTypeID(e.getInt("CallTypeID"));
                                     call.setPriorityID(e.getString("priorityID"));
                                     call.setOriginName(e.getString("OriginName"));
                                   call.setProblemTypeName(e.getString("problemTypeName"));
                                     call.setCallTypeName(e.getString("CallTypeName"));
                                     call.setCname(e.getString("Cname"));
                                     call.setCemail(e.getString("Cemail"));
                                    call.setContctCode(e.getInt("contctCode"));
                                     call.setCallStartTime(e.getString("callStartTime"));
                                     call.setCallEndTime(e.getString("callEndTime"));
                                    call.setCcompany(e.getString("Ccompany"));
                                     call.setClocation(e.getString("Clocation"));
                                     call.setCallOrder(e.getInt("callOrder"));
                                    call.setCaddress(e.getString("Caddress"));
                                  call.setCcity(e.getString("Ccity"));
                                    call.setCcomments(e.getString("Ccomments"));
                                    call.setCfname(e.getString("Cfname"));
                                     call.setClname(e.getString("Clname"));
                                     call.setTechName(e.getString("techName"));
                                    call.setAname(e.getString("Aname"));
                                    call.setContctName(e.getString("ContctName"));
                                     call.setContctAddress(e.getString("ContctAddress"));
                                    call.setContctCity(e.getString("ContctCity"));
                                    call.setContctCell(e.getString("ContctCell"));
                                     call.setContctPhone(e.getString("ContctPhone"));
                                    call.setCcell(e.getString("Ccell"));
                                    call.setTechColor(e.getString("techColor"));
                                     call.setContctCemail(e.getString("ContctCemail"));
                                     call.setCallParentID(e.getString("CallParentID"));
                                    DatabaseHelper.getInstance(context).addNewCall(call);
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                    Log.e("MYTAG",e1.getMessage());
                                    return "";
                                }
                                //ADD TO DATABASE
                                //ret.add(name);
                            }

                            //return  ret;
                        //}
                    }


                    return "1";//myResponse.toString();
                }catch(Exception e){
                    return "nothing? "+e.getMessage();
                }
            }

            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);
            }
        };
        task.execute();
    }
    //endregion

    //region Wz_Call_setTime
    public interface Wz_Call_setTime_Listener{
        public void onResult(String str);
    }
    public void Async_Wz_Call_setTime_Listener(final String macAddress,final int CallID,final String action,final String latitude,final String longtitude, final Wz_Call_setTime_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Wz_Call_setTime(macAddress,CallID,action,latitude,longtitude);
                try{
                    String myResponse = response;

                    myResponse = myResponse.replaceAll("Wz_Call_setTimeResponse", "");
                    myResponse = myResponse.replaceAll("Wz_Call_setTimeResult=", "Wz_Call_setTime:");
                    myResponse = myResponse.replaceAll(";", "");
                    myResponse= myResponse.replaceAll("\\<[^>]*>","");
                    return myResponse.toString();
                }catch(Exception e){
                    return "nothing? "+e.getMessage();
                }
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);
            }
        };
        task.execute();
    }
    //endregion

    //region Wz_Call_setTime
    public interface Wz_Call_getTime_Listener{
        public void onResult(String str);
    }
    public void Async_Wz_Call_getTime_Listener(final String macAddress,final int CallID,final String action, final Wz_Call_getTime_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Wz_Call_getTime(macAddress,CallID,action);
                try{
                    String myResponse = response;

                    myResponse = myResponse.replaceAll("Wz_Call_getTimeResponse", "");
                    myResponse = myResponse.replaceAll("Wz_Call_getTimeResult=", "Wz_Call_getTime:");
                    myResponse = myResponse.replaceAll(";", "");
                    myResponse= myResponse.replaceAll("\\<[^>]*>","");
                    return myResponse.toString();
                }catch(Exception e){
                    return "nothing? "+e.getMessage();
                }
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);
            }
        };
        task.execute();
    }
    //endregion
    //region Wz_Call_Update
    public interface Wz_Call_Update_Listener{
        public void onResult(String str);
    }
    public void Async_Wz_Call_Update_Listener(final String macAddress,final int CallID,final int CallStatusID,final String CallAnswer, final Wz_Call_Update_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Wz_Call_Update(macAddress,CallID,CallStatusID,CallAnswer);
                try{
                    String myResponse = response;

                    myResponse = myResponse.replaceAll("Wz_Call_UpdateResponse", "");
                    myResponse = myResponse.replaceAll("Wz_Call_UpdateResult=", "Wz_Call_Update:");
                    myResponse = myResponse.replaceAll(";", "");
                    myResponse= myResponse.replaceAll("\\<[^>]*>","");
                    return myResponse.toString();
                }catch(Exception e){
                    return "nothing? "+e.getMessage();
                }
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);
            }
        };
        task.execute();
    }
    //endregion
    //region Wz_Call_Update
    public interface Wz_Call_Statuses_Listener{
        public void onResult(String str);
    }
    public void Wz_Call_Statuses_Listener(final String macAddress, final Wz_Call_Statuses_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Wz_Call_Statuses(macAddress);
                try{
                    String myResponse = response;

                    myResponse = myResponse.replaceAll("Wz_Call_StatusesResponse", "");
                    myResponse = myResponse.replaceAll("Wz_Call_StatusesResult=", "Wz_Call_Statuses:");
                    myResponse = myResponse.replaceAll(";", "");
                    myResponse= myResponse.replaceAll("\\<[^>]*>","");

                    boolean flag = false;
                    helper.deleteFile("CallStatuses.txt");
                    DatabaseHelper.getInstance(context).deleteAllCalls();
                    flag =helper.writeTextToSpecificFile("","CallStatuses.txt",myResponse);
                    if (flag == true){
                        String strJson = "";
                        strJson = helper.readTextFromFile3("CallStatuses.txt");
                        DatabaseHelper.getInstance(context).deleteCallStatuses();
                        JSONObject j = null;
                        JSONArray jarray = null;
                        try {
                            j = new JSONObject(strJson);
                            jarray= j.getJSONArray("Wz_Call_Statuses");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("MYTAG"," CallStatuses "+e.getMessage().toString());
                            return "";
                        }
                        for (int i = 0; i < jarray.length(); i++) {
                            final JSONObject e;

                            try {
                                e = jarray.getJSONObject(i);
                                CallStatus callStatus= new CallStatus();//Integer.valueOf(cursor.getString(cursor.getColumnIndex("CallID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("AID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("CID"))), cursor.getString(cursor.getColumnIndex("CreateDate")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("statusID"))), cursor.getString(cursor.getColumnIndex("CallPriority")), cursor.getString(cursor.getColumnIndex("subject")), cursor.getString(cursor.getColumnIndex("comments")), cursor.getString(cursor.getColumnIndex("CallUpdate")), cursor.getString(cursor.getColumnIndex("cntrctDate")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("TechnicianID"))), cursor.getString(cursor.getColumnIndex("statusName")), cursor.getString(cursor.getColumnIndex("internalSN")), cursor.getString(cursor.getColumnIndex("Pmakat")), cursor.getString(cursor.getColumnIndex("Pname")), cursor.getString(cursor.getColumnIndex("contractID")), cursor.getString(cursor.getColumnIndex("Cphone")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("OriginID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("ProblemTypeID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("CallTypeID"))), cursor.getString(cursor.getColumnIndex("priorityID")), cursor.getString(cursor.getColumnIndex("OriginName")), cursor.getString(cursor.getColumnIndex("problemTypeName")), cursor.getString(cursor.getColumnIndex("CallTypeName")), cursor.getString(cursor.getColumnIndex("Cname")), cursor.getString(cursor.getColumnIndex("Cemail")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("contctCode"))), cursor.getString(cursor.getColumnIndex("callStartTime")), cursor.getString(cursor.getColumnIndex("callEndTime")), cursor.getString(cursor.getColumnIndex("Ccompany")), cursor.getString(cursor.getColumnIndex("Clocation")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("callOrder"))), cursor.getString(cursor.getColumnIndex("Caddress")), cursor.getString(cursor.getColumnIndex("Ccity")), cursor.getString(cursor.getColumnIndex("Ccomments")), cursor.getString(cursor.getColumnIndex("Cfname")), cursor.getString(cursor.getColumnIndex("Clname")), cursor.getString(cursor.getColumnIndex("techName")), cursor.getString(cursor.getColumnIndex("Aname")), cursor.getString(cursor.getColumnIndex("ContctName")), cursor.getString(cursor.getColumnIndex("ContctAddress")), cursor.getString(cursor.getColumnIndex("ContctCity")), cursor.getString(cursor.getColumnIndex("ContctCell")), cursor.getString(cursor.getColumnIndex("ContctPhone")), cursor.getString(cursor.getColumnIndex("ContctCity")), cursor.getString(cursor.getColumnIndex("Ccell")), cursor.getString(cursor.getColumnIndex("techColor")), cursor.getString(cursor.getColumnIndex("ContctCemail")), cursor.getString(cursor.getColumnIndex("CallParentID")));
                                callStatus.setCallStatusID(e.getInt("CallStatusID"));
                                callStatus.setCallStatusName(e.getString("CallStatusName"));
                                callStatus.setCallStatusOrder(e.getInt("CallStatusOrder"));
                                 DatabaseHelper.getInstance(context).addCallStatus(callStatus);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                                Log.e("MYTAG",e1.getMessage());
                                return "";
                            }
                            //ADD TO DATABASE
                            //ret.add(name);
                        }

                        //return  ret;
                        //}
                    }




                    return "1";
                }catch(Exception e){
                    return "nothing? "+e.getMessage();
                }
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);
            }
        };
        task.execute();
    }
    //endregion

    //region Wz_Forgot
    public interface Wz_Forgot_Listener{
        public void onResult(String str);
    }
    public void Async_Wz_Forgot_Listener(final String macAddress,final String Email, final Wz_Forgot_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Wz_Forgot(macAddress,Email);
                try{
                    String myResponse = response;

                    myResponse = myResponse.replaceAll("Wz_ForgotResponse", "");
                    myResponse = myResponse.replaceAll("Wz_ForgotResult=", "");
                    myResponse = myResponse.replaceAll("\\{","");
                    myResponse = myResponse.replaceAll("\\}","");
                    myResponse = myResponse.replaceAll(";", "");
                    myResponse= myResponse.replaceAll("\\<[^>]*>","");
                    return myResponse.toString();
                }catch(Exception e){
                    return "nothing? "+e.getMessage();
                }
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);
            }
        };
        task.execute();
    }
    //endregion
}


