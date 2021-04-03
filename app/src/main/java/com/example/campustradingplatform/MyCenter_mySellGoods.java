package com.example.campustradingplatform;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.campustradingplatform.Goods.Goods;
import com.example.campustradingplatform.Goods.changeGoods;
import com.example.campustradingplatform.Login.User;
import com.example.campustradingplatform.adapter.SellGoodsAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MyCenter_mySellGoods extends AppCompatActivity {
    private User user;

    public static MyCenter_mySellGoods myCenter_mySellGoods;

    private List<Goods> goodsList = new ArrayList<>();

    private SellGoodsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_center_my_sell_goods);
        myCenter_mySellGoods = this;
        user = (User)getIntent().getSerializableExtra("user");
        initGoods();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.mySellGoods_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SellGoodsAdapter(goodsList);
        recyclerView.setAdapter(adapter);
    }

    private void initGoods(){
        goodsList.clear();
        goodsList = new changeGoods().getAllGoods(user);
        if(goodsList == null || goodsList.size() == 0){
            Toast.makeText(MyCenter_mySellGoods.this, "您暂时还没有在售的商品",Toast.LENGTH_SHORT).show();
        }
    }


}