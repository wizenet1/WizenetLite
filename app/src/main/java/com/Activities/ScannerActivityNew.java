package com.Activities;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Helper;
import com.Adapters.NamesAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScannerActivityNew extends Activity {
    Helper helper;
    String strJson = null;
    Button scan;
    EditText txt_serial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_new);

        TextView example = (TextView)  findViewById(R.id.example);
        try {
            Uri data = getIntent().getData();
            Log.e("mytag",data.toString());
            String scheme;
            String host;
            //List<String> params = new ArrayList<>();
            //scheme = data.getScheme(); // "http"
            //host = data.getHost(); // "twitter.com"
            //params = data.getPathSegments();
            //String first = params.get(0); // "status"
            //String second = params.get(1); // "1234"
            //example.setText(params.size());
        }catch (Exception e){
            Log.e("mytag",e.getMessage().toString());
        }

         helper = new Helper();
        scan = (Button) findViewById(R.id.zxing_barcode_scanner);
        txt_serial = (EditText) findViewById(R.id.txt_serial);
        //List<String> myList = new ArrayList<>();

        List<String> responseList = new ArrayList<String>();

        strJson = helper.readTextFromFileCustomers();
        JSONObject j = null;
        JSONArray jarray = null;
        try {
            j = new JSONObject(strJson);
            jarray= j.getJSONArray("Customers");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < jarray.length(); i++) {
            final JSONObject e;
            String name = null;
            try {
                e = jarray.getJSONObject(i);
                name = e.getString("Ccompany")+'|'+e.getString("CID");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            responseList.add(name);
        }

        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.auto_complete_id);
        textView.setText("test");
//        try{
//            textView.setText(first);
//        }catch (Exception e){
//
//        }

        List<String> namesList = responseList; //your names list;
                NamesAdapter namesAdapter = new NamesAdapter(
                ScannerActivityNew.this,
                R.layout.search_auto_complete,
                R.id.name_search,
                namesList
        );
        final Activity activity = this;
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                IntentIntegrator integrator;
//                integrator = new IntentIntegrator(activity);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//                integrator.setPrompt("צלם לרוחב");
//                integrator.setCameraId(0);
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(false);
//                integrator.initiateScan();
            }
        });
        //set adapter into listStudent
        textView.setAdapter(namesAdapter);
        //STARTS AUTO COMPLETE FROM CHAR (0) (1)

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if(result != null) {
//            if(result.getContents() == null) {
//                Log.d("MainActivity", "Cancelled scan");
//                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
//            } else {
//                //goToMenuFragment();
//                Log.d("MainActivity", "Scanned");
//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//                txt_serial.setText(result.getContents());
//                try{
//                    Bundle bundle = new Bundle();
//                    bundle.putString("DocNum", result.getContents());   //parameters are (key, value).
//                    //ClipboardManager clipboard = (ClipboardManager) getApplication().getSystemService (getApplication().CLIPBOARD_SERVICE);
//                    //ClipData clip = ClipData.newPlainText(result.getContents(), result.getContents());
//                    //clipboard.setPrimaryClip(clip);
//                }catch (Exception ex){
//                    Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
//                }
//            }
//        } else {
//
//        }
    }



}
