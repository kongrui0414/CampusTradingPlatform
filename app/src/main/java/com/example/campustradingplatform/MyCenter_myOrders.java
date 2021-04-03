package com.example.campustradingplatform;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.campustradingplatform.Login.User;
import com.example.campustradingplatform.Order.Order;
import com.example.campustradingplatform.Order.changeOrder;
import com.example.campustradingplatform.adapter.MyOrdersAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyCenter_myOrders extends AppCompatActivity {
    private User user;
    private changeOrder changeOrder;

    private List<Order> orderList = new ArrayList<>();

    private MyOrdersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_center_fragment_myorders);
        user = (User)getIntent().getSerializableExtra("user");
        initOrders();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.myOrder_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyOrdersAdapter(orderList);
        recyclerView.setAdapter(adapter);
    }

    private void initOrders(){
        orderList.clear();
        changeOrder = new changeOrder();
        orderList = changeOrder.getAllOrders(user);
        if(orderList == null || orderList.size() == 0){
            Toast.makeText(MyCenter_myOrders.this, "您暂时还没有订单",Toast.LENGTH_SHORT).show();
        }
    }
}