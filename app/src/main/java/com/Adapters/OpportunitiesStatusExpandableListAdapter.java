package com.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.R;
import com.Fragments.FragmentOpportunityAlertDialog;
import com.Icon_Manager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * The class is used as an adapter for opportunities status fragment expandable list view.
 * Extends the the BaseExpandableListAdapter.
 */
public class OpportunitiesStatusExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String[]> listDataHeader;
    private HashMap<String, List<String[]>> listHashMap;
    private Icon_Manager iconManager;
    private android.support.v4.app.FragmentManager fragmentManager;

    /**
     * Constructor.
     *
     * @param context        context
     * @param listDataHeader list headers, what is shown before the list expands
     * @param listHashMap    list values, what is shown after the list expands
     */
    public OpportunitiesStatusExpandableListAdapter(Context context, List<String[]> listDataHeader,
                                                    HashMap<String, List<String[]>> listHashMap,
                                                    android.support.v4.app.FragmentManager fragmentManager) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
        this.fragmentManager = fragmentManager;
        this.iconManager = new Icon_Manager();
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.listHashMap.get(listDataHeader.get(i)[0]).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {

        // i = groupItem , i1 = childItem
        return this.listHashMap.get(this.listDataHeader.get(i)[0]).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        //Icon_Manager iconManager = new Icon_Manager();
        String[] headerData = (String[]) getGroup(i);
        String headerTitle = headerData[0];
        String headerCounter = headerData[1];

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.opportunities_status_expandable_list_group, null);
        }

        //Setting header title.
        TextView headerTitleText = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_group_title);
        headerTitleText.setText(headerTitle);

        //Setting header icon.
        TextView headerCounterText = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_group_counter);
        headerCounterText.setText("(" + headerCounter + ")");

//        lblListIcon.setText(context.getResources().getIdentifier(headerIcon, "string", context.getPackageName()));
//        lblListIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
//        lblListIcon.setTextSize(30);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        final String opportunityNumber = ((String[]) getChild(i, i1))[0];
        final String opportunityDescription = ((String[]) getChild(i, i1))[1];

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.opportunities_status_expandable_list_item, null);
        }

        //Setting opportunity number.
        TextView opportunityNumberText = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_item_number_text);
        opportunityNumberText.setText(opportunityNumber);

        //Setting opportunity description.
        TextView opportunityDescriptionText = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_item_description_text);
        opportunityDescriptionText.setText(opportunityDescription);

        //Setting the edit icon.
        TextView editIcon = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_item_edit_icon);
        editIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        editIcon.setTextSize(30);

        final int groupId = i;

        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlertDialog(groupId);
            }
        });

        return view;
    }

    /**
     * Opens an edit alert dialog.
     *
     * @param groupId group id
     */
    private void openAlertDialog(int groupId) {
        Bundle bundle = new Bundle();
        bundle.putInt("HeaderId", groupId);
        List<String> TitlesOnly = getOnlyFirstStrings();
        String headersInJson = new Gson().toJson(TitlesOnly);
        bundle.putString("HeadersInJson", headersInJson);

        FragmentOpportunityAlertDialog alertDialog = new FragmentOpportunityAlertDialog();
        alertDialog.setArguments(bundle);
        alertDialog.show(this.fragmentManager, "opportunity");
    }

    /**
     * Returns only the titles from the listDataHeaders list.
     * @return List of string
     */
    private List<String> getOnlyFirstStrings(){
        List<String> list = new ArrayList<>();

        for(String[] item : this.listDataHeader){
            list.add(item[0]);
        }

        return list;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
