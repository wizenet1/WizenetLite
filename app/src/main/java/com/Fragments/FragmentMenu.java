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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.ActivityCalls;
import com.Activities.MenuActivity;
import com.Activities.R;
import com.Adapters.SideNavigationMenuAdapter;
import com.Adapters.SideNavigationMenuAdapter;
import com.DatabaseHelper;
import com.Helper;
import com.Icon_Manager;
import com.ProgressTaskAll;
import com.ProgressTaskClient;
import com.model.Model;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 20/09/2016.
 */


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
    private DrawerLayout drawerLayout;
    private ListView sideNavigationListView;
    TextView menu_bar_welcome_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        context = this.getContext();
        View v = inflater.inflate(R.layout.menu_fragment, container, false);
        Icon_Manager icon_manager = new Icon_Manager();
        getCallStatuses();
        helper = new Helper();
        TextView menu_bar_profile = (TextView) v.findViewById(R.id.menu_bar_profile);
        menu_bar_profile.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf", getContext()));
        //menu_bar_profile.setTextSize(40);
        //The drawer layout which covers the entire fragment.
        this.drawerLayout = (DrawerLayout) v.findViewById(R.id.menu_drawer_layout);

        //THe list of items which are displayed in the side navigation menu.
        this.sideNavigationListView = (ListView) v.findViewById(R.id.side_nav_list);

        //Add the side navigation menu adapter.
        SideNavigationMenuAdapter sideNavMenuAdapter = new SideNavigationMenuAdapter(getContext());
        sideNavigationListView.setAdapter(sideNavMenuAdapter);

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
        ImageView id_calls = (ImageView) v.findViewById(R.id.id_calls);
        ImageView id_offers = (ImageView) v.findViewById(R.id.id_offers);
        final ImageView id_preferences = (ImageView) v.findViewById(R.id.id_preferences);
        final ImageView id_orders = (ImageView) v.findViewById(R.id.id_orders);
        ImageView id_accounting = (ImageView) v.findViewById(R.id.id_accounting);
        final ImageView id_reporttime = (ImageView) v.findViewById(R.id.id_reporttime);
        final ImageView id_missions = (ImageView) v.findViewById(R.id.id_missions);
        final ImageView id_favorites = (ImageView) v.findViewById(R.id.id_favorites);
        //db = DatabaseHelper.getInstance(getActivity().getApplicationContext());

        //helper = new Helper();

        menuBarOptionsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMenuBarOptionsClick(view);
            }
        });

        sideNavHeaderOptionsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSideNavOptionsClick(view);
            }
        });

        final int onClickColor = 0xFFFFA64D;

        id_customers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_customers.setColorFilter(onClickColor, PorterDuff.Mode.MULTIPLY);
                //FragmentManager fragManager = getFragmentManager();
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentCustomer frag = new FragmentCustomer();
                ft.replace(R.id.container, frag, "FragmentMenu");
                ft.addToBackStack("FragmentMenu");
                ft.commit();


            }
        });
        id_tools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_tools.setColorFilter(onClickColor, PorterDuff.Mode.MULTIPLY);
                //FragmentManager fragManager = getFragmentManager();
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentTools frag = new FragmentTools();
                ft.replace(R.id.container, frag, "FragmentTools");
                ft.addToBackStack("FragmentTools");
                ft.commit();


            }
        });
        id_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_favorites.setColorFilter(onClickColor, PorterDuff.Mode.MULTIPLY);
                //FragmentManager fragManager = getFragmentManager();
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentFavorite frag = new FragmentFavorite();
                ft.replace(R.id.container, frag, "FragmentFavorite");
                ft.addToBackStack("FragmentFavorite");
                ft.commit();


            }
        });


        id_reporttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_reporttime.setColorFilter(onClickColor, PorterDuff.Mode.MULTIPLY);
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentLoginReport frag = new FragmentLoginReport();
                ft.replace(R.id.container, frag, "FragmentLoginReport");
                ft.addToBackStack("FragmentLoginReport");
                ft.commit();
            }
        });
        id_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_orders.setColorFilter(onClickColor, PorterDuff.Mode.MULTIPLY);
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentMenuOffline frag = new FragmentMenuOffline();
                ft.replace(R.id.container, frag, "FragmentMenu");
                ft.addToBackStack("FragmentMenu");
                ft.commit();
            }
        });
        id_missions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_missions.setColorFilter(onClickColor, PorterDuff.Mode.MULTIPLY);
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                FragmentMessage frag = new FragmentMessage();
                ft.replace(R.id.container, frag, "FragmentMessage");
                ft.addToBackStack("FragmentMessage");
                ft.commit();
            }
        });
        id_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_preferences.setColorFilter(onClickColor, PorterDuff.Mode.MULTIPLY);
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                ControlPanelFragment fragment2 = new ControlPanelFragment();
                fragmentTransaction2.addToBackStack("xyz");
                fragmentTransaction2.hide(FragmentMenu.this);
                fragmentTransaction2.add(R.id.container, fragment2);
                fragmentTransaction2.commit();
            }
        });


        try {
            id_calls.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ActivityCalls.class);
                    startActivity(intent);
                }
            });
        } catch (Exception ex) {

            Log.e("mytag", ex.getMessage());
        }


        return v;
    }

    private void getCallStatuses() {
        Model.getInstance().Wz_Call_Statuses_Listener(helper.getMacAddr(), new Model.Wz_Call_Statuses_Listener() {
            @Override
            public void onResult(String str) {

            }
        });
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
//    public class ProgressTaskAll extends AsyncTask<String, String, String> {
//        private ProgressDialog dialog;
//        List<Message> titles;
//
//        //private List<Message> messages;
//        public ProgressTaskAll() {
//            dialog = new ProgressDialog(getContext());
//        }
//        @Override
//        protected void onPreExecute() {
//            //this.working_dialog = ProgressDialog.show(getContext(), "","Working please wait...", true);
//
//            this.dialog.setMessage("Working please wait...");
//            this.dialog.show();
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    dialog.dismiss();
//                }
//            }, 5000);
//        }
//        @Override
//        protected void onPostExecute(final String success) {
//        }
//        @Override
//        protected String doInBackground(final String... args) {
//            helper.ALLProductsSync(getContext());
//            return "";
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();

        if (helper.isNetworkAvailable(context)) {
            Log.e("mytag", "isNetworkAvailable");
            Model.getInstance().Async_User_Details_Listener(helper.getMacAddr(), new Model.User_Details_Listener() {
                @Override
                public void onResult(String str) {
                    menu_bar_welcome_txt.setText(" שלום " + str);
                }
            });
        } else {
            Log.e("mytag", "is not NetworkAvailable");
            menu_bar_welcome_txt.setText(" שלום " + DatabaseHelper.getInstance(context).getValueByKey("Cfname"));
        }
        //Toast.makeText(getActivity(),"onResume",Toast.LENGTH_SHORT).show();

    }

    public String getEditText() {
        return myEditText2.getText().toString();
    }


    public void goToMapFragment() {
        android.support.v4.app.FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        //MapFragment frag = new MapFragment();
        // ft.replace(R.id.container,frag,"MapFragment");
        //tv.setVisibility(TextView.GONE);
        ft.addToBackStack("MapFragment");
        ft.commit();
    }

//    public String getMacAddress() {
//        WifiManager manager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = manager.getConnectionInfo();
//        String address = info.getMacAddress();
//        return address;
    //}
}
