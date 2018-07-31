package com;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.Classes.Ccustomer;
import com.Classes.Ctype;
import com.Classes.IS_Action;
import com.Classes.IS_Status;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by WIZE02 on 16/07/2018.
 */

public class Json_ {
    Helper helper = new Helper();

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        }
        catch (JSONException ex) {

            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
    JSONArray jarray = null;

    /**
     * example [{bla},{bla}]
     * @return
     */
    public JSONArray getJSONArrayFromFile(String filenameWithSuffix,Context ctx){
        JSONArray jarray = null;
        String strJson = "";
        File_ f = new File_();

        strJson = f.readFromFileExternal(ctx,filenameWithSuffix);
        if (!isJSONValid(strJson)){
            return jarray;
        }
        try {
            jarray =  new JSONArray(strJson);
        } catch (JSONException e) {
            helper.LogPrintExStackTrace(e);
        }
        return jarray;
    }
    public JSONArray getJSONArrayByName(String strJson,String funcName){
        JSONArray jarray = null;
        try {
            JSONObject j = null;
            j = new JSONObject(strJson);
            jarray = j.getJSONArray(funcName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jarray;
    }

    public List<IS_Status> getISStatusList(Context ctx){

        List<IS_Status> list = new ArrayList<IS_Status>() ;
        JSONArray jarray = null;
        try {
            String strJson = "";
            File_ f = new File_();
            strJson = f.readFromFileExternal(ctx,"is_status.txt");
            jarray =  new JSONArray(strJson);

        } catch (JSONException e) {
            helper.LogPrintExStackTrace(e);
            return list;
        }
        for (int i = 0; i < jarray.length(); i++) {
            final JSONObject e;
            try {
                e = jarray.getJSONObject(i);
                list.add(new IS_Status(e.getString("statusID"),e.getString("statusName")));
            } catch (JSONException e1) {
                helper.LogPrintExStackTrace(e1);
                return list;
            }
        }
        return list;
    }
    public List<Ccustomer> getClientsListByJSONarray(JSONArray j,Context ctx){

        List<Ccustomer> list = new ArrayList<Ccustomer>() ;
        JSONArray jarray = null;
        try{
            for (int i = 0; i < j.length(); i++) {
                final JSONObject e;
                    e = j.getJSONObject(i);
                    list.add(new Ccustomer(
                            "",
                            "",
                            e.getString("Cemail"),
                            e.getString("Cphone"),
                            e.getString("Ccell"),
                            e.getString("Ccompany"),
                            e.getString("CID"),
                            e.getString("Caddress"),
                            ""));
            }
        }catch(Exception e){
            return list;
        }
        return list;
    }

    public List<String> getISStatusString(Context ctx){
        ArrayList<IS_Status> is_statuses;
        JSONArray jsonArray = new JSONArray();
        jsonArray = getJSONArrayFromFile("is_status.txt",ctx);
        ArrayList<String> listdata = new ArrayList<String>();
        is_statuses = new ArrayList<IS_Status>();
        JSONArray jArray = (JSONArray)jsonArray;
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                try {
                    is_statuses.add(new IS_Status(
                            jArray.getJSONObject(i).getString("statusID"),
                            jArray.getJSONObject(i).getString("statusName")))
                    ;
                    listdata.add(jArray.getJSONObject(i).getString("statusName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return listdata;
    }
    private Ccustomer[] getCustomerListByJson(Context ctx) {
        Helper helper = new Helper();
        File_ f = new File_();
        String myString = "";
        myString = f.readFromFileExternal(ctx, "wzClients.txt");
        JSONObject j = null;
        int length = 0;
        Ccustomer[] ccustomers;//= new Ccustomer[5];
        try {
            j = new JSONObject(myString);
            JSONArray jarray = j.getJSONArray("Wz_ret_ClientsAddressesByActions");
            length = jarray.length();
        } catch (JSONException e) {
            helper.LogPrintExStackTrace(e);
            return null;
        }
        Toast.makeText(ctx,"length:" + length, Toast.LENGTH_SHORT).show();
        ccustomers = new Ccustomer[length];
        ccustomers = helper.getWizenetClientsFromJson(myString);
        return ccustomers;
    }
    public boolean addCcustomerToDBfromFile(Context context){

        boolean ret = false;
        String strJson = "";
        File_ f = new File_();
        strJson = f.readFromFileExternal(context,"wzClients.txt");
        if (isJSONValid(strJson) == false){
            return false;
        }
        JSONObject j = null;
        JSONArray jarray = null;
        try {
            j = new JSONObject(strJson);
            jarray= j.getJSONArray("Wz_ret_ClientsAddressesByActions");
            //need to delete or update the ccustomers
            //DatabaseHelper.getInstance(context).delete_IS_Actions_Rows("");
            Log.e("MYTAG","jarray length is:" + jarray.length());
            if (jarray.length() == 0){
                //Log.e("MYTAG"," jarray is 0");
                return false;
            }
        } catch (JSONException e) {
            helper.LogPrintExStackTrace(e);
            //Log.e("MYTAG"," Wz_ACTIONS_retList "+e.getMessage().toString());
        }
        DatabaseHelper.getInstance(context).deleteAllCcustomers();
        for (int i = 0; i < jarray.length(); i++) {
            final JSONObject e;
            try {
                e = jarray.getJSONObject(i);
                Ccustomer c= new Ccustomer();//Integer.valueOf(cursor.getString(cursor.getColumnIndex("CallID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("AID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("CID"))), cursor.getString(cursor.getColumnIndex("CreateDate")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("statusID"))), cursor.getString(cursor.getColumnIndex("CallPriority")), cursor.getString(cursor.getColumnIndex("subject")), cursor.getString(cursor.getColumnIndex("comments")), cursor.getString(cursor.getColumnIndex("CallUpdate")), cursor.getString(cursor.getColumnIndex("cntrctDate")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("TechnicianID"))), cursor.getString(cursor.getColumnIndex("statusName")), cursor.getString(cursor.getColumnIndex("internalSN")), cursor.getString(cursor.getColumnIndex("Pmakat")), cursor.getString(cursor.getColumnIndex("Pname")), cursor.getString(cursor.getColumnIndex("contractID")), cursor.getString(cursor.getColumnIndex("Cphone")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("OriginID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("ProblemTypeID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("CallTypeID"))), cursor.getString(cursor.getColumnIndex("priorityID")), cursor.getString(cursor.getColumnIndex("OriginName")), cursor.getString(cursor.getColumnIndex("problemTypeName")), cursor.getString(cursor.getColumnIndex("CallTypeName")), cursor.getString(cursor.getColumnIndex("Cname")), cursor.getString(cursor.getColumnIndex("Cemail")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("contctCode"))), cursor.getString(cursor.getColumnIndex("callStartTime")), cursor.getString(cursor.getColumnIndex("callEndTime")), cursor.getString(cursor.getColumnIndex("Ccompany")), cursor.getString(cursor.getColumnIndex("Clocation")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("callOrder"))), cursor.getString(cursor.getColumnIndex("Caddress")), cursor.getString(cursor.getColumnIndex("Ccity")), cursor.getString(cursor.getColumnIndex("Ccomments")), cursor.getString(cursor.getColumnIndex("Cfname")), cursor.getString(cursor.getColumnIndex("Clname")), cursor.getString(cursor.getColumnIndex("techName")), cursor.getString(cursor.getColumnIndex("Aname")), cursor.getString(cursor.getColumnIndex("ContctName")), cursor.getString(cursor.getColumnIndex("ContctAddress")), cursor.getString(cursor.getColumnIndex("ContctCity")), cursor.getString(cursor.getColumnIndex("ContctCell")), cursor.getString(cursor.getColumnIndex("ContctPhone")), cursor.getString(cursor.getColumnIndex("ContctCity")), cursor.getString(cursor.getColumnIndex("Ccell")), cursor.getString(cursor.getColumnIndex("techColor")), cursor.getString(cursor.getColumnIndex("ContctCemail")), cursor.getString(cursor.getColumnIndex("CallParentID")));

                c.setCID(e.getString("CID"));
                c.setCParentID(e.getString("CParentID"));
                c.setTargetWeight(e.getString("TargetWeight"));
                c.setCusername(e.getString("Cusername"));
                c.setCpassword(e.getString("Cpassword"));
                c.setCfname(e.getString("Cfname"));
                c.setClname(e.getString("Clname"));
                c.setCemail(e.getString("Cemail"));
                c.setCaddress(e.getString("Caddress"));
                c.setCcity(e.getString("Ccity"));
                c.setCArea(e.getString("CArea"));
                c.setCcountry(e.getString("Ccountry"));
                c.setCstate(e.getString("Cstate"));
                c.setCzip(e.getString("Czip"));
                c.setCphone(e.getString("Cphone"));
                c.setCcell(e.getString("Ccell"));
                c.setCfax(e.getString("Cfax"));
                c.setCdate(e.getString("Cdate"));
                c.setCBirthDate(e.getString("CBirthDate"));
                c.setCStartDate(e.getString("CStartDate"));
                c.setCEndDate(e.getString("CEndDate"));
                c.setCFemilyStatus(e.getString("CFemilyStatus"));
                c.setCBuySum(e.getString("CBuySum"));
                c.setCcompany(e.getString("Ccompany"));
                c.setCprofession(e.getString("Cprofession"));
                c.setLNG(e.getString("LNG"));
                c.setCTypeID(e.getString("CTypeID"));
                c.setCCodeID(e.getString("CCodeID"));
                c.setCConfirm(e.getString("CConfirm"));
                c.setCInterest(e.getString("CInterest"));
                c.setCduty(e.getString("Cduty"));
                c.setCPoints(e.getString("CPoints"));
                c.setSendEmail(e.getString("SendEmail"));
                c.setCcName(e.getString("ccName"));
                c.setCcID(e.getString("ccID"));
                c.setCcType(e.getString("ccType"));
                c.setCcNo(e.getString("ccNo"));
                c.setCcExp(e.getString("ccExp"));
                c.setCcpayment(e.getString("ccpayment"));
                c.setCStatus(e.getString("CStatus"));
                c.setCstatusName(e.getString("CstatusName"));
                c.setCage("");//e.getString("Cage"));
                c.setCIMG(e.getString("CIMG"));
                c.setCSex(e.getString("CSex"));
                c.setCcomments(e.getString("Ccomments"));
                c.setCJoinerID(e.getString("CJoinerID"));
                c.setCweb(e.getString("Cweb"));
                c.setCstatusID(e.getString("CstatusID"));
                c.setCstatusDate(e.getString("CstatusDate"));
                c.setCstatusID2(e.getString("CstatusID2"));
                c.setCstatusDate2(e.getString("CstatusDate2"));
                c.setIsActive(e.getString("IsActive"));
                c.setHerb_ID(e.getString("Herb_ID"));
                c.setAge(e.getString("Age"));
                c.setWeight(e.getString("Weight"));
                c.setLooseWeight(e.getString("LooseWeight"));
                c.setHeight(e.getString("Height"));
                c.setAgentID(e.getString("AgentID"));
                c.setLastLogin(e.getString("LastLogin"));
                c.setSlpCode(e.getString("SlpCode"));
                c.setLastPassword(e.getString("LastPassword"));
                c.setCTypeName(e.getString("CTypeName"));
                c.setLongtitude(e.getString("Longtitude"));
                c.setLatitude(e.getString("Latitude"));
                c.setLink(e.getString("Link"));
                //"Distance" , c.getDistance());


                DatabaseHelper.getInstance(context).addNewCcustomer(c);
            } catch (JSONException e1) {
                helper.LogPrintExStackTrace(e1);
                return false;
            }
        }
        return ret;
    }

}
