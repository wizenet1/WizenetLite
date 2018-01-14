package com.Classes;

/**
 * Created by User on 31/08/2016.
 */
public class Message {
    String msgID;
    String msgSubject;
    String msgComment;
    String msgUrl;
    String msgDate ;
    String msgRead ;
    String msgType;

    public Message()
    {
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getMsgSubject() {
        return msgSubject;
    }

    public void setMsgSubject(String msgSubject) {
        this.msgSubject = msgSubject;
    }

    public String getMsgComment() {
        return msgComment;
    }

    public void setMsgComment(String msgComment) {
        this.msgComment = msgComment;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getMsgUrl() {
        return msgUrl;
    }

    public void setMsgUrl(String msgUrl) {
        this.msgUrl = msgUrl;
    }

    public String getMsgRead() {
        return msgRead;
    }

    public void setMsgRead(String msgRead) {
        this.msgRead = msgRead;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Message(String msgID,
                   String msgSubject,
                   String msgComment,
                   String msgUrl,
                   String msgDate,
                   String msgRead,
                   String msgType)
    {
        this.msgID=msgID;
        this.msgSubject= msgSubject;
        this.msgComment =msgComment;
        this.msgUrl= msgUrl;
        this.msgDate= msgDate;
        this.msgRead= msgRead;
        this.msgType= msgType;
    }

    @Override
    public String toString() {
        return "\n[Msg ID:"+this.msgID+"\nmsgSubject:"+this.msgSubject + "\n,msgComment:" + this.msgComment+ "\n,msgUrl:" +
                 this.msgUrl+ "\n,msgDate:"+ this.msgDate + "]" ;
    }
}
