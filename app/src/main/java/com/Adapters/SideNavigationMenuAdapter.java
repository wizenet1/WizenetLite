package com.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Activities.R;
import com.Helper;

/**
 * The side navigation menu adapter.
 * Responsible for handling the UI of the menu.
 */
public class SideNavigationMenuAdapter extends BaseAdapter {

    //Titles to display in each row.
    public String[] titles = {"דף הבית", "לקוחות", "קריאות", "הזמנות", "הודעות", "דוחות", "משימות",
            "הנחש", "כלים", "הגדרות","יציאה מהמערכת"};

    //Icons to display in each row.
    private int[] images = {R.drawable.side_nav_home, R.drawable.side_nav_customers, R.drawable.side_nav_calls,
            R.drawable.side_nav_orders, R.drawable.side_nav_messages, R.drawable.side_nav_reports,
            R.drawable.side_nav_goals, R.drawable.side_nav_accounting, R.drawable.side_nav_tools,
            R.drawable.side_nav_preferences,0};
    private android.support.constraint.ConstraintLayout wrapper;
    private LayoutInflater layoutInflater;
    private ImageView icon;
    private TextView title;
    private Context context;
    /**
     * Constructor.
     * @param context context
     */
    public SideNavigationMenuAdapter(Context context) {

        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        //For each row in the list, add the following items.
        view = this.layoutInflater.inflate(R.layout.side_navigation_row, viewGroup, false);

        this.icon = (ImageView) view.findViewById(R.id.side_nav_row_img);
        this.title = (TextView) view.findViewById(R.id.side_nav_row_title);
        this.wrapper = (android.support.constraint.ConstraintLayout) view.findViewById(R.id.wrapper);

        this.icon.setImageResource(this.images[i]);
        this.title.setText(this.titles[i]);


        this.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("mytag", titles[i].toString());
                GoToScreen(titles[i].toString());
            }
        });

        return view;
    }
    private void GoToScreen(String str){
        Helper helper = new Helper();
        switch(str) {
            case "יציאה מהמערכת":
                ((FragmentActivity)context).finish();
                break;
            case "לקוחות":
                helper.goTocustomers(context);
                break;
            case "הזמנות":
                helper.goToOrdersFragment(context);
                break;

            default:
                //setContentView(R.layout.default);
        }
        //
    }
}
