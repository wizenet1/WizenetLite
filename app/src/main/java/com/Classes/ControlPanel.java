package com.Classes;

import android.support.v4.app.Fragment;

public class ControlPanel {
    private int id;
    private String key;
    private String value;
    private String description;
    public ControlPanel()
    {
    }
    public ControlPanel(String key, String value,String description)
    {
        this.key=key;
        this.value=value;
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() { return id;}

    public void setId(int id) {this.id = id;}


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "control panel No"+this.id+"   ["+this.key + "\n," + this.value+ "\n," + this.description + "]" ;
    }
}
