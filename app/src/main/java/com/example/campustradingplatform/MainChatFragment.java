package com.example.campustradingplatform;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.Chat.ChatDetailActivity;
import com.example.campustradingplatform.Chat.Service.ChatService;
import com.example.campustradingplatform.Chat.Service.MainChatServiceThread;
import com.example.campustradingplatform.Chat.msgDir.ChatAdapter;
import com.example.campustradingplatform.Login.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 武当山道士 on 2017/8/16.
 */

public class MainChatFragment extends Fragment {
    public static final String TAG ="TAG";

    private String[] mStrs = {"aaa", "bbb", "ccc", "airsaid"};   //------------------------后期删除，初始化(搜索框下拉栏)ListView时使用-----------------------------

    View view;

    //初始化user----------------------测试用--------表示本人：买家
//    User user=new User(1,true);
    User user;

    //初始化当前界面的 会话列表
    List<ChatItem>  chatItems = new ArrayList<>();
    private SearchView mSearchView;
    private ListView mSearchListView;
    private ListView chatListView;
    private ChatAdapter chatAdapter;
    private Button buyerChatBtn;
    private Button sellerChatBtn;

    //判断当前是买家界面还是卖家界面
    private boolean isBuyerScreen = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_chat_fragment, container, false);
        this.view = view;

        //获取 intent传来的参数
        Activity activity = getActivity();
        if(activity instanceof MainActivity)
            user = ((MainActivity)activity).getUser();

//        Log.d(TAG, "onCreateView: "+user);
//        initTestBtn();
        initChatListData();  //初始化数据
        initUI();           //初始化界面
        return view;
    }
    /**
     * description: 循环等待DB操作完成
     */
    private void initChatListData() {

        MainChatServiceThread thread  = new ChatService().GetChatListByUser(user);

        while(!thread.isFinished());
        this.chatItems = thread.getChatItems();

        if(null == this.chatItems ||this.chatItems.size()==0)
            this.chatItems = new ArrayList<>();
    }


    /**
     * description: 初始化测试的按钮
     */
//    public  void initTestBtn(){
//        Button button = (Button)view.findViewById(R.id.test_btn);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(view.getContext(), BuyerOrderManyActivity.class);
//                user.setIsBuyer(true);
//                .putExtra("chatItem",new ChatItem(user));intent
//                startActivity(intent);
//            }
//        });
//
//        Button button2 = (Button)view.findViewById(R.id.test_btn2);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(view.getContext(), SellerOrderManyActivity.class);
//                user.setIsBuyer(false);
//                intent.putExtra("chatItem",new ChatItem(user));
//                startActivity(intent);
//            }
//        });
//
//        Button button3 = (Button)view.findViewById(R.id.test_btn3);
//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //测试的商品id 如果大于 数据库的id  ----会报错
//                User buyer = new User(1,true);
//                User seller = new User(2,false);
////                Log.d("TAG", "onClick: ");
//                ChatService.addChatItemByBidAndSidAndGid(new ChatItem(buyer,buyer,seller,new Goods(69)));
//            }
//        });
//    }


    public void initUI(){
        //搜索框 ---------start---------------------------------------------

        //获取搜索栏组件
        mSearchView = (SearchView)view.findViewById(R.id.searchView);
        mSearchListView = (ListView) view.findViewById(R.id.listView);


        //mChatListView.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, mStrs));
        mSearchListView.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, mStrs));
        mSearchListView.setTextFilterEnabled(true);

        // 设置搜索下拉栏文本监听
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
                    mSearchListView.setFilterText(newText);
                }else{
                    mSearchListView.clearTextFilter();
                }
                return false;
            }
        });

        //搜索框 ---------end---------------------------------------------


        //消息列表 ------------------------start -------------------------------------------
        chatListView=(ListView)view.findViewById(R.id.msgs_view);

        chatAdapter=new ChatAdapter(view.getContext(),R.layout.chat_item,chatItems);
        chatListView.setAdapter(chatAdapter);

        //跳转到详细的聊天界面
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),"第"+position+"个item", Toast.LENGTH_SHORT).show();

                ChatAdapter adpter= (ChatAdapter) parent.getAdapter();
                ChatItem chatItem = (ChatItem) adpter.getItem(position);

                Intent intent = new Intent(view.getContext(), ChatDetailActivity.class);

                intent.putExtra("chatItem",chatItem);

                startActivity(intent);
            }
        });
        //消息列表 ------------------------end -------------------------------------------


        //买卖双方切换-----------------------start-----------------------------------------
        buyerChatBtn = (Button)view.findViewById(R.id.buyer_chat_btn);
        sellerChatBtn = (Button)view.findViewById(R.id.seller_chat_btn);

        buyerChatBtn.setTextColor(Color.parseColor(	"#f4a460"));
        sellerChatBtn.setTextColor(Color.BLACK);


        buyerChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isBuyerScreen){
                    buyerChatBtn.setTextColor(Color.parseColor("#f4a460"));
                    sellerChatBtn.setTextColor(Color.BLACK);

                    isBuyerScreen = true;
                    user.setIsBuyer(true);

                    initChatListData();
                    flashAdapter();
                }
            }
        });


        sellerChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBuyerScreen){
                    buyerChatBtn.setTextColor(Color.BLACK);
                    sellerChatBtn.setTextColor(Color.parseColor("#f4a460"));

                    isBuyerScreen=false;
                    user.setIsBuyer(false);

                    initChatListData();
                    flashAdapter();
                }

            }
        });


        //买卖双方切换-----------------------end-----------------------------------------

    }

    public void flashAdapter(){
        chatAdapter=new ChatAdapter(view.getContext(),R.layout.chat_item,chatItems);
        chatListView.setAdapter(chatAdapter);
    }

}

