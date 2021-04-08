package com.example.campustradingplatform.Chat.tab;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.campustradingplatform.Chat.ChatBean.OrderItem;
import com.example.campustradingplatform.Chat.Service.OrderService;
import com.example.campustradingplatform.Chat.Service.OrderServiceThread;
import com.example.campustradingplatform.R;
import com.example.campustradingplatform.UtilTools.GlobalVars;

import java.util.List;

/**
 * @author: 竹林
 * @date: 2021/4/2
 */
public class OrderAdapter extends ArrayAdapter<OrderItem> {

    List<OrderItem> orderItems;
    Context context;
    private Button btn1;
    private Button btn2;
    private TextView state;
    private TextView pName;
    private TextView gName;
    private TextView gPrice;

    OrderAdapter orderAdapter;

    public OrderAdapter(@NonNull Context context, int resource, List<OrderItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.orderItems = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        OrderItem orderItem = getItem(position);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);

        btn1 = (Button)view.findViewById(R.id.order_op_left);
        btn2 =(Button)view.findViewById(R.id.order_op_right);
        state = (TextView)view.findViewById(R.id.order_state);
        pName = (TextView)view.findViewById(R.id.person_name);
        gName =(TextView)view.findViewById(R.id.g_name_order);
        gPrice = (TextView)view.findViewById(R.id.g_price_order);


        if(orderItem.getGoods()!=null){
            gName.setText(orderItem.getGoods().getGoodsName());
            gPrice.setText(String.valueOf(orderItem.getGoods().getPresentPrice()));
        }

        initView(orderItem);

        return view;
    }

    private void initView(OrderItem orderItem) {
        String mode = orderItem.getOrderState();
        if(mode.equals(GlobalVars.WAIT_DEAL_ORDER)){

            addListenerToBtn(btn2,GlobalVars.BUTTON_REQ_REFUND,orderItem);

            state.setText("已付款,等待买家同意");
            if(orderItem.getSeller()!=null)
                pName.setText("卖家："+orderItem.getSeller().getUserName());

        }
        else if(mode.equals(GlobalVars.WAIT_FINISH_ORDER)){

            addListenerToBtn(btn1,GlobalVars.BUTTON_REQ_REFUND,orderItem);

            if(orderItem.getSeller()!=null)
                pName.setText("卖家："+orderItem.getSeller().getUserName());

            if(orderItem.getOrderDBState().equals(GlobalVars.SELLER_AGREE_DB)){
                state.setText("等待发货");
            }
            if(orderItem.getOrderDBState().equals(GlobalVars.SELLER_HAS_DELIVER_DB)){
                state.setText("卖家已发货");
                addListenerToBtn(btn2,GlobalVars.BUTTON_RECIEVED_GOODS,orderItem);
            }
            else if(orderItem.getOrderDBState().equals(GlobalVars.SELLER_DISAGREE_REFUND_DB)){
                state.setText("卖家拒绝退款，继续交易");
            }
        }
        else if(mode.equals(GlobalVars.FINISHED_ORDER)){
            btn2.setVisibility(View.GONE);
            if(orderItem.getSeller()!=null)
                pName.setText("卖家："+orderItem.getSeller().getUserName());
            if(orderItem.getOrderDBState().equals(GlobalVars.SELLER_AGREE_REFUND_DB)){
                state.setText("退款成功");
            }
            if(orderItem.getOrderDBState().equals(GlobalVars.SELLER_DISAGREE_DB)){
                state.setText("卖家拒绝订单");
            }
            else if(orderItem.getOrderDBState().equals(GlobalVars.BUYER_RECIEVED_DB)){
                state.setText("已收货");
            }else if(orderItem.getOrderDBState().equals(GlobalVars.SELLER_CANCEL_ORDER_DB)){
                state.setText("卖家已取消订单");
            }
        }
        else if(mode.equals(GlobalVars.WAIT_REFUND_ORDER)){

            if(orderItem.getSeller()!=null)
                pName.setText("卖家："+orderItem.getSeller().getUserName());

            if(orderItem.getOrderDBState().equals(GlobalVars.BUYER_REQ_REFUND_DB)){
                state.setText("已发起退款申请");
            }
            if(orderItem.getOrderDBState().equals(GlobalVars.SELLER_AGREE_REFUND_DB)){
                state.setText("退款成功");
            }
            else if(orderItem.getOrderDBState().equals(GlobalVars.SELLER_DISAGREE_REFUND_DB)){
                state.setText("卖家拒绝退款");
                addListenerToBtn(btn2,GlobalVars.BUTTON_REQ_REFUND,orderItem);
            }
        }
        else if(mode.equals(GlobalVars.SELLER_WAIT_DEAL_ORDER)){

            addListenerToBtn(btn1,GlobalVars.BUTTON_REFUSE_ORDER,orderItem);
            addListenerToBtn(btn2,GlobalVars.BUTTON_AGREE_ORDER,orderItem);

            state.setText("买家正在等待处理");
            if(orderItem.getBuyer()!=null)
                pName.setText("买家："+orderItem.getBuyer().getUserName());
        }
        else if(mode.equals(GlobalVars.SELLER_WAIT_FINISH_ORDER)){


            //可以设置取消订单按钮----------------------------------
            if(orderItem.getBuyer()!=null)
                pName.setText("买家："+orderItem.getBuyer().getUserName());

            if(orderItem.getOrderDBState().equals(GlobalVars.SELLER_AGREE_DB)){
                state.setText("买家已付款");
                addListenerToBtn(btn2,GlobalVars.BUTTON_DELIVER_GOODS,orderItem);
            }
            if(orderItem.getOrderDBState().equals(GlobalVars.SELLER_HAS_DELIVER_DB)){
                state.setText("需要前往交易点");
                addListenerToBtn(btn2,GlobalVars.BUTTON_SELLER_CANCEL_ORDER,orderItem);
            }
            else if(orderItem.getOrderDBState().equals(GlobalVars.SELLER_DISAGREE_REFUND_DB)){
                state.setText("已拒绝退款，继续交易");
                addListenerToBtn(btn2,GlobalVars.BUTTON_DELIVER_GOODS,orderItem);
            }
        }
        else if(mode.equals(GlobalVars.SELLER_FINISHED_ORDER) ){
            btn2.setVisibility(View.GONE);
            if(orderItem.getBuyer()!=null)
                pName.setText("买家："+orderItem.getBuyer().getUserName());

            if(orderItem.getOrderDBState().equals(GlobalVars.SELLER_AGREE_REFUND_DB)){
                state.setText("已同意退款");
            }
            if(orderItem.getOrderDBState().equals(GlobalVars.SELLER_DISAGREE_DB)){
                state.setText("已拒绝订单");
            }
            else if(orderItem.getOrderDBState().equals(GlobalVars.BUYER_RECIEVED_DB)){
                state.setText("订单完成");
            }else if(orderItem.getOrderDBState().equals(GlobalVars.SELLER_CANCEL_ORDER_DB)){
                state.setText("卖家已取消订单");
            }

        }
        else if(mode.equals(GlobalVars.SELLER_WAIT_REFUND_ORDER) ){

            if(orderItem.getBuyer()!=null)
                pName.setText("买家："+orderItem.getBuyer().getUserName());

            if(orderItem.getOrderDBState().equals(GlobalVars.BUYER_REQ_REFUND_DB)){
                state.setText("等待退款处理");

                addListenerToBtn(btn1,GlobalVars.BUTTON_REFUSE_REFUND,orderItem);
                addListenerToBtn(btn2,GlobalVars.BUTTON_AGREE_REFUND,orderItem);
            }
            if(orderItem.getOrderDBState().equals(GlobalVars.SELLER_AGREE_REFUND_DB)){
                state.setText("已同意退款");
            }
            else if(orderItem.getOrderDBState().equals(GlobalVars.SELLER_DISAGREE_REFUND_DB)){
                state.setText("已拒绝退款");
            }
        }
    }

    public void addListenerToBtn(Button button,int mode,OrderItem orderItem){
        //退款
        button.setVisibility(View.VISIBLE);
        if(mode == GlobalVars.BUTTON_REQ_REFUND){
            button.setText("申请退款");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    OrderServiceThread thread = OrderService.reqOrderRefund(orderItem);
                    while(!thread.isFinished());
                    if(thread.getRefundState()==GlobalVars.REQ_REFUND_SUCCESS){
                        Log.d("TAG", "addListenerToBtn: 退款成功，需要刷新界面");

                    }else{
                        Log.d("TAG", "addListenerToBtn: 退款请求已发送，需要刷新界面");
                    }
                    orderItems.remove(orderItem);
                    orderAdapter.notifyDataSetChanged();
                }
            });
        }
        else if(mode == GlobalVars.BUTTON_RECIEVED_GOODS){
            button.setText("确认收货");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderService.recievedGoodsOrder(orderItem);
                    Log.d("TAG", "addListenerToBtn: 确认收货");
                    orderItems.remove(orderItem);
                    orderAdapter.notifyDataSetChanged();
                }
            });
        }
        else if(mode == GlobalVars.BUTTON_REFUSE_ORDER){
            button.setText("拒绝订单");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //刷新界面---------------------
                    OrderService.refuseOrder(orderItem);
                    Log.d("TAG", "addListenerToBtn: 拒绝订单");
                    orderItems.remove(orderItem);
                    orderAdapter.notifyDataSetChanged();
                }
            });
        }
        else if(mode == GlobalVars.BUTTON_AGREE_ORDER){
            button.setText("同意订单");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderService.sellerAgreeOrder(orderItem);
                    Log.d("TAG", "addListenerToBtn: 同意订单");
                    orderItems.remove(orderItem);
                    orderAdapter.notifyDataSetChanged();
                }
            });
        }
        else if(mode == GlobalVars.BUTTON_DELIVER_GOODS){
            button.setText("发货");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //刷新界面----------------------------------------------
                    OrderService.deliverOrder(orderItem);
                    Log.d("TAG", "addListenerToBtn: 发货");
                    state.setText("已发货");
                }
            });
        }
        else if(mode == GlobalVars.BUTTON_REFUSE_REFUND){
            button.setText("拒绝退款");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderService.refuseOrderRefund(orderItem);
                   //刷新界面--------------------------------
                    Log.d("TAG", "addListenerToBtn: 拒绝退款");
                    state.setText("已拒绝付款，继续交易");
                }
            });
        }
        else if(mode == GlobalVars.BUTTON_AGREE_REFUND){
            button.setText("同意退款");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderService.agreeOrderRefund(orderItem);
                    Log.d("TAG", "addListenerToBtn: 同意退款");
                    state.setText("退款成功");
                }
            });
        }
        else if(mode == GlobalVars.BUTTON_SELLER_CANCEL_ORDER){
            button.setText("取消订单");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderService.sellerCancelOrder(orderItem);
                    Log.d("TAG", "addListenerToBtn: 卖家取消订单");
                    orderItems.remove(orderItem);
                    orderAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    public void setOrderAdapter(OrderAdapter orderAdapter) {
        this.orderAdapter = orderAdapter;
    }
}
