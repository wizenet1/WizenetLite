package com.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.Activities.MenuActivity;
import com.Activities.R;
import com.Adapters.OpportunitiesStatusExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The fragment represents the opportunities status list.
 */
public class FragmentOpportunitiesStatus extends Fragment {

    private ExpandableListView expandableListView;
    private OpportunitiesStatusExpandableListAdapter opportunitiesStatusExpandableListAdapter;
    private List<String[]> listDataHeader;
    private HashMap<String, List<String[]>> hashMap;

    public FragmentOpportunitiesStatus() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_opportunities_status, container, false);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        this.expandableListView = (ExpandableListView) view.findViewById(R.id.opportunities_status_expandableListView);

        //Initialize the data lists.
        initData();

        this.opportunitiesStatusExpandableListAdapter =
                new OpportunitiesStatusExpandableListAdapter(view.getContext(), this.listDataHeader, this.hashMap);
        this.expandableListView.setAdapter(this.opportunitiesStatusExpandableListAdapter);

        return view;
    }

    /**
     * Initializes the headers list and the hash map that holds the header with its list of items.
     */
    private void initData() {

        this.listDataHeader = new ArrayList<>();
        this.hashMap = new HashMap<>();

        //TODO dynamically initialize the counters
        //The counters that appear next to each opportunity header type.
        int counterNew = 2;
        int counterWaiting = 5;
        int counterClosing = 3;

        this.listDataHeader.add(new String[]{"ליד חדש", Integer.toString(counterNew)});
        this.listDataHeader.add(new String[]{"ממתין", Integer.toString(counterWaiting)});
        this.listDataHeader.add(new String[]{"לפני סגירה", Integer.toString(counterClosing)});

        //TODO dynamically initialize the lists
        //The inner lists that appear inside the opportunity types.
        List<String[]> newOpportunity = new ArrayList<>();
        newOpportunity.add(new String[]{"111", "ליד חדש 1"});
        newOpportunity.add(new String[]{"222", "ליד חדש 2"});

        List<String[]> waitingOpportunity = new ArrayList<>();
        waitingOpportunity.add(new String[]{"111", "ליד ממתין 1"});
        waitingOpportunity.add(new String[]{"222", "ליד ממתין 2"});
        waitingOpportunity.add(new String[]{"333", "ליד ממתין 3"});
        waitingOpportunity.add(new String[]{"444", "ליד ממתין 4"});

        List<String[]> closingOpportunity = new ArrayList<>();
        closingOpportunity.add(new String[]{"777", "ליד נסגר 1"});
        closingOpportunity.add(new String[]{"888", "ליד נסגר 2"});
        closingOpportunity.add(new String[]{"999", "ליד נסגר 3"});

        //Inserting the lists with their headers to the hash.
        this.hashMap.put(this.listDataHeader.get(0)[0], newOpportunity);
        this.hashMap.put(this.listDataHeader.get(1)[0], waitingOpportunity);
        this.hashMap.put(this.listDataHeader.get(2)[0], closingOpportunity);
    }

}
