package com.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Activities.R;
import com.Icon_Manager;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCreateCustomer extends Fragment {


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

        TextView backArrow = (TextView)view.findViewById(R.id.create_customer_arrow);
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

        TextView customerProfileIcon = (TextView)view.findViewById(R.id.create_customer_profile_image);
        customerProfileIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerProfileIcon.setTextSize(30);

        TextView customerNameIcon = (TextView)view.findViewById(R.id.create_customer_name_icon);
        customerNameIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerNameIcon.setTextSize(30);

        TextView customerCompanyIcon = (TextView)view.findViewById(R.id.create_customer_company_icon);
        customerCompanyIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerCompanyIcon.setTextSize(30);

        TextView customerPositionIcon = (TextView)view.findViewById(R.id.create_customer_position_icon);
        customerPositionIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerPositionIcon.setTextSize(30);

        TextView customerCellIcon = (TextView)view.findViewById(R.id.create_customer_cell_icon);
        customerCellIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerCellIcon.setTextSize(30);

        TextView customerLandlineIcon = (TextView)view.findViewById(R.id.create_customer_landline_icon);
        customerLandlineIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerLandlineIcon.setTextSize(30);

        TextView customerAddressIcon = (TextView)view.findViewById(R.id.create_customer_address_icon);
        customerAddressIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerAddressIcon.setTextSize(30);

        TextView customerEmailIcon = (TextView)view.findViewById(R.id.create_customer_email_icon);
        customerEmailIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerEmailIcon.setTextSize(30);

        return view;
    }

}
