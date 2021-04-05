package com.example.campustradingplatform.Chat.ChatBean;

import com.example.campustradingplatform.Goods.Goods;
import com.example.campustradingplatform.Login.User;

import java.io.Serializable;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class OrderItem implements Serializable {
    private User user;
    private String orderID;
    private String orderState;
    private String orderDBState;
    private User buyer;
    private User seller;
    private Goods goods;
    private String chatID;

    public OrderItem(String orderID,User user, User buyer, User seller,
                     Goods goods, String orderState,String orderDBState,String chatID) {
        this.user = user;
        this.orderState = orderState;
        this.buyer = buyer;
        this.seller = seller;
        this.goods = goods;
        this.orderDBState = orderDBState;
        this.orderID=orderID;
        this.chatID=chatID;
    }

    public OrderItem(String orderID, String orderDBState) {
        this.orderID = orderID;
        this.orderDBState = orderDBState;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderDBState() {
        return orderDBState;
    }

    public void setOrderDBState(String orderDBState) {
        this.orderDBState = orderDBState;
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

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "user=" + user +
                ", orderID='" + orderID + '\'' +
                ", orderState='" + orderState + '\'' +
                ", orderDBState='" + orderDBState + '\'' +
                ", buyer=" + buyer +
                ", seller=" + seller +
                ", goods=" + goods +
                ", chatID='" + chatID + '\'' +
                '}';
    }
}
