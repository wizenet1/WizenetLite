package com.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.Classes.*;
import com.CallSoap;
import com.DatabaseHelper;
import com.File_;
import com.Helper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            try{
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
                }catch(Exception e){
                helper.LogPrintExStackTrace(e);
                    return "error has been occour "+e.getMessage();
                }
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
                try{
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
                }catch(Exception e){
                    return "nothing? "+e.getMessage();
                }
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
                String myStr=result;
                String fname,lname;
                try {
                    j = new JSONObject(result);
                    //get the array [...] in json
                    JSONArray jarray = j.getJSONArray("DetailsResult");
                    fname = jarray.getJSONObject(0).getString("Cfname");
                    lname = jarray.getJSONObject(0).getString("Clname");
                    if(!DatabaseHelper.getInstance(context).getValueByKey("CID").equals(jarray.getJSONObject(0).getString("CID"))){
                        DatabaseHelper.getInstance(context).updateValue("CID",jarray.getJSONObject(0).getString("CID"));
                    }
                    if(!DatabaseHelper.getInstance(context).getValueByKey("CtypeID").equals(jarray.getJSONObject(0).getString("CtypeID"))){
                        DatabaseHelper.getInstance(context).updateValue("CtypeID",jarray.getJSONObject(0).getString("CtypeID"));
                    }
                    if(!DatabaseHelper.getInstance(context).getValueByKey("Cfname").equals(fname+" "+lname)){
                        DatabaseHelper.getInstance(context).updateValue("Cfname",fname+" "+lname);
                    }
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
                try{
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);
                String response = cs.Call2(macAddress);
                String myResponse = response;
                myResponse = myResponse.replaceAll("USER_ClientsResponse", "");
                myResponse = myResponse.replaceAll("USER_ClientsResult=", "Customers:");
                myResponse = myResponse.replaceAll(";", "");


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
                try{
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);
                String response = cs.Call_getClientsContacts(macAddress);
                String myResponse = response;
                myResponse = myResponse.replaceAll("getClientsContactsResponse", "");
                myResponse = myResponse.replaceAll("getClientsContactsResult=", "Contacts:");
                myResponse = myResponse.replaceAll(";", "");

                return myResponse.toString();
            }catch(Exception e){
                return "nothing? "+e.getMessage();
            }}

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
                try{
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);
                String response = cs.getOrdersProducts(macAddress);
                String myResponse = response;
                myResponse = myResponse.replaceAll("getOrdersProductsResponse", "");
                myResponse = myResponse.replaceAll("getOrdersProductsResult=", "Orders:");
                myResponse = myResponse.replaceAll(";", "");

                return myResponse.toString();
            }
            catch(Exception e){
                return "nothing? "+e.getMessage();
            }}

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
                try{


                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);

                String response = cs.get_mgnet_client_items_list(macAddress,cid);

                String myResponse = response;
                myResponse = myResponse.replaceAll("PRODUCTS_CLIENTS_ITEMS_LISTResponse", "");
                myResponse = myResponse.replaceAll("PRODUCTS_CLIENTS_ITEMS_LISTResult=", "Orders:");
                myResponse = myResponse.replaceAll(";", "");

                return myResponse.toString();
            }
            catch(Exception e){
                return "nothing? "+e.getMessage();
            }}

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
                try{
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);
                String response = cs.CREATE_OFFLINE(macAddress,json);

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
                try{
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.GET_CALLS_LIST(macAddress);

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
    public void Async_Wz_Calls_List_Listener(final Context ctx,final String macAddress,final int CallStatusID, final  Wz_Calls_List_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################

            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                try{
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(ctx).getValueByKey("URL"));
                String response = cs.Wz_Calls_List(macAddress,CallStatusID);

                    Log.e("mytag","arrived here async");

                    String myResponse = response;

                    myResponse = myResponse.replaceAll("Wz_Calls_ListResponse", "");
                    myResponse = myResponse.replaceAll("Wz_Calls_ListResult=", "Calls:");
                    myResponse = myResponse.replaceAll(";", "");
                    myResponse= myResponse.replaceAll("\\<[^>]*>","");

                    boolean flag = false;
                    File_ f = new File_();
                    f.deleteFileExternal(ctx,"calls.txt");
                    flag = f.writeTextToFileExternal(ctx,"calls.txt",myResponse);
                    //Log.e("mytag","sss"+ String.valueOf(flag));
                    if (flag == true){
                        //public List<String> getCIDSlist(){
                            //List<Call> ret = new ArrayList<Call>();
                            String strJson = "";
                            strJson=f.readFromFileExternal(context,"calls.txt");
                            //strJson = f.readFromFileInternal(context,"calls.txt");
                            //strJson = helper.readTextFromFile3("calls.txt");

                            JSONObject j = null;
                            JSONArray jarray = null;
                            try {
                                j = new JSONObject(strJson);
                                jarray= j.getJSONArray("Calls");
                                DatabaseHelper.getInstance(ctx).deleteAllCalls();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("MYTAG","_Wz_Calls_List "+ e.getMessage() + "  dd");
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
                                    call.setCcomments(e.getString("comments"));
                                    //Log.e("mytag","e.getString(Ccomments): " +e.getString("comments").toString());
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
                                    call.setState(e.getString("state"));
                                    call.setSla(e.getString("sla"));
                                    Log.e("MYTAG","test doron");

                                    DatabaseHelper.getInstance(context).addNewCall(call);
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                    Log.e("MYTAG","model add call: " +e1.getMessage());
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
                    Log.e("mytag","error:" + e.getMessage().toString());

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
                try{
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Wz_Call_setTime(macAddress,CallID,action,latitude,longtitude);

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
                try{
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Wz_Call_getTime(macAddress,CallID,action);

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
                try{
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Wz_Call_Update(macAddress,CallID,CallStatusID,CallAnswer);

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
                try{
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Wz_Call_Statuses(macAddress);

                    String myResponse = response;

                    myResponse = myResponse.replaceAll("Wz_Call_StatusesResponse", "");
                    myResponse = myResponse.replaceAll("Wz_Call_StatusesResult=", "Wz_Call_Statuses:");
                    myResponse = myResponse.replaceAll(";", "");
                    myResponse = myResponse.replaceAll("\\<[^>]*>","");
                    Log.e("mytag","callstatuses: " );//myResponse
                    File_ f = new File_();
                    boolean flag = false;
                    if (helper.isJSONValid(myResponse)){
                        f.deleteFileExternal(context,"CallStatuses.txt");
                        DatabaseHelper.getInstance(context).deleteCallStatuses();
                        flag = f.writeTextToFileExternal(context,"CallStatuses.txt",myResponse);
                    }

                    if (flag == true){
                        String strJson = "";
                        strJson = f.readFromFileExternal(context,"CallStatuses.txt");

                        JSONObject j = null;
                        JSONArray jarray = null;
                        try {
                            j = new JSONObject(strJson);
                            jarray= j.getJSONArray("Wz_Call_Statuses");
                        } catch (JSONException e) {
                            helper.LogPrintExStackTrace(e);
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
                                Log.e("MYTAG","sss1:"+e1.getMessage());
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
                try{
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Wz_Forgot(macAddress,Email);

                    String myResponse = response;

                    myResponse = myResponse.replaceAll("Wz_ForgotResponse", "");
                    myResponse = myResponse.replaceAll("Wz_ForgotResult=", "");
                    myResponse = myResponse.replaceAll("\\{","");
                    myResponse = myResponse.replaceAll("\\}","");
                    myResponse = myResponse.replaceAll(";", "");
                    myResponse= myResponse.replaceAll("\\<[^>]*>","");
                    return myResponse.toString().trim();
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
    // region Wz_timeReport
    public interface Wz_timeReport_Listener{
        public void onResult(String str);
    }
    public void Async_Wz_timeReport_Listener(final String macAddress,final String action,final String latitude,final String longtitude, final Wz_timeReport_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                try{
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Wz_timeReport(macAddress,action,latitude,longtitude);

                    String myResponse = response;

                    myResponse = myResponse.replaceAll("Wz_timeReportResponse", "");
                    myResponse = myResponse.replaceAll("Wz_timeReportResult=", "Wz_timeReport:");
                    myResponse = myResponse.replaceAll(";", "");
                    myResponse= myResponse.replaceAll("\\<[^>]*>","");
                    return myResponse.toString().trim();
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
    // region Wz_getState
    public interface Wz_getState_Listener{
        public void onResult(String str);
    }
    public void Async_Wz_getState_Listener(final String macAddress,final Wz_getState_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {
                try{
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Wz_getState(macAddress);
                try{
                    String myResponse = response;

                    myResponse = myResponse.replaceAll("Wz_getStateResponse", "");
                    myResponse = myResponse.replaceAll("Wz_getStateResult=", "Wz_getState:");
                    myResponse = myResponse.replaceAll(";", "");
                    myResponse= myResponse.replaceAll("\\<[^>]*>","");
                    return myResponse.toString().trim();
                }catch(Exception e){
                    return "nothing? "+e.getMessage();
                }
                }catch(Exception e){
                    helper.LogPrintExStackTrace(e);
                    return "error";
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
    // region Wz_getState
    public interface Wz_Update_Call_Field_Listener{
        public void onResult(String str);
    }
    public void Async_Wz_Update_Call_Field_Listener(final String macAddress,final String callid,final String field,final String value,final Wz_Update_Call_Field_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                try{


                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Wz_Update_Call_Field(macAddress,callid,field,value);
                try{
                    String myResponse = response;

                    myResponse = myResponse.replaceAll("Wz_getStateResponse", "");
                    myResponse = myResponse.replaceAll("Wz_getStateResult=", "Wz_Update_Call_Field:");
                    myResponse = myResponse.replaceAll(";", "");
                    myResponse= myResponse.replaceAll("\\<[^>]*>","");
                    return myResponse.toString().trim();
                }catch(Exception e){
                    return "nothing? "+e.getMessage();
                }
                }catch(Exception e){
                    helper.LogPrintExStackTrace(e);
                    return "error";
                }
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
            }
        };
        task.execute();
    }
    //endregion
// region Wz_Update_Action_Field_Listener
    public interface Wz_Update_Action_Field_Listener{
        public void onResult(String str);
    }
    public void Async_Wz_Update_Action_Field_Listener(final String macAddress,final String actionid,final String field,final String value,final Wz_Update_Action_Field_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                try{


                    CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                    String response = cs.Wz_Update_Action_Field(macAddress,actionid,field,value);
                    try{
                        String myResponse = response;

                        myResponse = myResponse.replaceAll("Wz_Update_Action_Field_ListenerResponse", "");
                        myResponse = myResponse.replaceAll("Wz_Update_Action_Field_ListenerResult=", "Wz_Update_Action_Field_Listener:");
                        myResponse = myResponse.replaceAll(";", "");
                        myResponse= myResponse.replaceAll("\\<[^>]*>","");
                        return myResponse.toString().trim();
                    }catch(Exception e){
                        return "nothing? "+e.getMessage();
                    }
                }catch(Exception e){
                    helper.LogPrintExStackTrace(e);
                    return "error";
                }
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
            }
        };
        task.execute();
    }
    //endregion

    //region get_mgnet_client_item
    public interface Wz_Get_Client_Item_List_Listener{
        public void onResult(String str);
    }
    //PRODUCTS_CLIENTS_ITEMS_LIST
    public void Async_Wz_Get_Client_Item_List_Listener(final String macAddress,final String cardCodes, final Wz_Get_Client_Item_List_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                try{
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);

                String response = cs.Wz_Get_Client_Item_List(macAddress,cardCodes);
                String myResponse = response;
                myResponse = myResponse.replaceAll("PRODUCTS_CLIENTS_ITEMS_LISTResponse", "");
                myResponse = myResponse.replaceAll("PRODUCTS_CLIENTS_ITEMS_LISTResult=", "Orders:");
                myResponse = myResponse.replaceAll(";", "");
                return "";
                //return myResponse.toString();
                }catch(Exception e){
                    helper.LogPrintExStackTrace(e);
                    return "error";
                }
            }
            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
            }
        };
        task.execute();
    }
    //endregion

    //region get_mgnet_client_item
    public interface Wz_getUrl_Listener{
        public void onResult(String str);
    }
    //PRODUCTS_CLIENTS_ITEMS_LIST
    public void Async_Wz_getUrl_Listener(final String macAddress,final String msid, final Wz_getUrl_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                try{
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);

                String response = cs.Wz_getUrl(macAddress,msid);
                String myResponse = response;
                myResponse = myResponse.replaceAll("Wz_getUrlResponse", "");
                myResponse = myResponse.replaceAll("Wz_getUrlResult=", "Wz_getUrl:");
                myResponse = myResponse.replaceAll(";", "");
                //return "";
                return myResponse.toString();
                }catch(Exception e){
                    helper.LogPrintExStackTrace(e);
                    return "error";
                }
            }
            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
            }
        };
        task.execute();
    }
    //endregion
    //region Wz_retClientFavorites
    public interface Wz_retClientFavorites_Listener{
        public void onResult(String str);
    }
    //PRODUCTS_CLIENTS_ITEMS_LIST
    public void Async_Wz_retClientFavorites_Listener(final String macAddress, final Wz_retClientFavorites_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                try{
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);

                String response = cs.Wz_retClientFavorites(macAddress);
                String myResponse = response;
                myResponse = myResponse.replaceAll("Wz_retClientFavoritesResponse", "");
                myResponse = myResponse.replaceAll("Wz_retClientFavoritesResult=", "Wz_retClientFavorites:");
                myResponse = myResponse.replaceAll(";", "");
                //return "";
                return myResponse.toString();
                }catch(Exception e){
                    helper.LogPrintExStackTrace(e);
                    return "error";
                }
            }
            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
            }
        };
        task.execute();
    }
    //endregion

    //region Wz_retClientFavorites
    public interface Wz_Send_Call_Offline_Listener{
        public void onResult(String str);
    }
    //PRODUCTS_CLIENTS_ITEMS_LIST
    public void Async_Wz_Send_Call_Offline_Listener(final String macAddress,final String jsonString, final Wz_Send_Call_Offline_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                try{
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);

                String response = cs.Wz_Send_Call_Offline(macAddress,jsonString);
                String myResponse = response;
                myResponse = myResponse.replaceAll("Wz_Send_Call_OfflineResponse", "");
                myResponse = myResponse.replaceAll("Wz_Send_Call_OfflineResult=", "Wz_Send_Call_Offline:");
                myResponse = myResponse.replaceAll(";", "");
                //return "";
                return myResponse.toString();
                }catch(Exception e){
                    helper.LogPrintExStackTrace(e);
                    return "error";
                }
            }
            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
            }
        };
        task.execute();
    }
    //endregion
    //region Wz_calls_Summary
    public interface Wz_calls_Summary_Listener{
        public void onResult(String str);
    }
    //PRODUCTS_CLIENTS_ITEMS_LIST
    public void Async_Wz_calls_Summary_Listener(final String macAddress, final Wz_calls_Summary_Listener listener) {

        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                try{
                    CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                    //String response = cs.Call(mac_address, memail, mpass);

                    String response = cs.Wz_calls_Summary(macAddress);
                    String myResponse = response;
                    myResponse = myResponse.replaceAll("Wz_calls_SummaryResponse", "");
                    myResponse = myResponse.replaceAll("Wz_calls_SummaryResult=", "Wz_calls_Summary:");
                    myResponse = myResponse.replaceAll(";", "");
                    //return "";
                    return myResponse.toString();
                }catch(Exception e){
                    helper.LogPrintExStackTrace(e);
                    return "error";
                }

            }
            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
            }
        };
        task.execute();
    }
    //endregion
//region Wz_retClientReports
    public interface Wz_retClientReports_Listener{
        public void onResult(String str);
    }
    //PRODUCTS_CLIENTS_ITEMS_LIST
    public void Async_Wz_retClientReports_Listener(final String macAddress, final Wz_retClientReports_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                try{
                    CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                    //String response = cs.Call(mac_address, memail, mpass);

                    String response = cs.Wz_retClientReports(macAddress);
                    String myResponse = response;
                    myResponse = myResponse.replaceAll("Wz_retClientReportsResponse", "");
                    myResponse = myResponse.replaceAll("Wz_retClientReportsResult=", "Wz_retClientReports:");
                    myResponse = myResponse.replaceAll(";", "");
                    //return "";
                    return myResponse.toString();
                }catch(Exception e){
                    helper.LogPrintExStackTrace(e);
                    return "error";
                }
            }
            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
            }
        };
        task.execute();
    }
    //endregion
    //region Wz_Call_setTime_Offline
    public interface Wz_Call_setTime_Offline_Listener{
        public void onResult(String str);
    }
    //PRODUCTS_CLIENTS_ITEMS_LIST
    public void Async_Wz_Call_setTime_Offline_Listener(final String macAddress,final String jsonString, final Wz_Call_setTime_Offline_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                try{
                    CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                    //String response = cs.Call(mac_address, memail, mpass);

                    String response = cs.Wz_Call_setTime_Offline(macAddress,jsonString);
                    String myResponse = response;
                    myResponse = myResponse.replaceAll("Wz_Call_setTime_OfflineResponse", "");
                    myResponse = myResponse.replaceAll("Wz_Call_setTime_OfflineResult=", "Wz_Call_setTime_Offline:");
                    myResponse = myResponse.replaceAll(";", "");
                    //return "";
                    return myResponse.toString();
                }catch(Exception e){
                    helper.LogPrintExStackTrace(e);
                    return "error";
                }
            }
            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
            }
        };
        task.execute();
    }
    //endregion
    //region Wz_ACTIONS_retList
    public interface Wz_ACTIONS_retList_Listener{
        public void onResult(String str);
    }
    public boolean StrIsValidJson(String str){
        boolean res = false;
        Object json = null;
        try {
            json = new JSONTokener(str).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONObject || json instanceof JSONArray){
            res = true;
        }else{
            Log.e("mytag","new file arrived is error");
        }
        return res;
    }
    //Wz_ACTIONS_retList
    public void Async_Wz_ACTIONS_retList_Listener(final String macAddress, final Wz_ACTIONS_retList_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                try{
                    CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                    //String response = cs.Call(mac_address, memail, mpass);

                    String response = cs.Wz_ACTIONS_retList(macAddress);
                    String myResponse = response;
                    myResponse = myResponse.replaceAll("Wz_ACTIONS_retListResponse", "");
                    myResponse = myResponse.replaceAll("Wz_ACTIONS_retListResult=", "Wz_ACTIONS_retList:");
                    myResponse = myResponse.replaceAll(";", "");
                    if (StrIsValidJson(myResponse) == false){
                        Log.e("mytag","myResponse is not valid json: " +myResponse);
                        return "";
                    }

                    boolean flag = false;
                    File_ f = new File_();
                    f.deleteFileExternal(context,"is_actions.txt");
                    flag = f.writeTextToFileExternal(context,"is_actions.txt",myResponse);
                    if (flag == true){
                        addActions();
                    }
                    return myResponse.toString();
                }catch(Exception e){
                    helper.LogPrintExStackTrace(e);
                    return "error";
                }
            }
            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
            }
        };
        task.execute();
    }
    //endregion
    //region Wz_Clients_List
    public interface Wz_Clients_List_Listener{
        public void onResult(String str);
    }
    //Wz_ACTIONS_retList
    public void Async_Wz_Clients_List_Listener(final String macAddress,final int CtypeID,final int CparentID, final Wz_Clients_List_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                try{
                    CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                    //String response = cs.Call(mac_address, memail, mpass);

                    String response = cs.Wz_Clients_List(macAddress,CtypeID,CparentID);
                    String myResponse = response;
                    myResponse = myResponse.replaceAll("Wz_Clients_ListResponse", "");
                    myResponse = myResponse.replaceAll("Wz_Clients_ListResult=", "Wz_Clients_List:");
                    myResponse = myResponse.replaceAll(";", "");

                    boolean flag = false;
                    File_ f = new File_();
                    // the logic of writing in the fragment

                    //}
                    return myResponse.toString();
                }catch(Exception e){
                    helper.LogPrintExStackTrace(e);
                    return "error";
                }
            }
            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
            }
        };
        task.execute();
    }
    //endregion
//region Wz_Clients_List
    public interface Wz_ret_ClientsAddressesByActions_Listener{
        public void onResult(String str);
    }
    //Wz_ACTIONS_retList
    public void Async_Wz_ret_ClientsAddressesByActions_Listener(final String macAddress,final String action, final Wz_ret_ClientsAddressesByActions_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                try{
                    CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                    //String response = cs.Call(mac_address, memail, mpass);

                    String response = cs.Wz_ret_ClientsAddressesByActions(macAddress,action);
                    String myResponse = response;
                    myResponse = myResponse.replaceAll("Wz_ret_ClientsAddressesByActionsResponse", "");
                    myResponse = myResponse.replaceAll("Wz_ret_ClientsAddressesByActionsResult=", "Wz_ret_ClientsAddressesByActions:");
                    myResponse = myResponse.replaceAll(";", "");

                    boolean flag = false;
                    File_ f = new File_();
                   f.writeTextToFileExternal(context,"wzClients.txt",myResponse);
                    return myResponse.toString();
                }catch(Exception e){
                    helper.LogPrintExStackTrace(e);
                    return "error";
                }
            }
            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
            }
        };
        task.execute();
    }
    //endregion
    //region Wz_getCtypeIDandSons
    public interface Wz_getCtypeIDandSons_Listener{
        public void onResult(String str);
    }
    //Wz_ACTIONS_retList
    public void Async_Wz_getCtypeIDandSons_Listener(final String macAddress, final Wz_getCtypeIDandSons_Listener listener) {
        try{
            AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

                //###################################
                //extract the data and return it
                //###################################
                @Override
                protected String doInBackground(String... params) {
                    try{
                        CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                        //String response = cs.Call(mac_address, memail, mpass);

                        String response = cs.Wz_getCtypeIDandSons(macAddress);
                        String myResponse = response;
                        myResponse = myResponse.replaceAll("Wz_getCtypeIDandSonsResponse", "");
                        myResponse = myResponse.replaceAll("Wz_getCtypeIDandSonsResult=", "Wz_getCtypeIDandSons:");
                        myResponse = myResponse.replaceAll(";", "");

                        //boolean flag = helper.writeCtypeIDandSons(context,myResponse);
                        return myResponse.toString();
                    }catch(Exception e){
                        helper.LogPrintExStackTrace(e);
                        return "error";
                    }
                }
                //###################################
                //active the fragment with json result by bundle
                //###################################
                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    listener.onResult(result);
                }
            };
            task.execute();
        }catch(Exception e){
            helper.LogPrintExStackTrace(e);
        }

    }
    //endregion

    public interface Wz_getProjects_Listener{
        public void onResult(String str);
    }
    //Wz_getProjects
    public void Async_Wz_getProjects_Listener(final String macAddress, final Wz_getProjects_Listener listener) {
        try{
            AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

                //###################################
                //extract the data and return it
                //###################################
                @Override
                protected String doInBackground(String... params) {
                    try{
                        CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                        //String response = cs.Call(mac_address, memail, mpass);

                        String response = cs.Wz_getProjects(macAddress);
                        String myResponse = response;
                        //Log.e("mytag","response:" +response);
                        myResponse = myResponse.replaceAll("Wz_getProjectsResponse", "");
                        myResponse = myResponse.replaceAll("Wz_getProjectsResult=", "Wz_getProjects:");
                        myResponse = myResponse.replaceAll(";", "");
                        File_ f = new File_();
                        f.writeTextToFileExternal(context,"projects.txt",myResponse);
                        //boolean flag = helper.writeCtypeIDandSons(context,myResponse);
                        return "";// myResponse.toString();
                    }catch(Exception e){
                        helper.LogPrintExStackTrace(e);
                        return "error";
                    }
                }
                //###################################
                //active the fragment with json result by bundle
                //###################################
                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    listener.onResult(result);
                }
            };
            task.execute();
        }catch(Exception e){
            helper.LogPrintExStackTrace(e);
        }

    }
    //endregion
    public interface Wz_getTasks_Listener{
        public void onResult(String str);
    }
    //Wz_getProjects
    public void Async_Wz_getTasks_Listener(final String macAddress, final Wz_getTasks_Listener listener) {
        try{
            AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

                //###################################
                //extract the data and return it
                //###################################
                @Override
                protected String doInBackground(String... params) {
                    try{
                        CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                        //String response = cs.Call(mac_address, memail, mpass);

                        String response = cs.Wz_getTasks(macAddress);
                        String myResponse = response;
                        //Log.e("mytag","response:" +response);
                        myResponse = myResponse.replaceAll("Wz_getTasksResponse", "");
                        myResponse = myResponse.replaceAll("Wz_getTasksResult=", "Wz_getTasks:");
                        myResponse = myResponse.replaceAll(";", "");
                        File_ f = new File_();
                        f.writeTextToFileExternal(context,"tasks.txt",myResponse);
                        //boolean flag = helper.writeCtypeIDandSons(context,myResponse);
                        return "";// myResponse.toString();
                    }catch(Exception e){
                        helper.LogPrintExStackTrace(e);
                        return "error";
                    }
                }
                //###################################
                //active the fragment with json result by bundle
                //###################################
                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    listener.onResult(result);
                }
            };
            task.execute();
        }catch(Exception e){
            helper.LogPrintExStackTrace(e);
        }

    }
    //endregion
    public interface Wz_createISAction_Listener{
        public void onResult(String str);
    }
    //Wz_getProjects
    public void Async_Wz_createISAction(final String macAddress,final String jsonString, final Wz_createISAction_Listener listener) {
        try{
            AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

                //###################################
                //extract the data and return it
                //###################################
                @Override
                protected String doInBackground(String... params) {
                    try{
                        CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                        //String response = cs.Call(mac_address, memail, mpass);

                        String response = cs.Wz_createISAction(macAddress,jsonString);
                        String myResponse = response;
                        Log.e("mytag","response:" +response);
                        myResponse = myResponse.replaceAll("Wz_createISActionResponse", "");
                        myResponse = myResponse.replaceAll("Wz_createISActionResult=", "Wz_createISAction:");
                        myResponse = myResponse.replaceAll(";", "");
                        //boolean flag = helper.writeCtypeIDandSons(context,myResponse);
                        return myResponse;// myResponse.toString();
                    }catch(Exception e){
                        helper.LogPrintExStackTrace(e);
                        return "error";
                    }
                }
                //###################################
                //active the fragment with json result by bundle
                //###################################
                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    listener.onResult(result);
                }
            };
            task.execute();
        }catch(Exception e){
            helper.LogPrintExStackTrace(e);
        }

    }
    //endregion
    public interface Wz_createISActionTime_Listener{
        public void onResult(String str);
    }
    //Wz_createISActionTime
    public void Async_Wz_createISActionTime(final String macAddress,final String jsonString, final Wz_createISActionTime_Listener listener) {
        try{
            AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

                //###################################
                //extract the data and return it
                //###################################
                @Override
                protected String doInBackground(String... params) {
                    try{
                        CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                        //String response = cs.Call(mac_address, memail, mpass);

                        String response = cs.Wz_createISActionTime(macAddress,jsonString);
                        String myResponse = response;
                        Log.e("mytag","response:" +response);
                        myResponse = myResponse.replaceAll("Wz_createISActionTimeResponse", "");
                        myResponse = myResponse.replaceAll("Wz_createISActionTimeResult=", "Wz_createISActionTime:");
                        myResponse = myResponse.replaceAll(";", "");
                        //boolean flag = helper.writeCtypeIDandSons(context,myResponse);
                        return myResponse;// myResponse.toString();
                    }catch(Exception e){
                        helper.LogPrintExStackTrace(e);
                        return "error";
                    }
                }
                //###################################
                //active the fragment with json result by bundle
                //###################################
                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    listener.onResult(result);
                }
            };
            task.execute();
        }catch(Exception e){
            helper.LogPrintExStackTrace(e);
        }

    }
    //endregion
    public interface Wz_getIS_StatusList_Listener{
        public void onResult(String str);
    }
    //Wz_createISActionTime
    public void Async_Wz_getIS_StatusList(final String macAddress, final Wz_getIS_StatusList_Listener listener) {
        try{
            AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

                //###################################
                //extract the data and return it
                //###################################
                @Override
                protected String doInBackground(String... params) {
                    try{
                        CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                        //String response = cs.Call(mac_address, memail, mpass);

                        String response = cs.Wz_getIS_StatusList(macAddress);
                        String myResponse = response;
                        Log.e("mytag","response:" +response);
                        myResponse = myResponse.replaceAll("Wz_getIS_StatusListResponse", "");
                        myResponse = myResponse.replaceAll("Wz_getIS_StatusListResult=", "Wz_getIS_StatusList:");
                        myResponse = myResponse.replaceAll(";", "");
                        //boolean flag = helper.writeCtypeIDandSons(context,myResponse);
                        return myResponse;// myResponse.toString();
                    }catch(Exception e){
                        helper.LogPrintExStackTrace(e);
                        return "error";
                    }
                }
                //###################################
                //active the fragment with json result by bundle
                //###################################
                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    listener.onResult(result);
                }
            };
            task.execute();
        }catch(Exception e){
            helper.LogPrintExStackTrace(e);
        }

    }
    //endregion


    private void addActions(){
        String strJson = "";
        File_ f = new File_();
        strJson = f.readFromFileExternal(context,"is_actions.txt");
        JSONObject j = null;
        JSONArray jarray = null;
        try {
            j = new JSONObject(strJson);
            jarray= j.getJSONArray("Wz_ACTIONS_retList");
            DatabaseHelper.getInstance(context).delete_IS_Actions_Rows("");
            Log.e("MYTAG","jarray length is:" + jarray.length());
            if (jarray.length() == 0){
                Log.e("MYTAG"," jarray is 0");
                return;
            }
        } catch (JSONException e) {
            helper.LogPrintExStackTrace(e);
            Log.e("MYTAG"," Wz_ACTIONS_retList "+e.getMessage().toString());
        }
        for (int i = 0; i < jarray.length(); i++) {
            final JSONObject e;

            try {
                e = jarray.getJSONObject(i);
                IS_Action action= new IS_Action();//Integer.valueOf(cursor.getString(cursor.getColumnIndex("CallID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("AID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("CID"))), cursor.getString(cursor.getColumnIndex("CreateDate")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("statusID"))), cursor.getString(cursor.getColumnIndex("CallPriority")), cursor.getString(cursor.getColumnIndex("subject")), cursor.getString(cursor.getColumnIndex("comments")), cursor.getString(cursor.getColumnIndex("CallUpdate")), cursor.getString(cursor.getColumnIndex("cntrctDate")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("TechnicianID"))), cursor.getString(cursor.getColumnIndex("statusName")), cursor.getString(cursor.getColumnIndex("internalSN")), cursor.getString(cursor.getColumnIndex("Pmakat")), cursor.getString(cursor.getColumnIndex("Pname")), cursor.getString(cursor.getColumnIndex("contractID")), cursor.getString(cursor.getColumnIndex("Cphone")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("OriginID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("ProblemTypeID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("CallTypeID"))), cursor.getString(cursor.getColumnIndex("priorityID")), cursor.getString(cursor.getColumnIndex("OriginName")), cursor.getString(cursor.getColumnIndex("problemTypeName")), cursor.getString(cursor.getColumnIndex("CallTypeName")), cursor.getString(cursor.getColumnIndex("Cname")), cursor.getString(cursor.getColumnIndex("Cemail")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("contctCode"))), cursor.getString(cursor.getColumnIndex("callStartTime")), cursor.getString(cursor.getColumnIndex("callEndTime")), cursor.getString(cursor.getColumnIndex("Ccompany")), cursor.getString(cursor.getColumnIndex("Clocation")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("callOrder"))), cursor.getString(cursor.getColumnIndex("Caddress")), cursor.getString(cursor.getColumnIndex("Ccity")), cursor.getString(cursor.getColumnIndex("Ccomments")), cursor.getString(cursor.getColumnIndex("Cfname")), cursor.getString(cursor.getColumnIndex("Clname")), cursor.getString(cursor.getColumnIndex("techName")), cursor.getString(cursor.getColumnIndex("Aname")), cursor.getString(cursor.getColumnIndex("ContctName")), cursor.getString(cursor.getColumnIndex("ContctAddress")), cursor.getString(cursor.getColumnIndex("ContctCity")), cursor.getString(cursor.getColumnIndex("ContctCell")), cursor.getString(cursor.getColumnIndex("ContctPhone")), cursor.getString(cursor.getColumnIndex("ContctCity")), cursor.getString(cursor.getColumnIndex("Ccell")), cursor.getString(cursor.getColumnIndex("techColor")), cursor.getString(cursor.getColumnIndex("ContctCemail")), cursor.getString(cursor.getColumnIndex("CallParentID")));
                action.setActionID(e.getInt("actionID"));
                action.setTaskID(e.getInt("taskID"));
                action.setStatusID(e.getInt("statusID"));
                action.setOwnerID(e.getInt("ownerID"));
                action.setUserID(e.getInt("userID"));
                action.setDepID(e.getInt("depID"));
                action.setUserCtypeID(e.getInt("userCtypeID"));
                action.setOwnerCtypeID(e.getInt("ownerCtypeID"));
                action.setProjectID(e.getInt("projectID"));
                if (e.getString("ParentActionID").contains("null")){
                }else{
                    action.setParentActionID(Integer.valueOf(e.getString("ParentActionID")));
                }
                action.setActionDate(e.getString("actionDate"));
                action.setActionStartDate(e.getString("actionStartDate"));
                action.setActionDue(e.getString("actionDue"));
                action.setActionDesc(e.getString("actionDesc"));
                String result=e.getString("comments");
                try {
                    String string = e.getString("comments");
                    byte[] utf8 = string.getBytes("UTF-8");
                    string = new String(utf8, "UTF-8");
                    result= String.valueOf(string);
                } catch (UnsupportedEncodingException ee) {
                    helper.LogPrintExStackTrace(ee);
                }
                action.setComments(result);
                action.setPriorityID(e.getString("priorityID"));

                action.setReminderID(e.getString("reminderID"));

                action.setWorkHours(e.getString("WorkHours"));
                action.setWorkEstHours(e.getString("WorkEstHours"));
                action.setCreate(e.getString("Create"));
                action.setLastUpdate(e.getString("LastUpdate"));
                action.setActionLink(e.getString("actionLink"));

                action.setActionRef(e.getString("actionRef"));
                action.setUserCfname(e.getString("userCfname"));
                action.setUserClname(e.getString("userClname"));
                action.setUserCemail(e.getString("userCemail"));

                action.setOwnerCfname(e.getString("ownerCfname"));
                action.setOwnerClname(e.getString("ownerClname"));
                action.setOwnerCemail(e.getString("ownerCemail"));

                action.setStatusName(e.getString("statusName"));
                action.setPriorityName(e.getString("PriorityName"));
                action.setActionType(e.getString("actionType"));
                action.setActionSdate(e.getString("actionSdate"));
                action.setActionEdate(e.getString("actionEdate"));
                action.setWorkHoursM(e.getString("WorkHoursM"));
                action.setWorkEstHoursM(e.getString("WorkEstHoursM"));
                action.setActionPrice(e.getString("actionPrice"));
                action.setStatusColor(e.getString("statusColor"));
                action.setTaskSummery(e.getString("taskSummery"));
                action.setProjectSummery(e.getString("projectSummery"));
                action.setProjectType(e.getString("projectType"));
                action.setActionNum(e.getString("actionNum"));
                action.setActionFrom(e.getString("actionFrom"));
                action.setActionDays(e.getString("actionDays"));
                action.setRemindertime(e.getString("remindertime"));
                action.setExpr1(e.getString("Expr1"));
                action.setProjectDesc(e.getString("projectDesc"));
                DatabaseHelper.getInstance(context).addISAction(action);
            } catch (JSONException e1) {
                helper.LogPrintExStackTrace(e1);

            }
        }

        DatabaseHelper.getInstance(context).getISActions("");
    }



}


