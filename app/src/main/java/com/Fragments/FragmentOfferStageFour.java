package com.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.MenuActivity;
import com.Activities.R;
import com.Classes.Ccustomer;
import com.Classes.Product;
import com.Helper;
import com.Icon_Manager;
import com.model.Model;

import java.util.ArrayList;

/**
 * The fragment represents the fourth stage of an offer page.
 */
public class FragmentOfferStageFour extends Fragment {

    private View view;
    private Context context;
    private Icon_Manager iconManager;
    private TextView downloadLink;
    private TextView thumbImage,offer_stage_four_offer_number_label;

    private Button updateEmailButton;
    private TextView sendIcon;
    private Helper h;

    public FragmentOfferStageFour() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_offer_stage_four, container, false);
        h = new Helper();
        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);
        offer_stage_four_offer_number_label = (TextView) view.findViewById(R.id.offer_stage_four_offer_number_label);
        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();
        this.context = getContext();
        this.iconManager = new Icon_Manager();
        //Assign data fields.
        this.assignDataFields();
        //Set the icons.
        this.setIcons();
        //Set download file onClick listener.
        this.downloadLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO download the actual pdf file
                downloadLink.setTextColor(Color.BLUE);
                Toast.makeText(context, "מוריד קובץ", Toast.LENGTH_SHORT).show();
            }
        });

        //Set update email button onClick listener.
        this.updateEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO update email
                Toast.makeText(context, "דואר אלקטרוני עודכן", Toast.LENGTH_SHORT).show();
            }
        });

        //Next stage onClickListener.
        ConstraintLayout nextStageButton = (ConstraintLayout) view.findViewById(R.id.offer_stage_four_constraintLayout_save);
        nextStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO implement
            }
        });
        getAllParameters();
        return view;
    }
    public void getAllParameters(){
        FragmentManager fm = getFragmentManager();

        FragmentOfferStageOne fragment = (FragmentOfferStageOne)fm.findFragmentByTag("FragmentOfferStageOne");
        FragmentOfferStageTwo fragment2 = (FragmentOfferStageTwo)fm.findFragmentByTag("FragmentOfferStageTwo");
        FragmentOfferStageThree fragment3 = (FragmentOfferStageThree)fm.findFragmentByTag("FragmentOfferStageThree");
        Ccustomer c = fragment.getClientDetails();
        ArrayList<Product> products = fragment2.getProductsList();
        String comment = fragment3.getComment();
        //Toast.makeText(getContext(), c.toString(), Toast.LENGTH_SHORT).show();
        //for (Product ss:s) {
        //Toast.makeText(getContext(), ss.getPname()+" "+ss.getPID()+" "+ss.getPmakat()+" "+ss.getPprice() , Toast.LENGTH_SHORT).show();//
        //}
        //Toast.makeText(getContext(), comment, Toast.LENGTH_SHORT).show();
        String json = "";
        json += "{";
        json += "'items': {";
        json +=    "'product': [";
        for (Product p:products) {
            json += "{";
            json += "'PID': '"+ p.getPID() +"',";
            json += "'Pstock': '"+ p.getPstock() +"',";
            json += "'Pprice': '"+ p.getPprice() +"'";
            json += "},";
        }
        json = json.substring(0, json.lastIndexOf(","));
        json +=              "],";
        json +=     "'client': [{";
        json +=              "'CID': '"+ c.getCID() +"',";
        json +=              "'Ccompany': '"+ c.getCcompany() +"',";
        json +=              "'Cemail': '"+ c.getCemail() +"',";
        json +=              "'Cphone': '"+ c.getCphone() +"',";
        json +=              "'Ccell': '"+ c.getCcell() +"',";
        json +=              "'Ccity': '"+ c.getCcity() +"',";
        json +=              "'Caddress': '"+ c.getCaddress() +"',";
        json +=              "'CcID': '"+ c.getCcID() +"',";
        json +=              "'Cusername': '"+ c.getCusername() +"',";
        json = json.substring(0, json.lastIndexOf(","));
        json +=              "}],";
        json +=     "'comment':[{'comment':'" + comment + "'}]";
        json +=    "}";
        json += "}";



        Model.getInstance().Async_Wz_Json(h.getMacAddr(getContext()), json, "newLead", new Model.Wz_Json_Listener() {
            @Override
            public void onResult(String str) {
                Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                offer_stage_four_offer_number_label.setText(offer_stage_four_offer_number_label.getText() + " " + str);
            }
        });
    }


    /**
     * Assigns all the data fields to class members for future usage.
     */
    private void assignDataFields() {
        this.downloadLink = (TextView) view.findViewById(R.id.offer_stage_four_download_link);
        this.downloadLink.setPaintFlags(downloadLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        this.updateEmailButton = (Button)view.findViewById(R.id.offer_stage_four_update_button);
    }

    private void setIcons() {

        this.thumbImage = (TextView) view.findViewById(R.id.offer_stage_four_thumb_image);
        thumbImage.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        thumbImage.setTextSize(100);

        this.sendIcon = (TextView) view.findViewById(R.id.offer_stage_four_save_icon);
        this.sendIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        this.sendIcon.setTextSize(30);
    }
}
