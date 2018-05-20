package com.Adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.Activities.R;
import com.Fragments.FragmentOfferStageOne;
import com.Fragments.FragmentOfferStageTwo;
import com.Icon_Manager;

import java.util.ArrayList;

/**
 * Created by Danny on 20/05/2018.
 */

public class AddProductsAdapter extends BaseAdapter {
    private Context context;
    private ListView listView;
    private FragmentOfferStageTwo fragment;
    private LayoutInflater layoutInflater;
    //TODO need to change the list from string to Product object.
    private ArrayList<String[]> productsList;
    private Icon_Manager iconManager;

    public AddProductsAdapter(Context context, ListView listView, FragmentOfferStageTwo fragment){

        this.context = context;
        this.listView = listView;
        this.fragment = fragment;
        this.layoutInflater = LayoutInflater.from(context);
        this.productsList = new ArrayList<>();
        this.iconManager = new Icon_Manager();
    }

    @Override
    public int getCount() {
        return this.productsList.size();
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

        view = this.layoutInflater.inflate(R.layout.add_products_row, viewGroup, false);

        //Set delete icon.
        TextView deleteIcon = (TextView)view.findViewById(R.id.add_products_row_delete_icon);
        deleteIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        deleteIcon.setTextSize(20);

        //Set delete icon onClick listener.
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productsList.remove(i);
                notifyDataSetChanged();
                setListViewHeightBasedOnChildren(listView);
            }
        });

        //Set name.
        TextView name = (TextView) view.findViewById(R.id.add_products_row_name);
        name.setText(this.productsList.get(i)[0]);

        //Set amount.
        TextView amount = (TextView) view.findViewById(R.id.add_products_row_amount);
        amount.setText(this.productsList.get(i)[1]);

        //Set update product button onClick listener.
        ConstraintLayout updateButton = (ConstraintLayout)view.findViewById(R.id.add_products_row_ConstraintLayout_update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.updateProduct(productsList.get(i));
            }
        });

        return view;
    }

    public void addProduct(String product[]){
        this.productsList.add(product);
        this.notifyDataSetChanged();
        this.setListViewHeightBasedOnChildren(this.listView);
    }

    public double getTotalProductsPrice(){
        double sum = 0.0;

        for (String product[] : this.productsList){
            int quantity = Integer.parseInt(product[1]);
            double price = Double.parseDouble(product[2]);

            sum += price * quantity;
        }

        return sum;
    }

    public int getTotalProductsQuantity(){

        int sum = 0;

        for (String product[] : this.productsList){
            sum += Integer.parseInt(product[1]);
        }

        return sum;
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {

            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

        this.fragment.updateTotalPurchase();
    }
}
