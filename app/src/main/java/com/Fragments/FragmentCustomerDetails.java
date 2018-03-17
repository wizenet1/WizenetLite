package com.Fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
public class FragmentCustomerDetails extends Fragment {


    public FragmentCustomerDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_customer_details, container, false);

        Icon_Manager iconManager = new Icon_Manager();
        final Context context = getContext();

        //Setting back arrow icon.
        TextView arrowImage = (TextView) view.findViewById(R.id.customer_details_arrow);
        arrowImage.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        arrowImage.setTextSize(30);

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
        companyEditSign.setTextSize(30);
        companyEditSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //Setting job title edit icon.
        TextView jobTitleEditSign = (TextView) view.findViewById(R.id.customer_details_job_title_edit);
        jobTitleEditSign.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        jobTitleEditSign.setTextSize(30);
        jobTitleEditSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //Setting priority edit icon.
        TextView priorityEditSign = (TextView) view.findViewById(R.id.customer_details_priority_edit);
        priorityEditSign.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        priorityEditSign.setTextSize(30);
        priorityEditSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return view;
    }

}
