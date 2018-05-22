package com.Fragments;


import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.MenuActivity;
import com.Activities.R;
import com.Icon_Manager;

/**
 * The fragment represents the fourth stage of an offer page.
 */
public class FragmentOfferStageFour extends Fragment {

    private View view;
    private Context context;
    private Icon_Manager iconManager;
    private TextView downloadLink;
    private TextView thumbImage;
    private TextView sendIcon;

    public FragmentOfferStageFour() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_offer_stage_four, container, false);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        this.context = getContext();
        this.iconManager = new Icon_Manager();

        //Assign data fields.
        this.assignDataFields();

        //Set the icons.
        this.setIcons();

        //Set download file onClick listener.
        this.downloadLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO download the actual pdf file
                Toast.makeText(context, "מוריד קובץ", Toast.LENGTH_SHORT).show();
            }
        });

        //Next stage onClickListener.
        ConstraintLayout nextStageButton = (ConstraintLayout) view.findViewById(R.id.offer_stage_four_constraintLayout_save);
        nextStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO implement
            }
        });

        return view;
    }

    /**
     * Assigns all the data fields to class members for future usage.
     */
    private void assignDataFields() {
        this.downloadLink = (TextView) view.findViewById(R.id.offer_stage_four_download_link);
        this.downloadLink.setPaintFlags(downloadLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void setIcons() {

        this.thumbImage = (TextView) view.findViewById(R.id.offer_stage_four_thumb_image);
        thumbImage.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        thumbImage.setTextSize(100);

        this.sendIcon = (TextView) view.findViewById(R.id.offer_stage_four_save_icon);
        this.sendIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        this.sendIcon.setTextSize(30);
    }
}
