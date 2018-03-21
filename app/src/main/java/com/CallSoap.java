package com;


import android.content.Entity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by doron on 05/02/2016.
 */
public class CallSoap {
    //#############operation name######################
    public  final String USER_Login = "USER_Login";
    public  final String USER_Clients = "USER_Clients";
    public  final String USER_Status = "USER_Status";
    public  final String REMINDERS_retAlert = "REMINDERS_retAlert";
    public  final String USER_Details = "USER_Details";
    public  final String USER_getClientsContacts = "getClientsContacts";
    public  final String ORDER_getMgnetItemsList = "PRODUCTS_ITEMS_LIST";
    public  final String CREATE_OFFLINE = "CREATE_OFFLINE";
    public  final String ORDER_getMgnetClientItemsList = "PRODUCTS_CLIENTS_ITEMS_LIST";
    public  final String CALLS_List = "CALLS_List";
    public  final String Wz_Login = "Wz_Login";
    public  final String Wz_Calls_List = "Wz_Calls_List";
    public  final String Wz_Call_setTime = "Wz_Call_setTime";
    public  final String Wz_Call_getTime = "Wz_Call_getTime";
    public  final String Wz_Call_Update = "Wz_Call_Update";
    public  final String Wz_Call_Statuses = "Wz_Call_Statuses";
    public  final String Wz_Forgot = "Wz_Forgot";
    public  final String Wz_timeReport = "Wz_timeReport";
    public  final String Wz_getState = "Wz_getState";
    public  final String Wz_Update_Call_Field = "Wz_Update_Call_Field";
    public  final String Wz_Get_Client_Item_List = "Wz_Get_Client_Item_List";
    public  final String Wz_getUrl = "Wz_getUrl";
    public  final String Wz_retClientFavorites = "Wz_retClientFavorites";
    public  final String Wz_Send_Call_Offline = "Wz_Send_Call_Offline";
    public  final String Wz_calls_Summary = "Wz_calls_Summary";
    public  final String Wz_retClientReports = "Wz_retClientReports";

    //#############name space######################
    public  final String NAMESPACE = "http://tempuri.org/";
    //#############SOAP ACTION######################
    public final String USER_Login_SOAP_ACTION = "http://tempuri.org/USER_Login";
    public final String USER_Clients_SOAP_ACTION2 = "http://tempuri.org/USER_Clients";
    public final String USER_Status_SOAP_ACTION3 = "http://tempuri.org/USER_Status";
    public final String REMINDERS_retAlert_SOAP_ACTION4 = "http://tempuri.org/REMINDERS_retAlert";
    public final String USER_Details_SOAP_ACTION5 = "http://tempuri.org/USER_Details";
    public final String USER_getClientsContacts_SOAP_ACTION6 = "http://tempuri.org/getClientsContacts";
    public final String ORDER_getMgnetItemsList_SOAP_ACTION7 = "http://tempuri.org/PRODUCTS_ITEMS_LIST";
    public final String CREATE_OFFLINE_SOAP_ACTION8 = "http://tempuri.org/CREATE_OFFLINE";
    public final String ORDER_getMgnetClientItemsList_SOAP_ACTION9 = "http://tempuri.org/PRODUCTS_CLIENTS_ITEMS_LIST";
    public final String CALLS_List_SOAP_ACTION10 = "http://tempuri.org/CALLS_List";



    //new functions
    public final String Wz_Login_SOAP_ACTION = "http://tempuri.org/Wz_Login";
    public final String Wz_Calls_List_SOAP_ACTION = "http://tempuri.org/Wz_Calls_List";
    public  final String Wz_Call_setTime_SOAP_ACTION = "http://tempuri.org/Wz_Call_setTime";
    public  final String Wz_Call_getTime_SOAP_ACTION = "http://tempuri.org/Wz_Call_getTime";
    public  final String Wz_Call_Update_SOAP_ACTION = "http://tempuri.org/Wz_Call_Update";
    public  final String Wz_Call_Statuses_SOAP_ACTION = "http://tempuri.org/Wz_Call_Statuses";
    public  final String Wz_Forgot_SOAP_ACTION = "http://tempuri.org/Wz_Forgot";
    public  final String Wz_timeReport_SOAP_ACTION = "http://tempuri.org/Wz_timeReport";
    public  final String Wz_getState_SOAP_ACTION = "http://tempuri.org/Wz_getState";
    public  final String Wz_Update_Call_Field_SOAP_ACTION = "http://tempuri.org/Wz_Update_Call_Field";
    public  final String Wz_Get_Client_Item_List_SOAP_ACTION = "http://tempuri.org/Wz_Get_Client_Item_List";
    public  final String Wz_getUrl_SOAP_ACTION = "http://tempuri.org/Wz_getUrl";
    public  final String Wz_retClientFavorites_SOAP_ACTION = "http://tempuri.org/Wz_retClientFavorites";
    public  final String Wz_Send_Call_Offline_SOAP_ACTION = "http://tempuri.org/Wz_Send_Call_Offline";
    public  final String Wz_calls_Summary_SOAP_ACTION = "http://tempuri.org/Wz_calls_Summary";
    public  final String Wz_retClientReports_SOAP_ACTION = "http://tempuri.org/Wz_retClientReports";
    //public  final String URL = "http://main.wizenet.co.il/webservices/freelance.asmx";
    public String URL;

    //public  final String URL =readTextFromFile();
    String suffix = "/webservices/freelance.asmx";

    //URL is transfer because i cant pass the getInstance to here
    public CallSoap(String url)
    {
        this.URL = url+suffix;//db.getControlPanel(1).getUrl();
    }
    //###################################
    //LOGIN CALL
    //###################################
    //region login
    public String Call(String mac_address, String email, String pass)
    {
        SoapObject request = new SoapObject(NAMESPACE,USER_Login);//namespace , operation
        request.addProperty("MACaddress",mac_address);
        request.addProperty("Email",email);
        request.addProperty("Pass",pass);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        Object response=null;
        try
        {
            httpTransport.call(USER_Login_SOAP_ACTION, envelope);
            response = envelope.bodyIn;
        }catch (Exception exception)
        {
            //response=exception.toString();
        }
        try{
            return response.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "incorrect";
        }



    }
//endregion

//////////REMINDER///////////
//region reminder
public String Call4(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE,REMINDERS_retAlert);//namespace , operation
        request.addProperty("MACaddress",mac_address);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(REMINDERS_retAlert_SOAP_ACTION4, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {}
        try{
            return response.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "Error";
        }
    }
    //endregion
    //////////REMINDER///////////
//region user_details
    public String Call_USER_Details(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE,USER_Details);//namespace , operation
        request.addProperty("MACaddress",mac_address);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(USER_Details_SOAP_ACTION5, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {}
        try{
            return response.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "Error";
        }
    }
    //endregion
    //###################################
    //CUSTOMERS CALL
    //###################################
    //region clients
    public String Call2(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE,USER_Clients);//namespace , operation
        PropertyInfo pi=new PropertyInfo();
        pi.setName("MACaddress");
        pi.setValue(mac_address);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;

        try
        {
            httpTransport.call(USER_Clients_SOAP_ACTION2, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            response = result;
        }
        catch (Exception exception)
        {
            //response=exception.toString();
        }
        return response.toString();
    }
    //endregion
    //###################################
    //STATUS CALL
    //###################################
//region status
    public String Call3(String mac_address, String longtitude, String latitude)
    {
        SoapObject request = new SoapObject(NAMESPACE,USER_Status);//namespace , operation
        request.addProperty("MACaddress",mac_address);
        request.addProperty("latitude",latitude);
        request.addProperty("longitude",longtitude);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(USER_Status_SOAP_ACTION3, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            //response=exception.toString();
        }
        return response.toString();

    }
//endregion

    //region clientsContacts
    public String Call_getClientsContacts(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE, USER_getClientsContacts);//namespace , operation
        request.addProperty("MACaddress",mac_address);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(USER_getClientsContacts_SOAP_ACTION6, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();

    }
//endregion

    //region get item_order products
    public String getOrdersProducts(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE, ORDER_getMgnetItemsList);//namespace , operation
        request.addProperty("MACaddress",mac_address);
        request.addProperty("PcatID",-1);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(ORDER_getMgnetItemsList_SOAP_ACTION7, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();

    }
//endregion


    //region client items
    public String get_mgnet_client_items_list(String mac_address,String cid)
    {
        SoapObject request = new SoapObject(NAMESPACE, ORDER_getMgnetClientItemsList);//namespace , operation



        request.addProperty("MACaddress",mac_address);

        request.addProperty("CardCode",cid);

        request.addProperty("PcatID",-1);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(ORDER_getMgnetClientItemsList_SOAP_ACTION9, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    //endregion

    //region create_offline
    public String CREATE_OFFLINE(String mac_address,String json)
    {
        SoapObject request = new SoapObject(NAMESPACE, CREATE_OFFLINE);//namespace , operation
        request.addProperty("MACaddress",mac_address);
        request.addProperty("json",json);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(CREATE_OFFLINE_SOAP_ACTION8, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();

    }
//endregion

    //region create_offline
    public String GET_CALLS_LIST(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE, CALLS_List);//namespace , operation
        request.addProperty("MACaddress",mac_address);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(CALLS_List_SOAP_ACTION10, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();

    }
//endregion

//region Wz_Calls_List
public String Wz_Calls_List(String mac_address,int CallStatusID)
{
    SoapObject request = new SoapObject(NAMESPACE, Wz_Calls_List);//namespace , operation
    request.addProperty("MACaddress",mac_address);
    request.addProperty("CallStatusID",CallStatusID);

    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    envelope.dotNet = true;
    envelope.setOutputSoapObject(request);
    HttpTransportSE httpTransport = new HttpTransportSE(URL);
    Object response=null;
    try
    {
        httpTransport.call(Wz_Calls_List_SOAP_ACTION, envelope);
        response = envelope.bodyIn;
    }
    catch (Exception exception)
    {
        response=exception.toString();
    }
    return response.toString();

}
//endregion

    //region Wz_Calls_List
    public String Wz_Call_setTime(String mac_address,int CallID,String action,String latitude,String longtitude)
    {
        SoapObject request = new SoapObject(NAMESPACE, Wz_Call_setTime);//namespace , operation
        request.addProperty("MACaddress",mac_address);
        request.addProperty("CallID",CallID);
        request.addProperty("action",action);
        request.addProperty("latitude",latitude);
        request.addProperty("longtitude",longtitude);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(Wz_Call_setTime_SOAP_ACTION, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();

    }
//endregion
//region Wz_Calls_List
public String Wz_Call_getTime(String mac_address,int CallID,String action)
{
    SoapObject request = new SoapObject(NAMESPACE, Wz_Call_getTime);//namespace , operation
    request.addProperty("MACaddress",mac_address);
    request.addProperty("CallID",CallID);
    request.addProperty("action",action);


    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    envelope.dotNet = true;
    envelope.setOutputSoapObject(request);
    HttpTransportSE httpTransport = new HttpTransportSE(URL);
    Object response=null;
    try
    {
        httpTransport.call(Wz_Call_getTime_SOAP_ACTION, envelope);
        response = envelope.bodyIn;
    }
    catch (Exception exception)
    {
        response=exception.toString();
    }
    return response.toString();

}
//endregion

//region Wz_Call_Update
public String Wz_Call_Update(String mac_address,int CallID,int CallStatusID,String CallAnswer)
{
    SoapObject request = new SoapObject(NAMESPACE, Wz_Call_Update);//namespace , operation
    request.addProperty("MACaddress",mac_address);
    request.addProperty("CallID",CallID);
    request.addProperty("CallStatusID",CallStatusID);
    request.addProperty("CallAnswer",CallAnswer);


    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    envelope.dotNet = true;
    envelope.setOutputSoapObject(request);
    HttpTransportSE httpTransport = new HttpTransportSE(URL);
    Object response=null;
    try
    {
        httpTransport.call(Wz_Call_Update_SOAP_ACTION, envelope);
        response = envelope.bodyIn;
    }
    catch (Exception exception)
    {
        response=exception.toString();
    }
    return response.toString();

}
//endregion

    //region Wz_Call_Statuses
    public String Wz_Call_Statuses(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE, Wz_Call_Statuses);//namespace , operation
        request.addProperty("MACaddress",mac_address);



        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(Wz_Call_Statuses_SOAP_ACTION, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();

    }
//endregion
//region Wz_Forgot
public String Wz_Forgot(String mac_address,String Email)
{
    SoapObject request = new SoapObject(NAMESPACE, Wz_Forgot);//namespace , operation
    request.addProperty("MACaddress",mac_address);
    request.addProperty("Email",Email);


    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    envelope.dotNet = true;
    envelope.setOutputSoapObject(request);
    HttpTransportSE httpTransport = new HttpTransportSE(URL);
    Object response=null;
    try
    {
        httpTransport.call(Wz_Forgot_SOAP_ACTION, envelope);
        response = envelope.bodyIn;
    }
    catch (Exception exception)
    {
        response=exception.toString();
    }
    return response.toString();

}
    //endregion

    //region Wz_timeReport
    public String Wz_timeReport(String mac_address,String action,String latitude,String longtitude)
    {
        SoapObject request = new SoapObject(NAMESPACE, Wz_timeReport);//namespace , operation
        request.addProperty("MACaddress",mac_address);
        request.addProperty("action",action);
        request.addProperty("latitude",latitude);
        request.addProperty("longtitude",longtitude);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(Wz_timeReport_SOAP_ACTION, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();

    }
    //endregion
//region Wz_getState
    public String Wz_getState(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE, Wz_getState);//namespace , operation
        request.addProperty("MACaddress",mac_address);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(Wz_getState_SOAP_ACTION, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();

    }
    //endregion
//region Wz_Update_Call_Field
    public String Wz_Update_Call_Field(String mac_address,String callid,String field,String value)
    {
        SoapObject request = new SoapObject(NAMESPACE, Wz_Update_Call_Field);//namespace , operation
        request.addProperty("MACaddress",mac_address);
        request.addProperty("callid",callid);
        request.addProperty("field",field);
        request.addProperty("value",value);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(Wz_Update_Call_Field_SOAP_ACTION, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();

    }
    //endregion

    //region Wz_Get_Client_Item_List
    public String Wz_Get_Client_Item_List(String mac_address,String cid)
    {
        SoapObject request = new SoapObject(NAMESPACE, Wz_Get_Client_Item_List);//namespace , operation



        request.addProperty("MACaddress",mac_address);

        request.addProperty("CardCodes",cid);

        request.addProperty("PcatID",-1);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(Wz_Get_Client_Item_List_SOAP_ACTION, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    //endregion
    //region Wz_Get_Client_Item_List
    public String Wz_getUrl(String mac_address,String msid)
    {
        SoapObject request = new SoapObject(NAMESPACE, Wz_getUrl);//namespace , operation



        request.addProperty("MACaddress",mac_address);

        request.addProperty("msid",msid);



        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE("http://main.wizenet.co.il/webservices/freelance.asmx");
        Object response=null;
        try
        {
            httpTransport.call(Wz_getUrl_SOAP_ACTION, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    //endregion
    //region Wz_retClientFavorites
    public String Wz_retClientFavorites(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE, Wz_retClientFavorites);//namespace , operation
        request.addProperty("MACaddress",mac_address);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(Wz_retClientFavorites_SOAP_ACTION, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    //endregion
    //region Wz_Send_Call_Offline
    public String Wz_Send_Call_Offline(String mac_address,String jsonString)
    {
        SoapObject request = new SoapObject(NAMESPACE, Wz_Send_Call_Offline);//namespace , operation
        request.addProperty("MACaddress",mac_address);
        request.addProperty("jsonString",jsonString);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(Wz_Send_Call_Offline_SOAP_ACTION, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    //endregion
    //region Wz_calls_Summary
    public String Wz_calls_Summary(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE, Wz_calls_Summary);//namespace , operation
        request.addProperty("MACaddress",mac_address);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(Wz_calls_Summary_SOAP_ACTION, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    //endregion
    //region Wz_retClientReports
    public String Wz_retClientReports(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE, Wz_retClientReports);//namespace , operation
        request.addProperty("MACaddress",mac_address);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(Wz_retClientReports_SOAP_ACTION, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    //endregion
}
