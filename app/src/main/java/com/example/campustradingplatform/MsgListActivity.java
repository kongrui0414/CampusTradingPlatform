package com.example.campustradingplatform;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.campustradingplatform.Msg.MsgAdapter;
import com.example.campustradingplatform.Msg.MsgItem;

import java.util.ArrayList;
import java.util.List;

public class MsgListActivity extends AppCompatActivity {

    private String[] mStrs = {"aaa", "bbb", "ccc", "airsaid"};
    private SearchView mSearchView;
    private ListView mListView;
    private MsgAdapter msgAdapter;
    List<MsgItem> msgItems=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_list);

        //搜索框
        mSearchView = (SearchView) findViewById(R.id.searchView);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrs));
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
        initMsgs();
        ListView msgsView=(ListView)findViewById(R.id.msgs_view);
        msgAdapter=new MsgAdapter(MsgListActivity.this,R.layout.msg_item,msgItems);
        msgsView.setAdapter(msgAdapter);
    }

    private void initMsgs() {
        for(int i=0;i<9;i++){
            MsgItem msgItem=new MsgItem();
            msgItem.setUserName(i+"号人机");
            msgItem.setMsgDetail(i+"你好呀");
            msgItems.add(msgItem);
        }
    }
}
