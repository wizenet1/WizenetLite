package com.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.R;
import com.Fragments.ControlPanelFragment;
import com.Fragments.FragmentCalls;
import com.Fragments.FragmentCustomer;
import com.Fragments.FragmentMenu;
import com.Fragments.FragmentMessage;
import com.Fragments.FragmentOrders;
import com.Helper;

import java.util.ArrayList;

/**
 * The customer additional contacts list adapter.
 * Responsible for handling the UI of the contacts list.
 */
public class AdditionalContactsAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<String> contacts;
    private Context context;

    /**
     * Constructor.
     *
     * @param context context
     * @param contacts contacts list
     */
    public AdditionalContactsAdapter(Context context,
                                     ArrayList<String> contacts) {

        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return this.contacts.size();
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

        //For each row in the list, add the following items.
        view = this.layoutInflater.inflate(R.layout.additional_contacts_row, viewGroup, false);

        TextView name = (TextView) view.findViewById(R.id.additional_contacts_row_name);
        name.setText(this.contacts.get(i));

        //Setting view clickListener.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, contacts.get(i), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
