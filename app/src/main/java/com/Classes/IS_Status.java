package com.Classes;

public class IS_Status {

    String statusID;
    String statusName;



    public IS_Status(){}

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public IS_Status(String statusID, String statusName){
        this.statusID = statusID;
        this.statusName =statusName;
        };


    @Override
    public String toString() {
        return "statusID:"+this.getStatusID()+
                " \nstatusName:"+this.getStatusName();
    }



}


