package com.example.campustradingplatform;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.example.campustradingplatform.Deatil.GoodList;
import com.example.campustradingplatform.Deatil.GoodsDetail;
import com.example.campustradingplatform.Home.BaseAdapter;
import com.example.campustradingplatform.Home.BaseViewHolder;
import com.example.campustradingplatform.Home.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    Button search_button;

    String[] ITEM_NAMES = {"天猫", "聚划算", "天猫国际", "外卖"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = view.findViewById(R.id.my_recycle_view);
        search_button = view.findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), GoodList.class);
                startActivity(intent);
            }
        });
        initView();

        return view;
    }

    private void initView() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getActivity());
        recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        recyclerView.setAdapter(delegateAdapter);
        /**
         * 轮播Adapter
         */
        final ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add("https://gw.alicdn.com/imgextra/i3/20/O1CN01UTTfyr1C1CSm4hzOv_!!20" +
                "-0" +
                "-lubanu.jpg");
        BaseAdapter bannerAdapter = new BaseAdapter(new LinearLayoutHelper(),
                R.layout.vlayout_banner, arrayList) {
            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
                arrayList.add("https://gw.alicdn.com/imgextra/i3/106/O1CN01etvHvW1CeaWUFfTu9_" +
                        "!!106-0" +
                        "-lubanu.jpg");
                arrayList.add("https://gw.alicdn.com/imgextra/i3/20/O1CN01UTTfyr1C1CSm4hzOv_!!20" +
                        "-0" +
                        "-lubanu.jpg");
                arrayList.add("https://gw.alicdn.com/imgextra/i2/14/O1CN019L7VgZ1ByS5PJJpnO_!!14" +
                        "-0" +
                        "-lubanu.jpg");
                arrayList.add("https://img.alicdn.com/tps/i4/TB1YMbpeoKF3KVjSZFESutExFXa.jpg");
                // 绑定数据
                Banner mBanner = baseViewHolder.getView(R.id.banner);
                //设置banner样式
                mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                //设置图片集合
                mBanner.setImages(arrayList);
                //设置banner动画效果
                mBanner.setBannerAnimation(Transformer.Default);
                //设置标题集合（当banner样式有显示title时）
                //        mBanner.setBannerTitles(titles);
                //设置自动轮播，默认为true
                mBanner.isAutoPlay(true);
                mBanner.setImageLoader(new GlideImageLoader());
                //设置轮播时间
                mBanner.setDelayTime(3000);
                //设置指示器位置（当banner模式中有指示器时）
                mBanner.setIndicatorGravity(BannerConfig.CENTER);
                //banner设置方法全部调用完毕时最后调用
                mBanner.start();

                mBanner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Toast.makeText(getContext(), "banner点击了" + position,
                                Toast.LENGTH_SHORT).show();
                    }
                });
                super.onBindViewHolder(baseViewHolder, i);
            }
        };
        /**
         *Success--Classify
         */
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setPadding(30, 20, 30, 0);
        gridLayoutHelper.setVGap(20);//垂直间距
        gridLayoutHelper.setHGap(20);//水平间距
        BaseAdapter menuAdapter = new BaseAdapter(gridLayoutHelper, R.layout.classify_item, Arrays.asList(ITEM_NAMES)){
            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.setText(R.id.classify_text, ITEM_NAMES[position%4]);
                holder.setImageResource(R.id.classify_image, R.mipmap.ic_launcher);
                holder.getView(R.id.classify_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };


        /**
         *Goods-list
         */
        final List<String> gridlist = new ArrayList();
        for (int i=0;i<10;i++){
            gridlist.add("商品"+i);
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
                String imagename = "goods"+position%5;
                holder.setImageResource(R.id.goods_image,
                        getResources().getIdentifier(imagename, "drawable", requireActivity().getPackageName()));
                holder.getView(R.id.goods_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getContext(), "点击", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getActivity(), GoodsDetail.class);
                        startActivity(intent);
                    }
                });
            }
        };

        delegateAdapter.addAdapter(bannerAdapter);
        delegateAdapter.addAdapter(menuAdapter);
        delegateAdapter.addAdapter(gridAdapter);
    }
}



