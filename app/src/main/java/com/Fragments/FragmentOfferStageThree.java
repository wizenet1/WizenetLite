package com.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.Activities.MenuActivity;
import com.Activities.R;
import com.Icon_Manager;

import java.util.ArrayList;

/**
 * The fragment represents the third stage of an offer page.
 */
public class FragmentOfferStageThree extends Fragment {

    private Spinner commentsSpinner;
    private ArrayList<String> commentTopcis;
    EditText offer_stage_three_comments;
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

        this.initCommentsTopics();

        //Initialize spinner.
        this.commentsSpinner = (Spinner) view.findViewById(R.id.offer_stage_three_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, commentTopcis.toArray(new String[0])) {
            @NonNull
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(16);

                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.END);

                return v;
            }
        };
        this.commentsSpinner.setAdapter(arrayAdapter);

        //Set icon.
        TextView saveIcon = (TextView) view.findViewById(R.id.offer_stage_three_save_icon);
        saveIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        saveIcon.setTextSize(30);
        offer_stage_three_comments = (EditText) view.findViewById(R.id.offer_stage_three_comments);
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
    public String getComment(){
        return offer_stage_three_comments.getText().toString();
    }

    /**
     * Initializes the list of comment topics which appear in the spinner.
     */
    private void initCommentsTopics() {

        this.commentTopcis = new ArrayList<>();
        this.commentTopcis.add("נושא 1");
        this.commentTopcis.add("נושא 2");
        this.commentTopcis.add("נושא 3");
    }

}
