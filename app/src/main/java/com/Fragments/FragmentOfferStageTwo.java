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
 * The fragment represents the second stage of an offer page.
 */
public class FragmentOfferStageTwo extends Fragment {


    public FragmentOfferStageTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_offer_stage_two, container, false);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        Context context = getContext();
        Icon_Manager iconManager = new Icon_Manager();

        //Set the icons.
        TextView image = (TextView) view.findViewById(R.id.offer_stage_two_image);
        image.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        image.setTextSize(30);

        TextView serialNumberIcon = (TextView) view.findViewById(R.id.offer_stage_two_serial_number_icon);
        serialNumberIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        serialNumberIcon.setTextSize(30);

        TextView descriptionIcon = (TextView) view.findViewById(R.id.offer_stage_two_description_icon);
        descriptionIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        descriptionIcon.setTextSize(30);

        TextView quantityIcon = (TextView) view.findViewById(R.id.offer_stage_two_quantity_icon);
        quantityIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        quantityIcon.setTextSize(30);

        TextView priceIcon = (TextView) view.findViewById(R.id.offer_stage_two_price_icon);
        priceIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        priceIcon.setTextSize(25);

        TextView addProductIcon = (TextView) view.findViewById(R.id.offer_stage_two_add_product_icon);
        addProductIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        addProductIcon.setTextSize(30);

        TextView deleteIcon = (TextView) view.findViewById(R.id.offer_stage_two_delete_icon);
        deleteIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        deleteIcon.setTextSize(30);

        TextView saveIcon = (TextView) view.findViewById(R.id.offer_stage_two_save_icon);
        saveIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        saveIcon.setTextSize(30);

        //Add product button onClickListener.
        ConstraintLayout addProductButton = (ConstraintLayout) view.findViewById(R.id.offer_stage_two_ConstraintLayout_add_product);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO implement
            }
        });

        //Delete button onClickListener.
        ConstraintLayout deleteButton = (ConstraintLayout) view.findViewById(R.id.offer_stage_two_ConstraintLayout_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //TODO implement
            }
        });

        //Next stage onClickListener.
        ConstraintLayout nextStageButton = (ConstraintLayout) view.findViewById(R.id.offer_stage_two_constraintLayout_save);
        nextStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentOfferStageThree frag = new FragmentOfferStageThree();
                ft.replace(R.id.container, frag, "FragmentOfferStageThree");
                ft.addToBackStack("FragmentOfferStageThree");
                ft.commit();
            }
        });

        return view;
    }

}
