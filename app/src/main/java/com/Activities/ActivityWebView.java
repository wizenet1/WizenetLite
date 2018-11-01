package com.Activities;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.Classes.Call;
import com.DatabaseHelper;
import com.Helper;
import com.Adapters.CallsAdapter;
import com.model.Model;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityWebView extends FragmentActivity {

    Helper helper ;
    Context ctx;

    DatabaseHelper db;
    ListView myList;
    LocationManager manager = null;
    boolean result = false;
    private EditText mSearchEdt;
    CallsAdapter callsAdapter; //to refresh the list
    ArrayList<Call> data2 = new ArrayList<Call>() ;
    private TextWatcher mSearchTw;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageandroid5;
    private final static int FILECHOOSER_RESULTCODE=1;


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if(requestCode==FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage) return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_webview);
        ctx = this;

        int callid = -1;
        int cid = -1;
        int technicianid = -1;
        String action = "";
        String specialurl = "";
        Bundle b = getIntent().getExtras();
        if(b != null){
            callid = b.getInt("callid");
            cid = b.getInt("cid");
            technicianid = b.getInt("technicianid");
            action = b.getString("action");
            try{
                specialurl =b.getString("specialurl");
                Log.e("mytag","special url: "+specialurl);
            }catch (Exception e){
                Log.e("mytag","special url ex: " + e.getMessage());
            }

        }

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        View customNav = LayoutInflater.from(this).inflate(R.layout.top_bar_back, null); // layout which contains your button.

        actionBar.setCustomView(customNav, lp1);
        Button iv = (Button) customNav.findViewById(R.id.back);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //Toast.makeText(getApplicationContext(),"clicked", Toast.LENGTH_LONG).show();
            }
        });





        final WebView  mWebview  = new WebView(this);
        final Activity activity = this;
        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

        mWebview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }




        });

        try{


            mWebview.setWebChromeClient(new WebChromeClient() {
                //The undocumented magic method override
                //Eclipse will swear at you if you try to put @Override here


                public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                    ActivityWebView.this.showAttachmentDialog(uploadMsg);
                    Log.e("mytag",uploadMsg.toString());
                }

                // For Android > 3.x
                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                    ActivityWebView.this.showAttachmentDialog(uploadMsg);
                    Log.e("mytag",uploadMsg.toString());
                }

                // For Android > 4.1
                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                    ActivityWebView.this.showAttachmentDialog(uploadMsg);
                    Log.e("mytag",uploadMsg.toString());
                }

                public boolean onShowFileChooser (WebView webView,
                                                  ValueCallback<Uri[]> filePathCallback,
                                                  WebChromeClient.FileChooserParams fileChooserParams){
                    ///ActivityWebView.this.showAttachmentDialog(filePathCallback.onReceiveValue());
                    //openFileChooserImplForAndroid5(filePathCallback);

                  // ActivityWebView.this.mUploadMessageandroid5 = filePathCallback;

                  // Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                  // i.addCategory(Intent.CATEGORY_OPENABLE);
                  // i.setType("*/*");

                  //Intent fileIntent= fileChooserParams.createIntent();
                  // fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
                  // fileIntent.setType("*/*");
                  // ActivityWebView.this.startActivityForResult(fileIntent,FILECHOOSER_RESULTCODE);
                    //ActivityWebView.this.startActivityForResult(Intent.createChooser(fileintent, "Choose type of attachment"), FILECHOOSER_RESULTCODE);
                   Log.e("mytag","step4");
                   return true;
                }
            });

        }catch (Exception e){
            Log.e("mytag",e.getMessage() + " " + e.getStackTrace().toString());
        }

        String url = "";
        switch(action) {
            case "dynamic":
                url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL")
                        +"/IN.aspx?url="+ specialurl.replace(DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL"),"")
                        +"&MACAddress=" + helper.getMacAddr(ctx);
                //Toast.makeText(activity, url, Toast.LENGTH_SHORT).show();
                Log.e("mytag","url: " + url);
                break;
            case "calltimedetails":
                url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL")
                        +"/IN.aspx?url="
                        + "/iframe.aspx?control=modulesServices/reportcalltimebytech_details&OdateFrom=" + getCurrentTimeStamp() + "&OdateTo=" + getCurrentTimeStamp() + "&techListDashboard=" + technicianid + "&CID=-1&strstatus=-999"
                        //+ "/iframe.aspx?control=modulesServices/dashboard_report&action=totalCloseCallsToday&techctypeid=&calltypeidnot=&calltype=&ClientCtypeID=&techid=" + technicianid + ""
                        +"&MACAddress=" + helper.getMacAddr(ctx);
                Log.e("mytag","url: " + url);
                break;
            case "dashboard":
                url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL")
                        +"/IN.aspx?url="
                        + "/iframe.aspx?control=modulesServices/dashboard_report&action=totalCloseCallsToday&techctypeid=&calltypeidnot=&calltype=&ClientCtypeID=&techid=" + technicianid + ""
                        +"&MACAddress=" + helper.getMacAddr(ctx);
                Log.e("mytag","url: " + url);
                break;
            case "calltime":
                url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL")
                        +"/IN.aspx?url="
                        + "/iframe.aspx?control=/modulesServices/CallRepHistory&CallID=" + String.valueOf(callid) + "&class=tdCallRepHistory&mobile=True"
                        +"&MACAddress=" + helper.getMacAddr(ctx);
                Log.e("mytag","url: " + url);
                break;
            case "callparts":
                url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL")
                        +"/IN.aspx?url="
                        + "/iframe.aspx?control=modulesServices%2fCallParts&CallID=" + String.valueOf(callid) + "&type=customer&val=" + String.valueOf(cid) + ""
                        +"&MACAddress=" + helper.getMacAddr(ctx);
                Log.e("mytag","url: " + url);
                break;
            case "mycalls" :
                url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL")
                        +"/IN.aspx?url="
                        + "/mobile/control.aspx?control=modulesService/myCalls"
                        +"&MACAddress=" + helper.getMacAddr(ctx);
                Log.e("mytag","url: " + url);
                break;
            case "callfiles" :
                url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL")
                        +"/IN.aspx?url="
                        + "/iframe.aspx?control=/modulesServices/CallsFiles&CallID=" + String.valueOf(callid) + "&class=CallsFiles_appCell&mobile=True"
                        +"&MACAddress=" + helper.getMacAddr(ctx);
                Log.e("mytag","url: " + url);
                break;
            case "history" :
                url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL")
                        +"/IN.aspx?url="
                        + "/iframe.aspx?control=/modulesservices/callhistoryAll&CallID=" + String.valueOf(callid) + "&CID=" + String.valueOf(cid) + "&class=AppCelltable&mobile=True"
                        +"&MACAddress=" + helper.getMacAddr(ctx);
                Log.e("mytag","url: " + url);
                break;
            case "customercase" :
                url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL")
                        +"/IN.aspx?url="
                        +"/iframe.aspx?control=modules/tableextrafields&table=calls&pk=callid&pkvalue=" + String.valueOf(callid) + ""
                        //+ "/iframe.aspx?control=modules/TableExtraFields&table=clients&pk=cid&pkvalue=" + String.valueOf(cid) + "&mobile=True"
                        +"&MACAddress=" + helper.getMacAddr(ctx);
                Log.e("mytag","url: " + url);
                break;
            case "goToUserHistory" :
                url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL")
                        +"/IN.aspx?url="
                        + "/iframe.aspx?control=/modulesProjects/UsersTimeReport"
                        //+ "/mobile/control.aspx?control=/modulesProjects/UsersTimeReport"
                        +"&MACAddress=" + helper.getMacAddr(ctx);

                Log.e("mytag","url: " + url);
                break;
            case "masofon" :
                url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL")
                        +"/IN.aspx?url="
                        + "/AppMasofon/masofon?1=1"
                        +"&MACAddress=" + helper.getMacAddr(ctx);
                Log.e("mytag","url: " + url);
                break;
            case "callsign" :

               // String url = "";//DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL") + "/modulesSign/sign.aspx?callID=" + String.valueOf(call.getCallID());
//            url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL")
//                    +"/IN.aspx?url="
//                    + "/modulesSign/sign.aspx?callID=" + String.valueOf(call.getCallID())
//                    +"&MACAddress=" + helper.getMacAddr(getBaseContext());
//            Log.e("mytag",url);
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(browserIntent);

                url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL")
                   +"/IN.aspx?url="
                    + "/modulesSign/sign.aspx?callID=" + String.valueOf(callid)
                        +"&MACAddress=" + helper.getMacAddr(ctx);
                Log.e("mytag","url: " + url);
                break;
            default:
                //setContentView(R.layout.default);
        }
        Log.e("mytag","technicianid:" + technicianid);
        String cookieString = "CID=" + technicianid + "; path=/";
        String cookieString2 = "CtypeID=" + DatabaseHelper.getInstance(getBaseContext()).getValueByKey("CtypeID") + "; path=/";

        CookieManager.getInstance().setCookie(url, cookieString);
        CookieManager.getInstance().setCookie(url, cookieString2);


        mWebview .loadUrl(url);//"http://www.google.com");

        setContentView(mWebview );

    }

    private void showAttachmentDialog(ValueCallback<Uri> uploadMsg) {
        this.mUploadMessage = uploadMsg;

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");

        this.startActivityForResult(Intent.createChooser(i, "Choose type of attachment"), FILECHOOSER_RESULTCODE);
    }
    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }
    public void initList(){
        data2.clear();
        for (Call c : getCallsList()){
            data2.add(c);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Toast.makeText(getBaseContext(),"onRestart", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toast.makeText(getBaseContext(),"onResume", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.add:
//                //add the function to perform here
//                return (true);
            //case R.id.action_filter:
            //add the function to perform here
            // return (true);
//            case R.id.about:
//                //add the function to perform here
//                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    private List<Call> getCallsList(){
        JSONObject j = null;
        int length = 0;

        List<Call> calls = new ArrayList<Call>() ;
        try {
            calls= DatabaseHelper.getInstance(this).getCalls("");
            length = calls.size();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return calls;
    }

}
