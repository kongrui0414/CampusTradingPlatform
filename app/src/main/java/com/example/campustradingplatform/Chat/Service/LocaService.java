package com.example.campustradingplatform.Chat.Service;

import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.UtilTools.GlobalVars;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class LocaService {
    public static void insertLocaByBSid(ChatItem chatItem) {
        LocaServiceThread thread = new LocaServiceThread(chatItem, GlobalVars.INSERT_LOC_BY_BSID_THREAD);
        thread.start();
    }

    public static LocaServiceThread getTransAddr(ChatItem chatItem) {
        LocaServiceThread thread = new LocaServiceThread(chatItem,GlobalVars.SELECT_TRANS_ADDR_THREAD);
        thread.start();
        return thread;
    }

    public static void updateTmpTransAddrByChatID(ChatItem chatItem) {
        LocaServiceThread thread = new LocaServiceThread(chatItem,GlobalVars.UPDATE_TMP_TRANS_ADDR_THREAD);
        thread.start();
    }

    public static LocaServiceThread initAddrs(ChatItem chatItem) {
        LocaServiceThread thread = new LocaServiceThread(chatItem, GlobalVars.INIT_ADDR_THREAD);
        thread.start();
        return thread;
    }

    public static void updateTransAddrByClick(ChatItem chatItem) {
        LocaServiceThread thread = new LocaServiceThread(chatItem,GlobalVars.UPDATE_TRANS_ADDR_THREAD);
        thread.start();
    }

    public static void clearTmpTransAddrByChatID(ChatItem chatItem) {
        LocaServiceThread thread = new LocaServiceThread(chatItem,GlobalVars.CLEAR_TMP_TRANS_ADDR_THREAD);
        thread.start();
    }

    public static LocaServiceThread getFlashedAddrs(ChatItem chatItem) {
        LocaServiceThread thread = new LocaServiceThread(chatItem, GlobalVars.FlASHED_ADDR_THREAD);
        thread.start();
        return thread;
    }

    public static LocaServiceThread comfirmOrder(ChatItem chatItem) {
        LocaServiceThread thread = new LocaServiceThread(chatItem,GlobalVars.COMFIRM_ORDER);
        thread.start();
        return thread;
    }
}
