package com.example.campustradingplatform.UtilTools;

/**
 * @author: 竹林
 * @date: 2021/3/31
 */
public class GlobalVars {


    //用于ChatDetailServiceThread 用于不同dao处理使用
    public static  final int GET_CHAT_DETAIL_LIST_THREAD=1;  //获取历史消息
    public static  final int INSERT_CHAT_DETAIL_THREAD=2;      //插入历史消息
    public static  final int UPDATE_CHAT_DETAIL_THREAD=3;    //获取最新的历史消息



    //用于ChatDetailActivity更新 界面
    public static final int UPDATE_CHAT_DETAIL_HANDLER = 4;


    //用于LocaServiceThread 不同dao操作
    public static final int UPDATE_BUYER_ADDR_THREAD = 5;
    public static final int DELET_BUYER_ADDR_THREAD = 6;
    public static final int GET_SELLER_ADDR_THREAD = 7;


    //用于LocaActivity 更新 界面
    public static final int CLEAR_TMP_TRANS_ADDR_THREAD = 31;
    public static final int UPDATE_BUYER_ADDR_HANDLER = 29;
    public static final int UPDATE_TMP_TRANS_ADDR_THREAD = 30;
    public static final int INIT_SELLER_ADDR_HANDLER = 32;
    public static final int INIT_BUYER_ADDR_HANDLER = 33;
    public static final int FlASHED_ADDR_THREAD=34;

    public static final int UPDATE_SELLER_ADDR_HANDLER=8;
    public static final int UPDATE_TRANS_ADDR_HANDLER = 9;
    public static final int INIT_ADDR_THREAD = 10;
    public static final int UPDATE_TRANS_ADDR_THREAD = 11;
    public static final int COMFIRM_ORDER = 12;
    public static final int SELECT_TRANS_ADDR_THREAD = 13;

    //用于百度Activity 返回 ChatDetailACtivity界面 的 transAddr数据
    public static final int GET_RESULT_FROM_BAIDUMAP = 14;

    public static final int BUTTON_REQ_REFUND=15;  //请求退款
    public static final int BUTTON_REFUSE_REFUND=16; //拒绝退款
    public static final int BUTTON_AGREE_REFUND=17; //同意退款
    public static final int BUTTON_DELIVER_GOODS=18; //发货
    public static final int BUTTON_RECIEVED_GOODS=19; //收货
    public static final int BUTTON_AGREE_ORDER=20; //同意订单
    public static final int BUTTON_REFUSE_ORDER=21; //拒绝订单
    public static final int BUTTON_SELLER_CANCEL_ORDER=22; //拒绝订单


    //用于退款请求点击时
    public static final int REQ_REFUND_SUCCESS=23; //退款请求完成
    public static final int REQ_REFUND_SEND=24; //退款请求发送

    //用于消息页面 获取最新历史消息
    public static final int CHAT_DETAIL_GET_LAST_NOT_HIS = 25;


    //插入新的会话 MainChatThread
    public static final int BUYER_ADD_NEW_CHAT = 26;
    public static final int GET_CHAT_LIST_THREAD = 27;
    public static final int GET_CHAT_ITEM_THREAD = 35;

    //用于地图插入
    public static final int INSERT_LOC_BY_BSID_THREAD = 28;


    //用于商品显示
    public static final int GET_GOODS_LIST_THREAD = 36;
    public static final int GET_GOODS_OLDPRICE = 37;
    public static final int GET_GOODS_NOWPRICE = 38;
    //---------39start

    //用于判断订单状态  --- 界面
    public static final String WAIT_DEAL_ORDER = "0";
    public static final String WAIT_FINISH_ORDER = "1";
    public static final String FINISHED_ORDER = "2";
    public static final String WAIT_REFUND_ORDER = "3";

    public static final String SELLER_WAIT_DEAL_ORDER = "4";
    public static final String SELLER_WAIT_FINISH_ORDER = "5";
    public static final String SELLER_FINISHED_ORDER = "6";
    public static final String SELLER_WAIT_REFUND_ORDER = "7";

    //跳转到地图时使用
    public static final String BUYER_CHECK_IS_WAIT_FINISH = "8";




    //用于判断订单状态  --- 数据库
    public static final String BUYER_SUBMIT_DB="0";
    public static final String SELLER_AGREE_DB="1";
    public static final String SELLER_DISAGREE_DB="2";
    public static final String SELLER_HAS_DELIVER_DB ="3";
    public static final String BUYER_RECIEVED_DB="4";
    public static final String BUYER_REQ_REFUND_DB="5";
    public static final String SELLER_AGREE_REFUND_DB="6";
    public static final String SELLER_DISAGREE_REFUND_DB="7"; //与3的状态相同
    public static final String SELLER_CANCEL_ORDER_DB = "8";


    public static final String IF_BUYER_CAN_CHECK_MAP = "9";

}
