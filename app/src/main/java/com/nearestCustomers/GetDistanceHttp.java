package com.nearestCustomers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The class creates a thread to execute an HTTP request to the Google Maps server,
 * asking for the distance between the user and a given customer
 */
public class GetDistanceHttp extends AsyncTask<String , Void ,String> {

    //Server response.
    private String serverResponse;

    //Handler that will handle the response.
    private Handler handler;

    /**
     * Constructor.
     * @param handler handler
     */
    public GetDistanceHttp(Handler handler){
        this.handler = handler;
    }

    @Override
    protected String doInBackground(String... strings) {

        URL url;
        HttpURLConnection urlConnection = null;
        serverResponse =strings[0];
        //Perform an HTTP request.
        //try {
        //    url = new URL(strings[0]);
        //    urlConnection = (HttpURLConnection) url.openConnection();
//
        //    int responseCode = urlConnection.getResponseCode();
//
        //    if (responseCode == HttpURLConnection.HTTP_OK) {
        //        serverResponse = readStream(urlConnection.getInputStream());
        //    }
        //    else{
        //        Log.e("myTag", "Http error, status code" + responseCode);
        //        serverResponse = null;
        //    }
//
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}

        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        callHandler();
    }

    /**
     * Calls the handler to handle the response from the server.
     */
    private void callHandler(){

        /*
        Contains the message code:
        message.what = 1 : means everything is ok.
        message.what = 2: means an error has occurred.
        If what = 1, also contains the bundle with the distance.
         */
        Message message = Message.obtain();
        message.setTarget(handler);

        //Setting the message to ok for now.
        message.what = 1;
        JSONObject distanceJson = null;

        //Extract data from returned Json.
        try{
            //JSONObject jsonObject = new JSONObject(serverResponse.toString());
            //JSONArray array = jsonObject.getJSONArray("routes");
            //JSONObject routes = array.getJSONObject(0);
            //JSONArray legs = routes.getJSONArray("legs");
            //JSONObject steps = legs.getJSONObject(0);
            //distanceJson = steps.getJSONObject("distance");

            //Distance in a readable form(m, km)
            String distanceText = serverResponse;//"hello";//distanceJson.getString("text");

            //Distance in meters.
            double distanceValue = Double.parseDouble(serverResponse);//Double.parseDouble(distanceJson.getString("value"));

            //Create the bundle which will contain the result.
            Bundle bundle = new Bundle();
            bundle.putString("Text", distanceText);
            bundle.putDouble("Value", distanceValue);
            Log.e("myTag", "distanceText:" + distanceText + "  distanceValue: "+distanceValue);
            message.setData(bundle);
        }
        catch(Exception e){

            e.printStackTrace();

            Log.e("myTag", "Server response error " + serverResponse);

            //Setting that an error has occurred.
            message.what = 2;
        }

        //Send the message to the handler.
        message.sendToTarget();
    }

    /**
     * Reads the response to the HTTP request.
     * @param in input stream
     * @return HTTP response
     */
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
