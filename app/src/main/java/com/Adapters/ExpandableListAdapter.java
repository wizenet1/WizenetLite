package com.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.Activities.R;
import com.Icon_Manager;

import java.util.HashMap;
import java.util.List;


/**
 * The class is used as an adapter for expandable list views.
 * Extends the the BaseExpandableListAdapter.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String[]> listDataHeader;
    private HashMap<String, List<String>> listHashMap;

    /**
     * Constructor.
     * @param context context
     * @param listDataHeader list headers, what is shown before the list expands
     * @param listHashMap list values, what is shown after the list expands
     */
    public ExpandableListAdapter(Context context, List<String[]> listDataHeader, HashMap<String, List<String>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
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
        return this.listHashMap.get(this.listDataHeader.get(i)[0]).get(i1); // i = groupItem , i1 = childItem
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

        Icon_Manager iconManager = new Icon_Manager();
        String[] headerData = (String[])getGroup(i);
        String headerTitle = headerData[0];
        String headerIcon = headerData[1];

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandable_list_group, null);
        }

        //Setting header title.
        TextView lblListHeader = (TextView)view.findViewById(R.id.expandable_list_group_title);
        lblListHeader.setText(headerTitle);

        //Setting header icon.
        TextView lblListIcon = (TextView)view.findViewById(R.id.expandable_list_group_icon);
        lblListIcon.setText(context.getResources().getIdentifier(headerIcon, "string", context.getPackageName()));
        lblListIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        lblListIcon.setTextSize(30);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        final String childText = (String)getChild(i, i1);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandable_list_item, null);
        }

        TextView txtListChild = (TextView)view.findViewById(R.id.expandable_list_item_title);
        txtListChild.setText(childText);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
