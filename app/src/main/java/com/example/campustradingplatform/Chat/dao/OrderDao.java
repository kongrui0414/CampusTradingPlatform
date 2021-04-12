package com.example.campustradingplatform.Chat.dao;

import android.util.Log;

import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.Chat.ChatBean.OrderItem;
import com.example.campustradingplatform.Goods.Goods;
import com.example.campustradingplatform.Login.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class OrderDao {
    public static String getOrderStateByOrderID(String orderID, Connection conn) {
        String sql = "select selled_state from order_tb where orderid="+orderID;

        try {
            ResultSet rs = BaseDao.select(sql,conn);
            if(rs.next()){
                return rs.getString("selled_state");
            }
            return "";
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }


    public static int insertOrder(ChatItem chatItem, Connection conn) {
        String sql = "alter table order_tb auto_increment = 1";
        BaseDao.alert(sql,conn);

        sql  = "insert into order_tb value(null,"+chatItem.getBuyer().getId()+
                ","+chatItem.getSeller().getId()+","+chatItem.getGoods().getGoodsId()
                +",now(),'"+chatItem.getTranAddr()+"',now(),0,"+chatItem.getChatID()+")";


        return BaseDao.insert(sql,conn);
    }

    public static List<OrderItem> getOrderByBidAndStateInGoods(User user, String state,String opState,Connection conn) {


        List<OrderItem> orderItems = new ArrayList<>();
//        Log.d("TAG", "getOrderByBidAndStateInGoods: "+sql);
        try {

            //先查找订单
            String sql = "select * from order_tb where buyerid="+user.getId()+" and selled_state="+state;
//            Log.d("TAG", "getOrderByBidAndStateInGoods: "+sql);
            ResultSet rs  = BaseDao.select(sql,conn);
            while(rs.next()){

                //再查人
                User seller = UserDao.getUserByUID(Integer.valueOf(rs.getString("sellerid")),conn);
                seller.setIsBuyer(false);

                //再查商品
                Goods goods = GoodsDao.seletGoodsByGid(rs.getString("gid"),conn);

                OrderItem orderItem = new OrderItem(rs.getString("orderid"),user,user,seller,
                        goods, opState,rs.getString("selled_state"),rs.getString("chatid"));
//                Log.d("TAG", "getOrderByBidAndStateInGoods: "+orderItem);
                orderItems.add(orderItem);
            }

            return orderItems;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public static List<OrderItem> getOrderBySidAndStateInGoods(User user, String state,String opState, Connection conn) {


        List<OrderItem> orderItems = new ArrayList<>();
//        Log.d("TAG", "getOrderByBidAndStateInGoods: "+sql);
        try {

            //先查找订单
            String sql = "select * from order_tb where sellerid="+user.getId()+" and selled_state="+state;
//            Log.d("TAG", "getOrderByBidAndStateInGoods: "+sql);
            ResultSet rs  = BaseDao.select(sql,conn);
            while(rs.next()){

                //再查人
                User buyer = UserDao.getUserByUID(Integer.valueOf(rs.getString("buyerid")),conn);
                buyer.setIsBuyer(true);

                //再查商品
                Goods goods = GoodsDao.seletGoodsByGid(rs.getString("gid"),conn);

                OrderItem orderItem = new OrderItem(rs.getString("orderid"),user,buyer,user,
                        goods, opState,rs.getString("selled_state"),rs.getString("chatid"));
//                Log.d("TAG", "getOrderByBidAndStateInGoods: "+orderItem);
                orderItems.add(orderItem);
            }

            return orderItems;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }




    public static OrderItem getOrderStateByOrderItem(OrderItem orderItem, Connection conn) {
        String sql = "select  selled_state from order_tb where orderid="+orderItem.getOrderID();
        try {
            ResultSet rs = BaseDao.select(sql,conn);
            if(rs.next()){
                return new OrderItem(orderItem.getOrderID(),rs.getString("selled_state"));
            }
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void deletOrderByOrderItem(OrderItem orderItem, Connection conn) {
        String sql = "delete from order_tb   where orderid="+orderItem.getOrderID();
        BaseDao.delet(sql,conn);
    }

    public static void updateOrderStateByOrderItemAndState(OrderItem orderItem, String state, Connection conn) {
        String sql = "update order_tb set selled_state ="+state+" where orderid ="+orderItem.getOrderID();
        BaseDao.update(sql,conn);
    }

    public static void updateOrderStateAndGIDByOrderItemAndState(OrderItem orderItem,int id, String state, Connection conn) {
        String sql = "update order_tb set selled_state ="+state+" , gid="+id+" where orderid ="+orderItem.getOrderID();
//        Log.d("TAG", "updateOrderStateAndGIDByOrderItemAndState: 更新状态和GID"+sql);
        BaseDao.update(sql,conn);
    }


    public static List<OrderItem> getOrderByBidAndStatesInBuyedGoods(User user, List<String> states, String opState, Connection conn) {


//        String sql = "select * from order_tb a,user b,buyed_goods_tb c where buyerid="+user.getId()+" and (";
//
//        for(String state:states){
//            sql = sql+" selled_state="+state+" or ";
//        }
//        sql = sql.substring(0,sql.length()-3);
//        sql = sql+ ") and a.sellerid = b.uid and a.gid=c.gid";

        String sql = "select * from order_tb  where buyerid="+user.getId()+" and (";

        for(String state:states){
            sql = sql+" selled_state="+state+" or ";
        }
        sql = sql.substring(0,sql.length()-3);
        sql = sql+ ")";


        List<OrderItem> orderItems = new ArrayList<>();

        try {
            ResultSet rs  = BaseDao.select(sql,conn);
            while(rs.next()){
                User seller = UserDao.getUserByUID(Integer.valueOf(rs.getString("sellerid")),conn);
                Goods goods = GoodsDao.seletGoodsByGid(rs.getString("gid"),conn);

                OrderItem orderItem = new OrderItem(rs.getString("orderid"),user,user,seller,
                        goods, opState,rs.getString("selled_state"),rs.getString("chatid"));
                orderItems.add(orderItem);
            }
            return orderItems;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static List<OrderItem> getOrderBySidAndStatesInBuyedGoods(User user, List<String> states, String opState, Connection conn) {
        String sql = "select * from order_tb  where sellerid="+user.getId()+" and (";

        for(String state:states){
            sql = sql+" selled_state="+state+" or ";
        }
        sql = sql.substring(0,sql.length()-3);
        sql = sql+ ")";


        List<OrderItem> orderItems = new ArrayList<>();

        try {
            ResultSet rs  = BaseDao.select(sql,conn);
            while(rs.next()){
                User buyer = UserDao.getUserByUID(Integer.valueOf(rs.getString("buyerid")),conn);
                Goods goods = GoodsDao.seletGoodsByGid(rs.getString("gid"),conn);

                OrderItem orderItem = new OrderItem(rs.getString("orderid"),user,buyer,user,
                        goods, opState,rs.getString("selled_state"),rs.getString("chatid"));
                orderItems.add(orderItem);
            }
            return orderItems;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static List<OrderItem> getOrderByBidAndStatesInGoods(User user, List<String> states, String opState, Connection conn) {
//        String sql = "select * from order_tb a,user b,goods_tb c where buyerid="+user.getId()+" and (";
//
//        for(String state:states){
//            sql = sql+" selled_state="+state+" or ";
//        }
//        sql = sql.substring(0,sql.length()-3);
//        sql = sql+ ") and a.sellerid = b.uid and a.gid=c.gid";

        String sql = "select * from order_tb where buyerid="+user.getId()+" and (";

        for(String state:states){
            sql = sql+" selled_state="+state+" or ";
        }
        sql = sql.substring(0,sql.length()-3);
        sql = sql+ ")";

        List<OrderItem> orderItems = new ArrayList<>();

        try {
            ResultSet rs  = BaseDao.select(sql,conn);
            while(rs.next()){
                User seller = UserDao.getUserByUID(Integer.valueOf(rs.getString("sellerid")),conn);
                Goods goods = GoodsDao.seletGoodsByGid(rs.getString("gid"),conn);
                OrderItem orderItem = new OrderItem(rs.getString("orderid"),user,user,seller,
                        goods, opState,rs.getString("selled_state"),rs.getString("chatid"));
                orderItems.add(orderItem);
//                Log.d("TAG", "getWaitFinishForBuyer: "+orderItem);
            }
            return orderItems;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static List<OrderItem> getOrderByBidAndStateInBuyedGoods(User user, String state,String opState,Connection conn) {
//        String sql = "select * from order_tb a,user b,buyed_goods_tb c where buyerid="+user.getId()+" and selled_state="+state+
//                " and a.sellerid = b.uid and a.gid=c.gid";
//
        String sql = "select * from order_tb where buyerid="+user.getId()+" and selled_state="+state;


        List<OrderItem> orderItems = new ArrayList<>();
        try {
            ResultSet rs  = BaseDao.select(sql,conn);
            while(rs.next()){
                User seller = UserDao.getUserByUID(Integer.valueOf(rs.getString("sellerid")),conn);
                Goods goods = GoodsDao.seletGoodsByGid(rs.getString("gid"),conn);
                OrderItem orderItem = new OrderItem(rs.getString("orderid"),user,user,seller,
                        goods, opState,rs.getString("selled_state"),rs.getString("chatid"));
                orderItems.add(orderItem);
            }

            return orderItems;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static List<OrderItem> getOrderBySidAndStatesInGoods(User user, List<String> states, String opState, Connection conn) {
//        String sql = "select * from order_tb a,user b,goods_tb c where sellerid="+user.getId()+" and (";
//
//        for(String state:states){
//            sql = sql+" selled_state="+state+" or ";
//        }
//        sql = sql.substring(0,sql.length()-3);
//        sql = sql+ ") and a.buyerid = b.uid and a.gid=c.gid";
        String sql = "select * from order_tb  where sellerid="+user.getId()+" and (";

        for(String state:states){
            sql = sql+" selled_state="+state+" or ";
        }
        sql = sql.substring(0,sql.length()-3);
        sql = sql+ ")";

//        Log.d("TAG", "getOrderBySidAndStates: "+sql);
        List<OrderItem> orderItems = new ArrayList<>();
        try {
            ResultSet rs  = BaseDao.select(sql,conn);
            while(rs.next()){
                User buyer = UserDao.getUserByUID(Integer.valueOf(rs.getString("buyerid")),conn);
                Goods goods = GoodsDao.seletGoodsByGid(rs.getString("gid"),conn);
                OrderItem orderItem = new OrderItem(rs.getString("orderid"),user,buyer,user,
                        goods, opState,rs.getString("selled_state"),rs.getString("chatid"));
                orderItems.add(orderItem);
            }

            return orderItems;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static List<OrderItem> getOrderBySidAndStateInBuyedGoods(User user, String state,String opState, Connection conn) {
//        String sql = "select * from order_tb a,user b,buyed_goods_tb c where sellerid="+user.getUserID()+" and selled_state="+state+
//                " and a.buyerid = b.uid and a.gid=c.gid";

        String sql = "select * from order_tb where sellerid="+user.getId()+" and selled_state="+state;


        List<OrderItem> orderItems = new ArrayList<>();
        try {
            ResultSet rs  = BaseDao.select(sql,conn);
            while(rs.next()){
                User buyer = UserDao.getUserByUID(Integer.valueOf(rs.getString("buyerid")),conn);
                Goods goods = GoodsDao.seletGoodsByGid(rs.getString("gid"),conn);
                OrderItem orderItem = new OrderItem(rs.getString("orderid"),user,buyer,user,
                        goods, opState,rs.getString("selled_state"),rs.getString("chatid"));
                orderItems.add(orderItem);
//                Log.d("TAG", "getOrderByBidAndState: "+orderItem);
            }

            return orderItems;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
