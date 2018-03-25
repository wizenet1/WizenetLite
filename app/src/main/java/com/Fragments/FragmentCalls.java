//package com.Fragments;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.graphics.Color;
//import android.graphics.PorterDuff;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.Activities.R;
//import com.Classes.Call;
//import com.DatabaseHelper;
//import com.Adapters.CallsAdapter;
//import com.Helper;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by User on 31/08/2016.
// */
//public class FragmentCalls extends android.support.v4.app.Fragment {
//
//    DatabaseHelper db;
//    ListView myList;
//    LocationManager manager = null;
//    boolean result = false;
//    private EditText mSearchEdt;
//    CallsAdapter callsAdapter; //to refresh the list
//    ArrayList<Call> data2 = new ArrayList<Call>() ;
//    private TextWatcher mSearchTw;
//
//    String myString;
//
//
//
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            View v = inflater.inflate(R.layout.calls_fragment, null);
//            //        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                String u = data2.get(position).toString();
////                //Toast.makeText(getActivity(), "item click " + u, Toast.LENGTH_LONG).show();
////                alertDialog(data2.get(position));
////                dataName = "";
////            }
////        });
//            mSearchEdt = (EditText) v.findViewById(R.id.mSearchEdt);
//            //initUI
//            Helper helper = new Helper();
//            String mac = helper.getMacAddr();
//
//            data2.clear();
//            for (Call c : getCallsList()){
//                data2.add(c);
//            }
//            myList = (ListView) v.findViewById(R.id.calls_list);
//
//            callsAdapter=new CallsAdapter(data2,getContext());
//            myList.setAdapter(callsAdapter);
//            mSearchTw=new TextWatcher() {
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                    callsAdapter.getFilter().filter(s);
//                }
//
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count,
//                                              int after) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//
//                }
//            };
//            mSearchEdt.addTextChangedListener(mSearchTw);
//            return v;
//        };
//
//        private List<Call> getCallsList(){
//            JSONObject j = null;
//            int length = 0;
//
//            List<Call> calls = new ArrayList<Call>() ;
//            try {
//                calls= DatabaseHelper.getInstance(getContext()).getCalls("");
//                length = calls.size();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//            return calls;
//        }
//
//
//
//
//}
