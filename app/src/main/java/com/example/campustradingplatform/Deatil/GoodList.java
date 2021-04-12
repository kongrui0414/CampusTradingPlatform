package com.example.campustradingplatform.Deatil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.example.campustradingplatform.Home.BaseAdapter;
import com.example.campustradingplatform.Home.BaseViewHolder;
import com.example.campustradingplatform.R;

import java.util.ArrayList;
import java.util.List;

public class GoodList extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView back_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_list);
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

        /**
         *Goods-list
         */
        final List<String> gridlist = new ArrayList();
        for (int i=0;i<10;i++){
            gridlist.add("初音未来Fufu玩偶");
        }
        GridLayoutHelper gridLayoutHelper_list = new GridLayoutHelper(2);
        gridLayoutHelper_list.setPadding(30, 20, 30, 0);
        gridLayoutHelper_list.setVGap(20);//垂直间距
        gridLayoutHelper_list.setHGap(20);//水平间距
        BaseAdapter gridAdapter = new BaseAdapter(gridLayoutHelper_list, R.layout.goods_item,gridlist){
            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.setText(R.id.goods_describe, gridlist.get(position));
                holder.setText(R.id.goods_describe, gridlist.get(position));

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

//        delegateAdapter.addAdapter(bannerAdapter);

        back_image = findViewById(R.id.back);
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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