package com.example.campustradingplatform.Goods;

import java.util.Date;

public class Goods {
    private int sellerId;           //卖家id
    private int goodsId;            //商品id
    private String goodsName;       //商品名称
    private String description;     //商品描述
    private float originalPrice;    //原价
    private float presentPrice;     //现价
    private String oldorNew;        //新旧程度
    private Date launchTime;        //上架时间

    public Goods(int sellerId, int goodsId, String goodsName, String description, float originalPrice, float presentPrice, String oldorNew, Date launchTime) {
        this.sellerId = sellerId;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.description = description;
        this.originalPrice = originalPrice;
        this.presentPrice = presentPrice;
        this.oldorNew = oldorNew;
        this.launchTime = launchTime;
    }

    public Goods() {
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public float getPresentPrice() {
        return presentPrice;
    }

    public void setPresentPrice(float presentPrice) {
        this.presentPrice = presentPrice;
    }

    public String getOldorNew() {
        return oldorNew;
    }

    public void setOldorNew(String oldorNew) {
        this.oldorNew = oldorNew;
    }

    public Date getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Date launchTime) {
        this.launchTime = launchTime;
    }
}
