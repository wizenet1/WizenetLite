package com.Activities;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class SplashActivity extends Activity {
Helper helper;
    Context ctx;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        helper = new Helper();
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        String s = "http://wizenet.co.il/data/android.jpg" ;
        /**
         * async download the image to present it
         */
        try{
            new DownloadImageTask((ImageView) findViewById(R.id.imageView3))
                    .execute(s);
        }catch(Exception ex){
            Toast.makeText(getApplicationContext(),ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        //here after 5 secs we moves to main/login activity
        //and here i use handler/run to implement it
        final Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        if (helper.isNetworkAvailable(ctx)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }, 5000);
        }else{
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setScaleType(ImageView.ScaleType.FIT_XY);
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("myTag","splash stop/finish");
    }

}
