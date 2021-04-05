package com.example.campustradingplatform.Chat.ChatBean;

import com.example.campustradingplatform.Login.User;

import java.io.Serializable;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class ChatDetailItem implements Serializable {
    private String chatID="";
    private User user;
    private User sender;
    private User reciever;
    private String msg_con="";
    private String sendTime="";
    private boolean isMeSend=true;  //是不是本人发送

    public ChatDetailItem(String chatID,User user,User sender,User reciever, String msg_con, String sendTime,boolean isMeSend) {
        this.chatID = chatID;
        this.sender = sender;
        this.reciever = reciever;
        this.msg_con = msg_con;
        this.sendTime = sendTime;
        this.isMeSend = isMeSend;
    }
    public ChatDetailItem(){

    }
    public ChatDetailItem(String chatID, String msg_con, String sendTime) {
        this.chatID = chatID;
        this.msg_con = msg_con;
        this.sendTime = sendTime;
    }


    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public String getMsg_con() {
        return msg_con;
    }

    public void setMsg_con(String msg_con) {
        this.msg_con = msg_con;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public boolean isMeSend() {
        return isMeSend;
    }

    public void setMeSend(boolean meSend) {
        isMeSend = meSend;
    }

    @Override
    public String toString() {
        return "ChatDetailItem{" +
                "chatID='" + chatID + '\'' +
                ", sender=" + sender +
                ", reciever=" + reciever +
                ", msg_con='" + msg_con + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", isMeSend=" + isMeSend +
                '}';
    }
}
