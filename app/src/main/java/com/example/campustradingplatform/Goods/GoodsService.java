package com.example.campustradingplatform.Goods;

import com.example.campustradingplatform.UtilTools.GlobalVars;

/**
 * @author: 竹林
 * @date: 2021/4/12
 */
public class GoodsService {

    public static GoodsThread getGoodsList() {
        GoodsThread thread = new GoodsThread(GlobalVars.GET_GOODS_LIST_THREAD);
        thread.start();
        return thread;
    }
    public static GoodsThread getGoodsOldPrice(Goods goods){
        GoodsThread thread = new GoodsThread(GlobalVars.GET_GOODS_OLDPRICE);
        thread.start();
        return thread;
    }
}
