package com.example.campustradingplatform.Msg;

import org.litepal.crud.DataSupport;

public class MsgRowItem extends DataSupport {
    boolean isLeft = true;
    String headUrl = "";
    String msgRowCon = "";
    //时间 -------------

    MsgPanelObj msgPanelObj;

    public MsgPanelObj getMsgPanelObj() {
        return msgPanelObj;
    }

    public void setMsgPanelObj(MsgPanelObj msgPanelObj) {
        this.msgPanelObj = msgPanelObj;
    }

    public MsgRowItem(boolean isLeft) {
        this.isLeft = isLeft;
    }

    public MsgRowItem() {

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

    @Override
    public String toString() {
        return "MsgRowItem{" +
                "isLeft=" + isLeft +
                ", headUrl='" + headUrl + '\'' +
                ", msgRowCon='" + msgRowCon + '\'' +
                ", msgPanelObj=" + msgPanelObj +
                '}';
    }
}
