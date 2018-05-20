package com.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.Activities.MenuActivity;
import com.Activities.R;
import com.Adapters.AddContactsAdapter;
import com.Icon_Manager;

/**
 * The fragment represents the first stage of an offer page.
 */
public class FragmentOfferStageOne extends Fragment {

    private ListView addContactsListView;
    private AddContactsAdapter addContactsAdapter;

    public FragmentOfferStageOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer_stage_one, container, false);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        Context context = getContext();
        Icon_Manager iconManager = new Icon_Manager();

        //Set the icons.
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

        this.addContactsListView = (ListView)view.findViewById(R.id.offer_stage_one_additional_contacts_list);
        this.addContactsAdapter = new AddContactsAdapter(context);
        this.addContactsListView.setAdapter(this.addContactsAdapter);

        //Set add contacts onClick listener.
        ConstraintLayout addContactsButton = (ConstraintLayout) view.findViewById(R.id.offer_stage_one_constraintLayout_add_contacts_button);
        addContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContactsButtonClick();
            }
        });

        ConstraintLayout nextStageButton = (ConstraintLayout) view.findViewById(R.id.offer_stage_one_constraintLayout_save);
        final View fragmentView = view;
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
        if(requestCode == 1){
            if(resultCode == 1){
                String name = data.getStringExtra("CustomerName");
                this.addContactsAdapter.addContact(name);
            }
        }
    }

    /**
     * The method that is called in the event of an add contacts button click.
     */
    private void addContactsButtonClick(){
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
        EditText editText;
        TextView textView;
        String text;

        //Add customer name.
        editText = (EditText) view.findViewById(R.id.offer_stage_one_name_editText);
        text = editText.getText().toString();
        bundle.putString("CustomerName", text);

        //Add company.
        editText = (EditText) view.findViewById(R.id.offer_stage_one_company_editText);
        text = editText.getText().toString();
        bundle.putString("Company", text);

        //Add email.
        editText = (EditText) view.findViewById(R.id.offer_stage_one_email_editText);
        text = editText.getText().toString();
        bundle.putString("Email", text);

        //Add landline.
        editText = (EditText) view.findViewById(R.id.offer_stage_one_landline_editText);
        text = editText.getText().toString();
        bundle.putString("Landline", text);

        //Add cell.
        editText = (EditText) view.findViewById(R.id.offer_stage_one_cell_editText);
        text = editText.getText().toString();
        bundle.putString("Cell", text);

        //Add city.
        editText = (EditText) view.findViewById(R.id.offer_stage_one_city_editText);
        text = editText.getText().toString();
        bundle.putString("City", text);

        //Add address.
        editText = (EditText) view.findViewById(R.id.offer_stage_one_address_editText);
        text = editText.getText().toString();
        bundle.putString("Address", text);

        // Add customer number.
        textView = (TextView) view.findViewById(R.id.offer_stage_one_customer_number_text);
        text = textView.getText().toString();
        bundle.putString("CustomerNumber", text);

        //Add ERP number.
        textView = (TextView) view.findViewById(R.id.offer_stage_one_erp_number_text);
        text = textView.getText().toString();
        bundle.putString("ERP", text);

        //TODO add additional contacts list

        return bundle;
    }

}
