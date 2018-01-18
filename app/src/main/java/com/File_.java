package com;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by WIZE02 on 18/01/2018.
 */

public class File_ {
    public File_(){}

    public File retSubDir(Context c,String Subdirectory){
        File file;
        file = new File(c.getFilesDir().getAbsolutePath() + java.io.File.separator + "/wizenet/" + Subdirectory + "/");
        if(!(file.exists())) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public boolean createWizenetDir(Context c){
        File file;
        file = new File(c.getFilesDir().getAbsolutePath() + java.io.File.separator + "/wizenet/");
        boolean success = true;
        if(!(file.exists())) {
            try {
                file.createNewFile();
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            success = false;
        }


        return success;
    }
    public boolean createSubDirectory(Context c,String SubName){
        java.io.File file;
        file = new java.io.File(c.getFilesDir().getAbsolutePath() + java.io.File.separator + "/" + SubName + "/");
        boolean success = true;

        if(!(file.exists())) {
            try {
                file.createNewFile();
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            success = false;
        }
        return success;
    }

    public boolean deleteFile(Context c,String txtfile){
        java.io.File file;
        file = new java.io.File(c.getFilesDir().getAbsolutePath() + java.io.File.separator + "/wizenet/" + txtfile);
        boolean deleted = file.delete();
        return deleted;
    }
    ////----------------relevant-----------------------------
    public boolean writeTextToFileInternal(Context c, String txtfile, String str) {
        FileOutputStream fop = null;
        java.io.File file;
        String content = "";
        content = str;
        try {
            file = new java.io.File(c.getFilesDir().getAbsolutePath() + java.io.File.separator + "/wizenet/" + txtfile);
            fop = new FileOutputStream(file);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get the content in bytes
            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();
            return true;
            //System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;

    }
    ////----------------relevant-----------------------------
    public String readFromFileInternal(Context c,String txtfile) {
        java.io.File file;
        file = new java.io.File(c.getFilesDir().getAbsolutePath() + java.io.File.separator + "/wizenet/" + txtfile);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StringBuffer retBuf = new StringBuffer();

        try {
            if (fileInputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String lineData = bufferedReader.readLine();
                while (lineData != null) {
                    retBuf.append(lineData);
                    lineData = bufferedReader.readLine();
                }
            }
        }catch(IOException ex)
        {
            Log.e("mytag", ex.getMessage(), ex);
        }finally
        {
            return retBuf.toString();
        }
    }

    public boolean writeTextToFileWithSubDirectory(Context c,String subDirectory,String txtfile,String str){
        Boolean flag =null;
        createSubDirectory(c,subDirectory);
        flag =writeTextToFileInternal(c,"/"+subDirectory+"/"+txtfile,str);
        return flag;
    }

}
