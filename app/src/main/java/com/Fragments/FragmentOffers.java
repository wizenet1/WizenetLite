package com.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.Activities.MenuActivity;
import com.Activities.R;

/**
 * The fragment represents the user's offers.
 */
public class FragmentOffers extends Fragment {


    public FragmentOffers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_offers, container, false);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        Button newOfferButton = (Button) view.findViewById(R.id.offers_new_offer_button);
        newOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentOfferStageOne frag = new FragmentOfferStageOne();
                ft.replace(R.id.container, frag, "FragmentOfferStageOne");
                ft.addToBackStack("FragmentOfferStageOne");
                ft.commit();
            }
        });

        Button opportunityStatusButton = (Button) view.findViewById(R.id.offers_new_opportunity_button);
        opportunityStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentOpportunitiesStatus frag = new FragmentOpportunitiesStatus();
                ft.replace(R.id.container, frag, "FragmentOpportunitiesStatus");
                ft.addToBackStack("FragmentOpportunitiesStatus");
                ft.commit();
            }
        });

        return view;
    }

}
