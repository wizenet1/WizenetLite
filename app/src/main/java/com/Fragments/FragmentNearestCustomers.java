package com.Fragments;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Classes.Ccustomer;
import com.File_;
import com.GPSTracker;
import com.Helper;
import com.Json_;
import com.google.android.gms.maps.model.LatLng;

import com.Activities.R;
import com.model.Model;
import com.nearestCustomers.CustomerTmp;
import com.nearestCustomers.DistanceComparator;
import com.nearestCustomers.DistancesListAdapter;
import com.nearestCustomers.GetDistanceHttp;
import com.nearestCustomers.IObservable;
import com.nearestCustomers.IObserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * The fragment of the nearest customers page.
 */
public class FragmentNearestCustomers extends Fragment implements IObserver {

    private ListView distancesListView;
    private DistancesListAdapter adapter;
    private SeekBar seekBar;
    private TextView distanceText;
    private Helper h;
    private Button btnGPS,btnImport;
    GPSTracker gps = null;
    LatLng origin = null;
    //Customers list.
    //private ArrayList<CustomerTmp> customers;
    private ArrayList<CustomerTmp> customers;

    //Counter used in the handler to count the number of tasks that were finished.
    private static int distancesCalculatedCounter;

    //Initial seek bar cursor value.
    private final int INITIAL_PROGRESS = 10;

    //API key used to address the Google Maps server.
    //doron key
    private final String API_KEY = "AIzaSyDpObMzkQazFmlpvv_YvSUUOW9PiQMsWFA";

    public FragmentNearestCustomers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fragment_nearest_customers, container, false);
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
        this.btnImport = (Button)  view.findViewById(R.id.btnImport);
        this.btnGPS = (Button)  view.findViewById(R.id.btnGPS);
        this.seekBar = (SeekBar) view.findViewById(R.id.customers_distances_seekBar);
        this.distancesListView = (ListView) view.findViewById(R.id.customers_distances_listView);
        this.distanceText = (TextView) view.findViewById(R.id.customers_distances_distanceText);

        //Sets the initial position of the seek bar cursor.
        this.seekBar.setProgress(INITIAL_PROGRESS);

        //Initializes the customers list.

        initializeCustomers();

        //TODO The location values of the user, should be supplied from outside.
        gps = new GPSTracker(getContext());
        origin = new LatLng(gps.getLatitude(),gps.getLongitude());
        if (origin.longitude == 0.0){
            Toast.makeText(getContext(),"true", Toast.LENGTH_SHORT).show();
            origin = new LatLng(32.091412, 34.895811);
        }
        Toast.makeText(getContext(),"lat:"+gps.getLatitude()+" long:"+gps.getLongitude(), Toast.LENGTH_SHORT).show();
        //LatLng origin = new LatLng(32.091412, 34.895811);

        //Calculate distances from the user to the customers.
        boolean hasSucceeded = calculateDistancesToUser(origin);

        if (!hasSucceeded) {
            Toast.makeText(getContext(), "Failed to create all asyncTasks", Toast.LENGTH_SHORT).show();
        }
        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                origin = new LatLng(gps.getLatitude(),gps.getLongitude());
                Toast.makeText(getContext(),"lat:"+gps.getLatitude()+" long:"+gps.getLongitude(), Toast.LENGTH_SHORT).show();
            }
        });
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().Async_Wz_ret_ClientsAddressesByActions_Listener(h.getMacAddr(getContext()), "", new Model.Wz_ret_ClientsAddressesByActions_Listener() {
                    @Override
                    public void onResult(String str) {
                        Toast.makeText(getContext(),str, Toast.LENGTH_LONG).show();
                        Toast.makeText(getContext(),"יובא בהצלחה", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


            adapter = new DistancesListAdapter(getContext(), customers);
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

    /**
     * Calculates the distances between the user and all of his customers.
     * @param origin users position
     * @return has succeeded
     */
    private boolean calculateDistancesToUser(LatLng origin) {

        for (CustomerTmp customer : this.customers) {

            //String destination = "long:"+customer.getLatitude()+"|lat:"+customer.getCity();
            //origin

            try {
                //String encodedDestination = URLEncoder.encode(destination, "UTF-8");
                // //(float lat_a, float lng_a, float lat_b, float lng_b )
                float a=distance(
                        Float.valueOf(String.valueOf(origin.latitude)),
                        Float.valueOf(String.valueOf(origin.longitude)),
                        Float.valueOf(String.valueOf(customer.getLatitude())),
                        Float.valueOf(String.valueOf(customer.getLongtitude())));
                //float a = distanceFrom_in_Km(String.valueOf(origin.latitude),customer.getLatitude(),String.valueOf(origin.longitude),customer.getLongtitude(),"1");
                //The url to the Google Maps server with the user's and customer's locations.
                //String strUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                //        origin.latitude + "," + origin.longitude +
                //        "&destination=" + encodedDestination +
                //        "&sensor=false&units=metric&mode=driving&key=" + API_KEY;
                customer.setDistanceToUserValue(round(Double.valueOf(a)/1000, 2));
                //Perform distance calculation in a separate thread.
                Log.e("mytag","-----------------------------");
                Log.e("mytag","customer: " + customer.getName()+ "-- address:"+ customer.getAddress()+ " " + customer.getCity());
                Log.e("mytag","origin:" + String.valueOf(origin.latitude) +":"+String.valueOf(origin.longitude));
                Log.e("mytag","dest:" + customer.getLatitude() +":"+customer.getLongtitude());
                Log.e("mytag","in metric:" + String.valueOf(a)+ " in kilmoeters:" + (round(Double.valueOf(a)/1000, 2)));
                //new GetDistanceHttp(new GeocoderHandler(this, customer)).execute(String.valueOf((round(Double.valueOf(a)/1000, 2))));

            } catch (Exception e) {
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



    public float distance (float lat_a, float lng_a, float lat_b, float lng_b )
    {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }

    public boolean isDouble(String num){
        String decimalPattern = "([0-9]*)\\.([0-9]*)";
        String number=num;
        return Pattern.matches(decimalPattern, number);
    }

    /**
     * Initializes the customers list.
     */
    private void initializeCustomers() {

        this.customers = new ArrayList<>();
        //this.customers = getCustomerList();String name, String city, String address,String longtitude,String latitude
        for (Ccustomer c:getCustomerList()) {
            this.customers.add(new CustomerTmp(c.getCcompany(), c.getCcity(),c.getCaddress(),c.getLongtitude(),c.getLatitude()));
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
    private Ccustomer[] getCustomerList() {
        Helper helper = new Helper();
        Json_ j_ = new Json_();

        File_ f = new File_();
        Ccustomer[] ccustomers = new Ccustomer[1];
        //try{
        //    Object o = helper.getWizenetClientsFromJsonForLocations(getContext());
        //    if (o != null){
        //        Ccustomer[] c = (Ccustomer[]) o;
        //        return c;
        //    }
//
        //}catch(Exception e){
        //    Ccustomer c = new Ccustomer("","","","","");
        //    ccustomers =new Ccustomer[] {c};
        //}

        return ccustomers;
    }

    @Override
    public void update() {
        try{
            //Sort the customers by distance.
            Collections.sort(this.customers, new DistanceComparator());

            //Setting the listView adapter.
            adapter = new DistancesListAdapter(getContext(), customers);
            distancesListView.setAdapter(adapter);



            //Set the current filtered distance textView.
            this.distanceText.setText(this.seekBar.getProgress() + " km");

            //Perform filtering.
            if (customers != null){
                adapter.getFilter().filter(Integer.toString(INITIAL_PROGRESS));
            }


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
        }catch(Exception e){
            h.LogPrintExStackTrace(e);
        }


    }

    /**
     * Handles the responses from the threads that calculate the distances.
     */
    private class GeocoderHandler extends Handler implements IObservable {

        private CustomerTmp customer;
        private IObserver observer;

        /**
         * Constructor.
         * @param observer observer
         * @param customer customer
         */
        public GeocoderHandler(IObserver observer, CustomerTmp customer) {

            this.observer = observer;
            this.customer = customer;
        }

        @Override
        public void handleMessage(Message message) {

            //Distance in readable form.
            String distanceText = null;

            //Distance in meters.
            double distanceValue = 0.0;

            //Check the message status code.
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    distanceText = bundle.getString("Text");
                    distanceValue = bundle.getDouble("Value");
                    break;

                case 2:
                    distanceText = null;
                    break;

                default:
                    distanceText = null;
            }

            //Check if there is a valid answer.
            if (distanceText == null) {
                this.customer.setDistanceToUserText("שגיאה");

            } else {
                this.customer.setDistanceToUserText(distanceText);
                this.customer.setDistanceToUserValue(distanceValue);
            }

            //Decrease the tasks counter
            distancesCalculatedCounter--;

            //Check if all the tasks are done.
            if (distancesCalculatedCounter == 0) {

                //Notify the observers that all the tasks are done.
                this.notifyObservers();
            }
        }

        @Override
        public void notifyObservers() {
            observer.update();
        }
    }
}
