package com.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Activities.MenuActivity;
import com.Activities.R;
import com.Icon_Manager;

/**
 * The fragment represents the third stage of an offer page.
 */
public class FragmentOfferStageThree extends Fragment {


    public FragmentOfferStageThree() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer_stage_three, container, false);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        Context context = getContext();
        Icon_Manager iconManager = new Icon_Manager();

        //Set the icons.
        TextView customerImage = (TextView) view.findViewById(R.id.offer_stage_three_customer_image);
        customerImage.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        customerImage.setTextSize(50);

        TextView productImage = (TextView) view.findViewById(R.id.offer_stage_three_product_image);
        productImage.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        productImage.setTextSize(30);

        TextView serialNumberIcon = (TextView) view.findViewById(R.id.offer_stage_three_serial_number_icon);
        serialNumberIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        serialNumberIcon.setTextSize(30);

        TextView descriptionIcon = (TextView) view.findViewById(R.id.offer_stage_three_description_icon);
        descriptionIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        descriptionIcon.setTextSize(30);

        TextView unitPriceIcon = (TextView) view.findViewById(R.id.offer_stage_three_unit_price_icon);
        unitPriceIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        unitPriceIcon.setTextSize(25);

        TextView quantityIcon = (TextView) view.findViewById(R.id.offer_stage_three_quantity_icon);
        quantityIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        quantityIcon.setTextSize(30);

        TextView totalPriceIcon = (TextView) view.findViewById(R.id.offer_stage_three_total_price_icon);
        totalPriceIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        totalPriceIcon.setTextSize(25);

        TextView saveIcon = (TextView) view.findViewById(R.id.offer_stage_three_save_icon);
        saveIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        saveIcon.setTextSize(30);

        //Next stage onClickListener.
        ConstraintLayout nextStageButton = (ConstraintLayout) view.findViewById(R.id.offer_stage_three_constraintLayout_save);
        nextStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentOfferStageFour frag = new FragmentOfferStageFour();
                ft.replace(R.id.container, frag, "FragmentOfferStageFour");
                ft.addToBackStack("FragmentOfferStageFour");
                ft.commit();
            }
        });

        return view;
    }

}
