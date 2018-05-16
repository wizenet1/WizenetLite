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
        this.msgList.add("עסוק כרגע לא..");
        this.msgList.add("אני בחופשה נא..");
        this.msgList.add("אני בישיבה נא..");
        this.msgList.add("אני בנהיגה אחזור..");
        this.msgList.add("אני עסוק כרגע");
        this.msgList.add("ללא");
    }

}
