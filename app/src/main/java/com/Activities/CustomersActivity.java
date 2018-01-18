package com.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.CallSoap;
import com.DatabaseHelper;
import com.Fragments.FragmentCustomer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class CustomersActivity extends FragmentActivity {
    DatabaseHelper db;
//###################################
    //
//###################################

    File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/contacts.txt");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        db = new DatabaseHelper(this);
        if(!myFile.exists()){
            //txt isn't exists
            Toast.makeText(this,"txt isn't exists",Toast.LENGTH_LONG).show();
            new AsynchCallSoap2().execute();
        }else{//txt  exists
            Toast.makeText(this,"txt exists",Toast.LENGTH_LONG).show();
            String txt = readTextFromFile();
            goToCustomersFragment(txt);
        }
    }



    //###################################
    //asych item_call to CallSoap to webservice
    //###################################
    public class AsynchCallSoap2 extends AsyncTask<String, Void, String> {

        //###################################
        //extract the data and return it
        //###################################
        @Override
        protected String doInBackground(String... params) {
            // USER_ClientsResponse
            CallSoap cs = new CallSoap("http://main.wizenet.co.il");//db.getControlPanel(1).getUrl());
            //String response = cs.Call(mac_address, memail, mpass);
            String response = cs.Call2("some mac_address");
            String myResponse = response;
            myResponse = myResponse.replaceAll("USER_ClientsResponse","");
            myResponse = myResponse.replaceAll("USER_ClientsResult=","Customers:");
            myResponse = myResponse.replaceAll(";","");


            FileWriter fw = null;
            try {
                fw = new FileWriter(myFile);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(myResponse);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return myResponse.toString();
        }

        //###################################
        //active the fragment with json result by bundle
        //###################################
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            goToCustomersFragment(result);


        }
    }
    public void goToCustomersFragment(String txt){
        Bundle bundle=new Bundle();
        bundle.putString("name", txt);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        FragmentCustomer frag = new FragmentCustomer();
        frag.setArguments(bundle);
        ft.add(R.id.container, frag, "userListFragment");

        ft.commit();
    }
    public String readTextFromFile() {
        String ret = "";

        try {
            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/contacts.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow;
            }
            ret = aBuffer.toString().trim();
            myReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            InputStream inputStream = openFileInput("myurl.txt");
//
//            if (inputStream != null) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String receiveString = "";
//                StringBuilder stringBuilder = new StringBuilder();
//
//                while ((receiveString = bufferedReader.readLine()) != null) {
//                    stringBuilder.append(receiveString);
//                }
//
//                inputStream.close();
//                ret = stringBuilder.toString();
//                Toast.makeText(getApplicationContext(), ret.toString(), Toast.LENGTH_LONG).show();


//        } catch (FileNotFoundException e) {
//            Log.e(TAG, "File_ not found: " + e.toString());
//            return false;
//        } catch (IOException e) {
//            Log.e(TAG, "Can not read file: " + e.toString());
//            return false;
//        }
        return ret;
    }

}
