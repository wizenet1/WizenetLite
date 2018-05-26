package com;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_WORLD_READABLE;

public class File_ {
    public File_(){}

    public File retSubDir(Context c,String Subdirectory){
        File file;
        file = new File(c.getFilesDir().getAbsolutePath() + File.separator + "/wizenet/" + Subdirectory + "/");
        if(!(file.exists())) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    public File retSubDirExternal(Context c,String Subdirectory){
        File file;
        file = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/");
        //file = new File(c.getFilesDir().getAbsolutePath() + File.separator + "/wizenet/" + Subdirectory + "/");
        if(!(file.exists())) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    public boolean isSubDirectoryExist(String subDirectory){
        File file;
        boolean flag = false;
        file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator  +"wizenet/"+subDirectory + "/");
        if(file.exists()) {
                flag = true;
        }
        return flag;
    }
    public boolean isFileExist(String fileWithPrefix){
        File file;
        boolean flag = false;
        file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator  +"wizenet/"+fileWithPrefix);
        if(file.exists()) {
            flag = true;
        }
        return flag;
    }
    public boolean createWizenetDir(Context c){
        File file;
        file = new File(c.getFilesDir().getAbsolutePath() + File.separator + "/wizenet/");
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
        File file;
        file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator  +"wizenet/"+SubName + "/");
        boolean success = true;

        if(!(file.exists())) {
            file.mkdirs();
            success = true;

        }else{
            success = false;
        }
        return success;
    }

    public boolean deleteFileInternal(Context c,String txtfile){
        File file;
        file = new File(c.getFilesDir().getAbsolutePath() + File.separator + "/wizenet/" + txtfile);
        boolean deleted = file.delete();
        return deleted;
    }
    public boolean deleteFileExternal(Context c,String txtfile){
        File file;
        //file = new File(c.getFilesDir().getAbsolutePath() + File.separator + "/wizenet/" + txtfile);
        file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator  +"wizenet/"+txtfile);
        boolean deleted = file.delete();
        return deleted;
    }

    ////----------------relevant-----------------------------
    public boolean writeTextToFileInternal(Context c, String txtfile, String str) {
        FileOutputStream fop = null;
        File file;
        String content = "";
        content = str;
        boolean a = true;
        File listnames = new File(c.getFilesDir() + File.separator + "Lists");
        if( !listnames.exists() ) {
            a =listnames.mkdirs();
            Log.e("mytag","chk a"+String.valueOf(a));
        }

        else if( !listnames.isDirectory() && listnames.canWrite() ){
            listnames.delete();
            a = listnames.mkdirs();
            Log.e("mytag","chk b"+String.valueOf(a));
        }
        else{
            //you can't access there with write permission.
            //Try other way.
        }


        try {
            file = new File(c.getFilesDir() + File.separator + "wizenet"+ File.separator + txtfile);
            file.createNewFile();
            Log.e("mytag","addr:" +c.getFilesDir().getAbsolutePath() + File.separator + "wizenet"+ File.separator + txtfile);


            // if file doesnt exists, then create it
            if (!file.exists()) {
                File fileTmp = new File(c.getFilesDir() + File.separator + "/wizenet/");
                //File file = new File(c.getFilesDir().getAbsolutePath(),"mydir");
                if(!file.exists()){
                    file.createNewFile();
                }
                Log.e("mytag","dir exist? " +String.valueOf(fileTmp.exists()));
                //file = File.createTempFile("calls",".txt",fileTmp);

            }
            //fop = new FileOutputStream(file);




            fop = c.openFileOutput("calls.txt",Context.MODE_PRIVATE);


            // get the content in bytes
            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();
            return true;
            //System.out.println("Done");

        } catch (IOException e) {
            Log.e("mytag","IOException1 write file:" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                    return false;
                }
            } catch (IOException e) {
                Log.e("mytag","IOException2 write file:" + e.getMessage());

                e.printStackTrace();
            }
        }
        return false;

    }
    public boolean writeTextToFileExternal(Context c,String txtfile,String str){


        try {
            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/"+txtfile);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.write(str);
            myOutWriter.close();
            fOut.close();
            //Toast.makeText(getApplicationContext(), "File Created", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    ////----------------relevant-----------------------------
    public String readFromFileInternal(Context c,String txtfile) {
        File file;
        file = new File(c.getFilesDir().getAbsolutePath() + File.separator + "/wizenet/" + txtfile);
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
    public String readFromFileExternal(Context c,String txtfile) {
        String ret = "";
        try {
            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+  File.separator + "wizenet/" + txtfile);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow;
            }
            ret = aBuffer.toString().trim();
            myReader.close();
            //Toast.makeText(getApplicationContext(), ret, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public String readFromCurrentFileInternal(Context c,File currFile) {
        File file = currFile;
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
    public String readFromCurrentFileExternal(Context c,File currFile) {
        File file = currFile;
        if(!file.exists()){
            //try {
                Log.e("mytag","readFromCurrentFileExternal: file is not exist");
                return "";
                //file.createNewFile();
            //} catch (IOException e) {
            //    e.printStackTrace();
            //}
        }
        String ret = "";
        try {
            //File myFile = new File(Environment.getExternalStorageDirectory().getPath()+  File.separator + "wizenet/" + txtfile);
            FileInputStream fIn = new FileInputStream(file);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow;
            }
            ret = aBuffer.toString().trim();
            myReader.close();
            //Toast.makeText(getApplicationContext(), ret, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public String readFromFileWithSubDirectory(Context c,String subDirectory,String txtfile){
        String ret = "";
        ret = readFromFileExternal(c,subDirectory + "/" + txtfile);
        return ret;
    }
    public boolean writeTextToFileWithSubDirectory(Context c,String subDirectory,String txtfile,String str){
        Boolean flag =null;
        createSubDirectory(c,subDirectory);
        flag =writeTextToFileExternal(c,"/"+subDirectory+"/"+txtfile,str);

        //flag =writeTextToFileInternal(c,"/"+subDirectory+"/"+txtfile,str);
        return flag;
    }

}
