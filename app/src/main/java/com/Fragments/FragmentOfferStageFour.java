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
 * A simple {@link Fragment} subclass.
 */
public class FragmentOfferStageFour extends Fragment {


    public FragmentOfferStageFour() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_offer_stage_four, container, false);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        Context context = getContext();
        Icon_Manager iconManager = new Icon_Manager();

        //Set the icons.
        TextView thumbImage = (TextView) view.findViewById(R.id.offer_stage_four_thumb_image);
        thumbImage.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        thumbImage.setTextSize(100);

        TextView sendIcon = (TextView) view.findViewById(R.id.offer_stage_four_save_icon);
        thumbImage.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        thumbImage.setTextSize(30);


        //Next stage onClickListener.
        ConstraintLayout nextStageButton = (ConstraintLayout) view.findViewById(R.id.offer_stage_four_constraintLayout_save);
        nextStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

}
