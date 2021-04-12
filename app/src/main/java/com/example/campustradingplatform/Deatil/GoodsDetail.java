package com.example.campustradingplatform.Deatil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.Chat.ChatDetailActivity;
import com.example.campustradingplatform.Chat.Service.ChatService;
import com.example.campustradingplatform.Chat.Service.MainChatServiceThread;
import com.example.campustradingplatform.R;

public class GoodsDetail extends AppCompatActivity {

    private Button creatChatBtn;
    private ChatItem chatItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);

        chatItem = (ChatItem)getIntent().getSerializableExtra("chatItem");

        initView();
        initListener();
    }

    private void initView() {
        creatChatBtn  = (Button)findViewById(R.id.shopcar);
        if(chatItem.getGoods().getSellerId() == chatItem.getUser().getId()){
            creatChatBtn.setTextColor(Color.GRAY);
        }
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


}