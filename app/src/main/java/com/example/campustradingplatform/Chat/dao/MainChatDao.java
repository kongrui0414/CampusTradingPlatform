package com.example.campustradingplatform.Chat.dao;

import com.example.campustradingplatform.Chat.ChatBean.ChatDetailItem;
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
public class MainChatDao {

    public static final String TAG ="TAG";



    public static List<ChatItem> buyerGetChatListByUID(int userID, Connection conn){

        String sql = "SELECT * FROM main_chat_tb  WHERE buyerid="+userID+" order by chatid desc";

        List<ChatItem> res = new ArrayList<>();

        ResultSet rs = null;
        User user = null;
        User buyer = null;

        try {
            //买家  获取会话 列表
            rs = BaseDao.select(sql,conn);
            while(rs.next()){


                //1. 获取 每个会话 的 -->最新消息----------------可能会没有/也可能会有
                ChatDetailItem chatDetailItem = ChatDetailDao.getLastChatDetailByChatID(rs.getString("chatid"),conn);

                //2.获取 每个会话 的  -->商品信息
                // 2.1 可能在 已经出售的商品中 获取
                //2.2 可能是 正在出售的商品
                Goods goods=null;
                if(rs.getString("isSelled").equals("0")){
                    goods= GoodsDao.seletGoodsByGid(rs.getString("gid"),conn);
                }else{
                    goods = BuyedGoodsDao.seletGoodsByGid(rs.getString("gid"),conn);
                }

                String chatid = rs.getString("chatid");

                if(user == null){
                    user = UserDao.getUserByUID(userID,conn);
                    user.setIsBuyer(true);
                }

                if(buyer==null){
                    buyer = UserDao.getUserByUID(Integer.valueOf(rs.getString("buyerid")),conn);
                    buyer.setIsBuyer(true);
                }

                User seller = UserDao.getUserByUID(Integer.valueOf(rs.getString("sellerid")),conn);
                seller.setIsBuyer(false);

                String lastMsg = chatDetailItem.getMsg_con();  //最新的消息
                String msgSendedTime = chatDetailItem.getSendTime(); //消息被发送的时间
                String tranAddr = rs.getString("trans_address");  //定好的交易地点
                String isSelled = rs.getString("isSelled");  //本次商品是否已经被出售
                String orderid = rs.getString("orderid");    //本次会话是否产生了订单


                ChatItem chatItem = new ChatItem(chatid,user,buyer,seller,goods, lastMsg,msgSendedTime,tranAddr,isSelled,orderid);
                res.add(chatItem);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    public static List<ChatItem> sellerGetMainChatDaoByUID(int userID, Connection conn) {
        String sql = "SELECT * FROM main_chat_tb  WHERE sellerid="+userID+" order by chatid desc";

        List<ChatItem> res = new ArrayList<>();

        ResultSet rs = null;
        try {
            //买家  获取会话 列表
            rs = BaseDao.select(sql,conn);
            while(rs.next()){

                //1. 获取 每个会话 的 -->最新消息----------------可能会没有/也可能会有
                ChatDetailItem chatDetailItem = ChatDetailDao.getLastChatDetailByChatID(rs.getString("chatid"),conn);

                //2.获取 每个会话 的  -->商品信息
                // 2.1 可能在 已经出售的商品中 获取
                //2.2 可能是 正在出售的商品
                Goods goods=null;
                if(rs.getString("isSelled").equals("0")){
                    goods= GoodsDao.seletGoodsByGid(rs.getString("gid"),conn);
//                Log.d("TAG", "getMainChatDaoByUID: 在Goods中查找");
                }else{
//                Log.d("TAG", "getMainChatDaoByUID: 在buyedGoods中查找");
                    goods = BuyedGoodsDao.seletGoodsByGid(rs.getString("gid"),conn);
                }


                String chatid = rs.getString("chatid");

                User user = UserDao.getUserByUID(userID,conn);
                user.setIsBuyer(true);

                User buyer = UserDao.getUserByUID(Integer.valueOf(rs.getString("buyerid")),conn);
                buyer.setIsBuyer(true);

                User seller = UserDao.getUserByUID(Integer.valueOf(rs.getString("sellerid")),conn);
                seller.setIsBuyer(false);

                String lastMsg = chatDetailItem.getMsg_con();  //最新的消息
                String msgSendedTime = chatDetailItem.getSendTime(); //消息被发送的时间
                String tranAddr = rs.getString("trans_address");  //定好的交易地点
                String isSelled = rs.getString("isSelled");  //本次商品是否已经被出售
                String orderid = rs.getString("orderid");    //本次会话是否产生了订单


                ChatItem chatItem = new ChatItem(chatid,user,buyer,seller,goods, lastMsg,msgSendedTime,tranAddr,isSelled,orderid);

                res.add(chatItem);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    public static String getChatGIDByChatID(String chatID, Connection conn) {
        String sql = "SELECT gid FROM main_chat_tb where chatid="+chatID;

        try {
            ResultSet rs = BaseDao.select(sql,conn);
            if(rs.next()){
                return rs.getString("gid");
            }
            return "";
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public static void updateOrderIDByOIDAndChatID(String orderID,String chatID, Connection conn) {
        String sql="update main_chat_tb set orderid="+orderID+" where chatid="+chatID;
        BaseDao.update(sql,conn);
    }


    public static void updateChatGoodsSellStateByGidAndBuyedGid(Goods goods, int id,int isSelled, Connection conn) {
        String sql="update main_chat_tb set gid="+id+" , isSelled="+isSelled+" where gid="+goods.getGoodsId();

        BaseDao.update(sql,conn);
    }

    public static ChatItem getChatItemByOrderCID(OrderItem orderItem, Connection conn) {
        String sql = "SELECT * FROM main_chat_tb  WHERE chatid="+orderItem.getChatID();

        ResultSet rs = null;
        try {
            rs = BaseDao.select(sql,conn);
            if(rs.next()){
                ChatDetailItem chatDetailItem = ChatDetailDao.getLastChatDetailByChatID(rs.getString("chatid"),conn);

                Goods goods=null;
                if(rs.getString("isSelled").equals("0")){
                    goods= GoodsDao.seletGoodsByGid(rs.getString("gid"),conn);
                }else{
                    goods = BuyedGoodsDao.seletGoodsByGid(rs.getString("gid"),conn);
                }
                ChatItem chatItem = new ChatItem(rs.getString("chatid"),orderItem.getUser(),orderItem.getBuyer(),
                        orderItem.getSeller(),orderItem.getGoods(),chatDetailItem.getMsg_con(),chatDetailItem.getSendTime(),
                        rs.getString("trans_address"),rs.getString("isSelled"),rs.getString("orderid"));
                return chatItem;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;

    }

    public static String getChatIDByBSGid(ChatItem chatItem, Connection conn) {
        String sql = "SELECT chatid FROM main_chat_tb where buyerid="+chatItem.getBuyer().getId()+
                " and sellerid="+chatItem.getSeller().getId()+" and gid="+chatItem.getGoods().getGoodsId();
//        Log.d("TAG", "getChatIDByBSGid: "+sql);
        try {
            ResultSet rs = BaseDao.select(sql,conn);
            if(rs.next()){
                return rs.getString("chatid");
            }
            return "";
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public static void buyerAddNewChat(ChatItem chatItem, Connection conn) {
        String sql = "alter table main_chat_tb auto_increment = 1";
        BaseDao.alert(sql,conn);

        sql = "insert into main_chat_tb value (null,"+chatItem.getBuyer().getId()+
                ","+chatItem.getSeller().getId()+","+chatItem.getGoods().getGoodsId()+
                ",null,null,0,null)";

//        Log.d("TAG", "buyerAddNewChat: "+sql);
        int chatid = BaseDao.insert(sql,conn);

        //修改当前chatItem的内容
        chatItem.setChatID(String.valueOf(chatid));
    }
}
