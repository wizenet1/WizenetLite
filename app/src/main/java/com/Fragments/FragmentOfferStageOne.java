package com.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.Activities.MenuActivity;
import com.Activities.R;
import com.Icon_Manager;

/**
 * The fragment represents the first stage of an offer page.
 */
public class FragmentOfferStageOne extends Fragment {


    public FragmentOfferStageOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_offer_stage_one, container, false);

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

        ConstraintLayout nextStageButton = (ConstraintLayout) view.findViewById(R.id.offer_stage_one_constraintLayout_save);
        nextStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentOfferStageTwo frag = new FragmentOfferStageTwo();
                ft.replace(R.id.container, frag, "FragmentOfferStageTwo");
                ft.addToBackStack("FragmentOfferStageTwo");
                ft.commit();
            }
        });

        return view;
    }

}
