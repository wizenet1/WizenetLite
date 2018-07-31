package com.Fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.R;
import com.Classes.Ccustomer;
import com.DatabaseHelper;
import com.File_;
import com.GPSTracker;
import com.Helper;
import com.Json_;
import com.google.android.gms.maps.model.LatLng;
import com.model.Model;
import com.nearestCustomers.CustomerTmp;
import com.nearestCustomers.DistanceComparator;
import com.nearestCustomers.DistancesListAdapter;
import com.nearestCustomers.DistancesListAdapterNew;
import com.nearestCustomers.IObservable;
import com.nearestCustomers.IObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The fragment of the nearest customers page.
 */
public class FragmentNearestCustomersNew extends Fragment {

    private ListView distancesListView;
    //private DistancesListAdapter adapter;
    private DistancesListAdapterNew adapter;
    private SeekBar seekBar;
    private TextView distanceText;
    private Helper h;
    private Button btnGPS, btnImport;
    GPSTracker gps = null;
    LatLng origin = null;
    //Customers list.
    //private ArrayList<CustomerTmp> customers;
    private ArrayList<Ccustomer> customers;

    //Counter used in the handler to count the number of tasks that were finished.
    private static int distancesCalculatedCounter;

    //Initial seek bar cursor value.
    private final int INITIAL_PROGRESS = 10;

    //API key used to address the Google Maps server.
    //doron key
    private final String API_KEY = "AIzaSyDpObMzkQazFmlpvv_YvSUUOW9PiQMsWFA";

    public FragmentNearestCustomersNew() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_nearest_customers, container, false);
        this.h = new Helper();
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);
        FragmentManager fm = getFragmentManager();
        int backStackCount = getFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            // Get the back stack fragment id.
            int backStackId = getFragmentManager().getBackStackEntryAt(i).getId();
            String tag = getFragmentManager().getBackStackEntryAt(i).getName();
            Log.e("mytag", "fragName:" + tag);
//            if (tag.equals("FragmentMenu") || tag.indexOf("f2,f3,f4,f5") > -1) {
//            } else {
//                fm.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            }

        }
        this.btnImport = (Button) view.findViewById(R.id.btnImport);
        this.btnGPS = (Button) view.findViewById(R.id.btnGPS);
        this.seekBar = (SeekBar) view.findViewById(R.id.customers_distances_seekBar);
        this.distancesListView = (ListView) view.findViewById(R.id.customers_distances_listView);
        this.distanceText = (TextView) view.findViewById(R.id.customers_distances_distanceText);

        //Sets the initial position of the seek bar cursor.
        this.seekBar.setProgress(INITIAL_PROGRESS);

        //Initializes the customers list.



        gps = new GPSTracker(getContext());
        origin = new LatLng(gps.getLatitude(), gps.getLongitude());
        if (origin.longitude == 0.0) {
            Toast.makeText(getContext(), "true", Toast.LENGTH_SHORT).show();
            origin = new LatLng(32.091412, 34.895811);
        }
        Toast.makeText(getContext(), "lat:" + gps.getLatitude() + " long:" + gps.getLongitude(), Toast.LENGTH_SHORT).show();
        //LatLng origin = new LatLng(32.091412, 34.895811);

        //Calculate distances from the user to the customers.
        initializeCustomers();
        boolean hasSucceeded = calculateDistancesToUser(origin);
        Collections.sort(customers, new CcustomerComparator());

        if (!hasSucceeded) {
            Toast.makeText(getContext(), "Failed to create all asyncTasks", Toast.LENGTH_SHORT).show();
        }
        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                origin = new LatLng(gps.getLatitude(), gps.getLongitude());
                Toast.makeText(getContext(), "lat:" + gps.getLatitude() + " long:" + gps.getLongitude(), Toast.LENGTH_SHORT).show();
            }
        });
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().Async_Wz_ret_ClientsAddressesByActions_Listener(h.getMacAddr(getContext()), "", new Model.Wz_ret_ClientsAddressesByActions_Listener() {
                    @Override
                    public void onResult(String str) {
                        Toast.makeText(getContext(), str, Toast.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "יובא בהצלחה", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        adapter = new DistancesListAdapterNew(getContext(), customers);
        distancesListView.setAdapter(adapter);

        //Set the current filtered distance textView.
        this.distanceText.setText(this.seekBar.getProgress() + " km");

        //Perform filtering.


        adapter.getFilter().filter(Integer.toString(INITIAL_PROGRESS));
        adapter.setCustomers(customers);
        //Seek bar changes listener.
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progressValue;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                progressValue = i;
                distanceText.setText(progressValue + " km");
                adapter.getFilter().filter(Integer.toString(progressValue));
                adapter.setCustomers(customers);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                distanceText.setText(progressValue + " km");
                // adapter.getFilter().filter(Integer.toString(progressValue));
            }
        });


        return view;
    }
    public class CcustomerComparator implements Comparator<Ccustomer>
    {
        @Override
        public int compare(Ccustomer o1, Ccustomer o2) {
            return  Double.valueOf(o1.getDistance()).compareTo(Double.valueOf(o2.getDistance()));
        }
    }
    /**
     * Calculates the distances between the user and all of his customers.
     *
     * @param origin users position
     * @return has succeeded
     */
    private boolean calculateDistancesToUser(LatLng origin) {

        for (Ccustomer customer : this.customers) {
            try {
                Log.e("mytag","customer.getLatitude(): " + customer.getLatitude()+ "  customer.getLongtitude(): "+ customer.getLongtitude());
                float a = distance(
                        Float.valueOf(String.valueOf(origin.latitude)),
                        Float.valueOf(String.valueOf(origin.longitude)),
                        Float.valueOf(String.valueOf(customer.getLatitude())),
                        Float.valueOf(String.valueOf(customer.getLongtitude())));
                String dist =String.valueOf(round(Double.valueOf(a) / 1000, 2));
                DatabaseHelper.getInstance(getContext()).updateSpecificValueInTable2("Ccustomers","CID","'"+customer.getCID()+"'","Distance","'"+ dist +"'");
                customer.setDistance(dist);
                //Perform distance calculation in a separate thread.
                Log.e("mytag", "-----------------------------");
                Log.e("mytag", "customer: " + customer.getCcompany() + "-- address:" + customer.getCaddress() + " " + customer.getCcity());
                Log.e("mytag", "origin:" + String.valueOf(origin.latitude) + ":" + String.valueOf(origin.longitude));
                Log.e("mytag", "dest:" + customer.getLatitude() + ":" + customer.getLongtitude());
                //Log.e("mytag", "in metric:" + String.valueOf(a) + " in kilmoeters:" + (dist);

            } catch (Exception e) {
                customer.setDistance("0");
                Helper h = new Helper();
                h.LogPrintExStackTrace(e);//e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    public float distance(float lat_a, float lng_a, float lat_b, float lng_b) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b - lat_a);
        double lngDiff = Math.toRadians(lng_b - lng_a);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }

    public boolean isDouble(String num) {
        String decimalPattern = "([0-9]*)\\.([0-9]*)";
        String number = num;
        return Pattern.matches(decimalPattern, number);
    }

    /**
     * Initializes the customers list.
     */
    private void initializeCustomers() {

        this.customers = new ArrayList<>();
        List<Ccustomer> ccustomerList = new ArrayList<Ccustomer>();
        ccustomerList = DatabaseHelper.getInstance(getContext()).getCcustomers("");

        for (Ccustomer c : ccustomerList) { //String Ccompany,String Longtitude,String Latitude,String Caddress,String Ccity
            this.customers.add(new Ccustomer(Integer.valueOf(c.getCID()),c.getCcompany(), c.getLongtitude(), c.getLatitude(), c.getCaddress(), c.getCcity()));
        }
        //this.customers.add(new CustomerTmp("לקוח 4", "אילת", "התמרים 1"));
        //this.customers.add(new CustomerTmp("משה כהן", "פתח תקווה", "חיים עוזר 1"));
        //this.customers.add(new CustomerTmp("לקוח 2", "תל אביב", "דיזינגוף 5"));
        //this.customers.add(new CustomerTmp("לקוח 5", "אילת", "התמרים 10"));
        //this.customers.add(new CustomerTmp("לקוח 1", "תל אביב", "אלנבי 1"));
        //this.customers.add(new CustomerTmp("לקוח 3", "נתניה", "הרצל 1"));
        //this.customers.add(new CustomerTmp("אבי כהן", "פתח תקווה", "הרצל 8"));

        //Set the needed request counter.
        distancesCalculatedCounter = this.customers.size();
    }

    //private Ccustomer[] getCustomerList() {
    //    Helper helper = new Helper();
    //    Json_ j_ = new Json_();
//
    //    File_ f = new File_();
    //    Ccustomer[] ccustomers = new Ccustomer[1];
    //    try {
    //        List<Ccustomer> ccustomerList = new ArrayList<Ccustomer>();
    //        ccustomerList = DatabaseHelper.getInstance(getContext()).getCcustomers(""); //helper.getWizenetClientsFromJsonForLocations(getContext());
    //        if (o != null) {
    //            Ccustomer[] c = (Ccustomer[]) o;
    //            return c;
    //        }
//
    //    } catch (Exception e) {
    //        h.LogPrintExStackTrace(e);
    //        Ccustomer c = new Ccustomer("", "", "", "", "");
    //        ccustomers = new Ccustomer[]{c};
    //    }
//
    //    return ccustomers;
    //}



}
