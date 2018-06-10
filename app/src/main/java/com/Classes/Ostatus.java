package com.Classes;

public class Ostatus {

    int OstatusID;
    String OstatusName;

    public int getOstatusID() {
        return OstatusID;
    }

    public void setOstatusID(int ostatusID) {
        OstatusID = ostatusID;
    }

    public String getOstatusName() {
        return OstatusName;
    }

    public void setOstatusName(String ostatusName) {
        OstatusName = ostatusName;
    }

    public Ostatus(){}
    public Ostatus(Integer OstatusID, String OstatusName){
        this.OstatusID = OstatusID;
        this.OstatusName = OstatusName;
    }



    @Override
    public String toString() {
        return "OstatusID:"+this.getOstatusID()+ " OstatusName:"+this.getOstatusName();
                //this.cphone+ "\n,ccell:"+ this.ccell + "\n,ccompany:"+ this.ccompany +"]" ;
    }


    //public LatLng getLatlng() {return latlng;}
    //that's how we represent the customer.name in the values of spinner in mapsActivity
//    @Override
//    public String toString() {
//        return this.name;
//    }

}


