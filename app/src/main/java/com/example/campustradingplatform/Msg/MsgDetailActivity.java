package com.example.campustradingplatform.Msg;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campustradingplatform.R;
import com.example.campustradingplatform.UtilTools.GlobalUtil;

import java.util.ArrayList;


//先选定地址 -- > 确定购买

public class MsgDetailActivity extends AppCompatActivity {

    boolean isLeft = false;

    EditText msgSendText;
    ListView chatScrollListView;
    MsgRowAdapter msgAdapter;
    ImageButton moreToolsbtn;
    LinearLayout toolsLayout;
    Button sendTextBtn;
    ArrayList<MsgRowItem> msgRowItems = new ArrayList<>();

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_detail);


        DragFloatActionButton mBtn = findViewById(R.id.img_btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MsgDetailActivity.this,"选择地址",Toast.LENGTH_SHORT).show();
            }
        });

        //聊天消息窗口
        initMsgRows();
        chatScrollListView=(ListView)findViewById(R.id.chat_scroll_list_view);
        msgAdapter = new MsgRowAdapter(MsgDetailActivity.this, R.layout.msg_row_item, msgRowItems);
        chatScrollListView.setAdapter(msgAdapter);
        //去除item边框
        chatScrollListView.setDivider(null);
        //定位最后一行
        chatScrollListView.setSelection(msgRowItems.size());//将ListView定位到最后一行





        //tools按钮点击,显示toolsLayout
        moreToolsbtn = (ImageButton) findViewById(R.id.chat_more_tools_btn);
        toolsLayout = (LinearLayout)findViewById(R.id.chat_more_tools_layout);
        moreToolsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toolsLayout.getVisibility()==View.GONE){
                    GlobalUtil.hideKeyboard(MsgDetailActivity.this);
                    msgSendText.clearFocus(); //失去焦点
                    toolsLayout.setVisibility(View.VISIBLE);
                }else{
                    //设置edittext获取焦点
                    msgSendText.requestFocus();
                    //调用系统输入法
                    InputMethodManager inputManager = (InputMethodManager) msgSendText
                            .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(msgSendText, 0);
                    toolsLayout.setVisibility(View.GONE);
                }
            }

        });

        msgSendText = (EditText)findViewById(R.id.chat_edit_text);
        msgAdapter.setSendText(msgSendText);

        sendTextBtn = (Button)findViewById(R.id.send_text_btn);

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
                    moreToolsbtn.setVisibility(View.VISIBLE);
                    sendTextBtn.setVisibility(View.GONE);
                    chatScrollListView.smoothScrollToPosition(msgRowItems.size());
                }
//                Toast.makeText(MsgDetailActivity.this,"点击编辑",Toast.LENGTH_SHORT).show();
            }
        });



        //设置moreToolsBtn 和sendTextBtn的显示
        msgSendText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0){
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
                    MsgRowItem msgRowItem = new MsgRowItem(false);
                    msgRowItem.setMsgRowCon(conText);
                    msgRowItems.add(msgRowItem);

                    msgAdapter.notifyDataSetChanged();//有新消息时，刷新ListView中的显示
                    chatScrollListView.smoothScrollToPosition(msgRowItems.size());//将ListView定位到最后一行

                    msgSendText.setText("");//清空输入框的内容
                }


                sendTextBtn.setVisibility(View.GONE);
                moreToolsbtn.setVisibility(View.VISIBLE);
            }
        });
    }
    //初始化 详细的聊天信息
    private void initMsgRows() {
        for(int i=0;i<9;i++){
            MsgRowItem msgItem=new MsgRowItem(isLeft);
            isLeft = !isLeft;
            msgRowItems.add(msgItem);
        }
    }




    //        handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                // TODO Auto-generated method stub
//                super.handleMessage(msg);
////                Toast.makeText(MsgDetailActivity.this, "Progress is OK", Toast.LENGTH_SHORT).show();
//                chatScrollListView.smoothScrollToPosition(msgRowItems.size());
//            }
//        };
//
//
//        new Thread() {
//            @Override
//            public void run() {
//                //这里写入子线程需要做的工作
//                while(true){
//                    Message message = handler.obtainMessage();
//                    message.what = 6666;
//                    handler.sendMessage(message);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        }.start();
}