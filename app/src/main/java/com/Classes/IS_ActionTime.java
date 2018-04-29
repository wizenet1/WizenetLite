package com.Classes;

public class IS_ActionTime {

    String ID;
    String CID;
    String actionID;
    String actionStart;
    String actionEnd;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getActionID() {
        return actionID;
    }

    public void setActionID(String actionID) {
        this.actionID = actionID;
    }

    public String getActionStart() {
        return actionStart;
    }

    public void setActionStart(String actionStart) {
        this.actionStart = actionStart;
    }

    public String getActionEnd() {
        return actionEnd;
    }

    public void setActionEnd(String actionEnd) {
        this.actionEnd = actionEnd;
    }

    public IS_ActionTime(){}
    public IS_ActionTime(String ID,
                         String CID,
                         String actionID,
                         String actionStart,
                         String actionEnd){
        this.ID = ID;
        this.CID =CID;
        this.actionID =actionID;
        this.actionStart = actionStart;
        this.actionEnd = actionEnd;

        };


    @Override
    public String toString() {
        return "ID:"+this.getID()+" ACTIONID:"+this.getActionID()+ " CID:"+this.getCID() ;
                //this.cphone+ "\n,ccell:"+ this.ccell + "\n,ccompany:"+ this.ccompany +"]" ;
    }


    //public LatLng getLatlng() {return latlng;}
    //that's how we represent the customer.name in the values of spinner in mapsActivity
//    @Override
//    public String toString() {
//        return this.name;
//    }

}


