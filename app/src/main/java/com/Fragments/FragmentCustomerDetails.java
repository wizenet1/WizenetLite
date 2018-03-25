package com.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.Activities.R;
import com.Adapters.ExpandableListAdapter;
import com.Classes.Ccustomer;
import com.Icon_Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCustomerDetails extends Fragment {


    private ExpandableListView expandableListViewComments;
    private ExpandableListView expandableListViewContacts;
    private ExpandableListAdapter listAdapter;
    private List<String[]> listDataHeaderComments;
    private HashMap<String, List<String>> listHashComments;
    private List<String[]> listDataHeaderContacts;
    private HashMap<String, List<String>> listHashContacts;
    private Bundle customerArgumentsBundle;


    public FragmentCustomerDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_details, container, false);

        //Get customer data from given Bundle.
        this.customerArgumentsBundle = getArguments();
        String CID = this.customerArgumentsBundle.getString("CID");
        String firstName = this.customerArgumentsBundle.getString("FirstName");
        String lastName = this.customerArgumentsBundle.getString("LastName");
        String company = this.customerArgumentsBundle.getString("Company");
        String cell = this.customerArgumentsBundle.getString("Cell");
        String phone = this.customerArgumentsBundle.getString("Phone");

        Icon_Manager iconManager = new Icon_Manager();
        final Context context = getContext();

        //Setting back arrow icon.
        TextView arrowImage = (TextView) view.findViewById(R.id.customer_details_arrow);
        arrowImage.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        arrowImage.setTextSize(30);

        //Setting back arrow click listener.
        arrowImage.setOnClickListener(new View.OnClickListener() {
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

        //Setting customer's text parameters.
        TextView firstNameView = (TextView) view.findViewById(R.id.customer_details_first_name);
        firstNameView.setText(firstName);

        TextView lastNameView = (TextView) view.findViewById(R.id.customer_details_last_name);
        lastNameView.setText(lastName);

        TextView companyView = (TextView) view.findViewById(R.id.customer_details_company_txt);
        companyView.setText(company);

        //Setting customer profile image.
        TextView profileImage = (TextView) view.findViewById(R.id.customer_details_profile_image);
        profileImage.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        profileImage.setTextSize(80);

        //Setting mobile call icon.
        TextView callMobile = (TextView) view.findViewById(R.id.customer_details_mobile_call);
        callMobile.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        callMobile.setTextSize(30);
        callMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:" + "12345"));
//                context.startActivity(callIntent);
            }
        });

        //Setting landline call icon.
        TextView callLandline = (TextView) view.findViewById(R.id.customer_details_landline_call);
        callLandline.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        callLandline.setTextSize(30);
        callLandline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:" + "12345"));
//                context.startActivity(callIntent);
            }
        });

        //Setting company edit icon.
        TextView companyEditSign = (TextView) view.findViewById(R.id.customer_details_company_edit);
        companyEditSign.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        companyEditSign.setTextSize(20);
        companyEditSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //Setting job title edit icon.
        TextView jobTitleEditSign = (TextView) view.findViewById(R.id.customer_details_job_title_edit);
        jobTitleEditSign.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        jobTitleEditSign.setTextSize(20);
        jobTitleEditSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //Setting priority edit icon.
        TextView priorityEditSign = (TextView) view.findViewById(R.id.customer_details_priority_edit);
        priorityEditSign.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        priorityEditSign.setTextSize(20);
        priorityEditSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //Setting edit button icon.
        TextView editButtonIcon = (TextView) view.findViewById(R.id.customer_details_button_edit);
        editButtonIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        editButtonIcon.setTextSize(20);

        ConstraintLayout editButton = (ConstraintLayout) view.findViewById(R.id.customer_details_constraintLayout_edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditCustomerDetails();
            }
        });

        //Setting the comments expandable list.
        //this.expandableListViewComments = (ExpandableListView) view.findViewById(R.id.expandable_listView_additional_comments);
        initComments();
        this.listAdapter = new ExpandableListAdapter(context, listDataHeaderComments, listHashComments);
        //this.expandableListViewComments.setAdapter(this.listAdapter);

        //Setting the additional contacts expandable list.
        //this.expandableListViewContacts = (ExpandableListView) view.findViewById(R.id.expandable_listView_additional_contacts);
        initContacts();
        this.listAdapter = new ExpandableListAdapter(context, listDataHeaderContacts, listHashContacts);
        //this.expandableListViewContacts.setAdapter(this.listAdapter);

        TextView sendSms = (TextView) view.findViewById(R.id.customer_details_sendsms);
        sendSms.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        sendSms.setTextSize(30);

        TextView gps = (TextView) view.findViewById(R.id.customers_details_gps);
        gps.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        gps.setTextSize(30);

        return view;
    }

    /**
     * Goes to another page where the user can edit the customers details.
     */
    private void goToEditCustomerDetails() {

        android.support.v4.app.FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentCreateCustomer fragmentCreateCustomer = new FragmentCreateCustomer();

        //Pass the bundle which was earlier received from the customers list.
        fragmentCreateCustomer.setArguments(this.customerArgumentsBundle);
        ft.replace(R.id.container, fragmentCreateCustomer, "FragmentCreateCustomer");
        ft.addToBackStack("FragmentCreateCustomer");
        ft.commit();
    }


    //TODO delete this method
    private void initComments() {

        this.listDataHeaderComments = new ArrayList<>();
        this.listHashComments = new HashMap<>();

        String[] header = {"הערות", "fa_icon_comments"};
        listDataHeaderComments.add(header);
        //    listDataHeader.add("אנשי קשר של הלקוח");

        List<String> comments = new ArrayList<>();
        comments.add("הערה 1");
        comments.add("הערה 2");
        comments.add("הערה 3");

//        List<String> additionalContacts = new ArrayList<>();
//        additionalContacts.add("איש קשר 1");
//        additionalContacts.add("איש קשר 2");
//        additionalContacts.add("איש קשר 3");

        listHashComments.put(listDataHeaderComments.get(0)[0], comments);
        //    listHash.put(listDataHeader.get(0), additionalContacts);

    }

    //TODO delete this method
    private void initContacts() {

        this.listDataHeaderContacts = new ArrayList<>();
        this.listHashContacts = new HashMap<>();

        String[] header = {"אנשי קשר של הלקוח", "fa_icon_contacts_book"};
        listDataHeaderContacts.add(header);

        List<String> additionalContacts = new ArrayList<>();
        additionalContacts.add("איש קשר 1");
        additionalContacts.add("איש קשר 2");
        additionalContacts.add("איש קשר 3");

        listHashContacts.put(listDataHeaderContacts.get(0)[0], additionalContacts);
    }

}
