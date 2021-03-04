package com.example.campustradingplatform.Msg;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MsgPanelObj extends DataSupport {
    private List<MsgRowItem> msgRowItemList=new ArrayList<>();
    private String chatFriendId;


    public List<MsgRowItem> getMsgRowItemList() {
        return msgRowItemList;
    }

    public void setMsgRowItemList(List<MsgRowItem> msgRowItemList) {
        this.msgRowItemList = msgRowItemList;
    }

    public String getChatFriendId() {
        return chatFriendId;
    }

    public void setChatFriendId(String chatFriendId) {
        this.chatFriendId = chatFriendId;
    }

    @Override
    public String toString() {
        return "MsgPanelObj{" +
                "msgRowItemList=" + msgRowItemList +
                ", chatFriendId='" + chatFriendId + '\'' +
                '}';
    }
}
