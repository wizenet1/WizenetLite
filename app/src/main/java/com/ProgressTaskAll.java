package com;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.Classes.Message;

import java.util.List;

/**
 * Created by WIZE02 on 16/08/2017.
 */

public class ProgressTaskAll extends AsyncTask<String, String, String> {
    private ProgressDialog dialog;
    List<Message> titles;
    private Context context;
Helper helper;
    //private List<Message> messages;
    public ProgressTaskAll(Context context) {
        this.context = context;
        dialog = new ProgressDialog(context);
        helper= new Helper();
    }
    @Override
    protected void onPreExecute() {
        //this.working_dialog = ProgressDialog.show(getContext(), "","Working please wait...", true);

        this.dialog.setMessage("Working please wait...");
        this.dialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, 5000);
    }
    @Override
    protected void onPostExecute(final String success) {
    }
    @Override
    protected String doInBackground(final String... args) {
        helper.ALLProductsSync(context);
        return "";
    }
}