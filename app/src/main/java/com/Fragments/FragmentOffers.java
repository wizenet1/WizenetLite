package com.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.Activities.MenuActivity;
import com.Activities.R;
import com.Classes.Ccustomer;
import com.Helper;
import com.Json_;
import com.model.Model;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * The fragment represents the user's offers.
 */
public class FragmentOffers extends Fragment {
    Helper h;
    Json_ j_ = null;
    public FragmentOffers() {
        // Required empty public constructor
    }
    public String oCcompanyName;
    public String oEmail;
    public String oCphone;
    public String oCcell;
    public String oCcity;
    public String oCaddress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        j_ = new Json_();
        h = new Helper();
        View view =  inflater.inflate(R.layout.fragment_offers, container, false);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();
        addCustomersAndProducts();
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
    private void addCustomersAndProducts(){
        if (h.isNetworkAvailable(getContext()) == true){
            Model.getInstance().Async_Wz_ret_ClientsAddressesByActions_Listener(h.getMacAddr(getContext()), "", new Model.Wz_ret_ClientsAddressesByActions_Listener() {
                @Override
                public void onResult(String str) {
                    //Toast.makeText(getContext(), str, Toast.LENGTH_LONG).show();
                    //Toast.makeText(getContext(), "יובא בהצלחה", Toast.LENGTH_SHORT).show();
                }
            });
            Model.getInstance().Async_Wz_retProducts(h.getMacAddr(getContext()), new Model.Wz_retProducts_Listener() {
                @Override
                public void onResult(String str) {
                    //Toast.makeText(getContext(),str, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
