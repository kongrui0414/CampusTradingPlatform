package com.example.campustradingplatform.Msg;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.example.campustradingplatform.R;
import com.example.campustradingplatform.UtilTools.GlobalUtil;
import com.example.campustradingplatform.UtilTools.TextUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.leancloud.im.v2.AVIMClient;
import cn.leancloud.im.v2.AVIMConversation;
import cn.leancloud.im.v2.AVIMConversationsQuery;
import cn.leancloud.im.v2.AVIMException;
import cn.leancloud.im.v2.AVIMMessage;
import cn.leancloud.im.v2.AVIMMessageHandler;
import cn.leancloud.im.v2.callback.AVIMClientCallback;
import cn.leancloud.im.v2.callback.AVIMConversationCallback;
import cn.leancloud.im.v2.callback.AVIMConversationQueryCallback;
import cn.leancloud.im.v2.callback.AVIMMessagesQueryCallback;
import cn.leancloud.im.v2.messages.AVIMTextMessage;


//先选定地址 -- > 确定购买

public class MsgDetailActivity extends AppCompatActivity {

    boolean isLeft = false;

    ImageView sendVoiceBtn;
    EditText msgSendText;
    ListView chatScrollListView;
    MsgRowAdapter msgAdapter;
    ImageButton moreToolsbtn;
    LinearLayout toolsLayout;
    Button sendTextBtn;
    ArrayList<MsgRowItem> msgRowItems = new ArrayList<>();
    TextView sendVoiceline;
    ImageButton returnBtn;

    String mUserName = "kp";

    DragFloatActionButton dragLocateBtn;

    private AVIMClient tom;
    private Button buyBtn;


    AVIMConversation mConv=null;
    AVIMMessage lastestMsg=null;
    private boolean convIsQuery = true;
    private TextView chatUserNameText;
    private String chatUserName="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_detail);

        MsgItem msgItem = (MsgItem)getIntent().getSerializableExtra("msgItem");
        chatUserName = msgItem.getUserName();

        String convId = msgItem.getConvId();
        Log.d("20182005050", "MsgDetailActivityonCreate: "+convId);

        tom = AVIMClient.getInstance(mUserName);

        // Tom 登录
        tom.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    // 成功打开连接
                    Log.d("20182005050", "done: tom连接成功");

                    AVIMConversationsQuery query = tom.getConversationsQuery();
                    query.whereEqualTo("objectId",convId);
                    query.findInBackground(new AVIMConversationQueryCallback(){
                        @Override
                        public void done(List<AVIMConversation> convs,AVIMException e){
                            if(e==null){
                                if(convs!=null && !convs.isEmpty()){
                                    // convs.get(0) 就是想要的 conversation
                                    mConv=convs.get(0);
                                    getMsgsByConLimit(convs.get(0),10,false);
                                }
                            }
                        }
                    });
                }
            }
        });


        // 实现runnable借口，创建多线程并启动
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        Thread.sleep(1000);
//                                Log.d("20182005050", "run: ");
                        if(lastestMsg!=null && mConv!=null && !convIsQuery){
                            convIsQuery=true;
                            getMsgsByConLimit(mConv,1,true);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }) {
        }.start();

        initUI();


        //获取当前对话 --- 所有聊天信息



//        //-------------接收会话信息----------------------------------
//        JerryRecieve();
//        // 设置全局的对话事件处理 handler
//        AVIMMessageManager.setConversationEventHandler(new CustomConversationEventHandler());
//        // 设置全局的消息处理 handler
//        AVIMMessageManager.registerDefaultMessageHandler(new CustomMessageHandler());
        initListener();

    }

    //修改listView,增加聊天记录
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    Bundle bundle  = msg.getData();
                    ArrayList<String> contents = bundle.getStringArrayList("contents");
                    boolean[] isLefts = bundle.getBooleanArray("isLefts");

                    for(int i=0;i<contents.size();i++){
                        MsgRowItem msgRowItem = new MsgRowItem(isLefts[i]);
                        msgRowItem.setMsgRowCon(contents.get(i));
                        msgRowItems.add(msgRowItem);
                    }

//                    for(String con:contents){
//
//                    }

                    msgAdapter.notifyDataSetChanged();

                    Log.d("20182005050", "MsgDetailActivityhandleMessage: handler修改UI成功");
                    break;

                default:

                    break;
            }
        }
    };



    //获取聊天记录
    /*
    * isUpdate : true表示Thread中更新呢，false表示初始化聊天记录
    * */
    private void getMsgsByConLimit(AVIMConversation conversation,int limit,boolean isUpdate){
        convIsQuery=true;
        // limit 取值范围 1~100，如调用 queryMessages 时不带 limit 参数，默认获取 20 条消息记录
        conversation.queryMessages(limit, new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> messages, AVIMException e) {
                if (e == null) {
                    // 成功获取最新 10 条消息记录
                    if(!isUpdate){
                        Log.d("20182005050", "done: lastestMsg==null");
                        updateMsgList(messages);
                    }else{
                        if(((AVIMTextMessage)messages.get(0)).getTimestamp()!=(((AVIMTextMessage)lastestMsg).getTimestamp())){
                            Log.d("20182005050", "done: 最后消息不相等");
                            updateMsgList(messages);
                        }
                    }

                }
                convIsQuery=false;
            }
        });
    }


    /*
    * 更新聊天栏的最新信息
    * */
    private void updateMsgList(List<AVIMMessage> messages) {
        ArrayList<String> contents = new ArrayList<>();

        ArrayList<Boolean> isLefts = new ArrayList<>();
        for(int i=0;i<messages.size();i++){
            Log.d("20182005050", "done: "+i+":"+((AVIMTextMessage)messages.get(i)).getText());
            Log.d("20182005050", "done:消息来自于 "+i+":"+((AVIMTextMessage)messages.get(i)).getFrom());
            Log.d("20182005050", "done:消息时间来自于 "+i+":"+((AVIMTextMessage)messages.get(i)).getTimestamp());
            if(!((AVIMTextMessage)messages.get(i)).getFrom().equals(mUserName)){
                isLefts.add(true);
                Log.d("20182005050", "done:消息来自于 "+i+":true" );
            }
            else{
                isLefts.add(false);
                Log.d("20182005050", "done:消息来自于 "+i+":false");
            }

            contents.add(((AVIMTextMessage)messages.get(i)).getText());
        }
        if(messages.size()>0){
            lastestMsg = messages.get(messages.size()-1);
        }else{
            lastestMsg=new AVIMMessage();
        }
        Message message = new Message();
        message.what = 1;
//
        Bundle bundle = new Bundle();
////
        bundle.putString("content","((AVIMTextMessage)messages.get(i)).getText()");
        bundle.putStringArrayList("contents",contents);


        boolean[] isLeftsArray =  new boolean[isLefts.size()];

        for(int i=0;i<isLefts.size();i++){
            isLeftsArray[i]=isLefts.get(i);
        }
        bundle.putBooleanArray("isLefts",isLeftsArray);
////
        message.setData(bundle);
        handler.sendMessage(message);// 发送消息
    }


    // Java/Android SDK 通过定制自己的消息事件 Handler 处理服务端下发的消息通知
    public static class CustomMessageHandler extends AVIMMessageHandler {
        /**
         * 重载此方法来处理接收消息
         *
         * @param message
         * @param conversation
         * @param client
         */
        @Override
        public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client){
            if(message instanceof AVIMTextMessage){
                Log.d("20182005050",((AVIMTextMessage)message).getText()); // Jerry，起床了
            }
        }
    }

    public  void JerryRecieve(){
        // Jerry 登录
        AVIMClient jerry = AVIMClient.getInstance("Jerry");
        jerry.open(new AVIMClientCallback(){
            @Override
            public void done(AVIMClient client, AVIMException e){
                if(e==null){
                    // 登录成功后的逻辑

                }
            }
        });
    }

    private void initUI() {
        dragLocateBtn = findViewById(R.id.img_btn);

        //聊天消息窗口
        initMsgRows();
        chatScrollListView=(ListView)findViewById(R.id.chat_scroll_list_view);
        msgAdapter = new MsgRowAdapter(MsgDetailActivity.this, R.layout.msg_row_item, msgRowItems);
        chatScrollListView.setAdapter(msgAdapter);
        //去除item边框
        chatScrollListView.setDivider(null);
        //定位最后一行
        chatScrollListView.setSelection(msgRowItems.size());//将ListView定位到最后一行

        chatUserNameText = (TextView)findViewById(R.id.chat_user_name);
        chatUserNameText.setText(chatUserName);

        moreToolsbtn = (ImageButton) findViewById(R.id.chat_more_tools_btn);
        toolsLayout = (LinearLayout)findViewById(R.id.chat_more_tools_layout);


        msgSendText = (EditText)findViewById(R.id.chat_edit_text);
        msgAdapter.setSendText(msgSendText);

        sendTextBtn = (Button)findViewById(R.id.send_text_btn);


        sendVoiceBtn = (ImageView)findViewById(R.id.send_voice);
        sendVoiceline = (TextView)findViewById(R.id.press_to_say_btn);
        returnBtn = (ImageButton)findViewById(R.id.detail_return_btn);


        buyBtn = (Button)findViewById(R.id.comfirm_buy_btn);
        

    }

    private void initListener() {
        dragLocateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MsgDetailActivity.this,"选择地址", Toast.LENGTH_SHORT).show();
            }
        });

        //tools按钮点击,显示toolsLayout
        moreToolsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVoiceline.setVisibility(View.GONE);
                msgSendText.setVisibility(View.VISIBLE);
                if(toolsLayout.getVisibility()==View.GONE){
                    GlobalUtil.hideKeyboard(MsgDetailActivity.this);
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
                GlobalUtil.hideKeyboard(MsgDetailActivity.this);
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

                    chatScrollListView.smoothScrollToPosition(msgRowItems.size());
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
                    AVIMTextMessage msg = new AVIMTextMessage();
                    msg.setText(conText);
                    // 发送消息
                    mConv.sendMessage(msg, new AVIMConversationCallback() {
                        @Override
                        public void done(AVIMException e) {
                            if (e == null) {
                                Log.d("20182005050", "发送成功！");
                            }
                        }
                    });

//                    MsgRowItem msgRowItem = new MsgRowItem(false);
//                    msgRowItem.setMsgRowCon(conText);
//                    msgRowItems.add(msgRowItem);
//
//                    msgAdapter.notifyDataSetChanged();//有新消息时，刷新ListView中的显示
                    chatScrollListView.smoothScrollToPosition(msgRowItems.size());//将ListView定位到最后一行

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
                MsgDetailActivity.this.finish();
            }
        });


        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // Tomm 创建了一个 client，用自己的名字作为 clientId 登录
                // clientId 为 Tom
                AVIMClient tomm = AVIMClient.getInstance("Jerry");

                    // Tom 登录
                tomm.open(new AVIMClientCallback() {
                        @Override
                        public void done(AVIMClient client, AVIMException e) {
                            if (e == null) {
                                // 成功打开连接
                                //创建对话 AVIMConversation,Tom已经登录
                                AVIMConversationsQuery query = tomm.getConversationsQuery();
                                query.whereContainsIn("m", Arrays.asList("kp"));
                                query.findInBackground(new AVIMConversationQueryCallback(){
                                    @Override
                                    public void done(List<AVIMConversation> convs,AVIMException e){
                                        if(e==null){
                                            if(convs!=null && !convs.isEmpty()){
                                                // convs.get(0) 就是想要的 conversation
                                                AVIMTextMessage msg = new AVIMTextMessage();
                                                msg.setText("kp，起床了！");
                                                // 发送消息
                                                convs.get(0).sendMessage(msg, new AVIMConversationCallback() {
                                                    @Override
                                                    public void done(AVIMException e) {
                                                        if (e == null) {
                                                            Log.d("20182005050", "发送成功！");
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
            }
        });
    }



    public void initInputLine(){
        //全部取消，回到最初的状态
        GlobalUtil.hideKeyboard(MsgDetailActivity.this);
        toolsLayout.setVisibility(View.GONE);
        moreToolsbtn.setVisibility(View.VISIBLE);
        sendTextBtn.setVisibility(View.GONE);
        msgSendText.clearFocus();
    }

    //初始化 详细的聊天信息
    private void initMsgRows() {
        for(int i=0;i<9;i++){
            MsgRowItem msgItem=new MsgRowItem(isLeft);
            isLeft = !isLeft;
            msgRowItems.add(msgItem);
        }
    }
}