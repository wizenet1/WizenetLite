package com.Fragments;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.ActivityWebView;
import com.Activities.MenuActivity;
import com.Activities.R;
import com.Classes.Favorite;
import com.Classes.Message;
import com.DatabaseHelper;
import com.Helper;
import com.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 31/08/2016.
 */
public class FragmentSecret extends android.support.v4.app.Fragment {


    EditText key_et, val_et;
    Button addmem_btn, remove_btn;
    DatabaseHelper db;
    ListView myList;
    List<Message> data2 = new ArrayList<Message>();
    String dataName;
    CheckBox cb;
    LocationManager manager = null;
    String firstname = "";
    String lastname = "";
    boolean result = false;
    LinearLayout layout;
    Helper helper;
    EditText table;
    TextView id1,id2,id3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        helper = new Helper();
        View v = inflater.inflate(R.layout.fragment_secret, container, false);
        setHasOptionsMenu(true);

        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();
        table = (EditText)   v.findViewById(R.id.table);
        id1 = (TextView) v.findViewById(R.id.id1) ;
        id2 = (TextView) v.findViewById(R.id.id2) ;
        table.setText("IS_Actions");
        db = DatabaseHelper.getInstance(getContext());

        layout = (LinearLayout) v.findViewById(R.id.placeHolderFragment);
        id1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isISActionsEsixts = DatabaseHelper.getInstance(getContext()).isTableExists("IS_Actions") ? true : false;
                if (isISActionsEsixts == true){
                    Toast.makeText(getContext(), " IS_Actions קיים", Toast.LENGTH_LONG).show();
                }
            }
        });
        id2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean flag = false;
                flag =DatabaseHelper.getInstance(getContext()).delete_IS_Actions_Table();
                if (flag == true){
                    Toast.makeText(getContext(), "deleted successfully", Toast.LENGTH_LONG).show();
                }
            }
        });

//        Model.getInstance().Async_Wz_retClientFavorites_Listener(helper.getMacAddr(), new Model.Wz_retClientFavorites_Listener() {
//            @Override
//            public void onResult(String str) {
//                Log.e("mytag", str);
//                getFavorite(str);
//            }
//        });


        return v;
    }

    ;
//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        MenuItem item = menu.findItem(R.menu.menu_main);
//        item.setVisible(false);
//    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setEnabled(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    public Favorite[] getFavorite(String json) {


        Favorite[] FavoriteList = new Favorite[0];
        JSONObject j = null;
        try {
            j = new JSONObject(json);
            //get the array [...] in json
            JSONArray jarray = j.getJSONArray("Wz_retClientFavorites");
            FavoriteList = new Favorite[jarray.length()];
            //customersList = new Ccustomer[jarray.length()];
            for (int i = 0; i < jarray.length(); i++) {
                //JSONObject object = jarray.getJSONObject(i);
                String FID = jarray.getJSONObject(i).getString("FID");
                String CID = jarray.getJSONObject(i).getString("CID");
                String pageTitle = jarray.getJSONObject(i).getString("pageTitle");
                String pageUrl = jarray.getJSONObject(i).getString("pageUrl");

                Favorite c = new Favorite(FID, CID, pageTitle, pageUrl);
                FavoriteList[i] = c;
            }
        } catch (JSONException e1) {
            helper.LogPrintExStackTrace(e1);
            e1.printStackTrace();
        }

        final int N = FavoriteList.length; // total number of textviews to add

        //final TextView[] myTextViews = new TextView[N]; // create an empty array;

        for (final Favorite f : FavoriteList) {
            //Log.e("mytag",f.toString());
            final TextView rowTextView = new TextView(getContext());
            rowTextView.setText(f.getPageTitle());
            rowTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            rowTextView.setTextSize(20);
            //rowTextView.setHeight(30);

            // add the textview to the linearlayout
            layout.addView(rowTextView);
            rowTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ActivityWebView.class);
                    Bundle b = new Bundle();
                    b.putInt("callid", -1);
                    b.putInt("cid", -1);
                    b.putInt("technicianid", Integer.parseInt(String.valueOf(DatabaseHelper.getInstance(getContext()).getValueByKey("CID"))));
                    b.putString("action", "dynamic");
                    b.putString("specialurl", f.getPageUrl());
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }


        return FavoriteList;
    }


}
