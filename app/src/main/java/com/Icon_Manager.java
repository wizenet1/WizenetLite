package com;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by WIZE02 on 03/05/2017.
 */

public class Icon_Manager {
    private  static Hashtable<String,Typeface> cashed_icons = new Hashtable<>();
    public static Typeface get_Icons(String path , Context context){
        Typeface icons = cashed_icons.get(path);
        if (icons == null){
            icons = Typeface.createFromAsset(context.getAssets(),path);
            cashed_icons.put(path,icons);

        }
        return  icons;
    }
}
