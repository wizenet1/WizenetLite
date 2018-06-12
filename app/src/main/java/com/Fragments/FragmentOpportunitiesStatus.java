package com.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.Activities.MenuActivity;
import com.Activities.R;
import com.Adapters.OpportunitiesStatusExpandableListAdapter;
import com.Classes.Lead;
import com.Classes.Ostatus;
import com.DatabaseHelper;
import com.File_;
import com.Helper;
import com.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The fragment represents the opportunities status list.
 */
public class FragmentOpportunitiesStatus extends Fragment {

    private ExpandableListView expandableListView;
    private OpportunitiesStatusExpandableListAdapter opportunitiesStatusExpandableListAdapter;
    private List<String[]> listDataHeader;
    private HashMap<String, List<String[]>> hashMap;
    File_ f;
    Helper helper;
    View this_view;
    public FragmentOpportunitiesStatus() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        f = new File_();
        helper = new Helper();
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_opportunities_status, container, false);
        this_view =view;
        // Load the action bar.
        getActivity().findViewById(R.id.top_action_bar).setVisibility(View.VISIBLE);

        //Turn all the action bar icons off to their original color.
        ((MenuActivity) getActivity()).turnAllActionBarIconsOff();

        this.expandableListView = (ExpandableListView) view.findViewById(R.id.opportunities_status_expandableListView);
        initData();
        updatedInitData();

        return view;
    }
    public void updatedInitData(){
        if (helper.isNetworkAvailable(getContext())){
            try{

            }catch(Exception e){
                helper.LogPrintExStackTrace(e);
            }
            Model.getInstance().Async_Wz_getLeadsList(helper.getMacAddr(getContext()), new Model.Wz_getLeadsList_Listener() {
                @Override
                public void onResult(String str) {
                    Toast.makeText(getContext(),"לידים נוספו", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(),"נא להמתין, רושם בDB", Toast.LENGTH_SHORT).show();
                    addLeads();
                    Toast.makeText(getContext(),"פעולה הושלמה", Toast.LENGTH_SHORT).show();
                    //Initialize the data lists.
                    initData();
                    setListAdapter(this_view);
                }
            });
        }else{
            //Initialize the data lists.
            initData();
            setListAdapter(this_view);
        }
    }
    //Setting the list adapter.
    private void setListAdapter(View view){
        this.opportunitiesStatusExpandableListAdapter =
                new OpportunitiesStatusExpandableListAdapter(view.getContext(), this.listDataHeader,
                        this.hashMap, getFragmentManager());
        this.expandableListView.setAdapter(this.opportunitiesStatusExpandableListAdapter);
    }

    /**
     * Initializes the headers list and the hash map that holds the header with its list of items.
     */

    private void initData() {

        this.listDataHeader = new ArrayList<>();
        this.hashMap = new HashMap<>();


        //dynamic init
        List<Ostatus> OstatusList = new ArrayList<>();
        OstatusList = getOstatusList();
        for (Ostatus o: OstatusList ) {
            String count =  DatabaseHelper.getInstance(getContext()).getScalarByCountQuery("select count(*) from Leads where Ostatus='" + o.getOstatusID() + "'");
            if (Integer.valueOf(count) > 0){
                this.listDataHeader.add(new String[]{o.getOstatusName(), count});
            }
        }
        //load exactly who gotmore then 0
        //now loading the sub lists while we loop only who got more then 0.
        List<List<String[]>> csvList = new ArrayList<List<String[]>>();
        for (Ostatus o: OstatusList ) {
                List<Lead> new_list_from_db = new ArrayList<>();
                new_list_from_db = DatabaseHelper.getInstance(getContext()).getLeadsByOstatus(String.valueOf(o.getOstatusID()));
            if (new_list_from_db.size() > 0){
                List<String[]> new_list = new ArrayList<>();
                for (Lead l:new_list_from_db) {
                    new_list.add(new String[]{l.getOID(), l.getCcompany(),l.getSfname()+" "+l.getSlname(),l.getOdate(),l.getSphone(),l.getScell(),l.getSemail(),l.getOcomment()});
                }
                csvList.add(new_list);
            }
        }
//        int counter = 0;
//        for (List<String[]> list: csvList) {
//            this.hashMap.put(this.listDataHeader.get(0)[0], newOpportunity);
//        }
        for(int i = 0 ; i < csvList.size() ; i++){
            this.hashMap.put(this.listDataHeader.get(i)[0], csvList.get(i));
        }


    }
    private List<Ostatus> getOstatusList(){
        String json = f.readFromFileExternal(getContext(),"ostatus.txt");
        List<Ostatus> ostatusList = new ArrayList<Ostatus>() ;
        JSONObject j = null;
        try {
            j = new JSONObject(json);
            //get the array [...] in json
            JSONArray jarray = j.getJSONArray("Wz_getOstatusList");
            for (int i = 0; i < jarray.length(); i++) {
                Ostatus c = new Ostatus(jarray.getJSONObject(i).getInt("OstatusID"),jarray.getJSONObject(i).getString("OstatusName"));
                ostatusList.add(c);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return ostatusList;
    }
    private void addLeads(){
        String strJson = "";
        File_ f = new File_();
        strJson = f.readFromFileExternal(getContext(),"leads.txt");
        JSONObject j = null;
        JSONArray jarray = null;
        try {
            j = new JSONObject(strJson);
            jarray= j.getJSONArray("Wz_getLeadsList");
            //DatabaseHelper.getInstance(getContext()).delete_IS_Actions_Rows("");
            Log.e("MYTAG","jarray length is:" + jarray.length());
            if (jarray.length() == 0){
                Log.e("MYTAG"," jarray is 0");
                return;
            }
        } catch (JSONException e) {
            helper.LogPrintExStackTrace(e);
            Log.e("MYTAG"," Wz_getLeadsList "+e.getMessage().toString());
        }
        DatabaseHelper.getInstance(getContext()).deleteAllLeads();
        for (int i = 0; i < jarray.length(); i++) {
            final JSONObject e;

            try {
                e = jarray.getJSONObject(i);
                Lead lead= new Lead();//Integer.valueOf(cursor.getString(cursor.getColumnIndex("CallID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("AID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("CID"))), cursor.getString(cursor.getColumnIndex("CreateDate")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("statusID"))), cursor.getString(cursor.getColumnIndex("CallPriority")), cursor.getString(cursor.getColumnIndex("subject")), cursor.getString(cursor.getColumnIndex("comments")), cursor.getString(cursor.getColumnIndex("CallUpdate")), cursor.getString(cursor.getColumnIndex("cntrctDate")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("TechnicianID"))), cursor.getString(cursor.getColumnIndex("statusName")), cursor.getString(cursor.getColumnIndex("internalSN")), cursor.getString(cursor.getColumnIndex("Pmakat")), cursor.getString(cursor.getColumnIndex("Pname")), cursor.getString(cursor.getColumnIndex("contractID")), cursor.getString(cursor.getColumnIndex("Cphone")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("OriginID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("ProblemTypeID"))), Integer.valueOf(cursor.getString(cursor.getColumnIndex("CallTypeID"))), cursor.getString(cursor.getColumnIndex("priorityID")), cursor.getString(cursor.getColumnIndex("OriginName")), cursor.getString(cursor.getColumnIndex("problemTypeName")), cursor.getString(cursor.getColumnIndex("CallTypeName")), cursor.getString(cursor.getColumnIndex("Cname")), cursor.getString(cursor.getColumnIndex("Cemail")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("contctCode"))), cursor.getString(cursor.getColumnIndex("callStartTime")), cursor.getString(cursor.getColumnIndex("callEndTime")), cursor.getString(cursor.getColumnIndex("Ccompany")), cursor.getString(cursor.getColumnIndex("Clocation")), Integer.valueOf(cursor.getString(cursor.getColumnIndex("callOrder"))), cursor.getString(cursor.getColumnIndex("Caddress")), cursor.getString(cursor.getColumnIndex("Ccity")), cursor.getString(cursor.getColumnIndex("Ccomments")), cursor.getString(cursor.getColumnIndex("Cfname")), cursor.getString(cursor.getColumnIndex("Clname")), cursor.getString(cursor.getColumnIndex("techName")), cursor.getString(cursor.getColumnIndex("Aname")), cursor.getString(cursor.getColumnIndex("ContctName")), cursor.getString(cursor.getColumnIndex("ContctAddress")), cursor.getString(cursor.getColumnIndex("ContctCity")), cursor.getString(cursor.getColumnIndex("ContctCell")), cursor.getString(cursor.getColumnIndex("ContctPhone")), cursor.getString(cursor.getColumnIndex("ContctCity")), cursor.getString(cursor.getColumnIndex("Ccell")), cursor.getString(cursor.getColumnIndex("techColor")), cursor.getString(cursor.getColumnIndex("ContctCemail")), cursor.getString(cursor.getColumnIndex("CallParentID")));

                lead.setOID(e.getString("OID"));
                lead.setCID(e.getString("CID"));
                lead.setOdate(e.getString("Odate"));
                lead.setCcName(e.getString("ccName"));
                lead.setCcID(e.getString("ccID"));
                lead.setCcType(e.getString("ccType"));
                lead.setCcNum(e.getString("ccNum"));
                lead.setCcExp(e.getString("ccExp"));
                lead.setCCpayment(e.getString("CCpayment"));
                lead.setOshipping(e.getString("Oshipping"));
                lead.setOsum(e.getString("Osum"));
                lead.setSfname(e.getString("Sfname"));
                lead.setSlname(e.getString("Slname"));
                lead.setSaddress(e.getString("Saddress"));
                lead.setScity(e.getString("Scity"));
                lead.setScountry(e.getString("Scountry"));
                lead.setSstate(e.getString("Sstate"));
                lead.setSzip(e.getString("Szip"));
                lead.setSphone(e.getString("Sphone"));
                lead.setScell(e.getString("Scell"));
                lead.setSemail(e.getString("Semail"));
                lead.setScomment(e.getString("Scomment"));
                lead.setOprint(e.getString("Oprint"));
                lead.setOfile(e.getString("Ofile"));
                lead.setPStypeID(e.getString("PStypeID"));
                lead.setPSaleID(e.getString("PSaleID"));
                lead.setOstatus(e.getString("Ostatus"));
                lead.setOBstatus(e.getString("OBstatus"));
                lead.setOpaymentType(e.getString("OpaymentType"));
                lead.setLNG(e.getString("LNG"));
                lead.setPsupplierID(e.getString("PsupplierID"));
                lead.setOcard(e.getString("Ocard"));
                lead.setOinvoice(e.getString("Oinvoice"));
                lead.setOnotHome(e.getString("OnotHome"));
                lead.setOtimeShipping(e.getString("OtimeShipping"));
                lead.setOdateShipping(e.getString("OdateShipping"));
                lead.setOrate(e.getString("Orate"));
                lead.setOcoin(e.getString("Ocoin"));
                lead.setExpr1(e.getString("Expr1"));
                lead.setOstatusName(e.getString("OstatusName"));
                lead.setCparentName(e.getString("CparentName"));
                lead.setCParentID(e.getString("CParentID"));
                lead.setCparentUsername(e.getString("CparentUsername"));
                lead.setOcomment(e.getString("Ocomment"));
                lead.setOtax(e.getString("Otax"));
                lead.setSfax(e.getString("Sfax"));
                lead.setOmakats(e.getString("Omakats"));
                lead.setCfax(e.getString("Cfax"));
                lead.setOwnerID(e.getString("OwnerID"));
                lead.setOfname(e.getString("Ofname"));
                lead.setOlname(e.getString("Olname"));
                lead.setCcompany(e.getString("Ccompany"));
                lead.setProdSum(e.getString("ProdSum"));
                lead.setCJoinerID(e.getString("CJoinerID"));
                lead.setPsupplierNAME(e.getString("PsupplierNAME"));
                lead.setSUsername(e.getString("SUsername"));
                lead.setPsupplierLOGO(e.getString("PsupplierLOGO"));
                lead.setSPassword(e.getString("SPassword"));
                lead.setCusername(e.getString("Cusername"));
                lead.setOemail(e.getString("Oemail"));
                lead.setOUdate(e.getString("OUdate"));
                lead.setOTdate(e.getString("OTdate"));
                lead.setCTypeID(e.getString("CTypeID"));
                lead.setCTypeName(e.getString("CTypeName"));
                lead.setOcommentID(e.getString("OcommentID"));
                lead.setSID(e.getString("SID"));
                lead.setOref(e.getString("Oref"));
                lead.setCusername2(e.getString("Cusername2"));

                DatabaseHelper.getInstance(getContext()).addLead(lead);
            } catch (JSONException e1) {
                helper.LogPrintExStackTrace(e1);

            }
        }

        //DatabaseHelper.getInstance(context).getISActions("");
    }


}
