package com.example.campustradingplatform.Chat.dao;

import android.util.Log;

import com.example.campustradingplatform.Chat.ChatBean.ChatItem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class LocaDao {
    public static String getLocaByChatID(String chatID, Connection conn) {
        String sql = "SELECT chatid from loca_tb where chatid="+chatID;
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

    public static void insertLocByBSid(ChatItem chatItem, Connection conn) {
        String sql = "alter table loca_tb auto_increment = 1";
        BaseDao.alert(sql,conn);

        sql = "insert into loca_tb VALUE("+chatItem.getChatID()+","+chatItem.getSeller().getId()+","
                +chatItem.getBuyer().getId()+",null,null,null)";
        BaseDao.insert(sql,conn);
    }

    public static String selectTransAddr(ChatItem chatItem, Connection conn) {
        String sql = "SELECT trans_address from main_chat_tb where chatid="+chatItem.getChatID();
        ResultSet rs = null;
        try {
            rs = BaseDao.select(sql,conn);
            if(rs.next()){
                String res = rs.getString("trans_address");
                return res;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public static void updateTmpTransAddrByChatID(ChatItem chatItem, Connection conn) {
        String sql = "update loca_tb set tmp_trans_addr ='"+chatItem.getTranAddr()
                +"' where chatid="+chatItem.getChatID();
        BaseDao.update(sql,conn);
    }

    public static void updateBuyerAddr(ChatItem chatItem, Connection conn) {
        String sql = "update loca_tb set buyer_addr ='"+chatItem.getBuyer().getAddr()
                +"' where buyerid ="+chatItem.getBuyer().getId()+" and chatid="+chatItem.getChatID();
        BaseDao.update(sql,conn);
    }

    public static String getSellerAddr(ChatItem chatItem, Connection conn) {
        String sql = "SELECT sender_addr from loca_tb where sellerid="+chatItem.getSeller().getId()+" and chatid = "+chatItem.getChatID();
        ResultSet rs = null;
        try {
            rs = BaseDao.select(sql,conn);
            if(rs.next()){
                String sender_addr = rs.getString("sender_addr");
                return sender_addr;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return "";
    }

    public static void updateTransAddrByBuyerAddr(ChatItem chatItem, Connection conn) {
        String sql = "update main_chat_tb set trans_address ='" + chatItem.getUser().getAddr()+
                "' where chatid="+chatItem.getChatID();
//        Log.d("TAG", "updateTransAddrByBuyerAddr: "+sql);
        BaseDao.update(sql,conn);
    }

    public static void updateSellerAddr(ChatItem chatItem, Connection conn) {
        String sql = "update loca_tb set seller_addr ='"+chatItem.getSeller().getAddr()
                +"' where sellerid ="+chatItem.getSeller().getId()+" and chatid="+chatItem.getChatID();
        BaseDao.update(sql,conn);
    }

    public static String getBuyerAddr(ChatItem chatItem, Connection conn) {
        String sql = "SELECT buyer_addr from loca_tb where buyerid="+chatItem.getBuyer().getId()+" and chatid = "+chatItem.getChatID();
        ResultSet rs = null;
        try {
            rs = BaseDao.select(sql,conn);
            if(rs.next()){
                String buyer_addr = rs.getString("buyer_addr");

                return buyer_addr;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public static void updateTransAddrByAddr(ChatItem chatItem, Connection conn) {
        String sql = "update main_chat_tb set trans_address ='" + chatItem.getTranAddr()+
                "' where chatid="+chatItem.getChatID();
        BaseDao.update(sql,conn);
    }

    public static void clearTmpTransAddrByChatID(ChatItem chatItem, Connection conn) {
        String sql = "update loca_tb set tmp_trans_addr ='' where chatid="+chatItem.getChatID();
        BaseDao.update(sql,conn);
    }

    public static String getTmpTransAddrByChatID(String chatID, Connection conn) {
        String sql="SELECT tmp_trans_addr from loca_tb where chatid = "+chatID;
//        Log.d("TAG", "getTmpTransAddrByChatID: "+sql);
        try {
            ResultSet rs = BaseDao.select(sql,conn);
            if (rs.next()){
                Log.d("TAG", "getTmpTransAddrByChatID: "+ rs.getString("tmp_trans_addr"));
//                Log.d("TAG", "flashUI: "+thread.getTransAddr());
                return rs.getString("tmp_trans_addr");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }
}
