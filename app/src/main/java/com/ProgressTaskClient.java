package com;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.Classes.Message;
import com.model.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WIZE02 on 16/08/2017.
 */

public class ProgressTaskClient extends AsyncTask<String, Integer, String> {
    private ProgressDialog dialog;
    List<Message> titles;
    private Context context;
Helper helper;
    //private List<Message> messages;
    public ProgressTaskClient(Context context) {
        this.context = context;
        //dialog = new ProgressDialog(context);
        helper= new Helper();
    }
    @Override
    protected void onPreExecute() {
        //this.working_dialog = ProgressDialog.show(getContext(), "","Working please wait...", true);

        //this.dialog.setMessage("Working please wait...");
        //this.dialog.show();

    }
    @Override
    protected void onPostExecute(final String success) {
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //progressbar.set
    }

    @Override
    protected String doInBackground(final String... args) {
        final File_ f = new File_();
        Log.e("MYTAG","hello");
        List<String> listCIDS = new ArrayList<String>();
        listCIDS=helper.getCIDSlist();
        String rr = "";
        int i=0;
        for (final String c: listCIDS) {
            final String cc = c;
            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/client_products/"+"pl_" + cc +".txt");
            if(myFile.exists())
                myFile.delete();
                Model.getInstance().Async_Get_mgnet_client_items_Listener(helper.getMacAddr(),c, new Model.get_mgnet_client_items_Listener() {
                    @Override
                    public void onResult(String str) {
                        Log.e("MYTAG","pl_" + cc +".txt ---" +str);
                        f.writeTextToFileWithSubDirectory(context,"client_products","pl_" + cc +".txt", str);
                        //helper.writeTextToClientDirectory("client_products","pl_" + cc +".txt", str);
                    };
                    ;});
                i++;
                //publishProgress(i);



        };
        Log.e("MYTAG","FINISHED!!");

        //helper.ALLProductsSync(context);
        return "";
    }
}