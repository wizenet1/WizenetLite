package com.nearestCustomers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.Activities.R;
import com.Classes.Ccustomer;

import java.util.ArrayList;

/**
 * Adapter for the nearest customer listView.
 */
public class DistancesListAdapterNew extends BaseAdapter implements Filterable {

    private Context context;
    private LayoutInflater layoutInflater;

    //Customers list.
    private ArrayList<Ccustomer> customers;

    /**
     * Constructor.
     * @param context context
     * @param customers customers list
     */
    public DistancesListAdapterNew(Context context, ArrayList<Ccustomer> customers) {
        try{
            this.layoutInflater = LayoutInflater.from(context);
        }catch (Exception e){
            //getActivity().getSupportFragmentManager().popBackStack();
        }
        this.context = context;
        this.customers = customers;
    }

    /**
     * Customers list setter.
     * @param customers customers list
     */
    public void setCustomers(ArrayList<Ccustomer> customers){

        this.customers = customers;
    }

    @Override
    public int getCount() {
        return this.customers.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null) {

            view = this.layoutInflater.inflate(R.layout.customers_distances_row, viewGroup, false);
        }

        //For each row in the list, add the following items.
        TextView customerName = (TextView) view.findViewById(R.id.customers_distances_row_name);
        TextView distance = (TextView) view.findViewById(R.id.customers_distances_row_distance);
        TextView city = (TextView) view.findViewById(R.id.customers_distances_row_city);
        TextView address = (TextView) view.findViewById(R.id.customers_distances_row_address);

        //Get current customer.
        Ccustomer currentCustomer = this.customers.get(i);

        //Set customer values into the row.
        customerName.setText(currentCustomer.getCcompany());
        city.setText(currentCustomer.getCcity());
        address.setText(currentCustomer.getCaddress());
        distance.setText(currentCustomer.getDistance());

        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //if (results.values != null){
                    customers = (ArrayList<Ccustomer>) results.values;
                    notifyDataSetChanged();
                //}

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {




                FilterResults results = new FilterResults();
                ArrayList<Ccustomer> FilteredArrayNames = new ArrayList<>();
                results.values = customers;
                results.count = customers.size();



                //Convert given distance in km to meters.
                double maxDistance = Double.parseDouble(constraint.toString()) ;//* 1000;

                //Filter the customers with distance higher than the maximum.
                for (int i = 0; i < customers.size(); i++) {
                    Ccustomer customer = customers.get(i);
                    //Log.e("myTag", "Filter results: " + results.values.toString());
                    //Log.e("myTag", "Filter results: " + "distance: "+(Double.valueOf(customer.getDistance())) + " max distance: "+ (maxDistance));
                    if (Double.valueOf(customer.getDistance()) <= maxDistance)  {
                        FilteredArrayNames.add(customer);
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                //Log.i("myTag", "Filter results: " + results.values.toString());

                return results;
            }
        };

        return filter;
    }
}
