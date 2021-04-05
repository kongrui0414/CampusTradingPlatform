package com.example.campustradingplatform.Chat.Service;

import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.Chat.ChatBean.OrderItem;
import com.example.campustradingplatform.Chat.dao.BuyedGoodsDao;
import com.example.campustradingplatform.Chat.dao.DBConnection;
import com.example.campustradingplatform.Chat.dao.GoodsDao;
import com.example.campustradingplatform.Chat.dao.MainChatDao;
import com.example.campustradingplatform.Chat.dao.OrderDao;
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
public class OrderServiceThread extends Thread{



    String mode="-1";
    private int btnMode;


    //接收的参数
    ChatItem chatItem;
    private boolean isWaitFinish=false;
    User user;
    private OrderItem orderItem;


    //发送的参数
    private boolean isFinished=false;
    private boolean ifAllowCheckMap = false;
    private List<OrderItem> orderItems;
    private int refundState=-1;


    public OrderServiceThread(ChatItem chatItem, String mode) {
        this.chatItem = chatItem;
        this.mode = mode;
    }



    public OrderServiceThread(User user, String mode) {
        this.user = user;
        this.mode = mode;
    }

    public OrderServiceThread(OrderItem orderItem, int btnMode) {
        this.btnMode = btnMode;
        this.orderItem = orderItem;
    }

    public int getRefundState() {
        return refundState;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isWaitFinish() {
        return isWaitFinish;
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

        switch (mode){
            case GlobalVars.WAIT_DEAL_ORDER:
                getWaitDealForBuyer(user,conn);
                break;
            case GlobalVars.SELLER_WAIT_DEAL_ORDER:
                getWaitDealForSeller(user,conn);
                break;
            case GlobalVars.WAIT_FINISH_ORDER:
                getWaitFinishForBuyer(user,conn);
                break;
            case GlobalVars.SELLER_WAIT_FINISH_ORDER:
                getWaitFinishForSeller(user,conn);
                break;
            case GlobalVars.FINISHED_ORDER:
                getFinishedOrderToBuyer(user,conn);
                break;
            case GlobalVars.SELLER_FINISHED_ORDER:
                getFinishedOrderToSeller(user,conn);
                break;
            case GlobalVars.WAIT_REFUND_ORDER:
                getWaitRefundOrderToBuyer(user,conn);
                break;
            case GlobalVars.SELLER_WAIT_REFUND_ORDER:
                getWaitRefundOrderToSeller(user,conn);
                break;
            case GlobalVars.BUYER_CHECK_IS_WAIT_FINISH:
                buyerCheckIsWaitFinishOrder(chatItem,conn);
                break;
            default:break;
        }

        switch (btnMode){
            case GlobalVars.BUTTON_REQ_REFUND:
                //先查看是否只是提交了，卖家没同意
                buyerReqRefund(orderItem,conn);
                break;
            case GlobalVars.BUTTON_REFUSE_REFUND:
                sellerRefuseRefund(orderItem,conn);
                break;
            case GlobalVars.BUTTON_DELIVER_GOODS:
                deliverGoods(orderItem,conn);
                break;
            case GlobalVars.BUTTON_RECIEVED_GOODS:
                recievedGoodsOrder(orderItem,conn);
                break;
            case GlobalVars.BUTTON_REFUSE_ORDER:
                sellerRefuseOrder(orderItem,conn);
                break;
            case GlobalVars.BUTTON_AGREE_ORDER:
                sellerAgreeOrder(orderItem,conn);
                break;
            case GlobalVars.BUTTON_AGREE_REFUND:
                sellerAgreeRefund(orderItem,conn);
                break;
            case GlobalVars.BUTTON_SELLER_CANCEL_ORDER:
                sellerCancelOrder(orderItem,conn);
                break;
            default:break;
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

    private void getFinishedOrderToSeller(User user, Connection conn) {
        List<String> states = new ArrayList<>();
        states.add(GlobalVars.SELLER_DISAGREE_DB);
        states.add(GlobalVars.SELLER_AGREE_REFUND_DB);
        states.add(GlobalVars.SELLER_CANCEL_ORDER_DB);
        orderItems = OrderDao.getOrderBySidAndStatesInGoods(user,states,GlobalVars.SELLER_FINISHED_ORDER,conn);

        List<OrderItem> orderItems2 = OrderDao.getOrderBySidAndStateInGoods(user,GlobalVars.BUYER_RECIEVED_DB,GlobalVars.SELLER_FINISHED_ORDER,conn);
        orderItems.addAll(orderItems2);
    }

    private void getFinishedOrderToBuyer(User user, Connection conn) {
        //拒绝订单、正常结束 和 同意退款  --- 结束
        List<String> states = new ArrayList<>();
        states.add(GlobalVars.SELLER_DISAGREE_DB);
        states.add(GlobalVars.SELLER_AGREE_REFUND_DB);
        states.add(GlobalVars.SELLER_CANCEL_ORDER_DB);
        orderItems = OrderDao.getOrderByBidAndStatesInGoods(user,states,GlobalVars.FINISHED_ORDER,conn);

        List<OrderItem> orderItems2 = OrderDao.getOrderByBidAndStateInGoods(user,GlobalVars.BUYER_RECIEVED_DB,GlobalVars.FINISHED_ORDER,conn);
        orderItems.addAll(orderItems2);
    }

    private void getWaitRefundOrderToSeller(User user, Connection conn) {

        List<String> states = new ArrayList<>();
        states.add(GlobalVars.BUYER_REQ_REFUND_DB);
        states.add(GlobalVars.SELLER_AGREE_REFUND_DB);

        orderItems = OrderDao.getOrderBySidAndStatesInGoods(user,states,GlobalVars.SELLER_WAIT_REFUND_ORDER,conn);

        List<OrderItem> orderItems2 = OrderDao.getOrderBySidAndStateInBuyedGoods(user,GlobalVars.SELLER_DISAGREE_REFUND_DB,GlobalVars.SELLER_WAIT_REFUND_ORDER,conn);
        orderItems.addAll(orderItems2);
    }

    private void getWaitRefundOrderToBuyer(User user, Connection conn) {
        //退款处理，拒绝退款，等待同意
        List<String> states = new ArrayList<>();
        states.add(GlobalVars.BUYER_REQ_REFUND_DB);
        states.add(GlobalVars.SELLER_AGREE_REFUND_DB);
        orderItems = OrderDao.getOrderByBidAndStatesInGoods(user,states,GlobalVars.WAIT_REFUND_ORDER,conn);

        List<OrderItem> orderItems2 = OrderDao.getOrderByBidAndStateInBuyedGoods(user,GlobalVars.SELLER_DISAGREE_REFUND_DB,GlobalVars.WAIT_REFUND_ORDER,conn);
        orderItems.addAll(orderItems2);
    }

    private void getWaitFinishForSeller(User user, Connection conn) {
        List<String> states = new ArrayList<>();
        states.add(GlobalVars.SELLER_AGREE_DB);
        states.add(GlobalVars.SELLER_HAS_DELIVER_DB);
        states.add(GlobalVars.SELLER_DISAGREE_REFUND_DB);
        orderItems = OrderDao.getOrderBySidAndStatesInBuyedGoods(user,states,GlobalVars.SELLER_WAIT_FINISH_ORDER,conn);
    }

    /**
     * description:只有卖家同意了，才属于 需要 等待交易的类型，
     *              如果卖家不同意，就直接结束交易了
     */
    private void getWaitFinishForBuyer(User user, Connection conn) {
        List<String> states = new ArrayList<>();
        states.add(GlobalVars.SELLER_AGREE_DB);
        states.add(GlobalVars.SELLER_HAS_DELIVER_DB);
        states.add(GlobalVars.SELLER_DISAGREE_REFUND_DB);
        orderItems = OrderDao.getOrderByBidAndStatesInBuyedGoods(user,states,GlobalVars.WAIT_FINISH_ORDER,conn);
    }

    private void getWaitDealForSeller(User user, Connection conn) {
        //买家提交
        orderItems = OrderDao.getOrderBySidAndStateInGoods(user,GlobalVars.BUYER_SUBMIT_DB,GlobalVars.SELLER_WAIT_DEAL_ORDER,conn);
    }

    private void sellerCancelOrder(OrderItem orderItem, Connection conn) {
        //需要把goods重新发出
        int id = GoodsDao.insertGoods(orderItem.getGoods(),conn);
//        Log.d(TAG, "插入结果的id：" + id+"sellerAgreeOrder: "+orderItem.getGoods());
//
//        //删除buyedGoods
        BuyedGoodsDao.deletGoods(orderItem.getGoods(),conn);
//
//
//        //更新所有的会话的商品id
        MainChatDao.updateChatGoodsSellStateByGidAndBuyedGid(orderItem.getGoods(),id,0,conn);
//
        OrderDao.updateOrderStateAndGIDByOrderItemAndState(orderItem,id,GlobalVars.SELLER_CANCEL_ORDER_DB,conn);

    }

    private void sellerAgreeRefund(OrderItem orderItem, Connection conn) {
        int id = GoodsDao.insertGoods(orderItem.getGoods(),conn);
//        Log.d(TAG, "插入结果的id：" + id+"sellerAgreeOrder: "+orderItem.getGoods());
//
//        //删除buyedGoods
        BuyedGoodsDao.deletGoods(orderItem.getGoods(),conn);
//
//
//        //更新所有的会话的商品id
        MainChatDao.updateChatGoodsSellStateByGidAndBuyedGid(orderItem.getGoods(),id,0,conn);
//
        OrderDao.updateOrderStateAndGIDByOrderItemAndState(orderItem,id,GlobalVars.SELLER_AGREE_REFUND_DB,conn);

    }

    private void sellerRefuseRefund(OrderItem orderItem, Connection conn) {
        OrderDao.updateOrderStateByOrderItemAndState(orderItem,GlobalVars.SELLER_DISAGREE_REFUND_DB,conn);
    }

    private void deliverGoods(OrderItem orderItem, Connection conn) {
        OrderDao.updateOrderStateByOrderItemAndState(orderItem,GlobalVars.SELLER_HAS_DELIVER_DB,conn);
    }

    private void sellerAgreeOrder(OrderItem orderItem, Connection conn) {
        //将goods 加入到 buyed—goods表内
        int id = BuyedGoodsDao.insertGoods(orderItem.getGoods(),conn);
//        Log.d(TAG, "插入结果的id：" + id+"sellerAgreeOrder: "+orderItem.getGoods());

        GoodsDao.deleGoods(orderItem.getGoods(),conn);


        MainChatDao.updateChatGoodsSellStateByGidAndBuyedGid(orderItem.getGoods(),id,1,conn);

        OrderDao.updateOrderStateAndGIDByOrderItemAndState(orderItem,id,GlobalVars.SELLER_AGREE_DB,conn);
    }


    private void sellerRefuseOrder(OrderItem orderItem, Connection conn) {
        OrderDao.updateOrderStateByOrderItemAndState(orderItem,GlobalVars.SELLER_DISAGREE_DB,conn);
    }

    private void recievedGoodsOrder(OrderItem orderItem, Connection conn) {
        OrderDao.updateOrderStateByOrderItemAndState(orderItem,GlobalVars.BUYER_RECIEVED_DB,conn);
    }

    private void buyerReqRefund(OrderItem orderItem, Connection conn) {
        OrderItem selectedOrderItem  = OrderDao.getOrderStateByOrderItem(orderItem,conn);//查询到的orderitem
        //如果当前是已经提交，或者 卖家不同意 就直接删表操作；
        //其他所有操作都直接更新为 请求退款
        boolean flag = selectedOrderItem.getOrderDBState().equals(GlobalVars.BUYER_SUBMIT_DB);
        flag = flag | selectedOrderItem.getOrderDBState().equals(GlobalVars.SELLER_DISAGREE_DB);
        if(flag){
            //删表
            refundState =GlobalVars.REQ_REFUND_SUCCESS;
            //更新MainChat表的Order为空
            MainChatDao.updateOrderIDByOIDAndChatID("",orderItem.getChatID(),conn);
            OrderDao.deletOrderByOrderItem(orderItem,conn);
        }else{
            refundState =GlobalVars.REQ_REFUND_SEND;
            OrderDao.updateOrderStateByOrderItemAndState(orderItem,GlobalVars.BUYER_REQ_REFUND_DB,conn);
        }
    }

    private void getWaitDealForBuyer(User user, Connection conn) {
        //买家提交
        orderItems = OrderDao.getOrderByBidAndStateInGoods(user,GlobalVars.BUYER_SUBMIT_DB,GlobalVars.WAIT_DEAL_ORDER,conn);
    }


    /**
     * description:买家查看是不是待完成的订单
     */
    private void buyerCheckIsWaitFinishOrder(ChatItem chatItem, Connection conn) {
        String state = OrderDao.getOrderStateByOrderID(chatItem.getOrderID(),conn);
        List<String> waitFinishedState = new ArrayList<>();
        waitFinishedState.add(GlobalVars.SELLER_AGREE_DB);
        waitFinishedState.add(GlobalVars.SELLER_HAS_DELIVER_DB);
        waitFinishedState.add(GlobalVars.SELLER_DISAGREE_REFUND_DB);

        isWaitFinish = waitFinishedState.contains(state);
    }

}
