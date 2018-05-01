package com.Classes;

public class IS_Project {
    int projectID;
    String projectSummery;

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getProjectSummery() {
        return projectSummery;
    }

    public void setProjectSummery(String projectSummery) {
        this.projectSummery = projectSummery;
    }

    public IS_Project(){};
    public IS_Project(int projectID, String projectSummery){
        this.projectID=projectID;
        this.projectSummery=projectSummery;
    };


}
