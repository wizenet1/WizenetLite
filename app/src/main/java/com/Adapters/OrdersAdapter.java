package com.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.DatabaseHelper;
import com.File_;
import com.Helper;
import com.Classes.Order;
import com.Activities.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.Activities.MainActivity.ctx;


public class OrdersAdapter extends ArrayAdapter<String> {

    Context context;
    int resource, textViewResourceId;
    List<Order>   suggestions;
    List<Order> items,tempItems;
    Helper helper;
    String client_cid; //to know ALL or CLIENT and decide which table to get
    public OrdersAdapter(Context context, int resource, int textViewResourceId, List<Order> items,String allORclient) {
        super(context, resource, textViewResourceId);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Order>(items); // this makes the difference.
        suggestions = new ArrayList<Order>();
        this.client_cid = allORclient;
        helper = new Helper();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.search_auto_complete, parent, false);
        }
        try{
            //String pos = getItem(position);
            Order names = items.get(position+1);
            if (getItem(position) != null) {
                TextView lblName = (TextView) view.findViewById(R.id.name_search);
                if (lblName != null)
                    lblName.setText(getItem(position));

                //lblName.setText(items.get(pos).getPname());
            }

        }catch(Exception e){
            Log.e("MYTAG",e.getMessage());
        }
        return view;
    }


    @Override
    public int getCount() {
        return suggestions == null ? 0 : suggestions.size();
    }


    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((String) resultValue);
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            try{
                if (constraint != null ) {
                    if (client_cid == "0"){
                       // Toast.makeText(context, "עליך לבחור חברה תחילה", Toast.LENGTH_LONG).show();
                    }

                    suggestions.clear();
                    List<Order> orderList = new ArrayList<Order>();
                    if (client_cid == "all"){
                        orderList = DatabaseHelper.getInstance(context).get_mgnet_items_by_Pname(constraint.toString(),client_cid);
                    }else{
                        orderList = helper.getClientOrderList(client_cid,context);
                    }

                    for (Order names : orderList) {
                        if ((names.getPmakat().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                names.getPname().toLowerCase().contains(constraint.toString().toLowerCase()))
                                && constraint.toString().length() >=3){
                            suggestions.add(names);
                        }
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                } else {
                    //suggestions.add(po)
                    return new FilterResults();
                }
            }catch(Exception e){
                Log.e("MYTAG",e.getMessage());
            }
            return new FilterResults();
        }






        protected float roundOff(String x, int position)
        {
            float f = Float.parseFloat(x);
            float a = f;
            double temp = Math.pow(10.0, position);
            a *= temp;
            a = Math.round(a);
            return (a / (float)temp);
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try{
            List<Order> filterList = (ArrayList<Order>) results.values;
            if (results != null && getCount() > 0) {
                clear();
                try{
                    for (Order names : filterList) {
                        add(names.getPname()+ "|" + names.getPmakat() + "~" + roundOff(names.getPprice(),2)+"*"+roundOff(names.getPoprice(),2));
                        //add(names.getPmakat());

                        notifyDataSetChanged();
                    }
                }catch(Exception e){
                    Log.e("MYTAG",e.getMessage());
                }

            }
        }catch(Exception e){
            Log.e("MYTAG",e.getMessage());
        }}
    };
}