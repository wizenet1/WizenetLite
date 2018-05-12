package com;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bitro on 5/7/2018.
 */

public class Messages {
    public List<String> msgList;

    public Messages(){
        this.msgList = new ArrayList<>();
        this.msgList.add("Driving at the moment, sorry!");
        this.msgList.add("Call you back very soon..");
        this.msgList.add("I can't talk now");
        this.msgList.add("Busy will call back");
    }

}
