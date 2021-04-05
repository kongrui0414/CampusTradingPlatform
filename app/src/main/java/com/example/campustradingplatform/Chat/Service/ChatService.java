package com.example.campustradingplatform.Chat.Service;

import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.Chat.ChatBean.OrderItem;
import com.example.campustradingplatform.Login.User;
import com.example.campustradingplatform.UtilTools.GlobalVars;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class ChatService {


    public static MainChatServiceThread getChatByOrderItemCID(OrderItem orderItem) {
        MainChatServiceThread thread = new MainChatServiceThread(orderItem,GlobalVars.GET_CHAT_ITEM_THREAD);
        thread.start();
        return thread;
    }

    public static MainChatServiceThread addChatItemByBidAndSidAndGid(ChatItem chatItem) {
        MainChatServiceThread thread = new MainChatServiceThread(chatItem,GlobalVars.BUYER_ADD_NEW_CHAT);
        thread.start();
        return thread;
    }

    /**
     * description:分买家 /  卖家  查询聊天对象列表
     */
    public MainChatServiceThread GetChatListByUser(User user) {
        MainChatServiceThread thread = new MainChatServiceThread(user, GlobalVars.GET_CHAT_LIST_THREAD);
        thread.start();
        return thread;
    }
}
