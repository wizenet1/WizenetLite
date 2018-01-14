package com.Classes;


/**
 * Created by WIZE02 on 20/07/2017.
 */

public class Order {
    String pname,pmakat,pprice,poprice,pq,ocomment;

    public String getPq() {
        return pq;
    }

    public void setPq(String pq) {
        this.pq = pq;
    }

    public String getOcomment() {
        return ocomment;
    }

    public void setOcomment(String ocomment) {
        this.ocomment = ocomment;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPmakat() {
        return pmakat;
    }

    public void setPmakat(String pmakat) {
        this.pmakat = pmakat;
    }

    public Order(String pname, String pmakat, String pprice, String poprice){
        this.pname = pname;
        this.pmakat = pmakat;
        this.pprice = pprice;
        this.poprice = poprice;
    }
    Order(String pname,String pmakat){
        this.pname = pname;
        this.pmakat = pmakat;
    }
    public Order(String pname, String pmakat, String pprice, String poprice, String pq){
        this.pname = pname;
        this.pmakat = pmakat;
        this.pprice = pprice;
        this.poprice = poprice;
        this.pq = pq;
    }
    Order(String pname,String pmakat,String pprice,String poprice,String pq,String ocomment){
        this.pname = pname;
        this.pmakat = pmakat;
        this.pprice = pprice;
        this.poprice = poprice;
        this.pq = pq;
        this.ocomment = ocomment;
    }
    public String getPprice() {
        return pprice;
    }

    public void setPprice(String pprice) {
        this.pprice = pprice;
    }

    public String getPoprice() {
        return poprice;
    }

    public void setPoprice(String poprice) {
        this.poprice = poprice;
    }

    @Override
    public String toString() {
        return "\n[pname:"+this.pname+"\npmakat:"+this.pmakat +"]" ;
    }
}
