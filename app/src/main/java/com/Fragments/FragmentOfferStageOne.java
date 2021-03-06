package com.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.MenuActivity;
import com.Activities.R;
import com.Adapters.AddContactsAdapter;
import com.CallSoap;
import com.Classes.Ccustomer;
import com.DatabaseHelper;
import com.File_;
import com.Helper;
import com.Icon_Manager;
import com.Json_;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * The fragment represents the first stage of an offer page.
 */
public class FragmentOfferStageOne extends Fragment {

    private ListView addContactsListView;
    private AddContactsAdapter addContactsAdapter;
    private View view;
    private Context context;
    private Icon_Manager iconManager;
    private AutoCompleteTextView nameAutoComplete;
    private EditText company;
    private EditText email;
    private EditText landline;
    private EditText cell;
    private EditText city;
    private EditText address;
    private EditText cfname,clname;
    private TextView customerNumber;
    private TextView erpNumber;
    String strNameAutoComplete = "";
    String cid = "";
    Map<String, Ccustomer> customers;
    Helper h ;

    public FragmentOfferStageOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_offer_stage_one, container, false);
        h= new Helper();
        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        this.context = getContext();
        this.iconManager = new Icon_Manager();

        //Set the icons.
        this.setIcons();

        //Assign all the data fields.
        this.assignDataFields();
        //Initialize the customers autocomplete feature.
        this.setNameAutoComplete();
        //Initialize the additional contacts listView.
        this.addContactsListView = (ListView) view.findViewById(R.id.offer_stage_one_additional_contacts_list);
        this.addContactsAdapter = new AddContactsAdapter(context, this.addContactsListView);
        this.addContactsListView.setAdapter(this.addContactsAdapter);

        //Set add contacts onClick listener.
        ConstraintLayout addContactsButton = (ConstraintLayout) view.findViewById(R.id.offer_stage_one_constraintLayout_add_contacts_button);
        addContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContactsButtonClick();
            }
        });

        //Set next stage button onClick listener.
        ConstraintLayout nextStageButton = (ConstraintLayout) view.findViewById(R.id.offer_stage_one_constraintLayout_save);
        final View fragmentView = this.view;
        nextStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentOfferStageTwo frag = new FragmentOfferStageTwo();

                //Gather the data from the input fields.
                Bundle bundle = gatherFieldsData(fragmentView);
                frag.setArguments(bundle);
                ft.replace(R.id.container, frag, "FragmentOfferStageTwo");
                ft.addToBackStack("FragmentOfferStageTwo");
                ft.commit();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                //Add the selected contact to the list.
                String name = data.getStringExtra("CustomerName");
                this.addContactsAdapter.addContact(name);
            }
        }
    }

    /**
     * The method that is called in the event of an add contacts button click.
     */
    private void addContactsButtonClick() {
        FragmentContactsAlertListDialog fragmentAlertListDialog = new FragmentContactsAlertListDialog();
        fragmentAlertListDialog.setTargetFragment(this, 1);
        Bundle bundle = new Bundle();
        bundle.putString("Title", "בחר איש קשר");
        fragmentAlertListDialog.setArguments(bundle);
        fragmentAlertListDialog.show(getFragmentManager(), "contacts");
    }

    /**
     * Gathers all the data from the input fields into a bundle.
     *
     * @param view view
     * @return bundle
     */
    private Bundle gatherFieldsData(View view) {
        Bundle bundle = new Bundle();
        String text;

        //Add customer name.
        text = this.nameAutoComplete.getText().toString();
        bundle.putString("CustomerName", text);

        //Add company.
        text = this.company.getText().toString();
        bundle.putString("Company", text);

        //Add email.
        text = this.email.getText().toString();
        bundle.putString("Email", text);

        //Add landline.
        text = this.landline.getText().toString();
        bundle.putString("Landline", text);

        //Add cell.
        text = this.cell.getText().toString();
        bundle.putString("Cell", text);

        //Add city.
        text = this.city.getText().toString();
        bundle.putString("City", text);

        //Add address.
        text = this.address.getText().toString();
        bundle.putString("Address", text);

        // Add customer number.
        text = this.customerNumber.getText().toString();
        bundle.putString("CustomerNumber", text);

        //Add ERP number.
        text = this.erpNumber.getText().toString();
        bundle.putString("ERP", text);

        //TODO add additional contacts list

        return bundle;
    }
    public Ccustomer getClientDetails() {
        Ccustomer c = new Ccustomer();
        if (cid.length() > 0){
            if(isNumeric(cid) == true){c.setCID(cid);}else{c.setCID("-1");}

        }else{
            c.setCID("-1");
        }


        c.setCcompany(company.getText().toString());
        c.setCemail(email.getText().toString());
        c.setCphone(landline.getText().toString());
        c.setCcell(cell.getText().toString());
        c.setCcity(city.getText().toString());
        c.setCaddress(address.getText().toString());
        c.setCcID(customerNumber.getText().toString());
        c.setCusername(erpNumber.getText().toString());


        return c;
    }
    public boolean isNumeric(String s) {
        int len = s.length();
        for (int i = 0; i < len; ++i) {
            if (!isNumeric1(String.valueOf(s.charAt(i)))) {
                return false;
            }
        }

        return true;
    }
    public boolean isNumeric1(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }
    /**
     * Assigns all the data fields to class members for future usage.
     */
    private void assignDataFields() {

        this.nameAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.offer_stage_one_name_autoCompleteTextView);
        this.company = (EditText) view.findViewById(R.id.offer_stage_one_company_editText);
        this.email = (EditText) view.findViewById(R.id.offer_stage_one_email_editText);
        this.landline = (EditText) view.findViewById(R.id.offer_stage_one_landline_editText);
        this.cell = (EditText) view.findViewById(R.id.offer_stage_one_cell_editText);
        this.city = (EditText) view.findViewById(R.id.offer_stage_one_city_editText);
        this.address = (EditText) view.findViewById(R.id.offer_stage_one_address_editText);
        this.customerNumber = (TextView) view.findViewById(R.id.offer_stage_one_customer_number_text);
        this.erpNumber = (TextView) view.findViewById(R.id.offer_stage_one_erp_number_text);
        this.cfname = (EditText) view.findViewById(R.id.offer_stage_one_fname);
        this.clname = (EditText) view.findViewById(R.id.offer_stage_one_lname);

    }

    /**
     * Gets the customers from a file on the device.
     * @return customer array
     */
    private Ccustomer[] getCustomerList() {
        Helper helper = new Helper();
        File_ f = new File_();
        //myString = f.readFromFileInternal(getContext(),"customers.txt");
        String s = f.readFromFileExternal(getContext(), "customers.txt");
        //Log.e("mytag", myString);
        JSONObject j = null;
        int length = 0;
        Ccustomer[] ccustomers;//= new Ccustomer[5];
        try {
            j = new JSONObject(s);
            JSONArray jarray = j.getJSONArray("Wz_Clients_List");
            length = jarray.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ccustomers = new Ccustomer[length];
        ccustomers = helper.getCustomersFromJson2(s);

        return ccustomers;
    }

    /**
     * User's customers hashMap getter.
     *
     * @return customers hashMap
     */
    private Map<String, Ccustomer> getCustomersDictionary() {
        Map<String, Ccustomer> customers = new HashMap<>();

        List<Ccustomer> ccustomerList = new ArrayList<Ccustomer>();
        ccustomerList = DatabaseHelper.getInstance(getContext()).getCcustomers("");
        for(Ccustomer ccustomer : ccustomerList){
            String fullName = ccustomer.getCcompany();
            customers.put(fullName, ccustomer);
        }

        return customers;
    }

    /**
     * Sets the customers names autocomplete feature.
     */
    private void setNameAutoComplete() {

        //Get users's customers in a dictionary.
         customers = getCustomersDictionary();
        final String names[] = customers.keySet().toArray(new String[customers.keySet().size()]);
        this.nameAutoComplete.setAdapter(new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, names));








        this.nameAutoComplete.setThreshold(1);
        //Setting adapter
        this.nameAutoComplete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                nameAutoComplete.setSelectAllOnFocus(true);
                return false;
            }
        });
        //Set an onItemClick listener.
        this.nameAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //When a customer is selected from the list, fill all the data fields with it's values.
                Ccustomer ccustomer = customers.get(nameAutoComplete.getText().toString());
                cid =ccustomer.getCID();
                company.setText(ccustomer.getCcompany());
                email.setText(ccustomer.getCemail());
                landline.setText(ccustomer.getCphone());
                cell.setText(ccustomer.getCcell());
                city.setText(ccustomer.getCcity());
                address.setText(ccustomer.getCaddress());
                customerNumber.setText(ccustomer.getCcID());
                erpNumber.setText(ccustomer.getCusername());
                cfname.setText(ccustomer.getCfname());
                clname.setText(ccustomer.getClname());
                //hide keyboard
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                //nameAutoComplete.setText("");
            }
        });

        //Set a text changed listener.
        this.nameAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //Clean the fields when the user is typing.
                cleanFields();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                strNameAutoComplete = nameAutoComplete.getText().toString();

                String[] response = null;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * Cleans all the data fields and sets their initial value back.
     */
    private void cleanFields(){

        company.setText("");
        email.setText("");
        landline.setText("");
        cell.setText("");
        city.setText("");
        address.setText("");
        customerNumber.setText("-1");
        erpNumber.setText("-1");
    }

    /**
     * Sets all the icons that appear in the fragment.
     */
    private void setIcons() {

        TextView image = (TextView) view.findViewById(R.id.offer_stage_one_image);
        image.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        image.setTextSize(30);

        TextView searchIcon = (TextView) view.findViewById(R.id.offer_stage_one_search_icon);
        searchIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        searchIcon.setTextSize(30);

        TextView companyIcon = (TextView) view.findViewById(R.id.offer_stage_one_company_icon);
        companyIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        companyIcon.setTextSize(30);

        TextView emailIcon = (TextView) view.findViewById(R.id.offer_stage_one_email_icon);
        emailIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        emailIcon.setTextSize(30);

        TextView cellIcon = (TextView) view.findViewById(R.id.offer_stage_one_cell_icon);
        cellIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        cellIcon.setTextSize(30);

        TextView landlineIcon = (TextView) view.findViewById(R.id.offer_stage_one_landline_icon);
        landlineIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        landlineIcon.setTextSize(30);

        TextView cityIcon = (TextView) view.findViewById(R.id.offer_stage_one_city_icon);
        cityIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        cityIcon.setTextSize(30);

        TextView addressIcon = (TextView) view.findViewById(R.id.offer_stage_one_address_icon);
        addressIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        addressIcon.setTextSize(30);

        TextView addContactsIcon = (TextView) view.findViewById(R.id.offer_stage_one_add_contacts_icon);
        addContactsIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        addContactsIcon.setTextSize(30);

        TextView saveIcon = (TextView) view.findViewById(R.id.offer_stage_one_save_icon);
        saveIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        saveIcon.setTextSize(30);
    }
    //-----------the solution--------------


}


