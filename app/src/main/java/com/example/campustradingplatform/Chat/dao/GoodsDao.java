package com.example.campustradingplatform.Chat.dao;

import android.util.Log;

import com.example.campustradingplatform.Goods.Goods;
import com.example.campustradingplatform.Login.User;
import com.example.campustradingplatform.UtilTools.TimeUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class GoodsDao {
    public static final String TAG ="TAG";

    public static Goods seletGoodsByGid(String gid, Connection conn) {
        String sql="SELECT * FROM goods WHERE goodsId = "+gid;
//        Log.d(TAG, "Goods-seletGoodsByGid: "+sql);
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
//                Log.d(TAG, "Goods-seletGoodsByGid: "+goods);
//                Log.d(TAG, "");

                return goods;
            }
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;

    }

    public static void deleGoods(Goods goods, Connection conn) {
        String sql = "delete from goods where goodsId=" + goods.getGoodsId();
        BaseDao.delet(sql,conn);
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

        String sql = "insert into goods value("+sellerid+",null,'"+goodsName+"','"+description+"',"+
                originalPrice+","+presentPrice+",'"+oldorNew+"','"+TimeUtil.dateToString(launchTime)+"')";
        return BaseDao.insert(sql,conn);
    }

    public static List<Goods> getGoodsList(User user, Connection conn) {
        String sql="SELECT * FROM goods where sellerid !="+user.getId();
        List<Goods> goodsList = new ArrayList<>();
        try {
            ResultSet rs = BaseDao.select(sql,conn);
            while(rs.next()){

                int sellerid = Integer.valueOf(rs.getString("sellerid"));
                int goodsId = Integer.valueOf(rs.getString("goodsId"));
                String goodsName = rs.getString("goodsName");
                String description =rs.getString("description");
                float originalPrice =Float.valueOf(rs.getString("originalPrice"));
                float presentPrice =Float.valueOf(rs.getString("presentPrice"));
                String oldorNew = rs.getString("oldorNew");
                Date launchTime = TimeUtil.strToDate(rs.getString("launchTime"));


                Goods goods = new Goods(sellerid,goodsId,goodsName,description,originalPrice,presentPrice,oldorNew,launchTime);
                goodsList.add(goods);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return goodsList;
    }

    public static List<Goods> getGoodsListByKeyWords(String keyWords, Connection conn) {
        String sql="SELECT * FROM goods where goodsName like '%"+keyWords+"%' order by presentPrice, launchTime desc";
        Log.d(TAG, "getGoodsListByKeyWords: "+sql);
        List<Goods> goodsList = new ArrayList<>();
        try {
            ResultSet rs = BaseDao.select(sql,conn);
            while(rs.next()){

                int sellerid = Integer.valueOf(rs.getString("sellerid"));
                int goodsId = Integer.valueOf(rs.getString("goodsId"));
                String goodsName = rs.getString("goodsName");
                String description =rs.getString("description");
                float originalPrice =Float.valueOf(rs.getString("originalPrice"));
                float presentPrice =Float.valueOf(rs.getString("presentPrice"));
                String oldorNew = rs.getString("oldorNew");
                Date launchTime = TimeUtil.strToDate(rs.getString("launchTime"));


                Goods goods = new Goods(sellerid,goodsId,goodsName,description,originalPrice,presentPrice,oldorNew,launchTime);
                goodsList.add(goods);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return goodsList;
    }
}
