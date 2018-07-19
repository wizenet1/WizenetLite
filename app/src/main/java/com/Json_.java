package com;

import android.content.Context;

import com.Classes.Ctype;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by WIZE02 on 16/07/2018.
 */

public class Json_ {
    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
    JSONArray jarray = null;

    /**
     * example [{bla},{bla}]
     * @return
     */
    public JSONArray getJSONArrayFromFile(String filenameWithSuffix,Context ctx){
        JSONArray jarray = null;
        String strJson = "";
        File_ f = new File_();
        Helper h = new Helper();
        strJson = f.readFromFileExternal(ctx,filenameWithSuffix);
        if (!isJSONValid(strJson)){
            return jarray;
        }
        try {
            jarray =  new JSONArray(strJson);
        } catch (JSONException e) {
            h.LogPrintExStackTrace(e);
        }
        return jarray;
    }

}
