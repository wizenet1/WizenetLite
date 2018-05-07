package com.Fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.ActivityCalls;
import com.Activities.ActivityWebView;
import com.Activities.MenuActivity;
import com.Activities.R;
import com.Adapters.SideNavigationMenuAdapter;
import com.Adapters.SideNavigationMenuAdapter;
import com.AlertBadgeEnum;
import com.Classes.Call;
import com.Classes.Ccustomer;
import com.DatabaseHelper;
import com.File_;
import com.Helper;
import com.Icon_Manager;
//import com.MenuAlertBadgeEnum;
import com.MenuAlertBadgeEnum;
import com.ProgressTaskAll;
import com.ProgressTaskClient;
import com.model.Model;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentMenu extends android.support.v4.app.Fragment {
    TextView tv_username;
    CheckBox cb;
    Button btn2, mapid;
    EditText myEditText2;
    //BarCodeActivity barCodeActivity;
    Helper helper;
    TextView id1, id2, id3, id4, id5, id6, id7, id8, id9;
    TextView id1_text, id2_text, id3_text, id4_text, id5_text, id6_text, id7_text, id8_text, id9_text;
    DatabaseHelper db;
    Icon_Manager icon_manager;
    Context context;

    Map<MenuAlertBadgeEnum, NotificationBadge> alertBadgeDictionary = new HashMap<MenuAlertBadgeEnum, NotificationBadge>();

    private DrawerLayout drawerLayout;
    private ListView sideNavigationListView;
    TextView menu_bar_welcome_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        context = this.getContext();
        View v = inflater.inflate(R.layout.menu_fragment, container, false);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.GONE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        Icon_Manager icon_manager = new Icon_Manager();
        getCallStatuses();
        helper = new Helper();
        TextView menu_bar_profile = (TextView) v.findViewById(R.id.menu_bar_profile);
        menu_bar_profile.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", getContext()));
        //menu_bar_profile.setTextSize(40);
        //The drawer layout which covers the entire fragment.
        //this.drawerLayout = (DrawerLayout)v.findViewById(R.id.menu_drawer_layout);
        helper.writeCtypeIDandSons(getContext());
        helper.writeIS_Statuses(getContext());
        this.initializeBadgeDictionary(v);

        // Set the badge of customer.
        this.alertBadgeDictionary.get(MenuAlertBadgeEnum.badge_customers).
                setNumber(this.getCustomerListLength());
        // Set the badge of calls.
        this.alertBadgeDictionary.get(MenuAlertBadgeEnum.badge_calls).
                setNumber(this.getCallsListLength());


//        //THe list of items which are displayed in the side navigation menu.
//        this.sideNavigationListView = (ListView) v.findViewById(R.id.side_nav_list);
//
//        //Add the side navigation menu adapter.
//        SideNavigationMenuAdapter sideNavMenuAdapter = new SideNavigationMenuAdapter(getContext());
//        sideNavigationListView.setAdapter(sideNavMenuAdapter);

        //Top menu bar options image.
        ImageView menuBarOptionsImg = (ImageView) v.findViewById(R.id.menu_bar_options);
        menu_bar_welcome_txt = (TextView) v.findViewById(R.id.menu_bar_welcome_txt);
        //menu_bar_welcome_txt.setText(((MenuActivity)getActivity()).setName());
//        if (helper.isNetworkAvailable(context)){
//            Log.e("mytag","isNetworkAvailable");
//            Model.getInstance().Async_User_Details_Listener(helper.getMacAddr(), new Model.User_Details_Listener() {
//                @Override
//                public void onResult(String str) {
//                    menu_bar_welcome_txt.setText( " שלום " +str);
//                }
//            });
//        }else{
//            Log.e("mytag","is not NetworkAvailable");
//            menu_bar_welcome_txt.setText(DatabaseHelper.getInstance(context).getValueByKey("Cfname"));
//        }


        //Side navigation menu options image.
        ImageView sideNavHeaderOptionsImg = (ImageView) v.findViewById(R.id.side_nav_header_options);
        final ImageView id_tools = (ImageView) v.findViewById(R.id.id_tools);
        final ImageView id_customers = (ImageView) v.findViewById(R.id.id_customers);
        final ImageView id_calls = (ImageView) v.findViewById(R.id.id_calls);
        final ImageView id_offers = (ImageView) v.findViewById(R.id.id_offers);
        final ImageView id_preferences = (ImageView) v.findViewById(R.id.id_preferences);
        final ImageView id_orders = (ImageView) v.findViewById(R.id.id_orders);
        final ImageView id_accounting = (ImageView) v.findViewById(R.id.id_accounting);
        final ImageView id_reporttime = (ImageView) v.findViewById(R.id.id_reporttime);
        final ImageView id_missions = (ImageView) v.findViewById(R.id.id_missions);
        final ImageView id_favorites = (ImageView) v.findViewById(R.id.id_favorites);
        final ImageView id_masofon = (ImageView) v.findViewById(R.id.id_masofon);
        final ImageView id_reports = (ImageView) v.findViewById(R.id.id_reports);

        //db = DatabaseHelper.getInstance(getActivity().getApplicationContext());

        //helper = new Helper();

        //final int onTouchColor = 0xFFFFA64D;

        //Animation applied to menu icons to create click effect.
        final Animation clickAnimation = AnimationUtils.loadAnimation(context, R.anim.view_click_alpha);

        this.drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.main_drawer_layout);

        menuBarOptionsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMenuBarOptionsClick(view);
            }
        });

//        sideNavHeaderOptionsImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onSideNavOptionsClick(view);
//            }
//        });

        id_customers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentManager fragManager = getFragmentManager();
                id_customers.startAnimation(clickAnimation);
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentCustomer frag = new FragmentCustomer();
                ft.replace(R.id.container, frag, "f3");
                ft.addToBackStack("f3");
                ft.commit();


            }
        });

        id_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_offers.startAnimation(clickAnimation);
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentOffers frag = new FragmentOffers();
                ft.replace(R.id.container, frag, "FragmentOffers");
                ft.addToBackStack("FragmentOffers");
                ft.commit();
            }
        });

        id_tools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentManager fragManager = getFragmentManager();
                id_tools.startAnimation(clickAnimation);
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentTools frag = new FragmentTools();
                ft.replace(R.id.container, frag, "FragmentTools");
                ft.addToBackStack("FragmentTools");
                ft.commit();
                //((MenuActivity)getActivity()).initialIcons();
                //getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);


            }
        });
        id_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentManager fragManager = getFragmentManager();
                id_favorites.startAnimation(clickAnimation);
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentFavorite frag = new FragmentFavorite();
                ft.replace(R.id.container, frag, "FragmentFavorite");
                ft.addToBackStack("FragmentFavorite");
                ft.commit();
                //((MenuActivity)getActivity()).initialIcons();
                //getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

            }
        });


        id_reporttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_reporttime.startAnimation(clickAnimation);
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentLoginReport frag = new FragmentLoginReport();
                ft.replace(R.id.container, frag, "FragmentLoginReport");
                ft.addToBackStack("FragmentLoginReport");
                ft.commit();
                //((MenuActivity)getActivity()).initialIcons();
                //getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);
            }
        });
        id_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_orders.startAnimation(clickAnimation);
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentMenuOffline frag = new FragmentMenuOffline();
                ft.replace(R.id.container, frag, "FragmentMenuOffline");
                ft.addToBackStack("FragmentMenuOffline");
                ft.commit();
                //((MenuActivity)getActivity()).goToOrdersManually();
                //getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);
                //((MenuActivity)getActivity()).setOrderGray();
            }
        });
        id_missions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_missions.startAnimation(clickAnimation);
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentActions frag = new FragmentActions();
                ft.replace(R.id.container, frag, "FragmentActions");
                ft.addToBackStack("FragmentActions");
                ft.commit();
                //((MenuActivity)getActivity()).initialIcons();
                //getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);
            }
        });
        id_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_preferences.startAnimation(clickAnimation);
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                ControlPanelFragment fragment2 = new ControlPanelFragment();
                //TODO Doron, decide if the three commented lines below are needed or not
//                fragmentTransaction2.addToBackStack("xyz");
//                fragmentTransaction2.hide(FragmentMenu.this);
//                fragmentTransaction2.add(R.id.container, fragment2);
                fragmentTransaction2.replace(R.id.container,fragment2,"CPFragment");
                fragmentTransaction2.addToBackStack("CPFragment");
                fragmentTransaction2.commit();
                //((MenuActivity)getActivity()).initialIcons();
                //getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

            }
        });
        id_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_reports.startAnimation(clickAnimation);
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentClientReports frag = new FragmentClientReports();
                ft.replace(R.id.container, frag, "FragmentClientReports");
                ft.addToBackStack("FragmentClientReports");
                ft.commit();
                //((MenuActivity)getActivity()).initialIcons();
                //getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);
                //((MenuActivity)getActivity()).setOrderGray();
            }
        });


        id_accounting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_accounting.startAnimation(clickAnimation);
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentAccounting frag = new FragmentAccounting();
                ft.replace(R.id.container, frag, "FragmentAccounting");
                ft.addToBackStack("FragmentAccounting");
                ft.commit();
            }
        });

        id_masofon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id_masofon.startAnimation(clickAnimation);
                Intent intent = new Intent(getContext(), ActivityWebView.class);
                Bundle b = new Bundle();
                b.putInt("callid", -1);
                b.putInt("cid", -1);
                b.putInt("technicianid", Integer.parseInt(String.valueOf(DatabaseHelper.getInstance(getContext()).getValueByKey("CID"))));
                b.putString("action", "masofon");
                b.putString("specialurl", "");
                intent.putExtras(b);
                startActivity(intent);
            }
        });


        try {
            id_calls.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isCallsSummary = DatabaseHelper.getInstance(context).getValueByKey("APPS_CALLS_SUMMARY").equals("1");
                    if (isCallsSummary == true) {
                        id_calls.startAnimation(clickAnimation);
                        android.support.v4.app.FragmentManager fm = getFragmentManager();
                        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                        FragmentMidCalls frag = new FragmentMidCalls();
                        ft.replace(R.id.container, frag, "FragmentMidCalls");
                        ft.addToBackStack("FragmentMidCalls");
                        ft.commit();
                       // getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

                    } else {
                        Intent intent = new Intent(getActivity(), ActivityCalls.class);
                        intent.putExtra("choose", "total");

                        startActivity(intent);
                    }

                    //Intent intent = new Intent(getActivity(), ActivityCalls.class);
                    //startActivity(intent);
                }
            });
        } catch (Exception ex) {

            Log.e("mytag", ex.getMessage());
        }


        return v;
    }

    /**
     * Initializes all the badges in the action bar and main menu.
     */
    private void initializeBadgeDictionary(View v) {

        // Set all the badges objects.
        for (MenuAlertBadgeEnum alert : MenuAlertBadgeEnum.values()) {

            // Initialize the nav_bar badges.
            switch (alert) {
                case badge_customers:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge) v.findViewById(R.id.badge_customers));
                    break;
                case badge_offers:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge) v.findViewById(R.id.badge_offers));
                    break;
                case badge_orders:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge) v.findViewById(R.id.badge_orders));
                    break;
                case badge_calls:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge) v.findViewById(R.id.badge_calls));
                    break;
                case badge_missions:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge) v.findViewById(R.id.badge_missions));
                    break;
                case badge_reports:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge) v.findViewById(R.id.badge_reports));
                    break;
                case badge_masofon:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge) v.findViewById(R.id.badge_masofon));
                    break;
                case badge_favorites:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge) v.findViewById(R.id.badge_favorites));
                    break;
                case badge_reporttime:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge) v.findViewById(R.id.badge_reporttime));
                    break;
                case badge_tools:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge) v.findViewById(R.id.badge_tools));
                    break;
                case badge_preferences:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge) v.findViewById(R.id.badge_preferences));
                    break;
                case badge_accounting:
                    this.alertBadgeDictionary.put
                            (alert, (NotificationBadge) v.findViewById(R.id.badge_accounting));
                    break;

            }

        }
    }

    private void getCallStatuses() {
        Model.getInstance().Wz_Call_Statuses_Listener(helper.getMacAddr(), new Model.Wz_Call_Statuses_Listener() {
            @Override
            public void onResult(String str) {
                Log.e("mytag","write call statuses");
            }
        });
    }

    private int getCustomerListLength() {
        Ccustomer[] ccustomers;//= new Ccustomer[5];
        try{
            Helper helper = new Helper();
            File_ f = new File_();
            //myString = f.readFromFileInternal(getContext(),"customers.txt");
            String myString = f.readFromFileExternal(getContext(), "customers.txt");
            //Log.e("mytag", myString);
            JSONObject j = null;
            int length = 0;

            try {
                j = new JSONObject(myString);
                JSONArray jarray = j.getJSONArray("Wz_Clients_List");
                length = jarray.length();
            } catch (JSONException e) {
                e.printStackTrace();
                length = 0 ;
            }
            if (length == 0){
                return 0;
            }
            ccustomers = new Ccustomer[length];
            ccustomers = helper.getCustomersFromJson2(myString);
            return ccustomers.length;
        }catch (Exception e){
            return 0;
        }

        // ccustomers.length;
    }

    private int getCallsListLength() {
        JSONObject j = null;
        int length = 0;

        List<Call> calls = new ArrayList<Call>();
        try {
            calls = DatabaseHelper.getInstance(getContext()).getCalls("");
            length = calls.size();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return calls.size();
    }

//    FragmentManager fragmentManager2 = getFragmentManager();
//    FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
//    FragmentMenuOffline fragment2 = new FragmentMenuOffline();
//                fragmentTransaction2.addToBackStack("xyz");
//                fragmentTransaction2.hide(FragmentMenu.this);
//                fragmentTransaction2.add(R.id.container, fragment2);
//                fragmentTransaction2.commit();

    /**
     * Opens the side navigation menu from the left.
     *
     * @param view view
     */
    public void onMenuBarOptionsClick(View view) {

        this.drawerLayout.openDrawer(Gravity.START);
    }

    /**
     * Closes the side navigation menu.
     *
     * @param view view
     */
    public void onSideNavOptionsClick(View view) {

        this.drawerLayout.closeDrawer(Gravity.START);
    }

    protected void AlertDialogAllFirstTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("רשימת כל המוצרים");
        builder.setMessage("האם תרצה לסנכרן את כל המוצרים?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int ii) {
                boolean flag;
                flag = db.delete_from_mgnet_items("all");
                if (flag == true) {
                    if (db.mgnet_items_isEmpty("all")) {
                        new ProgressTaskAll(context).execute();
                    }
                }
            }
        });
        builder.setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int ii) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }

    protected void AlertDialogClientFirstTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("רשימת מוצרי לקוח לא הושלמה");
        builder.setMessage("האם תרצה לסנכרן מוצרי לקוח?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int ii) {

//                backServices.startService(new Intent(getContext(), BackServices.class));
                helper.startService_sync_products(getContext());
                Toast.makeText(getContext(), "startService_sync_products", Toast.LENGTH_LONG).show();
                // new ProgressTaskClient(getContext()).execute();
            }
        });
        builder.setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int ii) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }


    protected void setUsername() {
        try {
            Model.getInstance().Async_User_Details_Listener(helper.getMacAddr(), new Model.User_Details_Listener() {
                @Override
                public void onResult(String str) {
                    tv_username.setText("שלום " + str);
                    //Log.e("myTag",str);
                }
            });
        } catch (Exception ex) {
            Log.e("MYTAG", ex.getMessage());
        }
    }

    public void chkUpdateProducts() {


//------------------------
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sdt = df.format(new Date(System.currentTimeMillis()));
        String date_CLIENTS_PRODUCTS_UPDATE = db.getValueByKey("CLIENTS_PRODUCTS_UPDATE");
        String date_ALL_PRODUCTS_UPDATE = db.getValueByKey("PRODUCTS_UPDATE");


        Date convertedDate_all = new Date();
        Date convertedDate_client = new Date();
        Date currentTime = Calendar.getInstance().getTime();
        try {
            convertedDate_all = df.parse(date_ALL_PRODUCTS_UPDATE);
            convertedDate_client = df.parse(date_CLIENTS_PRODUCTS_UPDATE);
            //### to delete #####
            //SimpleDateFormat dfTry = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //Calendar c_week = Calendar.getInstance();
            //c_week.add(Calendar.MINUTE, 3);
            //Log.e("MYTAG","current:"+ currentTime.toString()+ " is after -try try- :"+c_week.getTime().toString() + "?");

            //##################
            Log.e("MYTAG", "current:" + currentTime.toString() + " is after convertedDate_all:" + convertedDate_all.toString() + "?");
            if (currentTime.after((convertedDate_all))) {
                AlertDialogAll();
            }
            Log.e("MYTAG", "current:" + currentTime.toString() + " is after convertedDate_all:" + convertedDate_client.toString() + "?");
            if (currentTime.after(convertedDate_client)) {
                AlertDialogClient();
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        Log.e("MYTAG","client:"+ convertedDate_client.toString()+
//                " \nnow:"+currentTime.toString() +
//                "\n all:"+ convertedDate_all.toString());

//            Toast.makeText(getActivity().getApplicationContext(),
//                    "yesterday-client"+ client.toString()+
//                            " \nnow:"+now.toString() +
//                            "\n all:"+ all.toString() , Toast.LENGTH_LONG).show();

//---------------------------


    }

    protected void AlertDialogAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("סנכרון רשימת כל המוצרים");
        builder.setMessage("לא בוצע סנכרון כבר 7 ימים, האם לסנכרן?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int ii) {
                boolean flag;
                flag = db.delete_from_mgnet_items("all");
                if (flag == true) {
                    if (db.mgnet_items_isEmpty("all") == true) {

                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar c_week = Calendar.getInstance();
                        c_week.add(Calendar.DAY_OF_YEAR, 7);
                        String formatted = df.format(c_week.getTime());
                        db.updateValue("PRODUCTS_UPDATE", formatted);


                        new ProgressTaskAll(context).execute();
                    }
                }
            }
        });
        builder.setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int ii) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }

    protected void DeleteForExample() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("סנכרון רשימת כל המוצרים");
        builder.setMessage("מחק מוצרי לקוח?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int ii) {
                try {
                    File dir = new File(Environment.getExternalStorageDirectory() + "/wizenet/client_products");
                    if (dir.isDirectory()) {
                        String[] children = dir.list();
                        for (int i = 0; i < children.length; i++) {
                            new File(dir, children[i]).delete();
                        }
                    }
                } catch (Exception ex) {
                    Log.e("MYTAG", "failed to delete");
                }

            }
        });
        builder.setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int ii) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }

    protected void AlertDialogClient() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("סנכרון רשימת מוצרי לקוח");
        builder.setMessage("לא בוצע סנכרון היום, האם לסנכרן?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int ii) {


                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c_week = Calendar.getInstance();
                c_week.add(Calendar.DAY_OF_YEAR, 7);
                String formatted = df.format(c_week.getTime());
                db.updateValue("CLIENTS_PRODUCTS_UPDATE", formatted);

                helper.deleteProductsFiles();
                new ProgressTaskClient(context).execute();


            }
        });
        builder.setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int ii) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }


    @Override
    public void onResume() {
        super.onResume();

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.GONE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        if (helper.isNetworkAvailable(context)) {
            //Log.e("mytag","isNetworkAvailable");
            Model.getInstance().Async_User_Details_Listener(helper.getMacAddr(), new Model.User_Details_Listener() {
                @Override
                public void onResult(String str) {
                    menu_bar_welcome_txt.setText(" שלום " + str);
                    if (str.contains("[]")){
                        ((MenuActivity) getActivity()).finish();
                    }
                }
            });
        } else {
            Log.e("mytag", "is not NetworkAvailable");
            menu_bar_welcome_txt.setText(" שלום " + DatabaseHelper.getInstance(context).getValueByKey("Cfname"));
        }
        //getActivity().findViewById(R.id.top_action_bar).setVisibility(View.GONE);
        //Toast.makeText(getActivity(),"onResume",Toast.LENGTH_SHORT).show();

    }

}
