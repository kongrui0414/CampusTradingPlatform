package com.example.campustradingplatform.Msg;

public class MsgRowItem {
    boolean isLeft = true;
    String headUrl = "";
    String msgRowCon = "";

    public MsgRowItem(boolean isLeft) {
        this.isLeft = isLeft;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getMsgRowCon() {
        return msgRowCon;
    }

    public void setMsgRowCon(String msgRowCon) {
        this.msgRowCon = msgRowCon;
    }
}
