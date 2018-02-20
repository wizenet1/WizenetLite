package com.Classes;

/**
 * Created by doron on 17/03/2016.
 */
public class Call_offline {

    int CallID;
    int statusID;
    String internalSN;
    String techAnswer;


    public int getCallID() {
        return CallID;
    }

    public void setCallID(int callID) {
        CallID = callID;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public String getInternalSN() {
        return internalSN;
    }

    public void setInternalSN(String internalSN) {
        this.internalSN = internalSN;
    }

    public String getTechAnswer() {
        return techAnswer;
    }

    public void setTechAnswer(String techAnswer) {
        this.techAnswer = techAnswer;
    }

    public Call_offline(){}
    public Call_offline(Integer callID, Integer statusID, String internalSN, String techAnswer){
        this.CallID = callID;
        this.statusID = statusID;
        this.internalSN = internalSN;
        this.techAnswer = techAnswer;

    }



    @Override
    public String toString() {
        return "callid:"+this.getCallID()+ " status:"+this.getStatusID()+ " internalSN:"+this.getInternalSN()+ " techAnswer:"+this.getTechAnswer() ;
                //this.cphone+ "\n,ccell:"+ this.ccell + "\n,ccompany:"+ this.ccompany +"]" ;
    }


    //public LatLng getLatlng() {return latlng;}
    //that's how we represent the customer.name in the values of spinner in mapsActivity
//    @Override
//    public String toString() {
//        return this.name;
//    }

}


