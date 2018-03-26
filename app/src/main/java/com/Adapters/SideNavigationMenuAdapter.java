package com.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Activities.ActivityCalls;
import com.Activities.MenuActivity;
import com.Activities.R;
import com.DatabaseHelper;
import com.Fragments.ControlPanelFragment;
import com.Fragments.FragmentAccounting;
import com.Fragments.FragmentActions;
import com.Fragments.FragmentClientReports;
import com.Fragments.FragmentCustomer;
import com.Fragments.FragmentMenu;
import com.Fragments.FragmentMenuOffline;
import com.Fragments.FragmentMessage;
import com.Fragments.FragmentMidCalls;
import com.Fragments.FragmentOrders;
import com.Fragments.FragmentReports;
import com.Fragments.FragmentTools;
import com.Helper;

/**
 * The side navigation menu adapter.
 * Responsible for handling the UI of the menu.
 */
public class SideNavigationMenuAdapter extends BaseAdapter {

    //Titles to display in each row.
    public String[] titles = {"דף הבית", "לקוחות", "קריאות", "הזמנות", "דוחות", "משימות",
            "הנחש", "כלים", "הגדרות", "יציאה מהמערכת"};

    //Icons to display in each row.
    private int[] images = {R.drawable.side_nav_home, R.drawable.side_nav_customers, R.drawable.side_nav_calls,
            R.drawable.side_nav_orders /*R.drawable.side_nav_messages*/, R.drawable.side_nav_reports,
            R.drawable.side_nav_goals, R.drawable.side_nav_accounting, R.drawable.side_nav_tools,
            R.drawable.side_nav_preferences, 0};
    private android.support.constraint.ConstraintLayout wrapper;
    private LayoutInflater layoutInflater;
    private ImageView icon;
    private TextView title;
    private Context context;
    private android.support.v4.app.FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;

    /**
     * Constructor.
     *
     * @param context context
     * @param fragmentManager FragmentManager
     * @param drawerLayout DrawerLayout to control the side menu
     */
    public SideNavigationMenuAdapter(Context context,
                                     android.support.v4.app.FragmentManager fragmentManager,
                                     DrawerLayout drawerLayout) {

        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.drawerLayout = drawerLayout;
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

        if(view == null){

            view = this.layoutInflater.inflate(R.layout.side_navigation_row, viewGroup, false);
        }

        //For each row in the list, add the following items.
        this.icon = (ImageView) view.findViewById(R.id.side_nav_row_img);
        this.title = (TextView) view.findViewById(R.id.side_nav_row_title);
        this.wrapper = (android.support.constraint.ConstraintLayout) view.findViewById(R.id.wrapper);

        this.icon.setImageResource(this.images[i]);
        this.title.setText(this.titles[i]);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToScreen(titles[i].toString());
            }
        });

        return view;
    }

    private void GoToScreen(String str) {
        Helper helper = new Helper();
        android.support.v4.app.FragmentTransaction ft = this.fragmentManager.beginTransaction();

        switch (str) {
            case "יציאה מהמערכת":
                ((Activity) context).finish();
                break;
            case "דף הבית":
                //helper.goToMenuFragment(context);
                FragmentMenu fragmentMenu = new FragmentMenu();
                ft.replace(R.id.container,fragmentMenu,"FragmentMenu");
                //tv.setVisibility(TextView.GONE);
                ft.addToBackStack("FragmentMenu");
                ft.commit();
                this.drawerLayout.closeDrawer(Gravity.START);
                break;
            case "לקוחות":
                //helper.goTocustomers(context);
                FragmentCustomer fragmentCustomer = new FragmentCustomer();
                ft.replace(R.id.container, fragmentCustomer, "FragmentCustomer");
                ft.addToBackStack("FragmentCustomer");
                ft.commit();
                this.drawerLayout.closeDrawer(Gravity.START);
                break;
            case "הזמנות":
                //helper.goToOrdersFragment(context);
                //TODO change to the correct fragment(FragmentOrders)
                FragmentMenuOffline fragmentOrders = new FragmentMenuOffline();
                ft.replace(R.id.container,fragmentOrders,"FragmentMenuOffline");
                //tv.setVisibility(TextView.GONE);
                ft.addToBackStack("FragmentMenuOffline");
                ft.commit();
                this.drawerLayout.closeDrawer(Gravity.START);
                break;
            case "קריאות":
                //Intent intent = new Intent(((Activity) context), ActivityCalls.class);
                //((Activity) context).startActivity(intent);
                boolean isCallsSummary = DatabaseHelper.getInstance(context).getValueByKey("APPS_CALLS_SUMMARY").equals("1");
                if (isCallsSummary == true) {
                    FragmentMidCalls frag = new FragmentMidCalls();
                    ft.replace(R.id.container, frag, "FragmentMidCalls");
                    ft.addToBackStack("FragmentMidCalls");
                    ft.commit();
                    // getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

                } else {
                    Intent intent = new Intent(context, ActivityCalls.class);
                    intent.putExtra("choose", "total");

                    context.startActivity(intent);
                }
//                FragmentMidCalls fragCalls = new FragmentMidCalls();
//                ft.replace(R.id.container, fragCalls, "FragmentMidCalls");
//                ft.addToBackStack("FragmentMidCalls");
//                ft.commit();
                this.drawerLayout.closeDrawer(Gravity.START);
                break;
            case "משימות":
                //helper.goToMessagesFragment(context);
                FragmentActions fragmentActions = new FragmentActions();
                ft.replace(R.id.container,fragmentActions,"FragmentActions");
                ft.addToBackStack("FragmentActions");
                ft.commit();
                this.drawerLayout.closeDrawer(Gravity.START);
                break;
//            case "הודעות":
//                //helper.goToMessagesFragment(context);
//                FragmentMessage fragmentMessage = new FragmentMessage();
//                ft.replace(R.id.container,fragmentMessage,"FragmentMessage");
//                //tv.setVisibility(TextView.GONE);
//                ft.addToBackStack("FragmentMessage");
//                ft.commit();
//                this.drawerLayout.closeDrawer(Gravity.START);
//                break;
            case "דוחות":
                //helper.goToMessagesFragment(context);
                FragmentClientReports fragmentClientReports = new FragmentClientReports();
                ft.replace(R.id.container,fragmentClientReports,"FragmentClientReports");
                ft.addToBackStack("FragmentClientReports");
                ft.commit();
                this.drawerLayout.closeDrawer(Gravity.START);
                break;
            case "הנחש":
                //helper.goToMessagesFragment(context);
                FragmentAccounting fragmentAccounting = new FragmentAccounting();
                ft.replace(R.id.container,fragmentAccounting,"FragmentAccounting");
                ft.addToBackStack("FragmentAccounting");
                ft.commit();
                this.drawerLayout.closeDrawer(Gravity.START);
                break;
            case "כלים":
                //helper.goToMessagesFragment(context);
                FragmentTools fragmentTools = new FragmentTools();
                ft.replace(R.id.container,fragmentTools,"FragmentTools");
                ft.addToBackStack("FragmentTools");
                ft.commit();
                this.drawerLayout.closeDrawer(Gravity.START);
                break;
            case "הגדרות":
                //helper.goToCPFragment(context);
                ControlPanelFragment controlPanelFragment = new ControlPanelFragment();
                ft.replace(R.id.container,controlPanelFragment,"CPFragment");
                ft.addToBackStack("CPFragment");
                //tv.setVisibility(TextView.GONE);
                ft.commit();
                this.drawerLayout.closeDrawer(Gravity.START);
                break;
            case "עדכון מערכת":
                //Intent returnIntent = new Intent();
                //returnIntent.putExtra("result","close");
                //((MenuActivity) context).setResult(Activity.RESULT_OK,returnIntent);
                //((MenuActivity) context).finish();

            default:
                //setContentView(R.layout.default);
        }
        //
    }
}
