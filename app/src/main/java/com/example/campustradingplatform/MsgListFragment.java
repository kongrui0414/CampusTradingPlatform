package com.example.campustradingplatform;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.msglist_fragment, container, false);
        this.view = view;
        init();
        return view;
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
        initMsgs();
        ListView msgsView=(ListView)view.findViewById(R.id.msgs_view);
        msgAdapter=new MsgAdapter(view.getContext(),R.layout.msg_item,msgItems);
        msgsView.setAdapter(msgAdapter);



        msgsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),"第"+position+"个item", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(getActivity(), MsgDetailActivity.class);
                getActivity().startActivity(intent);
            }
        });
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

