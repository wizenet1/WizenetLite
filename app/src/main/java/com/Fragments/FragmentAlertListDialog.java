package com.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.R;
import com.Adapters.AdditionalContactsAdapter;

import java.util.ArrayList;

/**
 * The fragment represents the floating additional contacts list dialog.
 * Responsible for handling the UI of the dialog.
 */

public class FragmentAlertListDialog extends DialogFragment {

    private ArrayList<String> items;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Get dialog arguments.
        Bundle bundle = getArguments();
        String title = bundle.getString("Title");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Setting the view.
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.customer_details_dialog, null);
        builder.setView(view);

        //Setting the dialog title.
        TextView titleTextView = (TextView)view.findViewById(R.id.customer_details_dialog_title);
        titleTextView.setText(title);

        this.items = new ArrayList<>();
        //TODO delete that
        items.add("לקוח 1");
        items.add("לקוח 2");
        items.add("לקוח 3");

        //Setting the additional contacts list.
        ListView listView = (ListView)view.findViewById(R.id.customer_details_dialog_listView);
        AdditionalContactsAdapter adapter = new AdditionalContactsAdapter(getContext(), items);
        listView.setAdapter(adapter);

        //builder.setItems(this.items, this);

        builder.setNegativeButton("בטל", null);
        return builder.create();
    }

//    @Override
//    public void onClick(DialogInterface dialogInterface, int i) {
//        Toast.makeText(getContext(), "customer " + i, Toast.LENGTH_SHORT).show();
//    }
}
