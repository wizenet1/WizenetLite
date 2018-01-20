package com.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Activities.R;

/**
 * The side navigation menu adapter.
 * Responsible for handling the UI of the menu.
 */
public class SideNavigationMenuAdapter extends BaseAdapter {

    //Titles to display in each row.
    private String[] titles = {"דף הבית", "לקוחות", "קריאות", "הזמנות", "הודעות", "דוחות", "משימות",
            "הנחש", "כלים", "הגדרות"};

    //Icons to display in each row.
    private int[] images = {R.drawable.side_nav_home, R.drawable.side_nav_customers, R.drawable.side_nav_calls,
            R.drawable.side_nav_orders, R.drawable.side_nav_messages, R.drawable.side_nav_reports,
            R.drawable.side_nav_goals, R.drawable.side_nav_accounting, R.drawable.side_nav_tools,
            R.drawable.side_nav_preferences};

    private LayoutInflater layoutInflater;
    private ImageView icon;
    private TextView title;

    /**
     * Constructor.
     * @param context context
     */
    public SideNavigationMenuAdapter(Context context) {

        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.titles.length;
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        //For each row in the list, add the following items.
        view = this.layoutInflater.inflate(R.layout.side_navigation_row, viewGroup, false);

        this.icon = (ImageView) view.findViewById(R.id.side_nav_row_img);
        this.title = (TextView) view.findViewById(R.id.side_nav_row_title);

        this.icon.setImageResource(this.images[i]);
        this.title.setText(this.titles[i]);

        return view;
    }
}
