package com.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.ActivityWebView;
import com.Activities.R;
import com.DatabaseHelper;
import com.Fragments.FragmentOpportunityAlertDialog;
import com.Icon_Manager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * The class is used as an adapter for opportunities status fragment expandable list view.
 * Extends the the BaseExpandableListAdapter.
 */
public class OpportunitiesStatusExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String[]> listDataHeader;
    private HashMap<String, List<String[]>> listHashMap;
    private Icon_Manager iconManager;
    private android.support.v4.app.FragmentManager fragmentManager;
    TextView opportunities_status_expandable_list_item_comment;
    TextView opportunities_status_expandable_list_item_link;
    TextView opportunities_status_expandable_list_item_phone;
    TextView opportunities_status_expandable_list_item_cell;
    /**
     * Constructor.
     *
     * @param context        context
     * @param listDataHeader list headers, what is shown before the list expands
     * @param listHashMap    list values, what is shown after the list expands
     */
    public OpportunitiesStatusExpandableListAdapter(Context context, List<String[]> listDataHeader,
                                                    HashMap<String, List<String[]>> listHashMap,
                                                    android.support.v4.app.FragmentManager fragmentManager) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
        this.fragmentManager = fragmentManager;
        this.iconManager = new Icon_Manager();
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.listHashMap.get(listDataHeader.get(i)[0]).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {

        // i = groupItem , i1 = childItem
        return this.listHashMap.get(this.listDataHeader.get(i)[0]).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        //Icon_Manager iconManager = new Icon_Manager();
        String[] headerData = (String[]) getGroup(i);
        String headerTitle = headerData[0];
        String headerCounter = headerData[1];

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.opportunities_status_expandable_list_group, null);
        }

        //Setting header title.
        TextView headerTitleText = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_group_title);
        headerTitleText.setText(headerTitle);

        //Setting header icon.
        TextView headerCounterText = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_group_counter);
        headerCounterText.setText("(" + headerCounter + ")");

//        lblListIcon.setText(context.getResources().getIdentifier(headerIcon, "string", context.getPackageName()));
//        lblListIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
//        lblListIcon.setTextSize(30);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        final String opportunityNumber = ((String[]) getChild(i, i1))[0];
        final String opportunityDescription = ((String[]) getChild(i, i1))[1];//getCcompany
        final String opportunitySname = ((String[]) getChild(i, i1))[2];//getSfname+getSlname
        final String opportunityOdate = ((String[]) getChild(i, i1))[3];//getOdate
        final String opportunitySphone = ((String[]) getChild(i, i1))[4];//getSphone
        final String opportunityScell = ((String[]) getChild(i, i1))[5];//getScell
        final String opportunitySemail = ((String[]) getChild(i, i1))[6];//getSemail
        final String opportunityOcomment = ((String[]) getChild(i, i1))[7];//getOcomment
        //final String opportunityDescription = ((String[]) getChild(i, i1))[1];

        //l.getOID(), l.getCcompany(),l.getSfname()+" "+l.getSlname(),l.getOdate(),l.getSphone(),l.getScell(),l.getSemail(),l.getOcomment()

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.opportunities_status_expandable_list_item, null);
        }

        //Setting opportunity number.
        TextView opportunityNumberText = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_item_number_text);
        opportunityNumberText.setText(opportunityNumber);

        //Setting opportunity description.
        TextView opportunities_status_expandable_list_item_description_text = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_item_description_text);
        opportunities_status_expandable_list_item_description_text.setText(opportunityDescription);
        TextView opportunities_status_expandable_list_item_date = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_item_date);
        opportunities_status_expandable_list_item_date.setText(opportunityOdate);
        TextView opportunities_status_expandable_list_item_contct = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_item_contct);
        opportunities_status_expandable_list_item_contct.setText(opportunitySname);

        opportunities_status_expandable_list_item_phone = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_item_phone);
        opportunities_status_expandable_list_item_phone.setText(opportunitySphone);
        opportunities_status_expandable_list_item_cell = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_item_cell);
        opportunities_status_expandable_list_item_cell.setText(opportunityScell);

        setOpportunities_status_expandable_list_item_phone(opportunitySphone);
        setOpportunities_status_expandable_list_item_cell(opportunityScell);



        TextView opportunities_status_expandable_list_item_email = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_item_email);
        opportunities_status_expandable_list_item_email.setText(opportunitySemail);
        //opportunities_status_expandable_list_item_comment
        opportunities_status_expandable_list_item_comment = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_item_comment);
        opportunities_status_expandable_list_item_link = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_item_link);

        setOpportunities_status_expandable_list_item_comment(opportunityOcomment);
        setOpportunities_status_expandable_list_item_link(opportunityNumber);

        opportunities_status_expandable_list_item_description_text.setGravity(Gravity.RIGHT);

        opportunities_status_expandable_list_item_contct.setTextDirection(View.TEXT_DIRECTION_RTL);
        opportunities_status_expandable_list_item_phone.setTextDirection(View.TEXT_DIRECTION_RTL);
        opportunities_status_expandable_list_item_cell.setTextDirection(View.TEXT_DIRECTION_RTL);
        opportunities_status_expandable_list_item_email.setTextDirection(View.TEXT_DIRECTION_RTL);

        opportunities_status_expandable_list_item_phone.setPaintFlags(opportunities_status_expandable_list_item_phone.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        opportunities_status_expandable_list_item_cell.setPaintFlags(opportunities_status_expandable_list_item_cell.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);


        //textview.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
        //Setting the edit icon.
        TextView editIcon = (TextView) view.findViewById(R.id.opportunities_status_expandable_list_item_edit_icon);
        editIcon.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        editIcon.setTextSize(30);

        opportunities_status_expandable_list_item_comment.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));

        final int groupId = i;

        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlertDialog(groupId,Integer.valueOf(opportunityNumber),opportunityOcomment);
            }
        });

        return view;
    }
    private void setOpportunities_status_expandable_list_item_phone(final String cell){
        opportunities_status_expandable_list_item_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDialScreen(cell);
            }
        });
    }
    private void setOpportunities_status_expandable_list_item_cell(final String cell){
        opportunities_status_expandable_list_item_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDialScreen(cell);
            }
        });
    }
    private void setOpportunities_status_expandable_list_item_link(final String OID){
        opportunities_status_expandable_list_item_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityWebView.class);
                Bundle b = new Bundle();
                b.putInt("callid", -1);
                b.putInt("cid", -1);
                b.putInt("technicianid", Integer.parseInt(String.valueOf(DatabaseHelper.getInstance(context).getValueByKey("CID"))));
                b.putString("action", "dynamic");
                b.putString("specialurl", "iframe.aspx?control=modulsorder/OrderUpdate&OID=" + OID + "&orderType=HE&connect=true");
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }
    private void setOpportunities_status_expandable_list_item_comment(final String html){
        opportunities_status_expandable_list_item_comment.setTypeface(iconManager.get_Icons("fonts/ionicons.ttf", context));
        opportunities_status_expandable_list_item_comment.setTextSize(30);
          opportunities_status_expandable_list_item_comment.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  AlertDialog.Builder alert = new AlertDialog.Builder(context);
                  //alert.setTitle("title");
                  WebView wv = new WebView(context);
                  wv.loadData("<html><body style='direction: rtl;'>" + html.replace("#","<br>") + "</body></html>", "text/html; charset=UTF-8", null);
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
      private void goToDialScreen(String tel){
          Intent intent = new Intent(Intent.ACTION_DIAL);
          intent.setData(Uri.parse("tel:" + tel + ""));
          context.startActivity(intent);
      }
    /**
     * Opens an edit alert dialog.
     *
     * @param groupId group id
     */
    private void openAlertDialog(int groupId,int OID,String Ocomment) {
        Bundle bundle = new Bundle();
        bundle.putInt("HeaderId", groupId);
        bundle.putInt("OID", OID);
        bundle.putString("Ocomment", "");//Ocomment);
        List<String> TitlesOnly = getOnlyFirstStrings();
        String headersInJson = new Gson().toJson(TitlesOnly);
        bundle.putString("HeadersInJson", headersInJson);

        FragmentOpportunityAlertDialog alertDialog = new FragmentOpportunityAlertDialog();
        alertDialog.setArguments(bundle);
        alertDialog.show(this.fragmentManager, "opportunity");
    }

    /**
     * Returns only the titles from the listDataHeaders list.
     * @return List of string
     */
    private List<String> getOnlyFirstStrings(){
        List<String> list = new ArrayList<>();

        for(String[] item : this.listDataHeader){
            list.add(item[0]);
        }

        return list;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
