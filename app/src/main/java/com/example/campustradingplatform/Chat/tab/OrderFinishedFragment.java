package com.example.campustradingplatform.Chat.tab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.campustradingplatform.Chat.BuyerOrderManyActivity;
import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.Chat.ChatBean.OrderItem;
import com.example.campustradingplatform.Chat.ChatDetailActivity;
import com.example.campustradingplatform.Chat.SellerOrderManyActivity;
import com.example.campustradingplatform.Chat.Service.ChatService;
import com.example.campustradingplatform.Chat.Service.MainChatServiceThread;
import com.example.campustradingplatform.Chat.Service.OrderService;
import com.example.campustradingplatform.Chat.Service.OrderServiceThread;
import com.example.campustradingplatform.Login.User;
import com.example.campustradingplatform.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class OrderFinishedFragment extends Fragment {
    List<OrderItem> orderItems = new ArrayList<>();
    private ListView orderListView;
    private Activity mOrderActivity;
    private User user;

    public View onCreateView(LayoutInflater inflater , ViewGroup container, Bundle savedInstanceState){
        View view=inflater .inflate(R.layout.order_finished_frag,container,false) ;

        initOrderItems();
        initView(view);

        return view;
    }

    private void initOrderItems() {

        if (mOrderActivity==null){
            mOrderActivity= getActivity();
        }

        orderItems = new ArrayList<>();
        if(getActivity() instanceof BuyerOrderManyActivity){

            user = ((BuyerOrderManyActivity)mOrderActivity).getUser();
            OrderServiceThread thread = OrderService.getFinishedOrderToBuyer(user);
            while(!thread.isFinished());

            orderItems = thread.getOrderItems();
            if(orderItems==null) orderItems = new ArrayList<>();
//            orderItems.add(new OrderItem(GlobalVars.FINISHED_ORDER));

        }else{

            user = ((SellerOrderManyActivity)mOrderActivity).getUser();

            OrderServiceThread thread = OrderService.getFinishedOrderToSeller(user);
            while(!thread.isFinished());

            orderItems = thread.getOrderItems();
            if(orderItems==null) orderItems = new ArrayList<>();

//            orderItems.add(new OrderItem(GlobalVars.SELLER_FINISHED_ORDER));
        }
    }

    public void initView(View view){
        orderListView = (ListView)view.findViewById(R.id.order_finshed_List);
        OrderAdapter orderAdapter = new OrderAdapter(view.getContext(), R.layout.order_item, orderItems);
        orderListView.setAdapter(orderAdapter);
        orderAdapter.setOrderAdapter(orderAdapter);
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderItem orderItem = orderItems.get(position);
                MainChatServiceThread thread =  ChatService.getChatByOrderItemCID(orderItem);
                while(!thread.isFinished()) {
                    try {
                        thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ChatItem chatItem = thread.getChatItem();

                Intent intent = new Intent(view.getContext(), ChatDetailActivity.class);
                intent.putExtra("chatItem",chatItem);
                startActivity(intent);
            }
        });
    }
}
