package com.Classes;

public class Product {

    String PID;
    String Pname;
    String Pmakat;
    String Pserial;
    String Pprice;
    String Pstock;
    String Pcomment;
    //String actionEnd;




    public Product(){}
    public Product(String PID,
                   String Pname,
                   String Pmakat,
                   String Pserial,
                   String Pprice,String Pstock){
        this.PID = PID;
        this.Pname =Pname;
        this.Pmakat =Pmakat;
        this.Pserial = Pserial;
        this.Pprice=Pprice;
        this.Pstock = Pstock;};
    public String getPprice() {
        return Pprice;
    }

    public String getPstock() {
        return Pstock;
    }

    public void setPstock(String pstock) {
        Pstock = pstock;
    }

    public void setPprice(String pprice) {
        Pprice = pprice;
    }
    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getPmakat() {
        return Pmakat;
    }

    public void setPmakat(String pmakat) {
        Pmakat = pmakat;
    }

    public String getPserial() {
        return Pserial;
    }

    public void setPserial(String pserial) {
        Pserial = pserial;
    }

    @Override
    public String toString() {
        return "PID:"+this.getPID()+
                " \ngetPmakat:"+this.getPmakat() +
                "\ngetPname:"+this.getPname() +
                "\ngetPstock:"+this.getPstock() +
                "\ngetPprice:"+this.getPprice();
                //+"\nActionEnd:"+this.getActionEnd()   ;
                //this.cphone+ "\n,ccell:"+ this.ccell + "\n,ccompany:"+ this.ccompany +"]" ;
    }


    //{"PID":11306,"Pmakat":"NB-509","Pmodel":null,"Pname":"LENOVO 59385926 TABLET 7\u0027\u0027 ANDROID 4.2 1Y","PdescS":"","PdescL":null,"Pprice":1.0000,"POprice":1.0000,"PSprice":null,"PCprice":null,"PMprice":1.0000,"Pcomment":"","Ptime":null,"Pguarantee":null,"Pshipping":null,"Ppayment":"","PimageS":null,"PimageB":null,"PcompanyID":null,"PsupplierID":null,"PcatID":null,"PStypeID":1,"Pspecial":false,"Ponline":true,"Psite":0,"Plight":false,"PsaleSpecial":false,"Porder":null,"Pstock":0,"PtypeID":null,"LNG":"HE","PimageR":null,"Pdisk":null,"Pmemory":null,"Pscreen":null,"U_nocat_7055524":null},

    //public LatLng getLatlng() {return latlng;}
    //that's how we represent the customer.name in the values of spinner in mapsActivity
//    @Override
//    public String toString() {
//        return this.name;
//    }

}


