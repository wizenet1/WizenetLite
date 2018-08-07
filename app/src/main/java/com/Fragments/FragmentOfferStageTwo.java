package com.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.MenuActivity;
import com.Activities.R;
import com.Adapters.AddProductsAdapter;
import com.Classes.Ccustomer;
import com.Classes.Product;
import com.DatabaseHelper;
import com.File_;
import com.Helper;
import com.Icon_Manager;
import com.Json_;
import com.ProgressTaskAll;
import com.model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The fragment represents the second stage of an offer page.
 */
public class FragmentOfferStageTwo extends Fragment {

    private View view;
    private Context context;
    private Icon_Manager iconManager;
    private ListView addProductsListView;
    private AddProductsAdapter addProductsAdapter;
    private AutoCompleteTextView productAutoComplete;
    private EditText serialNumber;
    private EditText description;
    private EditText quantity;
    private EditText price;
    private TextView totalPrice;
    private TextView totalQuantity;
    private boolean isEditable;
    String PID,Pmakat;
    File_ f = new File_();
    public FragmentOfferStageTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_offer_stage_two, container, false);

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

        //Initialize the products autocomplete feature.
        this.setProductAutoComplete();
        Helper h = new Helper();

        if (h.isNetworkAvailable(getContext()) && (!f.isFileExist("products.txt")==true)){
            Model.getInstance().Async_Wz_retProducts(h.getMacAddr(context), new Model.Wz_retProducts_Listener() {
                @Override
                public void onResult(String str) {
                    Toast.makeText(getContext(),str, Toast.LENGTH_SHORT).show();
                }
            });
        }

        //Setting products list.
        this.addProductsListView = (ListView) view.findViewById(R.id.offer_stage_two_add_products_list);
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
    public void updateTotalPurchase() {
        double price = this.addProductsAdapter.getTotalProductsPrice();
        int quantity = this.addProductsAdapter.getTotalProductsQuantity();

        this.totalPrice.setText(String.format("%.2f", price));
        this.totalQuantity.setText(String.format("%d", quantity));
    }

    /**
     * Fills the input fields with the given product's data to update it.
     *
     * @param product product
     */
    public void updateProduct(Product product) {
        //TODO extract all the product data and fill all the input fields
        String name = product.getPname();
        String quantity = product.getPstock();
        String price = product.getPprice();

        this.productAutoComplete.setText(name);
        this.quantity.setText(quantity);
        this.price.setText(price);
    }
    public ArrayList<Product> getProductsList(){
        return this.addProductsAdapter.getProductsList();
    }

    /**
     * Assigns all the data fields to class members for future usage.
     */
    private void assignDataFields() {

        this.productAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.offer_stage_two_name_autoCompleteTextView);
        this.serialNumber = (EditText) view.findViewById(R.id.offer_stage_two_serial_number_editText);
        this.description = (EditText) view.findViewById(R.id.offer_stage_two_description_editText);
        this.quantity = (EditText) view.findViewById(R.id.offer_stage_two_quantity_editText);
        this.price = (EditText) view.findViewById(R.id.offer_stage_two_price_editText);
        this.totalPrice = (TextView) view.findViewById(R.id.offer_stage_two_products_total_price_text);
        this.totalQuantity = (TextView) view.findViewById(R.id.offer_stage_two_products_total_quantity_text);
    }

    /**
     * The method is called in the event of an add product button click.
     */
    private void addProductButtonClick() {
        //TODO create an actual product object
        String product[] = new String[3];
        Product p = new Product();
        p.setPname(productAutoComplete.getText().toString());
        p.setPstock(quantity.getText().toString());
        p.setPprice(price.getText().toString());
        p.setPID(PID);
        p.setPmakat(Pmakat);

        //Get product name.
        //product[0] = this.productAutoComplete.getText().toString();
        //Get products amount.
        //product[1] = this.quantity.getText().toString();
        //Get product price.
        //product[2] = this.price.getText().toString();


        //Add the product to the listView.
        this.addProductsAdapter.addProduct(p);
        //Clean all the input fields.
        this.cleanDataFields();
        //Update total price.
        this.updateTotalPurchase();
    }

    /**
     * Cleans the text from all the data fields.
     */
    private void cleanDataFields() {

        //this.productAutoComplete.setText("");
        this.serialNumber.setText("");
        this.description.setText("");
        this.quantity.setText("");
        this.price.setText("");
        //TODO maybe make a separate method for cleaning the total price
        //this.totalPrice.setText("0");
    }

    /**
     * User's products hashMap getter.
     *
     * @return customers hashMap
     */
    private Map<String, Product> getProductsDictionary() {
        Json_ j = new Json_();
        Map<String, Product> customers = new HashMap<>();

        List<Product> productslist = new ArrayList<Product>();
        productslist =  j.getProductsByFile(getContext());//DatabaseHelper.getInstance(getContext()).getCcustomers("");
        for(Product prod : productslist){
            String fullName = prod.getPname();
            customers.put(fullName, prod);
        }

        return customers;
    }

    /**
     * Sets the products names autocomplete feature.
     */
    private void setProductAutoComplete() {
        //Get users's products.
        final Map<String, Product> products = getProductsDictionary();
        //customers = getCustomersDictionary();
        //customers = getCustomersDictionary();
        //final String names[] = customers.keySet().toArray(new String[customers.keySet().size()]);
        //Extract the names of the products for the autocomplete.
        final String names[] = products.keySet().toArray(new String[products.keySet().size()]);

        //Initialize the textViewAutoComplete adapter.
        this.productAutoComplete.setAdapter(new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, names));

        this.isEditable = true;

        //Set an onItemClick listener.
        this.productAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //When a product is selected from the list, fill all the data fields with it's values.
                //TODO get all the values from a real product object
                Product p = products.get(productAutoComplete.getText().toString());
                Toast.makeText(getContext(), p.toString(), Toast.LENGTH_SHORT).show();
                serialNumber.setText(p.getPmakat());
                description.setText(p.getPname());
                quantity.setText(p.getPstock());
                price.setText(p.getPprice());
                PID = p.getPID();
                Pmakat = p.getPmakat();
                //Disable editing the data fields.
                setFieldsIsEditable(false);
                isEditable = false;
            }
        });

        //Set a text changed listener.
        this.productAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //Clean the data fields.
                cleanDataFields();

                //Check if editing is disabled.
                if (!isEditable){

                    //Enable editing the data fields.
                    setFieldsIsEditable(true);
                    isEditable = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * Sets the is editing enabled for the input fields.
     * @param isEditable is editable
     */
    private void setFieldsIsEditable(boolean isEditable){
        this.serialNumber.setEnabled(isEditable);
        this.description.setEnabled(isEditable);
    }

    /**
     * Sets all the icons that appear in the fragment.
     */
    private void setIcons() {
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
