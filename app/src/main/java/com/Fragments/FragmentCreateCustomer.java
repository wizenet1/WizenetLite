package com.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.Activities.R;
import com.Icon_Manager;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCreateCustomer extends Fragment {

    private Bundle customerArgumentsBundle;

    public FragmentCreateCustomer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_customer, container, false);
        Context context = getContext();
        Icon_Manager iconManager = new Icon_Manager();

        try {
            //Try getting customer data from a Bundle.
            this.customerArgumentsBundle = getArguments();

            //If there were passed customer arguments, change to an editing layout.
            changeLayoutToEditCustomer(view);

        } catch (Exception e) {
        }

        TextView backArrow = (TextView) view.findViewById(R.id.create_customer_arrow);
        backArrow.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        backArrow.setTextSize(30);
        //Setting back arrow click listener.
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                } else {
                    android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                    FragmentCustomer frag = new FragmentCustomer();
                    ft.replace(R.id.container, frag, "FragmentCustomer");
                    ft.addToBackStack("FragmentCustomer");
                    ft.commit();
                }
            }
        });

        //Customer profile.
        TextView customerProfileIcon = (TextView) view.findViewById(R.id.create_customer_profile_image);
        customerProfileIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerProfileIcon.setTextSize(30);

        //Customer name
        TextView customerNameIcon = (TextView) view.findViewById(R.id.create_customer_name_icon);
        customerNameIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerNameIcon.setTextSize(30);

        //Customer company
        TextView customerCompanyIcon = (TextView) view.findViewById(R.id.create_customer_company_icon);
        customerCompanyIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerCompanyIcon.setTextSize(30);

        //Customer job position.
        TextView customerPositionIcon = (TextView) view.findViewById(R.id.create_customer_position_icon);
        customerPositionIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerPositionIcon.setTextSize(30);

        //Customer cellphone.
        TextView customerCellIcon = (TextView) view.findViewById(R.id.create_customer_cell_icon);
        customerCellIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerCellIcon.setTextSize(30);

        //Customer landline.
        TextView customerLandlineIcon = (TextView) view.findViewById(R.id.create_customer_landline_icon);
        customerLandlineIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerLandlineIcon.setTextSize(30);

        //Customer address.
        TextView customerAddressIcon = (TextView) view.findViewById(R.id.create_customer_address_icon);
        customerAddressIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerAddressIcon.setTextSize(30);

        //Customer email.
        TextView customerEmailIcon = (TextView) view.findViewById(R.id.create_customer_email_icon);
        customerEmailIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerEmailIcon.setTextSize(30);

        //Add contacts button.
        TextView addContactsIcon = (TextView) view.findViewById(R.id.create_customer_add_contacts_icon);
        addContactsIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        addContactsIcon.setTextSize(30);

        //Save customer button.
        TextView saveCustomerIcon = (TextView) view.findViewById(R.id.create_customer_save_icon);
        saveCustomerIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        saveCustomerIcon.setTextSize(30);

        //Animation applied to menu icons to create click effect.
        final Animation clickAnimation = AnimationUtils.loadAnimation(context, R.anim.view_click_alpha);

        //Add contacts button.
        final ConstraintLayout addContactsLayout = (ConstraintLayout) view.findViewById(R.id
                .create_customer_constraintLayout_add_contacts);

        addContactsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContactsLayout.startAnimation(clickAnimation);
                //TODO implement functionality
            }
        });

        //Save customer button.
        final ConstraintLayout saveLayout = (ConstraintLayout) view.findViewById(R.id
                .create_customer_constraintLayout_save);

        saveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLayout.startAnimation(clickAnimation);
                //TODO implement functionality
            }
        });

        return view;
    }

    private void changeLayoutToEditCustomer(View view) {

        String CID = this.customerArgumentsBundle.getString("CID");
        String firstName = this.customerArgumentsBundle.getString("FirstName");
        String lastName = this.customerArgumentsBundle.getString("LastName");
        String company = this.customerArgumentsBundle.getString("Company");
        String cell = this.customerArgumentsBundle.getString("Cell");
        String landline = this.customerArgumentsBundle.getString("Phone");
        String address = this.customerArgumentsBundle.getString("Address");
        String email = this.customerArgumentsBundle.getString("Email");

        //Change the welcome sign.
        TextView welcomeTxt = (TextView) view.findViewById(R.id.create_customer_welcome_txt);
        welcomeTxt.setText("עדכון לקוח");

        //TODO add an actual profile image.

        //Fill the input fields with the existing customer data.
        TextView customerNameEditText = (TextView) view.findViewById(R.id.create_customer_name_editText);
        customerNameEditText.setText(lastName + " " + firstName);//TODO should it be divided to two textviews?

        TextView customerCompanyEditText = (TextView) view.findViewById(R.id.create_customer_company_editText);
        customerCompanyEditText.setText(company);

        TextView customerPositionEditText = (TextView) view.findViewById(R.id.create_customer_position_editText);
        customerPositionEditText.setText("");//TODO delete if position is not needed

        TextView customerCellEditText = (TextView) view.findViewById(R.id.create_customer_cell_editText);
        customerCellEditText.setText(cell);

        TextView customerLandlineEditText = (TextView) view.findViewById(R.id.create_customer_landline_editText);
        customerLandlineEditText.setText(landline);

        TextView customerAddressEditText = (TextView) view.findViewById(R.id.create_customer_address_editText);
        customerAddressEditText.setText(address);

        TextView customerEmailEditText = (TextView) view.findViewById(R.id.create_customer_email_editText);
        customerEmailEditText.setText(email);
    }

}
