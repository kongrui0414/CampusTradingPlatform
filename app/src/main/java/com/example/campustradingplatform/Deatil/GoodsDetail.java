package com.example.campustradingplatform.Deatil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.Chat.ChatDetailActivity;
import com.example.campustradingplatform.Chat.Service.ChatService;
import com.example.campustradingplatform.Chat.Service.MainChatServiceThread;
import com.example.campustradingplatform.Goods.Goods;
import com.example.campustradingplatform.Goods.GoodsService;
import com.example.campustradingplatform.Goods.GoodsThread;
import com.example.campustradingplatform.R;

public class GoodsDetail extends AppCompatActivity {

    private Button creatChatBtn;
    private ChatItem chatItem;
    private Goods goods;
    TextView oldprice;
    TextView nowprice;
    TextView seller;
    TextView lauchtime;
    TextView description;
    TextView OldorNew;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);

        chatItem = (ChatItem)getIntent().getSerializableExtra("chatItem");
        goods=chatItem.getGoods();
        title=findViewById(R.id.title);
        title.setText(""+goods.getGoodsName());

        initView();
        initListener();
        getseller();
        getdescription();
        getlauchtime();
        getoldornew();
        getnowprice();
    }

    private void initView() {
        creatChatBtn  = (Button)findViewById(R.id.shopcar);
        if(chatItem.getGoods().getSellerId() == chatItem.getUser().getId()){
            creatChatBtn.setTextColor(Color.GRAY);
        }
        oldprice=findViewById(R.id.oldprice);
        chatItem = (ChatItem)getIntent().getSerializableExtra("chatItem");
        goods=chatItem.getGoods();
        oldprice.setText("原价："+goods.getOriginalPrice());
    }

    private void getseller(){
        seller=findViewById(R.id.seller_text);
        chatItem = (ChatItem)getIntent().getSerializableExtra("chatItem");
        goods=chatItem.getGoods();
        seller.setText("卖家id："+goods.getSellerId());
    }
    private void getdescription(){
        description=findViewById(R.id.description_text);
        chatItem = (ChatItem)getIntent().getSerializableExtra("chatItem");
        goods=chatItem.getGoods();
        description.setText("商品简介："+goods.getDescription());
    }
    private void getlauchtime(){
        lauchtime=findViewById(R.id.lauchtime_text);
        chatItem = (ChatItem)getIntent().getSerializableExtra("chatItem");
        goods=chatItem.getGoods();
        lauchtime.setText("上架时间："+goods.getLaunchTime());
    }
    private void getoldornew(){
        OldorNew=findViewById(R.id.OldorNew_text);
        chatItem = (ChatItem)getIntent().getSerializableExtra("chatItem");
        goods=chatItem.getGoods();
        OldorNew.setText("新旧程度："+goods.getOldorNew());
    }
    private void getnowprice(){
        nowprice=findViewById(R.id.nowprice);
        chatItem = (ChatItem)getIntent().getSerializableExtra("chatItem");
        goods=chatItem.getGoods();
        nowprice.setText("现价："+goods.getPresentPrice());
    }
    private void initListener() {
        creatChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建会话跳转
                if(chatItem.getGoods().getSellerId() == chatItem.getUser().getId()) return;

//                Log.d("TAG", "onClick: ");
                MainChatServiceThread thread =  ChatService.addChatItemByBidAndSidAndGid(chatItem);
                while(!thread.isFinished());
                ChatItem myChatItem = thread.getChatItem();

//                Log.d("TAG", "onClick: "+myChatItem);
                Intent intent = new Intent(GoodsDetail.this, ChatDetailActivity.class);
                intent.putExtra("chatItem",myChatItem);
                startActivity(intent);
            }
        });

    }

    private void getGoodsPrice(){
        GoodsThread thread= GoodsService.getGoodsOldPrice(chatItem.getGoods());
        while(!thread.isFinished());
        Log.d("tag","oldprice:"+oldprice);
    }


}