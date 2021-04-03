package com.example.campustradingplatform.Order;

import android.os.Looper;
import android.util.Log;

import com.example.campustradingplatform.Login.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class Order {
    private int orderId;        //订单编号
    private int buyerId;        //买家id
    private String buyerName;   //买家用户名
    private int sellerId;       //卖家id
    private String sellerName;   //卖家用户名
    private int goodsId;        //商品id
    private String goodsName;   //商品名
    private float price;         //订单价格
    private String address;     //收货地址
    private Date orderTime;     //下单时间
    private int state;          //订单状态，0为已下单，1为已完成

    public Order(int orderId, int buyerId, String buyerName, int sellerId, String sellerName, int goodsId, String goodsName, float price, String address, Date orderTime, int state) {
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.price = price;
        this.address = address;
        this.orderTime = orderTime;
        this.state = state;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Order() {
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
