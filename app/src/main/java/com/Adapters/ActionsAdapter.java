package com.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.ActivityCallDetails;
import com.Activities.R;
import com.Classes.Call;
import com.Classes.IS_Action;
import com.Classes.IS_ActionTime;
import com.DatabaseHelper;
import com.Helper;
import com.Icon_Manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * the position of adapter is to set the content into listview
 * with the paramters we pass it.
 *  ccustomerArrayList - is the list of customers
 *  ctx - the context we must pass it (relationship between classes and fragments/activities)
 *  Filterable is the additional built in interface that's allow us to implement the filter edit text
 */
public class ActionsAdapter extends BaseAdapter implements Filterable {
    private Dialog WebDialog1;
    private WebView URL1;
    TextView myccell,mycphone;
    Context c;
    TextView edit,mobile,sign,location,telephone,parts, goToSms,goToCustomers;
    Helper helper;
    ArrayList<IS_Action> callsArrayList;
    CustomFilter filter;
    ArrayList<IS_Action> filterList;
    TextView btn_play,btn_stop;
    Icon_Manager icon_manager;
    //LinearLayout layout_details;
    public ActionsAdapter(List<IS_Action> callsArrayList, Context ctx) {
        this.c=ctx;
        this.callsArrayList= (ArrayList<IS_Action>) callsArrayList;
        this.filterList= (ArrayList<IS_Action>) callsArrayList;
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
    public class ViewHolder {
        //Spinner spNames, spGrades;
        TextView is_actiontxt,is_desc,is_comments,btn_open_details;
        //LinearLayout layout_details;
    }
    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        helper = new Helper();
         icon_manager = new Icon_Manager();

        LayoutInflater inflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout layout_details;
        final TextView btn_open_details;
       // holder = new ViewHolder();
        if(convertView==null)
        {

            convertView=inflater.inflate(R.layout.is_action, null);
            convertView.getTag(pos);
            //convertView.setTag(callsArrayList.get(pos));
//            holder.is_actiontxt = (TextView) convertView.findViewById(R.id.is_actionid);
//            holder.is_desc = (TextView) convertView.findViewById(R.id.is_desc);
//            holder.is_comments = (TextView) convertView.findViewById(R.id.is_comments);
//            holder.btn_open_details = (TextView) convertView.findViewById(R.id.btn_open_details);
//            holder.layout_details = (LinearLayout) convertView.findViewById(R.id.layout_details);
//            holder.is_actiontxt.setText(String.valueOf(callsArrayList.get(pos).getActionID()) );
//            holder.is_desc.setText(String.valueOf(callsArrayList.get(pos).getActionDesc()) );
//            holder.is_comments.setText(String.valueOf(callsArrayList.get(pos).getComments()) );
//            holder.btn_open_details.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (holder.layout_details.getVisibility() == View.VISIBLE){
//                        holder.layout_details.setVisibility(View.GONE);
//                        holder.btn_open_details.setText("˅");
//                    }else{
//                        holder.layout_details.setVisibility(View.VISIBLE);
//                        holder.btn_open_details.setText("˄");
//                    }
//                }
//            });

        }
        TextView is_actiontxt = (TextView) convertView.findViewById(R.id.is_actionid);
        TextView is_comments = (TextView) convertView.findViewById(R.id.is_comments);
        btn_play = (TextView) convertView.findViewById(R.id.btn_play);
        btn_stop = (TextView) convertView.findViewById(R.id.btn_stop);

        btn_open_details = (TextView) convertView.findViewById(R.id.btn_open_details);

        layout_details=(LinearLayout) convertView.findViewById(R.id.layout_details);;
        TextView is_desc =(TextView) convertView.findViewById(R.id.is_desc);
        is_comments.setText(String.valueOf(callsArrayList.get(pos).getComments()) );
        layout_details.setTag(callsArrayList.get(pos).getActionID());

        btn_open_details.setTag(callsArrayList.get(pos).  getActionID());

        is_actiontxt.setText(String.valueOf(callsArrayList.get(pos).getActionID()) );
        is_desc.setText(callsArrayList.get(pos).getActionDesc());
        set_play(callsArrayList.get(pos).getActionID());
        set_stop(callsArrayList.get(pos).getActionID());


        //"@color/wizenetColor"
        btn_open_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("mytag","Button Tab at Pos "+pos+" "+btn_open_details.getTag()+"");
                if (layout_details.getVisibility() == View.VISIBLE){
                    layout_details.setVisibility(View.GONE);
                    btn_open_details.setText("˅");
                }else{
                    layout_details.setVisibility(View.VISIBLE);
                    btn_open_details.setText("˄");
                };
            }
        });
        convertView.setTag(convertView.getId(),pos);
        convertView.getTag(pos);

//        txtsubject.setText(callsArrayList.get(pos).getSubject());
//        txtCreateDate.setText(callsArrayList.get(pos).getCreateDate().substring(0,10) + " | " + callsArrayList.get(pos).getCreateDate().substring(11,16));
//        txtcallid.setText("קריאה: " +String.valueOf(callsArrayList.get(pos).getCallID()));
//        txtstatusname.setText(callsArrayList.get(pos).getStatusName());
//        txtCcompany.setText(callsArrayList.get(pos).getCcompany());
//        txtCcity.setText(callsArrayList.get(pos).getCcity());
//




        return convertView;
    }
    private void set_play(final int actionID){
        btn_play.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",c));
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("mytag","play actionID clicked: "+actionID);
                //String date1 = ct.getCallStartTime();
                //String fromdateinmillis = String.valueOf(stringToDate(date1,"yyyy-MM-dd HH:mm:ss").getTime());
                String currentDateTimeString = String.valueOf((new Date().getTime()));
                DatabaseHelper.getInstance(c).add_ISActionTime(new IS_ActionTime("-1","-1",String.valueOf(actionID),currentDateTimeString,null));
            }
        });
    }
    private void set_stop(final int actionID){
        btn_stop.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",c));
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("mytag","stop actionID clicked: "+actionID);
            }
        });
//        String date1 = ct.getCallStartTime();
//        String fromdateinmillis = String.valueOf(stringToDate(date1,"yyyy-MM-dd HH:mm:ss").getTime());
//        String currentDateTimeString = String.valueOf((new Date().getTime()));
//        int dateDifference = (int) helper.getDateDiff(fromdateinmillis, currentDateTimeString);

        //ct.setMinute(String.valueOf(Integer.valueOf(dateDifference)+1));
        //DatabaseHelper.getInstance(getApplicationContext()).update_calltime(ct);
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
                ArrayList<IS_Action> filters=new ArrayList<IS_Action>();
                //get specific items
                //||
                //String.valueOf(filterList.get(i).getCallID()).contains(constraint)||
                //String.valueOf(filterList.get(i).getCcompany()).contains(constraint)||
                //String.valueOf(filterList.get(i).getCname()).contains(constraint)
                for(int i=0;i<filterList.size();i++)
                {
                    if(String.valueOf(filterList.get(i).getActionID()).contains(constraint)
                            )
                    {//filterList.get(i).
                        IS_Action action=new IS_Action(
                                filterList.get(i).getActionID(),
                                filterList.get(i).getTaskID(),
                                filterList.get(i).getActionDate(),
                                filterList.get(i).getActionStartDate(),
                                filterList.get(i).getActionDue(),
                                filterList.get(i).getActionDesc(),
                                filterList.get(i).getComments(),
                                filterList.get(i).getPriorityID(),
                                filterList.get(i).getStatusID(),
                                filterList.get(i).getReminderID(),
                                filterList.get(i).getOwnerID(),
                                filterList.get(i).getUserID(),
                                filterList.get(i).getWorkHours(),
                                filterList.get(i).getWorkEstHours(),
                                filterList.get(i).getCreate(),
                                filterList.get(i).getLastUpdate(),
                                filterList.get(i).getActionLink(),
                                filterList.get(i).getDepID(),
                                filterList.get(i).getActionRef(),
                                filterList.get(i).getUserCfname(),
                                filterList.get(i).getUserClname(),
                                filterList.get(i).getUserCemail(),
                                filterList.get(i).getUserCtypeID(),
                                filterList.get(i).getOwnerCfname(),
                                filterList.get(i).getOwnerClname(),
                                filterList.get(i).getOwnerCemail(),
                                filterList.get(i).getOwnerCtypeID(),
                                filterList.get(i).getProjectID(),
                                filterList.get(i).getStatusName(),
                                filterList.get(i).getPriorityName(),
                                filterList.get(i).getActionType(),
                                filterList.get(i).getActionSdate(),
                                filterList.get(i).getActionEdate(),
                                filterList.get(i).getWorkHoursM(),
                                filterList.get(i).getWorkEstHoursM(),
                                filterList.get(i).getActionPrice(),
                                filterList.get(i).getStatusColor(),
                                filterList.get(i).getTaskSummery(),
                                filterList.get(i).getProjectSummery(),
                                filterList.get(i).getProjectType(),
                                filterList.get(i).getActionNum(),
                                filterList.get(i).getActionFrom(),
                                filterList.get(i).getActionDays(),
                                filterList.get(i).getParentActionID(),
                                filterList.get(i).getRemindertime(),
                                filterList.get(i).getExpr1(),
                                filterList.get(i).getProjectDesc()
                        );

                        filters.add(action);
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
            callsArrayList=(ArrayList<IS_Action>) results.values;
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