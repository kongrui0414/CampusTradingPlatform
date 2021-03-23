package com.example.campustradingplatform;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.campustradingplatform.Login.User;
import com.example.campustradingplatform.Msg.MsgPanelObj;
import com.example.campustradingplatform.Msg.MsgRowItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.Arrays;
import java.util.List;

import cn.leancloud.im.v2.AVIMClient;
import cn.leancloud.im.v2.AVIMConversation;
import cn.leancloud.im.v2.AVIMConversationsQuery;
import cn.leancloud.im.v2.AVIMException;
import cn.leancloud.im.v2.AVIMMessage;
import cn.leancloud.im.v2.callback.AVIMClientCallback;
import cn.leancloud.im.v2.callback.AVIMConversationCallback;
import cn.leancloud.im.v2.callback.AVIMConversationCreatedCallback;
import cn.leancloud.im.v2.callback.AVIMConversationQueryCallback;
import cn.leancloud.im.v2.callback.AVIMMessagesQueryCallback;
import cn.leancloud.im.v2.messages.AVIMTextMessage;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigation;
    //默认选择第一个fragment
    private int lastSelectedPosition = 0;

    private HomeFragment firstFragment;
    private DivideFragment secondFragment;
    private PublicFragment thirdFragment;
    private MsgListFragment fourthFragment;
    private MyCenterFragment fifthFragment;

    private Fragment[] fragments;

    private User user;          //用户类，用于储存登录的用户信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (User)getIntent().getSerializableExtra("user2"); //接收成功登录后用户储存用户信息的用户类对象。可用用户类的方法获取用户的各种信息。

        navigation = this.findViewById(R.id.navigation);
        initFragments();

//        TomSend();

        SQLiteDatabase db = Connector.getDatabase();
        Log.d("20182005050", "onCreate: 创建数据库成功");

        MsgRowItem msgRowItem1 = new MsgRowItem();
        msgRowItem1.setMsgRowCon("因为爱情");
        msgRowItem1.save();

        MsgRowItem msgRowItem2 = new MsgRowItem();
        msgRowItem2.setMsgRowCon("因为爱情");
        msgRowItem2.save();

        MsgPanelObj msgPanelObj = new MsgPanelObj();
        msgPanelObj.getMsgRowItemList().add(msgRowItem1);
        msgPanelObj.getMsgRowItemList().add(msgRowItem2);

        msgPanelObj.save();

        MsgPanelObj msgPanelObj1 = DataSupport.findLast(MsgPanelObj.class,true);

        MsgRowItem msgRowItem3 = DataSupport.findLast(MsgRowItem.class,true);

        Log.e("20182005050", msgPanelObj1.toString());
        Log.e("20182005050", msgRowItem3.toString());

//        List<MsgRowItem> itemList = new ArrayList<>();
//
//        for(int i=0;i<5;i++){
//            MsgPanelObj panelObj = new MsgPanelObj();
//            for(int j=0;j<5;j++){
//                MsgRowItem item = new MsgRowItem();
//                item.setMsgRowCon("content:"+i);
//                item.save();
//                itemList.add(item);
//            }
//            panelObj.setMsgRowItemList(itemList);
//            DataSupport.saveAll(itemList);
//            panelObj.save();
//        }
//
//        List<MsgPanelObj> lists = DataSupport.findAll(MsgPanelObj.class);
//        for(int i=0;i<lists.size();i++){
//            Log.d("20182005050", "List<PanelObj>"+i);
//            MsgPanelObj panelObjs  = (MsgPanelObj)lists.get(i);
//            List<MsgRowItem> chatItems = panelObjs.getMsgRowItemList();
//            for(int j=0;j<chatItems.size();j++){
//                Log.d("20182005050", "chatItems"+i+":"+chatItems.get(i).getMsgRowCon());
//            }
//        }

    }

    private void initFragments() {
        //监听切换事件
        navigation.setOnNavigationItemSelectedListener(this);
        //平均布局
//        setItemType(navigation);
        //添加角标消息数
//        setAddNumber();
        firstFragment = new HomeFragment();
        secondFragment = new DivideFragment();
        thirdFragment = new PublicFragment();
        fourthFragment = new MsgListFragment();
        fifthFragment = new MyCenterFragment();
        fifthFragment.getUser(user);        //将用户传给MyCenterFragment
        fragments = new Fragment[]{firstFragment, secondFragment, thirdFragment,fourthFragment,fifthFragment};
        lastSelectedPosition = 0;
//        //默认提交第一个
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.tb, firstFragment)//添加
                .show(firstFragment)//展示
                .commit();//提交
    }

//    private void setAddNumber() {
//        //获取整个的NavigationView
//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
//        //获取所添加的每一个Tab，并给第三个Tab添加消息角标
//        View tabView = menuView.getChildAt(2 );
//        BottomNavigationItemView itemView = (BottomNavigationItemView) tabView;
//        //加载我们的角标布局View，新创建的一个布局
//        View badgeView = LayoutInflater.from(this).inflate(R.layout.number_badge, menuView, false);
//        TextView number=badgeView.findViewById(R.id.msg_number);
//        //设置显示的内容
//        number.setText("99");
//        //添加到Tab上
//        itemView.addView(badgeView);
//    }

    /**
     * 切换Fragment
     * @param lastIndex 上个显示Fragment的索引
     * @param index     需要显示的Fragment的索引
     */
    public void setDefaultFragment(int lastIndex, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastIndex]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.tb, fragments[index]);
        }
//        //需要展示fragment下标的位置
//        //commit：安排该事务的提交。这一承诺不会立即发生;它将被安排在主线程上，以便在线程准备好的时候完成。
//        //commitAllowingStateLoss：与 commit类似，但允许在活动状态保存后执行提交。这是危险的，因为如果Activity需要从其状态恢复，
//        // 那么提交就会丢失，因此，只有在用户可以意外地更改UI状态的情况下，才可以使用该提交
        transaction.show(fragments[index]).commit();
    }

    /**
     * 切换事件
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_homefirst:
                if (0 != lastSelectedPosition) {
                    setDefaultFragment(lastSelectedPosition, 0);
                    lastSelectedPosition = 0;
                }
                return true;
            case R.id.navigation_homesecond:
                if (1 != lastSelectedPosition) {
                    setDefaultFragment(lastSelectedPosition, 1);
                    lastSelectedPosition = 1;
                }
                return true;
            case R.id.navigation_homethird:
                if (2 != lastSelectedPosition) {
                    setDefaultFragment(lastSelectedPosition, 2);
                    lastSelectedPosition = 2;
                }
                return true;
            case R.id.navigation_homefourth:
                if (3 != lastSelectedPosition) {
                    setDefaultFragment(lastSelectedPosition, 3);
                    lastSelectedPosition = 3;
                }
                return true;
            case R.id.navigation_homefifth:
                if (4 != lastSelectedPosition) {
                    setDefaultFragment(lastSelectedPosition, 4);
                    lastSelectedPosition = 4;
                }
                return true;
        }
        return false;
    }

    /**
     * 防止超过3个fragment布局不平分
     */
//    @SuppressLint("RestrictedApi")
//    private  void setItemType(BottomNavigationView view) {
//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
//        try {
//            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
//            shiftingMode.setAccessible(true);
//            shiftingMode.setBoolean(menuView, false);
//            shiftingMode.setAccessible(false);
//            for (int i = 0; i < menuView.getChildCount(); i++) {
//                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
//                //noinspection RestrictedApi
////                item.setShiftingMode(false);
//                // set once again checked value, so view will be updated
//                //noinspection RestrictedApi
//                item.setChecked(item.getItemData().isChecked());
//            }
//        } catch (NoSuchFieldException e) {
//            Log.e("BNVHelper", "Unable to get shift mode field", e);
//        } catch (IllegalAccessException e) {
//            Log.e("BNVHelper", "Unable to change value of shift mode", e);
//        }
//    }

    public void TomSend(){
        // Tom 创建了一个 client，用自己的名字作为 clientId 登录
        // clientId 为 Tom
        AVIMClient tom = AVIMClient.getInstance("Tom");

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

                    query.whereContainsIn("m", Arrays.asList("Tom"));
                    // 执行查询
                    query.findInBackground(new AVIMConversationQueryCallback(){
                        @Override
                        public void done(List<AVIMConversation> convs,AVIMException e){
                            if(e == null){
                                // convs 就是想要的结果
                                if(convs != null && !convs.isEmpty()) {
                                    // 获取符合查询条件的 conversation 列表
                                    Log.d("20182005050", "会话列表长度:"+convs.size());
                                    for(int i=0;i<convs.size();i++){
                                        Log.d("20182005050", "convs with: "+convs.get(i).getMembers());
                                        getMsgsByConLimit(convs.get(i),10);
                                    }
                                }
                            }
                        }
                    });

                }
            }
        });


//


        //创建对话 AVIMConversation,Tom已经登录
        tom.createConversation(Arrays.asList("Jerry"), "Tom & Jerry", null, false, true,
                new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation conversation, AVIMException e) {
                        if(e == null) {
                            // 创建成功，设置要发送的消息
                            getMsgsByConLimit(conversation,10);
                            sendMyMsg(conversation);
                        }
                    }
                });

        //创建对话 AVIMConversation,Tom已经登录
        tom.createConversation(Arrays.asList("Jerry2"), "Tom & Jerry2", null, false, true,
                new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation conversation, AVIMException e) {
                        if(e == null) {
                            // 创建成功，设置要发送的消息
                            getMsgsByConLimit(conversation,10);
                            sendMyMsg(conversation);
                        }
                    }
                });
    }

    //发送信息
    private void sendMyMsg(AVIMConversation conversation) {
        AVIMTextMessage msg = new AVIMTextMessage();
        msg.setText("Jerry，起床了heihei44444！");
        // 发送消息
        conversation.sendMessage(msg, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    Log.d("20182005050", "发送成功！");
                    Toast.makeText(MainActivity.this,"发送消息成功",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //获取聊天记录
    private void getMsgsByConLimit(AVIMConversation conversation,int limit){
        // limit 取值范围 1~100，如调用 queryMessages 时不带 limit 参数，默认获取 20 条消息记录
        conversation.queryMessages(limit, new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> messages, AVIMException e) {
                if (e == null) {
                    // 成功获取最新 10 条消息记录
                    for(int i=0;i<messages.size();i++){
                        Log.d("20182005050", "done: "+i+":"+((AVIMTextMessage)messages.get(i)).getText());
                    }
                }
            }
        });
    }

}

