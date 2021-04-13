package com.example.campustradingplatform.Deatil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.Goods.Goods;
import com.example.campustradingplatform.Goods.GoodsService;
import com.example.campustradingplatform.Goods.GoodsThread;
import com.example.campustradingplatform.Home.BaseAdapter;
import com.example.campustradingplatform.Home.BaseViewHolder;
import com.example.campustradingplatform.Login.User;
import com.example.campustradingplatform.R;
import com.example.campustradingplatform.UtilTools.TimeUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GoodList extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView back_image;
    String searchWords ="";
    int timeOrder = 1;
    int priceOrder = 1;
    private CheckBox priceOrderBtn;
    private List<Goods> gridlist;
    private BaseAdapter gridAdapter;
    private CheckBox timeOrderBtn;
    User user;
    private CheckBox sortBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_list);
        searchWords = (String) getIntent().getStringExtra("search_words");
        user = (User) getIntent().getSerializableExtra("user");
        init();
    }

    private void init(){
        recyclerView = findViewById(R.id.my_recycle_view);
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(this);
        recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        recyclerView.setAdapter(delegateAdapter);

        priceOrderBtn = (CheckBox)findViewById(R.id.cb_price);
        priceOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceOrder = -priceOrder;
                Collections.sort(gridlist,new Comparator<Goods>() {
                    @Override
                    public int compare(Goods o1, Goods o2) {
                        if(o1.getPresentPrice()==o2.getPresentPrice()) {
                            if(TimeUtil.compare(o1.getLaunchTime(),o2.getLaunchTime())){
                                return timeOrder;
                            }else
                                return -timeOrder;
                        }
                        else if(o1.getPresentPrice()>o2.getPresentPrice()){
                            return priceOrder;
                        }else{
                            return -priceOrder;
                        }
                    }
                });

                flashAdapter();
            }
        });

        timeOrderBtn  = (CheckBox)findViewById(R.id.cb_time);
        timeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeOrder = -timeOrder;
                Collections.sort(gridlist,new Comparator<Goods>() {

                    @Override
                    public int compare(Goods o1, Goods o2) {
                        if(o1.getLaunchTime().equals(o2.getLaunchTime())) {
                            if(o1.getPresentPrice()>=o2.getPresentPrice()){
                                return priceOrder;
                            }else
                                return -priceOrder;
                        }
                        else if(TimeUtil.compare(o1.getLaunchTime(),o2.getLaunchTime())){
                            return -timeOrder;
                        }else{
                            return timeOrder;
                        }
                    }
                });
                flashAdapter();
            }
        });

        sortBackBtn = (CheckBox)findViewById(R.id.cb_sort_back);
        sortBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timeOrder==1 && priceOrder ==1) return;
                timeOrder = 1;
                priceOrder = 1;
                Collections.sort(gridlist,new Comparator<Goods>() {

                    @Override
                    public int compare(Goods o1, Goods o2) {
                        if(o1.getLaunchTime().equals(o2.getLaunchTime())) {
                            if(o1.getPresentPrice()>=o2.getPresentPrice()){
                                return priceOrder;
                            }else
                                return -priceOrder;
                        }
                        else if(TimeUtil.compare(o1.getLaunchTime(),o2.getLaunchTime())){
                            return -timeOrder;
                        }else{
                            return timeOrder;
                        }
                    }
                });
                flashAdapter();
            }
        });
        /**
         *Goods-list
         */

        GoodsThread thread = GoodsService.getGoodsListByKeyWord(searchWords);
        while(!thread.isFinished());
        gridlist = thread.getGoodsList();



        GridLayoutHelper gridLayoutHelper_list = new GridLayoutHelper(2);
        gridLayoutHelper_list.setPadding(30, 20, 30, 0);
        gridLayoutHelper_list.setVGap(20);//垂直间距
        gridLayoutHelper_list.setHGap(20);//水平间距
        gridAdapter = new BaseAdapter(gridLayoutHelper_list, R.layout.goods_item,gridlist){
            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);

                Goods goods = gridlist.get(position);
                holder.setText(R.id.goods_describe, goods.getGoodsName());
                holder.setText(R.id.goods_price, String.valueOf(goods.getPresentPrice()));

                String imagename = "goods0";
                holder.setImageResource(R.id.goods_image,
                        getResourceId("mipmap", imagename));
                holder.getView(R.id.goods_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getContext(), "点击", Toast.LENGTH_SHORT).show();
                        User buyer = new User(user.getId(),true);
                        User seller = new User(goods.getSellerId(),false);
                        ChatItem chatItem = new ChatItem(user,user,seller,goods);
//                        Toast.makeText(getContext(), "点击", Toast.LENGTH_SHORT).show();
                        //先查询 --是否有本号chatItem ,有直接跳转
                        //没有，根据当前user,seller,goodsid 创建 chatItem
                        Intent intent =new Intent(GoodList.this, GoodsDetail.class);
                        intent.putExtra("chatItem",chatItem);
                        startActivity(intent);
                    }
                });
            }
        };
        delegateAdapter.addAdapter(gridAdapter);

//        delegateAdapter.addAdapter(bannerAdapter);

        back_image = findViewById(R.id.back);
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void flashAdapter() {
        recyclerView = findViewById(R.id.my_recycle_view);
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(GoodList.this);
        recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        recyclerView.setAdapter(delegateAdapter);

        GridLayoutHelper gridLayoutHelper_list = new GridLayoutHelper(2);
        gridLayoutHelper_list.setPadding(30, 20, 30, 0);
        gridLayoutHelper_list.setVGap(20);//垂直间距
        gridLayoutHelper_list.setHGap(20);//水平间距
        gridAdapter = new BaseAdapter(gridLayoutHelper_list, R.layout.goods_item,gridlist){
            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);

                Goods goods = gridlist.get(position);
                holder.setText(R.id.goods_describe, goods.getGoodsName());
                holder.setText(R.id.goods_price, String.valueOf(goods.getPresentPrice()));

                String imagename = "goods0";
                holder.setImageResource(R.id.goods_image,
                        getResourceId("mipmap", imagename));
                holder.getView(R.id.goods_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getContext(), "点击", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(GoodList.this,GoodsDetail.class);
                        startActivity(intent);
                    }
                });
            }
        };
        delegateAdapter.addAdapter(gridAdapter);
    }

    /**
     * 根据图片名称获取图片的资源id的方法
     * @param imageName
     * @return
     */
    private int  getResourceId(String position,String imageName){
        int resId = getResources().getIdentifier(imageName, position , this.getPackageName());
        return resId;
    }
}