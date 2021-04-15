package com.example.campustradingplatform.Chat.Service;

import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.Chat.dao.DBConnection;
import com.example.campustradingplatform.Chat.dao.LocaDao;
import com.example.campustradingplatform.Chat.dao.MainChatDao;
import com.example.campustradingplatform.Chat.dao.OrderDao;
import com.example.campustradingplatform.UtilTools.GlobalVars;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class LocaServiceThread extends Thread{
    int mode = -1;


    //接收的消息
    ChatItem chatItem;
    private boolean isFinished=false;
    private String transAddr="";
    private String sellerAddr="";
    private String buyerAddr;


    public LocaServiceThread(ChatItem chatItem, int mode) {
        this.chatItem = chatItem;
        this.mode = mode;
    }

    public String getTransAddr() {
        return transAddr;
    }

    public boolean isFinished() {
        return isFinished;
    }
    public String getSellerAddr() {
        return sellerAddr;
    }

    public String getBuyerAddr() {
        return buyerAddr;
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
            case GlobalVars.INIT_ADDR_THREAD:
                if(chatItem.getUser().isBuyer()){
                    buyerInitAllAddr(chatItem,conn);
                }else{
                    sellerInitAllAddr(chatItem,conn);
                }
                break;
//            case GlobalVars.DELET_BUYER_ADDR_THREAD:
//                deleBuyerAddr(chatItem,conn);
            case GlobalVars.UPDATE_TRANS_ADDR_THREAD:
                updateTransAddrByClick(chatItem,conn);
            case GlobalVars.SELECT_TRANS_ADDR_THREAD:
                selectTransAddr(chatItem,conn);
                break;
            case GlobalVars.COMFIRM_ORDER:
                comfirmOrder(chatItem,conn);
                break;
            case GlobalVars.INSERT_LOC_BY_BSID_THREAD:
                insertLocByBSid(chatItem,conn);
                break;
            case GlobalVars.UPDATE_TMP_TRANS_ADDR_THREAD:
                updateTmpTransAddrByChatID(chatItem,conn);
                break;
            case GlobalVars.CLEAR_TMP_TRANS_ADDR_THREAD:
                clearTmpTransAddrByChatID(chatItem,conn);
                break;
            case GlobalVars.FlASHED_ADDR_THREAD:
                if(chatItem.getUser().isBuyer()){
                    buyerGetFlashedAllAddr(chatItem,conn);
                }else{
                    sellerGetFlashedAllAddr(chatItem,conn);
                }
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

    private void comfirmOrder(ChatItem chatItem, Connection conn) {
        //先查询订单的商品id
        String gid = MainChatDao.getChatGIDByChatID(chatItem.getChatID(),conn);

        if(!"-1".equals(gid) && !"".equals(gid) && null != gid)
            chatItem.getGoods().setGoodsId(Integer.valueOf(gid));

        //加入订单,但是需要等待卖家同意
        OrderDao.insertOrder(chatItem,conn);
    }

    private void sellerGetFlashedAllAddr(ChatItem chatItem, Connection conn) {
        //更新本人的地址
        updateSellerAddr(chatItem,conn);
        //获取对方的地址
        buyerAddr = LocaDao.getBuyerAddr(chatItem,conn);

        //获取数据库的交易地点Tmp
        transAddr = LocaDao.getTmpTransAddrByChatID(chatItem.getChatID(),conn);
    }

    private void buyerGetFlashedAllAddr(ChatItem chatItem, Connection conn) {
        //更新本人的地址
        updateBuyerAddr(chatItem,conn);
        //获取对方的地址
        getSellerAddrMethod(chatItem,conn);

        //获取数据库的交易地点Tmp
        transAddr = LocaDao.getTmpTransAddrByChatID(chatItem.getChatID(),conn);
    }

    private void clearTmpTransAddrByChatID(ChatItem chatItem, Connection conn) {
        LocaDao.clearTmpTransAddrByChatID(chatItem,conn);
    }

    private void updateTransAddrByClick(ChatItem chatItem, Connection conn) {
        LocaDao.updateTransAddrByAddr(chatItem,conn);
    }

    private void sellerInitAllAddr(ChatItem chatItem, Connection conn) {
        //更新本人的地址
        updateSellerAddr(chatItem,conn);
        //获取对方的地址
        buyerAddr = LocaDao.getBuyerAddr(chatItem,conn);


        //先获取tmpTransAddr
        transAddr = LocaDao.getTmpTransAddrByChatID(chatItem.getChatID(),conn);
        if("".equals(transAddr) || null==transAddr|| "null".equals(transAddr)){
            //再查trandAddr
            transAddr = LocaDao.selectTransAddr(chatItem,conn);
            if("".equals(transAddr) || null==transAddr){
                initTransAddr(chatItem,conn);
                transAddr = chatItem.getUser().getAddr();
            }
        }

    }

    private void updateSellerAddr(ChatItem chatItem, Connection conn) {
        LocaDao.updateSellerAddr(chatItem,conn);
    }

    private void buyerInitAllAddr(ChatItem chatItem, Connection conn) {
        //更新本人的地址


        updateBuyerAddr(chatItem,conn);

        //获取对方的地址
        getSellerAddrMethod(chatItem,conn);

        //查询 数据库内是否有地址
        transAddr = LocaDao.selectTransAddr(chatItem,conn);


        if(null == transAddr || "".equals(transAddr) || "null".equals(transAddr)){
            initTransAddr(chatItem,conn);
            transAddr = chatItem.getUser().getAddr();
        }


    }

    private void initTransAddr(ChatItem chatItem, Connection conn) {
        LocaDao.updateTransAddrByBuyerAddr(chatItem,conn);
    }

    private void getSellerAddrMethod(ChatItem chatItem, Connection conn) {
        sellerAddr = LocaDao.getSellerAddr(chatItem,conn);
    }

    private void updateBuyerAddr(ChatItem chatItem, Connection conn) {
        LocaDao.updateBuyerAddr(chatItem,conn);
    }

    private void updateTmpTransAddrByChatID(ChatItem chatItem, Connection conn) {
        LocaDao.updateTmpTransAddrByChatID(chatItem,conn);
    }

    private void selectTransAddr(ChatItem chatItem, Connection conn) {
        transAddr = LocaDao.selectTransAddr(chatItem,conn);
    }

    private void insertLocByBSid(ChatItem chatItem, Connection conn) {
        String chatid = LocaDao.getLocaByChatID(chatItem.getChatID(),conn);
        if(null==chatid || "".equals(chatid)){
            LocaDao.insertLocByBSid(chatItem,conn);
        }
    }



}
