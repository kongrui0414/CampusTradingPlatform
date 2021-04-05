package com.example.campustradingplatform.Chat.dao;

import com.example.campustradingplatform.Chat.ChatBean.ChatDetailItem;
import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
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
public class ChatDetailDao {

    public static final String TAG ="TAG";

    /**
     * description:获取最新的历史消息
     */
    public static ChatDetailItem getLastChatDetailByChatID(String chatid, Connection conn) {
        String sql = "SELECT * FROM msg_detail_tb  WHERE chatid = "+chatid+" ORDER BY sendtime desc LIMIT 1";

        try {
            ResultSet rs = BaseDao.select(sql,conn);
            if(rs.next()){

                String msg_con = rs.getString("msg_con");
                String msgSendedTime = rs.getString("sendtime");
                return new ChatDetailItem(chatid,msg_con,msgSendedTime);

            }
            return new ChatDetailItem();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ChatDetailItem();
    }


    /**
     * description: 获取所有的历史消息
     */
    public static List<ChatDetailItem> getChatDetailListByCID(ChatItem chatItem, Connection conn) {


        //查询到所有的消息内容
        String sql = "SELECT * from msg_detail_tb where chatid="+chatItem.getChatID()+" order by sendtime";

        List<ChatDetailItem> chatDetailItems = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = BaseDao.select(sql,conn);

            while(rs.next()){
                //插入发送者 和 接收者
                User sender = UserDao.getUserByUID(Integer.valueOf(rs.getString("senderid")),conn);
                User reciever = UserDao.getUserByUID(Integer.valueOf(rs.getString("recieverid")),conn);

                String chatID =  rs.getString("chatid");
                String msg_con = rs.getString("msg_con");
                String sendTime = rs.getString("sendtime");
                boolean isMeSend = false;
                if(chatItem.getUser().getId()==sender.getId()){
                    isMeSend = true;
                }

                ChatDetailItem chatDetailItem = new ChatDetailItem(chatID,chatItem.getUser(),sender,reciever,msg_con,sendTime,isMeSend);
                chatDetailItems.add(chatDetailItem);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return chatDetailItems;
    }

    public static void insertChatDetail(ChatDetailItem chatDetailItem, Connection conn) {
        String sql = "alter table msg_detail_tb auto_increment = 1";
        BaseDao.alert(sql,conn);

        sql = "INSERT INTO msg_detail_tb(msgid,chatid,senderid,recieverid,msg_con,sendtime,isreaded) VALUES(null,"
                +chatDetailItem.getChatID()+","+chatDetailItem.getSender().getId()+","
                +chatDetailItem.getReciever().getId()+",'"+chatDetailItem.getMsg_con()+"','"+chatDetailItem.getSendTime()+"',false);";


        BaseDao.insert(sql,conn);
    }
}
