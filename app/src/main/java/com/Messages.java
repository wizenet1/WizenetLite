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
        this.msgList.add("Busy now..");
        this.msgList.add("Call me later..");
        this.msgList.add("I'll get back to you..");
        this.msgList.add("Driving, call you later..");
    }

}
