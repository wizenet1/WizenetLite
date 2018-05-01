package com.Classes;

public class Ctype {
    int CtypeID;
    String CtypeName;

    public Ctype(){};
    public Ctype(int CtypeID, String CtypeName){
        this.CtypeID=CtypeID;
        this.CtypeName=CtypeName;
    };

    public int getCtypeID() {
        return CtypeID;
    }

    public void setCtypeID(int ctypeID) {
        CtypeID = ctypeID;
    }

    public String getCtypeName() {
        return CtypeName;
    }

    public void setCtypeName(String ctypeName) {
        CtypeName = ctypeName;
    }
}
