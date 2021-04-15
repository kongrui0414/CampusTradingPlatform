package com.example.campustradingplatform.Chat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campustradingplatform.Chat.ChatBean.ChatDetailItem;
import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.Chat.Component.DragFloatActionButton;
import com.example.campustradingplatform.Chat.Service.ChatDetailService;
import com.example.campustradingplatform.Chat.Service.ChatDetailServiceThread;
import com.example.campustradingplatform.Chat.Service.LocaService;
import com.example.campustradingplatform.Chat.Service.LocaServiceThread;
import com.example.campustradingplatform.Chat.Service.OrderService;
import com.example.campustradingplatform.Chat.Service.OrderServiceThread;
import com.example.campustradingplatform.Chat.msgDir.ChatDetailAdapter;
import com.example.campustradingplatform.Login.User;
import com.example.campustradingplatform.R;
import com.example.campustradingplatform.UtilTools.GlobalUtil;
import com.example.campustradingplatform.UtilTools.GlobalVars;
import com.example.campustradingplatform.UtilTools.TextUtil;
import com.example.campustradingplatform.UtilTools.TimeUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatDetailActivity extends AppCompatActivity {


    //接收到的参数，用于当前界面信息获取
    private ChatItem chatItem;
    private List<ChatDetailItem> chatDetailItems=new ArrayList<>();
    private DragFloatActionButton dragLocateBtn;
    private ListView chatScrollListView;
    private ChatDetailAdapter chatDetailAdapter;
    private TextView chatUserNameText;
    private ImageButton moreToolsbtn;
    private LinearLayout toolsLayout;
    private EditText msgSendText;
    private Button sendTextBtn;
    private ImageView sendVoiceBtn;
    private TextView sendVoiceline;
    private ImageButton returnBtn;
    private Button buyBtn;
    private TextView gPriceText;
    private TextView gNameText;
    private String hasSelled="1";
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        chatItem = (ChatItem)getIntent().getSerializableExtra("chatItem");
        user = chatItem.getUser();
//        Log.d("TAG", "onCreate: "+chatItem);

        //初始化所有内容
        initData();
        initUI();
        initListener();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){

                    flashUI();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    /**
     * methodName(方法名）
     * description:刷新当前历史消息界面
     * @param
     * @param
     */
    public void flashUI(){
        //当对方发送消息时更新当前界面
        //持续查询服务器
        if(chatDetailItems.size()==0){
            ChatDetailServiceThread thread = ChatDetailService.getLastChatDetailByMeNotHistory(chatItem);
            while(!thread.isFinished());
            List<ChatDetailItem> newChatDetailList = thread.getChatDetailItems();

            if(null!=newChatDetailList || newChatDetailList.size()!=0){
                //发送给handler处理 UI更新
                Message msg = new Message();
                msg.what = GlobalVars.UPDATE_CHAT_DETAIL_HANDLER;
//            Log.d("TAG", "flashUI: 更新的历史消息"+newChatDetailList);

                Bundle bundle = new Bundle();
                bundle.putSerializable("updateChatDetail",(Serializable) newChatDetailList);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

        }else{

            ChatDetailServiceThread thread = ChatDetailService.getLastChatDetailByLastHisAndMe(chatDetailItems.get(chatDetailItems.size()-1),chatItem.getUser());

            while(!thread.isFinished());

            List<ChatDetailItem> newChatDetailList = thread.getChatDetailItems();

            if(null!=newChatDetailList || newChatDetailList.size()!=0){
                //发送给handler处理 UI更新
                Message msg = new Message();
                msg.what = GlobalVars.UPDATE_CHAT_DETAIL_HANDLER;
//            Log.d("TAG", "flashUI: 更新的历史消息"+newChatDetailList);

                Bundle bundle = new Bundle();
                bundle.putSerializable("updateChatDetail",(Serializable) newChatDetailList);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
//        else{
//            Log.d("TAG", "flashUI: 没有需要更新的历史消息"+newChatDetailList);
//        }
        }


    }

    //增加 最新的历史消息
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {
                case GlobalVars.UPDATE_CHAT_DETAIL_HANDLER:
                    Bundle bundle  = msg.getData();
                    List<ChatDetailItem> newChatDetailItems = (List<ChatDetailItem>) bundle.getSerializable("updateChatDetail");

                    for(ChatDetailItem chatDetailItem:newChatDetailItems){
                        chatDetailItems.add(chatDetailItem);
                    }
                    chatDetailAdapter.notifyDataSetChanged();//有新消息时，刷新ListView中的显示
                    break;

                default:

                    break;
            }
        }
    };


    /**
     * methodName(方法名）
     * description:初始化按钮的监听
     * @param
     * @param
     */
    private void initListener() {
        dragLocateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ChatDetailActivity.this,"选择地址", Toast.LENGTH_SHORT).show();
                //跳转    到  地图选择界面
                //初始时，买家和卖家 都需要 初始化地址

                //1.已经出售的情况下 -------------------------
                if(chatItem.getIsSelled().equals(hasSelled)){

                    if("".equals(chatItem.getOrderID())|| null == chatItem.getOrderID()) {
                        Toast.makeText(ChatDetailActivity.this,"抱歉，该商品已出售！",Toast.LENGTH_SHORT).show();
                        return ;
                    }

                    OrderServiceThread thread =  OrderService.checkIsWaitFinish(chatItem);
                    while(!thread.isFinished());
                    if(thread.isWaitFinish()){
                        //先插入loca表信息
                        LocaService.insertLocaByBSid(chatItem);

                        Intent intent = new Intent(ChatDetailActivity.this, BaiduMapActivity.class);
                        intent.putExtra("chatItem",chatItem);
                        startActivityForResult(intent, GlobalVars.GET_RESULT_FROM_BAIDUMAP);
                    }else{
                        Toast.makeText(ChatDetailActivity.this,"抱歉，交易已完成，无法继续定位！",Toast.LENGTH_SHORT).show();
                    }
                }
                else{ //2.没有出售直接跳转------------------------
                    LocaService.insertLocaByBSid(chatItem);

                    Intent intent = new Intent(ChatDetailActivity.this, BaiduMapActivity.class);
                    intent.putExtra("chatItem",chatItem);
                    startActivityForResult(intent,GlobalVars.GET_RESULT_FROM_BAIDUMAP);
                }
            }
        });


        //tools按钮点击,显示toolsLayout
        moreToolsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVoiceline.setVisibility(View.GONE);
                msgSendText.setVisibility(View.VISIBLE);
                if(toolsLayout.getVisibility()==View.GONE){
                    GlobalUtil.hideKeyboard(ChatDetailActivity.this);
                    msgSendText.clearFocus(); //失去焦点
                    toolsLayout.setVisibility(View.VISIBLE);
                }else{
                    //设置edittext获取焦点
                    msgSendText.requestFocus();
                    GlobalUtil.getKeyBoard(msgSendText);
                    toolsLayout.setVisibility(View.GONE);
                }
            }

        });


        chatScrollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //取消软键盘
                GlobalUtil.hideKeyboard(ChatDetailActivity.this);
                msgSendText.clearFocus();
//                Toast.makeText(MsgDetailActivity.this,"点击了listview",Toast.LENGTH_SHORT).show();
            }
        });

        //取消tools的显示
        msgSendText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    toolsLayout.setVisibility(View.GONE);

                    if(TextUtil.replaceSpecialStr(msgSendText.getText().toString()).length()==0){
                        moreToolsbtn.setVisibility(View.VISIBLE);
                        sendTextBtn.setVisibility(View.GONE);
                    }
                    else{
                        moreToolsbtn.setVisibility(View.GONE);
                        sendTextBtn.setVisibility(View.VISIBLE);
                    }

                    chatScrollListView.smoothScrollToPosition(chatDetailItems.size());
                }
            }
        });



        //设置moreToolsBtn 和sendTextBtn的显示
        msgSendText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtil.replaceSpecialStr(s.toString()).length()!=0){
                    moreToolsbtn.setVisibility(View.GONE);
                    sendTextBtn.setVisibility(View.VISIBLE);
                }else{
                    moreToolsbtn.setVisibility(View.VISIBLE);
                    sendTextBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        sendTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击发送，添加聊天信息panel的内容
//                Toast.makeText(MsgDetailActivity.this,"点击发送按钮",Toast.LENGTH_SHORT).show();

                String conText = msgSendText.getText().toString();

                if(!"".equals(conText)){
                    //发送
                    ChatDetailItem chatDetailItem=null;
                    if(chatItem.getUser().isBuyer()){
                        String chatid = chatItem.getChatID();
                        User user = chatItem.getUser();
                        User seller = chatItem.getSeller();
                        chatDetailItem= new ChatDetailItem(chatid,user,user,seller,conText, TimeUtil.getCurrentTime(),true);
                    }else{
                        String chatid = chatItem.getChatID();
                        User user = chatItem.getUser();
                        User buyer = chatItem.getBuyer();
                        chatDetailItem = new ChatDetailItem(chatid,user,buyer,user,conText,TimeUtil.getCurrentTime(),true);
                    }

                    chatDetailItems.add(chatDetailItem);
                    chatDetailAdapter.notifyDataSetChanged();//有新消息时，刷新ListView中的显示
                    chatScrollListView.smoothScrollToPosition(chatDetailItems.size());//将ListView定位到最后一行

                    //将发送内容插入到数据库内
                    ChatDetailService.insertChatDetailItemBySender(chatDetailItem);


                    msgSendText.setText("");//清空输入框的内容
                }


                sendTextBtn.setVisibility(View.GONE);
                moreToolsbtn.setVisibility(View.VISIBLE);
            }
        });



        sendVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(msgSendText.getVisibility()==View.VISIBLE){
                    //开始发送语音消息
                    initInputLine();
                    sendVoiceline.setVisibility(View.VISIBLE);
                    msgSendText.setVisibility(View.GONE);
                }else{
                    //发送文字消息
                    sendVoiceline.setVisibility(View.GONE);
                    msgSendText.setVisibility(View.VISIBLE);

                    if(msgSendText.getText().toString().trim().length()!=0){
                        moreToolsbtn.setVisibility(View.GONE);
                        sendTextBtn.setVisibility(View.VISIBLE);
                        msgSendText.requestFocus();
                        GlobalUtil.getKeyBoard(msgSendText);
                    }

                }
            }
        });

        //返回按钮
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatDetailActivity.this.finish();
            }
        });

        //确认购买按钮
        buyBtn.setOnClickListener(new View.OnClickListener() {
            private Thread toPayCheckThread;

            @Override
            public void onClick(View v) {
//                Log.d("TAG", "onClick: 确认购买buyBtn");
                //跳转结算界面
                if(chatItem.getGoods().getSellerId() == chatItem.getUser().getId()){
                    Toast.makeText(ChatDetailActivity.this, "此为本人出售的商品",Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(chatItem.getIsSelled().equals("1")) {
                    Toast.makeText(ChatDetailActivity.this,"该商品正在交易或已完成交易",Toast.LENGTH_SHORT).show();
                    return ;
                }
                else if(null != chatItem.getOrderID() && !"".equals(chatItem.getOrderID()) && !"null".equals(chatItem.getOrderID())){
                    Toast.makeText(ChatDetailActivity.this,"请等待卖家处理",Toast.LENGTH_SHORT).show();
                    return ;
                }
                LocaServiceThread thread = LocaService.getTransAddr(chatItem);
                while(!thread.isFinished());
                if(!"-1".equals(thread.getTransAddr()) && !"".equals(thread.getTransAddr())&&!"null".equals(thread.getTransAddr())){
                    chatItem.setTranAddr(thread.getTransAddr());

                    Intent intent = new Intent(ChatDetailActivity.this, PayCheckActivity.class);
                    intent.putExtra("chatItem",chatItem);
                    startActivity(intent);
                }else{
                    Toast.makeText(ChatDetailActivity.this,"请先选择交易地址",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void initData() {

        ChatDetailServiceThread thread  = ChatDetailService.getChatDetailListByCID(chatItem);
//
        while(!thread.isFinished());
        this.chatDetailItems = thread.getChatDetailItems();

        if(null == this.chatDetailItems || this.chatDetailItems.size()==0){
            this.chatDetailItems = new ArrayList<>();
        }
    }

    private void initUI() {
        dragLocateBtn = (DragFloatActionButton) findViewById(R.id.img_btn);

        //初始化消息列表UI
        chatScrollListView=(ListView)findViewById(R.id.chat_scroll_list_view);
        chatDetailAdapter = new ChatDetailAdapter(ChatDetailActivity.this, R.layout.chat_detail_item, chatDetailItems);
        chatScrollListView.setAdapter(chatDetailAdapter);

        //去除item边框
        chatScrollListView.setDivider(null);

        //定位最后一行
        chatScrollListView.setSelection(chatDetailItems.size());//将ListView定位到最后一行

        chatUserNameText = (TextView)findViewById(R.id.chat_user_name);
        if(chatItem.getUser().isBuyer())
            chatUserNameText.setText(chatItem.getSeller().getUserName());
        else
            chatUserNameText.setText(chatItem.getBuyer().getUserName());

        moreToolsbtn = (ImageButton) findViewById(R.id.chat_more_tools_btn);
        toolsLayout = (LinearLayout)findViewById(R.id.chat_more_tools_layout);


        msgSendText = (EditText)findViewById(R.id.chat_edit_text);
        chatDetailAdapter.setSendText(msgSendText);

        sendTextBtn = (Button)findViewById(R.id.send_text_btn);


        sendVoiceBtn = (ImageView)findViewById(R.id.send_voice);
        sendVoiceline = (TextView)findViewById(R.id.press_to_say_btn);
        returnBtn = (ImageButton)findViewById(R.id.detail_return_btn);


        buyBtn = (Button)findViewById(R.id.comfirm_buy_btn);
        if(chatItem.getGoods().getSellerId() == chatItem.getUser().getId()){
            buyBtn.setText("本人出售");
            Drawable drawable = getResources().getDrawable(R.color.yellow_bright);
            buyBtn.setBackground(drawable);
        }
        if(chatItem.getIsSelled().equals("1")){
            buyBtn.setText("已出售");
            Drawable drawable = getResources().getDrawable(R.color.yellow_bright);
            buyBtn.setBackground(drawable);
        }
        else if(null != chatItem.getOrderID() && !"".equals(chatItem.getOrderID()) && !"null".equals(chatItem.getOrderID())){
            buyBtn.setText("已购买");
            Drawable drawable = getResources().getDrawable(R.color.yellow_bright);
            buyBtn.setBackground(drawable);
        }
        //商品信息
        gPriceText = (TextView)findViewById(R.id.g_price);
        gNameText = (TextView) findViewById(R.id.g_name);
        gPriceText.setText(String.valueOf(chatItem.getGoods().getPresentPrice()));
        gNameText.setText(chatItem.getGoods().getGoodsName());
    }

    public void initInputLine(){
        //全部取消，回到最初的状态
        GlobalUtil.hideKeyboard(ChatDetailActivity.this);
        toolsLayout.setVisibility(View.GONE);
        moreToolsbtn.setVisibility(View.VISIBLE);
        sendTextBtn.setVisibility(View.GONE);
        msgSendText.clearFocus();
    }

}