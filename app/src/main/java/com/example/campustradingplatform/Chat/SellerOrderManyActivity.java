package com.example.campustradingplatform.Chat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.Chat.tab.OrderFinishedFragment;
import com.example.campustradingplatform.Chat.tab.OrderWaitDealFragment;
import com.example.campustradingplatform.Chat.tab.OrderWaitFinishFragment;
import com.example.campustradingplatform.Chat.tab.OrderWaitRefundFragment;
import com.example.campustradingplatform.Chat.tab.TabFragmentPagerAdapter;
import com.example.campustradingplatform.Login.User;
import com.example.campustradingplatform.R;

import java.util.ArrayList;

public class SellerOrderManyActivity extends AppCompatActivity {
    private TextView orderWaitDeal;
    private TextView orderWaitFinish;
    private TextView orderWaitRefund;
    private TextView orderFinished;
    private TextView topTitle;
    private ViewPager myViewPager;
    private ArrayList<Fragment> list;
    private TabFragmentPagerAdapter adapter;
    private ImageButton returnBtn;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_many);

        ChatItem chatItem = (ChatItem)getIntent().getSerializableExtra("chatItem");
        user = chatItem.getUser();


        initView();
        initListener();

    }
    public User getUser() {
        return user;
    }

    public void initListener(){
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerOrderManyActivity.this.finish();
            }
        });
    }



    private void initView()
    {
        orderWaitDeal=(TextView)  findViewById(R.id.order_wait_deal_frag ) ;
        orderWaitFinish =(TextView)  findViewById(R.id.order_wait_finish_frag ) ;
        orderWaitRefund =(TextView)  findViewById(R.id.order_wait_refund_frag  ) ;
        orderFinished =(TextView)  findViewById(R.id.order_finished_frag  ) ;

//        topTitle =(TextView)  findViewById(R.id.textView_topTitle ) ;

        returnBtn = (ImageButton)findViewById(R.id.buyer_order_return);


        myViewPager = (ViewPager) findViewById(R.id.myViewPager);
        //绑定点击事件
        myViewPager.setOnPageChangeListener(new MyPagerChangeListener()) ;
        //把Fragment添加到List集合里面
        list = new ArrayList<>();
        list.add(new OrderWaitDealFragment() );
        list.add(new OrderWaitFinishFragment());
        list.add(new OrderWaitRefundFragment());
        list.add(new OrderFinishedFragment());

        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list);
        myViewPager.setAdapter(adapter);
        myViewPager.setCurrentItem(0);  //初始化显示第一个页面
        orderWaitDeal.setTextColor(Color.GREEN );

    }



    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            //拖拽页面时候调用
            switch (arg0) {
                case 0:
                    orderWaitDeal.setTextColor(Color.GREEN  );
                    orderWaitFinish.setTextColor(Color.BLACK );
                    orderWaitRefund.setTextColor(Color.BLACK );
                    orderFinished.setTextColor(Color.BLACK );
//                    topTitle .setText("等待同意") ;

                    break;
                case 1:
                    orderWaitDeal.setTextColor(Color.BLACK  );
                    orderWaitFinish.setTextColor(Color.GREEN );
                    orderWaitRefund.setTextColor(Color.BLACK );
                    orderFinished.setTextColor(Color.BLACK );
//                    topTitle .setText("等待完成") ;

                    break;
                case 2:
                    orderWaitDeal.setTextColor(Color.BLACK  );
                    orderWaitFinish.setTextColor(Color.BLACK  );
                    orderWaitRefund.setTextColor(Color.GREEN);
                    orderFinished.setTextColor(Color.BLACK );
//                    topTitle .setText("等待退款") ;
                    break;
                case 3:
                    orderWaitDeal.setTextColor(Color.BLACK  );
                    orderWaitFinish.setTextColor(Color.BLACK  );
                    orderWaitRefund.setTextColor(Color.BLACK);
                    orderFinished.setTextColor(Color.GREEN );
//                    topTitle .setText("已完成") ;
                    break;
            }

        }

    }
}