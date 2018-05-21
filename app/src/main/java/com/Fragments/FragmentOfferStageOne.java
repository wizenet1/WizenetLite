package com.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.Icon_Manager;

import java.util.HashMap;
import java.util.Map;

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
    private TextView customerNumber;
    private TextView erpNumber;


    public FragmentOfferStageOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_offer_stage_one, container, false);

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
    }

    /**
     * User's customers hashMap getter.
     *
     * @return customers hashMap
     */
    private Map<String, String[]> getCustomersDictionary() {
        //TODO get a real customers list.
        Map<String, String[]> customers = new HashMap<>();
        customers.put("fff", new String[]{"אבי כהן", "אדידס"});
        customers.put("fgg", new String[]{"משה כהן", "נייק"});

        return customers;
    }

    /**
     * Sets the customers names autocomplete feature.
     */
    private void setNameAutoComplete() {

        //Get users's customers.
        final Map<String, String[]> customers = getCustomersDictionary();

        //Extract the names of the customers for the autocomplete.
        final String names[] = customers.keySet().toArray(new String[customers.keySet().size()]);

        //Initialize the textViewAutoComplete adapter.
        this.nameAutoComplete.setAdapter(new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, names));

        //Set an onItemClick listener.
        this.nameAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //When a customer is selected from the list, fill all the data fields with it's values.
                //TODO get all the values from a real customer object
                String companyName = customers.get(nameAutoComplete.getText().toString())[1];
                company.setText(companyName);
            }
        });
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

}
