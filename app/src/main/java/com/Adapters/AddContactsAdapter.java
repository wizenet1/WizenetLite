package com.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.Activities.R;
import com.Icon_Manager;

import java.util.ArrayList;

/**
 * The adapter for the add contacts option list.
 * Responsible for handling the UI of the contacts list.
 */

public class AddContactsAdapter extends BaseAdapter {

    private Context context;
    private ListView listView;
    private LayoutInflater layoutInflater;
    //TODO need to change the list from string to Contact object.
    private ArrayList<String> contactsList;
    private Icon_Manager iconManager;

    public AddContactsAdapter(Context context, ListView listView){

        this.context = context;
        this.listView = listView;
        this.layoutInflater = LayoutInflater.from(context);
        this.contactsList = new ArrayList<>();
        this.iconManager = new Icon_Manager();
    }

    @Override
    public int getCount() {
        return this.contactsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view = this.layoutInflater.inflate(R.layout.add_contacts_row, viewGroup, false);

        //Set delete icon.
        TextView deleteIcon = (TextView)view.findViewById(R.id.add_contacts_row_delete_icon);
        deleteIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        deleteIcon.setTextSize(20);

        //Set delete icon onClick listener.
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactsList.remove(i);
                notifyDataSetChanged();
                setListViewHeightBasedOnChildren(listView);
            }
        });

        //Set name.
        TextView name = (TextView) view.findViewById(R.id.add_contacts_row_name);
        name.setText(this.contactsList.get(i));

        return view;
    }

    public void addContact(String name){
        this.contactsList.add(name);
        this.notifyDataSetChanged();
        this.setListViewHeightBasedOnChildren(this.listView);
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {

            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

}
