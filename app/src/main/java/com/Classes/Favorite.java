package com.Classes;

public class Favorite {
    String FID;
    String CID;
    String pageTitle;
    String pageUrl;


    public Favorite()
    {
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getFID() {
        return FID;

    }

    public void setFID(String FID) {
        this.FID = FID;
    }

    public Favorite(String FID,
                    String CID,
                    String pageTitle,
                    String pageUrl)
    {
        this.FID=FID;
        this.CID= CID;
        this.pageTitle =pageTitle;
        this.pageUrl= pageUrl;
    }

    @Override
    public String toString() {
        return "\n[CID FID:"+this.FID+"\nCID:"+this.CID + "\n,pageTitle:" + this.pageTitle+ "\n,pageUrl:" +
                 this.pageUrl + "]" ;
    }
}
