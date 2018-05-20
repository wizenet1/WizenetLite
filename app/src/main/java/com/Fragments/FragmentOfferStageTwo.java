package com.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.Activities.MenuActivity;
import com.Activities.R;
import com.Adapters.AddProductsAdapter;
import com.Icon_Manager;

/**
 * The fragment represents the second stage of an offer page.
 */
public class FragmentOfferStageTwo extends Fragment {

    private View view;
    private Context context;
    private Icon_Manager iconManager;
    private ListView addProductsListView;
    private AddProductsAdapter addProductsAdapter;
    private EditText productName;
    private EditText serialNumber;
    private EditText description;
    private EditText quantity;
    private EditText price;
    private TextView totalPrice;

    public FragmentOfferStageTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view =  inflater.inflate(R.layout.fragment_offer_stage_two, container, false);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        this.context = getContext();
        Bundle bundle = getArguments();
        this.iconManager = new Icon_Manager();

        //Set the icons.
        this.setIcons();

        //Assign data fields.
        this.assignDataFields();

        //Setting products list.
        this.addProductsListView = (ListView)view.findViewById(R.id.offer_stage_two_add_products_list);
        this.addProductsAdapter = new AddProductsAdapter(this.context, this.addProductsListView, this);
        this.addProductsListView.setAdapter(this.addProductsAdapter);

        //Add product button onClickListener.
        ConstraintLayout addProductButton = (ConstraintLayout) view.findViewById(R.id.offer_stage_two_ConstraintLayout_add_product);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductButtonClick();
            }
        });

        //Delete button onClickListener.
        ConstraintLayout deleteButton = (ConstraintLayout) view.findViewById(R.id.offer_stage_two_ConstraintLayout_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //TODO implement
            }
        });

        //Next stage onClickListener.
        ConstraintLayout nextStageButton = (ConstraintLayout) view.findViewById(R.id.offer_stage_two_constraintLayout_save);
        nextStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentOfferStageThree frag = new FragmentOfferStageThree();
                ft.replace(R.id.container, frag, "FragmentOfferStageThree");
                ft.addToBackStack("FragmentOfferStageThree");
                ft.commit();
            }
        });

        return view;
    }

    /**
     * Updates the total price of the selected products.
     */
    public void updateTotalPrice(){
        double price = this.addProductsAdapter.getTotalProductsPrice();
        this.totalPrice.setText(String.format("%.2f", price));
    }

    /**
     * Assigns all the data fields to class members for future usage.
     */
    private void assignDataFields(){
        this.productName = (EditText)view.findViewById(R.id.offer_stage_two_name_editText);
        this.serialNumber = (EditText)view.findViewById(R.id.offer_stage_two_serial_number_editText);
        this.description = (EditText)view.findViewById(R.id.offer_stage_two_description_editText);
        this.quantity = (EditText)view.findViewById(R.id.offer_stage_two_quantity_editText);
        this.price = (EditText)view.findViewById(R.id.offer_stage_two_price_editText);
        this.totalPrice = (TextView)view.findViewById(R.id.offer_stage_two_products_total_text);
    }

    /**
     * The method is called in the event of an add product button click.
     */
    private void addProductButtonClick(){
        //TODO create an actual product object
        String product[] = new String[3];

        //Get product name.
        product[0] = this.productName.getText().toString();

        //Get products amount.
        product[1] = this.quantity.getText().toString();

        //Get product price.
        product[2] = this.price.getText().toString();

        //Add the product to the listView.
        this.addProductsAdapter.addProduct(product);

        //Clean all the input fields.
        this.cleanDataFields();

        //Update total price.
        this.updateTotalPrice();
    }

    /**
     * Cleans the text from all the data fields.
     */
    private void cleanDataFields(){

        this.productName.setText("");
        this.serialNumber.setText("");
        this.description.setText("");
        this.quantity.setText("");
        this.price.setText("");
        this.totalPrice.setText("0");
    }

    /**
     * Sets all the icons that appear in the fragment.
     */
    private void setIcons(){
        TextView image = (TextView) view.findViewById(R.id.offer_stage_two_image);
        image.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        image.setTextSize(30);

        TextView searchIcon = (TextView) view.findViewById(R.id.offer_stage_two_search_icon);
        searchIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        searchIcon.setTextSize(30);

        TextView serialNumberIcon = (TextView) view.findViewById(R.id.offer_stage_two_serial_number_icon);
        serialNumberIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        serialNumberIcon.setTextSize(30);

        TextView descriptionIcon = (TextView) view.findViewById(R.id.offer_stage_two_description_icon);
        descriptionIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        descriptionIcon.setTextSize(30);

        TextView quantityIcon = (TextView) view.findViewById(R.id.offer_stage_two_quantity_icon);
        quantityIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        quantityIcon.setTextSize(30);

        TextView priceIcon = (TextView) view.findViewById(R.id.offer_stage_two_price_icon);
        priceIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        priceIcon.setTextSize(25);

        TextView addProductIcon = (TextView) view.findViewById(R.id.offer_stage_two_add_product_icon);
        addProductIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        addProductIcon.setTextSize(30);

        TextView deleteIcon = (TextView) view.findViewById(R.id.offer_stage_two_delete_icon);
        deleteIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        deleteIcon.setTextSize(30);

        TextView saveIcon = (TextView) view.findViewById(R.id.offer_stage_two_save_icon);
        saveIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        saveIcon.setTextSize(30);
    }
}
