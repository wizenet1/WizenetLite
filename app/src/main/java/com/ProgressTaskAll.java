package com;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.Classes.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static com.Activities.MainActivity.ctx;

/**
 * Created by WIZE02 on 16/08/2017.
 */

public class ProgressTaskAll extends AsyncTask<String, String, String> {
    private ProgressDialog pDialog;
    List<Message> titles;
    private Context context;

    public ProgressTaskAll(Context context) {
        this.context = context;
        pDialog = new ProgressDialog(context);
    }

    /**
     * Before starting background thread
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Starting download");

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading... Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            Log.e("mytag: ","s1:");

            String root = Environment.getExternalStorageDirectory().getPath()+  File.separator + "wizenet/";

            System.out.println("Downloading");
            URL url = new URL(DatabaseHelper.getInstance(context).getValueByKey("URL")+"/data/products/productss.txt");

            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            File_ f = new File_();
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/wizenet/productss.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream output = new FileOutputStream(root+"/productss.txt");
            byte data[] = new byte[1024];

            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();


            Log.e("mytag: ","s2:");
            DatabaseHelper db = DatabaseHelper.getInstance(ctx);

            db.delete_from_mgnet_items("all");

                String strJson = "";
                strJson += "{Orders:";
                strJson += f.readFromFileExternal(ctx, "productss.txt");
                strJson += "}";

                Log.e("mytag", "*strJson*: " + strJson);
                //strJson = strJson.replace("PRODUCTS_ITEMS_LISTResponse", "");
                //strJson = strJson.replace("PRODUCTS_ITEMS_LISTResult=", "Orders:");
                JSONObject j = null;
                JSONArray jarray = null;
                j = null;
                jarray = null;
                try {
                    j = new JSONObject(strJson);
                    jarray = j.getJSONArray("Orders");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jarray.length(); i++) {
                    final JSONObject e;
                    try {
                        e = jarray.getJSONObject(i);
                        db.add_mgnet_items(
                                e.getString("Pname"),
                                e.getString("Pmakat"),
                                e.getString("Pprice"),
                                e.getString("Poprice"), "all");

                    } catch (JSONException e1) {
                        e1.printStackTrace();

                    }
                }





        } catch (Exception e) {
            Log.e("mytag: ","s:" + e.getMessage());
        }

        return null;
    }



    /**
     * After completing background task
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        System.out.println("Downloaded");

        pDialog.dismiss();
    }
}

