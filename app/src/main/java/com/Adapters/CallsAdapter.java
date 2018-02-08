package com.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.*;
import com.Classes.*;

import com.DatabaseHelper;
import com.Fragments.*;

import com.Helper;
import com.Icon_Manager;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by WIZE02 on 23/05/2017.
 */

/**
 * the position of adapter is to set the content into listview
 * with the paramters we pass it.
 *  ccustomerArrayList - is the list of customers
 *  ctx - the context we must pass it (relationship between classes and fragments/activities)
 *  Filterable is the additional built in interface that's allow us to implement the filter edit text
 */
public class CallsAdapter extends BaseAdapter implements Filterable {
    private Dialog WebDialog1;
    private WebView URL1;
    TextView myccell,mycphone;
    Context c;
    TextView edit,mobile,sign,location,telephone,parts, goToSms,goToCustomers;
    Helper helper;
    ArrayList<Call> callsArrayList;
    CustomFilter filter;
    ArrayList<Call> filterList;
    public CallsAdapter(ArrayList<Call> callsArrayList, Context ctx) {
        this.c=ctx;
        this.callsArrayList=callsArrayList;
        this.filterList=callsArrayList;
    }
    @Override
    public int getCount() {
        return this.callsArrayList.size();
    }
    @Override
    public Object getItem(int pos) {
        return callsArrayList.get(pos);
    }
    @Override
    public long getItemId(int pos) {
        return callsArrayList.indexOf(getItem(pos));
    }
    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {

        helper = new Helper();
        Icon_Manager icon_manager;
        icon_manager = new Icon_Manager();
        LayoutInflater inflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.call_item, null);
            convertView.getTag(pos);
        }
        TextView stateid=(TextView) convertView.findViewById(R.id.stateid);
        stateid.setTextColor(Color.parseColor("#32CD32"));

        TextView txtsubject=(TextView) convertView.findViewById(R.id.txtsubject);
        TextView txtCreateDate=(TextView) convertView.findViewById(R.id.txtcreatedate);
        TextView txtcallid=(TextView) convertView.findViewById(R.id.txtcallid);
        TextView txtstatusname=(TextView) convertView.findViewById(R.id.txtstatusname);
        TextView txtCcompany=(TextView) convertView.findViewById(R.id.txtcname);
        TextView txtCcity=(TextView) convertView.findViewById(R.id.txtccity);
        TextView txtCallStartTime=(TextView) convertView.findViewById(R.id.txtcallstarttime1);
        LinearLayout assigmentlayout=(LinearLayout) convertView.findViewById(R.id.assigmentlayout);

        TextView asterisk=(TextView) convertView.findViewById(R.id.asterisk);
        //###################### TELEPHONE #############################
        telephone= (TextView) convertView.findViewById(R.id.telephone);
        edit = (TextView) convertView.findViewById(R.id.edit);
        mobile = (TextView) convertView.findViewById(R.id.mobile);
        sign = (TextView) convertView.findViewById(R.id.sign);
        location = (TextView) convertView.findViewById(R.id.location);

        //Log.e("mytag",callsArrayList.get(pos).getState().toString());
//        mobile.setBackgroundResource(R.drawable.btn_circle2);
//        telephone.setBackgroundResource(R.drawable.btn_circle2);
//        sign.setBackgroundResource(R.drawable.btn_circle2);
       // asterisk.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",c));
        //asterisk.setTextSize(30);
         telephone.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",c));
        telephone.setTextSize(30);
        mobile.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",c));
        mobile.setTextSize(30);

        //mobile.setBackgroundColor(Color.parseColor("#E94e1b"));
        sign.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",c));
        sign.setTextSize(30);
        location.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",c));
        location.setTextSize(30);




        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //helper.goToCallDetailsFragNew(c,String.valueOf(callsArrayList.get(pos).getCallID()));
                //helper.goToCallDetailsFrag(c,String.valueOf(callsArrayList.get(pos).getCallID()));
                //bundle.putString("receiver", dataName);

                Intent intent = new Intent(c, ActivityCallDetails.class);
                intent.putExtra("EXTRA_SESSION_ID", String.valueOf(callsArrayList.get(pos).getCallID()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(intent);
            }
        });
        convertView.setTag(convertView.getId(),pos);
        convertView.getTag(pos);

        edit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(c,"ccell:" + callsArrayList.get(pos).getCcell().replace("null","") +
                        "\ncphone:" + callsArrayList.get(pos).getCphone().replace("null","")+
                        "\ncontctCcell:" + callsArrayList.get(pos).getContctCell().replace("null","") +
                        "\ncontctCphone:" + callsArrayList.get(pos).getContctPhone().replace("null","")
                        , Toast.LENGTH_LONG).show();
                return true;
            }
        });
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m = "";

                if (!(callsArrayList.get(pos).getCcell().trim().contains("null")) && !(callsArrayList.get(pos).getCcell().trim().equals(""))){
                    m = callsArrayList.get(pos).getCcell().trim();
                }
                if (!(callsArrayList.get(pos).getContctCell().trim().contains("null")) && !(callsArrayList.get(pos).getContctCell().trim().equals(""))){
                    m =callsArrayList.get(pos).getContctCell().trim();
                }

                if (m.equals("") ){
                    Toast.makeText(c,"no cell", Toast.LENGTH_LONG).show();
                }else{
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + m));//String.valueOf(callsArrayList.get(pos).getCallID())
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.startActivity(callIntent);
                }
            }
        });


        telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m = "";

                if (!(callsArrayList.get(pos).getCphone().trim().contains("null")) && !(callsArrayList.get(pos).getCphone().trim().equals(""))){
                    m = callsArrayList.get(pos).getCphone().trim();
                }
                if (!(callsArrayList.get(pos).getContctPhone().trim().contains("null")) && !(callsArrayList.get(pos).getContctPhone().trim().equals(""))){
                    m =callsArrayList.get(pos).getContctPhone().trim();
                }
                if (m.equals("") ){
                    Toast.makeText(c,"no phone", Toast.LENGTH_LONG).show();
                }else{
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + m));//String.valueOf(callsArrayList.get(pos).getCallID())
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.startActivity(callIntent);
                }
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    String url = DatabaseHelper.getInstance(c).getValueByKey("URL") + "/modulesSign/sign.aspx?callID=" + String.valueOf(callsArrayList.get(pos).getCallID());

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.startActivity(browserIntent);
                    //AlertDialogWeb(String.valueOf(callsArrayList.get(pos).getCallID()));
                }
                catch ( ActivityNotFoundException ex  )
                {
                    Toast.makeText(c, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    // Launch Waze to look for Hawaii:
                    String url = "waze://?q=" + callsArrayList.get(pos).getCaddress() + " " + callsArrayList.get(pos).getCcity();
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.startActivity( intent );
                }
                catch ( ActivityNotFoundException ex  )
                {
                    // If Waze is not installed, open it in Google Play:
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
                    c.startActivity(intent);
                }
            }
        });

        //#################    TEXTVIEW    ##########################

        txtsubject.setText(callsArrayList.get(pos).getSubject());
        txtCreateDate.setText(callsArrayList.get(pos).getCreateDate().substring(0,10) + " | " + callsArrayList.get(pos).getCreateDate().substring(11,16));
        txtcallid.setText("קריאה: " +String.valueOf(callsArrayList.get(pos).getCallID()));
        txtstatusname.setText(callsArrayList.get(pos).getStatusName());
        txtCcompany.setText(callsArrayList.get(pos).getCcompany());
        txtCcity.setText(callsArrayList.get(pos).getCcity());


        String type = callsArrayList.get(pos).getState().toString().trim();
        if (!type.contains("null")){
            stateid.setText(getType(type.trim()));
        }else{
            stateid.setText("");
        }
        stateid.setTypeface(stateid.getTypeface(), Typeface.BOLD);

        TextView alert = (TextView) convertView.findViewById(R.id.alert);
        alert.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",c));
        alert.setTextSize(30);
        alert.setTextColor(Color.parseColor("#FF0000"));
        if (!callsArrayList.get(pos).getPriorityID().toLowerCase().contains("h")){
            alert.setVisibility(View.GONE);
        }

        txtCallStartTime.setText(callsArrayList.get(pos).getCallStartTime());
        if ((callsArrayList.get(pos).getCallStartTime().toLowerCase().contains("null"))){
            assigmentlayout.setVisibility(View.GONE);
        }else{
            txtCallStartTime.setText(callsArrayList.get(pos).getCallStartTime().substring(0,10)+"  " +callsArrayList.get(pos).getCallStartTime().substring(11,16)+"-"+callsArrayList.get(pos).getCallEndTime().substring(11,16));;
            txtCallStartTime.setTextColor(Color.parseColor("#E94E1B"));
        }




        return convertView;
    }
    public boolean isNumeric(String s) {
        int len = s.length();
        for (int i = 0; i < len; ++i) {
            if (!isNumeric1(String.valueOf(s.charAt(i)))) {
                return false;
            }
        }

        return true;
    }
    public boolean isNumeric1(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }
    private String retCcell(String cell,String contctCell){
        String ret = "";
        //Log.e("mytag","contctCell.length():" + contctCell.length() + " contctCell.contains(null):" + contctCell.contains("null"));
        //Log.e("mytag","cell.length():" + cell.length() + " cell.contains(null):" + cell.contains("null"));

        if ((contctCell.length() > 0 && !(contctCell.contains("null")))){
            return contctCell;
        }
        if ((cell.length() > 0 && !(cell.contains("null")))){
            return cell;
        }
        return ret;
    }

    private String retCphone(String cphone,String contctcphone){
        String ret = "";
        //Log.e("mytag","contctcphone.length():" + contctcphone.length() + " contctcphone.contains(null):" + contctcphone.contains("null"));
        //Log.e("mytag","cphone.length():" + cphone.length() + " cphone.contains(null):" + cphone.contains("null"));
        if ((contctcphone.length() > 0 && !(contctcphone.contains("null")))){
            return contctcphone;
        }
        if ((cphone.length() > 0 && !(cphone.contains("null")))){
            return cphone;
        }
        return ret;
    }

    private String getType(String type){
        String ret = "";
        if (type.contains("work")){
            ret = "עבודה";
        }else if(type.contains("ride")){
            ret = "נסיעה";
        }
        return ret;
    }
    @Override
    public Filter getFilter() {
        if(filter == null)
        {
            /**
             * call the filter class to return the correct filtered list
             */
            filter=new CustomFilter();
        }
        return filter;
    }

    //INNER CLASS
    class CustomFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint != null && constraint.length()>0)
            {
                //CONSTARINT TO UPPER
                constraint=constraint.toString().toUpperCase();
                ArrayList<Call> filters=new ArrayList<Call>();
                //get specific items

                for(int i=0;i<filterList.size();i++)
                {
                    if(filterList.get(i).getSubject().contains(constraint)||
                            String.valueOf(filterList.get(i).getCallID()).contains(constraint)||
                            String.valueOf(filterList.get(i).getCcompany()).contains(constraint)||
                            String.valueOf(filterList.get(i).getCname()).contains(constraint))
                    {
                        Call p=new Call(
                                filterList.get(i).getCallID(),
                                filterList.get(i).getAID(),
                                filterList.get(i).getCID(),
                                filterList.get(i).getCreateDate(),
                                filterList.get(i).getStatusID(),
                                filterList.get(i).getCallPriority(),
                                filterList.get(i).getSubject(),
                                filterList.get(i).getComments(),
                                filterList.get(i).getCallUpdate(),
                                filterList.get(i).getCntrctDate(),
                                filterList.get(i).getTechnicianID(),
                                filterList.get(i).getStatusName(),
                                filterList.get(i).getInternalSN(),
                                filterList.get(i).getPmakat(),
                                filterList.get(i).getPname(),
                                filterList.get(i).getContractID(),
                                filterList.get(i).getCphone(),
                                filterList.get(i).getOriginID(),
                                filterList.get(i).getProblemTypeID(),
                                filterList.get(i).getCallTypeID(),
                                filterList.get(i).getPriorityID(),
                                filterList.get(i).getOriginName(),
                                filterList.get(i).getProblemTypeName(),
                                filterList.get(i).getCallTypeName(),
                                filterList.get(i).getCname(),
                                filterList.get(i).getCemail(),
                                filterList.get(i).getContctCode(),
                                filterList.get(i).getCallStartTime(),
                                filterList.get(i).getCallEndTime(),
                                filterList.get(i).getCcompany(),
                                filterList.get(i).getClocation(),
                                filterList.get(i).getCallOrder(),
                                filterList.get(i).getCaddress(),
                                filterList.get(i).getCcity(),
                                filterList.get(i).getCcomments(),
                                filterList.get(i).getCfname(),
                                filterList.get(i).getClname(),
                                filterList.get(i).getTechName(),
                                filterList.get(i).getAname(),
                                filterList.get(i).getContctName(),
                                filterList.get(i).getContctAddress(),
                                filterList.get(i).getContctCity(),
                                filterList.get(i).getContctCell(),
                                filterList.get(i).getContctPhone(),
                                filterList.get(i).getCcity(),
                                filterList.get(i).getCcell(),
                                filterList.get(i).getTechColor(),
                                filterList.get(i).getContctCemail(),
                                filterList.get(i).getCallParentID(),
                                filterList.get(i).getState()
                        );

                        filters.add(p);
                    }
                }
                results.count=filters.size();
                results.values=filters;
            }else
            {
                results.count=filterList.size();
                results.values=filterList;
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            /**
             * what are you going to see in the results
             */
            callsArrayList=(ArrayList<Call>) results.values;
            //update the listview
            notifyDataSetChanged();
        }
    }

    public void AlertDialogWeb(String callid){
//        AlertDialog.Builder alert = new AlertDialog.Builder(c);
//        alert.setTitle("Title here");
//
//        WebView wv = new WebView(c);
//        wv.loadUrl("http:\\www.google.com");
//        wv.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//
//                return true;
//            }
//        });
//
//        alert.setView(wv);
//        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.dismiss();
//            }
//        });
//        alert.show();
//        Dialog dialog2 = new Dialog(c, R.style.DialogTheme);
//        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog2.getWindow().setBackgroundDrawable(null);
//        dialog2.setContentView(R.layout.activity_main);
//        WindowManager.LayoutParams lp = dialog2.getWindow().getAttributes();
//        Window window = dialog2.getWindow();
//        lp.copyFrom(window.getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//        window.setAttributes(lp);
//        lp.gravity = Gravity.CENTER;


//        final ImageView imgprofile=(ImageView)dialog2.findViewById(R.id.img_centre);
//        Picasso.with(context)
//                .load(arrayImages.get(position).get("image"))
//                .resize(800,1000)
//                .centerInside()
//                .into(imgprofile, new Callback() {
//
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onError() {
//                        imgprofile.setImageResource(R.drawable.user);
//                    }
                //});
        //dialog2.show();
        //Dialog dialog = new Dialog(this.c.getApplicationContext());
        //AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog.Builder alert = new AlertDialog.Builder(c.getApplicationContext());
        alert.setTitle("Title here");
        WebView wv = new WebView(this.c.getApplicationContext());
        String url = DatabaseHelper.getInstance(c).getValueByKey("URL") + "/modulesSign/sign.aspx?callID=" + String.valueOf(callid);
        //String url = DatabaseHelper.getInstance(c).getValueByKey("URL") + "/iframe.aspx?control=/modulesServices/CallsFiles&CallID=" + callid + "&class=CallsFiles_appCell&mobile=True";

        wv.loadUrl(url);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }



}