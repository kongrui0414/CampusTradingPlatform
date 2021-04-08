package com.example.campustradingplatform.Chat.Service;

import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.Chat.ChatBean.OrderItem;
import com.example.campustradingplatform.Login.User;
import com.example.campustradingplatform.UtilTools.GlobalVars;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class OrderService {

    /*
    * 在点击地图前，先判断是否是等待交易完成的状态
    * 1.是：则进入查询
    * 1.1 该商品已经下单且是本人下单 --- 进行查看
    * 1.2 该商品已经下单但 不是本人下单 --- 无法查看
    * 2.不是
    * 2.1 该商品还未下单 ---- 可以进行交流
    * 2.2 该商品已经完成 ---- 无法查看
    * */
    public static OrderServiceThread checkIsWaitFinish(ChatItem chatItem) {
        OrderServiceThread thread =new OrderServiceThread(chatItem, GlobalVars.BUYER_CHECK_IS_WAIT_FINISH);
        thread.start();
        return thread;
    }

    public static OrderServiceThread getWaitDealOrderToBuyer(User user) {
        OrderServiceThread thread = new OrderServiceThread(user, GlobalVars.WAIT_DEAL_ORDER);
        thread.start();
        return thread;
    }

    public static OrderServiceThread reqOrderRefund(OrderItem orderItem) {
        OrderServiceThread thread = new OrderServiceThread(orderItem, GlobalVars.BUTTON_REQ_REFUND);
        thread.start();
        return thread;
    }

    public static void recievedGoodsOrder(OrderItem orderItem) {
        OrderServiceThread thread =new OrderServiceThread(orderItem, GlobalVars.BUTTON_RECIEVED_GOODS);
        thread.start();
    }

    public static void refuseOrder(OrderItem orderItem) {
        OrderServiceThread thread =new OrderServiceThread(orderItem, GlobalVars.BUTTON_REFUSE_ORDER);
        thread.start();
    }

    public static void sellerAgreeOrder(OrderItem orderItem) {
        OrderServiceThread thread =new OrderServiceThread(orderItem, GlobalVars.BUTTON_AGREE_ORDER);
        thread.start();
    }

    public static void deliverOrder(OrderItem orderItem) {
        OrderServiceThread thread =new OrderServiceThread(orderItem, GlobalVars.BUTTON_DELIVER_GOODS);
        thread.start();
    }

    public static void refuseOrderRefund(OrderItem orderItem) {
        OrderServiceThread thread =new OrderServiceThread(orderItem, GlobalVars.BUTTON_REFUSE_REFUND);
        thread.start();
    }

    public static void agreeOrderRefund(OrderItem orderItem) {
        OrderServiceThread thread =new OrderServiceThread(orderItem, GlobalVars.BUTTON_AGREE_REFUND);
        thread.start();
    }

    public static void sellerCancelOrder(OrderItem orderItem) {
        OrderServiceThread thread =new OrderServiceThread(orderItem, GlobalVars.BUTTON_SELLER_CANCEL_ORDER);
        thread.start();
    }

    public static OrderServiceThread getWaitDealOrderToSeller(User user) {
        OrderServiceThread thread = new OrderServiceThread(user, GlobalVars.SELLER_WAIT_DEAL_ORDER);
        thread.start();
        return thread;
    }

    public static OrderServiceThread getWaitFinishOrderToBuyer(User user) {
        OrderServiceThread thread = new OrderServiceThread(user, GlobalVars.WAIT_FINISH_ORDER);
        thread.start();
        return thread;
    }

    public static OrderServiceThread getWaitFinishOrderToSeller(User user) {
        OrderServiceThread thread = new OrderServiceThread(user, GlobalVars.SELLER_WAIT_FINISH_ORDER);
        thread.start();
        return thread;
    }

    public static OrderServiceThread getWaitRefundOrderToBuyer(User user) {
        OrderServiceThread thread = new OrderServiceThread(user, GlobalVars.WAIT_REFUND_ORDER);
        thread.start();
        return thread;
    }

    public static OrderServiceThread getWaitRefundOrderToSeller(User user) {

        OrderServiceThread thread = new OrderServiceThread(user, GlobalVars.SELLER_WAIT_REFUND_ORDER);
        thread.start();
        return thread;
    }

    public static OrderServiceThread getFinishedOrderToBuyer(User user) {
        OrderServiceThread thread = new OrderServiceThread(user, GlobalVars.FINISHED_ORDER);
        thread.start();
        return thread;
    }

    public static OrderServiceThread getFinishedOrderToSeller(User user) {
        OrderServiceThread thread = new OrderServiceThread(user, GlobalVars.SELLER_FINISHED_ORDER);
        thread.start();
        return thread;
    }
}
