package com.Fragments;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.R;
import com.DatabaseHelper;
import com.Helper;
import com.Icon_Manager;


import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 20/09/2016.
 */



public class FragmentWebView extends android.support.v4.app.Fragment  {
    TextView tv_username;
    CheckBox cb;
    Button btn2,mapid;
    EditText myEditText2;
    //BarCodeActivity barCodeActivity;
    Helper helper;

    DatabaseHelper db;
    Icon_Manager icon_manager;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        context = this.getContext();
        View v = inflater.inflate(R.layout.fragment_webview, null);
        db = DatabaseHelper.getInstance(getActivity().getApplicationContext());
       // icon_manager = new Icon_Manager();
        helper = new Helper();


//        WebView  mWebView = (WebView) v.findViewById(R.id.webview);
//        mWebView.loadUrl("https://google.com");
//
//        // Enable Javascript
//        WebSettings webSettings = mWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//
//        // Force links and redirects to open in the WebView instead of in a browser
//        mWebView.setWebViewClient(new WebViewClient());

//        WebView wv = new WebView(this.getApplicationContext());
//        wv.scrollTo(1, 0);
//        wv.scrollTo(0, 0);
//        String url = DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL")
//                + "/iframe.aspx?control=modulesServices%2fCallParts&CallID=" + callid + "&type=customer&val=" + String.valueOf(call.getCID()) + "";
//        //+ "/iframe.aspx?control=/modulesServices/CallsFiles&CallID=" + callid + "&class=CallsFiles_appCell&mobile=True";
//
//        wv.loadUrl(url);
//        wv.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);





        return v;
    }




//    public String getMacAddress() {
//        WifiManager manager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = manager.getConnectionInfo();
//        String address = info.getMacAddress();
//        return address;
    //}
}
