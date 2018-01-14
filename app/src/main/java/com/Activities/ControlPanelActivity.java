package com.Activities;

import android.content.Context;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.Fragments.ControlPanelFragment;

public class ControlPanelActivity extends FragmentActivity {
    //final public LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
    //final LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel_control_activity);


        goToCPFragment();
    }
    public void goToCPFragment(){
        Bundle bundle=new Bundle();
        //bundle.putString("name", txt);
        //mydb.getInstance(getApplicationContext());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ControlPanelFragment frag = new ControlPanelFragment();

        //frag.setArguments(bundle);
        ft.add(R.id.container, frag, "Fragment");

        ft.commit();
    }
}
