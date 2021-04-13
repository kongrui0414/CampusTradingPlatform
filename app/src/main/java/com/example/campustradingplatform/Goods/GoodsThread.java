package com.example.campustradingplatform.Goods;

import com.example.campustradingplatform.Chat.dao.DBConnection;
import com.example.campustradingplatform.Chat.dao.GoodsDao;
import com.example.campustradingplatform.UtilTools.GlobalVars;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 竹林
 * @date: 2021/4/12
 */
public class GoodsThread extends Thread{
    int mode =-1;
    boolean isFinished = false;
    List<Goods> goodsList = new ArrayList<>();
    float oldprice;
    Goods goods;

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public GoodsThread(int mode){
        this.mode = mode;
    }

    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void run() {
        super.run();
        Connection conn = DBConnection.getDBconnection();
        //开启事务
        try {
            conn.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //执行所有dao操作
        switch (mode){
            case GlobalVars.GET_GOODS_LIST_THREAD:
                getGoodsListDao(conn);
                break;
            case GlobalVars.GET_GOODS_OLDPRICE:
//                GoodsDao.seletGoodsByGid(goods.getGoodsId(),conn);//数据类型不对
                break;
            default:
                break;
        }

        //回滚，关闭事务
        try {
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            isFinished = true;
            if(conn!=null){
                try {
                    conn.close();
//                    Log.d("TAG", "ChatService: 数据库连接已经关闭");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void getGoodsListDao(Connection conn) {
        goodsList = GoodsDao.getGoodsList(conn);
    }

}
