package com.Classes;

public class Call {

    int CallID;
    int AID;
    int CID;
    String CreateDate;
    int statusID;
    String CallPriority;
    String subject;
    String comments;
    String CallUpdate;
    String cntrctDate;
    int TechnicianID;
    String statusName;
    String internalSN;
    String Pmakat;
    String Pname;
    String contractID;
    String Cphone;
    int OriginID;
    int ProblemTypeID;
    int CallTypeID;
    String priorityID;
    String OriginName;
    String problemTypeName;
    String CallTypeName;
    String Cname;
    String Cemail;
    int contctCode;
    String callStartTime;
    String callEndTime;
    String Ccompany;
    String Clocation;
    int callOrder;
    String Caddress;
    String Ccity;
    String Ccomments;
    String Cfname;
    String Clname;
    String techName;
    String Aname;
    String ContctName;
    String ContctAddress;
    String ContctCity;
    String ContctCell;
    String ContctPhone;
    String Ccell;
    String techColor;
    String ContctCemail;
    String state;
    String sla;




    public int getCallID() {
        return CallID;
    }

    public int getAID() {
        return AID;
    }

    public int getCID() {
        return CID;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public int getStatusID() {
        return statusID;
    }

    public String getCallPriority() {
        return CallPriority;
    }

    public String getSubject() {
        return subject;
    }

    public String getComments() {
        return comments;
    }

    public String getCallUpdate() {
        return CallUpdate;
    }

    public String getCntrctDate() {
        return cntrctDate;
    }

    public int getTechnicianID() {
        return TechnicianID;
    }

    public String getStatusName() {
        return statusName;
    }

    public String getInternalSN() {
        return internalSN;
    }

    public String getPmakat() {
        return Pmakat;
    }

    public String getPname() {
        return Pname;
    }

    public String getSla() {
        return sla;
    }

    public void setSla(String sla) {
        this.sla = sla;
    }

    public String getContractID() {
        return contractID;
    }

    public String getCphone() {
        return Cphone;
    }

    public int getOriginID() {
        return OriginID;
    }

    public int getProblemTypeID() {
        return ProblemTypeID;
    }

    public int getCallTypeID() {
        return CallTypeID;
    }

    public String getPriorityID() {
        return priorityID;
    }

    public String getOriginName() {
        return OriginName;
    }

    public String getProblemTypeName() {
        return problemTypeName;
    }

    public String getCallTypeName() {
        return CallTypeName;
    }

    public String getCname() {
        return Cname;
    }

    public String getCemail() {
        return Cemail;
    }

    public int getContctCode() {
        return contctCode;
    }

    public String getCallStartTime() {
        return callStartTime;
    }

    public String getCallEndTime() {
        return callEndTime;
    }

    public String getCcompany() {
        return Ccompany;
    }

    public String getClocation() {
        return Clocation;
    }

    public int getCallOrder() {
        return callOrder;
    }

    public String getCaddress() {
        return Caddress;
    }

    public String getCcity() {
        return Ccity;
    }

    public String getCcomments() {
        return Ccomments;
    }

    public String getCfname() {
        return Cfname;
    }

    public String getClname() {
        return Clname;
    }

    public String getTechName() {
        return techName;
    }

    public String getAname() {
        return Aname;
    }

    public String getContctName() {
        return ContctName;
    }

    public String getContctAddress() {
        return ContctAddress;
    }

    public String getContctCity() {
        return ContctCity;
    }

    public String getContctCell() {
        return ContctCell;
    }

    public String getContctPhone() {
        return ContctPhone;
    }

    public String getCcell() {
        return Ccell;
    }

    public String getTechColor() {
        return techColor;
    }

    public String getContctCemail() {
        return ContctCemail;
    }

    public String getCallParentID() {
        return CallParentID;
    }

    public String getState() {
        return this.state;
    }




    String CallParentID;
    public Call(){}
    public Call(Integer callID, Integer aid, Integer cid, String createDate, Integer statusID, String callPriority,
                String subject, String comments, String callUpdate, String cntrctDate, Integer technicianID, String statusName,
                String internalSN, String pmakat, String pname, String contractID, String cphone, Integer originID, Integer problemTypeID,
                Integer callTypeID, String priorityID, String originName, String problemTypeName, String callTypeName, String cname, String cemail,
                Integer contctCode, String callStartTime, String callEndTime, String ccompany, String clocation, Integer callOrder, String caddress,
                String ccity, String ccomments, String cfname, String clname, String techName, String aname, String contctName, String contctAddress,
                String contctCity, String contctCell, String contctPhone, String city, String ccell, String techColor, String contctCemail, String callParentID,String state,String sla){
        this.CallID = callID;
        this.callStartTime = callStartTime;
        this.callEndTime = callEndTime;
        this.Ccompany = ccompany;
        this.Clocation = clocation;
        this.callOrder = callOrder;
        this.Ccomments = ccomments;
        this.Cfname = cfname;
        this.Clname = clname;
        this.techName = techName;
        this.Aname = aname;
        this.ContctName = contctName;
        this.ContctAddress = contctAddress;
        this.ContctCity = contctCity;
        this.ContctPhone = contctPhone;
        this.Ccity = city;
        this.Ccell = ccell;
        this.techColor = techColor;
        this.ContctCemail = contctCemail;
        this.CallParentID = callParentID;
        this.AID = aid;
        this.subject = subject;
        this.CreateDate = createDate;
        this.Ccompany = ccompany;
        this.statusName = statusName;
        this.Caddress = caddress;
        this.Ccity = ccity;
        this.ContctCell = contctCell;
        this.CID = cid;
        this.statusID = statusID;
        this.CallPriority = callPriority;
        this.Ccomments = comments;
        this.CallUpdate = callUpdate;
        this.cntrctDate = cntrctDate;
        this.TechnicianID = technicianID;
        this.internalSN = internalSN;
        this.Pmakat = pmakat;
        this.Pname = pname;
        this.contractID = contractID;
        this.Cphone = cphone;
        this.OriginID = originID;
        this.ProblemTypeID = problemTypeID;
        this.CallTypeID = callTypeID;
        this.priorityID = priorityID;
        this.OriginName = originName;
        this.problemTypeName = problemTypeName;
        this.CallTypeName = callTypeName;
        this.Cname = cname;
        this.Cemail = cemail;
        this.contctCode = contctCode;
        this.state = state;
        this.sla = sla;
    }
    public Call(int callID,String subject,String createDate,String ccompany,String statusName,String contctCell,String ccity,String caddress)
    {
        this.CallID = callID;
        this.subject = subject;
        this.CreateDate = createDate;
        this.Ccompany = ccompany;
        this.statusName = statusName;
        this.Caddress = caddress;
        this.Ccity = ccity;
        this.ContctCell = contctCell;

    }
    public void setState(String state) {
        this.state = state;
    }
    public void setCallID(int callID) {
        CallID = callID;
    }

    public void setAID(int AID) {
        this.AID = AID;
    }

    public void setCID(int CID) {
        this.CID = CID;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public void setCallPriority(String callPriority) {
        CallPriority = callPriority;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    public void setCallUpdate(String callUpdate) {
        CallUpdate = callUpdate;
    }
    public void setCntrctDate(String cntrctDate) {
        this.cntrctDate = cntrctDate;
    }

    public void setTechnicianID(int technicianID) {
        TechnicianID = technicianID;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setInternalSN(String internalSN) {
        this.internalSN = internalSN;
    }

    public void setPmakat(String pmakat) {
        Pmakat = pmakat;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }

    public void setCphone(String cphone) {
        Cphone = cphone;
    }

    public void setOriginID(int originID) {
        OriginID = originID;
    }

    public void setProblemTypeID(int problemTypeID) {
        ProblemTypeID = problemTypeID;
    }

    public void setCallTypeID(int callTypeID) {
        CallTypeID = callTypeID;
    }

    public void setPriorityID(String priorityID) {
        this.priorityID = priorityID;
    }

    public void setOriginName(String originName) {
        OriginName = originName;
    }

    public void setProblemTypeName(String problemTypeName) {
        this.problemTypeName = problemTypeName;
    }

    public void setCallTypeName(String callTypeName) {
        CallTypeName = callTypeName;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public void setCemail(String cemail) {
        Cemail = cemail;
    }

    public void setContctCode(int contctCode) {
        this.contctCode = contctCode;
    }

    public void setCallStartTime(String callStartTime) {
        this.callStartTime = callStartTime;
    }

    public void setCallEndTime(String callEndTime) {
        this.callEndTime = callEndTime;
    }

    public void setCcompany(String ccompany) {
        Ccompany = ccompany;
    }

    public void setClocation(String clocation) {
        Clocation = clocation;
    }

    public void setCallOrder(int callOrder) {
        this.callOrder = callOrder;
    }

    public void setCaddress(String caddress) {
        Caddress = caddress;
    }

    public void setCcity(String ccity) {
        Ccity = ccity;
    }

    public void setCcomments(String ccomments) {
        Ccomments = ccomments;
    }

    public void setCfname(String cfname) {
        Cfname = cfname;
    }

    public void setClname(String clname) {
        Clname = clname;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public void setAname(String aname) {
        Aname = aname;
    }

    public void setContctName(String contctName) {
        ContctName = contctName;
    }

    public void setContctAddress(String contctAddress) {
        ContctAddress = contctAddress;
    }

    public void setContctCity(String contctCity) {
        ContctCity = contctCity;
    }

    public void setContctCell(String contctCell) {
        ContctCell = contctCell;
    }

    public void setContctPhone(String contctPhone) {
        ContctPhone = contctPhone;
    }

    public void setCcell(String ccell) {
        Ccell = ccell;
    }

    public void setTechColor(String techColor) {
        this.techColor = techColor;
    }

    public void setContctCemail(String contctCemail) {
        ContctCemail = contctCemail;
    }

    public void setCallParentID(String callParentID) {
        CallParentID = callParentID;
    }

    @Override
    public String toString() {
        return "callid:"+this.getCallID()+ " status:"+this.getStatusName() ;
                //this.cphone+ "\n,ccell:"+ this.ccell + "\n,ccompany:"+ this.ccompany +"]" ;
    }


    //public LatLng getLatlng() {return latlng;}
    //that's how we represent the customer.name in the values of spinner in mapsActivity
//    @Override
//    public String toString() {
//        return this.name;
//    }

}


