package com.example.campustradingplatform;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campustradingplatform.Msg.MsgAdapter;
import com.example.campustradingplatform.Msg.MsgDetailActivity;
import com.example.campustradingplatform.Msg.MsgItem;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.im.v2.AVIMClient;
import cn.leancloud.im.v2.AVIMConversation;
import cn.leancloud.im.v2.AVIMConversationsQuery;
import cn.leancloud.im.v2.AVIMException;
import cn.leancloud.im.v2.AVIMMessage;
import cn.leancloud.im.v2.callback.AVIMClientCallback;
import cn.leancloud.im.v2.callback.AVIMConversationQueryCallback;
import cn.leancloud.im.v2.callback.AVIMMessagesQueryCallback;
import cn.leancloud.im.v2.messages.AVIMTextMessage;

/**
 * Created by 武当山道士 on 2017/8/16.
 */

public class MsgListFragment extends Fragment {

    private String[] mStrs = {"aaa", "bbb", "ccc", "airsaid"};
    private SearchView mSearchView;
    private ListView mListView;
    private MsgAdapter msgAdapter;
    List<MsgItem> msgItems=new ArrayList<>();
    View view;

    ArrayList<String> userNames = new ArrayList<>();
    private AVIMClient tom;

    MyThread myThread = new MyThread();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.msglist_fragment, container, false);
        this.view = view;
        TomSend();
        init();

        myThread.start();

        return view;

    }


    class MyThread extends Thread{

        List<AVIMConversation> mConvs;

        @Override
        public void run() {
            super.run();
            try {
                while (true){
                    Thread.sleep(1000);
                    if(mConvs!=null && msgItems.size()>0){
                        for(int i=0;i<mConvs.size();i++){
//                            Log.d("20182005050", "convs with: "+mConvs.get(i).getMembers());
                            MsgItem mMsgItem = msgItems.get(i);
                            mConvs.get(i).queryMessages(1, new AVIMMessagesQueryCallback() {
                                @Override
                                public void done(List<AVIMMessage> messages, AVIMException e) {
                                    if (e == null) {
                                        // 成功获取最新 10 条消息记录
                                        String msgStr = "";
                                        if(messages.size()==0){
                                            msgStr = "暂时没有聊天记录";
                                        }
                                        else{
                                            msgStr = ((AVIMTextMessage)messages.get(0)).getText();
                                        }

                                        if(!msgStr.equals(mMsgItem.getMsgDetail())){
                                            Bundle bundle =  new Bundle();
                                            bundle.putSerializable("msgItem",mMsgItem);
                                            bundle.putString("msgStr",msgStr);

                                            Message message = new Message();
                                            message.what = 2;
                                            message.setData(bundle);
                                            handler.sendMessage(message);// 发送消息
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void setmConvs(List<AVIMConversation> mConvs) {
            this.mConvs = mConvs;
        }
    }

    //修改listView
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    Bundle bundle  = msg.getData();
//                    ArrayList<String> userNames = bundle.getStringArrayList("userNames");
//                    ArrayList<String> lasthistorys = bundle.getStringArrayList("lasthistorys");


                    MsgItem msgItem = (MsgItem) bundle.getSerializable("msgItem");

                    msgItems.add(msgItem);

                    msgAdapter.notifyDataSetChanged();
                    Log.d("20182005050", "handleMessage: handler修改UI成功");
                    break;
                case 2:
                    Bundle bundle2  = msg.getData();
                    MsgItem msgItem2 = (MsgItem) bundle2.getSerializable("msgItem");
                    String msgStr = (String)bundle2.getString("msgStr");
                    msgItem2.setMsgDetail(msgStr);
                    msgAdapter.notifyDataSetChanged();
                default:

                    break;
            }
        }
    };



    public void TomSend(){
        // Tom 创建了一个 client，用自己的名字作为 clientId 登录
        // clientId 为 Tom
        tom = AVIMClient.getInstance("kp");

        // Tom 登录
        tom.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    // 成功打开连接
                    Log.d("20182005050", "done: tom连接成功");

                    //查询会话列表-----------------------------------------------------
                    AVIMConversationsQuery query = client.getConversationsQuery();
                    // 按对话的创建时间降序
                    query.orderByDescending("createdAt");

//                    query.whereContainsIn("m", Arrays.asList("Tom"));
                    // 执行查询
                    query.findInBackground(new AVIMConversationQueryCallback(){
                        @Override
                        public void done(List<AVIMConversation> convs,AVIMException e){
                            if(e == null){
                                // convs 就是想要的结果
                                if(convs != null && !convs.isEmpty()) {
                                    // 获取符合查询条件的 conversation 列表
                                    myThread.setmConvs(convs);

                                    Log.d("20182005050", "会话列表长度:"+convs.size());

                                    for(int i=0;i<convs.size();i++){
                                        Log.d("20182005050", "convs with: "+convs.get(i).getMembers());
                                        addMsgRowItemLastHistory(convs.get(i));
                                    }
                                }
                            }
                        }
                    });

                }
            }
        });
    }

    public void init(){

        //搜索框
        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, mStrs));
        mListView.setTextFilterEnabled(true);

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mListView.setFilterText(newText);
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });

        //消息列表
//        TomSend();
//        initMsgs();

        ListView msgsView=(ListView)view.findViewById(R.id.msgs_view);
        msgAdapter=new MsgAdapter(view.getContext(),R.layout.msg_item,msgItems);
        msgsView.setAdapter(msgAdapter);



        msgsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),"第"+position+"个item", Toast.LENGTH_SHORT).show();
                tom.close(null);
                MsgAdapter adpter= (MsgAdapter) parent.getAdapter();
                MsgItem msgItem = (MsgItem) adpter.getItem(position);

                Intent intent = new Intent(getActivity(), MsgDetailActivity.class);

                intent.putExtra("msgItem",msgItem);


                getActivity().startActivity(intent);
            }
        });
    }

    private void   addMsgRowItemLastHistory(AVIMConversation conversation){

        // limit 取值范围 1~100，如调用 queryMessages 时不带 limit 参数，默认获取 20 条消息记录
        Log.d("20182005050", "convId:"+conversation.getConversationId());

        String convId = conversation.getConversationId();
        conversation.queryMessages(1, new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> messages, AVIMException e) {
                if (e == null) {
                    // 成功获取最新 10 条消息记录
                    Log.d("20182005050", "lastHistory:"+((AVIMTextMessage)messages.get(0)).getText());

                    Message message = new Message();
                    message.what = 1;
                    Bundle bundle = new Bundle();

                    MsgItem msgItem=new MsgItem();

                    if(messages.size()==0){
                         msgItem.setMsgDetail("暂时没有聊天记录");
                    }
                    else{
                         msgItem.setMsgDetail(((AVIMTextMessage)messages.get(0)).getText());
                    }

                    msgItem.setUserName(conversation.getMembers().get(0));

                    msgItem.setConvId(convId);


                    bundle.putSerializable("msgItem",msgItem);
                    message.setData(bundle);
                    handler.sendMessage(message);// 发送消息
                }
            }
        });



    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        tom.close(null);
    }
}

