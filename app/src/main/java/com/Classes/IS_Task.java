package com.Classes;

/**
 * Created by doron on 17/03/2016.
 */
public class IS_Task {

    int taskID;
    int projectID;
    String taskSummery;

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getTaskSummery() {
        return taskSummery;
    }

    public void setTaskSummery(String taskSummery) {
        this.taskSummery = taskSummery;
    }
    public IS_Task(){

    }
    public IS_Task(int taskID, int projectID, String taskSummery)
    {
        this.taskID = taskID;
        this.projectID = projectID;
        this.taskSummery = taskSummery;
    };


    @Override
    public String toString() {
        return "taskID:"+this.getTaskID()+ " TaskSummery:"+this.getTaskSummery() ;
                //this.cphone+ "\n,ccell:"+ this.ccell + "\n,ccompany:"+ this.ccompany +"]" ;
    }


    //public LatLng getLatlng() {return latlng;}
    //that's how we represent the customer.name in the values of spinner in mapsActivity
//    @Override
//    public String toString() {
//        return this.name;
//    }

}


