package com;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.Classes.Call;
import com.Classes.Ccustomer;
import com.Classes.Ctype;
import com.Classes.IS_Action;
import com.Classes.IS_Status;
import com.Classes.Product;
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
    File_ f = new File_();
    public JSONArray retJsonArrayFromResponse(String response,String name){
        String myResponse = response;
        myResponse = myResponse.replaceAll(name+"Response", "");
        myResponse = myResponse.replaceAll(name+"Result=", "'a':");
        myResponse = myResponse.replaceAll(";", "");

        try {
            JSONArray j = getJSONArrayByName(myResponse,"a");
            return j;
        } catch (Exception e) {
            helper.LogPrintExStackTrace(e);
            return null;
        }
    }
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
    //[{bla}]
    public  JSONArray getJSONArrayFromString(String str,Context context){
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(str);
            if (jsonArray.length() == 0)
                return null;

            return jsonArray;
        } catch (JSONException e) {
            helper.LogPrintExStackTrace(e);
            return null;
        }


    }

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
            helper.LogPrintExStackTrace(e);
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

    public List<Product> getProductsByFile(Context c) {
        String strJson = "";
        List<Product> a = new ArrayList<Product>();

        strJson = f.readFromFileExternal(c,"products.txt");
        JSONArray jArray = new JSONArray();
        jArray = getJsonArrayFromString(strJson);
        if (jArray == null){
            return a;
        }
        for (int i=0;i<jArray.length();i++){
            try {
                a.add(new Product(
                        jArray.getJSONObject(i).getString("PID"),
                        jArray.getJSONObject(i).getString("Pname"),
                        jArray.getJSONObject(i).getString("Pmakat"),
                        "",
                        jArray.getJSONObject(i).getString("Pprice"),
                        jArray.getJSONObject(i).getString("Pstock")));//jArray.getJSONObject(i).getString("Pserial")
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //{"PID":11306,"Pmakat":"NB-509","Pmodel":null,"Pname":"LENOVO 59385926 TABLET 7\u0027\u0027 ANDROID 4.2 1Y","PdescS":"","PdescL":null,"Pprice":1.0000,"POprice":1.0000,"PSprice":null,"PCprice":null,"PMprice":1.0000,"Pcomment":"","Ptime":null,"Pguarantee":null,"Pshipping":null,"Ppayment":"","PimageS":null,"PimageB":null,"PcompanyID":null,"PsupplierID":null,"PcatID":null,"PStypeID":1,"Pspecial":false,"Ponline":true,"Psite":0,"Plight":false,"PsaleSpecial":false,"Porder":null,"Pstock":0,"PtypeID":null,"LNG":"HE","PimageR":null,"Pdisk":null,"Pmemory":null,"Pscreen":null,"U_nocat_7055524":null},
        return a;
    }
    public JSONArray getJsonArrayFromString(String strJson){
        JSONArray j;
        try {
            j= new JSONArray(strJson);
        } catch (JSONException e) {
            j=null;
            helper.LogPrintExStackTrace(e);
        }
        return j;
    }
    public void addActions(Context context){
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
    public String addCalls(Context context){
        String ret = "";
        String strJson = "";
        strJson=f.readFromFileExternal(context,"calls.txt");

        JSONObject j = null;
        JSONArray jarray = null;
        try {
            j = new JSONObject(strJson);
            jarray= j.getJSONArray("Calls");
            DatabaseHelper.getInstance(context).deleteAllCalls();
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
                addCallByJSONObject(e,context);

            } catch (JSONException e1) {
                Log.e("MYTAG","model add call: " +e1.getMessage());
                return "1";
            }
            //ADD TO DATABASE
            //ret.add(name);
        }
        return "0";
    }
    public void addCallByJSONObject(JSONObject e,Context context){

        Call call= new Call();//Integer.valueOf(cursor.getString(cursor.getColumnIndex("CallID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("AID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("CID"))), cursor.getString(cursor.getColumnIndex("CreateDate")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("statusID"))), cursor.getString(cursor.getColumnIndex("CallPriority")), cursor.getString(cursor.getColumnIndex("subject")), cursor.getString(cursor.getColumnIndex("comments")), cursor.getString(cursor.getColumnIndex("CallUpdate")), cursor.getString(cursor.getColumnIndex("cntrctDate")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("TechnicianID"))), cursor.getString(cursor.getColumnIndex("statusName")), cursor.getString(cursor.getColumnIndex("internalSN")), cursor.getString(cursor.getColumnIndex("Pmakat")), cursor.getString(cursor.getColumnIndex("Pname")), cursor.getString(cursor.getColumnIndex("contractID")), cursor.getString(cursor.getColumnIndex("Cphone")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("OriginID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("ProblemTypeID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("CallTypeID"))), cursor.getString(cursor.getColumnIndex("priorityID")), cursor.getString(cursor.getColumnIndex("OriginName")), cursor.getString(cursor.getColumnIndex("problemTypeName")), cursor.getString(cursor.getColumnIndex("CallTypeName")), cursor.getString(cursor.getColumnIndex("Cname")), cursor.getString(cursor.getColumnIndex("Cemail")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("contctCode"))), cursor.getString(cursor.getColumnIndex("callStartTime")), cursor.getString(cursor.getColumnIndex("callEndTime")), cursor.getString(cursor.getColumnIndex("Ccompany")), cursor.getString(cursor.getColumnIndex("Clocation")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("callOrder"))), cursor.getString(cursor.getColumnIndex("Caddress")), cursor.getString(cursor.getColumnIndex("Ccity")), cursor.getString(cursor.getColumnIndex("Ccomments")), cursor.getString(cursor.getColumnIndex("Cfname")), cursor.getString(cursor.getColumnIndex("Clname")), cursor.getString(cursor.getColumnIndex("techName")), cursor.getString(cursor.getColumnIndex("Aname")), cursor.getString(cursor.getColumnIndex("ContctName")), cursor.getString(cursor.getColumnIndex("ContctAddress")), cursor.getString(cursor.getColumnIndex("ContctCity")), cursor.getString(cursor.getColumnIndex("ContctCell")), cursor.getString(cursor.getColumnIndex("ContctPhone")), cursor.getString(cursor.getColumnIndex("ContctCity")), cursor.getString(cursor.getColumnIndex("Ccell")), cursor.getString(cursor.getColumnIndex("techColor")), cursor.getString(cursor.getColumnIndex("ContctCemail")), cursor.getString(cursor.getColumnIndex("CallParentID")));
        try {
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
            // take care for erp cases where fields are null
            call.setOriginID(!e.getString("OriginID").contains("null") ? e.getInt("OriginID") : -1);
            call.setProblemTypeID(!e.getString("ProblemTypeID").contains("null") ? e.getInt("ProblemTypeID") : -1);
            call.setCallTypeID(!e.getString("CallTypeID").contains("null") ? e.getInt("CallTypeID") : -1);


            call.setPriorityID(e.getString("priorityID"));
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
            Log.e("mytag","exception insert call");
            helper.LogPrintExStackTrace(e1);
            e1.printStackTrace();
        }

    }

    public boolean addCallsFromJSONArray(JSONArray jarray,Context c) {
        try{
            if (jarray.length() > 0){
                for (int i = 0; i < jarray.length(); i++) {
                    try {
                        JSONObject e = jarray.getJSONObject(i);
                        addCallByJSONObject(e,c);

                    } catch (JSONException e1) {
                        Log.e("mytag","addCallsFromJSONArray:" );
                        helper.LogPrintExStackTrace(e1);
                        e1.printStackTrace();
                        return false;
                    }
                }
                return true;

            }
        }catch(Exception e){

        }return false;



    }

    public String getElementValueInJarray(JSONArray jarray, String subject) {
        try {
            JSONObject e = jarray.getJSONObject(0);
            return e.getString(subject);
        } catch (JSONException e1) {
            helper.LogPrintExStackTrace(e1);
            return "";
        }
    }
}

