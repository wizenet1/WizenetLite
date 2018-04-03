package com.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import com.Activities.R;
import com.nearestCustomers.CustomerTmp;
import com.nearestCustomers.DistanceComparator;
import com.nearestCustomers.DistancesListAdapter;
import com.nearestCustomers.GetDistanceHttp;
import com.nearestCustomers.IObservable;
import com.nearestCustomers.IObserver;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNearestCustomers extends Fragment implements IObserver {

    private ListView distancesListView;
    private DistancesListAdapter adapter;
    private SeekBar seekBar;
    private TextView distanceText;

    //Customers list.
    private ArrayList<CustomerTmp> customers;

    //Counter used in the handler to count the number of tasks that were finished.
    private static int distancesCalculatedCounter;

    //Initial seek bar cursor value.
    private final int INITIAL_PROGRESS = 10;

    //API key used to address the Google Maps server.
    //TODO add your key here
    private final String API_KEY = "AIzaSyDpObMzkQazFmlpvv_YvSUUOW9PiQMsWFA";

    public FragmentNearestCustomers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fragment_nearest_customers, container, false);

        this.seekBar = (SeekBar) view.findViewById(R.id.customers_distances_seekBar);
        this.distancesListView = (ListView) view.findViewById(R.id.customers_distances_listView);
        this.distanceText = (TextView) view.findViewById(R.id.customers_distances_distanceText);

        //Sets the initial position of the seek bar cursor.
        this.seekBar.setProgress(INITIAL_PROGRESS);

        //Initializes the customers list.
        initializeCustomers();

        //TODO The location values of the user, should be supplied from outside.
        LatLng origin = new LatLng(32.091412, 34.895811);

        //Calculate distances from the user to the customers.
        boolean hasSucceeded = calculateDistancesToUser(origin);

        if (!hasSucceeded) {
            Toast.makeText(getContext(), "Failed to create all asyncTasks", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    /**
     * Calculates the distances between the user and all of his customers.
     * @param origin users position
     * @return has succeeded
     */
    private boolean calculateDistancesToUser(LatLng origin) {

        for (CustomerTmp customer : this.customers) {

            String destination = customer.getAddress() + customer.getCity();

            try {
                String encodedDestination = URLEncoder.encode(destination, "UTF-8");

                //The url to the Google Maps server with the user's and customer's locations.
                String strUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                        origin.latitude + "," + origin.longitude +
                        "&destination=" + encodedDestination +
                        "&sensor=false&units=metric&mode=driving&key=" + API_KEY;

                //Perform distance calculation in a separate thread.
                new GetDistanceHttp(new GeocoderHandler(this, customer)).execute(strUrl);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

                return false;
            }
        }

        return true;
    }

    /**
     * Initializes the customers list.
     */
    private void initializeCustomers() {

        this.customers = new ArrayList<>();
        this.customers.add(new CustomerTmp("לקוח 4", "אילת", "התמרים 1"));
        this.customers.add(new CustomerTmp("משה כהן", "פתח תקווה", "חיים עוזר 1"));
        this.customers.add(new CustomerTmp("לקוח 2", "תל אביב", "דיזינגוף 5"));
        this.customers.add(new CustomerTmp("לקוח 5", "אילת", "התמרים 10"));
        this.customers.add(new CustomerTmp("לקוח 1", "תל אביב", "אלנבי 1"));
        this.customers.add(new CustomerTmp("לקוח 3", "נתניה", "הרצל 1"));
        this.customers.add(new CustomerTmp("אבי כהן", "פתח תקווה", "הרצל 8"));

        //Set the needed request counter.
        distancesCalculatedCounter = this.customers.size();
    }

    @Override
    public void update() {

        //Sort the customers by distance.
        Collections.sort(this.customers, new DistanceComparator());

        //Setting the listView adapter.
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
