package com.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.R;
import com.Classes.IS_Action;
import com.Classes.IS_ActionTime;
import com.DatabaseHelper;
import com.Fragments.FragmentActions;
import com.Fragments.FragmentActions2;
import com.Helper;
import com.Icon_Manager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * the position of adapter is to set the content into listview
 * with the paramters we pass it.
 *  ccustomerArrayList - is the list of customers
 *  ctx - the context we must pass it (relationship between classes and fragments/activities)
 *  Filterable is the additional built in interface that's allow us to implement the filter edit text
 */
public class ActionsAdapter2 extends BaseAdapter implements Filterable {
    private Dialog WebDialog1;
    private WebView URL1;
    TextView myccell,mycphone;
    Context c;
    TextView edit;
    Helper helper;
    ArrayList<IS_Action> callsArrayList;
    CustomFilter filter;
    ArrayList<IS_Action> filterList;
    TextView btn_play,btn_stop,txtcreatedate,txt_owner,txt_user,txt_destination,txt_assignment;
    Icon_Manager icon_manager;
    FragmentActions2 fragment;
    Spinner spinner;
    String update_action_id,update_status_id;
    boolean flag ;
    Button btn_update_status;
    //LinearLayout layout_details;
    public ActionsAdapter2(List<IS_Action> callsArrayList, Context ctx, FragmentActions2 fragmentActions) {
        this.c=ctx;
        this.callsArrayList= (ArrayList<IS_Action>) callsArrayList;
        this.filterList= (ArrayList<IS_Action>) callsArrayList;
        this.fragment = fragmentActions;
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
        flag = false;
        LayoutInflater inflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.is_action, null); /////// this is solve the problem!!
        final LinearLayout layout_details;
        final TextView btn_open_details;
       // holder = new ViewHolder();
        if(convertView==null)
        {
            //convertView=inflater.inflate(R.layout.is_action, null);
            //convertView.getTag(pos);
        }
        txt_owner = (TextView) convertView.findViewById(R.id.txt_owner);
        txt_user = (TextView) convertView.findViewById(R.id.txt_user);
        txt_destination= (TextView) convertView.findViewById(R.id.txt_destination);
        txt_assignment= (TextView) convertView.findViewById(R.id.txt_assignment);
        TextView is_actiontxt = (TextView) convertView.findViewById(R.id.is_actionid);
        //TextView is_comments = (TextView) convertView.findViewById(R.id.is_comments);
        edit = (TextView) convertView.findViewById(R.id.edit);
        btn_play = (TextView) convertView.findViewById(R.id.btn_play);
        btn_stop = (TextView) convertView.findViewById(R.id.btn_stop);
        //btn_update_status = (Button) convertView.findViewById(R.id.btn_update_status);
        txtcreatedate = (TextView) convertView.findViewById(R.id.txtcreatedate);
        spinner = (Spinner) convertView.findViewById(R.id.spinner);
        //btn_open_details = (TextView) convertView.findViewById(R.id.btn_open_details);
        layout_details=(LinearLayout) convertView.findViewById(R.id.layout_details);;
        TextView is_desc =(TextView) convertView.findViewById(R.id.is_desc);

        String result = "";
        try {
            String string = callsArrayList.get(pos).getComments();
            byte[] utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
            //Log.e("mytag", (String.valueOf(string)));
            result= String.valueOf(string);
            //is_comments.setText(Html.fromHtml(String.valueOf(string)), TextView.BufferType.SPANNABLE);
            //is_comments.setText(Html.fromHtml(String.valueOf(string) ));

        } catch (UnsupportedEncodingException e) {
            helper.LogPrintExStackTrace(e);
        }
        setEdit(result);
        txt_destination.setText(callsArrayList.get(pos).getActionDate().substring(0,10));
        //txt_assignment.setText(callsArrayList.get(pos).getActionSdate().substring(0,10));
        txt_owner.setText(callsArrayList.get(pos).getOwnerCfname()+" "+callsArrayList.get(pos).getOwnerClname());
        txt_user.setText(callsArrayList.get(pos).getUserCfname()+" "+callsArrayList.get(pos).getUserClname());

        txtcreatedate.setText(callsArrayList.get(pos).getCreate());
        //is_comments.setText(String.valueOf(callsArrayList.get(pos).getComments()) );
        setStatusSpinner(callsArrayList.get(pos).getStatusName(),String.valueOf(callsArrayList.get(pos).getActionID()));

        layout_details.setTag(callsArrayList.get(pos).getActionID());
        //btn_open_details.setTag(callsArrayList.get(pos).getActionID());
        is_actiontxt.setText(String.valueOf(callsArrayList.get(pos).getActionID()) );
        is_desc.setText(callsArrayList.get(pos).getActionDesc());
        is_desc.setTypeface(is_desc.getTypeface(), Typeface.BOLD_ITALIC);
        is_desc.setTextSize(20);
        setBtn_update_status();
        //chk_if_play(callsArrayList.get(pos).getActionID());
        set_play(callsArrayList.get(pos).getActionID());
        set_stop(callsArrayList.get(pos).getActionID());
        btn_play.setTag(pos);
        btn_stop.setTag(pos);



        convertView.setTag(convertView.getId(),pos);
        convertView.getTag(pos);

        return convertView;
    }
    private void setBtn_update_status(){

        btn_update_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (helper.isNetworkAvailable(c)){
                     //Model.getInstance().Async_Wz_Update_Action_Field_Listener(helper.getMacAddr(c), update_action_id, "statusID", update_status_id, new Model.Wz_Update_Action_Field_Listener() {
                     //    @Override
                     //    public void onResult(String str) {
                     //        Log.e("mytag","status changed");
                     //        fragment.returnListAndRefresh();
                     //        //fragment.refresh();
                     //    }
                     //});
                     AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(c);
                    // ...Irrelevant code for customizing the buttons and title
                     //LayoutInflater inflater = c.getLayoutInflater();
                     LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                     View dialogView = inflater.inflate(R.layout.change_status, null);
                     dialogBuilder.setView(dialogView);

                     //EditText editText = (EditText) dialogView.findViewById(R.id.label_field);
                     //editText.setText("test label");
                     AlertDialog alertDialog = dialogBuilder.create();
                     alertDialog.show();
                 }else{
                     Toast.makeText(c, "offline - no internet", Toast.LENGTH_LONG).show();
                 }
                 }
                });




    }
    private void setEdit(final String html){
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(c);
                //alert.setTitle("title");
                WebView wv = new WebView(c);
                wv.loadData("<html><body style='direction: rtl;'>" + html + "</body></html>", "text/html; charset=UTF-8", null);
                wv.setWebViewClient(new WebViewClient()
                {
                    public boolean shouldOverrideUrlLoading(WebView view, String url)
                    {
                        view.loadUrl(url);
                        return true;
                    }
                });
                alert.setView(wv);
                alert.setNegativeButton("סגור", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });


    }
    private void setStatusSpinner(String statName, final String actionid){
//        int i = fragment.getIS_Status().size();
//        String[] arraySpinner = new String[i];
//        int counter = 0;
//        for (String s:fragment.getIS_Status().keySet()) {
//            arraySpinner[counter] = s;
//            counter++;
//        }
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, arraySpinner);
        //spinner.setAdapter(adapter);
        //try{
        //    int selectionPosition2 = adapter.getPosition(statName.trim());
        //    spinner.setSelection(selectionPosition2);
        //}catch(Exception e){statName.trim(); helper.LogPrintExStackTrace(e);}


        //spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        //    @Override
        //    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //        Log.e("mytag","onItemSelected");
        //        String select = parent.getItemAtPosition(position).toString().trim();
        //        if (helper.isNetworkAvailable(c)){
        //            Log.e("mytag","actionid:"+actionid + "  statusID="+fragment.getIS_Status().get(select));
        //            update_action_id = actionid;
        //            update_status_id = fragment.getIS_Status().get(select);
        //            //AlertDialogConfirm(actionid,select);
//
        //        }
//
//
        //    }
//
        //    @Override
        //    public void onNothingSelected(AdapterView<?> parent) {
//
        //    }
        //});


    }
    private void set_play(final int actionID){
        btn_play.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",c));
        btn_play.setTextSize(30);
        String countUnClosed = "";
        countUnClosed = DatabaseHelper.getInstance(c).getScalarByCountQuery("SELECT count(ID) from IS_ActionsTime where ActionID = '" + actionID + "' and ActionEnd  IS NULL OR ActionEnd = '' order by ID desc limit 1");
        if (Integer.valueOf(countUnClosed) > 0){
            btn_play.setTextColor(Color.parseColor("#E94E1B"));

            //id1.setTextColor(Color.parseColor("black"));
        }
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("mytag","play actionID clicked: "+actionID);
                DatabaseHelper.getInstance(c).add_ISActionTime(new IS_ActionTime("-1","-1",String.valueOf(actionID),helper.getDate("yyyy-MM-dd HH:mm:ss"),null));
                Toast.makeText(c,"btn_play successfully",Toast.LENGTH_LONG).show();
                btn_play.setTextColor(Color.parseColor("#E94E1B"));
                fragment.refresh();
            }
        });

    }
    private void set_stop(final int actionID){
        btn_stop.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",c));
        btn_stop.setTextSize(30);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("mytag","stop actionID clicked: "+actionID);
                IS_ActionTime at = DatabaseHelper.getInstance(c).getISActionTimeByActionID(String.valueOf(actionID));
                if (at != null){
                    at.setActionEnd(helper.getDate("yyyy-MM-dd HH:mm:ss"));
                    boolean flag = false;
                     flag = DatabaseHelper.getInstance(c).updateISActionTime(at);
                    if (flag == true){
                        btn_play.setBackgroundColor(Color.parseColor("black"));
                        Toast.makeText(c,"updated successfully",Toast.LENGTH_LONG).show();
                        helper.sendAsyncActionsTime(c);
                        fragment.refresh();
                    }
                   // updateISActionTime
                }

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
                for(int i=0;i<filterList.size();i++)
                {
                    if(String.valueOf(filterList.get(i).getActionID()).contains(constraint)||
                       String.valueOf(filterList.get(i).getActionDesc()).contains(constraint) ||
                       String.valueOf(filterList.get(i).getComments()).contains(constraint))
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






}