package com.example.campustradingplatform.Chat.Service;

import com.example.campustradingplatform.Chat.ChatBean.ChatDetailItem;
import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.Chat.dao.ChatDetailDao;
import com.example.campustradingplatform.Chat.dao.DBConnection;
import com.example.campustradingplatform.Login.User;
import com.example.campustradingplatform.UtilTools.GlobalVars;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class ChatDetailServiceThread extends Thread{

    int mode = -1;
    boolean isFinished = false;


    //接收的参数
    ChatItem chatItem;
    ChatDetailItem chatDetailItem;
    User user;

    //传出参数
    List<ChatDetailItem> chatDetailItems=null;

    public ChatDetailServiceThread(ChatDetailItem chatDetailItem, int mode) {
        this.chatDetailItem = chatDetailItem;
        this.mode = mode;
    }

    public ChatDetailServiceThread(ChatDetailItem chatDetailItem, User user) {
        this.chatDetailItem = chatDetailItem;
        this.user = user;
        mode = GlobalVars.UPDATE_CHAT_DETAIL_THREAD;
    }

    public List<ChatDetailItem> getChatDetailItems() {
        return chatDetailItems;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public ChatDetailServiceThread(ChatItem chatItem, int mode) {
        this.chatItem = chatItem;
        this.mode = mode;
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
            case GlobalVars.GET_CHAT_DETAIL_LIST_THREAD:
                getChatDetailListByChatItem(chatItem,conn);
                break;
                case GlobalVars.INSERT_CHAT_DETAIL_THREAD:
                    insertChatDetail(chatDetailItem,conn);
                    break;
                case GlobalVars.UPDATE_CHAT_DETAIL_THREAD:
                    getLastAddChatDetails(chatDetailItem,user,conn);
                    break;
                case GlobalVars.CHAT_DETAIL_GET_LAST_NOT_HIS:
                    getLastAddChatDetailsNotHis(chatItem,conn);
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

    private void getLastAddChatDetails(ChatDetailItem chatDetailItem, User user, Connection conn) {
        List<ChatDetailItem> chatDetailItems = ChatDetailDao.getLastChatDetailByLastHisAndMe(chatDetailItem,user,conn);

        this.chatDetailItems = chatDetailItems;
    }

    private void getLastAddChatDetailsNotHis(ChatItem chatItem, Connection conn) {
        List<ChatDetailItem> chatDetailItems = ChatDetailDao.getLastChatDetailByUser(chatItem,conn);
        this.chatDetailItems = chatDetailItems;
    }

    private void insertChatDetail(ChatDetailItem chatDetailItem, Connection conn) {
        ChatDetailDao.insertChatDetail(chatDetailItem,conn);
    }

    private void getChatDetailListByChatItem(ChatItem chatItem, Connection conn) {
        //获取 因为最原始的界面需要等待查找结束才能够执行，所以不断 while，知道子线程处理完成，不然无法获得子线程的返回值
        List<ChatDetailItem> chatDetailItems = ChatDetailDao.getChatDetailListByCID(chatItem,conn);
        this.chatDetailItems = chatDetailItems;
    }


}
