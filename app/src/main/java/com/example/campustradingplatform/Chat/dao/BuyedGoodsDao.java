package com.example.campustradingplatform.Chat.dao;

import android.util.Log;

import com.example.campustradingplatform.Goods.Goods;
import com.example.campustradingplatform.UtilTools.TimeUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class BuyedGoodsDao {
    public static final String TAG ="TAG";

    public static Goods seletGoodsByGid(String gid, Connection conn) {
        String sql="SELECT * FROM buyed_goods WHERE goodsId = "+gid;
//        Log.d(TAG, "BuyedGoodsDao-seletGoodsByGid: "+sql);
        try {
            ResultSet rs = BaseDao.select(sql,conn);
            if(rs.next()){

                int sellerid = Integer.valueOf(rs.getString("sellerid"));
                int goodsId = Integer.valueOf(rs.getString("goodsId"));
                String goodsName = rs.getString("goodsName");
                String description =rs.getString("description");
                float originalPrice =Float.valueOf(rs.getString("originalPrice"));
                float presentPrice =Float.valueOf(rs.getString("presentPrice"));
                String oldorNew = rs.getString("oldorNew");
                Date launchTime = TimeUtil.strToDate(rs.getString("launchTime"));

                Goods goods = new Goods(sellerid,goodsId,goodsName,description,originalPrice,presentPrice,oldorNew,launchTime);
//                Log.d(TAG, "BuyedGoodsDao-seletGoodsByGid: "+goods);
//                Log.d(TAG, "");
                return goods;
            }
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static int insertGoods(Goods goods, Connection conn) {
        
        int sellerid = goods.getSellerId();
        int goodsId = goods.getGoodsId();
        String goodsName =goods.getGoodsName();
        String description =goods.getDescription();
        float originalPrice =goods.getOriginalPrice();
        float presentPrice =goods.getPresentPrice();
        String oldorNew = goods.getOldorNew();
        Date launchTime = goods.getLaunchTime();


        String sql = "insert into buyed_goods value(null,"+sellerid+",'"+goodsName+"','"+description+"',"+
                originalPrice+","+presentPrice+",'"+oldorNew+"','"+TimeUtil.dateToString(launchTime)+"')";
        Log.d(TAG, "insertGoods: "+sql);
        return BaseDao.insert(sql,conn);
    }

    public static void deletGoods(Goods goods, Connection conn) {
        String sql = "delete from buyed_goods_tb where goodsId=" + goods.getGoodsId();
        BaseDao.delet(sql,conn);
    }
}
