package com.example.campustradingplatform.Goods;

import com.example.campustradingplatform.Login.User;
import com.example.campustradingplatform.UtilTools.GlobalVars;

/**
 * @author: 竹林
 * @date: 2021/4/12
 */
public class GoodsService {

    public static GoodsThread getGoodsList(User user) {
        GoodsThread thread = new GoodsThread(user,GlobalVars.GET_GOODS_LIST_THREAD);
        thread.start();
        return thread;
    }


    public static GoodsThread getGoodsListByKeyWord(String searchWords) {
        GoodsThread thread = new GoodsThread(searchWords,GlobalVars.GET_GOODS_LIST_BY_KEY_THREAD);
        thread.start();
        return thread;
    }
}
