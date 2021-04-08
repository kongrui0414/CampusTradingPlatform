package com.example.campustradingplatform.Chat.Service;

import com.example.campustradingplatform.Chat.ChatBean.ChatDetailItem;
import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.UtilTools.GlobalVars;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class ChatDetailService {
    public static ChatDetailServiceThread getChatDetailListByCID(ChatItem chatItem) {
        ChatDetailServiceThread thread = new ChatDetailServiceThread(chatItem, GlobalVars.GET_CHAT_DETAIL_LIST_THREAD);
        thread.start();
        return thread;
    }

    public static void insertChatDetailItemBySender(ChatDetailItem chatDetailItem) {
        ChatDetailServiceThread thread = new ChatDetailServiceThread(chatDetailItem,GlobalVars.INSERT_CHAT_DETAIL_THREAD);
        thread.start();
    }
}
