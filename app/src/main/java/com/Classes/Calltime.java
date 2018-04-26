package com.Classes;

public class Calltime {
    int CTID;
    int CallID;
    String CallStartTime;
    String Minute;
    String CTcomment;
    String ctq;


    public String getCtq() {
        return ctq;
    }

    public void setCtq(String ctq) {
        this.ctq = ctq;
    }

    public Calltime(){}
    public Calltime(int CTID,int callID, String CallStartTime, String Minute, String CTcomment,String ctq){
        this.CTID = CTID;
        this.CallID = callID;
        this.CallStartTime = CallStartTime;
        this.Minute = Minute;
        this.CTcomment = CTcomment;
        this.ctq = ctq;
    }

    public int getCTID() {
        return CTID;
    }

    public void setCTID(int CTID) {
        this.CTID = CTID;
    }

    public int getCallID() {
        return CallID;
    }

    public void setCallID(int callID) {
        CallID = callID;
    }

    public String getCallStartTime() {
        return CallStartTime;
    }

    public void setCallStartTime(String callStartTime) {
        CallStartTime = callStartTime;
    }

    public String getMinute() {
        return Minute;
    }

    public void setMinute(String minute) {
        Minute = minute;
    }

    public String getCTcomment() {
        return CTcomment;
    }

    public void setCTcomment(String CTcomment) {
        this.CTcomment = CTcomment;
    }

    @Override
    public String toString() {
        return "callid:"+this.getCallID()+ "\nCTID:"+this.getCTID()+ "\nCallStartTime:"+this.getCallStartTime()+ "\nCTcomment:"+this.getCTcomment()+ "\nMinute:"+this.getMinute()+ "\nctq:"+this.getCtq() ;
                //this.cphone+ "\n,ccell:"+ this.ccell + "\n,ccompany:"+ this.ccompany +"]" ;
    }


    //public LatLng getLatlng() {return latlng;}
    //that's how we represent the customer.name in the values of spinner in mapsActivity
//    @Override
//    public String toString() {
//        return this.name;
//    }

}


