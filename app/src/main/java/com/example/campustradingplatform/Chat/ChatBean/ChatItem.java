package com.example.campustradingplatform.Chat.ChatBean;

import com.example.campustradingplatform.Goods.Goods;
import com.example.campustradingplatform.Login.User;

import java.io.Serializable;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class ChatItem implements Serializable {

    private String chatID;
    private User user;
    private User buyer;
    private User seller;
    private Goods goods;
    private String lastMsg;
    private String lastMsgSendTime;
    private String tranAddr;
    private String isSelled;
    private String orderID="";
    private String transDate;

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransDate() {
        return transDate;
    }

    public ChatItem(String chatID, User user, User buyer, User seller, Goods goods,
                    String lastMsg, String lastMsgSendTime, String tranAddr, String isSelled, String orderID) {
        this.chatID = chatID;
        this.user = user;
        this.buyer = buyer;
        this.seller = seller;
        this.goods = goods;
        this.lastMsg = lastMsg;
        this.lastMsgSendTime = lastMsgSendTime;
        this.tranAddr = tranAddr;
        this.orderID = orderID;
        this.isSelled = isSelled;
    }
    public ChatItem(User user){
        this.user = user;
    }

    public ChatItem(User user, User buyer, User seller, Goods goods) {
        this.user = user;
        this.buyer = buyer;
        this.seller = seller;
        this.goods = goods;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getLastMsgSendTime() {
        return lastMsgSendTime;
    }

    public void setLastMsgSendTime(String lastMsgSendTime) {
        this.lastMsgSendTime = lastMsgSendTime;
    }

    public String getTranAddr() {
        return tranAddr;
    }

    public void setTranAddr(String tranAddr) {
        this.tranAddr = tranAddr;
    }

    public String getIsSelled() {
        return isSelled;
    }

    public void setIsSelled(String isSelled) {
        this.isSelled = isSelled;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @Override
    public String toString() {
        return "ChatItem{" +
                "chatID='" + chatID + '\'' +
                ", user=" + user +
                ", buyer=" + buyer +
                ", seller=" + seller +
                ", goods=" + goods +
                ", lastMsg='" + lastMsg + '\'' +
                ", lastMsgSendTime='" + lastMsgSendTime + '\'' +
                ", tranAddr='" + tranAddr + '\'' +
                ", isSelled='" + isSelled + '\'' +
                ", orderID='" + orderID + '\'' +
                '}';
    }


}
