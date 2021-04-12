package com.example.campustradingplatform.Chat.Service;

import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.Chat.ChatBean.OrderItem;
import com.example.campustradingplatform.Chat.dao.DBConnection;
import com.example.campustradingplatform.Chat.dao.MainChatDao;
import com.example.campustradingplatform.Chat.dao.UserDao;
import com.example.campustradingplatform.Login.User;
import com.example.campustradingplatform.UtilTools.GlobalVars;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class MainChatServiceThread extends Thread{
    //传入数据
    private User user;
    private OrderItem orderItem;

    //传出数据
    List<ChatItem> chatItems = new ArrayList<>();


    //状态数据
    private int mode = -1;
    private boolean isFinished = false;
    private ChatItem chatItem;


    public List<ChatItem> getChatItems() {
        return chatItems;
    }

    public ChatItem getChatItem() {
        return chatItem;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public MainChatServiceThread(User user, int mode) {
        this.user = user;
        this.mode = mode;
    }

    public MainChatServiceThread(ChatItem chatItem, int mode) {
        this.chatItem = chatItem;
        this.mode = mode;
    }


    public MainChatServiceThread(OrderItem orderItem, int mode) {
        this.orderItem = orderItem;
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

            case GlobalVars.GET_CHAT_LIST_THREAD:
                getChatItemsByUser(user,conn);
                break;
            case GlobalVars.GET_CHAT_ITEM_THREAD:
                getChatItemByOrder(orderItem,conn);
                break;
            case GlobalVars.BUYER_ADD_NEW_CHAT:
                buyerAddNewChat(chatItem,conn);
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
            isFinished=true;
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

    private void buyerAddNewChat(ChatItem chatItem, Connection conn) {
        ChatItem myChatItem = MainChatDao.getChatItemByBSGid(chatItem,conn);
//        String chatid = MainChatDao.getChatIDByBSGid(chatItem,conn);
        if(null == myChatItem){
            MainChatDao.buyerAddNewChat(chatItem,conn);
            this.chatItem = MainChatDao.getChatItemByBSGid(chatItem,conn);
        }else{
            this.chatItem = myChatItem;
        }
    }

    private void getChatItemByOrder(OrderItem orderItem, Connection conn) {
        ChatItem newChatItem = MainChatDao.getChatItemByOrderCID(orderItem,conn);
//        Log.d("TAG", "getChatItemByOrder: "+newChatItem);
        if(null == newChatItem) return;

        if(newChatItem.getUser().isBuyer()){

            User buyer = (User) UserDao.getUserByUID(newChatItem.getUser().getId(),conn);
            User seller = (User) UserDao.getUserByUID(newChatItem.getSeller().getId(),conn);
            seller.setIsBuyer(false);

            newChatItem.setSeller(seller);
            newChatItem.setUser(buyer);
            newChatItem.setBuyer(buyer);

        }
        else{
            User seller = (User) UserDao.getUserByUID(newChatItem.getUser().getId(),conn);
            User buyer = (User) UserDao.getUserByUID(newChatItem.getBuyer().getId(),conn);
            seller.setIsBuyer(false);

            newChatItem.setSeller(seller);
            newChatItem.setUser(seller);
            newChatItem.setBuyer(buyer);
        }
        this.chatItem = newChatItem;
    }

    private void getChatItemsByUser(User user, Connection conn) {
//执行所有dao操作
        List<ChatItem> chatList =new ArrayList<>();
        if(user.isBuyer()){
            //获取 chatID,uid1,uid2 ---- 形成chatItem ---后面再具体查询对象User
            chatList =  MainChatDao.buyerGetChatListByUID(user.getId(),conn);
        }
        else{  //卖家的 - 会话列表
            chatList =  MainChatDao.sellerGetMainChatDaoByUID(user.getId(),conn);
        }
        this.chatItems = chatList;
    }
}
