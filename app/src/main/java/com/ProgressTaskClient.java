package com;
import com.Classes.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.Classes.Message;
import com.model.Model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ProgressTaskClient extends AsyncTask<String, Integer, String> {
    private ProgressDialog pDialog;
    List<Message> titles;
    private Context context;
    private Helper helper;

    public ProgressTaskClient(Context context) {
        this.context = context;
        pDialog = new ProgressDialog(context);
        helper = new Helper();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //System.out.println("Starting download");

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading... Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    @Override
    protected void onPostExecute(final String success) {
        pDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //super.onProgressUpdate(values);
        //progressbar.set
    }

    @Override
    protected String doInBackground(final String... args) {

        //Log.e("mytag","step 1");
        String strCustomers = helper.getCusernamelist();
        String [] Customers = strCustomers.split(",");
        String myCID = DatabaseHelper.getInstance(context).getValueByKey("CID");
        //
        File_ f = new File_();
        f.createSubDirectory(context,"client_products");
        //Log.e("mytag","step 3");
        for (String c:Customers) {
            //Log.e("mytag","step 1"+ c);
            String ret = "";
            ret =writeFile(myCID,c);
            if (ret == "-1"){
                Log.e("mytag","step error");
//                Toast.makeText(context, "error sync", Toast.LENGTH_SHORT).show();
                //break;
            }
        }

        return "";
    }

    private String writeFile(String myCID,String customerCID){
        String ret = "";
        int count;
        try {
            //Log.e("mytag: ","s1:");
            String root = Environment.getExternalStorageDirectory().getPath()+  File.separator + "wizenet/";
            //System.out.println("Downloading");
            URL url = new URL(DatabaseHelper.getInstance(context).getValueByKey("URL")+"/data/products/" + myCID + "/pl_" + customerCID + ".txt");
            int response = -1;
            URLConnection conection = url.openConnection();
            conection.connect();
            HttpURLConnection httpConn = (HttpURLConnection) conection;


            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {

                // getting file length
                //int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                File_ f = new File_();
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/wizenet/client_products/pl_" + customerCID + ".txt");
                if (!file.exists()) {
                    file.createNewFile();
                }
                OutputStream output = new FileOutputStream(root+"/client_products/pl_" + customerCID + ".txt");
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

            } else { // this is new
                //Log.e("mytag","collapse");
                return "-1";
                //Log.e("mytag",helper.LogPrintExStackTrace(response));
                //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                //throw new HTTPException( response );

            }

            return "";
        } catch (FileNotFoundException e) {
            helper.LogPrintExStackTrace(e);
            e.printStackTrace();
            return  "-1";
        } catch (MalformedURLException e) {
            helper.LogPrintExStackTrace(e);
            e.printStackTrace();
            return "-1";
        } catch (IOException e) {
            helper.LogPrintExStackTrace(e);
            e.printStackTrace();
            return "-1";
        }
        //return ret;
    }
}