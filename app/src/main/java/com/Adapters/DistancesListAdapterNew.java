package com.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.ActivityWebView;
import com.Activities.R;
import com.Classes.Ccustomer;
import com.DatabaseHelper;
import com.Icon_Manager;

import java.util.ArrayList;

/**
 * Adapter for the nearest customer listView.
 */
public class DistancesListAdapterNew extends BaseAdapter implements Filterable {
    private Icon_Manager icon_manager;
    private Context context;
    private LayoutInflater layoutInflater;
    private TextView tv_gps,tv_link;
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
        icon_manager = new Icon_Manager();
        if (view == null) {

            view = this.layoutInflater.inflate(R.layout.customers_distances_row, viewGroup, false);
        }

        //For each row in the list, add the following items.
        TextView customerName = (TextView) view.findViewById(R.id.customers_distances_row_name);
        TextView distance = (TextView) view.findViewById(R.id.customers_distances_row_distance);
        TextView city = (TextView) view.findViewById(R.id.customers_distances_row_city);
        TextView address = (TextView) view.findViewById(R.id.customers_distances_row_address);
        tv_gps =(TextView) view.findViewById(R.id.tv_gps);
        tv_link =(TextView) view.findViewById(R.id.tv_link);
        tv_gps.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",context));
        tv_link.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",context));

        tv_gps.setTextSize(30);
        tv_link.setTextSize(30);
        //goToSms.setTag(pos);
        //Get current customer.
        Ccustomer currentCustomer = this.customers.get(i);

        //Set customer values into the row.
        customerName.setText(currentCustomer.getCcompany());
        city.setText(currentCustomer.getCcity());
        address.setText(currentCustomer.getCaddress());
        distance.setText(currentCustomer.getDistance());
        tv_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToWaze(i);
            }
        });
        tv_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOptions(context,i);
            }
        });


        return view;
    }
    private void goToOptions(Context c,int pos){
        try
        {
            Intent intent = new Intent(c, ActivityWebView.class);
            Bundle b = new Bundle();
            b.putInt("callid", -1);
            b.putInt("cid", -1);
            b.putInt("technicianid", Integer.parseInt(String.valueOf(DatabaseHelper.getInstance(c).getValueByKey("CID"))));
            b.putString("action", "dynamic");
            b.putString("specialurl", "/mobile/clientoptions.aspx?CID=" + customers.get(pos).getCID() + "");
            intent.putExtras(b);
            c.startActivity(intent);
        }
        catch ( ActivityNotFoundException ex  )
        {
            Toast.makeText(c, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
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
    public void goToWaze(int pos){
        try
        {
            // Launch Waze to look for Hawaii:
            String url = "waze://?q=" + customers.get(pos).getCaddress() + " " + customers.get(pos).getCcity();
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity( intent );
        }
        catch ( ActivityNotFoundException ex  )
        {
            // If Waze is not installed, open it in Google Play:
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
            context.startActivity(intent);
        }
    }
}
