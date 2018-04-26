package com.Classes;

public class CallStatus {
    int CallStatusID;
    String CallStatusName;
    int CallStatusOrder;

    public int getCallStatusID() {
        return CallStatusID;
    }

    public void setCallStatusID(int callStatusID) {
        CallStatusID = callStatusID;
    }

    public String getCallStatusName() {
        return CallStatusName;
    }

    public void setCallStatusName(String callStatusName) {
        CallStatusName = callStatusName;
    }

    public int getCallStatusOrder() {
        return CallStatusOrder;
    }

    public void setCallStatusOrder(int callStatusOrder) {
        CallStatusOrder = callStatusOrder;
    }

    public CallStatus(){};
    public CallStatus(int CallStatusID,String CallStatusName,int CallStatusOrder){
        this.CallStatusID=CallStatusID;
        this.CallStatusName=CallStatusName;
        this.CallStatusOrder=CallStatusOrder;
    };
}
