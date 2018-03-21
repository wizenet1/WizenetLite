package com.Classes;

/**
 * Created by doron on 17/03/2016.
 */
public class Ccustomer {

    String cid, cparentid, cfname, clname, cemail, cphone, ccell, ccompany;
    String address;
    String image;

    //LatLng latlng;
    public String getCcompany() {
        return ccompany;
    }

    public String getCfname() {
        return cfname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getClname() {
        return clname;
    }

    public String getCemail() {
        return cemail;
    }

    public String getCphone() {
        return cphone;
    }

    public String getCcell() {
        return ccell;
    }

    public String getCID() {
        return cid;
    }

    public String getCParentID() {
        return cparentid;
    }

    public void setCfname(String cfname) {
        this.cfname = cfname;
    }

    public void setCcompany(String ccompany) {
        this.ccompany = ccompany;
    }

    public void setClname(String clname) {
        this.clname = clname;
    }

    public void setCemail(String cemail) {
        this.cemail = cemail;
    }

    public void setCphone(String cphone) {
        this.cphone = cphone;
    }

    public void setCcell(String ccell) {
        this.ccell = ccell;
    }

    public Ccustomer(String cfname, String clname, String cemail, String cphone, String ccell, String ccompany) {
        this.cfname = cfname;
        this.clname = clname;
        this.cemail = cemail;
        this.cphone = cphone;
        this.ccell = ccell;
        this.ccompany = ccompany;

    }

    public Ccustomer(String cfname, String clname, String cemail, String cphone, String ccell, String ccompany, String cid) {
        this.cfname = cfname;
        this.clname = clname;
        this.cemail = cemail;
        this.cphone = cphone;
        this.ccell = ccell;
        this.ccompany = ccompany;
        this.cid = cid;

    }

    public Ccustomer(String cfname, String clname, String cemail, String cphone, String ccell, String ccompany, String cid, String cparentid) {
        this.cfname = cfname;
        this.clname = clname;
        this.cemail = cemail;
        this.cphone = cphone;
        this.ccell = ccell;
        this.ccompany = ccompany;
        this.cid = cid;
        this.cparentid = cparentid;
    }

    public Ccustomer(String cfname, String clname, String cemail, String cphone, String ccell, String ccompany, String address, String image, String cid){
        this.cfname = cfname;
        this.clname = clname;
        this.cemail = cemail;
        this.cphone = cphone;
        this.ccell = ccell;
        this.ccompany = ccompany;
        this.cid = cid;
        this.address = address;
        this.image = image;

    }

    @Override
    public String toString() {
        return "\n[cfname:" + this.cfname + "\nclname:" + this.clname + "\n,cemail:" + this.cemail + "\n,cphone:" +
                this.cphone + "\n,ccell:" + this.ccell + "\n,ccompany:" + this.ccompany + "]";
    }


    //public LatLng getLatlng() {return latlng;}
    //that's how we represent the customer.name in the values of spinner in mapsActivity
//    @Override
//    public String toString() {
//        return this.name;
//    }

}


