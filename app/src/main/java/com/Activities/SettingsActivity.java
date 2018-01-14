package com.Activities;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.DatabaseHelper;

public class SettingsActivity extends AppCompatActivity {

    CheckBox cb;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = new DatabaseHelper(this);

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        cb = (CheckBox) findViewById(R.id.checkBox);
        if(true)//db.getControlPanel(1).getGps_track().equals("1"))
        {
            cb.setChecked(true);
        }else{
            cb.setChecked(false);
        }
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb.isChecked()){
                    //after click is checked and GPS isn't available
                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        //GPS available
                    }else{

                        //MenuActivity.isTrack = true;

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("MESSAGE", "1");
                        setResult(RESULT_OK, resultIntent);
                        finish();
                        //Toast.makeText(getBaseContext(), "start tracking", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getBaseContext(), "cb is checked", Toast.LENGTH_SHORT).show();
                }else{
                    //MenuActivity.isTrack = false;

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("MESSAGE", "0");
                    setResult(RESULT_OK, resultIntent);
                    finish();

                    //Toast.makeText(getBaseContext(), "start tracking", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(!MenuActivity.isTrack){
//            cb.setChecked(false);
//        }else{
//            cb.setChecked(true);
//        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
